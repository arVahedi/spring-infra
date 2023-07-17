package springinfra.utility.cryptographic;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import springinfra.exception.CryptographyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Slf4j
@UtilityClass
public class PkiUtility {

    public String encrypt(String message, Key key)
            throws CryptographyException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException,
            IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        final byte[] plainTextBytes = message.getBytes(StandardCharsets.UTF_8);
        final byte[] cipherText = cipher.doFinal(plainTextBytes);
        return Base64.toBase64String(cipherText);
    }

    public String decrypt(String message, Key key)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException,
            CryptographyException {

        Cipher decipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        decipher.init(Cipher.DECRYPT_MODE, key);
        final byte[] plainText = decipher.doFinal(Base64.decode(message));
        return new String(plainText, StandardCharsets.UTF_8);
    }
}
