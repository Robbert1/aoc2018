import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AoC_4 {

    private static final int MINUTES_PER_HOUR = 60;

    public static void main(String... args) throws IOException {
        final Map<Integer, List<SleepPeriod>> sleepById = Files.lines(Paths.get("input4.txt")).sorted()
                .map(GuardChange::fromInput)
                .collect(ArrayList::new, new SleepCollector()::accumulator, List::addAll).stream()
                .collect(Collectors.groupingBy(SleepPeriod::getId));

        System.out.println(guardWithMostSleep(sleepById));
        System.out.println(guardWithMostConsistentNap(sleepById));
    }

    static int guardWithMostSleep(final Map<Integer, List<SleepPeriod>> sleepById) {
        final int guardWithMostSleep = sleepById.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue().stream().mapToInt(SleepPeriod::duration).sum()))
                .max(Comparator.comparing(Pair::getValue)).map(Pair::getKey).get();

        return highestCountAtMinute(sleepById.get(guardWithMostSleep)).getKey() * guardWithMostSleep;
    }

    static int guardWithMostConsistentNap(final Map<Integer, List<SleepPeriod>> sleepById) {
        return sleepById.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), highestCountAtMinute(e.getValue()).getValue()))
                .max(Comparator.comparing(Pair::getValue))
                .map(e -> e.getKey() * highestCountAtMinute(sleepById.get(e.getKey())).getKey()).get();
    }

    private static Pair<Integer, Integer> highestCountAtMinute(List<SleepPeriod> sleepPeriods) {
        return IntStream.range(0, MINUTES_PER_HOUR)
                .mapToObj(minute -> new Pair<>(minute, (int) sleepPeriods.stream().filter(p -> p.asleepAt(minute)).count()))
                .max(Comparator.comparing(Pair::getValue)).get();
    }
}

class SleepCollector {
    GuardChange shiftStart;
    GuardChange previous;

    void accumulator(List<SleepPeriod> sleep, GuardChange change) {
        if (change.id != -1) {
            shiftStart = change;
        } else if (change.asleep) {
            previous = change;
        } else {
            sleep.add(new SleepPeriod(shiftStart.id, previous.minute, change.minute));
        }
    }
}

class SleepPeriod {
    final int id;
    final int start;
    final int end;

    SleepPeriod(final int id, final int start, final int end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    int duration() {
        return end - start;
    }

    boolean asleepAt(int minute) {
        return minute >= start && minute < end;
    }

    int getId() {
        return id;
    }
}

class GuardChange {
    final int id;
    final int minute;
    final boolean asleep;

    GuardChange(final int id) {
        this.id = id;
        this.minute = -1;
        this.asleep = false;
    }

    GuardChange(final boolean asleep, int minute) {
        this.id = -1;
        this.asleep = asleep;
        this.minute = minute;
    }

    static GuardChange fromInput(String input) {
        String[] splitted = input.split("[:\\] #]+");
        if (splitted.length == 7) {
            return new GuardChange(Integer.parseInt(splitted[4]));
        } else {
            return new GuardChange(splitted[4].equals("asleep"), Integer.parseInt(splitted[2]));
        }
    }
}