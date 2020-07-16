import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

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
        if (this.getPosition().adjacent(this.getPosition(), target.getPosition()))
        {
            this.setResourceCount();
            worldModel.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            SingleStepPathingStrategy path = new SingleStepPathingStrategy();
            Predicate<Point> canpass = (pt) -> worldModel.withinBounds(pt) && !worldModel.isOccupied(pt);
            BiPredicate<Point, Point> withinreach = (pt1, pt2) -> Point.adjacent(pt1, pt2);
            List<Point> path1 = path.computePath(this.getPosition(), target.getPosition(), canpass, withinreach, PathingStrategy.CARDINAL_NEIGHBORS);
            Point nextPos = null;
            if (path1.size() != 0) {
                nextPos = path1.get(0);
            }

            if (nextPos != null)
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
