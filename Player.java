
public class Player 
{
    public int lives;
    public boolean isActive;
    public int X = 0;
    public int Y = 0;
    public boolean respawn = false;
       
    public Player(int numberOfLives, boolean startActive)
    {
        lives = numberOfLives;
        isActive = startActive;
    }
    
    public Player()
    {
        this(3, true);
    }  
    
    public Player(boolean startActive)
    {
        this(3, startActive);       
    }  
}
