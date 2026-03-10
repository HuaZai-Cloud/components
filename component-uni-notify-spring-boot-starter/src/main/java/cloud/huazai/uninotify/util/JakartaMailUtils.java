package cloud.huazai.uninotify.util;

import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;
import cloud.huazai.tool.java.util.IOUtils;
import cloud.huazai.tool.java.util.MapUtils;
import cloud.huazai.uninotify.JakartaMail;
import cloud.huazai.uninotify.JakartaUserPasswordAuthentication;
import cloud.huazai.uninotify.MailAccount;
import jakarta.mail.Authenticator;
import jakarta.mail.Session;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * JakartaMailUtil
 *
 * @author Devon
 * @since 2026/3/9 15:02
 */

public class JakartaMailUtils {

    public static String sendText(String to, String subject, String content, File... files) {
        return send(to, subject, content, false, files);
    }

    public static String sendHtml(String to, String subject, String content, File... files) {
        return send(to, subject, content, true, files);
    }

    public static String send(String to, String subject, String content, boolean isHtml, File... files) {
        return send((Collection)splitAddress(to), subject, content, isHtml, files);
    }

    public static String send(String to, String cc, String bcc, String subject, String content, boolean isHtml, File... files) {
        return send((Collection)splitAddress(to), (Collection)splitAddress(cc), (Collection)splitAddress(bcc), subject, content, isHtml, files);
    }

    public static String sendText(Collection<String> tos, String subject, String content, File... files) {
        return send(tos, subject, content, false, files);
    }

    public static String sendHtml(Collection<String> tos, String subject, String content, File... files) {
        return send(tos, subject, content, true, files);
    }

    public static String send(Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
        return send((Collection)tos, (Collection)null, (Collection)null, subject, content, isHtml, files);
    }

    public static String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, boolean isHtml, File... files) {
        return send(MailUtils.getMailAccount(), true, tos, ccs, bccs, subject, content, (Map)null, isHtml, files);
    }

    public static String send(MailAccount mailAccount, String to, String subject, String content, boolean isHtml, File... files) {
        return send((MailAccount)mailAccount, (Collection)splitAddress(to), subject, content, isHtml, files);
    }

    public static String send(MailAccount mailAccount, Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
        return send((MailAccount)mailAccount, tos, (Collection)null, (Collection)null, subject, content, isHtml, files);
    }

    public static String send(MailAccount mailAccount, Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, boolean isHtml, File... files) {
        return send(mailAccount, false, tos, ccs, bccs, subject, content, (Map)null, isHtml, files);
    }

    public static String sendHtml(String to, String subject, String content, Map<String, InputStream> imageMap, File... files) {
        return send(to, subject, content, imageMap, true, files);
    }

    public static String send(String to, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send((Collection)splitAddress(to), subject, content, (Map)imageMap, isHtml, files);
    }

    public static String send(String to, String cc, String bcc, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send((Collection)splitAddress(to), splitAddress(cc), splitAddress(bcc), subject, content, (Map)imageMap, isHtml, files);
    }

    public static String sendHtml(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, File... files) {
        return send(tos, subject, content, imageMap, true, files);
    }

    public static String send(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send((Collection)tos, (Collection)null, (Collection)null, subject, content, (Map)imageMap, isHtml, files);
    }

    public static String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(MailUtils.getMailAccount(), true, tos, ccs, bccs, subject, content, imageMap, isHtml, files);
    }

    public static String send(MailAccount mailAccount, String to, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send((MailAccount)mailAccount, (Collection)splitAddress(to), subject, content, (Map)imageMap, isHtml, files);
    }

    public static String send(MailAccount mailAccount, Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(mailAccount, tos, (Collection)null, (Collection)null, subject, content, imageMap, isHtml, files);
    }

    public static String send(MailAccount mailAccount, Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(mailAccount, false, tos, ccs, bccs, subject, content, imageMap, isHtml, files);
    }

    public static Session getSession(MailAccount mailAccount, boolean isSingleton) {
        Authenticator authenticator = null;
        if (mailAccount.isAuth()) {
            authenticator = new JakartaUserPasswordAuthentication(mailAccount.getUser(), mailAccount.getPass());
        }

        return isSingleton ? Session.getDefaultInstance(mailAccount.getSmtpProps(), authenticator) : Session.getInstance(mailAccount.getSmtpProps(), authenticator);
    }

    private static String send(MailAccount mailAccount, boolean useGlobalSession, Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        JakartaMail mail = JakartaMail.create(mailAccount).setUseGlobalSession(useGlobalSession);
        if (CollectionUtils.isNotEmpty(ccs)) {
            mail.setCcs((String[])ccs.toArray(new String[0]));
        }

        if (CollectionUtils.isNotEmpty(bccs)) {
            mail.setBccs((String[])bccs.toArray(new String[0]));
        }

        mail.setTos((String[])tos.toArray(new String[0]));
        mail.setTitle(subject);
        mail.setContent(content);
        mail.setHtml(isHtml);
        mail.setFiles(files);
        if (MapUtils.isNotEmpty(imageMap)) {
            for(Map.Entry<String, InputStream> entry : imageMap.entrySet()) {
                mail.addImage((String)entry.getKey(), (InputStream)entry.getValue());
                IOUtils.safeClose((Closeable)entry.getValue());
            }
        }

        return mail.send();
    }

    private static List<String> splitAddress(String addresses) {
        if (StringUtils.isBlank(addresses)) {
            return null;
        } else {
            List<String> result;
            if (StringUtils.contains(addresses, ",")) {
                result = StringUtils.splitTrim(addresses, ",");
            } else if (StringUtils.contains(addresses, ";")) {
                result = StringUtils.splitTrim(addresses, ";");
            } else {
                result = List.of( addresses);
            }

            return result;
        }
    }
}
