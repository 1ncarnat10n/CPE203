import processing.core.PImage;

import java.util.List;

public abstract class OctoClass extends Move{
    private int resourceCount;
    private int resourceLimit;
    public OctoClass(String id, Point position, List<PImage> images, int resourceCount, int resourceLimit, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    protected int getResourceCount() {return resourceCount;}
    protected int getResourceLimit() {return resourceLimit;}
    protected void setResourceCount() {resourceCount += 1;}

    protected Point nextPosition(Point destPos, WorldModel worldModel)
    {
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz,
                this.getPosition().getY());

        if (horiz == 0 || worldModel.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(),
                    this.getPosition().getY() + vert);

            if (vert == 0 || worldModel.isOccupied(newPos))
            {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }
}
