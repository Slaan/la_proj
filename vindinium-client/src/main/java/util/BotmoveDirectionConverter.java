package util;

import bot.bender.BotMove;
import bot.bender.DirectionType;

/**
 * Created by Daniel Hofmeister on 21.11.2015.
 */
public class BotmoveDirectionConverter {

  public static DirectionType getDirectionTypeFromBotMove(BotMove move) {
    DirectionType result = null;
    if (move.equals(BotMove.EAST)) {
      result = DirectionType.EAST;
    } else if (move.equals(BotMove.SOUTH)) {
      result = DirectionType.SOUTH;
    } else if (move.equals(BotMove.WEST)) {
      result = DirectionType.WEST;
    } else if (move.equals(BotMove.NORTH)) {
      result = DirectionType.NORTH;
    } else {
      throw new IllegalArgumentException("no such BotMove");
    }
    return result;
  }

  public static BotMove getBotMoveFromDirectionType(DirectionType dir) {
    BotMove result = null;
    if (dir.equals(DirectionType.EAST)) {
      result = BotMove.EAST;
    } else if (dir.equals(DirectionType.SOUTH)) {
      result = BotMove.SOUTH;
    } else if (dir.equals(DirectionType.WEST)) {
      result = BotMove.WEST;
    } else if (dir.equals(DirectionType.NORTH)) {
      result = BotMove.NORTH;
    } else {
      throw new IllegalArgumentException("no such DirectionType");
    }
    return result;
  }
}
