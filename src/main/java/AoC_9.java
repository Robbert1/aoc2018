import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class AoC_9 {

    public static final int MAX_CAPACITY = 50_000;

    public static void main(String... args) throws IOException {
        // 388844
        // 3212081616

        //TODO: rewrite to rotating ArrayDeque impl
        Files.lines(Paths.get("input9.txt")).map(MarbleGame::fromInput).map(AoC_9::highscore).forEach(System.out::println);
    }

    static long highscore(MarbleGame game) {
        final List<List<Integer>> marbles = new ArrayList<>();
        IntStream.range(0, 25).forEach(i -> marbles.add(i, new ArrayList<>(MAX_CAPACITY)));

        int index = 0, totalSize = 1;
        marbles.get(0).add(0);
        for (int i = 1; i <= game.marbles; i++) {
            final int player = (i - 1) % game.players.length;
            if (i % 23 == 0) {
                index = move(index - 7, totalSize);
                game.players[player] += i + removeMarble(index, marbles);
                totalSize--;
            } else {
                index = move(index + 2, totalSize);
                addMarble(index, i, marbles);
                totalSize++;
            }
        }
        return IntStream.range(0, game.players.length).mapToLong(i -> game.players[i]).max().getAsLong();
    }

    static void addMarble(final int index, final int marble, final List<List<Integer>> marbles) {
        final Pair<Integer, Integer> arrayAndIndex = arrayAndIndex(index, marbles);
        marbles.get(arrayAndIndex.getKey()).add(arrayAndIndex.getValue(), marble);
        if (marbles.get(arrayAndIndex.getKey()).size() >= MAX_CAPACITY) {
            divideList(marbles, arrayAndIndex.getKey());
        }
    }

    static void divideList(final List<List<Integer>> marbles, int index) {
        final List<Integer> original = marbles.remove(index);
        for (int i = 0; i < 10; i++) {
            final List<Integer> newList = new ArrayList<>(MAX_CAPACITY);
            newList.addAll(original.subList(i * (MAX_CAPACITY / 10), (i + 1) * (MAX_CAPACITY / 10)));
            marbles.add(index + i, newList);
        }
    }

    static int removeMarble(final int index, final List<List<Integer>> marbles) {
        final Pair<Integer, Integer> arrayAndIndex = arrayAndIndex(index, marbles);
        return marbles.get(arrayAndIndex.getKey()).remove((int) arrayAndIndex.getValue());
    }

    static Pair<Integer, Integer> arrayAndIndex(int index, final List<List<Integer>> marbles) {
        int arrayKey = 0;
        for (int nextSize = marbles.get(arrayKey).size(); index >= nextSize && nextSize != 0; nextSize = marbles.get(arrayKey).size()) {
            index -= nextSize;
            arrayKey++;
        }
        return new Pair<>(arrayKey, index);
    }

    static int move(int move, int max) {
        return (move <= max) ? (move < 0 ? max + move : move) : move - max;
    }
}

class MarbleGame {
    long[] players;
    int marbles;

    public MarbleGame(final int players, final int marbles) {
        this.players = new long[players];
        this.marbles = marbles;
    }

    static MarbleGame fromInput(String input) {
        final String[] gameInput = input.split(" ");
        return new MarbleGame(parseInt(gameInput[0]), parseInt(gameInput[6]));
    }
}