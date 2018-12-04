import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AoC_1_2 {

    public static void main(String... args) throws IOException {
        System.out.println(frequency(Files.lines(Paths.get("input1.txt"))
                .map(Integer::parseInt).collect(Collectors.toList())));
    }

    private static int frequency(List<Integer> input) {
        Set<Integer> frequencies = new HashSet<>(2000);
        int frequency = 0;
        frequencies.add(frequency);
        while (true) {
            for (Integer delta : input) {
                frequency += delta;
                if (!frequencies.add(frequency)) {
                    return frequency;
                }
            }
        }
    }
}
