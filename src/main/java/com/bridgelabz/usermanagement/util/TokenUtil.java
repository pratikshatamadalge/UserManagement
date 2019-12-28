package com.bridgelabz.usermanagement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

/**
 * @author pratiksha
 *
 */
@Component
public class TokenUtil {
	private TokenUtil() {
	}

	public static final String TOKEN_SECRET = "BridgeLabz";
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class);

	/**
	 * @param email
	 * @return JWT token
	 */
	public static String getJWTToken(String email) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

			return JWT.create().withClaim("emailId", email).sign(algorithm);
		} catch (Exception e) {
			LOGGER.error("Unable to create JWT Token");
		}
		return null;
	}

	/**
	 * @param token
	 * @return decode token
	 */
	public static String decodeToken(String token) {
		Verification verification = null;
		try {
			verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Unable to decode JWT Token");
		}
		if (verification == null)
			return "Unable to decode JWT Token";
		JWTVerifier jwtverifier = verification.build();
		DecodedJWT decodedjwt = jwtverifier.verify(token);

		Claim claim = decodedjwt.getClaim("emailId");
		if (claim == null)
			return null;
		return claim.asString();
	}
}
