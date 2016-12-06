package cn.entersoft.mail.ding;
public class SmtpServer {
	
	public String smtp;
	public String user;
	public String password;
	
	public SmtpServer(String line) {
		String[] temp = line.split(",");
		this.smtp = temp[0];
		this.user = temp[1];
		this.password = temp[2];
	}
	
	public String getPassword() {
		return password;
	}
		
	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	

}