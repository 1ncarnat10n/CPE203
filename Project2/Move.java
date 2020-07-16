public interface Move extends Animation{
    Point nextPosition(Point destPos, WorldModel worldModel);
}
