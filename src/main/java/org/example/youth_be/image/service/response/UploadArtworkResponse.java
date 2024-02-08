package org.example.youth_be.image.service.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UploadArtworkResponse{

    private List<String> urls;
    private List<String> fileNames;

}
