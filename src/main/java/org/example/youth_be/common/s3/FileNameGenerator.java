package org.example.youth_be.common.s3;

public interface FileNameGenerator {

    String generateName(String originalFileName, String dirName);
}
