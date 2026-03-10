package cloud.huazai.uninotify.util;

import cloud.huazai.tool.java.lang.ArrayUtils;
import cloud.huazai.uninotify.MailException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JakartaInternalMailUtils
 *
 * @author Devon
 * @since 2026/3/9 14:54
 */

public class JakartaInternalMailUtils {

    public static InternetAddress[] parseAddressFromStrs(String[] addrStrs, Charset charset) {
        List<InternetAddress> resultList = new ArrayList(addrStrs.length);

        for(String addrStr : addrStrs) {
            InternetAddress[] addrs = parseAddress(addrStr, charset);
            if (ArrayUtils.isNotEmpty(addrs)) {
                Collections.addAll(resultList, addrs);
            }
        }

        return (InternetAddress[])resultList.toArray(new InternetAddress[0]);
    }

    public static InternetAddress parseFirstAddress(String address, Charset charset) {
        InternetAddress[] internetAddresses = parseAddress(address, charset);
        if (ArrayUtils.isEmpty(internetAddresses)) {
            try {
                return new InternetAddress(address);
            } catch (AddressException e) {
                throw new MailException(e);
            }
        } else {
            return internetAddresses[0];
        }
    }

    public static InternetAddress[] parseAddress(String address, Charset charset) {
        InternetAddress[] addresses;
        try {
            addresses = InternetAddress.parse(address);
        } catch (AddressException e) {
            throw new MailException(e);
        }

        if (ArrayUtils.isNotEmpty(addresses)) {
            String charsetStr = null == charset ? null : charset.name();

            for(InternetAddress internetAddress : addresses) {
                try {
                    internetAddress.setPersonal(internetAddress.getPersonal(), charsetStr);
                } catch (UnsupportedEncodingException e) {
                    throw new MailException(e);
                }
            }
        }

        return addresses;
    }

    public static String encodeText(String text, Charset charset) {
        try {
            return MimeUtility.encodeText(text, charset.name(), (String)null);
        } catch (UnsupportedEncodingException var3) {
            return text;
        }
    }
}
