package cn.entersoft.mail.ding;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class EnterTrustManager implements X509TrustManager{

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		//  everything is trusted
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		//  everything is trusted
		
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];	
	}

}
