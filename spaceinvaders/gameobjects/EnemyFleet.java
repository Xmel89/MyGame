package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyFleet {
    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    public EnemyFleet() {
        createShips();
    }

    private void createShips() {

        ships = new ArrayList<EnemyShip>();
        for (int i = 0; i < ROWS_COUNT; i++) {
            for (int j = 0; j < COLUMNS_COUNT; j++) {
                ships.add(new EnemyShip(j * STEP, i * STEP + 12));
            }
        }
        ships.add(new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5));
    }

    public void draw(Game game) {
        ships.forEach(x -> x.draw((SpaceInvadersGame) game));
    }

    private double getLeftBorder() {
        double minX = ships.get(0).x;
        for (int i = 0; i < ships.size(); i++) {
            minX = ships.get(i).x < minX ? ships.get(i).x : minX;
        }
        return minX;
    }

    private double getRightBorder() {
        double maxX = 0;
        for (int i = 0; i < ships.size(); i++) {
            maxX = (ships.get(i).x + ships.get(i).width) > maxX ? (ships.get(i).x + ships.get(i).width) : maxX;
        }
        return maxX;
    }

    private double getSpeed() {
        if (2.0 < 3.0 / ships.size()) return 2.0;
        else return 3.0 / ships.size();
    }

    public void move() {
        if (ships.size() != 0) {
            if (direction == Direction.LEFT && getLeftBorder()<0) {
                direction = Direction.RIGHT;
                ships.forEach(x->x.move(Direction.DOWN, getSpeed()));
            }
            else if (direction == Direction.RIGHT && getRightBorder()> SpaceInvadersGame.WIDTH) {
                direction = Direction.LEFT;
                ships.forEach(x->x.move(Direction.DOWN, getSpeed()));
            }
            else {
                ships.forEach(x->x.move(direction, getSpeed()));
            }
        getSpeed();
        }
    }

    public Bullet fire(Game game) {
        if (ships.size()==0) return null;
        else if (game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0) return null;
        return ships.get(game.getRandomNumber(ships.size())).fire();
    }

    public void deleteHiddenShips() {
        Iterator iterator = ships.iterator();
        while (iterator.hasNext()){
            EnemyShip i = (EnemyShip) iterator.next();
            if (!i.isVisible()) iterator.remove();
        }
    }

    public double getBottomBorder() {
        double maxY = 0;
        for (int i = 0; i < ships.size(); i++) {
            maxY = ships.get(i).y + ships.get(i).height > maxY ? ships.get(i).y + ships.get(i).height : maxY;
        }
        return maxY;
    }

    public  int getShipsCount() {
        return ships.size();
    }

    public int verifyHit(List<Bullet> bullets) {
        if (bullets.size() == 0) {
            return 0;
        }
        int result = 0;
        for (int i = 0; i < ships.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                if (ships.get(i).isCollision(bullets.get(j))) {
                    if (ships.get(i).isAlive && bullets.get(j).isAlive) {
                        ships.get(i).kill();
                        bullets.get(j).kill();
                        result += ships.get(i).score;
                    }
                }
            }
        }
        return result;
    }
}
