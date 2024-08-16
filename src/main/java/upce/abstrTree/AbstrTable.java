package upce.abstrTree;

import java.util.Iterator;
import java.util.NoSuchElementException;
import upce.abstrTreeEnum.eTypProhl;

import upce.ads.KolekceException;


public class AbstrTable <K extends Comparable<K>, V> implements TableInterface<K, V> {

    private Node<K, V> root;

    private class Node<K, V> {
        private K key;
        private V value;
        private int n;
        private Node<K, V> right;
        private Node<K, V> left;

        public Node(K key, V value, int n) {
            this.key = key;
            this.value = value;
            this.n = n;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public Node<K, V> getRight() {
            return right;
        }

        public void setRight(Node<K, V> right) {
            this.right = right;
        }

        public Node<K, V> getLeft() {
            return left;
        }

        public void setLeft(Node<K, V> left) {
            this.left = left;
        }
    }

    @Override
    public void zrus() {
        root = null;
    }

    @Override
    public boolean jePrazdny() {
        return size() == 0;
    }

    @Override
    public void vloz(K key, V value) throws KolekceException {
        if (key == null){
            throw new NullPointerException();
        }
        if (isContained(key)) {
            throw new KolekceException();
        }
        /*
        if (value == null) {
            odeber(root, key);
        }

         */
        root = vloz(root, key, value);
    }

    @Override
    public V najdi(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return get(root, key);
    }

    @Override
    public V odeber(K key) throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException();
        }
        if (key == null) {
            throw  new NullPointerException();
        }
        V value = najdi(key);
        root = odeber(root, key);
        return value;
    }

    @Override
    public Iterator vytvorIterator(eTypProhl typ) {
        if (typ == null) {
            throw new NullPointerException();
        }
        switch (typ) {
            case DO_HLOUBKY:
                return new IteratorBFS();
            case DO_SIRZKY:
                return new IteratorDFS();
            default:
                return null;
        }
    }

    private boolean isContained(K key) {
        return najdi(key) != null;
    }

    public int size() {
        return size(root);
    }

    private int size(Node<K, V> node) {
        if (node == null) {
            return 0;
        } else {
            return node.n;
        }
    }

    private Node<K,V> vloz(Node<K, V> node, K key, V value) {
        if (node == null) {
            return new Node<>(key, value, 1);
        }
        int comaparison = key.compareTo(node.key);
        if (comaparison < 0) {
            node.left = vloz(node.left, key, value);
        }else if (comaparison > 0) {
            node.right = vloz(node.right, key, value);
        }else {
            node.value = value;
        }
        node.n = size(node.left) + size(node.right) + 1;
        return node;
    }

    private Node<K,V> odeber(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        int comaparison = key.compareTo(node.key);
        if (comaparison < 0) {
            node.left = odeber(node.left, key);
        } else if (comaparison > 0) {
            node.right = odeber(node.right, key);
        } else {
            if (node.right == null) {
                return node.left;
            }
            if (node.left == null) {
                return node.right;
            }
            Node<K, V> pom = node;
            node = min(pom.right);
            node.right = odeberMin(pom.right);
            node.left = pom.left;
        }
        node.n = size(node.left) + size(node.right) + 1;
        return node;
    }

    private V get(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        int comparison = key.compareTo(node.key);
        if (comparison < 0) {
            return get(node.left, key);
        } else if (comparison > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }



    public void odeberMin() {
        if (jePrazdny()) {
            throw new NoSuchElementException();
        }
        root = odeberMin(root);
    }



    private Node<K, V> odeberMin(Node<K, V> x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = odeberMin(x.left);
        x.n = size(x.left) + size(x.right) + 1;
        return x;
    }


    public void odeberMax() {
        if (jePrazdny()) {
            throw new NoSuchElementException();
        }
        root = odeberMax(root);
    }


    private Node<K, V> odeberMax(Node<K, V> x) {
        if (x.right == null) {
            return x.left;
        }else {
            x.right = odeberMax(x.right);
            x.n = size(x.left) + size(x.right) + 1;
            return x;
        }
    }


    public K min() {
        if (jePrazdny()) {
            return null;
        }
        return min(root).key;
    }

    private Node<K, V> min(Node<K, V> x) {
        if (x.left == null) {
            return x;
        } else {
            return min(x.left);
        }
    }

    private class IteratorBFS<T> implements Iterator<T> {
        Node<K, V> node;
        FIFO<Node<K, V>> queue;

        public IteratorBFS() {
            node = root;
            queue = new AbstrFIFO<>();
            if (root != null) {
                queue.vloz(node);
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.jePrazdny();
        }

        @Override
        public T next() {
            if (hasNext()) {
                try {
                    node = queue.odeber();
                    if (node.getLeft() != null) {
                        queue.vloz(node.getLeft());
                    }
                    if (node.getRight() != null) {
                        queue.vloz(node.getRight());
                    }
                } catch (KolekceException exception) {
                    exception.printStackTrace();
                }
            } else {
                throw new NoSuchElementException();
            }
            return (T) node.getValue();
        }
    }

    private class IteratorDFS<T> implements Iterator<T> {
        Node<K, V> node;
        Node<K, V> result;
        LIFO<Node<K, V>> stack;

        public IteratorDFS() {
            node = root;
            stack = new AbstrLIFO<>();
            while (root != null) {
                stack.vloz(node);
                node = node.getLeft();
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.jePrazdny();
        }

        @Override
        public T next() {
            try {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                node = stack.odeber();
                result = node.getRight();
                if (node.getRight() != null) {
                    node = node.getRight();
                    while (node != null) {
                        stack.vloz(node);
                        node = node.getLeft();
                    }
                }
                assert result != null;
                return (T) result.getValue();
            } catch (KolekceException exception) {
                exception.printStackTrace();
            }
            return null;
        }
    }




}
