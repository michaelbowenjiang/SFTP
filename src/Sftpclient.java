import java.io.*;
import java.net.*;

public class Sftpclient {

	public static void main(String[] args){
		
		Socket s = null;
		DataOutputStream bos = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		byte[] byteArray = null;
		try {
			
		s = new Socket("localhost", 7735);
		bos = new DataOutputStream(s.getOutputStream());
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		File sendingFile = new File("D:\\Projects\\SFTP\\Test.txt");
		byteArray = new byte[(int)sendingFile.length()];
		
		try {
			fis = new FileInputStream(sendingFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bis = new BufferedInputStream(fis);
		try {
			bis.read(byteArray, 0, byteArray.length);
			bos.write(byteArray, 0, byteArray.length);
			bos.flush();
			bos.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		}
	}
	

