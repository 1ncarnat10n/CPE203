import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public interface Entity
{



   void nextImage();
   PImage getCurrentImage(Object entity);
   Point getPosition();
   void setPosition(Point pos);
   void setResourceCount();
   int getEntityImageIndex();
   List<PImage> getEntityImages();
   int getActionPeriod();
   int getAnimationPeriod();
}
