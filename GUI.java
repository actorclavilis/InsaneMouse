
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.awt.image.*;
import enemies.*;
import util.*;

public class GUI extends JPanel implements MouseMotionListener, ActionListener, KeyEventDispatcher
{
    private Dimension d; 
      
    private JPanel menu;
    private JButton easy, normal, insane, back;
    private JLabel highscoreL;
    
    private int ballN, monsterN, counterN, randomN, rainN, monsterMultiplier;
    private int mouseSpeed;
    private int multiplier,highscore, score, level;
    private int invSpeed, defaultDistance, width, height, timeDifficulty1, timeDifficulty2, distanceLimit;
    private int[] borders;
    
    private long startTime, timeElapse, timeLast, timeCircle, timeRain, programLoopCounter;
    private long shrapnelLifetime = 3000;
    
    private boolean countdownF, spawnCircleB, spawnMonsterB, spawnRandomersB, spawnRainB;
    private boolean circular, spawnIncrease;
    private boolean up1, down1, left1, right1, up2, down2, left2, right2;
    
    private float distance, programSpeedAdjust;
    
    private Set enemies;			
    private Thread t, r;   
    private scbClass scbInstance = new scbClass();
    private Player[] player;
    
    public GUI(Dimension a) throws Exception
    {                   
        d = a;
        
        this.setBackground(Color.darkGray);
        this.setLayout(null);
        this.setBounds(0, 0, d.width, d.height);           
        this.addMouseMotionListener(this);
        this.setFocusable(true);
        this.setVisible(true);
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        
        player = new Player[2];
        player[0] = new Player();
        player[1] = new Player(false);
        
        highscore = 0;
        mouseSpeed = 1;             
        width = this.getWidth();
        height = this.getHeight();    
        player[0].X = width/2;
        player[0].Y = (height+100)/2;

        borders = new int[4];
        borders[0] = 15;
        borders[1] = 15;
        borders[2] = width-20;
        borders[3] = height-20;
        
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
                up2 = true;    		
            }
            if(e.getKeyCode() == KeyEvent.VK_NUMPAD5)
            {
                down2 = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_NUMPAD4)
            {
                left2 = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_NUMPAD6)
            {
                right2 = true;
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
                up2 = false;    		
            }
            if(e.getKeyCode() == KeyEvent.VK_NUMPAD5)
            {
                down2 = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_NUMPAD4)
            {
                left2 = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_NUMPAD6)
            {
                right2 = false;
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
        
        JLabel author = new JLabel("By SJ and HH");
        author.setBackground(Color.darkGray);
        author.setBounds((width/2)-40, (height/2)-200, 500, 100);
        author.setForeground(Color.white);
        
        highscoreL = new JLabel(String.valueOf(highscore));
        highscoreL.setBackground(Color.darkGray);
        highscoreL.setBounds((width/2)-40, (height/2)+200, 500, 100);
        highscoreL.setForeground(Color.white);
        
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
       
        reset();
        
        ballN = 1; 
        level = 1;
        timeLast = 0;        
        score = 0;        
        counterN = 10;
        timeCircle = 0;
        timeRain = 0;
        programLoopCounter = 1;
        programSpeedAdjust = 1;
                      
        countdownF = true;
        circular = true;
        spawnIncrease = true;
        spawnCircleB = false;  
        spawnMonsterB = false;
        spawnRandomersB = false;   
                        
        levelSetup();
        countdown();
        animate();    
    }
    
    private void reset()
    {
        if(enemies != null)
        {
            enemies.clear();
        }
        else 
        {
            enemies = new HashSet();
        }
    }
    
    private void levelSetup()
    {
        reset();
        
        switch(level)
        {
            case 1:                              
                distance = 600;
                monsterN = 0;
                randomN = 0;
                rainN = 40;
                defaultDistance = 600;
                timeLast = 10000;
                spawnCircleB = false;    
                break;
            case 2:
                distance = 600;
                monsterN = 0;
                randomN = 40;
                rainN = 0;
                defaultDistance = 600;
                timeLast += 10000;
                spawnCircleB = false;
                break;
            case 3:
                distance = 600;
                monsterN = 40;
                randomN = 0;
                rainN = 0;
                defaultDistance = 600;
                timeLast += 10000;
                spawnCircleB = false;
                break;
            case 4:
                distance = 400;
                monsterN = 20;
                randomN = 0;
                rainN = 0;
                defaultDistance = 400;
                spawnCircleB = true;
                timeLast += 10000;
                break;
            case 5:
                distance = 400;
                monsterN = 20;
                randomN = 20;
                rainN = 20;
                defaultDistance = 400;
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

        borders[2] = width-20;
        borders[3] = height-20;
        
        for(int i = 0; i < 2; i++)
        {
            if(player[i].isActive)
            {
                if(player[i].X < borders[0] || player[i].Y < borders[1] || player[i].X > borders[2] || player[i].Y > borders[3])
                {
                    if(!countdownF)
                    {
                        player[i].lives--;
                        player[i].respawn = true;
                    }
                }
            }
        }
        
        if(((player[0].lives == 0)&&(player[1].lives == 0))||((player[0].lives == 0)&&(!player[1].isActive)))
        {
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
        if(circular) 
        {
            for(int i = 0; i < ballN; i++) 
            {
                double degree = Math.random()*2*Math.PI;
                float x = player[0].X + distance * (float) Math.sin(degree * i);
                float y = player[0].Y + distance * (float) Math.cos(degree * i);
                enemies.add(new EnemyTypes.Circle(x, y, invSpeed));
            }
        } 
        else 
        {
            for(int i = 1; i < (ballN/2); i++) 
            {
                float x = (i * 2 * width) / (ballN);
                float y = 0;
                enemies.add(new EnemyTypes.Circle(x, y, invSpeed));
            }

            for(int i = (ballN/2) + 1; i < ballN; i++) 
            {
                float x = ((i-ballN/2)*2*width)/ballN;
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
            float r = (float)Math.sqrt(Math.pow(player[0].X - x, 2) + Math.pow(player[0].Y - y, 2));
            
            while(r < distanceLimit)
            {
            	x = (float)Math.random()*width;
            	y = (float)Math.random()*height;
            	r = (float)Math.sqrt(Math.pow(player[0].X - x, 2) + Math.pow(player[0].Y - y, 2));
            }
            
            enemies.add(new EnemyTypes.Monster(x, y, .8f));
        }

        spawnMonsterB = false;
    }
    
    private void spawnRandomers() 
    {
        for (int i = 0; i < randomN; i++) 
        {
            float x = (float) Math.random() * width;
            float y = (float) Math.random() * height;
            float r = (float) Math.sqrt(Math.pow(player[0].X - x, 2) + Math.pow(player[0].Y - x, 2));

            while (r < distanceLimit) 
            {
                x = (float) Math.random() * width;
                y = (float) Math.random() * height;
                r = (float) Math.sqrt(Math.pow(player[0].X - x, 2) + Math.pow(player[0].Y - y, 2));
            }
            
            enemies.add(new EnemyTypes.Random(x, y, 0.5f, borders));
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
           
    private class scbClass implements util.SetCallback 
    {
        public void add(Object o) 
        {
            enemies.add(o);
        }
    }
           
    private void spawnBomb()                           
    {
        float x,y;
        if(Math.random() > .5) { //top/bottom
            y = (Math.random()>.5)? borders[1]: borders[3];
            x = (float)Math.random()*borders[2]-borders[0]; //width
        }
        else {
            x = (Math.random()>.5)? borders[0]: borders[2];
            y = (float)Math.random()*borders[3]-borders[1]; //height
        }
       enemies.add(new EnemyTypes.Bomb(x, y, 1f, borders, scbInstance));
    }
    
    int iter = 0;
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
                        
                        if(((player[0].lives == 0)&&(player[1].lives == 0))||((player[0].lives == 0)&&(!player[1].isActive)))
                        {              
                            if(highscore < score)
                            {
                                highscore = score;
                                highscoreL.setText(String.valueOf(highscore));
                            }
                            break;
                        } 
                        
                        timeElapse = 1+System.currentTimeMillis()-startTime;  
                        programLoopCounter++;
                        if(programLoopCounter % 100 == 0)
                        {
                            float loopPerSec = (float)programLoopCounter/timeElapse;
                            programSpeedAdjust = (0.3f)/loopPerSec;
                        }
                                               
                        if(timeElapse > timeCircle)
                        {
                            timeCircle = timeElapse + timeDifficulty1;
                            ballN++;
                            distance++;
                            spawnIncrease = true;
                        }
                        
                        if(timeElapse > timeRain)
                        {
                            timeRain = timeElapse + timeDifficulty2;
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
                            deleteIf(new EnemyPredicate() {
                                public boolean satisfiedBy(Enemy e) {
                                    return e.getClass().equals(EnemyTypes.Circle.class);
                                }
                            });
                            spawnBomb();
                            if(iter++%3==0)
                            {
                            	spawnMonsters();
                            }
                            ballN = 1;
                            distance = defaultDistance;
                            circular = !circular;                           
                        }
                        
                        deleteIf(new EnemyPredicate() {
                            public boolean satisfiedBy(Enemy e) {
                                return e.getClass().equals(EnemyTypes.Shrapnel.class)
                                    && (System.currentTimeMillis() - ((EnemyTypes.Shrapnel)e).getBorn() > shrapnelLifetime);
                            }
                        });
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
    
    private void deleteIf(EnemyPredicate p) {
        Set newEnemies = new HashSet(enemies.size());
        Iterator i = enemies.iterator();
        while (i.hasNext()) {
            Enemy e = (Enemy) i.next();
            if (!p.satisfiedBy(e)) {
                newEnemies.add(e);
            }
        }
        enemies = newEnemies;
    }
    
    private void movePlayers()
    {
    	if(up1)
    	{
            player[1].Y -= mouseSpeed;    		
    	}
    	if(down1)
    	{
            player[1].Y += mouseSpeed;
    	}
    	if(left1)
    	{
            player[1].X -= mouseSpeed;
    	}
    	if(right1)
    	{
            player[1].X += mouseSpeed;
    	}
        if(up2)
    	{
            player[1].Y -= mouseSpeed;    		
    	}
    	if(down2)
    	{
            player[1].Y += mouseSpeed;
    	}
    	if(left2)
    	{
            player[1].X -= mouseSpeed;
    	}
    	if(right2)
    	{
            player[1].X += mouseSpeed;
    	}
    }
   
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
       	movePlayers();
       	
        height = this.getHeight();
        width = this.getWidth();
        
        g.setColor(Color.white);
        g.fillOval(player[0].X-5, player[0].Y-5, 10, 10);
        g.drawString("Score", width-50, height-35);
        g.drawString(String.valueOf(score), width-60, height-20);
        
        if(countdownF)
        {
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
            while(i.hasNext()) 
            {
                Enemy e = (Enemy)i.next();
                e.move(player[0].X, player[0].Y, programSpeedAdjust);
                e.paint(g);
                
                for(int x = 0; x < 2; x++)
                {
                    if(player[x].isActive)
                    {
                        if(e.collidesWith(player[x].X, player[x].Y))
                        {
                            player[x].lives--;
                            player[x].respawn = true;
                        }
                    }
                }
            }
            
        }     
        drawLayout(g);       
    }
      
    public void mouseDragged(MouseEvent e)
    {
        player[0].X = e.getX();
        player[0].Y = e.getY();    
    }
    
    public void mouseMoved(MouseEvent e)
    {
        player[0].X = e.getX();
        player[0].Y = e.getY();   
    }
       
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == easy)
        {          
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