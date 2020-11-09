package com.javarush.games.game2048;


import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score;

    @Override
    public void initialize() {

        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (!canUserMove()) {
            gameOver();
        }
        if (isGameStopped = true) {
            if (key.equals(Key.SPACE) && isGameStopped) {
                isGameStopped = false;
                createGame();
                drawScene();
            }
        } else {
            if (key.equals(Key.LEFT)) {
                moveLeft();
                drawScene();
            }
            if (key.equals(Key.RIGHT)) {
                moveRight();
                drawScene();
            }
            if (key.equals(Key.UP)) {
                moveUp();
                drawScene();
            }
            if (key.equals(Key.DOWN)) {
                moveDown();
                drawScene();
            }
        }
    }

    private void createGame() {
        score = 0;
        setScore(score);
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                setCellColoredNumber(j, i, gameField[i][j]);

            }
        }
    }
    private void createNewNumber() {
        while (true) {
            int a = getRandomNumber(SIDE);
            int b = getRandomNumber(SIDE);
            if (gameField[a][b]==0) {
                gameField[a][b]=(getRandomNumber(10)==0)?4:2;
                break;
            }

        }
        if (getMaxTileValue()==2048) win();
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case 2 : return Color.ALICEBLUE;
            case 4: return Color.AQUA;
            case 8: return Color.RED;
            case 16: return Color.PINK;
            case 32: return Color.PALEVIOLETRED;
            case 64: return Color.PLUM;
            case 128: return Color.YELLOW;
            case 256: return Color.BLUE;
            case 512: return Color.GREY;
            case 1024: return Color.GREEN;
            case 2048: return Color.BEIGE;
            default: return Color.WHITE;
        }
    }

    private void setCellColoredNumber(int x, int y, int value) {
        if (value!=0) setCellValueEx(x,y,getColorByValue(value),Integer.toString(value));
        else  setCellValueEx(x,y,getColorByValue(value),"");
    }

    private  boolean compressRow(int[] row) {
        boolean  boo = false;
        for (int i = 1; i < row.length; i++) {
            if (row[i] != 0 && row[0] == 0) {
                row[0] = row[i];
                row[i] = 0;
                boo = true;
            } else if (row[i] != 0 && row[1] == 0 && i > 1) {
                row[1] = row[i];
                row[i] = 0;
                boo = true;
            } else if (row[i] != 0 && row[2] == 0 && i > 2) {
                row[2] = row[i];
                row[i] = 0;
                boo = true;
            }
        }
        return boo;
    }

    private boolean mergeRow(int[] row) {
        boolean  boo = false;
        for (int i = 0; i < row.length-1; i++) {
            if (row[i] != 0 && row[i+1] == row[i]) {
                score = score + row[i] + row[i+1];
                setScore(score);
                row[i] *= 2;
                row[i+1] = 0;
                boo = true;
                i++;
            }
        }
        return boo;
    }

    private void moveLeft() {
        boolean boo = false;
        for (int i = 0; i < SIDE; i++) {
            boolean a = compressRow(gameField[i]);
            boolean b = mergeRow(gameField[i]);
            boolean c = compressRow(gameField[i]);
            if (a || b || c) {
                boo = true;
            }
        }
        if (boo) createNewNumber();
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int[][] clo = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                clo[i][j] = gameField[SIDE-1-j][i];

            }
        }
        gameField = clo.clone();
    }

    private int getMaxTileValue() {
        int max = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                max = gameField[i][j] > max ? gameField[i][j] : max;
            }
        }
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "YOU WIN!", Color.GOLD, 30);
    }

    private boolean canUserMove() {
        boolean boo = false;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0) boo= true;
            }
        }
        if (!boo) {
            for (int i = 1; i < SIDE; i++) {
                for (int j = 1; j < SIDE; j++) {
                    if (gameField[i][j]==gameField[i-1][j] || gameField[i][j]==gameField[i][j-1]) boo = true;
                }
            }
            for (int i = 0; i < SIDE-1; i++) {
                for (int j = 0; j < SIDE-1; j++) {
                    if (gameField[i][j]==gameField[i+1][j] || gameField[i][j]==gameField[i][j+1]) boo = true;
                }
            }
        }
        return boo;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "YOU LOSE!", Color.ANTIQUEWHITE, 30);
    }
}

