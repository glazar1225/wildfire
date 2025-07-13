import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude {

    protected int resourceCount;

    public DudeNotFull(String id, Point p, double ap, double anp, int rl, List<PImage> i) {
        super(id, p, i, rl, ap, anp);
        this.resourceCount = 0;
    }
//    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
//        Point nextPos = this.nextPosition(world, target.position);
//
//        // 🔥 Check if next position is fire -> Transform
//        if (world.withinBounds(nextPos) && world.getOccupancyCell(nextPos) instanceof Wildfire) {
//            System.out.println("🔥 DudeNotFull caught fire! Transforming...");
//            this.transformToDudeOnFire(world, scheduler, imageStore);
//            return true;
//        }
//
//        // 🌲 Collect resources if adjacent
//        if (this.position.adjacent(target.position)) {
//            this.resourceCount += 1;
//            if (target instanceof Plant p) {
//                p.health--; // Reduce plant health
//            }
//            return true;
//        } else {
//            if (!this.position.equals(nextPos)) {
//                world.moveEntity(scheduler, this, nextPos);
//            }
//            return false;
//        }
//    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore) {
        if (this.position.adjacent(target.position)) {
            this.resourceCount += 1;
            if (target instanceof Plant p) {
                p.health--;
            }
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

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            DudeFull dude = new DudeFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
//    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//        // Check if Dude touches Fire
//        if (world.getOccupancyCell(this.position) instanceof Wildfire) {
//            transformToDudeOnFire(world, scheduler, imageStore);
//            return; // Stop further execution since the dude is now on fire
//        }
//
//        Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(House.class)));
//
//        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler)) {
//            this.transform(world, scheduler, imageStore);
//            return; // Transformation happens, no need to schedule another action
//        }
//
//        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
//    }

    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Optional<Entity> target = world.findNearest(this.position, new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (target.isEmpty() || !this.moveTo(world, (Plant) target.get(), scheduler, imageStore) || !this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
        }
    }

}
