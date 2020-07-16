import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


final class Entity
{
   private EntityKind kind;
   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;
   private int resourceLimit;
   private int resourceCount;
   private int actionPeriod;
   private int animationPeriod;
   public static final String CRAB_KEY = "crab";
   public static final String CRAB_ID_SUFFIX = " -- crab";
   public static final int CRAB_PERIOD_SCALE = 4;
   public static final int CRAB_ANIMATION_MIN = 50;
   public static final int CRAB_ANIMATION_MAX = 150;
   public static final String QUAKE_KEY = "quake";
   public static final String FISH_ID_PREFIX = "fish -- ";
   public static final int FISH_CORRUPT_MIN = 20000;
   public static final int FISH_CORRUPT_MAX = 30000;
   public static final String FISH_KEY = "fish";

   public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

   public int getAnimationPeriod()
   {
      switch (this.kind)
      {
         case OCTO_FULL:
         case OCTO_NOT_FULL:
         case CRAB:
         case QUAKE:
         case ATLANTIS:
            return this.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            this.kind));
      }
   }

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }

   public boolean transformNotFull(Entity entity, WorldModel world,
                                          EventScheduler scheduler, ImageStore imageStore)
   {
      if (this.resourceCount >= this.resourceLimit)
      {
         Entity octo = world.createOctoFull(this.id, this.resourceLimit,
                 this.position, this.actionPeriod, this.animationPeriod,
                 this.images);

         world.removeEntity(entity);
         scheduler.unscheduleAllEvents(entity);

         world.addEntity(octo);
         scheduler.scheduleActions(octo, world, imageStore);

         return true;
      }

      return false;
   }

   public void transformFull(Entity entity, WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
   {
      Entity octo = world.createOctoNotFull(entity.id, entity.resourceLimit,
              entity.position, entity.actionPeriod, entity.animationPeriod,
              entity.images);

      world.removeEntity(entity);
      scheduler.unscheduleAllEvents(entity);

      world.addEntity(octo);
      scheduler.scheduleActions(octo, world, imageStore);
   }

   public void executeOctoFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = world.findNearest(this.position,
              EntityKind.ATLANTIS);

      if (fullTarget.isPresent() &&
              world.moveToFull(this, fullTarget.get(), scheduler))
      {
         //at atlantis trigger animation
         scheduler.scheduleActions(fullTarget.get(), world, imageStore);

         //transform to unfull
         this.transformFull(this, world, scheduler, imageStore);
      }
      else
      {
         scheduler.scheduleEvent(this,
                 scheduler.createActivityAction(this, world, imageStore),
                 this.actionPeriod);
      }
   }

   public void executeOctoNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(this.position,
              EntityKind.FISH);

      if (!notFullTarget.isPresent() ||
              !world.moveToNotFull(this, notFullTarget.get(), scheduler) ||
              !this.transformNotFull(this, world, scheduler, imageStore))
      {
         scheduler.scheduleEvent(this,
                 scheduler.createActivityAction(this, world, imageStore),
                 this.actionPeriod);
      }
   }

   public void executeFishActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = this.position;  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      Entity crab = world.createCrab(this.id + CRAB_ID_SUFFIX,
              pos, this.actionPeriod / CRAB_PERIOD_SCALE,
              CRAB_ANIMATION_MIN +
                      Functions.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
              imageStore.getImageList(CRAB_KEY));

      world.addEntity(crab);
      scheduler.scheduleActions(crab, world, imageStore);
   }

   public void executeCrabActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> crabTarget = world.findNearest(
              this.position, EntityKind.SGRASS);
      long nextPeriod = this.actionPeriod;

      if (crabTarget.isPresent())
      {
         Point tgtPos = crabTarget.get().position;

         if (world.moveToCrab(this, crabTarget.get(), scheduler))
         {
            Entity quake = world.createQuake(tgtPos,
                    imageStore.getImageList(QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += this.actionPeriod;
            scheduler.scheduleActions(quake, world, imageStore);
         }
      }

      scheduler.scheduleEvent(this,
              scheduler.createActivityAction(this, world, imageStore),
              nextPeriod);
   }

   public void executeQuakeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeAtlantisActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeSgrassActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(this.position);

      if (openPt.isPresent())
      {
         Entity fish = world.createFish(FISH_ID_PREFIX + this.id,
                 openPt.get(), FISH_CORRUPT_MIN +
                         Functions.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                 imageStore.getImageList(FISH_KEY));
         world.addEntity(fish);
         scheduler.scheduleActions(fish, world, imageStore);
      }

      scheduler.scheduleEvent(this,
              scheduler.createActivityAction(this, world, imageStore),
              this.actionPeriod);
   }

   public PImage getCurrentImage(Object entity)
   {
      if (entity instanceof Background)
      {
         return ((Background)entity).getImages()
                 .get(((Background)entity).getImageIndex());
      }
      else if (entity instanceof Entity)
      {
         return ((Entity)entity).images.get(((Entity)entity).imageIndex);
      }
      else
      {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }

   public Point getPosition() {
      return position;
   }

   public void setPosition(Point pos) {
      this.position = pos;
   }

   public EntityKind getKind() {
      return kind;
   }

   public void setResourceCount() {
      resourceCount += 1;
   }

   public int getEntityImageIndex() {
      return imageIndex;
   }

   public List<PImage> getEntityImages() {
      return images;
   }

   public int getActionPeriod() {
      return actionPeriod;
   }
}
