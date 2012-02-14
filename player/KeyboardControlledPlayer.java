package player;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import util.EnemyDeletable;

public class KeyboardControlledPlayer extends Player implements KeyEventDispatcher {
    private int up, left, down, right, sbskey, sbsRkey;
    private float speed;
    private boolean mUp = false, mLeft = false, mDown = false, mRight = false;
    
    public KeyboardControlledPlayer(int _x, int _y, int _lives, boolean _active,
                                    EnemyDeletable _parent, int _infoOffset,
                                    int _up, int _left, int _down, int _right,
                                    int _speed, int _sbskey, int _sbsRkey, int _playerNumber, int _width) {
        super(_x, _y, _lives, _active, _parent, _infoOffset, _playerNumber, _width);
        up = _up; down = _down; left = _left; right = _right;
        speed = (float)_speed/100;
        sbskey = _sbskey; sbsRkey = _sbsRkey;
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        boolean pressed = e.getID() == KeyEvent.KEY_PRESSED;
        int ev = e.getKeyCode();
        if (ev == up) {
            mUp = pressed;
            return true;
        }
        if (ev == left) {
            mLeft = pressed;
            return true;
        }
        if (ev == down) {
            mDown = pressed;
            return true;
        }
        if (ev == right) {
            mRight = pressed;
            return true;
        }
        if (ev == sbskey) {
            senbonSakura(false);
            return true;
        }
        if (ev == sbsRkey) {
            senbonSakura(true);
            return true;
        }
        return false;
    }

    public void move() {
        if(mRight)
        {
            x += speed;
        }
        if(mLeft)
        {
            x -= speed;
        }
        if(mDown)
        {
            y += speed;
        }
        if(mUp)
        {
            y -= speed;
        }
    }
}
