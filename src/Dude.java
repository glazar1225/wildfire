import processing.core.PImage;

import java.util.List;

public abstract class Dude extends MoveEntity implements CanTransform{

    public static final int ACTION_PERIOD_IDX = 0;
    public static final int ANIMATION_PERIOD_IDX = 1;
    public static final int RESOURCE_LIMIT_IDX = 2;
    public static final int NUM_PROPERTIES = 3;
    public static final String KEY = "dude";
    protected static final PathingStrategy Dude_PATHING = new
            AStarPathingStrategy();



    protected int resourceLimit;


    public Dude(String id, Point p, List<PImage> i, int rl, double ap, double anp) {
        super(id, p, i, ap, anp, Dude_PATHING);
        this.resourceLimit = rl;
    }
    public double getAnimationPeriod(){return this.animationPeriod;}


    protected void transformToDudeOnFire(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        DudeOnFire dudeOnFire = new DudeOnFire(
                this.getId(),
                this.getPosition(),
                imageStore.getImageList(DudeOnFire.KEY),  // Use the instance variable
                this.resourceLimit,
                this.getActionPeriod(),
                this.getAnimationPeriod()
        );
        world.removeEntity(scheduler, this);
        scheduler.unscheduleAllEvents(this);
        world.addEntity(dudeOnFire);
        dudeOnFire.scheduleActions(scheduler, world, imageStore);
    }

    //this allows the dude to walk on the fire in order to transform
    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> path = Dude_PATHING.computePath(
                this.position,
                destPos,
                p -> world.withinBounds(p) &&
                        (world.getOccupancyCell(p) == null || world.getOccupancyCell(p) instanceof Wildfire),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        return path.isEmpty() ? this.position : path.get(0);
    }

    /*
    public Point nextPosition(WorldModel world, Point destPos) {

        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
                newPos = this.position;
            }
        }
        return newPos;
    }

 */
}

