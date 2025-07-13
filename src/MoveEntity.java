import processing.core.PImage;

import java.nio.file.Path;
import java.util.List;

import static java.lang.Math.abs;

abstract public class MoveEntity extends ExecutableEntity implements CanMove{
    protected PathingStrategy strategy;

    public MoveEntity(String id, Point p, List<PImage> i, double ap, double anp, PathingStrategy ps){
        super(id,p,i,ap, anp);
        this.strategy = ps;
    }
    protected static boolean neighbors(Point p1, Point p2){
        if (p1.x == p2.x){
            if (abs(p1.y-p2.y) <= 1){
                return true;
            }
        }
        if (p1.y == p2.y){
            if (abs(p1.x - p2.x) <= 1){
                return true;
            }
        }
        return false;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> path = strategy.computePath(getPosition(), destPos,
                p -> world.withinBounds(p)&& !world.isOccupied(p), //canPassTrough
                MoveEntity::neighbors, //withinReach
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0)
            return this.getPosition();
        else
            return path.get(0);

    }
}
