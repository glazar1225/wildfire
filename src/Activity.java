public final class Activity extends Action{
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(Entity e, WorldModel w, ImageStore is){
        super(e);
        this.world = w;
        this.imageStore = is;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        if (entity instanceof ExecutableEntity e)
            e.executeActivity(scheduler, world, imageStore);
        }
}
