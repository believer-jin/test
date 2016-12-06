package cn.entersoft.mail.framework.entity;


public class MailParam {
	private String protocol;//协议
	private String userName;//用户名
	private String passWord;//用户密码
	private boolean isValidate;//是否登录验证
	private String serverHost;//服务器主机
	private String mailType;// S:SSL；T:TLS；P:普通
	private String serverPort;//服务器端口
	private boolean isDebug;//是否开启deBug
	
	public boolean isDebug() {
		return isDebug;
	}
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getMailType() {
		return mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public boolean isValidate() {
		return isValidate;
	}
	public void setValidate(boolean isValidate) {
		this.isValidate = isValidate;
	}
	
}
