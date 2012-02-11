/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author harrison
 */
public class MouseControlledPlayer extends Player implements MouseMotionListener {
    public MouseControlledPlayer(int _x, int _y, int _lives, boolean _active) {
        super(_x, _y, _lives, _active);
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
}
