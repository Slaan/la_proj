package algorithms;

/**
 * Utility to easily shift Bits.
 */
public class IdShifter {
  int id;

  public IdShifter() {}
  public IdShifter(int id) { this.id = id; }
  public void shift(int wert, int shift) {
    id <<= shift;
    id += wert;
  }

  public int unShift(int shift) {
    int wert;
    int shifter = ((1 << shift) - 1);
    wert = id & shifter;
    id >>= shift;
    return wert;
  }

  public int getId() { return id; }
}
