import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author AL
 *	Simple Java server. Support multi-threads
 *
 */



public class Server extends Thread{
		
		private int PORT = 5657;
		private ServerSocket sSocket;
		private Socket client = null;
		private boolean run = false;
		private PrintWriter writer;
		private OnMessageReceived messageListener;
		
		/**
		 * Constructor 
		 * @param args
		 */
		
		public Server(OnMessageReceived messageListener){
				this.messageListener = messageListener;
		}
		
		
		
		public void sendMessage(String message)
		{
			try
			{
				if (writer != null && !writer.checkError())
				{
					System.out.println(message);
					// Here you can connect with database or else you can do what you want with static message
					writer.println(message);
					writer.flush();
				}
			}
			catch (Exception e)
			{
			}
		}
		
		
		@Override
		public void run(){
				super.run();
				run = true;
				try{
					System.out.println("Connecting...");
					
					sSocket = new ServerSocket(PORT);
					
					try{
							client = sSocket.accept();
							System.out.println("Receiving...");
							writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
							System.out.println("Sent");
							System.out.println("Connected");
							BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
							sendMessage("Server connected with Client now you can chat with socket server.");
							
							while (run){
								String message = in.readLine();
								if (message != null && messageListener != null)
								{
									// call the method messageReceived from ServerBoard class
									messageListener.messageReceived(message);
								}
							}
					}
					catch (Exception e){
							System.out.println("Error: "+e.getMessage());
							e.printStackTrace();
					}
					finally{
						client.close();
						System.out.println("Done.");
					}
				}
				catch (Exception e)
				{
					System.out.println("Error");
					e.printStackTrace();
				}
			
		}
		public static void main(String[] args){
				Server s = new Server(new Server.OnMessageReceived() {
					
					@Override
					public void messageReceived(String message) {
						// TODO Auto-generated method stub
						System.out.println("Msg Recieved");
						System.out.println("\n" + message);
					}
				});
				s.start();
		}
		
		public interface OnMessageReceived
		{
			public void messageReceived(String message);
		}
}
