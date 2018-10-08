package period;


import java.util.*;

public class OrderedDuplicateMap {
  Vector keys;
  Vector values;

  public OrderedDuplicateMap() {
    keys = new Vector();
    values = new Vector();
  }

  public void put(Object key, Object value) {
    int i = 0;
    while (i < keys.size() && ((Comparable)key).compareTo(keys.get(i)) >= 0) i++;
    keys.add(i, key);
    values.add(i, value);
  }

  public Object firstKey() {
    return keys.isEmpty() ? null : keys.get(0);
  }

  public Object remove(Object key) {
    int i = keys.indexOf(key);
    if (i == -1) return null;

    keys.remove(i);
    return values.remove(i);
  }

  public boolean isEmpty() {
    return keys.isEmpty();
  }

  public static void main(String argv[]) {
    OrderedDuplicateMap odm = new OrderedDuplicateMap();
    odm.put(new Integer(5), new Character('a'));
    odm.put(new Integer(3), new Character('b'));
    odm.put(new Integer(7), new Character('c'));
    odm.put(new Integer(1), new Character('d'));
    odm.put(new Integer(3), new Character('e'));
    System.out.println(odm.firstKey());
    System.out.println(odm.remove(odm.firstKey()));
    System.out.println(odm.firstKey());
    System.out.println(odm.remove(odm.firstKey()));
    System.out.println(odm.keys);
    System.out.println(odm.values);
  }
}
