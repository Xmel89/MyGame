package com.javarush.games.spaceinvaders.gameobjects;
import com.javarush.games.spaceinvaders.*;

import java.util.List;

public class PlayerShip extends Ship {
    private Direction direction = Direction.UP;

    public void setDirection(Direction newDirection) {

        this.direction = newDirection!=Direction.DOWN?newDirection:direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH / 2.0, SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length - 1);
        setStaticView(ShapeMatrix.PLAYER);
    }

    @Override
    public void kill() {
        if (isAlive) {
            isAlive = false;
            setAnimatedView(false, ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST, ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND, ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD, ShapeMatrix.DEAD_PLAYER);
        }
    }

    @Override
    public Bullet fire() {
        if (!isAlive) return null;
        return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
    }

    public void verifyHit(List<Bullet> bullets) {
        if (bullets.size() != 0 && isAlive) {
            for (Bullet bullet:bullets){
                if (bullet.isAlive && isCollision(bullet)) {
                    kill();
                    bullet.kill();
                }
            }
        }
    }

    public void move() {
        if (isAlive){
            switch (direction) {
                case LEFT:
                    x--;
                    break;
                case RIGHT:
                    x++;
                    break;
            }
            if (x<0) x = 0;
            if ((x + width)>SpaceInvadersGame.WIDTH) x = SpaceInvadersGame.WIDTH - width;
        }
    }

    public void win() {
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }
}
