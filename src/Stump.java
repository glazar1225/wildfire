import processing.core.PImage;

import java.util.List;

final class Stump extends Entity {
    public static final String KEY = "stump";
    public static final int NUM_PROPERTIES = 0;

    public Stump(String id, Point p, List<PImage> im) {
        super(id, p, im);
    }
}
