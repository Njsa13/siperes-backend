package com.siperes.siperes.common.util;

import com.cloudinary.Cloudinary;
import com.siperes.siperes.exception.ServiceBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.*;

@Component
@RequiredArgsConstructor
public class ImageUtil {
    private final Cloudinary cloudinary;

    @Async
    public CompletableFuture<String> base64UploadImage(String base64Image) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] decodedBytes = Base64.getMimeDecoder().decode(base64Image.split(",")[1]);
                return cloudinary.uploader().upload(
                        decodedBytes,
                        Collections.singletonMap("public_id", UUID.randomUUID().toString())
                ).get("url").toString();
            } catch (Exception e) {
                throw new ServiceBusinessException(FAILED_UPLOAD_IMG);
            }
        });
    }


    @Async
    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, Map.of("invalidate", true));
        } catch (Exception e) {
            throw new ServiceBusinessException(FAILED_UPDATE_IMG);
        }
    }

    private String extractPublicId(String imageUrl) {
        try {
            int startIndex = imageUrl.lastIndexOf("/") + 1;
            int endIndex = imageUrl.lastIndexOf(".");
            return imageUrl.substring(startIndex, endIndex);
        } catch (Exception e) {
            throw new ServiceBusinessException(FAILED_DELETE_IMG);
        }
    }
}
