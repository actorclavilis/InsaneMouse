package player;

import util.EnemyDeletable;
import java.awt.*;

public abstract class Player {

    private static final int IMMUNITY_LIFETIME = 2000;
    protected int lives;
    protected boolean isActive;
    protected boolean hasImmunity;
    protected long lastImmunity;
    protected float x;
    protected float y;
    protected SenbonSakura s;

    public Player(int _x, int _y, int numberOfLives, boolean startActive, EnemyDeletable _parent, int _infoOffset, int _playerNumber, int _width) 
    {
        lives = numberOfLives;
        isActive = startActive;
        x = _x;
        y = _y;
        
        s = new SenbonSakura(_width, _infoOffset, _playerNumber, _parent);       
    }

    public int getX() 
    {
        return (int)x;
    }

    public int getY() 
    {
        return (int)y;
    }

    public int getLives() 
    {
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

    public void resetLives(int _lives) 
    {
        lives = _lives;
    }

    public void decLives(float _x, float _y) 
    {
        lives--;
        x = _x;
        y = _y;
    }

    public boolean isActive() 
    {
        return isActive;
    }

    public void setActive(boolean isActive) 
    {
        this.isActive = isActive;
    }

    public abstract void move();

    protected void senbonSakura(boolean remote) 
    {
        if(remote)
        {
            s.detonateRemote(x, y);
        }
        else
        {
            s.detonateLocal(x, y);
        }
    }

    public void paint(Graphics g) 
    {
        g.setColor(Color.WHITE);
        g.fillOval((int)x - 5, (int)y - 5, 10, 10);
        s.paint(g);
    }
}
