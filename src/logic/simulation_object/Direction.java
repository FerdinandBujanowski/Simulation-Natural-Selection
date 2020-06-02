package logic.simulation_object;

import java.awt.*;
import java.util.Random;

public enum Direction {

    UP, DOWN, LEFT, RIGHT;

    public static Direction getRandomDirection(Direction lastDirection) {
        Random random = new Random();
        switch(random.nextInt(4)) {
            case 0:
                return(lastDirection != DOWN ? UP : DOWN);
            case 1:
                return(lastDirection != UP ? DOWN : UP);
            case 2:
                return(lastDirection != RIGHT ? LEFT : RIGHT);
            case 3:
                return(lastDirection != LEFT ? RIGHT : LEFT);
        }
        return null;
    }

    public static Direction getOppositeDirection(Direction lastDirection) {
        switch(lastDirection) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return null;
        }
    }

    public static Point getRelativeLocation(Direction nextDirection) {
        switch(nextDirection) {
            case UP:
                return new Point(0, -1);
            case DOWN:
                return new Point(0, 1);
            case LEFT:
                return new Point(-1, 0);
            case RIGHT:
                return new Point(1, 0);
            default:
                return new Point(0, 0);
        }
    }
}
