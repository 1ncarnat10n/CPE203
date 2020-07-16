import processing.core.PImage;

import java.util.List;

public class Fish extends Execute{
    private static final String CRAB_KEY = "crab";
    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;

    public Fish(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = super.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Crab crab = Crab.createCrab(super.getId() + CRAB_ID_SUFFIX,
                pos, super.getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        Functions.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY));

        world.addEntity(crab);
        (crab).scheduleActions(world, imageStore, scheduler);
    }

    protected static Fish createFish(String id, Point position, int actionPeriod,
                           List<PImage> images)
    {
        return new Fish(id, position, images, actionPeriod);
    }
}
