import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;

   public static final String QUAKE_ID = "quake";
   public static final int QUAKE_ACTION_PERIOD = 1100;
   public static final int QUAKE_ANIMATION_PERIOD = 100;
   public static final int FISH_REACH = 1;
   public static final String OCTO_KEY = "octo";
   public static final int OCTO_NUM_PROPERTIES = 7;
   public static final int OCTO_ID = 1;
   public static final int OCTO_COL = 2;
   public static final int OCTO_ROW = 3;
   public static final int OCTO_LIMIT = 4;
   public static final int OCTO_ACTION_PERIOD = 5;
   public static final int OCTO_ANIMATION_PERIOD = 6;
   public static final String OBSTACLE_KEY = "obstacle";
   public static final int OBSTACLE_NUM_PROPERTIES = 4;
   public static final int OBSTACLE_ID = 1;
   public static final int OBSTACLE_COL = 2;
   public static final int OBSTACLE_ROW = 3;
   public static final String FISH_KEY = "fish";
   public static final int FISH_NUM_PROPERTIES = 5;
   public static final int FISH_ID = 1;
   public static final int FISH_COL = 2;
   public static final int FISH_ROW = 3;
   public static final int FISH_ACTION_PERIOD = 4;
   public static final String ATLANTIS_KEY = "atlantis";
   public static final int ATLANTIS_NUM_PROPERTIES = 4;
   public static final int ATLANTIS_ID = 1;
   public static final int ATLANTIS_COL = 2;
   public static final int ATLANTIS_ROW = 3;
   public static final String SGRASS_KEY = "seaGrass";
   public static final int SGRASS_NUM_PROPERTIES = 5;
   public static final int SGRASS_ID = 1;
   public static final int SGRASS_COL = 2;
   public static final int SGRASS_ROW = 3;
   public static final int SGRASS_ACTION_PERIOD = 4;
   public static final String BGND_KEY = "background";
   public static final int PROPERTY_KEY = 0;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public Entity getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.getY()][pos.getX()];
   }

   public void setOccupancyCell(Point pos, Entity entity)
   {
      this.occupancy[pos.getY()][pos.getX()] = entity;
   }

   public boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < this.numRows &&
              pos.getX() >= 0 && pos.getX() < this.numCols;
   }

   public Entity createAtlantis(String id, Point position,
                                List<PImage> images)
   {
      return new Entity(EntityKind.ATLANTIS, id, position, images,
              0, 0, 0, 0);
   }

   public Entity createOctoFull(String id, int resourceLimit,
                                Point position, int actionPeriod, int animationPeriod,
                                List<PImage> images)
   {
      return new Entity(EntityKind.OCTO_FULL, id, position, images,
              resourceLimit, resourceLimit, actionPeriod, animationPeriod);
   }

   public Entity createOctoNotFull(String id, int resourceLimit,
                                   Point position, int actionPeriod, int animationPeriod,
                                   List<PImage> images)
   {
      return new Entity(EntityKind.OCTO_NOT_FULL, id, position, images,
              resourceLimit, 0, actionPeriod, animationPeriod);
   }

   public Entity createObstacle(String id, Point position,
                                List<PImage> images)
   {
      return new Entity(EntityKind.OBSTACLE, id, position, images,
              0, 0, 0, 0);
   }

   public Entity createFish(String id, Point position, int actionPeriod,
                            List<PImage> images)
   {
      return new Entity(EntityKind.FISH, id, position, images, 0, 0,
              actionPeriod, 0);
   }

   public Entity createCrab(String id, Point position,
                            int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new Entity(EntityKind.CRAB, id, position, images,
              0, 0, actionPeriod, animationPeriod);
   }

   public Entity createQuake(Point position, List<PImage> images)
   {
      return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
              0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
   }

   public Entity createSgrass(String id, Point position, int actionPeriod,
                              List<PImage> images) {
      return new Entity(EntityKind.SGRASS, id, position, images, 0, 0,
              actionPeriod, 0);
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (this.withinBounds(newPt) &&
                    !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public boolean isOccupied(Point pos)
   {
      return this.withinBounds(pos) &&
              this.getOccupancyCell(pos) != null;
   }

   public boolean moveToNotFull(Entity octo, Entity target, EventScheduler scheduler)
   {
      if (octo.getPosition().adjacent(target.getPosition()))
      {
         octo.setResourceCount();
         this.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = this.nextPositionOcto(octo, target.getPosition());

         if (!octo.getPosition().equals(nextPos))
         {
            Optional<Entity> occupant = this.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            this.moveEntity(octo, nextPos);
         }
         return false;
      }
   }

   public Point nextPositionOcto(Entity entity,
                                        Point destPos)
   {
      int horiz = Integer.signum(destPos.getX() - entity.getPosition().getX());
      Point newPos = new Point(entity.getPosition().getX() + horiz,
              entity.getPosition().getY());

      if (horiz == 0 || this.isOccupied(newPos))
      {
         int vert = Integer.signum(destPos.getY() - entity.getPosition().getY());
         newPos = new Point(entity.getPosition().getX(),
                 entity.getPosition().getY() + vert);

         if (vert == 0 || this.isOccupied(newPos))
         {
            newPos = entity.getPosition();
         }
      }

      return newPos;
   }

   public Optional<Entity> getOccupant(Point pos)
   {
      if (this.isOccupied(pos))
      {
         return Optional.of(this.getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         this.setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         this.setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntityAt(Point pos)
   {
      if (this.withinBounds(pos)
              && this.getOccupancyCell(pos) != null)
      {
         Entity entity = this.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.entities.remove(entity);
         this.setOccupancyCell(pos, null);
      }
   }

   public void removeEntity(Entity entity)
   {
      this.removeEntityAt(entity.getPosition());
   }

   public void tryAddEntity( Entity entity)
   {
      if (this.isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      this.addEntity(entity);
   }

   public void addEntity(Entity entity)
   {
      if (this.withinBounds(entity.getPosition()))
      {
         this.setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public Optional<Entity> findNearest(Point pos,
                                              EntityKind kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities)
      {
         if (entity.getKind() == kind)
         {
            ofType.add(entity);
         }
      }

      return this.nearestEntity(ofType, pos);
   }

   public Point nextPositionCrab(Entity entity, Point destPos)
   {
      int horiz = Integer.signum(destPos.getX() - entity.getPosition().getX());
      Point newPos = new Point(entity.getPosition().getX() + horiz,
              entity.getPosition().getY());

      Optional<Entity> occupant = this.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().getKind() == EntityKind.FISH)))
      {
         int vert = Integer.signum(destPos.getY() - entity.getPosition().getY());
         newPos = new Point(entity.getPosition().getX(), entity.getPosition().getY() + vert);
         occupant = this.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().getKind() == EntityKind.FISH)))
         {
            newPos = entity.getPosition();
         }
      }

      return newPos;
   }

   public boolean moveToCrab(Entity crab, Entity target, EventScheduler scheduler)
   {
      if (crab.getPosition().adjacent(target.getPosition()))
      {
         this.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = nextPositionCrab(crab, target.getPosition());

         if (!crab.getPosition().equals(nextPos))
         {
            Optional<Entity> occupant = this.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            this.moveEntity(crab, nextPos);
         }
         return false;
      }
   }

   public boolean moveToFull(Entity octo, Entity target, EventScheduler scheduler)
   {
      if (octo.getPosition().adjacent(target.getPosition()))
      {
         return true;
      }
      else
      {
         Point nextPos = this.nextPositionOcto(octo, target.getPosition());

         if (!octo.getPosition().equals(nextPos))
         {
            Optional<Entity> occupant = this.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            this.moveEntity(octo, nextPos);
         }
         return false;
      }
   }

   public Optional<Entity> nearestEntity(List<Entity> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = nearest.getPosition().distanceSquared(pos);

         for (Entity other : entities)
         {
            int otherDistance = other.getPosition().distanceSquared(pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public boolean parseOcto(String [] properties, ImageStore imageStore)
   {
      if (properties.length == OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                 Integer.parseInt(properties[OCTO_ROW]));
         Entity entity = this.createOctoNotFull(properties[OCTO_ID],
                 Integer.parseInt(properties[OCTO_LIMIT]),
                 pt,
                 Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(OCTO_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   public boolean parseObstacle(String [] properties, ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = this.createObstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public boolean parseFish(String [] properties, ImageStore imageStore)
   {
      if (properties.length == FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));
         Entity entity = this.createFish(properties[FISH_ID],
                 pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                 imageStore.getImageList(FISH_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }

   public boolean parseAtlantis(String [] properties, ImageStore imageStore)
   {
      if (properties.length == ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));
         Entity entity = this.createAtlantis(properties[ATLANTIS_ID],
                 pt, imageStore.getImageList(ATLANTIS_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   public boolean parseSgrass(String [] properties, ImageStore imageStore)
   {
      if (properties.length == SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                 Integer.parseInt(properties[SGRASS_ROW]));
         Entity entity = this.createSgrass(properties[SGRASS_ID], // changed createSgrass to entity.createSgrass
                 pt,
                 Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                 imageStore.getImageList(SGRASS_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }

   public boolean parseBackground(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Functions.BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Functions.BGND_COL]),
                 Integer.parseInt(properties[Functions.BGND_ROW]));
         String id = properties[Functions.BGND_ID];
         this.setBackground(pt, new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == Functions.BGND_NUM_PROPERTIES;
   }

   public void setBackground(Point pos, Background background) {
      if (this.withinBounds(pos)) {
         this.setBackgroundCell(pos, background);
      }
   }

   public void setBackgroundCell(Point pos, Background background)
   {
      this.background[pos.getY()][pos.getX()] = background;
   }

   public Background getBackgroundCell(WorldModel world, Point pos)
   {
      return world.background[pos.getY()][pos.getX()];
   }

   public void drawBackground(WorldView view)
   {
      for (int row = 0; row < view.getViewport().getNumRow(); row++)
      {
         for (int col = 0; col < view.getViewport().getNumCol(); col++)
         {
            Point worldPoint = view.getViewport().viewportToWorld(col, row);
            Optional<PImage> image = getBackgroundImage(view.getWorld(),
                    worldPoint);
            if (image.isPresent())
            {
               view.getScreen().image(image.get(), col * view.getTileWidth(),
                       row * view.getTileHeight());
            }
         }
      }
   }

   public Optional<PImage> getBackgroundImage(WorldModel world,
                                              Point pos)
   {
      if (world.withinBounds(pos))
      {
         return Optional.of(this.getCurrentImage(world.getBackgroundCell(world, pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   public void drawViewport(WorldView view)
   {
      drawBackground(view);
      view.drawEntities();
   }

   public PImage getCurrentImage(Object entity) {
      if (entity instanceof Background) {
         return ((Background) entity).getImages()
                 .get(((Background) entity).getImageIndex());
      } else if (entity instanceof Entity) {
         return ((Entity) entity).getEntityImages().get(((Entity) entity).getEntityImageIndex());
      } else {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }

   public boolean processLine(String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return this.parseBackground(properties, imageStore);
            case OCTO_KEY:
               return this.parseOcto(properties, imageStore);
            case OBSTACLE_KEY:
               return this.parseObstacle(properties, imageStore);
            case FISH_KEY:
               return this.parseFish(properties, imageStore);
            case ATLANTIS_KEY:
               return this.parseAtlantis(properties, imageStore);
            case SGRASS_KEY:
               return this.parseSgrass(properties, imageStore);
         }
      }

      return false;
   }

   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!this.processLine(in.nextLine(),imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public Set<Entity> getEntities() {
      return entities;
   }

   public int getNumCols() {
      return numCols;
   }

   public int getNumRows() {
      return numRows;
   }
}
