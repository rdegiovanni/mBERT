package casestudies.motivation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements sorted list of integers using a singly linked list with a sentintel node.
 * @author Facundo Molina <fmolina@dc.exa.unrc.edu.ar>
 */
public class SortedList implements Serializable {

  static final long serialVersionUID = 20200617L;

  private int x;
  private SortedList next;
  private static final int SENTINEL = Integer.MAX_VALUE;

  private SortedList(int x, SortedList next) {
    this.x = x;
    this.next = next;
  }

  public SortedList() {
    this(SENTINEL, null);
  }

  /**
   * Inserts the given element in the list
   */
  public void insert(int data) {
    if (data > this.x) {
      next.push(data);
    } else {
      next = new SortedList(x, next);
      x = data;
    }
  }


}
