package springinfra.utility.cryptographic;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.MessageFormat;

@UtilityClass
@Slf4j
public class JksUtility {

    public KeyStore getKeyStore(KeyStoreType keyStoreType, String keyStorePath, String keyStorePass) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType.getValue());
        InputStream fileInputStream = new ClassPathResource(keyStorePath).getInputStream();
        keyStore.load(fileInputStream, keyStorePass.toCharArray());
        return keyStore;
    }

    public KeyPair getKeyPair(KeyStore keyStore, String keyAlias, String jceksPass) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        Key key = getKey(keyStore, keyAlias, jceksPass);

        if (key instanceof PrivateKey privateKey) {
            // Get certificate of public key
            java.security.cert.Certificate cert = keyStore.getCertificate(keyAlias);

            // Get public key
            PublicKey publicKey = cert.getPublicKey();

            // Return a key pair
            return new KeyPair(publicKey, privateKey);
        }

        throw new KeyStoreException(MessageFormat.format("The key {0} is not a private key", keyAlias));
    }

    public Key getKey(KeyStore keyStore, String keyAlias, String jceksPass) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        Key key = keyStore.getKey(keyAlias, jceksPass.toCharArray());
        if (key == null) {
            throw new KeyStoreException(MessageFormat.format("There is not any secret key in keystore for given alias {0}}.", keyAlias));
        }

        return key;
    }

    public enum KeyStoreType {
        JKS("JKS"),
        JCEKS("JCEKS");

        private final String value;

        KeyStoreType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
