import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Node {

    private String id;

    private String hashFile;

    private Node left;

    private Node right;


    private Node(Node left, Node right) {
        calculateDigest(left, right);
        this.left = left;
        this.right = right;
    }

    private Node(String id, String hashFile) {
        this.id = id;
        this.hashFile = hashFile;
    }

    public static Node of(Node left, Node right) {
        return new Node(left, right);
    }

    public static Node of(String id, String hashFile) {
        return new Node(id, hashFile);
    }

    private void calculateDigest(Node left, Node right) {

        try {
            String hashCombination = "";
            String id = "";

            if (left != null) {
                hashCombination += left.getHashFile();
                id += left.getId();
            }

            if (right != null) {
                hashCombination += right.getHashFile();
                id += right.getId();
            }

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.reset();
            md.update(hashCombination.getBytes());
            String hashFile = Node.toHex(md.digest());

            md.reset();
            md.update(id.getBytes());
            String idHash = Node.toHex(md.digest());

            this.id = idHash;
            this.hashFile = hashFile;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public Boolean isLeaf() {
        return left == null && right == null;
    }

    public String getId() {
        return id;
    }

    public String getHashFile() {
        return hashFile;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hashFile == null) ? 0 : hashFile.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (hashFile == null) {
            if (other.hashFile != null)
                return false;
        } else if (!hashFile.equals(other.hashFile))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (left == null) {
            if (other.left != null)
                return false;
        } else if (!left.equals(other.left))
            return false;
        if (right == null) {
            if (other.right != null)
                return false;
        } else if (!right.equals(other.right))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.s("");
    }

    private String s(String a) {
        return a +
                this.id + "\n"
                + (left != null ? left.s(a + "- - - ") : a + "- - - "+"NULL\n")
                + (right != null ? right.s(a + "- - - ") :a + "- - - "+"NULL\n");
    }

}
