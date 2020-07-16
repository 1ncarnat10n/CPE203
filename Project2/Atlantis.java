import processing.core.PImage;

import java.util.List;

public class Atlantis implements Animation {
    public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Atlantis(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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
    public List<PImage> getEntityImages() {return images;}
    public int getActionPeriod() {return actionPeriod;}
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
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

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.scheduleEvent(this,
                AnimationClass.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }

    public static Atlantis createAtlantis(String id, Point position,
                                   List<PImage> images)
    {
        return new Atlantis(id, position, images,
                0, 0, 0, 0);
    }

}
