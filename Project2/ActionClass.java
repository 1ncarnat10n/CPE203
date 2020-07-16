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
        if (this.entity instanceof OctoFull) {
            ((OctoFull) this.entity).executeActivity(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof OctoNotFull) {
            ((OctoNotFull) this.entity).executeActivity(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof Fish) {
            ((Fish) this.entity).executeActivity(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof Crab) {
            ((Crab) this.entity).executeActivity(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof Quake) {
            ((Quake) this.entity).executeActivity(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof Sgrass) {
            ((Sgrass) this.entity).executeActivity(this.world, this.imageStore, scheduler);
        }
        if (this.entity instanceof Atlantis) {
            ((Atlantis) this.entity).executeActivity(this.world, this.imageStore, scheduler);
        }
        }
    }

