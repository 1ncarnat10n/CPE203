import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy{
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        int small_f;
        List<Point> path = new LinkedList<>();
        ArrayList<Node> closed_list = new ArrayList<>();
        PriorityQueue<Node> open_list = new PriorityQueue<>();
        Map<Point,Node> open_map = new HashMap<>();
        open_list.add(new Node(0, computeG(start, end), computeH(start, end), start, null));
        Node current = null;
        //System.out.println(current);
        //System.out.println(open_list);
        int g_val;
        while(!open_list.isEmpty()) {
            current = open_list.remove();
            //System.out.println(current);
            if (withinReach.test(current.getPos(), end)) {
                while(current.getPrev() != null) {
                    path.add(0, current.getPos());
                    current = current.getPrev();
                }
                return path;
            }

            List<Point> pot_neighbors = potentialNeighbors.apply(current.getPos()).filter(canPassThrough)
                    .filter(pt -> !pt.equals(end) && !pt.equals(start)).collect(Collectors.toList());
            //System.out.println(pot_neighbors);
            for (Point pt : pot_neighbors) {
                if (!closed_list.contains(pt)) {
                    g_val = current.getG() + 1;
                    if (open_map.containsKey(pt)) {
                        if (g_val < open_map.get(pt).getG()) {
                            open_list.remove(open_map.get(pt));
                            open_list.add(new Node(g_val, computeH(pt, end), computeF(g_val, computeH(pt, end)), pt, current));
                            open_map.remove(pt);
                            open_map.put(pt, new Node(g_val, computeH(pt, end), computeF(g_val, computeH(pt, end)), pt, current));
                        }

                    }
                    if (!open_list.contains(pt)) {
                        open_list.add(new Node(current.getG() + 1, computeH(pt, end), computeF(current.getG() + 1, computeH(pt, end)), pt, current));
                        open_map.put(pt, new Node(current.getG() + 1, computeH(pt, end), computeF(current.getG() + 1, computeH(pt, end)), pt, current));
                    }
                }
            }
            closed_list.add(current);
        }
        //System.out.println(path);
        return path;
    }

    public int computeG(Point start, Point pt) {
        return Math.abs(start.getX() - pt.getX()) + Math.abs(start.getY() - pt.getY());
    }

    public int computeH(Point end, Point pt) {
        return Math.abs(end.getX() - pt.getX()) + Math.abs(end.getY() - pt.getY());
    }

    public int computeF(int g, int h) {
        return g + h;
    }
}
