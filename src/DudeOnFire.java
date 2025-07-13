import processing.core.PImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeOnFire extends Dude{
    public DudeOnFire(String id, Point p, List<PImage> i, int rl, double ap, double anp) {
        super(id, p, i, rl, ap, anp);
    }
    public static final String KEY = "dudeonfire";

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore) {
        Optional<Entity> nearestWater = world.findNearest(this.position, List.of(Obstacle.class));

        if (nearestWater.isPresent()) {
            Point fountainPos = nearestWater.get().position;

            if (this.position.adjacent(fountainPos)) {
                this.transform(world, scheduler, imageStore);
                return true;
            } else {
                Point nextPos = this.nextPosition(world, fountainPos);
                if (!this.position.equals(nextPos)) {
                    world.moveEntity(scheduler, this, nextPos);
                }
            }
        }
        return false;
    }



    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        MoveEntity dude = new DudeFull(this.getId(), this.getPosition(),this.actionPeriod,
                this.getAnimationPeriod(),  this.resourceLimit, imageStore.getImageList(Dude.KEY));

        world.removeEntity(scheduler, this);
        //scheduler.unscheduleAllEvents(this);
        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
        return true;
        /*
        DudeNotFull dude = new DudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit,this.images);
        world.removeEntity(scheduler, this);
        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
        return true;
         */
    }

    @Override
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler, imageStore)) {
            this.transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
        }

    }
    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> path = Dude_PATHING.computePath(
                this.position,
                destPos,
                p -> world.withinBounds(p) &&
                        world.getOccupant(p).map(e -> !(e instanceof Tree) && !(e instanceof Wildfire)).orElse(true), // Avoid trees// DudeOnFire can move through anything
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        return path.isEmpty() ? this.position : path.get(0);
    }

}





