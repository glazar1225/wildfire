import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends Entity {
    protected double animationPeriod;

    public AnimatedEntity(String id, Point p, List<PImage> i, double anp) {
        super(id, p, i);
        this.animationPeriod = anp;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Animation(this, 0), this.animationPeriod);
    }
}
