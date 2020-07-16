import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoNotFull extends OctoClass {

    public OctoNotFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceCount, resourceLimit, actionPeriod, animationPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !this.moveToNotFull(notFullTarget.get(), scheduler, world) ||
                !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    ActionClass.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    protected boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit())
        {
            OctoFull octo = OctoFull.createOctoFull(this.getId(), this.getResourceLimit(),
                    this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                    this.getEntityImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            (octo).scheduleActions(world, imageStore, scheduler);

            return true;
        }

        return false;
    }

    protected boolean moveToNotFull(Entity target, EventScheduler scheduler, WorldModel worldModel)
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

                worldModel.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected static OctoNotFull createOctoNotFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new OctoNotFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

}
