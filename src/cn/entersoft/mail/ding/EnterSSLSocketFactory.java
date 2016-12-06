package cn.entersoft.mail.ding;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;


public class EnterSSLSocketFactory extends SSLSocketFactory{
	private SSLSocketFactory factory;
	
	private EnterSSLSocketFactory(){
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{new EnterTrustManager()}, null);
			factory = sslContext.getSocketFactory();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SocketFactory getDefault(){
		return new EnterSSLSocketFactory();
	}
	
	public Socket createSocket() throws IOException{
		return factory.createSocket();
	}

	@Override
	public Socket createSocket(Socket s, String host, int port,
			boolean autoClose) throws IOException {
		return factory.createSocket(s,host,port,autoClose);
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return factory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return factory.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket(String arg0, int arg1) throws IOException,
			UnknownHostException {
		return factory.createSocket(arg0,arg1);
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
		return factory.createSocket(arg0,arg1);
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
			throws IOException, UnknownHostException {
		return factory.createSocket(arg0,arg1,arg2,arg3);
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2,
			int arg3) throws IOException {
		return factory.createSocket(arg0,arg1,arg2,arg3);
	}

}
