import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DeleteDuplicatePictures {

    public static String folderPath = "";

    public static void main(String args[]) {
        removeDuplicates(Path.of(folderPath));
    }

    public static void removeDuplicates(Path targetFolder) {
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(targetFolder)) {
            Set<String> seen = new HashSet<>();
            for (Path path : paths) {
                if (Files.isDirectory(path)) continue;
                if (!seen.add(getHash(path))) {
                    Files.delete(path);
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getHash(Path path) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(Files.readAllBytes(path));
        return toHexadecimal(md.digest());
    }

    public static String toHexadecimal(byte[] bytes) {
        return IntStream.range(0, bytes.length)
                .mapToObj(i -> String.format("%02x", bytes[i]))
                .collect(Collectors.joining());
    }
}
