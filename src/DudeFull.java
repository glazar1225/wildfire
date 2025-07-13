import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude{
    public DudeFull(String id, Point p, double ap, double anp, int rl, List<PImage> i) {
        super(id, p, i, rl, ap, anp);
    }

    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler, imageStore)) {
            this.transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
        }
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore) {
        if (this.position.adjacent(target.position)) {
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.position);

            if (!this.position.equals(nextPos)) {
                if (world.getOccupancyCell(nextPos) instanceof Wildfire) {
                    this.transformToDudeOnFire(world, scheduler, imageStore);
                    return true;
                }
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
//public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
//    Point nextPos = this.nextPosition(world, target.position);
//
//    // 🔥 Check if next position is fire -> Transform
//    if (world.withinBounds(nextPos) && world.getOccupancyCell(nextPos) instanceof Wildfire) {
//        System.out.println("🔥 DudeFull caught fire! Transforming...");
//        this.transformToDudeOnFire(world, scheduler, imageStore);
//        return true;
//    }
//
//    // 🏠 If adjacent to house, stop moving
//    if (this.position.adjacent(target.position)) {
//        return true;
//    } else {
//        if (!this.position.equals(nextPos)) {
//            world.moveEntity(scheduler, this, nextPos);
//        }
//        return false;
//    }
//}


    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)  {
        DudeNotFull dude = new DudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit,this.images);
        world.removeEntity(scheduler, this);
        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
        return true;
    }

}
