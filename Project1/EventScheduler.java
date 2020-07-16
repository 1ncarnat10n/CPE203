import java.util.*;

/*
EventScheduler: ideally our way of controlling what happens in our virtual world
 */

final class EventScheduler
{
   public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   private PriorityQueue<Event> eventQueue;
   private Map<Entity, List<Event>> pendingEvents;
   private double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void removePendingEvent(Event event)
   {
      List<Event> pending = this.pendingEvents.get(event.getEntity());

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public void scheduleEvent(Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
              (long)(afterPeriod * this.timeScale);
      Event event = new Event(action, time, entity);

      this.eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = this.pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
      this.pendingEvents.put(entity, pending);
   }

   public void scheduleActions(Entity entity, WorldModel world, ImageStore imageStore)
   {
      switch (entity.getKind())
      {
         case OCTO_FULL:
            scheduleEvent(entity,
                    this.createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity, this.createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
            break;

         case OCTO_NOT_FULL:
            scheduleEvent(entity,
                    this.createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity,
                    this.createAnimationAction(entity, 0), entity.getAnimationPeriod());
            break;

         case FISH:
            scheduleEvent(entity,
                    this.createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            break;

         case CRAB:
            scheduleEvent(entity,
                    this.createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity,
                    this.createAnimationAction(entity, 0), entity.getAnimationPeriod());
            break;

         case QUAKE:
            scheduleEvent(entity,
                    this.createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity,
                    this.createAnimationAction(entity, QUAKE_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         case SGRASS:
            scheduleEvent(entity,
                    this.createActivityAction(entity, world, imageStore),
                    entity.getActionPeriod());
            break;
         case ATLANTIS:
            scheduleEvent(entity,
                    this.createAnimationAction(entity, ATLANTIS_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         default:
      }
   }

   public void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = this.pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            this.eventQueue.remove(event);
         }
      }
   }

   public void updateOnTime(long time, EventScheduler scheduler)
   {
      while (!this.eventQueue.isEmpty() &&
              this.eventQueue.peek().getTime() < time)
      {
         Event next = this.eventQueue.poll();

         this.removePendingEvent(next);

         next.getAction().executeAction(scheduler);
      }
   }

   public Action createAnimationAction(Entity entity, int repeatCount)
   {
      return new Action(ActionKind.ANIMATION, entity, null, null, repeatCount);
   }

   public Action createActivityAction(Entity entity, WorldModel world,
                                      ImageStore imageStore)
   {
      return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
   }


}
