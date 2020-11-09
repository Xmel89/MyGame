package com.javarush.games.racer;

import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;

public class RacerGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int CENTER_X = WIDTH/2;
    public static final int ROADSIDE_WIDTH = 14;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private boolean isGameStopped;
    private FinishLine finishLine;
    private static final int RACE_GOAL_CARS_COUNT = 40;
    private ProgressBar progressBar;
    private int score;

    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x < 0 || x >= 64 || y < 0 || y >= 64) {

        } else {
            super.setCellColor(x, y, color);

        }
    }

    @Override
    public void onTurn(int step) {
        score -= 5;
        setScore(score);
        if (roadManager.checkCrush(player)) {
            gameOver();
            drawScene();
        }
        else {
            if (roadManager.getPassedCarsCount()>=RACE_GOAL_CARS_COUNT) finishLine.show();
            if (finishLine.isCrossed(player)) {
                win();
                drawScene();
            }
            else {
                moveAll();
                roadManager.generateNewRoadObjects(this);
                drawScene();
            }
        }
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case RIGHT:
                player.setDirection(Direction.RIGHT);
                break;
            case LEFT:
                player.setDirection(Direction.LEFT);
                break;
            case SPACE:
                if (isGameStopped) createGame();
                break;
            case UP:
                player.speed = 2;
                break;

        }
    }

    @Override
    public void onKeyReleased(Key key) {
        switch (key) {
            case RIGHT:
                if (player.getDirection()==Direction.RIGHT) player.setDirection(Direction.NONE);
                break;
            case LEFT:
                if (player.getDirection()==Direction.LEFT) player.setDirection(Direction.NONE);
                break;
            case UP:
                player.speed = 1;
                break;
        }
    }

    private void createGame(){
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        setTurnTimer(40);
        roadManager = new RoadManager();
        isGameStopped = false;
        finishLine = new FinishLine();
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        score = 3500;
        drawScene();
    }

    private void drawScene() {
        drawField();
        roadMarking.draw(this);
        player.draw(this);
        roadManager.draw(this);
        finishLine.draw(this);
        progressBar.draw(this);
    }

    private void drawField() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH ; j++) {
                if (j==CENTER_X) setCellColor(CENTER_X, i, Color.WHITE);
                else if (j >= ROADSIDE_WIDTH && j < (WIDTH - ROADSIDE_WIDTH))setCellColor(j,i,Color.DIMGREY);
                else setCellColor(j,i,Color.GREEN);

            }
        }
    }

    private void moveAll() {

        roadMarking.move(player.speed);
        player.move();
        roadManager.move(player.speed);
        finishLine.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "YOU LOSE", Color.AQUA, 30);
        stopTurnTimer();
        player.stop();
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "YOU WIN!", Color.AQUA, 30);
        stopTurnTimer();
    }
}
