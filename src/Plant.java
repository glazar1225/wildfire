import java.util.List;

import processing.core.PImage;

abstract class Plant extends ExecutableEntity implements CanTransform {
    protected int health;
    //protected int healthLimit;

    public Plant(String id, Point p, List<PImage> im, double ap, double anp, int h) {
        super(id, p, im, ap, anp);
        this.health = h;
        //this.healthLimit = hl;
    }

    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        if (!this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
        }
    }
    public int getHealth(){return this.health;}
    //public int getHealthLimit(){return this.healthLimit;}
}