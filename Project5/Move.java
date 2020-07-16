import processing.core.PImage;

import java.util.List;

public abstract class Move extends Animation{
    public Move(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected abstract Point nextPosition(Point destPos, WorldModel worldModel);

    protected void scheduleActions( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                AnimationClass.createAnimationAction(this, 0), this.getAnimationPeriod());
    }

}
