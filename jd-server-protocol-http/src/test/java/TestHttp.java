import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

public class TestHttp {

	public static void main(String[] args) throws IOException  {
		ServerSocket ser = null;
		Socket client = null ;
			ser = new ServerSocket(65535);
			while((client=ser.accept())!= null) {
				InputStream is = client.getInputStream();
					try {
						System.out.println(IOUtils.toString(is));
						client.shutdownInput();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						IOUtils.closeQuietly(is,client.getOutputStream());
						IOUtils.closeQuietly(client);
					}
					
			}
	
	}

}
