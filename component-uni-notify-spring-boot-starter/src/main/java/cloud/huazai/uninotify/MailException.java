package cloud.huazai.uninotify;

import cloud.huazai.tool.java.lang.StringUtils;

/**
 * MailException
 *
 * @author Devon
 * @since 2026/3/9 14:55
 */

public class MailException extends RuntimeException{

    private static final long serialVersionUID = 8247610319171014183L;

    public MailException(Throwable e) {
        super(null == e ? "null" : StringUtils.format("{}: {}", new Object[]{e.getClass().getSimpleName(), e.getMessage()}));
    }

    public MailException(String message) {
        super(message);
    }

    public MailException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public MailException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public MailException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public MailException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }
}
