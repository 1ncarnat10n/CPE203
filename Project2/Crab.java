import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab implements Move {
    public static final String QUAKE_KEY = "quake";

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Crab(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
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
        Optional<Entity> crabTarget = world.findNearest(
                this.position, Sgrass.class);
        long nextPeriod = this.actionPeriod;

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveToCrab(crabTarget.get(), scheduler, world))
            {
                Entity quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                ((Quake)quake).scheduleActions(world, imageStore, scheduler);
            }
        }

        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                nextPeriod);
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

    public boolean moveToCrab(Entity target, EventScheduler scheduler, WorldModel worldModel)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
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

                worldModel.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public Point nextPosition(Point destPos, WorldModel worldModel)
    {
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz,
                this.getPosition().getY());

        Optional<Entity> occupant = worldModel.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().getClass() == Fish.class)))
        {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
            occupant = worldModel.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().getClass() == Fish.class)))
            {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void scheduleActions( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                AnimationClass.createAnimationAction(this, 0), this.getAnimationPeriod());
    }

    public static Crab createCrab(String id, Point position,
                           int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Crab(id, position, images,
                0, 0, actionPeriod, animationPeriod);
    }




}
