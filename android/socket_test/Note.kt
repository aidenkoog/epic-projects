import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import java.net.UnknownHostException
import kotlin.time.Duration.Companion.seconds

class TcpClient(
    private val host: String,
    private val port: Int,
    private val reconnectDelay: Long = 3000L // 재연결 대기 시간 (3초)
) {
    private var socket: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: BufferedWriter? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // 연결 상태 Flow
    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState.asStateFlow()

    // 수신 데이터 Flow
    private val _incomingMessages = MutableSharedFlow<String>(replay = 0)
    val incomingMessages: SharedFlow<String> = _incomingMessages.asSharedFlow()

    // 연결 관리 및 데이터 수신 실행
    fun start() {
        coroutineScope.launch {
            while (isActive) {
                try {
                    connect()
                    receiveMessages()
                } catch (e: Exception) {
                    handleException(e)
                }
                delay(reconnectDelay) // 일정 시간 후 재연결 시도
            }
        }
    }

    // TCP 서버 연결
    private suspend fun connect() {
        withContext(Dispatchers.IO) {
            try {
                socket = Socket().apply { connect(InetSocketAddress(host, port), 5000) } // 타임아웃 5초
                reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                writer = BufferedWriter(OutputStreamWriter(socket!!.getOutputStream()))
                _connectionState.value = true
                println("TCP 연결 성공: $host:$port")
            } catch (e: Exception) {
                _connectionState.value = false
                throw e
            }
        }
    }

    // 서버로부터 메시지 수신 (Flow 사용)
    private suspend fun receiveMessages() {
        withContext(Dispatchers.IO) {
            try {
                while (socket?.isConnected == true) {
                    val message = reader?.readLine() ?: break
                    _incomingMessages.emit(message) // 메시지 Flow로 전달
                }
            } catch (e: Exception) {
                throw e
            } finally {
                disconnect()
            }
        }
    }

    // 서버로 메시지 전송 (타임아웃 5초 설정)
    suspend fun sendMessage(message: String) {
        withContext(Dispatchers.IO) {
            try {
                if (socket?.isConnected == true && writer != null) {
                    withTimeoutOrNull(5.seconds) {
                        writer!!.write("$message\n")
                        writer!!.flush()
                    } ?: throw SocketTimeoutException("메시지 전송 시간 초과")
                } else {
                    throw IOException("소켓이 닫혀 있음")
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    // 연결 종료
    fun stop() {
        coroutineScope.cancel()
        disconnect()
    }

    private fun disconnect() {
        try {
            socket?.close()
            reader?.close()
            writer?.close()
        } catch (e: Exception) {
            println("소켓 종료 오류: ${e.message}")
        } finally {
            _connectionState.value = false
        }
    }

    // 예외 처리 및 로그 출력
    private fun handleException(e: Exception) {
        when (e) {
            is UnknownHostException -> println("서버 주소를 찾을 수 없음: ${e.message}")
            is SocketTimeoutException -> println("소켓 연결 시간 초과: ${e.message}")
            is SocketException -> println("소켓 오류 발생: ${e.message}")
            is IOException -> println("입출력 오류 발생: ${e.message}")
            else -> println("알 수 없는 오류 발생: ${e.message}")
        }
        disconnect()
    }
}

fun main() = runBlocking {
    val client = TcpClient("192.168.1.100", 8080)

    // TCP 연결 시작
    client.start()

    // 연결 상태 감지
    launch {
        client.connectionState.collect { isConnected ->
            println("TCP 연결 상태: $isConnected")
        }
    }

    // 서버 메시지 수신
    launch {
        client.incomingMessages.collect { message ->
            println("서버로부터 수신된 메시지: $message")
        }
    }

    // 5초 후 메시지 전송
    delay(5000)
    client.sendMessage("Hello Server!")

    // 10초 후 종료
    delay(10000)
    client.stop()
}