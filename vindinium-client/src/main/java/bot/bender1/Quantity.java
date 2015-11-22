package bot.bender1;

/**
 * Created by slaan on 02.11.15.
 */
public enum Quantity {
    FEW(0), MIDDLE(1), LOTS(2);

    private int value;
    private Quantity(int value) { this.value = value; }
    public int getValue() { return value; }
    public static Quantity fromValue(int value) {
        switch (value) {
            case 0: return FEW;
            case 1: return MIDDLE;
            case 2: return LOTS;
            default: throw new RuntimeException("Tried to create a non supported Quantity: " + value);
        }
    }
}
