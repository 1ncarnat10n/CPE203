import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sgrass implements Execute {
    public static final String FISH_ID_PREFIX = "fish -- ";
    public static final int FISH_CORRUPT_MIN = 20000;
    public static final int FISH_CORRUPT_MAX = 30000;
    public static final String FISH_KEY = "fish";

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Sgrass(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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
    public int getAnimationPeriod() {return animationPeriod;}

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent())
        {
            Entity fish = Fish.createFish(FISH_ID_PREFIX + this.id,
                    openPt.get(), FISH_CORRUPT_MIN +
                            Functions.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(FISH_KEY));
            world.addEntity(fish);
            ((Fish)fish).scheduleActions(world, imageStore, scheduler);
        }

        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                this.actionPeriod);
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

    public static Sgrass createSgrass(String id, Point position, int actionPeriod,
                               List<PImage> images) {
        return new Sgrass(id, position, images, 0, 0,
                actionPeriod, 0);
    }

}
