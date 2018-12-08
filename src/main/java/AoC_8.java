import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AoC_8 {

    public static void main(String... args) throws IOException {
        final String[] values = Files.lines(Paths.get("input8.txt")).findFirst().get().split(" ");

        //40977
        System.out.println(Node.createNode(Stream.of(values).mapToInt(Integer::parseInt).iterator()).metadataSum());

        //27490
        System.out.println(Node.createNode(Stream.of(values).mapToInt(Integer::parseInt).iterator()).metadataValue());
    }
}

class Node {
    List<Integer> metadata = new ArrayList<>();
    List<Node> childs = new ArrayList<>();

    int metadataSum() {
        return metadata.stream().mapToInt(Integer::intValue).sum() + childs.stream().mapToInt(Node::metadataSum).sum();
    }

    int metadataValue() {
        final int childSize = childs.size();
        if (childSize == 0) {
            return metadata.stream().mapToInt(Integer::intValue).sum();
        } else {
            return metadata.stream().map(i -> i - 1).filter(i -> i < childSize).map(childs::get).mapToInt(Node::metadataValue).sum();
        }
    }

    static Node createNode(PrimitiveIterator.OfInt input) {
        final Node node = new Node();
        final int childNodes = input.nextInt();
        final int metadataSize = input.nextInt();
        IntStream.range(0, childNodes)
                .forEach(i -> node.childs.add(createNode(input)));
        IntStream.range(0, metadataSize)
                .forEach(i -> node.metadata.add(input.nextInt()));
        return node;
    }
}