
import player.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.awt.image.*;
import enemies.*;
import java.util.ArrayList;
import java.util.Arrays;
import util.*;

public class GUI extends JPanel implements ActionListener, EnemyDeletable
{
    private Dimension d; 
      
    private JPanel menu;
    private JButton easy, hard, back, howTo, howToBack;
    private JLabel highscoreL, howToIMGL, menuIMGL, keyboardSpeedL1, keyboardSpeedL2;
    private JRadioButton onePlayerRB, twoPlayerRB, mouseRB, keyboardRB;
    private JCheckBox musicCB;
    private JSlider keyboardSpeedS1, keyboardSpeedS2;
    
    private int ballN, monsterN, counterN, randomN, rainN, bombN, monsterMultiplier;
    private int multiplier,highscore, score, level;
    private int invSpeed, width, height, timeDifficulty1, distanceLimit;
    private int[] borders, deathLocation;
    
    private long startTime, timeElapse, timeLast, timeCircle, timeCircleSwitch, programLoopCounter;
    
    private boolean countdownF, spawnCircleB, spawnMonsterB, spawnRandomersB, spawnRainB;
    private boolean circular, spawnIncrease, onePlayerAlive;
    
    private float distance, defaultDistance, programSpeedAdjust;
    
    private Set enemies;
    private java.util.List players;
    private Thread t, r;   
    private scbClass scbInstance = new scbClass();
    private MP3 mp3;    
    
    public GUI(Dimension a) throws Exception
    {                   
        d = a;
        
        this.setBackground(Color.darkGray);
        this.setLayout(null);
        this.setBounds(0, 0, d.width, d.height); 
        this.setFocusable(true);
        this.setVisible(true);
        
        highscore = 0;          
        width = this.getWidth();
        height = this.getHeight();
                     
        borders = new int[4];
        borders[0] = 15;
        borders[1] = 15;
        borders[2] = width-20;
        borders[3] = height-20;
        
        makeMenuScreen();
        this.add(menu);
    }

    private void playerSetup() {
        
        players = new ArrayList(twoPlayerRB.isSelected()?2:1);
        if (mouseRB.isSelected()) {
            MouseControlledPlayer p1 = new MouseControlledPlayer(width / 2, height / 2, 3, true, this, 3, 1, width);
            addMouseMotionListener(p1);
            addMouseListener(p1);
            players.add(p1);
        } else {
            KeyboardControlledPlayer p1 =
                    new KeyboardControlledPlayer(width / 2, height / 2, 3, true, this, 3, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, keyboardSpeedS1.getValue(), KeyEvent.VK_SPACE, KeyEvent.VK_F, 1, width);
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(p1);
            players.add(p1);
        }
        
        if(twoPlayerRB.isSelected()){
            if (mouseRB.isSelected()) {
                KeyboardControlledPlayer p2 =
                    new KeyboardControlledPlayer(width / 2, height / 2, 3, true, this, 3, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, keyboardSpeedS2.getValue(), KeyEvent.VK_SPACE, KeyEvent.VK_F, 2, width);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(p2);
                players.add(p2);
            }
            else
            {
                KeyboardControlledPlayer p2 =
                        new KeyboardControlledPlayer(width / 2, height / 2, 3, true, this, 3, KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD6, keyboardSpeedS2.getValue(), KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1, 2, width);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(p2);
                players.add(p2);
            }
        }
    }
        
    private void makeMenuScreen()
    {     
        menu = new JPanel();
        menu.setBackground(Color.BLACK);
        menu.setLayout(null);
        menu.setBounds(0, 0, width, height);   
        
        try
        { 
            BufferedImage menuIMG = ImageIO.read(new File("MenuBackground.png")); 
            menuIMGL = new JLabel(new ImageIcon(menuIMG.getScaledInstance((int)(width*0.8), (int)(height*0.8), Image.SCALE_SMOOTH)));
            menuIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
        
       
        highscoreL = new JLabel(String.valueOf(highscore));
        highscoreL.setBackground(Color.darkGray);
        highscoreL.setBounds((width/2)+100, (height/2)+70, 500, 100);
        highscoreL.setForeground(Color.white);
        
        easy = new JButton("Easy");
        hard = new JButton("Hard");
        
        easy.addActionListener(this);
        hard.addActionListener(this);
        
        easy.setBounds((width/2)-60, (height/2)-50, 120, 20);
        hard.setBounds((width/2)-60, height/2-10, 120, 20);
        
        onePlayerRB = new JRadioButton("One Player");
        twoPlayerRB = new JRadioButton("Two Player");
        mouseRB = new JRadioButton("Mouse (Player 1)");
        keyboardRB = new JRadioButton("Keyboard (Player 1)");
        keyboardSpeedS1 = new JSlider(JSlider.HORIZONTAL, 10, 300, 50);
        keyboardSpeedS2 = new JSlider(JSlider.HORIZONTAL, 10, 300, 50);
        musicCB = new JCheckBox("Music");
        
        onePlayerRB.setBackground(null);
        twoPlayerRB.setBackground(null);
        mouseRB.setBackground(null);
        keyboardRB.setBackground(null);
        keyboardSpeedS1.setBackground(null);  
        keyboardSpeedS2.setBackground(null);
        musicCB.setBackground(null);
        
        onePlayerRB.setForeground(Color.WHITE);
        twoPlayerRB.setForeground(Color.WHITE);
        mouseRB.setForeground(Color.WHITE);
        keyboardRB.setForeground(Color.WHITE);
        keyboardSpeedS1.setForeground(Color.WHITE);
        keyboardSpeedS2.setForeground(Color.WHITE);
        musicCB.setForeground(Color.WHITE);
        
        ButtonGroup playerChoice = new ButtonGroup();
        playerChoice.add(onePlayerRB);
        playerChoice.add(twoPlayerRB);
        onePlayerRB.setSelected(true);
        
        ButtonGroup peripheralChoice = new ButtonGroup();
        peripheralChoice.add(mouseRB);
        peripheralChoice.add(keyboardRB);
        mouseRB.setSelected(true);
        
        musicCB.setSelected(true);
        
        onePlayerRB.setBounds((width/2)+100, (height/2)-50, 100, 20);
        twoPlayerRB.setBounds((width/2)+100, (height/2)-30, 100, 20);
        mouseRB.setBounds((width/2)+100, (height/2), 200, 20);
        keyboardRB.setBounds((width/2)+100, (height/2)+20, 200, 20);
        keyboardSpeedS1.setBounds(width/2-120, height/2+100, 200, 50);
        keyboardSpeedS2.setBounds(width/2-120, height/2+183, 200, 50);
        musicCB.setBounds((width/2)+100, (height/2)+50, 100, 20);
 
        keyboardSpeedL1 = new JLabel("Keyboard Speed (Player One)");
        keyboardSpeedL1.setForeground(Color.WHITE);
        keyboardSpeedL1.setBounds(width/2-113, height/2+67, 200, 50);
        
        keyboardSpeedL2 = new JLabel("Keyboard Speed (Player Two)");
        keyboardSpeedL2.setForeground(Color.WHITE);
        keyboardSpeedL2.setBounds(width/2-113, height/2+150, 200, 50);
        
        howTo = new JButton("How To Play");
        howTo.addActionListener(this);
        howTo.setBounds((width/2)-60, height/2+30, 120, 20);
                   
        try
        { 
            BufferedImage howToIMG = ImageIO.read(new File("HowTo.png")); 
            howToIMGL = new JLabel(new ImageIcon(howToIMG));
            howToIMGL.setBounds(width/2-howToIMG.getWidth()/2,height/2-howToIMG.getHeight()/2,howToIMG.getWidth(),howToIMG.getHeight());
        }catch(Exception e) {}
             
        howToBack = new JButton("X");
        howToBack.setBounds((int)(width/2+width*0.25)-50, (int)(height/2-height*0.25), 50, 50);
        howToBack.setBackground(Color.BLACK);
        howToBack.setForeground(Color.WHITE);
        howToBack.addActionListener(this);
               
        menu.add(easy);
        menu.add(hard);
        menu.add(howTo);
        menu.add(highscoreL);
        menu.add(onePlayerRB);
        menu.add(twoPlayerRB);
        menu.add(mouseRB);
        menu.add(keyboardRB);
        menu.add(keyboardSpeedL1);
        menu.add(keyboardSpeedL2);
        menu.add(keyboardSpeedS1);
        menu.add(keyboardSpeedS2);
        menu.add(musicCB);   
        menu.add(menuIMGL);
        
        back = new JButton("Back");
        back.setBounds(width/2-40, height/2, 100, 20);
        back.addActionListener(this);
        back.setVisible(false);
        this.add(back);       
    }
    
    private void audioSetup() {
        if(musicCB.isSelected())
        {
            try 
            {
                if(mp3 == null)
                {
                    mp3 = new MP3();
                }
            } catch (Exception e) {}
            mp3.play();
        }
    }
    
    private void setup()
    {
        playerSetup();
        
        deathLocation = new int[4];
        Arrays.fill(deathLocation, -1);
        
        menu.setVisible(false);
        this.revalidate();
        
        audioSetup();
        
        Iterator i = players.iterator();
        Player p;
        while(i.hasNext()) {
            p = (Player)i.next();
            p.resetLives(3);
            p.setActive(true);
        }
        
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
        timeCircleSwitch = 0;
        programLoopCounter = 1;
        programSpeedAdjust = 1;
        
        onePlayerAlive = true;                      
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
                rainN = 30;
                defaultDistance = 600;
                timeLast = 10000;
                spawnCircleB = true;    
                break;
            case 2:
                distance = 600;
                monsterN = 0;
                randomN = 30;
                rainN = 30;
                defaultDistance = 600;
                timeLast += 10000;
                spawnCircleB = true;
                break;
            case 3:
                distance = 600;
                monsterN = 30;
                randomN = 0;
                rainN = 0;
                defaultDistance = 600;
                timeLast += 10000;
                spawnCircleB = true;
                break;
            default:
                distance = (float)(Math.random()*200+400-level*5);
                monsterN = (int)(Math.random()*25+10+level);
                randomN = (int)(Math.random()*25+10+level);
                rainN = (int)(Math.random()*10+10+level);
                defaultDistance = distance;
                timeLast += 5000+level*1000;
                spawnCircleB = true;
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

        g.setColor(Color.red.brighter());
        Iterator f = players.iterator();
        int lifeDisplayPosition = 0;
        while(f.hasNext()) 
        {
            Player h = (Player)f.next();
            if(h.isActive())
            {
                lifeDisplayPosition++;
                String lifeInformation = "Player " + lifeDisplayPosition + ": ";
                for(int i = 0; i < h.getLives(); i++)
                {
                    lifeInformation += "\u2606";
                }
                g.drawString(lifeInformation, 20, lifeDisplayPosition*40);
            }
        }
        
        borders[2] = width-20;
        borders[3] = height-20;
        
        Iterator i = players.iterator();
        Player p;
        while(i.hasNext()) {
            p = (Player)i.next();
            if(p.isActive() && !countdownF &&
                    (p.getX() < borders[0] || p.getY() < borders[1] || p.getX() > borders[2] || p.getY() > borders[3])
                    &&(!p.getImmunity())){
                p.decLives(width / 2, height / 2);    
                p.setImmunity(true);
            }                        
            if(p.getLives() <= 0) 
            {         
                if (p instanceof MouseControlledPlayer) {
                    MouseControlledPlayer m = (MouseControlledPlayer) p;
                    removeMouseListener(m);
                    removeMouseMotionListener(m);
                } else if (p instanceof KeyboardControlledPlayer) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher((KeyboardControlledPlayer) p);
                }
                i.remove();
                for(int h = 0; h < 4; h++)
                {
                    if(deathLocation[h] == -1)
                    {
                        deathLocation[h] = p.getX();
                        deathLocation[h+1] = p.getY();
                        break;
                    }
                }
            }
        }
        
        if((deathLocation[0] != -1)&&(deathLocation[1] != -1))
        {
            g.drawLine(deathLocation[0]-15, deathLocation[1]-15, deathLocation[0]+15, deathLocation[1]+15);
            g.drawLine(deathLocation[0]+15, deathLocation[1]-15, deathLocation[0]-15, deathLocation[1]+15);
        }
        if((deathLocation[2] != -1)&&(deathLocation[3] != -1))
        {
            g.drawLine(deathLocation[2]-15, deathLocation[3]-15, deathLocation[2]+15, deathLocation[3]+15);
            g.drawLine(deathLocation[2]+15, deathLocation[3]-15, deathLocation[2]-15, deathLocation[3]+15);
        }
        
        if(!onePlayerAlive) {
            Font old = g.getFont();
            g.setFont(new Font("monospaced", Font.BOLD, 20));
            g.setColor(Color.red.darker());
            g.drawString("GAME OVER", width/2-40, height/2);
            g.setFont(old);
            g.setColor(Color.WHITE);
            g.drawString("Score", width - 60, 25);
            g.drawString(String.valueOf(score), width-60, 40);
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
                float x = ((Player)players.get(0)).getX() + distance * (float) Math.sin(degree * i);
                float y = ((Player)players.get(0)).getY() + distance * (float) Math.cos(degree * i);
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
            float r = (float)Math.sqrt(Math.pow(((Player)players.get(0)).getX() - x, 2) 
                    + Math.pow(((Player)players.get(0)).getY() - y, 2));
            
            while(r < distanceLimit)
            {
            	x = (float)Math.random()*width;
            	y = (float)Math.random()*height;
            	r = (float)Math.sqrt(Math.pow(((Player)players.get(0)).getX() - x, 2) 
                        + Math.pow(((Player)players.get(0)).getY() - y, 2));
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
            float r = (float) Math.sqrt(Math.pow(((Player)players.get(0)).getX() - x, 2) 
                    + Math.pow(((Player)players.get(0)).getY() - x, 2));

            while (r < distanceLimit) 
            {
                x = (float) Math.random() * width;
                y = (float) Math.random() * height;
                r = (float) Math.sqrt(Math.pow(((Player)players.get(0)).getX() - x, 2) 
                        + Math.pow(((Player)players.get(0)).getY() - y, 2));
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
            enemies.add(new EnemyTypes.Rain(x, y, 4, height));
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
    
    private boolean spawnBombB()
    {
        boolean bombPartsExist = false;
        try
        {
            Iterator i = enemies.iterator();
            while (i.hasNext()) 
            {
                Enemy e = (Enemy) i.next();
                if(e.getClass().equals(EnemyTypes.Bomb.class)||(e.getClass().equals(EnemyTypes.Shrapnel.class)))
                {
                    bombPartsExist = true;
                    break;
                }
            }
        }catch(Exception e)
        {}
        return !bombPartsExist;
    }
    
    private void spawnBomb()                           
    {
        float x,y;
        for(int i = 0; i < bombN; i++)
        {
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
                        
                        if(!onePlayerAlive)
                        {                       
                            if(highscore < score)
                            {
                                highscore = score;
                                highscoreL.setText(String.valueOf(highscore));
                            }
                            if(musicCB.isSelected())
                            {
                                mp3.stop();
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
                                               
                        if((timeElapse > timeCircle)&&(ballN < 20))
                        {
                            timeCircle = timeElapse + timeDifficulty1;
                            ballN++;
                            distance++;
                            spawnIncrease = true;
                        }
                        
                        score = (int)timeElapse*multiplier;
                        
                        if(timeElapse > timeLast)
                        {
                            level++;
                            levelSetup();
                        }                       
                        
                        if(timeElapse > timeCircleSwitch)
                        {
                            timeCircleSwitch = timeElapse + 10000;
                            deleteIf(new EnemyPredicate() {
                                public boolean satisfiedBy(Enemy e) {
                                    return e.getClass().equals(EnemyTypes.Circle.class);
                                }
                            });
                            if(iter++%2==0)
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
                                    &&(((EnemyTypes.Shrapnel)e).getBounces() > 1);
                            }
                        });
                        
                        deleteIf(new EnemyPredicate() {
                            public boolean satisfiedBy(Enemy e) {
                                return e.getClass().equals(EnemyTypes.Bomb.class)
                                    &&(((EnemyTypes.Bomb)e).isMortal());
                            }
                        });
                        
                        movePlayers();
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
    
    public void deleteIf(EnemyPredicate p) {
        try
        {
            Set newEnemies = new HashSet(enemies.size());
            Iterator i = enemies.iterator();
            while (i.hasNext()) {
                Enemy e = (Enemy) i.next();
                if (!p.satisfiedBy(e)) {
                    newEnemies.add(e);
                }
            }
            enemies = newEnemies;
        }catch(Exception e)
        {}
    }
    
    private void movePlayers()
    {
        onePlayerAlive = false;
        Iterator i = players.iterator();
        while(i.hasNext()) {
            Player p = (Player)i.next();
            if(p.getLives() == 0)
            {
                p.setActive(false);
            }
            if(p.isActive())
            {
                onePlayerAlive = true;
                p.move();
            }
        }
    }
    
    private void drawPlayers(Graphics g) 
    {           
        Iterator i = players.iterator();
        Player p;
        while(i.hasNext()) 
        {
            p = (Player)i.next();

            if(p.isActive())
            {
                if(p.getImmunity())
                {
                    g.setColor(Color.red.brighter());
                    g.drawOval(p.getX()-30, p.getY()-30, 60, 60);
                }
                g.setColor(Color.WHITE);
                g.fillOval(p.getX()-5, p.getY()-5, 10, 10);
            }

            p.paint(g);
        }
    }
   
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        height = this.getHeight();
        width = this.getWidth();
        
        drawPlayers(g);
        g.setColor(Color.WHITE);
        g.drawString("Score", width - 60, 25);
        g.drawString(String.valueOf(score), width-60, 40);
        
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
            if(spawnBombB())
            {
                spawnBomb();
            }
            
            try
            {
                Iterator i = enemies.iterator();
                while(i.hasNext()) 
                {
                    Enemy e = (Enemy)i.next();
                    Iterator j = players.iterator();
                    while(j.hasNext()) {
                        Player p = (Player)j.next();
                        e.move(players, programSpeedAdjust/*/players.size()*/);
                        if((e.collidesWith(p.getX(), p.getY()))&&(!p.getImmunity())) {
                            p.decLives(p.getX(), p.getY());
                            p.setImmunity(true);
                        }
                    }
                    e.paint(g);
                }
            }catch(Exception e)
            {               
            }
            
        }     
        drawLayout(g);       
    }
       
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == easy)
        {          
            invSpeed = 50000;
            bombN = 1;
            timeDifficulty1 = 1000;
            distanceLimit = 400;
            monsterMultiplier = 1;
            multiplier = 1;
            setup();
        }
        else if(e.getSource() == hard)
        {
            invSpeed = 30000;
            bombN = 4;
            timeDifficulty1 = 500;
            distanceLimit = 200;
            monsterMultiplier = 2;
            multiplier = 2;       
            setup();
        }
        else if(e.getSource() == back)
        {
            r = null;
            menu.setVisible(true);      	
            back.setVisible(false);
            this.revalidate();        
            repaint();            
        }
        else if(e.getSource() == howTo)
        {
            menu.removeAll();
            
            menu.add(howToBack);
            menu.add(howToIMGL);

            menu.revalidate();
            menu.repaint();         
        }
        else if(e.getSource() == howToBack)
        {           
            menu.remove(howToIMGL);
            menu.remove(howToBack);
            
            menu.add(keyboardSpeedL1);
            menu.add(keyboardSpeedL2);
            menu.add(easy);
            menu.add(hard);
            menu.add(howTo);
            menu.add(onePlayerRB);
            menu.add(twoPlayerRB);
            menu.add(mouseRB);
            menu.add(keyboardRB);
            menu.add(keyboardSpeedS1);
            menu.add(keyboardSpeedS2);
            menu.add(musicCB);   
            menu.add(highscoreL);
            menu.add(menuIMGL);
            
            menu.revalidate();
            menu.repaint();           
        }
    }
}