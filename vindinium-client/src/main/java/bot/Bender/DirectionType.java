package bot.Bender;

import bot.dto.GameState;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public enum DirectionType {
    NORTH(0), SOUTH(1), WEST(2), EAST(3);

    private int value;
    private DirectionType(int value) { this.value = value; }
    /**
     * "Casts" this DirectionType to an integer.
     * @return
     */
    public int getValue() { return value; }

    /**
     * "Casts" an integer to a DirectionType.
     * @param value Value retrieved by "getValue".
     */
    public static DirectionType fromValue(int value) {
        switch (value) {
            case 0: return NORTH;
            case 1: return SOUTH;
            case 2: return WEST;
            case 3: return EAST;
            default: throw new RuntimeException("DirectionType " + value + " does not exist.");
        }
    }

    public static DirectionType fromPositions(GameState.Position currenPosition, GameState.Position position){
        if(currenPosition.getX() - position.getX() == 0 && currenPosition.getY() - position.getY() == 1){
            return NORTH;
        } else if(currenPosition.getX() - position.getX() == 0 && currenPosition.getY() - position.getY() == -1){
            return SOUTH;
        } else if(currenPosition.getX() - position.getX() == -1 && currenPosition.getY() - position.getY() == 0){
            return EAST;
        } else if(currenPosition.getX() - position.getX() == 1 && currenPosition.getY() - position.getY() == 0){
            return WEST;
        } else {
            throw new RuntimeException("DirectionType fromPositions failed.");
        }
    }
}
