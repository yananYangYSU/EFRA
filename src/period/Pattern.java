package period;


import java.util.*;

class Pattern extends Vector {
  public Pattern() {
    super();
  }

  public Pattern(String pattern) {
    super();

    for (int i = 0; i < pattern.length(); i++) {
      ItemSet itemset = new ItemSet();
      itemset.add(new Character(pattern.charAt(i)));
      add(itemset);
    }
  }

  public Pattern(int length) {
    super();

    for (int i = 0; i < length; i++)
      add(new ItemSet());
  }

  public Vector getOnePatterns() {
    Vector onepatterns = new Vector();
    for (int i = 0; i < size(); i++)
      for (Iterator it = ((ItemSet)get(i)).iterator(); it.hasNext();) {
        Pattern pattern = new Pattern(size());
        ((ItemSet)pattern.get(i)).add(it.next());
        onepatterns.add(pattern);
      }

    return onepatterns;
  }

  public int lLength() {
    int l = 0;
    for (int i = 0; i < size(); i++)
      l += ((ItemSet)get(i)).size();

    return l;
  }

  public int firstNonEmptyIndex() {
    for (int i = 0; i < size(); i++)
      if (!((ItemSet)get(i)).isEmpty()) return i;
    return -1;
  }

  public Character firstItemAtIndex(int index) {
    return (Character)((ItemSet)get(index)).first();
  }

  public Pattern addItemAtIndex(Character item, int index) {
    Pattern p = new Pattern(size());
    for (int i = 0; i < size(); i++)
      ((ItemSet)p.get(i)).addAll((ItemSet)get(i));
    ((ItemSet)p.get(index)).add(item);

    return p;
  }

  public Pattern removeItemAtIndex(Character item, int index) {
    Pattern p = new Pattern(size());
    for (int i = 0; i < size(); i++)
      ((ItemSet)p.get(i)).addAll((ItemSet)get(i));
    ((ItemSet)p.get(index)).remove(item);

    return p;
  }

  public Pattern removeAllAtIndex(int index) {
    Pattern p = new Pattern(size());
    for (int i = 0; i < size(); i++)
      ((ItemSet)p.get(i)).addAll((ItemSet)get(i));
    ((ItemSet)p.get(index)).clear();

    return p;
  }

  public boolean isSubpattern(Pattern pattern) {
    return equals(intersect(this, pattern));
  }

  public String toString() {
    String s = "";
    for (int i = 0; i < size(); i++) {
      ItemSet itemset = (ItemSet)get(i);
      s += itemset.isEmpty() ? "*" : (itemset.size() == 1 ? itemset.first() : itemset);
    }

    return s;
  }

  public static Pattern intersect(Pattern p1, Pattern p2) {
    Pattern p = new Pattern();
    for (int i = 0; i < p1.size(); i++)
      p.add(ItemSet.intersect((ItemSet)p1.get(i), (ItemSet)p2.get(i)));

    return p;
  }

  public static Pattern union(Pattern p1, Pattern p2) {
    Pattern p = new Pattern();
    for (int i = 0; i < p1.size(); i++)
      p.add(ItemSet.union((ItemSet)p1.get(i), (ItemSet)p2.get(i)));

    return p;
  }

  public static Pattern difference(Pattern p1, Pattern p2) {
    Pattern p = new Pattern();
    for (int i = 0; i < p1.size(); i++)
      p.add(ItemSet.difference((ItemSet)p1.get(i), (ItemSet)p2.get(i)));

    return p;
  }

  public static Pattern unionAll(Collection patterns) {
    if (patterns.isEmpty()) return new Pattern();

    Iterator it = patterns.iterator();
    Pattern p = (Pattern)it.next();
    while (it.hasNext())
      p = union(p, (Pattern)it.next());

    return p;
  }
}
