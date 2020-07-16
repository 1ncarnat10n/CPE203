import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoFull extends OctoClass {

    public OctoFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                Atlantis.class);

        if (fullTarget.isPresent() &&
                moveToFull(fullTarget.get(), scheduler, world))
        {
            //at atlantis trigger animation
            ((Atlantis)fullTarget.get()).scheduleActions(world, imageStore, scheduler);

            //transform to unfull
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    ActionClass.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    protected void transformFull(WorldModel world,
                              EventScheduler scheduler, ImageStore imageStore)
    {
        OctoNotFull octo = OctoNotFull.createOctoNotFull(this.getId(), this.getResourceLimit(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getEntityImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(world, imageStore, scheduler);
    }

    protected boolean moveToFull(Entity target, EventScheduler scheduler, WorldModel worldModel)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
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

    protected static OctoFull createOctoFull(String id, int resourceLimit,
                                   Point position, int actionPeriod, int animationPeriod,
                                   List<PImage> images)
    {
        return new OctoFull(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }
}
