package com.example.ciscoproject.model;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;


public class Mail extends Authenticator {

    private String mailhost = "smtp.gmail.com";
    private String user;
    private String pass;
    private Session session;

    static {
        Security.addProvider(new JsseProvider());
    }

    public Mail(String user,String pass)
    {
        this.user=user;
        this.pass=pass;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol","smtp");
        props.setProperty("mail.host",mailhost);
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.port","465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(user,pass);
    }

    public synchronized void sendMail(String subject,String body,String sender,String receipent) throws Exception
    {
        try{
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(),"text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receipent));
            Transport.send(message);
        }
        catch (Exception e){

        }
    }
    public class ByteArrayDataSource implements DataSource {

        private byte[] data;

        private String type;





        public ByteArrayDataSource(byte[] data, String type) {

            super();

            this.data = data;

            this.type = type;

        }



        public ByteArrayDataSource(byte[] data) {

            super();

            this.data = data;

        }





        public void setType(String type) {

            this.type = type;

        }



        public String getContentType() {

            if (type == null)

                return "application/octet-stream";

            else

                return type;

        }



        public InputStream getInputStream() throws IOException {

            return new ByteArrayInputStream(data);

        }



        public String getName() {

            return "ByteArrayDataSource";

        }



        public OutputStream getOutputStream() throws IOException {

            throw new IOException("Not Supported");

        }

    }
}
