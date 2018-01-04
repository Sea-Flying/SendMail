import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.lang.*;


class Authentication extends Authenticator{  
    String username=null;  
    String password=null;  

    public Authentication(){  
    }
    
    public Authentication(String username, String password) {  
    this.username = username;  
    this.password = password;  
    }
    
    protected PasswordAuthentication getPasswordAuthentication(){
    PasswordAuthentication pa = new PasswordAuthentication(username, password);
    return pa;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 
            
}


public class Sendmail {
	
	public static void main(String[] args) throws Exception{
		 // 收件人电子邮箱
		String from="",passwd="",smtp="",subject="",content="";
		List<String> to = new ArrayList<String>() ;
		List<String> cc = new ArrayList<String>() ;
		List<String> bcc = new ArrayList<String>() ;
		int n_to=0,n_cc=0,n_bcc=0,n_total=0;
		Properties conf = new Properties();
		
        if("".equals(args[0])||args[0]==null) {
        	try {
				conf.load(new FileInputStream("config.properties"));
			}
			catch (Exception e){
        		e.printStackTrace();
			}
			smtp = conf.getProperty("SMTPServer");
        	from = conf.getProperty("From");
        	passwd = conf.getProperty("Passwd");
        	n_to = Integer.parseInt(conf.getProperty("N_to"));
        	n_cc = Integer.parseInt(conf.getProperty("N_cc"));
        	n_bcc = Integer.parseInt(conf.getProperty("N_bcc"));
        	for (int i = 0; i < n_to; i++) {
				to.add(conf.getProperty("To_"+i));
			}
			for (int i = 0; i < n_cc; i++) {
				cc.add(conf.getProperty("Cc_"+i));
			}
			for (int i = 0; i < n_bcc; i++) {
				cc.add(conf.getProperty("Bcc_"+i));
			}
			subject = conf.getProperty("Subject");
        	content = conf.getProperty("Content");
		//Exception e = new RuntimeException("No parameter specific");
        }
        else if (args[1].contains("@")) {			
        	smtp = args[0];
        	from = args[1];
        	passwd = args[2];
            for(int i = 0; i < (n_to=Integer.parseInt(args[3])); i++) {
            	to.add(args[3+i+1]);
            }
            for(int i = 0; i < (n_cc=Integer.parseInt(args[4+n_to])); i++) {
            	cc.add(args[4+n_to+1+i]);
            }
            for(int i = 0; i < (n_bcc=Integer.parseInt(args[5+n_to+n_cc])); i++) {
            	bcc.add(args[5+n_to+n_cc+1+i]);
            }
            n_total = n_to + n_cc + n_bcc;
            if (args[5+n_total+1] != null) {
				//subject = new String(args[5 + n_total + 1].getBytes("gbk"),"utf-8");
				//content = new String(args[5 + n_total + 2].getBytes("gbk"),"utf-8");
				subject = args[5 + n_total + 1];
				content = args[5 + n_total + 2];
            }
			else {
				try {
					conf.load(new FileInputStream("config.properties"));
				}
				catch (Exception e){
					e.printStackTrace();
				}
				subject = conf.getProperty("Subject");
				content = conf.getProperty("Content");
			}
		}
        else {
			Exception e = new RuntimeException("No parameter or config file specific");
        }

	      Properties prop_session = new Properties();
	      	    
	      prop_session.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
		  prop_session.setProperty("mail.smtp.host", smtp);   // 发件人的邮箱的 SMTP 服务器地址
	      prop_session.put("mail.smtp.auth", "true"); 
	      
	      Authentication auth = new Authentication(from, passwd);
	      // 获取默认session对象
	      Session session = Session.getDefaultInstance(prop_session,auth);
	      session.setDebug(true);       
	 
	      try{
	         // 创建默认的 MimeMessage 对象
	         MimeMessage message = new MimeMessage(session);
	 
	         // Set From: 头部头字段
	         message.setFrom(new InternetAddress(from));
	 
	         // Set To: 头部头字段
	         for(int i = 0; i < n_to; i++) {
	        	 message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.get(i)));
	         } 
	         // 
	         for(int i = 0; i < n_cc; i++) {
		         message.addRecipient(Message.RecipientType.CC, new InternetAddress(to.get(i)));
	         }
	         //
	         for(int i = 0; i < n_bcc; i++) {
		         message.addRecipient(Message.RecipientType.BCC, new InternetAddress(to.get(i)));
	         } 
	         
	         // Set Subject: 头部头字段
	         message.setSubject(subject);	 
	         // 设置消息体
	         message.setContent(content, "text/html;charset=UTF-8");
	         message.setSentDate(new Date());
	         
	         message.saveChanges();
	 
	         Transport transport = session.getTransport();
	         transport.connect(from, passwd);
	         // 发送消息
	         Transport.send(message,message.getAllRecipients());
	         transport.close();
	         
	         System.out.println("Sent message successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }

	}
	


}
