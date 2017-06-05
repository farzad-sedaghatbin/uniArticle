package ir.university.toosi.wtms.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi, Mostafa Rastgar
 * @version : 0.8
 */

public class FileUtil {

    public static List<String> readDirectory(String directoryPath) {
        List<String> list = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream =
                     Files.newDirectoryStream(FileSystems.getDefault().getPath(directoryPath))) {
            for (Path path : directoryStream) {
                list.add(readFile(path.toAbsolutePath().toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }
}
