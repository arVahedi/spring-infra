package org.springinfra.assets;

import org.jose4j.jwt.ReservedClaimNames;

public class ClaimName extends ReservedClaimNames {

    public static final String USERNAME = "username";

    private ClaimName() {
        throw new AssertionError("Instance creation of this class is illegal");
    }
}
