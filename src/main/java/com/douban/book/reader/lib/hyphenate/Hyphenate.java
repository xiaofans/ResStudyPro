package com.douban.book.reader.lib.hyphenate;

import com.douban.book.reader.util.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Hyphenate {
    public static final String TAG = "Hyphenate";
    private static Map<String, ArrayList<Integer>> sExceptionsMap = null;
    private static Tree sHyphenTree = null;

    private static class Node {
        public ArrayList<Node> children = new ArrayList();
        public String data;
        public Node parent;

        public Node(String data) {
            this.data = data;
        }

        public Node createChildNode(Node childNode) {
            childNode.parent = this;
            this.children.add(childNode);
            return childNode;
        }
    }

    private static class Tree {
        private Node root;

        public Node getRoot() {
            return this.root;
        }

        public void setRoot(Node root) {
            this.root = root;
        }

        public Tree(String rootData) {
            this.root = new Node(rootData);
        }
    }

    public static void initPatterns() {
        makeExceptionMap();
        makePatterns();
    }

    public static ArrayList<String> hyphenateWord(String originalWord) {
        initPatterns();
        ArrayList<String> result = new ArrayList();
        if (originalWord.length() <= 4) {
            result.add(originalWord);
        } else {
            int[] points;
            int i;
            int j;
            String word = originalWord.toLowerCase();
            if (sExceptionsMap.containsKey(word)) {
                ArrayList<Integer> pieces = (ArrayList) sExceptionsMap.get(word);
                points = new int[pieces.size()];
                for (i = 0; i < points.length; i++) {
                    points[i] = ((Integer) pieces.get(i)).intValue();
                }
            } else {
                word = "." + word + ".";
                points = new int[(word.length() + 1)];
                for (i = 0; i < word.length(); i++) {
                    Node currentNode = sHyphenTree.getRoot();
                    String partWord = word.substring(i);
                    for (j = 0; j < partWord.length(); j++) {
                        String c = partWord.substring(j, j + 1);
                        boolean flag = false;
                        ArrayList<Node> childrenNodes = currentNode.children;
                        for (int k = 0; k < childrenNodes.size(); k++) {
                            Node tempNode = (Node) childrenNodes.get(k);
                            if (tempNode.data.equals(c)) {
                                currentNode = tempNode;
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            break;
                        }
                        ArrayList<Node> testFinalNodes = currentNode.children;
                        for (int m = 0; m < testFinalNodes.size(); m++) {
                            Node tmpNode = (Node) testFinalNodes.get(m);
                            if (tmpNode.data.contains(", ")) {
                                String[] strArr = tmpNode.data.split(", ");
                                int[] intArr = new int[strArr.length];
                                for (int n = 0; n < intArr.length; n++) {
                                    intArr[n] = Integer.parseInt(strArr[n]);
                                    points[i + n] = Math.max(points[i + n], intArr[n]);
                                }
                            }
                        }
                    }
                }
                points[1] = 0;
                points[2] = 0;
                int len = points.length;
                points[len - 2] = 0;
                points[len - 3] = 0;
            }
            int lastj = 0;
            i = 2;
            j = 0;
            while (i < points.length && j < originalWord.length()) {
                if (points[i] % 2 == 1) {
                    result.add(originalWord.substring(lastj, j + 1));
                    lastj = j + 1;
                }
                i++;
                j++;
            }
            result.add(originalWord.substring(lastj));
        }
        return result;
    }

    private static void makeExceptionMap() {
        if (sExceptionsMap == null) {
            HashMap<String, ArrayList<Integer>> map = new HashMap();
            for (String exception : Patterns.exceptions) {
                String ex = exception.replace("-", "");
                String[] chars = exception.split("[a-z]", -1);
                ArrayList<Integer> points = new ArrayList();
                points.add(Integer.valueOf(0));
                for (String equals : chars) {
                    if (equals.equals("-")) {
                        points.add(Integer.valueOf(1));
                    } else {
                        points.add(Integer.valueOf(0));
                    }
                }
                map.put(ex, points);
            }
            sExceptionsMap = map;
        }
    }

    private static void makePatterns() {
        if (sHyphenTree == null) {
            long before = System.currentTimeMillis();
            Tree hyphenateTree = new Tree("@");
            for (String pattern : Patterns.patterns) {
                int i;
                String chars = pattern.replaceAll("[0-9]", "");
                String[] strs = pattern.split("[.a-z]", -1);
                int[] points = new int[strs.length];
                for (i = 0; i < strs.length; i++) {
                    if (strs[i].equals("")) {
                        points[i] = 0;
                    } else {
                        points[i] = Integer.parseInt(strs[i]);
                    }
                }
                Node currentNode = hyphenateTree.getRoot();
                boolean flag = false;
                for (i = 0; i < chars.length(); i++) {
                    String charStr = chars.substring(i, i + 1);
                    ArrayList<Node> childrenNodes = currentNode.children;
                    for (int j = 0; j < childrenNodes.size(); j++) {
                        Node tempNode = (Node) childrenNodes.get(j);
                        if (tempNode.data.equals(charStr)) {
                            currentNode = tempNode;
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        Node newNode = new Node(charStr);
                        currentNode.createChildNode(newNode);
                        currentNode = newNode;
                    }
                    flag = false;
                }
                Node leafNode = new Node(Arrays.toString(points));
                currentNode.createChildNode(leafNode);
                leafNode.data = leafNode.data.substring(1, leafNode.data.length() - 1);
            }
            Logger.d(TAG, "make pattern elapsed: " + (System.currentTimeMillis() - before), new Object[0]);
            sHyphenTree = hyphenateTree;
        }
    }
}
