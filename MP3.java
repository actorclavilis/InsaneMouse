package insanemouse;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.*;

class MP3 
{
    private Player player; 
    private final BufferedInputStream bis = new BufferedInputStream(new FileInputStream("Tonic.mp3"));
    
    public MP3() throws Exception
    {    
    }
    
    public void stop() 
    {
        if(player != null)
        {
            player.close();
        } 
    }

    public void play() 
    {     
        Thread t = new Thread() 
        {
            @Override
            public void run() 
            {
                while(true)
                {
                    if((player == null)||(player.isComplete()))
                    {
                        try 
                        {                           
                            player = new Player(bis); 
                            player.play(); 
                        }
                        catch(Exception e) 
                        {
                        } 
                    }
                }
            }
        };
        t.start();            
    }
}
