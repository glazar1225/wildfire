import processing.core.PImage;

import java.util.List;

public abstract class ExecutableEntity extends AnimatedEntity{
    protected double actionPeriod;


    public ExecutableEntity(String id, Point p, List<PImage> i, double ap, double anp) {
        super(id,p,i,anp);
        this.actionPeriod = ap;
    }
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, new Animation(this, 0), this.animationPeriod);
    }

    public abstract void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore);


    protected double getActionPeriod() {return this.actionPeriod;
    }
}
