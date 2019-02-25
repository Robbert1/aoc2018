import java.util.Comparator;
import java.util.stream.IntStream;

public class AoC_11 {
    public static void main(String... args) {
        int serialNumber = 8141;

        int[][] fuelCells = new int[300][300];

        //TODO: reuse small grids to create larger grids

        //235,16
        //236,227,14
        IntStream.range(0, 300)
                .forEach(xPos -> IntStream.range(0, 300)
                        .forEach(yPos -> fuelCells[xPos][yPos] = powerlevel(xPos, yPos, serialNumber)));

        IntStream.rangeClosed(1, 300).boxed()
                .map(size -> {
                    final Grid grid = IntStream.rangeClosed(0, 300 - size).boxed()
                            .flatMap(xPos -> IntStream.rangeClosed(0, 300 - size)
                                    .mapToObj(yPos -> Grid.fromInput(xPos, yPos, fuelCells, size)))
                            .max(Comparator.comparing(Grid::powerLevel)).get();
                    System.out.println(grid);
                    return grid;
                })
                .max(Comparator.comparing(Grid::powerLevel)).ifPresent(System.out::println);
    }

    static int powerlevel(int xPos, int yPos, int serial) {
        int rackID = xPos + 10;
        int powerLevel = rackID * yPos + serial;
        powerLevel *= rackID;
        powerLevel %= 1000;
        powerLevel /= 100;
        powerLevel -= 5;
        return powerLevel;
    }
}

class Grid {
    int xPos, yPos, powerLevel, size;

    public Grid(final int xPos, final int yPos, final int powerLevel, final int size) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.powerLevel = powerLevel;
        this.size = size;
    }

    static Grid fromInput(int xPos, int yPos, int[][] fuelCells, int size) {
        return new Grid(xPos, yPos, IntStream.range(0, size)
                .flatMap(xGPos -> IntStream.range(0, size)
                        .map(yGPos -> fuelCells[xPos + xGPos][yPos + yGPos]))
                .sum(), size);
    }

    public int powerLevel() {
        return powerLevel;
    }

    @Override
    public String toString() {
        return String.format("%d,%d,%d=%d", xPos, yPos, size, powerLevel);
    }
}
