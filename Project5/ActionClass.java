public class ActionClass implements Action{
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActionClass(Entity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public static ActionClass createActivityAction(Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new ActionClass( entity, world, imageStore, 0);
    }

    public void executeAction(EventScheduler scheduler)
    {
        ((Execute)this.entity).executeActivity(world, imageStore, scheduler);
        }

    }

