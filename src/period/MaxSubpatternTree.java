package period;


import java.util.*;

class MaxSubpatternTree {
  class Node {
    Pattern pattern;
    int count;

    public Node(Pattern _pattern) {
      pattern = _pattern;
      count = 0;
    }
  }

  class Label {
    Character item;
    int index;

    public Label(Character _item, int _index) {
      item = _item;
      index = _index;
    }

    public boolean equals(Object o) {
      return item.equals(((Label)o).item) && index == ((Label)o).index;
    }

    public int hashCode() {
      return item.hashCode() + new Integer(index).hashCode();
    }

    public String toString() {
      return item.toString() + ":" + index;
    }
  }

  Node root;
  Hashtable links; // the keys are the edge labels, the values are the subtrees

  public MaxSubpatternTree(Pattern maxpattern) {
    root = new Node(maxpattern);
    links = new Hashtable();
  }

  // buffer functionality
  Vector buffer;
  int maxsize;
  
  public MaxSubpatternTree(Pattern maxpattern, int _maxsize) {
    root = new Node(maxpattern);
    links = new Hashtable();

    buffer = new Vector();
    maxsize = _maxsize;
  }

  public void accumulateBuffer(String items) {
    for (int i = 0; i < items.length(); i++)
      buffer.add(new Character(items.charAt(i)));

    if (buffer.size() > maxsize) {
      // empty the buffer by removing from its beggining
      // the buffer is emptied such that a period of items form a pattern that is pushed to the tree
      int period = root.pattern.size();
      while(buffer.size() >= period) {
        Pattern segment = new Pattern(period);
        for (int i = 0; i < period; i++)
          segment = segment.addItemAtIndex((Character)buffer.remove(0), i);
        Pattern hit = Pattern.intersect(root.pattern, segment);
        if (hit.lLength() > 1) insert(hit);
      }
    }
  }

  public Pattern rootNodePattern() {
    return root.pattern;
  }

  public void insert(Pattern pattern) {
    insert(pattern, 1);
  }

  public void insert(Pattern pattern, int count) {
    if (root.pattern.equals(pattern)) root.count += count;
    else {
      // locate the path to insert the pattern
      Pattern diff = Pattern.difference(root.pattern, pattern);
      int index = diff.firstNonEmptyIndex();
      Character item = diff.firstItemAtIndex(index);
      Label label = new Label(item, index);
      MaxSubpatternTree subtree = (MaxSubpatternTree)links.get(label);
      if (subtree == null) {
        subtree = new MaxSubpatternTree(root.pattern.removeItemAtIndex(item, index), maxsize);
        links.put(label, subtree);
      }
      subtree.insert(pattern, count);
    }
  }

  public void insert(MaxSubpatternTree tree) {
    Pattern hit = Pattern.intersect(root.pattern, tree.root.pattern);
    if (hit.lLength() > 1 && tree.root.count > 0) insert(hit, tree.root.count);

    for (Iterator it = tree.links.entrySet().iterator(); it.hasNext();)
      insert((MaxSubpatternTree)((Map.Entry)it.next()).getValue());
  }

  public MaxSubpatternTree updateRoot(Pattern pattern) {
    // check first if the new root pattern or the current one is empty
    if (pattern.isEmpty() || root.pattern.isEmpty()) return new MaxSubpatternTree(pattern, maxsize);

    // check if the new root pattern equals the root pattern
    if (root.pattern.equals(pattern)) return this;

    MaxSubpatternTree subtree;

    // check for the deleted items
    Pattern deleted = Pattern.difference(root.pattern, pattern);
    if (deleted.lLength() > 0) {
      // locate the subtree of the subpattern, and remove its link from the original tree
      int index = deleted.firstNonEmptyIndex();
      Character item = deleted.firstItemAtIndex(index);
      Label label = new Label(item, index);
      subtree = (MaxSubpatternTree)links.remove(label);
      // create this subtree if it's not there
      if (subtree == null)
        subtree = new MaxSubpatternTree(root.pattern.removeItemAtIndex(item, index), maxsize);
      // insert the nodes of this tree inside the subtree
      subtree.insert(this);
    } else { // inserted items
      // create an uplink from this tree to a new tree with the new pattern
      Pattern inserted = Pattern.difference(pattern, root.pattern);
      int index = inserted.firstNonEmptyIndex();
      Character item = inserted.firstItemAtIndex(index);
      Label label = new Label(item, index);
      subtree = new MaxSubpatternTree(root.pattern.addItemAtIndex(item, index), maxsize);
      subtree.links.put(label, this);
    }

    return subtree.updateRoot(pattern);
  }

  public CountTable traverse() {
    return traverse(this);
  }

  private CountTable traverse(MaxSubpatternTree tree) {
    CountTable treepatterns = new CountTable();

    // the pattern real count is its count plus all its parents counts
    treepatterns.add(root.pattern, root.count);
    Vector parents = tree.getParents(root.pattern);
    for (int i = 0; i < parents.size(); i++)
      treepatterns.add(root.pattern, ((Node)parents.get(i)).count);
    
    for (Iterator it = links.entrySet().iterator(); it.hasNext();)
      treepatterns.addAll(((MaxSubpatternTree)((Map.Entry)it.next()).getValue()).traverse(tree));

    return treepatterns;
  }

  private Vector getParents(Pattern pattern) {
    Vector parents = new Vector();

    if (root.pattern.lLength() > pattern.lLength()) {
      if (pattern.isSubpattern(root.pattern)) parents.add(root);
      for (Iterator it = links.entrySet().iterator(); it.hasNext();)
        parents.addAll(((MaxSubpatternTree)((Map.Entry)it.next()).getValue()).getParents(pattern));
    }

    return parents;
  }

  private String toString(String indent) {
    String tree = indent + root.pattern + ":" + root.count + "\n";
    for (Iterator it = links.entrySet().iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry)it.next();
      tree += indent + " " + entry.getKey() + "\n" + ((MaxSubpatternTree)entry.getValue()).toString(indent + "  ");
    }

    return tree;
  }

  public String toString() {
    return toString("");
  }
}
