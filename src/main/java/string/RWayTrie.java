package string;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * RWayTrie
 * (or prefix tree)
 *
 * todo: pattern search add delete op
 *
 * Reference: https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/TrieST.java
 */
public class RWayTrie {
    private static final int R = 256;
    private Node root;
    private int wordCount;

    public RWayTrie() {
    }

    public Object get(String key) {
//		return getRecursively(root, key, 0);
        return getIteratively(key);
    }

    private Object getIteratively(String key) {
        if (root == null) {
            return null;
        }

        Node currentNode = root;
        int n = key.length();

        for (int i = 0; i < n; i++) {
            char c = key.charAt(i);
            if (!currentNode.contains(c)) {
                return null;
            }
            currentNode = currentNode.get(c);
        }

        if (currentNode.isWordEnd()) {
            return currentNode.val;
        }
        return null;
    }

    public void put(String key, Object val) {
//		root = putRecursively(root, 0, key, val);
        putIteratively(key, val);
    }

    private void putIteratively(String key, Object val) {
        if (root == null) {
            root = new Node();
        }

        Node currentNode = root;
        int n = key.length();

        for (int i = 0; i < n; i++) {
            char currentChar = key.charAt(i);
            if (!currentNode.contains(currentChar)) {
                currentNode.put(currentChar);
            }
            currentNode.prefixCount++;
            currentNode = currentNode.get(currentChar);
        }

        if (!currentNode.isEnd) {
            currentNode.prefixCount++;
            currentNode.setEnd();
            wordCount++;
        }

        currentNode.setVal(val);
    }

    private Object getRecursively(Node currentNode, String key, int dist) {
        if (currentNode == null) {
            return null;
        }

        // distance from start is the word length = we traversed a whole word
        if (dist == key.length()) {
            System.out.println(currentNode.prefixCount);
            if (currentNode.isWordEnd()) {
                return currentNode.val;
            }
            return null;
        }

        char nextChar = key.charAt(dist);
        return getRecursively(currentNode.get(nextChar), key, dist + 1);
    }

    public void put(String key, int lo, int hi, Object val) {
        putIteratively(key, lo, hi, val);
    }

    private void putIteratively(String key, int lo, int hi, Object val) {
        if (root == null) {
            root = new Node();
        }

        Node currentNode = root;

        for (int i = lo; i <= hi; i++) {
            char currentChar = key.charAt(i);
            if (!currentNode.contains(currentChar)) {
                currentNode.put(currentChar);
            }
            currentNode = currentNode.get(currentChar);
        }

        if (!currentNode.isEnd) {
            currentNode.setEnd();
            wordCount++;
        }

        currentNode.setVal(val);
    }

    private Node putRecursively(Node currentNode, int count, String key, Object val) {
        if (currentNode == null) {
            currentNode = new Node();
        }
        currentNode.prefixCount++;
        if (count == key.length()) {
            currentNode.val = val;
            if (!currentNode.isEnd) {
                currentNode.isEnd = true;
                wordCount++;
            }
            return currentNode;
        }
        char c = key.charAt(count);
        currentNode.getNext()[c] = putRecursively(currentNode.getNext()[c], count + 1, key, val);
        return currentNode;
    }

    public void putSuffixes(String str) {
        int n = str.length();
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                // if (!(i==0&&j==n-1)) - do not put whole words
                put(str, i, j);
            }
        }
    }

    public void put(String key, int lo, int hi) {
        putIteratively(key, lo, hi, null);
    }

    public int getWordCount() {
        return wordCount;
    }

    public boolean prefixExists(String prefix) {
        if (root == null) {
            return false;
        }

        Node currentNode = root;
        int n = prefix.length();

        for (int i = 0; i < n; i++) {
            char c = prefix.charAt(i);
            if (!currentNode.contains(c)) {
                return false;
            }
            currentNode = currentNode.get(c);
        }

        return true;
    }

    public List<String> getWordsWithPrefix(String prefix) {
        if (root == null) return Collections.emptyList();
        Node curNode = root;
        int n = prefix.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char curChar = prefix.charAt(i);
            if (!curNode.contains(curChar)) {
                return Collections.emptyList();
            }
            sb.append(curChar);
            curNode = curNode.get(curChar);
        }
        List<String> ans = new LinkedList<>();
        gen(curNode, sb, ans);
        return ans;
    }

    private void gen(Node curNode, StringBuilder sb, List<String> ans) {
        if (curNode.isWordEnd()) {
            ans.add(sb.toString());
        }
        for (int i = 0; i < curNode.getSize(); i++) {
            char c = (char) i;
            if (!curNode.contains(c)) continue;
            sb.append(c);
            gen(curNode.get(c), sb, ans);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    public void printAllWords() {
        printAllWords(root, new StringBuilder());
    }

    public void printAllWords(Node root, StringBuilder sb) {
        if (root == null) {
            return;
        }
        if (root.isWordEnd()) {
            System.out.println(sb.toString());
        }
        for (int i = 0; i < R; i++) {
            Node next = root.getNext()[i];
            sb.append(toChar(i));
            printAllWords(next, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    private char toChar(int i) {
        return (char) i;
    }

    /**
     * R-way trie node.
     */
    private class Node {
        private Object val;
        private boolean isEnd;
        private Node[] next = new Node[R];
        private int prefixCount; // how many words have this char node

        public boolean isWordEnd() {
            return this.isEnd;
        }

        public void setEnd() {
            this.isEnd = true;
        }

        public boolean contains(char c) {
            return get(c) != null;
        }

        public boolean contains(int c) {
            return get(c) != null;
        }

        public Node get(char c) {
            return next[c];
        }

        public Node get(int c) {
            return next[c];
        }

        private void put(char currentChar) {
            next[currentChar] = new Node();
        }

        private void setVal(Object val) {
            this.val = val;
        }

        public Node[] getNext() {
            return next;
        }

        public int getSize() {
            return next.length;
        }
    }
}
