package com.siperes.siperes.common.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.siperes.siperes.exception.ServiceBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.*;

@Slf4j
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
                log.error(e.getMessage());
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
            log.error(e.getMessage());
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

    @Async
    public CompletableFuture<String> downloadImageAsBase64(String imageUrl) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String publicId = extractPublicId(imageUrl);
                Map result = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
                String imageUrlFromCloudinary = result.get("url").toString();

                URL url = new URL(imageUrlFromCloudinary);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                try (InputStream inputStream = url.openStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                byte[] imageBytes = outputStream.toByteArray();
                return Base64.getEncoder().encodeToString(imageBytes);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new ServiceBusinessException(FAILED_DOWNLOAD_IMG);
            }
        });
    }
}
