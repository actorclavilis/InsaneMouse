package player;


public abstract class Player 
{
    protected int lives;
    protected boolean isActive;
    protected int x;
    protected int y;
       
    public Player(int _x, int _y, int numberOfLives, boolean startActive)
    {
        lives = numberOfLives;
        isActive = startActive;
        x = _x;
        y = _y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLives() {
        return lives;
    }
    
    public void resetLives(int _lives) {
        lives = _lives;
    }
    
    public void decLives(int _x, int _y) {
        lives--;
        x = _x; y = _y;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public abstract void move();
}
