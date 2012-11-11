import java.io.*;
import java.net.*;

public class Sftpclient {

	public static void main(String[] args){
		
		DatagramSocket socket = null;
		BufferedReader br = null;
		FileReader fr = null;
		byte[] buffer = null;
		
		try {
			
		socket = new DatagramSocket();
		
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		File sendingFile = new File("D:\\Projects\\SFTP\\Test.txt");
		buffer = new byte[1024];
        DatagramPacket packet = null;
        InetAddress address = null;;
		try {
			address = InetAddress.getByName("localhost");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			fr = new FileReader(sendingFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        br = new BufferedReader(fr);
		
		try {
			
			
				buffer = br.readLine().getBytes();
				packet = new DatagramPacket(buffer, buffer.length,address,7735);
				socket.send(packet);
				if(br.readLine()!=null)
				{		
					br.close();
					socket.close();
				}
			}
		 catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	

