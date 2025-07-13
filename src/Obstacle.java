import processing.core.PImage;

import java.util.List;

public class Obstacle extends AnimatedEntity{
    public static final String KEY = "obstacle";
    public static final int ANIMATION_PERIOD_IDX = 0;
    public static final int NUM_PROPERTIES = 1;

    public Obstacle(String id, Point p, double anp, List<PImage> i) {
        super(id, p, i, anp);
    }

}
