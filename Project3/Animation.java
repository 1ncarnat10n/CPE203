import processing.core.PImage;

import java.util.List;

public abstract class Animation extends Execute{
    private int animationPeriod;
    public Animation(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    protected int getAnimationPeriod() {return animationPeriod;}
    protected void nextImage()
    {
        super.setImageIndex((super.getEntityImageIndex() + 1) % super.getEntityImages().size());
    }

}
