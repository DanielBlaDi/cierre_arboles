import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class TreeSetAVL<T extends Comparable<T>> implements Iterable<T> {

  private Node root;
  private int size;

  public TreeSetAVL() {
    size = 0;
    root = null;
  }

  public boolean add(T val) {
    if (contains(val))
      return false;
    root = add_rec(val, root);
    size++;
    return true;
  }

  private Node add_rec(T val, Node node) {
    if (node == null)
      return new Node(val);
    if (val.compareTo(node.data) < 0)
      node.left = add_rec(val, node.left);
    else
      node.right = add_rec(val, node.right);
    node = balance(node);
    node.update_h();
    return node;
  }

  public boolean contains(T val) {
    return contains_rec(val, root);
  }

  private boolean contains_rec(T val, Node node) {
    if (node == null)
      return false;
    if (val.compareTo(node.data) == 0)
      return true;
    if (val.compareTo(node.data) < 0)
      return contains_rec(val, node.left);
    else
      return contains_rec(val, node.right);
  }

  public boolean remove(T val) {
    if (contains(val) == false)
      return false;
    root = remove_rec(val, root);
    size--;
    return true;
  }

  private Node remove_rec(T val, Node node) {
    if (val.compareTo(node.data) < 0)
      node.left = remove_rec(val, node.left);
    else if (val.compareTo(node.data) > 0)
      node.right = remove_rec(val, node.right);
    else {
      if (node.left == null && node.right == null)
        return null;
      if (node.right == null)
        return node.left;
      if (node.left == null)
        return node.right;
      Node suc = node.right;
      while (suc.left != null)
        suc = suc.left;
      node.data = suc.data;
      node.right = remove_rec(suc.data, node.right);
    }
    node = balance(node);
    node.update_h();
    return node;
  }

  public int size() {
    return size;
  }

  private void traversal(Node node, List<T> list) {
    if (node == null)
      return;
    traversal(node.left, list);
    list.add(node.data);
    traversal(node.right, list);
  }

  public String toString() {
    List<T> list = new ArrayList<T>();
    traversal(root, list);
    return list.toString();
  }

  public Iterator<T> iterator() {
    List<T> list = new ArrayList<T>();
    traversal(root, list);
    return list.iterator();
  }

  private Node balance(Node node) {
    if (Math.abs(balance_factor(node)) <= 1)
      return node;
    if (height(node.left) > height(node.right)) { // IZQ
      if (height(node.left.left) > height(node.left.right)) { // IZQ-IZQ
        return right_rotate(node);
      } else { // IZQ-DER
        node.left = left_rotate(node.left);
        return right_rotate(node);
      }
    } else { // DERECHA
      if (height(node.right.left) > height(node.right.right)) { // DER-IZQ
        node.right = right_rotate(node.right);
        return left_rotate(node);
      } else { // DER-DER
        return left_rotate(node);
      }
    }
  }

  private Node left_rotate(Node node) {
    if (node.right == null)
      return node;
    Node pivot = node.right;
    node.right = pivot.left;
    pivot.left = node;
    node.update_h();
    pivot.update_h();
    return pivot;
  }

  private Node right_rotate(Node node) {
    if (node.left == null)
      return node;
    Node pivot = node.left;
    node.left = pivot.right;
    pivot.right = node;
    node.update_h();
    pivot.update_h();
    return pivot;
  }

  private int balance_factor(Node node) {
    if (node == null)
      return 0;
    return height(node.left) - height(node.right);
  }

  private int height(Node node) {
    if (node == null)
      return -1;
    return node.h;
  }

  private class Node {
    T data;
    Node left;
    Node right;
    int h;

    Node(T d) {
      data = d;
      h = 0;
    }

    void update_h() {
      h = Math.max(height(left), height(right)) + 1;
    }
  }

}
