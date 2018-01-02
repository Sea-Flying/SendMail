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


public class sendmail {
	
	public static void main(String[] args) {
		 // �ռ��˵�������
		String from,passwd,smtp,subject,content;
		List<String> to = new ArrayList<String>() ;
		List<String> cc = new ArrayList<String>() ;
		List<String> bcc = new ArrayList<String>() ;
		int n_to,n_cc,n_bcc,n_total;
		Properties conf = new Properties();
		
        if("".equals(args[0])||args[0]==null) {	
        	conf.load(new FileInputStream("config.properties"));
        	
        	
        	Exception e = new RuntimeException("No parameter specific");
        	throw e;
        }
        else if (args[1].contains("@")) {			
        	smtp = args[0];
        	from = args[1];
        	passwd = args[2];
            for(int i = 0; i < (n_to=Integer.parseInt(args[3])); i++) {
            	to.add(args[3+i]);
            }
            for(int i = 0; i < (n_cc=Integer.parseInt(args[4])); i++) {
            	cc.add(args[3+i]);
            }
            for(int i = 0; i < (n_bcc=Integer.parseInt(args[5])); i++) {
            	bcc.add(args[3+i]);
            }
            n_total = n_to + n_cc + n_bcc;
            subject = args[5+n_total+1];
            content = args[5+n_total+2];
		}
        else {
        	
        	
        	
        	String 
        }
		
	 
	 

	      Properties prop_session = new Properties();
	      	    
	      prop_session.setProperty("mail.transport.protocol", "smtp");   // ʹ�õ�Э�飨JavaMail�淶Ҫ��
		  prop_session.setProperty("mail.smtp.host", smtp);   // �����˵������ SMTP ��������ַ
	      prop_session.put("mail.smtp.auth", "true"); 
	      
	      Authentication auth = new Authentication(from, passwd);
	      // ��ȡĬ��session����
	      Session session = Session.getDefaultInstance(prop_session,auth);
	      session.setDebug(true);       
	 
	      try{
	         // ����Ĭ�ϵ� MimeMessage ����
	         MimeMessage message = new MimeMessage(session);
	 
	         // Set From: ͷ��ͷ�ֶ�
	         message.setFrom(new InternetAddress(from));
	 
	         // Set To: ͷ��ͷ�ֶ�
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
	         
	         // Set Subject: ͷ��ͷ�ֶ�
	         message.setSubject(subject);	 
	         // ������Ϣ��
	         message.setContent(content, "text/html;charset=UTF-8");
	         message.setSentDate(new Date());
	         
	         message.saveChanges();
	 
	         Transport transport = session.getTransport();
	         transport.connect(from, passwd);
	         // ������Ϣ
	         Transport.send(message,message.getAllRecipients());
	         transport.close();
	         
	         System.out.println("Sent message successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }

	}
	


}
