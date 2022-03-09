package ssii.pai1.integrity.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ssii.pai1.integrity.model.Node;

public class TreeBuilder {

    public static Node buildTree(String[] filePaths) {

        List<Node> nodes = TreeBuilder.parseFileNodes(filePaths);

        Node root = TreeBuilder.createTreeBottomUp(nodes);
        return root;
    }

    public static List<Node> parseFileNodes(String[] filePaths) {
        return Stream.of(filePaths)
                .map(TreeBuilder::parseFileNode)
                .collect(Collectors.toList());
    }

    public static Node createTreeBottomUp(List<Node> nodes) {
        List<Node> parentNodes = new ArrayList<>();
        Integer numberChildren = nodes.size();
        Integer total = 0;
        while (nodes.size() > 1) {
            parentNodes = new ArrayList<>();
            total += nodes.size();
            for (int i = 0; i < nodes.size(); i += 2) {
                if (i + 1 == nodes.size()) {
                    Node left = nodes.get(i);
                    Node parent = Node.of(left, null);
                    parentNodes.add(parent);
                } else {
                    Node left = nodes.get(i);
                    Node right = nodes.get(i + 1);
                    Node parent = Node.of(left, right);
                    parentNodes.add(parent);
                }
            }
            nodes = parentNodes;
        }
        total++;

        Node root = parentNodes.get(0);
        root.setNumberOfNodes(total);
        root.setNumberOfChildren(numberChildren);
        return root;
    }

    public static Node parseFileNode(String path) {
        try {

            byte[] data = Files.readAllBytes(Path.of(path));

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(data);

            String id = path;
            byte[] digest = md.digest();
            String fileHash = Node.toHex(digest);

            return Node.of(id, fileHash);
        } catch (IOException | NoSuchAlgorithmException e) {
            String id = path;

            return Node.of(id, "no-file");

        }
    }

    public static String[] getAllFilesFromDirectory(String directory) throws IOException {
        return Files.walk(Path.of(directory))
                .map(Path::toFile)
                .filter(File::isFile)
                .map(File::getAbsolutePath)
                .map(path -> path.replace("\\", "/"))
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

}
