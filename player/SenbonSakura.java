package player;

import enemies.Enemy;
import util.EnemyDeletable;
import util.EnemyPredicate;
import java.awt.*;

public class SenbonSakura
{
    private static final int 
            SENBONSAKURA_RADIUS = 250, 
            SENBONSAKURA_SQUARE = SENBONSAKURA_RADIUS*SENBONSAKURA_RADIUS,
            SENBONSAKURA_TIMEOUT = 5000,
            SENBONSAKURA_TIMER = 5000;  
    private long senbonSakuraT = 0;
    protected EnemyDeletable parent;
    protected int playerNumber, width, infoOffset, senbonSakuraC;
    protected float x, y, remoteX, remoteY;
    protected boolean detonateLocal = false, detonateRemote = false;
    
    public SenbonSakura(int _width, int _infoOffset, int _playerNumber, EnemyDeletable _parent)
    {
        width = _width;
        infoOffset = _infoOffset;
        playerNumber = _playerNumber;
        parent = _parent;
    }
    
    public void detonateLocal(float _x, float _y)
    {     
        if((System.currentTimeMillis() - senbonSakuraT)>SENBONSAKURA_TIMEOUT) 
        {
            x = _x;
            y = _y;
            senbonSakuraC = 40;
            doRemoval(x, y);
            senbonSakuraT = System.currentTimeMillis();
            detonateLocal = true;
        }
    }
    
    public void detonateRemote(float _x, float _y)
    {        
        if((System.currentTimeMillis() - senbonSakuraT)>SENBONSAKURA_TIMEOUT) 
        {
            remoteX = _x;
            remoteY = _y;
            senbonSakuraT = System.currentTimeMillis();
            detonateRemote = true;
            senbonSakuraC = 40;
        }         
    }
    
    private void doRemoval(final float _x, final float _y)
    {
        parent.deleteIf(new EnemyPredicate() 
        {
                public boolean satisfiedBy(Enemy e) 
                {
                    float p1 = (_x + 5) - e.getX();
                    float p2 = (_y + 5) - e.getY();
                    return (p1*p1 + p2*p2) < SENBONSAKURA_SQUARE;
                }
        });
    }
    
    public void paint(Graphics g) 
    {
        g.setColor(Color.PINK);
        
        if(detonateLocal)
        {
            if (senbonSakuraC-->0) 
            {
                g.fillOval((int)x-SENBONSAKURA_RADIUS/2, (int)y-SENBONSAKURA_RADIUS/2, SENBONSAKURA_RADIUS, SENBONSAKURA_RADIUS);
            }
            else
            {
                detonateLocal = false;
            }
        }
        if(detonateRemote)
        {          
            if((System.currentTimeMillis()-senbonSakuraT) > SENBONSAKURA_TIMER)
            {
                doRemoval(remoteX, remoteY);
                if (senbonSakuraC-->0) 
                {
                    g.fillOval((int)remoteX-SENBONSAKURA_RADIUS/2, (int)remoteY-SENBONSAKURA_RADIUS/2, SENBONSAKURA_RADIUS, SENBONSAKURA_RADIUS);
                }
                else
                {
                    detonateRemote = false;
                }
            }
            else
            {
                int timer = (int)((SENBONSAKURA_TIMER-(System.currentTimeMillis()-senbonSakuraT))/1000) + 1;
                g.fillOval((int)remoteX-5, (int)remoteY-5, 10, 10);
                g.setColor(Color.BLACK);
                Font old = g.getFont();
                g.setFont(new Font("sansserif", Font.BOLD, 10));
                g.drawString(String.valueOf(timer), (int)remoteX-3, (int)remoteY+4);
                g.setFont(old);
                g.setColor(Color.PINK);
            }
        }
        
        int barWidth = (int)Math.min(SENBONSAKURA_TIMEOUT, System.currentTimeMillis()-senbonSakuraT);
        barWidth *=.05;
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
