import java.util.*;

import processing.core.PImage;


public abstract class Entity{
    protected final String id;
    protected Point position;
    protected List<PImage> images;
    protected int imageIndex;



    public Entity(String id, Point p, List<PImage> i) {
        this.id = id;
        this.position = p;
        this.images = i;
        this.imageIndex = 0;

    }
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }
    public String getId() {return this.id;}
    public Point getPosition(){return position;}
    public void setPosition(Point newPos){ this.position = newPos;}
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex % this.images.size());
    }
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

    public static Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.getFirst();
            int nearestDistance = nearest.position.distanceSquared(pos);

            for (Entity other : entities) {
                int otherDistance = other.position.distanceSquared(pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

}


