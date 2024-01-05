import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SetFileNameAsDateTime {

    public static String folderPath = "";

    public static void main(String args[]) {
        changeFileName(Path.of(folderPath));
    }

    public static void changeFileName(Path targetFolder) {
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(targetFolder)) {
            for (Path path : paths) {
                Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());
                ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                LocalDateTime ldt = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                String newLdt = ldt.format(myFormatObj);
                String pathName = path.toString();
                String extension = pathName.substring(pathName.lastIndexOf("."));
                File newName = new File(folderPath + "\\" + newLdt + extension);
                path.toFile().renameTo(newName);
            }

        } catch (IOException | ImageProcessingException e) {
            e.printStackTrace();
        }
    }
}
