package personal.project.springinfra.utility.cryptographic;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import personal.project.springinfra.exception.CryptographyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;

@UtilityClass
@Slf4j
public class JKSUtility {

    public static KeyStore getKeyStore(KeyStoreType keyStoreType, String keyStorePath, String keyStorePass)
            throws Exception {

        if (keyStorePath.startsWith("file:/")) {
            keyStorePath = keyStorePath.substring(5);
        }
        KeyStore ks = KeyStore.getInstance(keyStoreType.getValue());
        FileInputStream fileInputStream = new FileInputStream(keyStorePath);
        if (fileInputStream == null) {
            throw new Exception("JKS doesn't exist!");
        }
        ks.load(fileInputStream, keyStorePass.toCharArray());
        return ks;
    }

    public static String encrypt(KeyStore keyStore, String message, String alias, String jceksPass)
            throws CryptographyException, NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException,
            InvalidKeyException, NoSuchPaddingException, BadPaddingException,
            IllegalBlockSizeException {

        Key secretKeyEntry = keyStore.getKey(alias, jceksPass.toCharArray());
        if (secretKeyEntry == null) {
            log.error(String.format("There isn't any secret key in keystore for given alias [%s].", alias));
            throw new CryptographyException(String.format("There isn't any secret key in keystore for given alias [%s].", alias));
        }
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeyEntry);
        final byte[] plainTextBytes = message.getBytes(StandardCharsets.UTF_8);
        final byte[] cipherText = cipher.doFinal(plainTextBytes);
        return Base64.toBase64String(cipherText);
    }

    public static String decrypt(KeyStore keyStore, String message, String alias, String jceksPass)
            throws NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException,
            InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException,
            CryptographyException {

        Key secretKeyEntry = keyStore.getKey(alias, jceksPass.toCharArray());
        if (secretKeyEntry == null) {
            log.error(String.format("There isn't any secret key in keystore for given alias [%s].", alias));
            throw new CryptographyException(String.format("There isn't any secret key in keystore for given alias [%s].", alias));
        }

        Cipher decipher = Cipher.getInstance("AES");
        decipher.init(Cipher.DECRYPT_MODE, secretKeyEntry);
        final byte[] plainText = decipher.doFinal(Base64.decode(message));
        return new String(plainText, StandardCharsets.UTF_8);
    }

    public enum KeyStoreType {
        JKS("JKS"),
        JCEKS("JCEKS");

        private String value;

        KeyStoreType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
