public class AnimationClass implements Action{
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationClass(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        if (this.entity instanceof Animation) {
            ((Animation)this.entity).nextImage();

            if (this.repeatCount != 1) {
                scheduler.scheduleEvent(this.entity,
                        createAnimationAction(this.entity,
                                Math.max(this.repeatCount - 1, 0)),
                        ((Animation) this.entity).getAnimationPeriod());
            }
        }
    }

    public static AnimationClass createAnimationAction(Entity entity, int repeatCount)
    {
        return new AnimationClass(entity, null, null, repeatCount);
    }
}
