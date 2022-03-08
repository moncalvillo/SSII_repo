import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeBuilder {

    public static void main(String[] args) {
        String[] paths = { "./README.md",
                "D:\\Projects\\SSI\\test\\asd.txt",
                "D:\\Projects\\SSI\\test\\e.txt",
                "D:\\Projects\\SSI\\test\\eee.txt",
                "D:\\Projects\\SSI\\test\\tt.txt" };
        Node root = TreeBuilder.buildTree(paths);
        System.out.println(root);
        Map<String, String> db = new HashMap<>();
        db.put("C4436A79C581785B01F17CF34925215200AF7F39DA59E3E6C5A5ED6282254772",
                "3C1303C9093FBB99CE0797A9F0ECD1AC4C15AE4D4C31D5888C39CF4DFAA02ACF");
        db.put("82D629AC1E99BC5FB4D1F90178AE3D9C1B80F130AB3001EA9168778161F37C61",
                "D3A2720BC8989793703266718D772CDE4D06CCE7B657D5C5ADBF7F1ED9C9754B");
        db.put("DD848969A28AC59D401296E864365BD742D5B75D73C0EC91C5C1EB2EAB9FC264",
                "FEE8399BCF5C4A932571F20D4FFA8033413827670A1CF47C6ECCAA29D530A563");
        db.put("36E70E22147347AD19E93F9D210B2D9622C3A0BB5AED0431961CECA72B98160D",
                "8D2269E751B7F2BC316B82A80A2270D64361593E28D98843FA640D5DBE1F3DC8");
        db.put("3025D3BA37E099E79E6E4D2876DFA5C45C778643FF93A3F03A1525D3969B40DA",
                "8E151200E2C4CE846D90FD2D170612FD7059240CD382CAA974E9C7204CE53DBF");
        db.put("F79D2BE4E288EC2A76EBE9E21DDF6EE0427A6FEBB122E24DB3847A594D3784C1",
                "7D1CEC6DAF8E7C41C2FCF608E22DFEE880E6FB2178C5B6991F9D5FF75E2817CB");
        db.put("./README.md", "5374447717F5FB01FFEA63984964F2917A644557861D3E7C859B6AA834ADC81F");
        db.put("D:\\Projects\\SSI\\test\\asd.txt", "282B91E08FD50A38F030DBBDEE7898D36DD523605D94D9DD6E50B298E47844BE");
        db.put("D:\\Projects\\SSI\\test\\e.txt", "A871C47A7F48A12B38A994E48A9659FAB5D6376F3DBCE37559BCB617EFE8662D");
        db.put("D:\\Projects\\SSI\\test\\eee.txt", "282B91E08FD50A38F030DBBDEE7898D36DD523605D94D9DD6E50B298E47844BE");
        db.put("D:\\Projects\\SSI\\test\\tt.txt", "no-file");

        IntegrityChecker.bfsIterate(root, (node) -> {
            String verifiedHash = db.get(node.getId());
            Boolean equals = verifiedHash.equals(node.getHashFile());
            if (!equals && node.isLeaf()) {
                System.out.println("Node: " + node.getId() + " exected: " + verifiedHash + " got: " + node.getHashFile()
                        + " isOc: " + equals);
            }

            return equals;
        });
    }

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

        while (nodes.size() > 1) {
            parentNodes = new ArrayList<>();
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

        return parentNodes.get(0);
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

}
