package cloud.huazai.uninotify;

import cloud.huazai.tool.java.lang.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * MailAccount
 *
 * @author Devon
 * @since 2026/3/9 13:44
 */
@Data
public class MailAccount implements Serializable {
    private static final String MAIL_PROTOCOL = "mail.transport.protocol";
    private static final String SMTP_HOST = "mail.smtp.host";
    private static final String SMTP_PORT = "mail.smtp.port";
    private static final String SMTP_AUTH = "mail.smtp.auth";
    private static final String SMTP_TIMEOUT = "mail.smtp.timeout";
    private static final String SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
    private static final String SMTP_WRITE_TIMEOUT = "mail.smtp.writetimeout";
    private static final String STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String SSL_ENABLE = "mail.smtp.ssl.enable";
    private static final String SSL_PROTOCOLS = "mail.smtp.ssl.protocols";
    private static final String SOCKET_FACTORY = "mail.smtp.socketFactory.class";
    private static final String SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
    private static final String SOCKET_FACTORY_PORT = "smtp.socketFactory.port";
    private static final String SPLIT_LONG_PARAMS = "mail.mime.splitlongparameters";
    private static final String MAIL_DEBUG = "mail.debug";
    public static final String[] MAIL_SETTING_PATHS = new String[]{"config/mail.setting", "config/mailAccount.setting", "mail.setting"};
    private String host;
    private Integer port;
    private Boolean auth;
    private String user;
    private String pass;
    private String from;
    private boolean debug;
    private Charset charset;
    private boolean splitlongparameters;
    private boolean encodefilename;
    private boolean starttlsEnable;
    private Boolean sslEnable;
    private String sslProtocols;
    private String socketFactoryClass;
    private boolean socketFactoryFallback;
    private int socketFactoryPort;
    private long timeout;
    private long connectionTimeout;
    private long writeTimeout;
    private final Map<String, Object> customProperty = new HashMap<>();


    public Boolean isAuth() {
        return this.auth;
    }

    public Properties getSmtpProps() {
        System.setProperty("mail.mime.splitlongparameters", String.valueOf(this.splitlongparameters));
        Properties p = new Properties();
        p.put("mail.transport.protocol", "smtp");
        p.put("mail.smtp.host", this.host);
        p.put("mail.smtp.port", String.valueOf(this.port));
        p.put("mail.smtp.auth", String.valueOf(this.auth));
        if (this.timeout > 0L) {
            p.put("mail.smtp.timeout", String.valueOf(this.timeout));
        }

        if (this.connectionTimeout > 0L) {
            p.put("mail.smtp.connectiontimeout", String.valueOf(this.connectionTimeout));
        }

        if (this.writeTimeout > 0L) {
            p.put("mail.smtp.writetimeout", String.valueOf(this.writeTimeout));
        }

        p.put("mail.debug", String.valueOf(this.debug));
        if (this.starttlsEnable) {
            p.put("mail.smtp.starttls.enable", "true");
            if (null == this.sslEnable) {
                this.sslEnable = true;
            }
        }

        if (null != this.sslEnable && this.sslEnable) {
            p.put("mail.smtp.ssl.enable", "true");
            p.put("mail.smtp.socketFactory.class", this.socketFactoryClass);
            p.put("mail.smtp.socketFactory.fallback", String.valueOf(this.socketFactoryFallback));
            p.put("smtp.socketFactory.port", String.valueOf(this.socketFactoryPort));
            if (StringUtils.isNotBlank(this.sslProtocols)) {
                p.put("mail.smtp.ssl.protocols", this.sslProtocols);
            }
        }

        p.putAll(this.customProperty);
        return p;
    }


}
