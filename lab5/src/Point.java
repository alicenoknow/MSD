import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Point {

    public ArrayList<Point> neighbors;
    public static Integer []types ={0,1,2,3}; // 0 - floor, 1 - wall, 2 - exit, 3 - pedestrian
    public int type;
    public int staticField;
    public boolean isPedestrian;
    private boolean blocked = false;

    public Point() {
        type=0;
        staticField = 100000;
        neighbors= new ArrayList<Point>();
    }

    public void clear() {
        staticField = 100000;

    }

    public boolean calcStaticField() {
        boolean changed = false;

        for(Point nei : this.neighbors)
            if (nei.staticField + 1 < this.staticField) {
                this.staticField = nei.staticField + 1;
                changed = true;
            }
        return changed;
    }

    public void move(){
        if(this.isPedestrian){
            Point moveTo = findLowestNeighbor();
            if(moveTo.type != 2)
                moveTo.isPedestrian = true;
            moveTo.blocked = true;
            this.isPedestrian = false;
        }
    }

    public void addNeighbor(Point nei) {
        neighbors.add(nei);
    }

    private Point findLowestNeighbor(){
        List<Point> freeFields =
                this.neighbors.stream()
                .filter(p -> p.type == 0 || p.type == 2)
                .filter(p -> !p.isPedestrian)
                .collect(toList());

        if(freeFields.isEmpty())
            return this;
        int minStaticField = freeFields.get(0).staticField;
        Point moveTo = freeFields.get(0);
        for(Point nei : freeFields)
            if(nei.staticField <= minStaticField){
                minStaticField = nei.staticField;
                moveTo = nei;
            }
        return moveTo;
    }

    public void unblock() {
        this.blocked = false;
    }
}