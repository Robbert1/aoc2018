import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class AoC_3_2 {

    public static void main(String... args) throws IOException {
        System.out.println(overlappingInches(Files.lines(Paths.get("input3.txt")).collect(Collectors.toList())));
    }

    private static int overlappingInches(final List<String> claimRecords) {
        final List<Claim> claims = claimRecords.stream()
                .map(Claim::fromInput)
                .collect(Collectors.toList());

        return claims.stream()
                .filter(claim -> claims.stream().filter(c -> c.id != claim.id).noneMatch(claim::conflicted))
                .findFirst().get().id;
    }

    static class Claim {
        final int id;
        final int xPos, yPos;
        final int width, height;

        Claim(final int id, final int xPos, final int yPos, final int width, final int height) {
            this.id = id;
            this.xPos = xPos;
            this.yPos = yPos;
            this.width = width;
            this.height = height;
        }

        static Claim fromInput(String input) {
            String[] splitted = input.split("[#x@,: ]+");
            return new Claim(parseInt(splitted[1]), parseInt(splitted[2]), parseInt(splitted[3]), parseInt(splitted[4]), parseInt(splitted[5]));
        }

        boolean conflicted(Claim claim) {
            return ((xPos < claim.xPos && xPos + width > claim.xPos) || (xPos >= claim.xPos && xPos < claim.xPos + claim.width))
                    && ((yPos < claim.yPos && yPos + height > claim.yPos) || (yPos >= claim.yPos && yPos < claim.yPos + claim.height));
        }
    }
}