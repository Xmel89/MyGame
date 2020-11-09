package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private List<Star> stars;
    private EnemyFleet enemyFleet;
    public static final int COMPLEXITY = 5;
    private List<Bullet> enemyBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private List<Bullet> playerBullets;
    private static final int PLAYER_BULLETS_MAX = 1;
    private int score;


    @Override

    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    @Override
    public void onTurn(int step) {
        moveSpaceObjects();
        check();
        Bullet bullet = enemyFleet.fire(this);
        if (bullet != null) enemyBullets.add(bullet);
        setScore(score);
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case SPACE:
                if(playerShip.fire()!=null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                    playerBullets.add(playerShip.fire());
                };
                if (isGameStopped) createGame();
                break;
            case LEFT:
                playerShip.setDirection(Direction.LEFT);
                break;
            case RIGHT:
                playerShip.setDirection(Direction.RIGHT);
                break;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        switch (key) {
            case LEFT:
                if (playerShip.getDirection()==Direction.LEFT)playerShip.setDirection(Direction.UP);
                break;
            case RIGHT:
                if (playerShip.getDirection()==Direction.RIGHT)playerShip.setDirection(Direction.UP);
                break;
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if (x < WIDTH && x > 0 && y < HEIGHT && y > 0) {
            super.setCellValueEx(x, y, cellColor, value);
        }

    }

    private void createGame() {
        createStars();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<Bullet>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        playerBullets = new ArrayList<Bullet>();
        score = 0;
        drawScene();
        setTurnTimer(40);
    }

    private void drawScene() {
        drawField();
        playerBullets.forEach(x->x.draw(this));
        enemyBullets.forEach(x -> x.draw(this));
        enemyFleet.draw(this);
        playerShip.draw(this);
    }

    private void drawField() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setCellValueEx(i, j, Color.BLACK, "");
            }
        }
        stars.forEach(x -> x.draw(this));
    }

    private void createStars() {
        stars = new ArrayList<Star>();
        stars.add(new Star(1, 1));
        stars.add(new Star(10, 1));
        stars.add(new Star(1, 10));
        stars.add(new Star(20, 1));
        stars.add(new Star(1, 20));
        stars.add(new Star(30, 1));
        stars.add(new Star(1, 30));
        stars.add(new Star(56, 48));
    }

    private void moveSpaceObjects() {

        enemyFleet.move();
        enemyBullets.forEach(x -> x.move());
        playerShip.move();
        playerBullets.forEach(x->x.move());
    }

    private void removeDeadBullets() {
        for (int i = 0; i < enemyBullets.size(); i++) {
            if (!enemyBullets.get(i).isAlive || enemyBullets.get(i).y >= HEIGHT - 1) enemyBullets.remove(i);
        }
        Iterator iterator = playerBullets.iterator();
        Bullet bullet;
        while (iterator.hasNext()){
            bullet = (Bullet) iterator.next();
            if (!bullet.isAlive || (bullet.y + bullet.height) < 0) {
                iterator.remove();
            }
        }
    }

    private void check() {
        playerShip.verifyHit(enemyBullets);
        enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
        if (!playerShip.isAlive)stopGameWithDelay();
        if (enemyFleet.getBottomBorder()>= playerShip.y) playerShip.kill();
        if (enemyFleet.getShipsCount()==0) playerShip.win();
        score+=enemyFleet.verifyHit(enemyBullets);
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) showMessageDialog(Color.GOLD, "YOU WIN!", Color.GREEN, 30);
        else showMessageDialog(Color.GOLD, "YOU LOSE!", Color.RED, 30);
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount>=10)stopGame(playerShip.isAlive);
    }
}
