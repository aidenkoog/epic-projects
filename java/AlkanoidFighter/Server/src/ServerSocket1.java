import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerSocket1 
{
	ServerSocket sSocket = null;
	Socket socket = null;
	
	public ServerSocket1()
	{
		try
		{
			sSocket = new ServerSocket(10000);
		}
		catch(IOException e)
		{
			System.exit(-1);
		}
		
		try
		{
			socket = sSocket.accept();
		}
		catch(IOException e){}
	}	
}

