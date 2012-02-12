package player;

import enemies.Enemy;
import util.EnemyDeletable;
import java.awt.*;
import util.EnemyPredicate;

public abstract class Player {

    private static final int 
            SENBONSAKURA_RADIUS = 250, 
            SENBONSAKURA_SQUARE = SENBONSAKURA_RADIUS*SENBONSAKURA_RADIUS,
            IMMUNITY_LIFETIME = 2000,
            SENBONSAKURA_TIMEOUT = 5000;
    protected int lives;
    protected boolean isActive;
    protected boolean hasImmunity;
    protected long lastImmunity;
    protected float x;
    protected float y;
    protected EnemyDeletable parent;
    private int senbonSakuraN;
    private int senbonSakuraC;
    private long senbonSakuraT = 0;
    protected int infoOffset;
    protected int playerNumber;
    protected int width;

    public Player(int _x, int _y, int numberOfLives, boolean startActive, int numberOfSenbonsakura, EnemyDeletable _parent, int _infoOffset, int _playerNumber, int _width) {
        lives = numberOfLives;
        isActive = startActive;
        x = _x;
        y = _y;
        parent = _parent;
        senbonSakuraN = numberOfSenbonsakura;
        infoOffset = _infoOffset;
        playerNumber = _playerNumber;
        width = _width;
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public int getLives() {
        return lives;
    }
       
    public void setImmunity(boolean _hasImmunity)
    {
        hasImmunity = _hasImmunity;
        lastImmunity = System.currentTimeMillis();
    }
    
    public boolean getImmunity()
    {
        if((lastImmunity+IMMUNITY_LIFETIME) < System.currentTimeMillis())
        {
            hasImmunity = false;       
        }
        return hasImmunity;
    }

    public void resetLives(int _lives) {
        lives = _lives;
    }

    public void decLives(float _x, float _y) {
        lives--;
        x = _x;
        y = _y;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public abstract void move();

    protected void senbonSakura() {
        if (((System.currentTimeMillis() - senbonSakuraT)>SENBONSAKURA_TIMEOUT)&&(senbonSakuraN-- > 0)) {
            senbonSakuraC = 40;
            parent.deleteIf(new EnemyPredicate() {
                public boolean satisfiedBy(Enemy e) {
                    float p1 = (x + 5) - e.getX();
                    float p2 = (y + 5) - e.getY();
                    return (p1*p1 + p2*p2) < SENBONSAKURA_SQUARE;
                }
            });
            senbonSakuraT = System.currentTimeMillis();
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int)x - 5, (int)y - 5, 10, 10);
        if (senbonSakuraC-->0) {
            g.setColor(Color.PINK);
            g.fillOval((int)x-SENBONSAKURA_RADIUS/2, (int)y-SENBONSAKURA_RADIUS/2, SENBONSAKURA_RADIUS, SENBONSAKURA_RADIUS);
        }
        int barWidth = (int)Math.min(SENBONSAKURA_TIMEOUT, System.currentTimeMillis()-senbonSakuraT);
        barWidth *=.05;
        g.setColor(Color.PINK);
        if(playerNumber == 1)
        {
            g.fillRect(100, infoOffset, barWidth, 5);
        }
        else if(playerNumber == 2)
        {
            g.fillRect(width-barWidth-100, infoOffset, barWidth, 5);
        }
    }
}
