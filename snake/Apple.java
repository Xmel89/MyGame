package com.javarush.games.snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.*;

public class Apple extends GameObject {
    public boolean isAlive = true;
    private static final String APPLE_SIGN = "\uD83C\uDF4E";

    public Apple(int x, int y) {
        super(x, y);
    }

    public void draw(Game o ){
        o.setCellValueEx(this.x, this.y, Color.NONE, APPLE_SIGN, Color.RED, 75);
    }
}
