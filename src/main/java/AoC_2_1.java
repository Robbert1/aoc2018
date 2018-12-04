import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AoC_2_1 {

    public static void main(String... args) throws IOException {
        System.out.println(checksum(Files.lines(Paths.get("input2.txt"))));
    }

    private static int checksum(final Stream<String> ids) {
        final AtomicInteger doubleOccurrences = new AtomicInteger(0);
        final AtomicInteger tripleOccurrences = new AtomicInteger(0);

        ids.forEach(id -> {
            possibleChars()
                    .filter(query -> id.chars().filter(c -> c == query).count() == 2)
                    .findAny()
                    .ifPresent((f) -> doubleOccurrences.incrementAndGet());

            possibleChars()
                    .filter(query -> id.chars().filter(c -> c == query).count() == 3)
                    .findAny()
                    .ifPresent((f) -> tripleOccurrences.incrementAndGet());
        });
        return doubleOccurrences.get() * tripleOccurrences.get();
    }

    private static IntStream possibleChars() {
        return IntStream.range(97, 123);
    }
}
