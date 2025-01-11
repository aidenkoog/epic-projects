package io.github.aidenkoog.socket

import android.util.Log
import io.github.aidenkoog.socket.SocketInfo.SOCKET_IP_ADDRESS
import io.github.aidenkoog.socket.SocketInfo.SOCKET_PORT
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.Charset

object SocketService {
    private const val TAG = "SocketService"
    private var socket: Socket? = null

    /* notify error messages related to socket errors. */
    private suspend fun postSocketError(e: Exception) {
        EventBus.post(
            SocketEvent.SocketFailureEvent(
                "errorMessage -> ${e.message}, localizedMessage -> ${e.localizedMessage}"
            )
        )
    }

    /* get socket's output stream. */
    private fun getSocketOutputStream(): OutputStream? = socket?.getOutputStream()

    /* send socket message through output stream path. */
    @OptIn(DelicateCoroutinesApi::class)
    fun sendSocketMessage(message: String?) {
        GlobalScope.launch {
            try {
                val writer = OutputStreamWriter(getSocketOutputStream())
                if (message.isNullOrEmpty()) {
                    Log.e(TAG, "sendMessage: message -> $message")
                    writer.write("hello, message -> $message")

                } else {
                    writer.write(message)
                }
                writer.flush()
                EventBus.post(SocketEvent.SendSocketEvent(message = message ?: "empty message"))

            } catch (e: Exception) {
                Log.e(TAG, "error -> ${e.localizedMessage}")
                postSocketError(e)
            }
        }
    }

    /* close socket. */
    @OptIn(DelicateCoroutinesApi::class)
    fun closeSocket() {
        GlobalScope.launch {
            try {
                socket?.close()
                EventBus.post(SocketEvent.CloseSocketEvent)

            } catch (e: Exception) {
                Log.e(TAG, "error -> ${e.localizedMessage}")
                postSocketError(e)
            }
        }

    }

    /* observe and receive socket message from input stream path. */
    @OptIn(DelicateCoroutinesApi::class)
    fun observeSocketMessage() {
        Log.e(TAG, "observeSocketMessage: ")
        if (socket == null) {
            Log.e(TAG, "observeSocketMessage: socket -> null")
            Log.d(TAG, "observeSocketMessage: call openSocket until connection is established")
            openSocket()
            return
        }

        GlobalScope.launch {
            try {
                val byteArray = ByteArray(1024)
                while (true) {
                    val input = withContext(Dispatchers.IO) {
                        socket!!.getInputStream()
                    }
                    val byte = withContext(Dispatchers.IO) {
                        input.read(byteArray)
                    }
                    val charSet = Charset.forName("ksc5601")
                    val message = String(byteArray, 0, byte, charSet)
                    Log.e(TAG, "observeSocketMessage: byte -> $byte, message -> $message")

                    EventBus.post(SocketEvent.ObservedSocketEvent(message = message))
                }

            } catch (e: IOException) {
                Log.e(TAG, "socket read error = ${e.localizedMessage}")
                postSocketError(e)

            } catch (e: StringIndexOutOfBoundsException) {
                Log.e(TAG, "socket read error = ${e.localizedMessage}")
                postSocketError(e)
                closeSocket()

            } catch (e: Exception) {
                Log.e(TAG, "error -> ${e.localizedMessage}")
                postSocketError(e)
                closeSocket()
            }
        }
    }

    private var outputStream: OutputStream? = null
    private var bufferedReader: BufferedReader? = null

    /* open and connect to server socket. */
    fun openSocket() {
        Log.d(TAG, "openSocket: ")
        Thread {
            try {
                socket = Socket(SOCKET_IP_ADDRESS, SOCKET_PORT)
                Log.d(TAG, "openSocket: completed to connect to server")

                // 출력 스트림 생성
                outputStream = socket?.getOutputStream()

                // 입력 스트림 생성
                val inputStream = socket?.getInputStream()
                bufferedReader = BufferedReader(InputStreamReader(inputStream))

                Log.d(TAG, "openSocket: socket -> $socket, post socket event (open)")
            } catch (e: Exception) {
                Log.e(TAG, "error -> ${e.localizedMessage}")
            }
        }.start()
    }
}