import processing.core.PImage;

import java.util.List;

public abstract class Execute extends Entity {
    private int actionPeriod;

    public Execute(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    protected int getActionPeriod() { return actionPeriod; }

    protected void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                actionPeriod);
    }
}
