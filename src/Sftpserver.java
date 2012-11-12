import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class Sftpserver {
	
		public static void main(String[] args){
			String FileName = args[1];
	        int port = Integer.parseInt(args[0]);
			DatagramSocket socket = null;
			byte[] buffer = new byte[536];
			DatagramPacket packet = null;
			OutputStream os = null;
			File newFile = new File(FileName);
			
			try {
				socket = new DatagramSocket(port);
				} catch (IOException e) {
					e.printStackTrace();
				}
			try {
				
				packet = new DatagramPacket(buffer, buffer.length);
				while(true)
				{
					
					socket.receive(packet);
			          
				try {
					os = new FileOutputStream(newFile,true);
					buffer = packet.getData();
					int length = (int)newFile.length();
					System.out.println("Offset:"+length);
					System.out.println("Length"+packet.getLength());
					os.write(packet.getData(),0,packet.getLength());
				os.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}}catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}	
	}


