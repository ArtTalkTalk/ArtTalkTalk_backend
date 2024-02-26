package org.example.youth_be.common.enums;

import lombok.Getter;

@Getter
public enum LockUsageType {
    ARTWORK("ARTWORK::"),
    COMMENT("COMMENT::"),
    USER("USER::"),
    FOLLOW("FOLLOW::"),
    LIKE("LIKE::"),
    VIEW_COUNT("VIEW_COUNT::");
    private final String usagePrefix;

    LockUsageType(String usagePrefix) {
        this.usagePrefix = usagePrefix;
    }
}
