import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */

public abstract class Entity {
   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;

   public Entity(String id, Point position, List<PImage> images) {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
   }
   protected PImage getCurrentImage() { return (images.get(imageIndex));}
   protected Point getPosition()  { return position;}
   protected void setPosition(Point pos)  { this.position = pos;}
   protected int getEntityImageIndex() {
      return imageIndex;
   }
   protected List<PImage> getEntityImages() {
      return images;
   }
   protected String getId() {return id;}
   protected void setImageIndex(int ind) {imageIndex = ind;}

}
