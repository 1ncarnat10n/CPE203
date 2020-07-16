public class Node {
    private int g;
    private int f;
    private int h;
    private Point pos;
    private Node prev;

    public Node(int g, int h, int f, Point pos, Node prev) {
        this.g = g;
        this.h = h;
        this.f = f;
        this.pos = pos;
        this.prev = prev;
    }
    public int getH(){return h;}
    public int getF(){return f;}
    public void setG(int g){this.g = g;}
    public void setH(int h){this.h = h;}
    public int getG(){return g;}
    public void setPos(Point p){pos = p;}
    public Point getPos(){return pos;}
    public Node getPrev(){return prev;}

    public int compareTo(Object o) {
        if (this.g < ((Node)o).g) {
            return -1;
        }
        else if (this.g > ((Node)o).g) return 1;
        else return 0;
    }
}
