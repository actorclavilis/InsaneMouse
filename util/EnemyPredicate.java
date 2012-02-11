/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import enemies.Enemy;

/**
 *
 * @author harrison
 */
public interface EnemyPredicate {
    public boolean satisfiedBy(Enemy e);
}
