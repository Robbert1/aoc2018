package reactor;

import reactor.core.publisher.Flux;
import reactor.math.MathFlux;
import reactor.retry.Repeat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RAoC_1_2 {

    public static void main(String... args) throws IOException {
        final List<Integer> deltas = Files.lines(Paths.get("input1.txt"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.println(firstRepeatedFrequency(deltas));
    }

    private static int firstRepeatedFrequency(final List<Integer> deltas) {
        final AtomicInteger currentFrequency = new AtomicInteger(0);
        final Set<Integer> frequencies = new HashSet<>(2000);
        return Flux.fromIterable(deltas)
                .repeat() // back-pressured so no while(true) needed
                .map(currentFrequency::addAndGet)
                .takeUntil(frequency -> !frequencies.add(frequency))
                .blockLast();
    }
}
