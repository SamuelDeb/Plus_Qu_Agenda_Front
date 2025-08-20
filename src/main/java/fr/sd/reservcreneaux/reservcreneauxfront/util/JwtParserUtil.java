package fr.sd.reservcreneaux.reservcreneauxfront.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JwtParserUtil {

    private static PublicKey publicKey;
    private static final Logger logger = Logger.getLogger(JwtParserUtil.class.getName());

    static {
        try {
            logger.log(Level.INFO, "Initializing JwtParserUtil");
            publicKey = KeyUtils.getPublicKey("publicKey.pem");
            logger.log(Level.INFO, "Public key loaded successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load public key", e);
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }


}
