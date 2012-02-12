package util;
import enemies.Enemy;

public interface EnemyPredicate 
{
    public boolean satisfiedBy(Enemy e);
}
