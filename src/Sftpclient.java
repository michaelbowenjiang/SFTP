import java.io.*;
import java.net.*;

public class Sftpclient {

	public static void main(String[] args){
		String hostName = args[0];
		int port = Integer.parseInt(args[1]);
		String FileName = args[2];
		File sendingFile = new File(FileName);
		InputStream is = null;;
		byte[] buffer = null;
		buffer = new byte[(int)sendingFile.length()];
		DatagramSocket socket = null;
        DatagramPacket packet = null;
        InetAddress address = null;
        try {
			is = new FileInputStream(sendingFile);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	
        
        try {
			socket = new DatagramSocket();
			}catch (IOException e) {
			e.printStackTrace();
		}
		try {
			address = InetAddress.getByName(hostName);
			} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	//UDP Packets can hold a maximum of 536 bytes. 
		//Split the packets into packets of 536 bytes and send. 
		//So, right now MSS = 536 bytes. Here size = 536
		try {
				int i = 0;
				int size;
				while(i<buffer.length)
				{
					if(buffer.length - i > 536)
					{
						
						size = 536;
						packet = new DatagramPacket(buffer,i,size,address,port);
						System.out.println(i);
						System.out.println(size);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						socket.send(packet);
						i = i+536;
					}
					// Else block to handle the last packet. 
					//size determines the size of the last packet.
					//Here, size<=536
					else
					{	
						size= buffer.length - i;
						packet = new DatagramPacket(buffer,i,size,address,port);
						System.out.println("Last Packet!");
						System.out.println("Offset:"+i);
						System.out.println("Size:"+size);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						socket.send(packet);
						i = i+536;
					}
					
					
				}	
				System.out.println("Client done!");
				is.close();
				socket.close();
				
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
	

