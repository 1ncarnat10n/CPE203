import processing.core.PImage;

import java.util.List;

public class Quake extends Animation{
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);

    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    protected void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                AnimationClass.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }

    protected static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }


}
