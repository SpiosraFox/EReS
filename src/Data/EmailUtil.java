/**
    Utility for sending email
    CSC-289 - Group 4
    @author Timothy Burns
*/

package Data;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil
{
    /**
        EmailAdmin - Send an email to the administrator of the system
    
        @param senderName Name of sender
        @param senderAddress Email address of sender
        @param body Body of message
        @throws AddressException Error parsing addresses
        @throws MessagingException Error sending message
        @throws UnsupportedEncodingException Failed to encode name in address
    */
    
    public static void emailAdmin(String senderName, String senderAddress,
                                  String body)
            throws AddressException, MessagingException,
                   UnsupportedEncodingException
    {
        // Build address to display in message
        InternetAddress address = new InternetAddress(senderAddress);
        if (senderName != null && !senderName.isEmpty())
            address.setPersonal(senderName);
        
        // Get the properties for the guest to send email
        Properties props = SystemUtil.getGuestSMTPProperties();
        
        // Get address, username, password from properties
        InternetAddress from = new InternetAddress(props
                .getProperty("Address"));
        String username = props.getProperty("User");
        String password = props.getProperty("Pass");
        
        // Parse the properties
        props = parseProps(props);
        
        // Get the address for the administrator to receive email at
        InternetAddress to = new InternetAddress
            (SystemUtil.getAdminGetAddress());
        
        // Build authenticator
        Authenticator auth = null;
        if (props.containsKey("mail.smtp.auth"))
        {
            auth = new Authenticator()
            {
                private PasswordAuthentication pa = new PasswordAuthentication
                        (username, password);

                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }
        
        // Create session
        Session session = Session.getInstance(props, auth);
        
        // Create & send message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(from);
        message.setRecipient(Message.RecipientType.TO, to);
        message.setSubject("Message from Guest " + senderName);
        message.setSentDate(new Date());
        message.setText("Message from guest " + address.toString() +
                "\n\n" + body);
        Transport.send(message);
    }
    
    /**
        EmailReserver - Send an email to a reserver
    
        @param reserver The reserver to send an email message to
        @param subject The subject of the message
        @param body The body of the message
        @throws AddressException Error parsing addresses
        @throws MessagingException Error sending message
    */
    
    public static void emailReserver(Reserver reserver, String subject,
                                     String body)
            throws AddressException, MessagingException
    {
        // Get the properties for the admin to send email
        Properties props = SystemUtil.getAdminSMTPProperties();
        
        // Get address, username, password from properties
        InternetAddress from = new InternetAddress(props
                .getProperty("Address"));
        String username = props.getProperty("User");
        String password = props.getProperty("Pass");
        
        // Parse the properties
        props = parseProps(props);
        
        // Get the address for the reserver to receive email at
        InternetAddress to = new InternetAddress(reserver.getEmailAddress());
        
        // Build authenticator
        Authenticator auth = null;
        if (props.containsKey("mail.smtp.auth"))
        {
            auth = new Authenticator()
            {
                private PasswordAuthentication pa = new PasswordAuthentication
                        (username, password);

                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }
        
        // Create session
        Session session = Session.getInstance(props, auth);
        
        // Create & send message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(from);
        message.setRecipient(Message.RecipientType.TO, to);
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setText(body);
        Transport.send(message);
    }
    
    /**
        ParseProps - Parse email properties into formats suitable for sending
        email
    
        @param props Email properties to parse
        @return parsedProps Properties parsed into suitable formats
    */
    
    private static Properties parseProps(Properties props)
    {
        Properties parsedProps = new Properties();
        
        parsedProps.put("mail.smtp.host", props.getProperty("Host"));
        parsedProps.put("mail.smtp.port", props.getProperty("Port"));
        
        if (props.getProperty("Security").equalsIgnoreCase("SSL"))
            parsedProps.put("mail.smtp.ssl.enable", "true");
        else if (props.getProperty("Security").equalsIgnoreCase("TLS"))
            parsedProps.put("mail.smtp.starttls.enable", "true");
        
        if (!props.getProperty("User").isEmpty() ||
            !props.getProperty("Pass").isEmpty())
        {
            parsedProps.put("mail.smtp.auth", "true");
        }
        
        return parsedProps;
    }
}