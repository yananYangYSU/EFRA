package period;

import java.util.*;

class CountTable extends Hashtable {
  public void add(Object key) {
    add(key, 1);
  }

  public void add(Object key, int count) {
    count += containsKey(key) ? ((Integer)get(key)).intValue() : 0;
    put(key, new Integer(count));
  }

  public void addAll(CountTable table) {
    for (Iterator it = table.keySet().iterator(); it.hasNext();) {
      Object o = it.next();
      add(o, table.getCount(o));
    }
  }

  public void addAll(Collection collection) {
    for (Iterator it = collection.iterator(); it.hasNext();)
      add(it.next());
  }

  public void removeAll(Collection collection) {
    for (Iterator it = collection.iterator(); it.hasNext();)
      remove(it.next());
  }

  public int getCount(Object key) {
    return ((Integer)get(key)).intValue();
  }

  public CountTable top(int min) {
    CountTable top = new CountTable();
    for (Iterator it = keySet().iterator(); it.hasNext();) {
      Object o = it.next();
      if (getCount(o) >= min) top.add(o, getCount(o));
    }

    return top;
  }

  public Hashtable scale(double scale) {
    Hashtable scaled = new Hashtable();
    for (Iterator it = keySet().iterator(); it.hasNext();) {
      Object o = it.next();
      scaled.put(o, new Double(scale * getCount(o)));
    }

    return scaled;
  }

  public Vector bottom(int max) {
    Vector bottom = new Vector();
    for (Iterator it = keySet().iterator(); it.hasNext();) {
      Object o = it.next();
      if (getCount(o) < max) bottom.add(o);
    }

    return bottom;
  }

  public Vector nonZero() {
    Vector nonzero = new Vector();
    for (Iterator it = keySet().iterator(); it.hasNext();) {
      Object o = it.next();
      if (getCount(o) != 0) nonzero.add(o);
    }

    return nonzero;
  }
}
