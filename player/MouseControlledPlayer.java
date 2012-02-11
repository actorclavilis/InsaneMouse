/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.event.*;
import util.*;

/**
 *
 * @author harrison
 */
public class MouseControlledPlayer extends Player implements MouseMotionListener, MouseListener {
    public MouseControlledPlayer(int _x, int _y, int _lives, boolean _active, int _sbsk, EnemyDeletable _parent) {
        super(_x, _y, _lives, _active, _sbsk, _parent);
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

    public void mouseClicked(MouseEvent e) {
        this.senbonSakura();
        e.consume();
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}
