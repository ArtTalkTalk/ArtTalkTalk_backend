package org.example.youth_be.common.enums;

import lombok.Getter;

@Getter
public enum LockUsageType {
    ARTWORK("ARTWORK::"),
    COMMENT("COMMENT::"),
    USER("USER::"),
    FOLLOW("FOLLOW::"),
    LIKE("LIKE::");
    private final String usagePrefix;

    LockUsageType(String usagePrefix) {
        this.usagePrefix = usagePrefix;
    }
}
