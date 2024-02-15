package org.example.youth_be.common.s3;

import org.example.youth_be.image.domain.ImageEntity;

public class FileNameExtractor {

    public static String getFileName(ImageEntity image) {

        String url = image.getImageUrl();
        int lastIndex = url.lastIndexOf('/');
        int secondLastIndex = url.lastIndexOf('/', lastIndex - 1);
        String fileName = url.substring(secondLastIndex + 1);

        return fileName;
    }
}
