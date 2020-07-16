import processing.core.PImage;

import java.util.List;

public class Fish implements Execute{
    public static final String CRAB_KEY = "crab";
    public static final String CRAB_ID_SUFFIX = " -- crab";
    public static final int CRAB_PERIOD_SCALE = 4;
    public static final int CRAB_ANIMATION_MIN = 50;
    public static final int CRAB_ANIMATION_MAX = 150;

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Fish(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public Point getPosition() {
        return position;
    }
    public void setPosition(Point pos) {
        this.position = pos;
    }
    public void setResourceCount() {
        resourceCount += 1;
    }
    public int getEntityImageIndex() {
        return imageIndex;
    }
    public List<PImage> getEntityImages() {
        return images;
    }
    public int getActionPeriod() {
        return actionPeriod;
    }
    public int getAnimationPeriod() {return animationPeriod;}

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity crab = Crab.createCrab(this.id + CRAB_ID_SUFFIX,
                pos, this.actionPeriod / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        Functions.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY));

        world.addEntity(crab);
        ((Crab)crab).scheduleActions(world, imageStore, scheduler);
    }
    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage(Object entity)
    {
        if (entity instanceof Background)
        {
            return ((Background)entity).getImages()
                    .get(((Background)entity).getImageIndex());
        }
        else if (entity instanceof Entity)
        {
            return (images.get(imageIndex));
        }
        else
        {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }

    public static Fish createFish(String id, Point position, int actionPeriod,
                           List<PImage> images)
    {
        return new Fish(id, position, images, 0, 0,
                actionPeriod, 0);
    }
}
