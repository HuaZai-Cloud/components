package cloud.huazai.uninotify;

import cloud.huazai.tool.java.lang.ArrayUtils;
import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.IOUtils;
import cloud.huazai.uninotify.util.JakartaInternalMailUtils;
import cloud.huazai.uninotify.util.JakartaMailUtils;
import cloud.huazai.uninotify.util.MailUtils;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.activation.FileTypeMap;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.util.ByteArrayDataSource;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * JakartaMail
 *
 * @author Devon
 * @since 2026/3/9 14:28
 */

public class JakartaMail implements Builder<MimeMessage>{

    private static final long serialVersionUID = 1L;
    private final MailAccount mailAccount;
    private String[] tos;
    private String[] ccs;
    private String[] bccs;
    private String[] reply;
    private String title;
    private String content;
    private boolean isHtml;
    private final Multipart multipart;
    private boolean useGlobalSession;
    private PrintStream debugOutput;

    public static JakartaMail create(MailAccount mailAccount) {
        return new JakartaMail(mailAccount);
    }

    public static JakartaMail create() {
        return new JakartaMail();
    }

    public JakartaMail() {
        this(MailUtils.getMailAccount());
    }

    public JakartaMail(MailAccount mailAccount) {
        this.multipart = new MimeMultipart();
        this.useGlobalSession = false;
        this.mailAccount = mailAccount;
    }

    public JakartaMail to(String... tos) {
        return this.setTos(tos);
    }

    public JakartaMail setTos(String... tos) {
        this.tos = tos;
        return this;
    }

    public JakartaMail setCcs(String... ccs) {
        this.ccs = ccs;
        return this;
    }

    public JakartaMail setBccs(String... bccs) {
        this.bccs = bccs;
        return this;
    }

    public JakartaMail setReply(String... reply) {
        this.reply = reply;
        return this;
    }

    public JakartaMail setTitle(String title) {
        this.title = title;
        return this;
    }

    public JakartaMail setContent(String content) {
        this.content = content;
        return this;
    }

    public JakartaMail setHtml(boolean isHtml) {
        this.isHtml = isHtml;
        return this;
    }

    public JakartaMail setContent(String content, boolean isHtml) {
        this.setContent(content);
        return this.setHtml(isHtml);
    }

    public JakartaMail setFiles(File... files) {
        if (ArrayUtils.isEmpty(files)) {
            return this;
        } else {
            DataSource[] attachments = new DataSource[files.length];

            for(int i = 0; i < files.length; ++i) {
                attachments[i] = new FileDataSource(files[i]);
            }

            return this.setAttachments(attachments);
        }
    }

    public JakartaMail setAttachments(DataSource... attachments) {
        if (ArrayUtils.isNotEmpty(attachments)) {
            Charset charset = this.mailAccount.getCharset();

            try {
                for(DataSource attachment : attachments) {
                    MimeBodyPart bodyPart = new MimeBodyPart();
                    bodyPart.setDataHandler(new DataHandler(attachment));
                    String nameEncoded = attachment.getName();
                    if (this.mailAccount.isEncodefilename()) {
                        nameEncoded = JakartaInternalMailUtils.encodeText(nameEncoded, charset);
                    }

                    bodyPart.setFileName(nameEncoded);
                    if (StringUtils.startWith(attachment.getContentType(), "image/")) {
                        bodyPart.setContentID(nameEncoded);
                        bodyPart.setDisposition("inline");
                    }

                    this.multipart.addBodyPart(bodyPart);
                }
            } catch (MessagingException e) {
                throw new MailException(e);
            }
        }

        return this;
    }

    public JakartaMail addImage(String cid, InputStream imageStream) {
        return this.addImage(cid, imageStream, (String)null);
    }

    public JakartaMail addImage(String cid, InputStream imageStream, String contentType) {
        ByteArrayDataSource imgSource;
        try {
            imgSource = new ByteArrayDataSource(imageStream, (StringUtils.isBlank(contentType) ? "image/jpeg" : contentType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imgSource.setName(cid);
        return this.setAttachments(imgSource);
    }

    public JakartaMail addImage(String cid, File imageFile) {
        InputStream in = null;

        JakartaMail var4;
        try {
            in = new FileInputStream(imageFile);
            var4 = this.addImage(cid, in, FileTypeMap.getDefaultFileTypeMap().getContentType(imageFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } finally {
            IOUtils.safeClose(in);
        }

        return var4;
    }

    public JakartaMail setCharset(Charset charset) {
        this.mailAccount.setCharset(charset);
        return this;
    }

    public JakartaMail setUseGlobalSession(boolean isUseGlobalSession) {
        this.useGlobalSession = isUseGlobalSession;
        return this;
    }

    public JakartaMail setDebugOutput(PrintStream debugOutput) {
        this.debugOutput = debugOutput;
        return this;
    }

    public MimeMessage build() {
        try {
            return this.buildMsg();
        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }

    public String send() throws MailException {
        try {
            return this.doSend();
        } catch (MessagingException e) {
            if (e instanceof SendFailedException) {
                Address[] invalidAddresses = ((SendFailedException)e).getInvalidAddresses();
                String msg = StringUtils.format("Invalid Addresses: {}", new Object[]{ArrayUtils.toString(invalidAddresses)});
                throw new MailException(msg, e);
            } else {
                throw new MailException(e);
            }
        }
    }

    private String doSend() throws MessagingException {
        MimeMessage mimeMessage = this.buildMsg();
        Transport.send(mimeMessage);
        return mimeMessage.getMessageID();
    }

    private MimeMessage buildMsg() throws MessagingException {
        Charset charset = this.mailAccount.getCharset();
        MimeMessage msg = new MimeMessage(this.getSession());
        String from = this.mailAccount.getFrom();
        if (StringUtils.isEmpty(from)) {
            msg.setFrom();
        } else {
            msg.setFrom(JakartaInternalMailUtils.parseFirstAddress(from, charset));
        }

        msg.setSubject(this.title, null == charset ? null : charset.name());
        msg.setSentDate(new Date());
        msg.setContent(this.buildContent(charset));
        msg.setRecipients(MimeMessage.RecipientType.TO, JakartaInternalMailUtils.parseAddressFromStrs(this.tos, charset));
        if (ArrayUtils.isNotEmpty(this.ccs)) {
            msg.setRecipients(MimeMessage.RecipientType.CC, JakartaInternalMailUtils.parseAddressFromStrs(this.ccs, charset));
        }

        if (ArrayUtils.isNotEmpty(this.bccs)) {
            msg.setRecipients(MimeMessage.RecipientType.BCC, JakartaInternalMailUtils.parseAddressFromStrs(this.bccs, charset));
        }

        if (ArrayUtils.isNotEmpty(this.reply)) {
            msg.setReplyTo(JakartaInternalMailUtils.parseAddressFromStrs(this.reply, charset));
        }

        return msg;
    }

    private Multipart buildContent(Charset charset) throws MessagingException {
        String charsetStr = null != charset ? charset.name() : MimeUtility.getDefaultJavaCharset();
        MimeBodyPart body = new MimeBodyPart();
        body.setContent(this.content, StringUtils.format("text/{}; charset={}", new Object[]{this.isHtml ? "html" : "plain", charsetStr}));
        this.multipart.addBodyPart(body);
        return this.multipart;
    }

    private Session getSession() {
        Session session = JakartaMailUtils.getSession(this.mailAccount, this.useGlobalSession);
        if (null != this.debugOutput) {
            session.setDebugOut(this.debugOutput);
        }

        return session;
    }
}
