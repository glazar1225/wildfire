import org.junit.jupiter.api.parallel.ResourceLock;
import processing.core.PImage;

import java.util.*;

public class Wildfire extends ExecutableEntity{
    public static final int NUM_PROPERTIES = 2;
    public static final int ACTION_PERIOD_IDX = 0;
    public static final int ANIMATION_PERIOD_IDX = 1;
    private int fire_iter;
    public static final String KEY = "fire";
    private Random rand = new Random();

    public Wildfire(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images, int iter) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.fire_iter = iter;
    }

    @Override
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(House.class)));
        //System.out.println("fire execute activity");
        if (this.fire_iter < 6) {
            this.spread(scheduler, world, imageStore);
        }
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
    }
    /*
    @Override
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Optional<Entity> fairyTarget = world.findNearest(this.position, new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Sapling sapling = new Sapling( Sapling.KEY + "_"+fairyTarget.get().id, tgtPos, imageStore.getImageList(Sapling.KEY), 3);

                world.addEntity(sapling);

                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
    }

     */
    public boolean spread(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        //System.out.println("hi");
        List<Point> neighbors = List.of(new Point[]{ new Point(this.position.x+1, this.position.y),
                                                    new Point(this.position.x-1, this.position.y),
                                                    new Point(this.position.x, this.position.y-1),
                                                    new Point(this.position.x, this.position.y+1)
                                                    });
        for (Point p: neighbors){
            Optional<Entity> occ = world.getOccupant(p);
            if (occ.isEmpty() || (!(occ.get() instanceof Wildfire|| occ.get() instanceof Obstacle))) {
                if (rand.nextDouble() < 0.15) {
                    Wildfire fire = new Wildfire(this.id, p, this.actionPeriod, this.animationPeriod, this.images, this.fire_iter + 1);
                    //world.removeEntity(scheduler, this);
                    world.addEntity(fire);
                    fire.scheduleActions(scheduler, world, imageStore);
                }
            }
        }
        this.fire_iter ++;
        return true;
    }
}
