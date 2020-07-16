import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoNotFull implements Move {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public OctoNotFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.position,
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !this.moveToNotFull(notFullTarget.get(), scheduler, world) ||
                !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    ActionClass.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            Entity octo = OctoFull.createOctoFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod, this.animationPeriod,
                    this.images);

            world.removeEntity((Entity)this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            ((OctoFull)octo).scheduleActions(world, imageStore, scheduler);

            return true;
        }

        return false;
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

    public boolean moveToNotFull(Entity target, EventScheduler scheduler, WorldModel worldModel)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            this.setResourceCount();
            worldModel.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(target.getPosition(), worldModel);

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = worldModel.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                worldModel.moveEntity((Entity)this, nextPos);
            }
            return false;
        }
    }

    public Point nextPosition(Point destPos, WorldModel worldModel)
    {
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz,
                this.getPosition().getY());

        if (horiz == 0 || worldModel.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(),
                    this.getPosition().getY() + vert);

            if (vert == 0 || worldModel.isOccupied(newPos))
            {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                AnimationClass.createAnimationAction(this, 0), this.getAnimationPeriod());
    }

    public static OctoNotFull createOctoNotFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new OctoNotFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

}
