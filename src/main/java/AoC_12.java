import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AoC_12 {
    public static final int PLANT_CHAR = 35;

    public static void main(String... args) throws IOException {
        Generation generation = new Generation(
                interpret(Files.lines(Paths.get("input12.txt")).findFirst()
                        .map(input -> input.split(" ")[2])
                        .get()),
                Files.lines(Paths.get("input12.txt"))
                        .skip(2)
                        .filter(input -> input.charAt(input.length() - 1) == PLANT_CHAR)
                        .map(input -> input.split(" ")[0])
                        .map(AoC_12::interpret)
                        .collect(Collectors.toList()));

        for (int i = 0; i < 20; i++) {
            generation = generation.mutate();
        }

        System.out.println(generation.sumPlantIndices());
    }

    static List<Boolean> interpret(String input) {
        return input.chars().mapToObj(c -> c == PLANT_CHAR).collect(Collectors.toList());
    }
}

class Generation {
    final List<Boolean> plants;
    final List<List<Boolean>> patterns;

    Generation(final List<Boolean> plants, final List<List<Boolean>> patterns) {
        this.plants = plants;
        this.patterns = patterns;
    }

    Generation mutate() {
        final List<Boolean> nextGeneration = new ArrayList<>(plants.size());
        IntStream.range(0, plants.size())
                .forEach(index -> {
                    final ArrayList<Boolean> poi = new ArrayList<>();
                    poi.add(index > 2 ? plants.get(index -2) : false);
                    poi.add(index > 1 ? plants.get(index -1) : false);
                    poi.add(plants.get(index));
                    poi.add(index < plants.size() -2 ? plants.get(index + 1) : false);
                    poi.add(index < plants.size() -3 ? plants.get(index + 2) : false);

                    if (index == plants.size() - 1) {
                        System.out.println("asdf");
                    }
                    nextGeneration.add(patterns.stream().anyMatch(poi::equals));
                });
        return new Generation(nextGeneration, this.patterns);
    }

    public int sumPlantIndices() {
        return 0;
    }
}
