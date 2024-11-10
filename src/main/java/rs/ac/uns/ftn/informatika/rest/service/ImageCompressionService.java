package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.nio.file.*;
import java.util.Iterator;

@Service
public class ImageCompressionService {

    // Define the directory where images are stored
    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/uploads/images/";

    // Compress images older than 30 days
    @Scheduled(cron = "0 0 0 * * ?") // Daily at midnight
    public void compressOldImages() throws IOException {
        Path imageDir = Paths.get(IMAGE_DIRECTORY);
        File dir = imageDir.toFile();
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (isImage(file) && isOlderThan30Days(file)) {
                    compressImage(file);
                }
            }
        }
    }

    // Check if the file is an image by its extension or content type
    private boolean isImage(File file) {
        String[] validExtensions = {".jpg", ".jpeg", ".png", ".jfif", ".gif", ".bmp", ".tiff"};
        String fileName = file.getName().toLowerCase();
        for (String ext : validExtensions) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    // Check if the image file is older than 30 days
    private boolean isOlderThan30Days(File file) {
        long currentTime = System.currentTimeMillis();
        long lastModifiedTime = file.lastModified();
        long daysDifference = (currentTime - lastModifiedTime) / (1000 * 60 * 60 * 24);
        return daysDifference > 30;
    }

    public void compressImage(File imageFile) throws IOException {
        // Step 1: Rename the original image to add '_original' suffix.
        String originalFileName = imageFile.getName();
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String originalImagePath = imageFile.getParent() + "/" + baseName + "_original" + getFileExtension(imageFile); // Add "_original" suffix
        File renamedOriginal = new File(originalImagePath);
        boolean renamed = imageFile.renameTo(renamedOriginal);

        if (!renamed) {
            // Handle the case if the renaming failed
            throw new IOException("Failed to rename the original image: " + imageFile.getName());
        }

        // Step 2: Compress the renamed original image and save as the same name.
        BufferedImage image = ImageIO.read(renamedOriginal);
        if (image == null) {
            return; // Skip non-image files
        }

        // Create the compressed image and save it with the original file name
        String compressedImagePath = imageFile.getParent() + "/" + baseName + getFileExtension(imageFile);
        File compressedImage = new File(compressedImagePath);

        // Use ImageIO to compress the image (e.g., as JPEG)
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (writers.hasNext()) {
            ImageWriter writer = writers.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(0.7f); // Adjust quality (0.0f to 1.0f)

            // Write the compressed image
            try (FileImageOutputStream outputStream = new FileImageOutputStream(compressedImage)) {
                writer.setOutput(outputStream);
                writer.write(null, new IIOImage(image, null, null), writeParam);
                writer.dispose();
            }
        }

        // Step 3: Now you have two files:
        // - Renamed original file: "oldname_original.extension"
        // - Compressed image: "oldname.extension"
        // Update database with the new image path accordingly (to point to "oldname.extension").
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
