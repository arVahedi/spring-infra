package org.springinfra.utility.persistence;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class PublicIdUtil {

    public UUID generate() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
