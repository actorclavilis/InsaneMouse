import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.*;

class MP3 
{
    private Player player; 
    private Thread t;
    private boolean play;

    public MP3() throws Exception
    {   
        play = false;
    }
    
    public void stop() 
    {
        if(player != null)
        {
            player.close();
        } 
        play = false;
        try
        {
            t.wait();
        }catch(Exception e)
        {}
        t = null;
        player = null;
    }

    public void play() 
    {     
        play = true;
        t = new Thread() {
            public void run() 
            {
                while(true)
                {
                    if(((player == null)||(player.isComplete()))&&(play))
                    {                       
                        try 
                        {   
                            FileInputStream fi = new FileInputStream("Tonic.mp3");
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
}
