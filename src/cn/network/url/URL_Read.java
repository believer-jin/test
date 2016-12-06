package cn.network.url;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class URL_Read {
	private final static String PROXY_HOST = "10.71.19.195";
	private final static String PROXY_PORT = "3125";

	private static void readUrl(String url) throws IOException {

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		int status = client.executeMethod(method);
		System.out.println("status:" + status + "/r response=" + method.getResponseBodyAsString());
		method.releaseConnection();
	}

	private static void setProxy() {
		Properties prop = System.getProperties();
		// ����http����Ҫʹ�õĴ���������ĵ�ַ
		prop.setProperty("http.proxyHost", PROXY_HOST);
		// ����http����Ҫʹ�õĴ���������Ķ˿�
		prop.setProperty("http.proxyPort", PROXY_PORT);
		// ���ò���Ҫͨ��������������ʵ�����������ʹ��*ͨ����������ַ��|�ָ�
		prop.setProperty("http.nonProxyHosts", "localhost|127.0.0.1|10.71.32.*");
		// ���ð�ȫ����ʹ�õĴ����������ַ��˿�
		// ��û��https.nonProxyHosts���ԣ�������http.nonProxyHosts �����õĹ������
		prop.setProperty("https.proxyHost", PROXY_HOST);
		prop.setProperty("https.proxyPort", PROXY_PORT);
		// ʹ��ftp������������������˿��Լ�����Ҫʹ��ftp���������������
		prop.setProperty("ftp.proxyHost", PROXY_HOST);
		prop.setProperty("ftp.proxyPort", PROXY_PORT);
		prop.setProperty("ftp.nonProxyHosts", "localhost|192.168.0.*");
		// socks����������ĵ�ַ��˿�
		prop.setProperty("socksProxyHost", PROXY_HOST);
		prop.setProperty("socksProxyPort", PROXY_PORT);
		// ���õ�½��������������û���������
		Authenticator.setDefault(new MyAuthenticator("userName", "Password"));
	}

	static class MyAuthenticator extends Authenticator {
		private String user = "";
		private String password = "";

		public MyAuthenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, password.toCharArray());
		}

	}

	public static void main(String[] args) {
		try {
			// setProxy();
			readUrl("http://www.ip138.com/ip2city.asp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
