package enemies;

import java.awt.*;

public abstract class Enemy {

    protected float x, y;

    public Enemy /*
             * Number One
             */(float _x, float _y) {
        x = _x;
        y = _y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Color getColor() {
        return Color.WHITE;
    }

    public abstract void move(int mx, int my);

    public boolean collidesWith(int _x, int _y) {
        float p1 = (_x - 5) - y;
        float p2 = (_y - 5) - x;
        float h = (float) Math.pow(p2, 2) + (float) Math.pow(p1, 2);
        return h < 25;
    }

    public void paint(Graphics g) {
        Color old = g.getColor();
        g.setColor(this.getColor());
        g.fillOval((int) x, (int) y, 10, 10);
        g.setColor(old);
    }

    public boolean isMortal() {
        return false;
    }
}