import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

public class AoC_10 {
    public static void main(String... args) throws IOException {
        //ajznxhke
        final List<Star> stars = Files.lines(Paths.get("input10.txt"))
                .map(Star::fromInput)
                .collect(Collectors.toList());

        int rounds = 0;
        while (!stars.stream().allMatch(Star::withinBounds)) {
            rounds++;
            stars.forEach(Star::move);
        }

        System.out.println(String.format("\nround %d", rounds));
        drawSky(stars);
    }

    private static void drawSky(final List<Star> stars) {
        IntStream.range(190, 205)
                .forEach(yPos -> {
                    IntStream.range(110, 200)
                            .mapToObj(xPos -> stars.stream().anyMatch(s -> s.matches(xPos, yPos)))
                            .map(hasStar -> hasStar ?  '#' : '.')
                            .forEach(System.out::print);
                    System.out.print('\n');
                });
    }
}

class Star {
    int xPos, yPos;
    int xSpeed, ySpeed;

    public Star(final int xPos, final int yPos, final int xSpeed, final int ySpeed) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    static Star fromInput(String input) {
        final String[] words = input.split("[ ,><]+");
        return new Star(parseInt(words[1]), parseInt(words[2]), parseInt(words[4]), parseInt(words[5]));
    }

    boolean withinBounds() {
        return (abs(xPos) + abs(yPos)) < 390;
    }

    boolean matches(int xPos, int yPos) {
        return this.xPos == xPos && this.yPos == yPos;
    }

    void move() {
        xPos += xSpeed;
        yPos += ySpeed;
    }
}
