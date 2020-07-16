import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class OctoFull extends OctoClass {
public final String DEMON_OCTO_KEY = "octoDemon";

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

    protected void transformDemonFull(WorldModel world,
                                 EventScheduler scheduler, ImageStore imageStore)
    {
        OctoDemon octo = OctoDemon.createOctoDemon(this.getId(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                imageStore.getImageList(DEMON_OCTO_KEY));

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(world, imageStore, scheduler);
    }

    protected boolean moveToFull(Entity target, EventScheduler scheduler, WorldModel worldModel)
    {
        if (this.getPosition().adjacent(this.getPosition() ,target.getPosition()))
        {
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

    protected static OctoFull createOctoFull(String id, int resourceLimit,
                                   Point position, int actionPeriod, int animationPeriod,
                                   List<PImage> images)
    {
        return new OctoFull(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }
}
