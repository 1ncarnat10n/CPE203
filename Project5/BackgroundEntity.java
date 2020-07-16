import processing.core.PImage;

import java.util.List;

public class BackgroundEntity extends Entity{
    public BackgroundEntity(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
    public static BackgroundEntity createBackgroundEntity(String id, Point position, List<PImage> images) {
        return new BackgroundEntity(id, position, images);
    }
}
