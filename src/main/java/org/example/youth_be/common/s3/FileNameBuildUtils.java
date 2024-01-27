package org.example.youth_be.common.s3;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileNameBuildUtils implements FileNameGenerator{
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    @Override
    public String generateName(String originalFileName, String dirName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = UUID.randomUUID().toString();

        return dirName + "/" + fileName + fileExtension;
    }
}
