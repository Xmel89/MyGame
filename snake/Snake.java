package com.javarush.games.snake;
import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Snake {
    public int x;
    public int y;
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    private List<GameObject> snakeParts = new ArrayList<>();
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;


    public void setDirection(Direction direcnew){
        if ( ((direction==Direction.RIGHT) || (direction==Direction.LEFT)) && (snakeParts.get(0).x == snakeParts.get(1).x)  ) {
            return;
        }

        if ( (direction==Direction.DOWN || direction==Direction.UP) && (snakeParts.get(0).y == snakeParts.get(1).y) ) {
            return;
        }
        this.direction = direcnew;
    }

    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x+1, y));
        snakeParts.add(new GameObject(x+2, y));
    }

    public void draw(Game game) {
        Color color;
        color = this.isAlive==true?Color.GOLD:Color.RED;
        game.setCellValueEx(snakeParts.get(0).x, snakeParts.get(0).y, Color.NONE, HEAD_SIGN, color, 75);
        for (int i = 1; i < snakeParts.size(); i++) {
            game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, color, 75);
        }
    }

    public void move(Apple apple) {
        GameObject o = createNewHead();
        if (o.x < 0 || o.y < 0 || o.x > 14 || o.y > 14) isAlive = false;
        else {
            if(checkCollision(o)){
                isAlive = false;
            }
            else {
                snakeParts.add(0, o);
                if (snakeParts.get(0).x==apple.x && snakeParts.get(0).y==apple.y) {
                    apple.isAlive = false;
                }
                else removeTail();
            }
        }
    }

    public GameObject createNewHead() {
        GameObject o = null;
        if (direction.equals(Direction.LEFT)) o = new GameObject(snakeParts.get(0).x-1, snakeParts.get(0).y);
        if (direction.equals(Direction.RIGHT)) o = new GameObject(snakeParts.get(0).x+1, snakeParts.get(0).y);
        if (direction.equals(Direction.UP)) o = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y-1);
        if (direction.equals(Direction.DOWN)) o = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y+1);
        return o;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size()-1);
    }

    public boolean checkCollision(GameObject o) {
        boolean answer = false;
        for (GameObject i : snakeParts) {
            if(i.x==o.x && i.y==o.y) answer=true;
        }
        return answer;
    }

    public int getLength() {
        return snakeParts.size();
    }

}
