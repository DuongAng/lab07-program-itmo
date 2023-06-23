package utility;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashes password.
 */
public class PasswordHasher {
    /**
     * Hashes password.
     *
     * @param password Password itseft.
     * @return Hashes password.
     */
    public static String hashPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(password.getBytes());
            BigInteger integer = new BigInteger(1, bytes);
            String newPassword = integer.toString(16);
            while (newPassword.length() < 32) {
                newPassword = "0" + newPassword;
            }
            return newPassword;
        } catch (NoSuchAlgorithmException exception){
            throw new IllegalStateException(exception);
        }
    }
}
