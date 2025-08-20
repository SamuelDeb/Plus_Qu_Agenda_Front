package fr.sd.reservcreneaux.reservcreneauxfront.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyUtils {
    private static final Logger logger = Logger.getLogger(KeyUtils.class.getName());

    public static PublicKey getPublicKey(String resourcePath) throws Exception {
        logger.log(Level.INFO, "Loading public key from resource: " + resourcePath);

        try (InputStream is = KeyUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Public key resource not found: " + resourcePath);
            }

            byte[] keyBytes = is.readAllBytes();
            String key = new String(keyBytes)
                    .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            logger.log(Level.INFO, "Public key content (base64): " + key);

            byte[] decodedKey = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        }
    }
}
