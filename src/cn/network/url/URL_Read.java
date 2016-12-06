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
		// 设置http访问要使用的代理服务器的地址
		prop.setProperty("http.proxyHost", PROXY_HOST);
		// 设置http访问要使用的代理服务器的端口
		prop.setProperty("http.proxyPort", PROXY_PORT);
		// 设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
		prop.setProperty("http.nonProxyHosts", "localhost|127.0.0.1|10.71.32.*");
		// 设置安全访问使用的代理服务器地址与端口
		// 它没有https.nonProxyHosts属性，它按照http.nonProxyHosts 中设置的规则访问
		prop.setProperty("https.proxyHost", PROXY_HOST);
		prop.setProperty("https.proxyPort", PROXY_PORT);
		// 使用ftp代理服务器的主机、端口以及不需要使用ftp代理服务器的主机
		prop.setProperty("ftp.proxyHost", PROXY_HOST);
		prop.setProperty("ftp.proxyPort", PROXY_PORT);
		prop.setProperty("ftp.nonProxyHosts", "localhost|192.168.0.*");
		// socks代理服务器的地址与端口
		prop.setProperty("socksProxyHost", PROXY_HOST);
		prop.setProperty("socksProxyPort", PROXY_PORT);
		// 设置登陆到代理服务器的用户名和密码
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
