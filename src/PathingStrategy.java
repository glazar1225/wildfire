import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface PathingStrategy {
    /*
     * Returns a prefix of a path from the start point to a point within reach
     * of the end point. This path is only valid (“clear”) when returned, but
     * may be invalidated by the movement of other entities.
     *
     * The prefix includes neither the start point nor the end point.
     */
    List<Point> computePath(Point start, Point end,
                            Predicate<Point> canPassThrough,
                            BiPredicate<Point, Point> withinReach,
                            Function<Point, Stream<Point>> potentialNeighbors);

    static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.x, point.y - 1))
                            .add(new Point(point.x, point.y + 1))
                            .add(new Point(point.x - 1, point.y))
                            .add(new Point(point.x + 1, point.y))
                            .build();

    static Field pointXBox[] = {null};
    static Field pointYBox[] = {null};

    static void publicizePoint()    {
        try {
            // Nice security you've got there, Oracle.
            pointXBox[0] = Point.class.getDeclaredField("x");
            pointXBox[0].setAccessible(true);
            pointYBox[0] = Point.class.getDeclaredField("y");
            pointYBox[0].setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            // All bets are off if they didn't name their fields "x" and "y". But
            // we were already assuming that anyways.
        }
    }
    static int getX(Point p) {
        try {  return (int)pointXBox[0].get(p);
        }
        catch (IllegalAccessException e) {  return -1;
        }
    }
    static int getY(Point p) {
        try { return (int)pointYBox[0].get(p);
        }
        catch (IllegalAccessException e) {  return -1;
        }
    }
}