import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AoC_7 {

    public static void main(String... args) throws IOException {
        final List<String> input = Files.lines(Paths.get("input7.txt")).collect(Collectors.toList());

        //BFGKNRTWXIHPUMLQVZOYJACDSE
        System.out.println(ordered(new LinkedHashSet<>(), requirements(input)));

        //1163
        System.out.println(totalCount(requirements(input)));
    }

    private static Map<Character, Set<Character>> requirements(final List<String> input) {
        final Map<Character, Set<Character>> requirements = new HashMap<>();
        input.stream()
                .map(line -> line.split("([a-z ]+)"))
                .forEach(splitted -> requirements.computeIfAbsent(splitted[2].charAt(0), k -> new HashSet<>()).add(splitted[1].charAt(0)));
        return requirements;
    }

    private static int totalCount(final Map<Character, Set<Character>> reqs) {
        final Set<Integer> used = new HashSet<>();
        final AtomicInteger count = new AtomicInteger();
        final Map<Integer, Pair<AtomicInteger, AtomicInteger>> workers = new HashMap<>();
        IntStream.range(0, 5).forEach(i -> workers.put(i, new Pair<>(new AtomicInteger(), new AtomicInteger())));

        while (!reqs.isEmpty()) {
            final int current = count.getAndIncrement();
            IntStream.range(0, 5).forEach(i -> assignWork(reqs, i+1, workers.get(i).getKey(), workers.get(i).getValue(), used, current));
        }
        return workers.values().stream().map(Pair::getKey).mapToInt(AtomicInteger::get).max().getAsInt();
    }

    private static void assignWork(final Map<Character, Set<Character>> reqs, final int worker, final AtomicInteger threshold, final AtomicInteger currentChar, final Set<Integer> used, final int current) {
        if (current >= threshold.get()) {
            // work done, remove char from requirements
            Set<Character> toRemove = new HashSet<>();
            reqs.forEach((k, before) -> {
                before.remove((char)currentChar.get());
                if (before.isEmpty()) {
                    toRemove.add(k);
                }
            });
            toRemove.forEach(reqs::remove);

            // worker free again, try to load new work
            freeChar(used, reqs).ifPresent(freeChar -> {
                System.out.println(String.format("worker %d takes %c at second %d", worker, freeChar, current));
                currentChar.set(freeChar);
                threshold.set(current + freeChar - 4);
                used.add(freeChar);
            });
        }
    }

    private static OptionalInt freeChar(final Set<Integer> used, final Map<Character, Set<Character>> reqs) {
        return IntStream.rangeClosed('A', 'Z')
                .filter(i -> !used.contains(i))
                .filter(i -> !reqs.containsKey((char)i))
                .min();
    }

    private static String ordered(final Set<Character> used, final Map<Character, Set<Character>> reqs) {
        if (reqs.isEmpty()) {
            used.add(getNext(used, reqs));
            return used.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
        }

        char next = getNext(used, reqs);
        reqs.forEach((k, before) -> before.remove(next));
        used.add(next);

        return ordered(used, reqs.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private static char getNext(final Set<Character> used, final Map<Character, Set<Character>> reqs) {
        return (char) IntStream.rangeClosed('A', 'Z')
                    .filter(i -> !used.contains((char)i))
                    .filter(i -> !reqs.containsKey((char)i))
                    .min()
                    .getAsInt();
    }
}