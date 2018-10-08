package period;


import java.util.TreeSet;

class ItemSet extends TreeSet {

  public static ItemSet intersect(ItemSet s1, ItemSet s2) {
    ItemSet s = new ItemSet();
    s.addAll(s1);
    s.retainAll(s2);

    return s;
  }

  public static ItemSet union(ItemSet s1, ItemSet s2) {
    ItemSet s = new ItemSet();
    s.addAll(s1);
    s.addAll(s2);

    return s;
  }

  public static ItemSet difference(ItemSet s1, ItemSet s2) {
    ItemSet s = new ItemSet();
    s.addAll(s1);
    s.removeAll(s2);

    return s;
  }
}
