import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AoC_1_1 {

    public static void main(String... args) throws IOException {
        Path mappingPath = Paths.get("input1.txt");

        final int result = Files.lines(mappingPath)
                .map(Integer::parseInt)
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println(result);
    }
}
