package com.javarush.games.racer.road;

import com.javarush.engine.cell.Game;
import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private List<RoadObject> items = new ArrayList<>();
    private static final int PLAYER_CAR_DISTANCE = 12;
    private int passedCarsCount = 0;

    public int getPassedCarsCount() {
        return passedCarsCount;
    }

    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        if (type == RoadObjectType.THORN) {
            return new Thorn(x, y);
        }
        else if (type == RoadObjectType.DRUNK_CAR) return new MovingCar(x, y);
        else return new Car(type, x, y);
    }

    private void addRoadObject(RoadObjectType type, Game game) {
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject b = createRoadObject(type, x, y);
        if (b!=null && isRoadSpaceFree(b)) items.add(b);
    }

    public void draw(Game game) {
        items.forEach(x->x.draw(game));
    }

    public void move(int boost) {

        items.forEach(x->x.move(boost+x.speed, items));
        deletePassedItems();
    }

    private boolean isThornExists() {
        boolean b = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).type==RoadObjectType.THORN) {
                b = true;
                return true;
            }
        }
        return b;
    }

    private void generateThorn(Game game) {
        if(game.getRandomNumber(100)<10 && !isThornExists()) addRoadObject(RoadObjectType.THORN, game);
    }

    public void generateNewRoadObjects(Game game) {

        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }
    private void deletePassedItems() {
        Iterator iterator = items.iterator();
        while (iterator.hasNext()) {
            RoadObject obj = (RoadObject) iterator.next();
            if (obj .y >= RacerGame.HEIGHT) {
                if (obj.type!=RoadObjectType.THORN) passedCarsCount++;
                iterator.remove();
            }
        }
    }
    public boolean checkCrush(PlayerCar playerCar) {
        boolean b = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isCollision(playerCar)) b = true;
        }
        return b;
    }

    private void generateRegularCar(Game game) {
        int carTypeNumber = game.getRandomNumber(4);
        if (game.getRandomNumber(100)<30) addRoadObject(RoadObjectType.values()[carTypeNumber], game);

    }

    private boolean isRoadSpaceFree(RoadObject object) {
        boolean b = false;
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)) b = true;
        }
        return b;
    }

    private  boolean isMovingCarExists() {
        boolean b = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).type==RoadObjectType.DRUNK_CAR) {
                b = true;
                return true;
            }
        }
        return b;
    }

    private void generateMovingCar(Game game) {
        if (game.getRandomNumber(100)<10 && !isMovingCarExists()) addRoadObject(RoadObjectType.DRUNK_CAR, game);

    }
}
