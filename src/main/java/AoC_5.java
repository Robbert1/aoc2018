import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class AoC_5 {

    public static void main(String... args) throws IOException {
        //9526
        //6694
        String polymer = Files.lines(Paths.get("input5.txt")).findFirst().get();
        System.out.println(polymerLengthAfterReaction(polymer));
        System.out.println(shortestPolymerLength(polymer));
    }

    private static int polymerLengthAfterReaction(String polymer) {
        String newPolymer;
        while (!(newPolymer = react(polymer)).equals(polymer)) {
            polymer = newPolymer;
        }
        return polymer.length();
    }

    private static int shortestPolymerLength(String polymer) {
        return IntStream.range('A', 'Z')
                .mapToObj(i -> polymer.chars()
                        .filter(c -> c != i && c != i + 32)
                        .collect(StringBuilder::new, (sb, v) -> sb.append((char)v), StringBuilder::append).toString())
                //.mapToObj(i -> polymer.replaceAll(String.format("[%c%c]", i, i + 32), ""))
                .mapToInt(AoC_5::polymerLengthAfterReaction)
                .min().getAsInt();
    }

    private static String react(final String polymer) {
        final char[] chars = polymer.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1 && match(chars[i], chars[i + 1])) {
                i++;
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    static boolean match(char c1, char c2) {
        return Math.abs(c1 - c2) == 32;
    }
}