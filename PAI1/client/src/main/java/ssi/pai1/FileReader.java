package ssi.pai1;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {

    public static List<File> readFiles(String[] filePaths) {
        return Stream.of(filePaths)
                .map(Path::of)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }
}
