package tree.SegmentTree;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ImplicitSegmentTree
 *
 * Similar to static segment tree, we create nodes only when required, that’s
 * why it is also known as dynamic segment tree.
 *
 * increment - creates interval nodes for the interval and increments the delta.
 * if intersects hi+lo / 2 intervals then we create.
 *
 * TODO
 * impl w/o lazy prop
 *
 * TODO
 * create a version where we return a node not just AGR_FUN value
 * check this out https://cp-algorithms.com/graph/lca.html
 */
public class ImplicitSegmentTree implements SegmentTreeQuery {

    private final Node root;
    private final AggregateFunction af;
    private final int n;

    public ImplicitSegmentTree(int n) {
        this.n = n;
        root = new Node(0, n);
        af = AggregateFunction.MIN;
    }

    public ImplicitSegmentTree(int n, AggregateFunction aggregateFunction) {
        this.n = n;
        root = new Node(0, n);
        af = aggregateFunction;
    }

    /**************************************************************************/

    public void increment(int a, int b, int val) {
        increment(root, a, b, val);
    }

    public void increment(Node cur, int a, int b, int val) {
        if (notIntersects(cur, a, b)) {
            return;
        }

        if (covers(cur, a, b)) {
            cur.delta += val;
            return;
        }

        int mid = cur.lo + (cur.hi - cur.lo) / 2;
        if (cur.left == null) cur.left = new Node(cur.lo, mid);
        if (cur.right == null) cur.right = new Node(mid + 1, cur.hi);

        propagate(cur);

        increment(cur.left, a, b, val);
        increment(cur.right, a, b, val);

        // after children have been updated - we can use up-to-date data from them
        update(cur);
    }

    /**************************************************************************/

    public int min(int a, int b) {
        return min(root, a, b);
    }

    // TODO - get index of the minimum
    public int getMinIndex(int a, int b) {
        return -1;
    }

    private int min(Node cur, int lo, int hi) {
        if (notIntersects(cur, lo, hi)) {
            return Integer.MAX_VALUE;
        }

        if (covers(cur, lo, hi)) {
            return cur.delta + cur.min;
        }

        int mid = cur.lo + (cur.hi - cur.lo) / 2;
        if (cur.left == null) cur.left = new Node(cur.lo, mid);
        if (cur.right == null) cur.right = new Node(mid + 1, cur.hi);

        propagate(cur);

        int left = min(cur.left, lo, hi);
        int right = min(cur.right, lo, hi);

        update(cur);

        return Math.min(left, right);
    }

    /**************************************************************************/

    public int max(int a, int b) {
        return max(root, a, b);
    }

    private int max(Node cur, int a, int b) {
        if (notIntersects(cur, a, b)) return Integer.MIN_VALUE;
        if (covers(cur, a, b)) return cur.delta + cur.max;

        int mid = cur.lo + (cur.hi - cur.lo) / 2;
        if (cur.left == null) cur.left = new Node(cur.lo, mid);
        if (cur.right == null) cur.right = new Node(mid + 1, cur.hi);

        propagate(cur);

        int left = max(cur.left, a, b);
        int right = max(cur.right, a, b);

        update(cur);

        return Math.max(left, right);
    }

    /**************************************************************************/

    public int sum(int lo, int hi) {
        return sum(root, lo, hi);
    }

    private int sum(Node cur, int lo, int hi) {
        if (notIntersects(cur, lo, hi)) return 0;
        if (covers(cur, lo, hi)) return cur.val + cur.delta;

        int mid = cur.lo + (cur.hi - cur.lo) / 2;
        if (cur.left == null) cur.left = new Node(cur.lo, mid);
        if (cur.right == null) cur.right = new Node(mid + 1, cur.hi);

        propagate(cur);

        int left = sum(cur.left, lo, hi);
        int right = sum(cur.right, lo, hi);

        update(cur);

        return left + right;
    }

    /**************************************************************************/

    private boolean notIntersects(Node cur, int lo, int hi) {
        return lo > cur.hi || hi < cur.lo;
    }

    private boolean covers(Node root, int a, int b) {
        return a <= root.lo && root.hi <= b;
    }

    private void propagate(Node cur) {
        cur.left.delta += cur.delta;
        cur.right.delta += cur.delta;
        cur.delta = 0;
    }

    private void update(Node cur) {
        if (af == AggregateFunction.MIN) {
            cur.min = Math.min(cur.left.min + cur.left.delta, cur.right.min + cur.right.delta);
        } else if (af == AggregateFunction.MAX) {
            cur.max = Math.max(cur.left.max + cur.left.delta, cur.right.max + cur.right.delta);
        } else if (af == AggregateFunction.SUM) {
            cur.val = cur.left.val + cur.left.delta + cur.right.val + cur.right.delta;
        }
    }

    /**************************************************************************/

    public void print() {
        print(root.lo, root.hi);
        System.out.println("=============");
    }

    public void print(int a, int b) {
        Deque<Node> q = new ArrayDeque<>();
        q.add(root);
        int level = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            while (--size >= 0) {
                Node cur = q.removeFirst();
                System.out.print("level: " + level + ", ");
                System.out.print("ind: [" + cur.lo + ", " + cur.hi + "] ");
                System.out.print("delta: " + cur.delta + ", ");
                System.out.print("min: " + cur.min + ", ");
                System.out.print("max: " + cur.max + ", ");
                System.out.print("sum: " + cur.val + " | ");
                if (cur.left != null) q.addLast(cur.left);
                if (cur.right != null) q.addLast(cur.right);
            }
            level++;
            System.out.println();
        }
    }

    public void fillFromAr(int[] a) {
        for (int i = 0; i < a.length; i++) {
            this.increment(i, i, a[i]);
        }
    }

    public Node getRoot() {
        return root;
    }

    public int getN() {
        return n;
    }

    /**************************************************************************/

    public static class Node {
        int lo, hi;
        int delta, val, min, max;
        Node left, right;

        private Node(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
        }
    }
}

