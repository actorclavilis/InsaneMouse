package enemies;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import util.SetCallback;

public final class EnemyTypes 
{
    private EnemyTypes() 
    {
    }

    public static class Circle extends Enemy 
    {
        protected float invSpeed = 30000;

        public Color getColor() 
        {
            return Color.RED;
        }

        public Circle(float _x, float _y, float _invSpeed) 
        {
            super(_x, _y);
            invSpeed = _invSpeed;
        }

        public void move(int mx, int my) 
        {
            int directionX = 1;
            int directionY = 1;
            float p1 = (my - 5) - y;
            float p2 = (mx - 5) - x;

            if(p1 < 0) 
            {
                directionY = -1;
            }
            
            if(p2 < 0) 
            {
                directionX = -1;
            }

            float angle = (float) Math.atan(p1 / p2);
            float dx = (float)Math.pow(p2, 2) + (float)Math.pow(p1, 2);
            float deltaD = dx/invSpeed;
            float deltaX = deltaD * (float) Math.abs(Math.cos(angle)) * directionX;
            float deltaY = deltaD * (float) Math.abs(Math.sin(angle)) * directionY;

            x += deltaX;
            y += deltaY;
        }

        public boolean isMortal() 
        {
            return true;
        }
    }

    public static class Monster extends Enemy 
    {
        protected float speed = 8;

        public Color getColor() 
        {
            return Color.MAGENTA.darker();
        }

        public Monster(float _x, float _y, float _speed) 
        {
            super(_x, _y);
            speed = _speed;
        }

        public void move(int mx, int my) 
        {
            int directionX = 1;
            int directionY = 1;
            float p1 = (my - 5) - y;
            float p2 = (mx - 5) - x;

            if(p1 < 0) 
            {
                directionY = -1;
            }
            if(p2 < 0) 
            {
                directionX = -1;
            }

            float angle = (float) Math.atan(p1 / p2);
            float deltaX = speed * (float) Math.abs(Math.cos(angle)) * directionX;
            float deltaY = speed * (float) Math.abs(Math.sin(angle)) * directionY;

            x += deltaX;
            y += deltaY;
        }
    }

    public static class Random extends Enemy 
    {
        private float vx, vy;
        int[] borders;

        public Color getColor() 
        {
            return Color.GREEN;
        }

        public Random(float _x, float _y, float _speed, int[] _borders) 
        {
            super(_x, _y);
            double ang = Math.random() * Math.PI;
            int qx = (Math.random() > .5)? 1: -1;
            int qy = (Math.random() > .5)? 1: -1;
            vx = _speed * qx * (float) Math.abs(Math.cos(ang));
            vy = _speed * qy * (float) Math.abs(Math.sin(ang));
            
            borders = _borders;
        }

        public Random(int _x, int _y) 
        {
            this(_x, _y, 4, null);
        }

        public void move(int mx, int my)
        {     
            if(x > borders[2])
            {                
                vx = Math.abs(vx)*-1;
            }
            if(x < borders[0])
            {
            	vx = Math.abs(vx);
            }
            if(y > borders[3])
            {                
                vy = Math.abs(vy)*-1;
            }
            if(y < borders[1])
            {
            	vy = Math.abs(vy);
            }
            
            x += vx;
            y += vy;
        }
    }

    public static class Rain extends Enemy 
    {
        private float vx, vy;

        public Color getColor() 
        {
            return Color.YELLOW;
        }

        public Rain(float _x, float _y, float _speed) 
        {
            super(_x, _y);
            vx = -_speed;
            vy = _speed;
        }

        public Rain(int _x, int _y) 
        {
            this(_x, _y, (float)2.4);
        }

        public void move(int mx, int my) 
        {
            x += vx;
            y += vy;
        }
    }
    
    public static class Bomb extends Monster {
        private SetCallback mod;
        private int[] borders;
        private static final int PIECES = 50;
        private boolean existant = true;
        
        public Color getColor() {
            return Color.BLUE;
        }
        
        public Bomb (float _x, float _y, float _speed, int[] _borders, SetCallback _mod) {
            super(_x, _y, _speed);
            mod = _mod;
            borders = _borders;
        }
        
        public void move(int mx, int my) {
            if(existant) {
                super.move(mx, my);
                if(distanceFrom(mx, my) < 60000) {
                    for (int i = 0; i < PIECES; i++) {
                        Enemy e = new EnemyTypes.Random (x, y, speed*3, borders) {
                            public boolean isMortal() {
                                return true;
                            }
                        };
                        mod.add(e);
                    }
                    existant = false;
                }
            }
        }
        
        public boolean isMortal() {
            return existant;
        }
        
        public void paint(Graphics g) {
            if(existant)
                super.paint(g);
        }
        
        public boolean collidesWith(int mx, int my) {
            return existant && super.collidesWith(mx, my);
        }
    }
}