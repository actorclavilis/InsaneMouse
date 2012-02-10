import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.awt.image.*;
import java.util.*;
import enemies.*;

public class GUI extends JPanel implements MouseMotionListener, ActionListener, KeyEventDispatcher
{
    private Dimension d;   
    private JPanel menu;
    private JButton easy, normal, insane, back;
    private JLabel highscoreL;
    private float[] oldxC, oldyC, xC, yC, randomX, randomY, randAngle, rainX, rainY;
    private long startTime, timeElapse, timeLast, timeIncrease1, timeIncrease2;
    private int invSpeed, ballN, monsterN, multiplier, defaultDistance, width, height, level, highscore, score, counterN, monsterMultiplier, timeDifficulty1, timeDifficulty2, randomN, rainN, distanceLimit;
    private boolean collision, spawnCircleB, spawnMonsterB, countdownF, circular, spawnIncrease, spawnRandomersB, spawnRainB;   
    private float monsterSpeed, distance, monsterSpeedRamp;
    private Thread t, r;
    private int[] randDirX, randDirY;
    private Set enemies;
    private Rectangle bbox;
    
	private int mouseSpeed;
	private boolean up1, down1, left1, right1, up2, down2, left2, right2;
	private int mouseX1, mouseY1, mouseX2, mouseY2;
	
	private float BOMB_RADIUS_MAX = 100;
	private boolean bombing = false;
	private float bombX, bombY, bombR;
    
    public GUI(Dimension a) throws Exception
    {    
        //MP3 music = new MP3();
        //music.play();
        
        d = a;        
        highscore = 0;
        mouseSpeed = 1;
        this.setBackground(Color.darkGray);
        this.setLayout(null);
        this.setBounds(0, 0, d.width, d.height);           
        this.addMouseMotionListener(this);
        this.setFocusable(true);
        this.setVisible(true);
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        
                   
        width = this.getWidth();
        height = this.getHeight();
        bbox = new Rectangle(15, 15, width-15, height-15);
        mouseX1 = width/2;
        mouseY1 = (height+100)/2;
        
        makeMenuScreen();
        this.add(menu);
    }  
    
    public boolean dispatchKeyEvent(KeyEvent e)
    {
    	if(e.getID() == KeyEvent.KEY_PRESSED)
    	{  	
	    	if(e.getKeyCode() == KeyEvent.VK_W)
	    	{
	    		up1 = true;    		
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_S)
	    	{
	    		down1 = true;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_A)
	    	{
	    		left1 = true;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_D)
	    	{
	    		right1 = true;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_NUMPAD8)
	    	{
	    		up1 = true;    		
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_NUMPAD5)
	    	{
	    		down1 = true;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_NUMPAD4)
	    	{
	    		left1 = true;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_NUMPAD6)
	    	{
	    		right1 = true;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_SPACE)
	    	{
	    		bombing = true;
	    		bombX = mouseX1;
	    		bombY = mouseY1;
	    		bombR = 0;
	    	}
	    }
	    
	    if(e.getID() == KeyEvent.KEY_RELEASED)
	    {	
	    	if(e.getKeyCode() == KeyEvent.VK_W)
	    	{
	    		up1 = false;    		
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_S)
	    	{
	    		down1 = false;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_A)
	    	{
	    		left1 = false;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_D)
	    	{
	    		right1 = false;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_NUMPAD8)
	    	{
	    		up1 = false;    		
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_NUMPAD5)
	    	{
	    		down1 = false;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_NUMPAD4)
	    	{
	    		left1 = false;
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_NUMPAD6)
	    	{
	    		right1 = false;
	    	}
	    }
	    
    	return false;
    }
    
    private void makeMenuScreen()
    {     
        menu = new JPanel();
        menu.setBackground(Color.darkGray);
        menu.setLayout(null);
        menu.setBounds(0, 0, width, height);   
        
        JLabel title = new JLabel("INSANE MOUSE");
        title.setBackground(Color.darkGray);
        title.setBounds((width/2)-50, (height/2)-250, 600, 100);
        title.setForeground(Color.white);
        //title.setFont(new Font(Font., 1, 40));
        
        JLabel author = new JLabel("By SJ");
        author.setBackground(Color.darkGray);
        author.setBounds((width/2)-40, (height/2)-200, 500, 100);
        author.setForeground(Color.white);
       // author.setFont(new Font(Font.SANS_SERIF, 1, 30));
        
        highscoreL = new JLabel(String.valueOf(highscore));
        highscoreL.setBackground(Color.darkGray);
        highscoreL.setBounds((width/2)-40, (height/2)+200, 500, 100);
        highscoreL.setForeground(Color.white);
        //highscoreL.setFont(new Font(FontSANS_SERIF, 1, 20));
        
        easy = new JButton("Easy");
        normal = new JButton("Normal");
        insane = new JButton("Insane");
        
        easy.addActionListener(this);
        normal.addActionListener(this);
        insane.addActionListener(this);
        
        easy.setBounds((width/2)-50, (height/2)-50, 100, 20);
        normal.setBounds((width/2)-50, height/2, 100, 20);
        insane.setBounds((width/2)-50, (height/2)+50, 100, 20);
        
        menu.add(title);
        menu.add(author);
        menu.add(easy);
        menu.add(normal);
        menu.add(insane);
        menu.add(highscoreL);
        
        back = new JButton("Back");
        back.setBounds(width/2-40, height/2+20, 100, 20);
        back.addActionListener(this);
        back.setVisible(false);
        this.add(back);
    }
    
    private void setup()
    {
        this.remove(menu);
        this.revalidate();
        
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0,0), "BLANK");
        this.setCursor(blank);
        
        //mouseX1 = MouseInfo.getPointerInfo().getLocation().x-8;
        //mouseY1 = MouseInfo.getPointerInfo().getLocation().y-30;
       
        reset();
        
        ballN = 1; 
        level = 1;
        timeLast = 0;        
        score = 0;        
        counterN = 10;
        timeIncrease1 = 0;
        timeIncrease2 = 0;
                      
        countdownF = true;
        circular = true;
        spawnIncrease = true;
        spawnCircleB = false;  
        spawnMonsterB = false;
        spawnRandomersB = false;
        collision = false;
                      
        monsterSpeed = 8000*monsterSpeedRamp;
                        
        levelSetup();
        countdown();
        animate();    
    }
    
    private void reset()
    {
        if(enemies != null)
            enemies.clear();
        else enemies = new HashSet();
    }
    
    private void levelSetup()
    {
        reset();
        
        switch(level)
        {
            case 1:                              
                distance = 600;
                monsterN = 20;
                randomN = 20;
                rainN = 30;
                defaultDistance = 600;
                timeLast = 20000;
                spawnCircleB = true;
                break;
            case 2:
                distance = 500;
                monsterN = 20;
                randomN = 20;
                rainN = 30;
                defaultDistance = 500;
                timeLast += 20000;
                spawnCircleB = true;
                break;
            case 3:
                distance = 400;
                monsterN = 20;
                randomN = 20;
                rainN = 30;
                defaultDistance = 400;
                timeLast += 20000;
                spawnCircleB = true;
                break;
            case 4:
                distance = 300;
                monsterN = 20;
                randomN = 20;
                rainN = 30;
                defaultDistance = 300;
                spawnCircleB = true;
                timeLast += 20000;
                break;
            case 5:
                distance = 300;
                monsterN = 10;
                randomN = 25;
                rainN = 40;
                defaultDistance = 300;
                spawnCircleB = true;
                timeLast += 50000000;
                level = 1;
                break;
        }       
        monsterN *= monsterMultiplier;
        randomN *= monsterMultiplier;
        spawnMonsterB = true;    
        spawnRandomersB = true;
        spawnIncrease = true;
        spawnRainB = true;
    }
                  
    private void drawLayout(Graphics g)
    {
        g.setColor(Color.blue);
        g.drawRect(10, 10, width-20, height-20);
        
        if(!bbox.contains(mouseX1, mouseY1))
        {
            if(!countdownF)
            {
                collision = true;
            }
        } 
        
        if(collision)
        {
            //g.setFont(new Font(Font.SANS_SERIF, 1, 40));
            g.setColor(Color.red.darker());
            g.drawString("GAME OVER", width/2-20, height/2);
        }
    }
       
    private void countdown() 
    {       
        t = new Thread() 
        {

            public void run()
            {   
                long time = System.currentTimeMillis();
                long timeNow = System.currentTimeMillis();
                while((time + 1000) > timeNow)
                {
                    timeNow = System.currentTimeMillis();
                    counterN = (int)(1000-(timeNow-time))/100;
                }             
                countdownF = false;
                startTime = System.currentTimeMillis();        
                
            }
        };
        if(countdownF)
        {
            t.start();
        }
        else
        {
            t = null;
        }
    }
    
    private void spawnCircles()
    {
        if (circular) 
        {
            for(int i = 0; i < ballN; i++) 
            {
                double degree /*in math and computer science*/ = Math.random()*2*Math.PI;
                float x = mouseX1 + distance * (float) Math.sin(degree * i);
                float y = mouseY1 + distance * (float) Math.cos(degree * i);
                enemies.add(new EnemyTypes.Circle(x, y, invSpeed));
            }
        } 
        else 
        {
            for(int i = 1; i < (ballN / 2); i++) 
            {
                float x = (i * 2 * width) / (ballN);
                float y = 0;
                enemies.add(new EnemyTypes.Circle(x, y, invSpeed));
            }

            for(int i = (ballN / 2) + 1; i < ballN; i++) 
            {
                float x = ((i - ballN / 2) * 2 * width) / ballN;
                float y = height;
                enemies.add(new EnemyTypes.Circle(x, y, invSpeed));
            }
        }      
        spawnIncrease = false;
    }
    
    private void spawnMonsters()
    {
        for( int i = 0; i < monsterN; i++)
        {
            float x = (float)Math.random()*width;
            float y = (float)Math.random()*height;
            
            float r = (float)Math.sqrt(Math.pow(mouseX1 - x, 2) + Math.pow(mouseY1 - y, 2));
            
            while(r < distanceLimit)
            {
            	x = (float)Math.random()*width;
            	y = (float)Math.random()*height;
            	r = (float)Math.sqrt(Math.pow(mouseX1 - x, 2) + Math.pow(mouseY1 - y, 2));
            }
            
            enemies.add(new EnemyTypes.Monster(x, y, 4));
        }

        spawnMonsterB = false;
    }
    
    private void spawnRandomers() {
        for (int i = 0; i < randomN; i++) {
            float x = (float) Math.random() * width;
            float y = (float) Math.random() * height;

            float r = (float) Math.sqrt(Math.pow(mouseX1 - x, 2) + Math.pow(mouseY1 - x, 2));

            while (r < distanceLimit) {
                x = (float) Math.random() * width;
                y = (float) Math.random() * height;
                r = (float) Math.sqrt(Math.pow(mouseX1 - x, 2) + Math.pow(mouseY1 - y, 2));
            }

            enemies.add(new EnemyTypes.Random(x, y, 2, bbox));
        }

        spawnRandomersB = false;
    }

    private void spawnRain()
    {   
        for(int i = 0; i < rainN; i++) 
        {
            float x = (i*2*width)/rainN;
            float y = 0;
            enemies.add(new EnemyTypes.Rain(x, y, 4));
        }        
        spawnRainB = false; 
    }
    
    
    
    private void animate()
    {
        r = new Thread() 
        {

            public void run()
            {
                while(true)
                {               
                    if(!countdownF)
                    {
                        countdown();
                        if(collision)
                        {                       
                            if(highscore < score)
                            {
                                highscore = score;
                                highscoreL.setText(String.valueOf(highscore));
                            }
                            break;
                        } 
                        
                        timeElapse = 1+System.currentTimeMillis()-startTime;                         
                        if(timeElapse > timeIncrease1)
                        {
                            timeIncrease1 = timeElapse + timeDifficulty1;
                            ballN++;
                            distance++;
                            spawnIncrease = true;
                        }
                        if(timeElapse > timeIncrease2)
                        {
                            timeIncrease2 = timeElapse + timeDifficulty2;
                            spawnRainB = true;
                        }
                        score = (int)timeElapse*multiplier;
                        
                        if(timeElapse > timeLast)
                        {
                            level++;
                            levelSetup();
                        }                       
                        
                        if(ballN > 25)
                        {
                            Set newEnemies = new HashSet();
                            Iterator i = enemies.iterator();
                            while(i.hasNext()) {
                                Enemy e = (Enemy)i.next();
                                if(!e.isMortal())
                                    newEnemies.add(e);
                            }
                            enemies = newEnemies;
                            ballN = 1;
                            distance = defaultDistance;
                            circular = !circular;                           
                        }
                    }

                    try
                    {
                        SwingUtilities.invokeAndWait(new Runnable() 
                        {

                            public void run()
                            {
                                repaint();
                            }
                        });
                    }catch(Exception e)
                    {
                    }     
                }
                back.setVisible(true);
                setCursor(Cursor.getDefaultCursor());
            }
        };
        r.start();
    }
    
    private void movePlayers()
    {
    	if(up1)
    	{
    		mouseY1 -= mouseSpeed;    		
    	}
    	if(down1)
    	{
    		mouseY1 += mouseSpeed;
    	}
    	if(left1)
    	{
    		mouseX1 -= mouseSpeed;
    	}
    	if(right1)
    	{
    		mouseX1 += mouseSpeed;
    	}
    }
   
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
       	movePlayers();
        height = this.getHeight();
        width = this.getWidth();
        g.setColor(Color.white);
        g.fillOval(mouseX1-5, mouseY1-5, 10, 10);
        g.drawString("Score", width-50, height-35);
        g.drawString(String.valueOf(score), width-60, height-20);
        
        if(countdownF)
        {
            //g.setFont(new Font(Font.SANS_SERIF, 1, 40));
            g.drawString(String.valueOf(counterN), width/2-10, height/2);
        }
        else
        {                  
            if((spawnCircleB)&&(spawnIncrease))
            {
                spawnCircles();
            }
            if(spawnMonsterB)
            {
                spawnMonsters();
            }
            if(spawnRandomersB)
            {
                spawnRandomers();
            }
            if(spawnRainB)
            {
                spawnRain();
            }
            
            Iterator i = enemies.iterator();
            while(i.hasNext()) {
                Enemy e = (Enemy)i.next();
                e.move(mouseX1, mouseY1);
                e.paint(g);
                if(e.collidesWith(mouseX1, mouseY1)) endOfGame(); 
            }
        }     
        drawLayout(g);       
    }
      
    public void endOfGame() {
        
    }

    public void mouseDragged(MouseEvent e)
    {
        mouseX1 = e.getX();
        mouseY1 = e.getY();    
    }
    

    public void mouseMoved(MouseEvent e)
    {
        mouseX1 = e.getX();
        mouseY1 = e.getY();   
    }
       

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == easy)
        {          
            monsterSpeedRamp = 3;
            invSpeed = 30000;
            timeDifficulty1 = 100;
            timeDifficulty2 = 4000;
            distanceLimit = 400;
            monsterMultiplier = 1;
            multiplier = 1;
            setup();
        }
        else if(e.getSource() == normal)
        {
            monsterSpeedRamp = 4;
            invSpeed = 20000;
            timeDifficulty1 = 300;
            timeDifficulty2 = 4000;
            distanceLimit = 300;
            monsterMultiplier = 5;
            multiplier = 5;
            setup();
        }
        else if(e.getSource() == insane)
        {
            monsterSpeedRamp = 5;
            invSpeed = 10000;
            timeDifficulty1 = 100;
            timeDifficulty2 = 4000;
            distanceLimit = 200;
            monsterMultiplier = 15;
            multiplier = 15;       
            setup();
        }
        else if(e.getSource() == back)
        {
            r = null;
            this.add(menu);      	
            back.setVisible(false);
            this.revalidate();        
            repaint();            
        }
    }
}