import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Demon extends Move{
    public static final String DEMON_OCTO_KEY = "demon_octo";
    public static final int DEMONOCTO_ACTION_PERIOD = 5;
    public static final int DEMONOCTO_ANIMATION_PERIOD = 6;
    public Demon(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected Point nextPosition(Point destPos, WorldModel worldModel)
    {
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz,
                this.getPosition().getY());

        Optional<Entity> occupant = worldModel.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent()))
        {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
            occupant = worldModel.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent()))
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
        Optional<Entity> DemonTarget2 = world.findNearest(this.getPosition(), OctoFull.class);
        long nextPeriod = this.getActionPeriod();

        if (DemonTarget.isPresent() && DemonTarget2.isPresent())
        {
            Point finalTgt;
            Point tgtPos = DemonTarget.get().getPosition();
            Point tgtPos2 = DemonTarget2.get().getPosition();
            if (Point.distanceSquared(this.getPosition(), tgtPos) <= Point.distanceSquared(this.getPosition(), tgtPos2)) {
                finalTgt = tgtPos;
                if (moveToDemon(DemonTarget.get(), scheduler, world))
                {
                    Entity quake = Quake.createQuake(finalTgt,
                            imageStore.getImageList(Crab.QUAKE_KEY));
                    OctoDemon octo = OctoDemon.createOctoDemon("demon_octo", finalTgt,
                            DEMONOCTO_ACTION_PERIOD, DEMONOCTO_ANIMATION_PERIOD, imageStore.getImageList(DEMON_OCTO_KEY));
                    world.addEntity(octo);
                    octo.scheduleActions(world, imageStore, scheduler);
                    world.addEntity(quake);
                    nextPeriod += this.getActionPeriod();
                    ((Quake)quake).scheduleActions(world, imageStore, scheduler);
                }
            }
            else {
                finalTgt = tgtPos2;
                if (moveToDemon(DemonTarget2.get(), scheduler, world))
                {
                    Entity quake = Quake.createQuake(finalTgt,
                            imageStore.getImageList(Crab.QUAKE_KEY));
                    OctoDemon octo = OctoDemon.createOctoDemon("demon_octo", finalTgt,
                            DEMONOCTO_ACTION_PERIOD, DEMONOCTO_ANIMATION_PERIOD, imageStore.getImageList(DEMON_OCTO_KEY));
                    world.addEntity(octo);
                    octo.scheduleActions(world, imageStore, scheduler);
                    world.addEntity(quake);
                    nextPeriod += this.getActionPeriod();
                    ((Quake)quake).scheduleActions(world, imageStore, scheduler);
                }
            }
        }

        else if (DemonTarget.isPresent())
        {
            Point tgtPos = DemonTarget.get().getPosition();
            if (moveToDemon(DemonTarget.get(), scheduler, world))
            {
                Entity quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(Crab.QUAKE_KEY));
                OctoDemon octo = OctoDemon.createOctoDemon("demon_octo", tgtPos,
                        DEMONOCTO_ACTION_PERIOD, DEMONOCTO_ANIMATION_PERIOD, imageStore.getImageList(DEMON_OCTO_KEY));
                world.addEntity(octo);
                octo.scheduleActions(world, imageStore, scheduler);
                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                ((Quake)quake).scheduleActions(world, imageStore, scheduler);
            }
        }

        else
        {
            Point tgtPos2 = DemonTarget2.get().getPosition();
            if (moveToDemon(DemonTarget.get(), scheduler, world))
            {

                Entity quake = Quake.createQuake(tgtPos2,
                        imageStore.getImageList(Crab.QUAKE_KEY));
                OctoDemon octo = OctoDemon.createOctoDemon("demon_octo", tgtPos2,
                        DEMONOCTO_ACTION_PERIOD, DEMONOCTO_ANIMATION_PERIOD, imageStore.getImageList(DEMON_OCTO_KEY));
                world.addEntity(octo);
                octo.scheduleActions(world, imageStore, scheduler);
                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                ((Quake)quake).scheduleActions(world, imageStore, scheduler);
            }
        }

        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    protected boolean moveToDemon(Entity target, EventScheduler scheduler, WorldModel worldModel)
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
//                Optional<Entity> occupant = worldModel.getOccupant(nextPos);
//                if (occupant.isPresent())
//                {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }

                worldModel.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected static Demon createDemon(String id, Point position,
                                     int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Demon(id, position, images, actionPeriod, animationPeriod);
    }
}
