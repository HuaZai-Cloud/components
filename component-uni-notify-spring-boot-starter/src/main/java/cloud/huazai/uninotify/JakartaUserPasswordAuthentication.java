package cloud.huazai.uninotify;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

/**
 * JakartaUserPasswordAuthentication
 *
 * @author Devon
 * @since 2026/3/9 14:22
 */

public class JakartaUserPasswordAuthentication extends Authenticator {
    private final String user;
    private final String pass;

    public JakartaUserPasswordAuthentication(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user, this.pass);
    }
}
