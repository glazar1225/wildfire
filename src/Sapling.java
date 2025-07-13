import java.util.List;

import processing.core.PImage;

public final class Sapling extends Plant{
    public static final String KEY = "sapling";
    public static final int HEALTH_LIMIT = 5;
    public static final int ANIMATION_PERIOD = 1;
    public static final int ACTION_PERIOD = 1;
    public static final int NUM_PROPERTIES = 1;
    public static final int HEALTH_IDX = 0;

    private final int healthLimit;
    public Sapling(String id, Point p, List<PImage> im, int h) {
        super(id, p, im, ACTION_PERIOD, ANIMATION_PERIOD, h);
        this.healthLimit = HEALTH_LIMIT;
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = new Stump(Stump.KEY + "_" + this.id, this.position, imageStore.getImageList(Stump.KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.health >= this.healthLimit) {
            Tree tree = new Tree(Tree.KEY + "_" + this.id, this.position,
                    Functions.getNumFromRange(Tree.ACTION_MAX, Tree.ACTION_MIN),
                    Functions.getNumFromRange(Tree.ANIMATION_MAX, Tree.ANIMATION_MIN),
                    Functions.getIntFromRange(Tree.HEALTH_MAX, Tree.HEALTH_MIN),
                    imageStore.getImageList(Tree.KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }

    @Override
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        this.health++;
        if (!this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
        }
    }
}