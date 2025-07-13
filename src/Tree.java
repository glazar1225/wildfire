import java.util.List;

import processing.core.PImage;

final class Tree extends Plant {
    public static final String KEY = "tree";
    public static final int NUM_PROPERTIES = 3;
    public static final int ANIMATION_PERIOD_IDX = 0;
    public static final int ACTION_PERIOD_IDX = 1;
    public static final Double ANIMATION_MAX = 0.600;
    public static final double ANIMATION_MIN = 0.050;
    public static final double ACTION_MAX = 1.400;
    public static final double ACTION_MIN = 1.000;
    public static final int HEALTH_MAX = 3;
    public static final int HEALTH_MIN = 1;
    public static final int HEALTH_IDX = 2;

    public Tree(String i, Point p, double ap, double anp, int h, List<PImage> im) {
        super(i, p, im, ap, anp, h);
        //this.imageIndex = 20;
        //System.out.println(this.imageIndex);
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = new Stump(Stump.KEY + "_"+this.id, this.getPosition(), imageStore.getImageList(Stump.KEY));
            world.removeEntity(scheduler, this);
            world.addEntity(stump);
            return true;
        }
        return false;
    }
}
