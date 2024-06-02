import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MakePictureSmallerInSize {

    public static String TMP_FILE;
    public static String folderPath = "";
    public static double maxMB = 30;

    public static void main(String args[]) {
        checkPictureSize(Path.of(folderPath));
    }

    public static void checkPictureSize(Path targetFolder) {
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(targetFolder)) {
            for (Path path : paths) {
                double MB = getFileSize(path);
                if (MB <= maxMB) {
                    continue;
                }
                BufferedImage bi = ImageIO.read(path.toFile());
                TMP_FILE = path.toString().substring(0, path.toString().lastIndexOf(".")) + "a.jpg";
                while (MB > maxMB) {
                    bi = resizeImage(bi);
                    ImageIO.write(bi, "jpg", new File(TMP_FILE));
                    MB = getFileSize(Paths.get(TMP_FILE));
                }
                Files.delete(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double getFileSize(Path path) throws IOException {
        long bytes = Files.size(path);
        return (double) bytes / 1024 / 1024;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(originalImage)
                    .size(originalImage.getWidth(), originalImage.getHeight())
                    .outputFormat("JPEG")
                    .outputQuality(0.90)
                    .toOutputStream(outputStream);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                return ImageIO.read(inputStream);
            }
        }
    }
}

