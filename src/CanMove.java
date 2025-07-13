public interface CanMove {
    boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore);
    Point nextPosition(WorldModel world, Point destPos);
    

}