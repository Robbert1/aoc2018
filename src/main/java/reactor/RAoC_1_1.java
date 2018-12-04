package reactor;

import reactor.core.publisher.Flux;
import reactor.math.MathFlux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RAoC_1_1 {

    public static void main(String... args) throws IOException {
        System.out.println(MathFlux.sumInt(
                Flux.fromStream(Files.lines(Paths.get("input1.txt")))
                        .map(Integer::parseInt)).block());
    }
}
