public final class Animation extends Action{
    private final int repeatCount;
    public Animation(Entity e, int rc){
        super(e);
        this.repeatCount = rc;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        entity.nextImage();
        if (repeatCount != 1) {
            if (entity instanceof AnimatedEntity a){
            scheduler.scheduleEvent(entity, new Animation(entity, Math.max(repeatCount - 1, 0)), a.animationPeriod);
        }
        }
    }
}
