import processing.core.PImage;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;


public class Fairy extends MoveEntity{
    public static final String KEY = "fairy";
    public static final int ANIMATION_PERIOD_IDX = 0;
    public static final int ACTION_PERIOD_IDX = 1;
    public static final int NUM_PROPERTIES = 2;
    private static final PathingStrategy Fairy_PATHING = new AStarPathingStrategy();



    public Fairy(String id, Point position, double ap, double anp, List<PImage> images) {
        super (id, position, images, ap, anp, Fairy_PATHING);
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore) {
        // public Entity(String id, Point p, List<PImage> i, int rl, int rc, int ap, int anp
        if (this.position.adjacent(target.position)) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    @Override
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Optional<Entity> fairyTarget = world.findNearest(this.position, new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (this.moveTo(world, fairyTarget.get(), scheduler, imageStore)) {

                Sapling sapling = new Sapling( Sapling.KEY + "_"+fairyTarget.get().id, tgtPos, imageStore.getImageList(Sapling.KEY), 3);

                world.addEntity(sapling);

                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
    }

    /*
    public Point nextPosition(WorldModel world, Point destPos) {

        Predicate<Point> isNotOccupied = point -> (!world.isOccupied(point));
        //placeholder to pass something. not used for fairy one steo impl
        BiPredicate<Point, Point> placeholderBP = (p1,p2) -> (true);

        List<Point> path = strategy.computePath(this.position, destPos, isNotOccupied,
                placeholderBP, this::adjacent;


        if (path.size() == 0)
            return this.getPosition();
        else
            return path.get(0);
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }
         */
}
