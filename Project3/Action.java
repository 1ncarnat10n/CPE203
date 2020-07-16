/*
Action: ideally what our various entities might do in our virutal world
 */

import processing.core.PImage;

public interface Action
{
   void executeAction(EventScheduler scheduler);
}
