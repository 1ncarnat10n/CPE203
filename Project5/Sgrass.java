import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sgrass extends Execute {
    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;
    private static final String FISH_KEY = "fish";

    public Sgrass(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(super.getPosition());

        if (openPt.isPresent())
        {
            Entity fish = Fish.createFish(FISH_ID_PREFIX + super.getId(),
                    openPt.get(), FISH_CORRUPT_MIN +
                            Functions.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(FISH_KEY));
            world.addEntity(fish);
            ((Fish)fish).scheduleActions(world, imageStore, scheduler);
        }

        scheduler.scheduleEvent(this,
                ActionClass.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
    }

    protected static Sgrass createSgrass(String id, Point position, int actionPeriod,
                               List<PImage> images) {
        return new Sgrass(id, position, images, actionPeriod);
    }

}
