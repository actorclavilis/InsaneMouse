package player;

import java.awt.event.*;
import util.*;

public class MouseControlledPlayer extends Player implements MouseMotionListener, MouseListener {
    public MouseControlledPlayer(int _x, int _y, int _lives, boolean _active, int _sbsk, EnemyDeletable _parent, int _infoOffset, int _playerNumber, int _width) {
        super(_x, _y, _lives, _active, _sbsk, _parent, _infoOffset, _playerNumber, _width);
    }

    public void mouseDragged(MouseEvent e) {
        if(isActive) {
            x = e.getX();
            y = e.getY();
        }
        e.consume();
    }

    public void mouseMoved(MouseEvent e) {
        mouseDragged(e);
    }
    
    public void move() {
        //moves on mouse event only
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        this.senbonSakura();
        e.consume();
    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}
