public interface Execute extends Entity {
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler scheduler);


}
