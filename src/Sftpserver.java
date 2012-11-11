import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Sftpserver {
	public static void main(String[] args){
		Socket s = null;
		ServerSocket ss = null;
		DataInputStream is = null;
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        baos = new ByteArrayOutputStream();
        byte[] byteArray = new byte[1024];
        int bytesRead;

		try {
			ss = new ServerSocket(7735);
			s = ss.accept();
			is = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
			
			
			
			try {
				fos = new FileOutputStream("D:\\Projects\\SFTP\\Testoutput.txt");
				bos = new BufferedOutputStream(fos);
				bytesRead = is.read(byteArray);
				do {
	                baos.write(byteArray);
	                bytesRead = is.read(byteArray);
	            } while (bytesRead != -1);
				bos.write(baos.toByteArray());
                bos.flush();
                bos.close();
                s.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}	
	}


