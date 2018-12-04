import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class AoC_3_1 {

    public static void main(String... args) throws IOException {
        System.out.println(overlappingInches(Files.lines(Paths.get("input3.txt")).collect(Collectors.toList())));
    }

    private static int overlappingInches(final List<String> claimRecords) {
        final List<Claim> claims = claimRecords.stream()
                .map(Claim::fromInput)
                .collect(Collectors.toList());

        return contested(fabric(claims));
    }

    private static int contested(final int[][] fabric) {
        int contested = 0;
        for (int i = 0; i < 1500; i++) {
            for (int j = 0; j < 1500; j++) {
                if (fabric[i][j] > 1) {
                    contested++;
                }
            }
        }
        return contested;
    }

    private static int[][] fabric(final List<Claim> claims) {
        int[][] fabric = new int[1500][1500];
        for (Claim claim : claims) {
            int offsetX = claim.xPos;
            int offsetY = claim.yPos;
            for (int i = 0; i < claim.width; i++) {
                for (int j = 0; j < claim.height; j++) {
                    fabric[offsetX+i][offsetY+j]++;
                }
            }
        }
        return fabric;
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
    }
}