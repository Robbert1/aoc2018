import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AoC_2_2 {

    public static void main(String... args) throws IOException {
        System.out.println(matchingIdChars(Files.lines(Paths.get("input2.txt")).collect(Collectors.toList())));
    }

    private static String matchingIdChars(final List<String> ids) {
        return ids.stream().flatMap(id -> {
            final char[] chars = id.toCharArray();
            return ids.stream()
                    .map(String::toCharArray)
                    .map(other -> IntStream.range(0, other.length).filter(i -> chars[i] != other[i])
                            .reduce((deltaA, deltaB) -> deltaA == deltaB ? deltaA : -1).orElse(-1))
                    .filter(deltaIndex -> deltaIndex != -1)
                    .map(deltaIndex -> id.substring(0, deltaIndex) + id.substring(deltaIndex + 1));
        }).findFirst().get();
    }
}