import java.io.BufferedInputStream;
import java.io.InputStream;
import javazoom.jl.player.*;

class MP3 
{
    private Player player; 
    private Thread t;
    private boolean play;
    
    public MP3() throws Exception
    {   
        play = false;
        t = new Thread() {
            public void run() 
            {
                while(true)
                {
                    if(((player == null)||(player.isComplete()))&&(play))
                    {                       
                        try 
                        {   
                            InputStream fi = this.getClass().getResource("/Resources/Tonic.mp3").openStream();
                            BufferedInputStream bis = new BufferedInputStream(fi);
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
    
    public void stop() 
    {
        play = false;
        if(player != null)
        {
            player.close();
        } 
        player = null;      
    }

    public void play() 
    {     
        play = true;                       
    }
}
