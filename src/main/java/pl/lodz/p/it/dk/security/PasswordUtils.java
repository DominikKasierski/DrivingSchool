package pl.lodz.p.it.dk.security;

import org.apache.commons.codec.digest.DigestUtils;

import javax.security.enterprise.identitystore.PasswordHash;

public class PasswordUtils implements PasswordHash {

    @Override
    public String generate(char[] password) {
        return DigestUtils.sha256Hex(new String(password));
    }

    @Override
    public boolean verify(char[] password, String hashedPassword) {
        String toVerify = generate(password);
        return hashedPassword.equals(toVerify);
    }

    public static String generate(String password) {
        return DigestUtils.sha256Hex(password);
    }

    public static boolean verify(String password, String hashedPassword) {
        String toVerify = generate(password);
        return hashedPassword.equals(toVerify);
    }
}
