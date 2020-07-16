import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class OctoDemon extends Move{
    public OctoDemon(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected Point nextPosition(Point destPos, WorldModel worldModel)
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

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> DemonTarget = world.findNearest(
                this.getPosition(), OctoNotFull.class);
        long nextPeriod = this.getActionPeriod();

        if (DemonTarget.isPresent())
        {
            Point tgtPos = DemonTarget.get().getPosition();

            if (moveToOctoDemon(DemonTarget.get(), scheduler, world))
            {
                Entity quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(Crab.QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                ((Quake)quake).scheduleActions(world, imageStore, scheduler);
            }
        }

        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    protected boolean moveToOctoDemon(Entity target, EventScheduler scheduler, WorldModel worldModel)
    {
        SingleStepPathingStrategy singleStep = new SingleStepPathingStrategy();
        Predicate<Point> canPass = (point) -> worldModel.withinBounds(point) && !worldModel.isOccupied(point);
        BiPredicate<Point, Point> withinReach = (point1, point2) -> Point.adjacent(point1, point2);

        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            worldModel.removeEntity(target);

            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            List<Point> path = singleStep.computePath(this.getPosition(), target.getPosition(), canPass, withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
            Point nextPos = null;
            if (path.size() != 0) {
                nextPos = path.get(0);
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

    protected static OctoDemon createOctoDemon(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new OctoDemon(id, position, images, actionPeriod, animationPeriod);
    }
}
