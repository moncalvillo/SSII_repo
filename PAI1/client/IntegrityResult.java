public class IntegrityResult {
    Node node;
    Boolean isOk;

    public IntegrityResult(Node node, Boolean isOk) {
        this.node = node;

        this.isOk = isOk;
    }

    public Node getNode() {
        return node;
    }

    public Boolean getIsOk() {
        return isOk;
    }

}
