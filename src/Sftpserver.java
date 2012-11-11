import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class Sftpserver {
	public static void main(String[] args){
		DatagramSocket socket = null;
		byte[] buffer = new byte[1024];
		DatagramPacket packet = null;
        String s = null;

		try {
			socket = new DatagramSocket(7735);
		} catch (IOException e) {
			e.printStackTrace();
		}
			try {
				FileWriter fw = new FileWriter("D:\\Projects\\SFTP\\Testoutput.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				s = new String(packet.getData(),0,packet.getLength());
				System.out.println(s);
				bw.write(s);
				bw.flush();
				bw.close();
				socket.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}	
	}


