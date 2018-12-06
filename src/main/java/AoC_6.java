import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.out;

public class AoC_6 {

    public static final int THRESHOLD = 10000;

    public static void main(String... args) throws IOException {
        //3871
        //44667 needs -Xss5m to work ;)
        final List<String> input = Files.lines(Paths.get("input6.txt")).collect(Collectors.toList());
        out.println(largestArea(input));
        out.println(largestCommonArea(input));
    }

    private static int largestCommonArea(final List<String> input) {
        final List<Coord> coords = input.stream().map(Coord::fromInput).collect(Collectors.toList());
        final int maxX = coords.stream().mapToInt(Coord::getX).max().getAsInt() + 1;
        final int maxY = coords.stream().mapToInt(Coord::getY).max().getAsInt() + 1;

        final int[][] totalDistances = new int[maxX][maxY];
        IntStream.range(0, maxX)
                .forEach(xPos -> IntStream.range(0, maxY)
                        .forEach(yPos -> IntStream.range(0, coords.size())
                                .forEach(i -> totalDistances[xPos][yPos] += coords.get(i).manhattanDistance(xPos, yPos))));

        final int[][] counted = new int[maxX][maxY];
        return IntStream.range(0, maxX)
                .map(xPos -> IntStream.range(0, maxY)
                        .map(yPos -> size(counted, totalDistances, xPos, yPos))
                        .max().getAsInt())
                .max().getAsInt();
    }

    private static int size(final int[][] counted, final int[][] totalDistances, int x, int y) {
        int size = 0;
        if (counted[x][y] == 0 && totalDistances[x][y] < THRESHOLD) {
            counted[x][y]++;
            size++;
            if (x < counted.length - 1)
                size += size(counted, totalDistances, x + 1, y);
            if (y < counted[0].length - 1)
                size += size(counted, totalDistances, x, y + 1);
            if (x > 1)
                size += size(counted, totalDistances, x - 1, y);
            if (y > 1)
                size += size(counted, totalDistances, x, y - 1);
        }
        return size;
    }

    private static int largestArea(final List<String> input) {
        final List<Coord> coords = input.stream().map(Coord::fromInput).collect(Collectors.toList());
        final int maxX = coords.stream().mapToInt(Coord::getX).max().getAsInt() + 1;
        final int maxY = coords.stream().mapToInt(Coord::getY).max().getAsInt() + 1;
        final int[][][] claims = new int[maxX][maxY][input.size()];
        final int[][] owners = new int[maxX][maxY];
        //todo remove the need for this to prevent coords[0] getting to much
        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                owners[i][j] = -1;
            }
        }

        IntStream.range(0, maxX)
                .forEach(xPos -> IntStream.range(0, maxY)
                        .forEach(yPos -> IntStream.range(0, coords.size())
                                .forEach(i -> claims[xPos][yPos][i] = coords.get(i).manhattanDistance(xPos, yPos))));

        IntStream.range(0, maxX)
                .forEach(xPos -> IntStream.range(0, maxY)
                        .forEach(yPos -> IntStream.range(0, coords.size()).mapToObj(i -> new Pair<>(i, claims[xPos][yPos][i]))
                                .filter(p -> IntStream.range(0, coords.size()).filter(i -> i != p.getKey())
                                        .map(i -> claims[xPos][yPos][i])
                                        .min().getAsInt() > p.getValue())
                                .findFirst()
                                .ifPresent(p -> owners[xPos][yPos] = p.getKey())));


        final List<Integer> infiniteAreas = IntStream.concat(
                IntStream.of(0, maxX - 1).flatMap(xBorder -> IntStream.range(0, maxY).map(yPos -> owners[xBorder][yPos]).distinct()),
                IntStream.of(0, maxY - 1).flatMap(yBorder -> IntStream.range(0, maxX).map(xPos -> owners[xPos][yBorder]).distinct()))
                .distinct().boxed().collect(Collectors.toList());

        return IntStream.range(0, coords.size())
                .filter(i -> !infiniteAreas.contains(i))
                .map(i -> IntStream.range(0, maxX)
                        .map(xPos -> (int) IntStream.range(0, maxY)
                                .filter(yPos -> owners[xPos][yPos] == i)
                                .count())
                        .sum()).max().getAsInt();
    }
}

class Coord {
    int x, y;

    Coord(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    int manhattanDistance(int otherX, int otherY) {
        return Math.abs(x - otherX) + Math.abs(y - otherY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    static Coord fromInput(String input) {
        final String[] coords = input.split(", ");
        return new Coord(Integer.parseInt(coords[0]) - 1, Integer.parseInt(coords[1]) - 1);
    }
}