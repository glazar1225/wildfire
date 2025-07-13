import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.Math.abs;

public class AStarPathingStrategy implements PathingStrategy {

    private int h(Point start, Point end){
        // manhattan dist
        return abs(start.x - end.x) + abs(start.y - end.y);
    }
    private class Anode implements Comparable<Anode>{
        private Point p;
        private int g; //dist from start
        private int h; // dist from endpoint manhattan
        private int f; //total
        private Anode prev;

        public Anode (Point p, int g, int h, Anode prev){
            this.p = p;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.prev = prev;
        }

        public int compareTo(Anode other){
            return Integer.compare(this.f, other.f);
        }
    }


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Point> visited = new ArrayList<>();
        Queue<Anode> toVisit = new PriorityQueue<>();

        toVisit.add(new Anode(start, 0, h(start, end), null));
        while(!toVisit.isEmpty()){
            Anode curNode = toVisit.poll(); // pop in java is poll for priority queue

            if (visited.contains(curNode.p)) continue; // skip

            visited.add(curNode.p);

            if (withinReach.test(curNode.p, end)){
                List<Point> path = new ArrayList<>();
                while (curNode != null){
                    path.add(curNode.p);
                    curNode = curNode.prev;
                }
                Collections.reverse(path);
                path.removeFirst();
                return path;
            }
            Anode finalCurNode = curNode;

            potentialNeighbors.apply(curNode.p).filter(canPassThrough)
                    .filter(p -> !visited.contains(p))
                    .forEach(n -> toVisit.add(
                            new Anode(n, finalCurNode.g + 1, h(n, end), finalCurNode)));
        }
        return Collections.emptyList();
    }


}
