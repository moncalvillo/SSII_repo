package src.main.java.pai1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

public class IntegrityChecker {

    public static void bfsIterate(Node root, Function<Node, Boolean> checkNodeIntegrity) {
        Queue<Node> queue = new LinkedList<>();

        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            Boolean isOk = checkNodeIntegrity.apply(node);
            if (isOk) {
                continue;
            }

            Node left = node.getLeft();
            Node right = node.getRight();

            if (left != null) {
                queue.add(left);
            }

            if (right != null) {
                queue.add(right);
            }
        }

    }

    public static void processProgress(Node node, Boolean isOk, IntegrityProgress integrityProgress) {
        IntegrityResult result = new IntegrityResult(node, isOk);
        integrityProgress.getResults().add(result);
        if (integrityProgress.isNewProgress()) {
            integrityProgress.showProgress();
        }
    }
}
