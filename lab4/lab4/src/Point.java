import java.util.ArrayList;
import java.util.Random;

public class Point {

    private boolean occupied;
    public int velocity;
    private final ArrayList<Point> leftNeighbors = new ArrayList<>();
    private final ArrayList<Point> centerNeighbors = new ArrayList<>();
    private final ArrayList<Point> rightNeighbors = new ArrayList<>();
    private final int MAX_VELOCITY;
    private final double probabilityOfSlowdown = 0.5;
    private final Random generator = new Random();
    private boolean isAfterMove = false;
    private int hasOvertaken;
    private Lane nextDirection; //  left | center | right


    public Point(int maxV) {
        this.MAX_VELOCITY = maxV;
        this.clear();
    }

    public void clicked() {
        this.occupied = true;
    }

    public void clear() {
        this.occupied = false;
        this.velocity = 0;
        this.isAfterMove = false;
        this.hasOvertaken = 0;
        this.nextDirection = Lane.Center;
    }

    public void addLeftNeighbor(Point next){
        this.leftNeighbors.add(next);
    }
    public void addCenterNeighbor(Point next){
        this.centerNeighbors.add(next);
    }
    public void addRightNeighbor(Point next){
        this.rightNeighbors.add(next);
    }

    // Stage I
    public void acceleration(){
        if(this.occupied && this.velocity < MAX_VELOCITY)
            velocity++;
    }

    // Stage II
    public void slowingDownOrChangingLane(){
        if(this.hasOvertaken > 0 && this.canChangeLane(this.rightNeighbors)) {
            this.nextDirection = Lane.Right;
            return;
        }
        for(int i = MAX_VELOCITY; i < MAX_VELOCITY + this.velocity; i++)
            if(this.centerNeighbors.get(i).occupied) {
                if(this.canChangeLane(this.leftNeighbors)) {
                    this.nextDirection = Lane.Left;
                    return;
                }
                this.nextDirection = Lane.Center;
                this.velocity = i - MAX_VELOCITY;
                return;
            }
    }

    // Stage III
    public void randomization(){
        if(this.velocity >= 1)
            if(generator.nextDouble() <= probabilityOfSlowdown)
                this.velocity--;
    }

    // Stage IV
    public void carMotion(){
        if(this.velocity == 0 || this.isAfterMove())
            return;
        
        Point next;
        if(this.nextDirection == Lane.Right) {
            next = this.rightNeighbors.get(MAX_VELOCITY + this.velocity-1);
            next.hasOvertaken = 0;
        }
        else if(this.nextDirection == Lane.Center) {
            // Not moving case
            if(this.velocity == 0){
                this.isAfterMove = true;
                return;
            }
            next = this.centerNeighbors.get(MAX_VELOCITY + this.velocity-1);
            next.hasOvertaken = Math.max(0, this.hasOvertaken - 1);
        }
        else {
            next = this.leftNeighbors.get(MAX_VELOCITY + this.velocity-1);
            next.hasOvertaken = this.hasOvertaken = 5;
        }

        next.occupied = true;
        next.velocity = this.velocity;
        next.isAfterMove = true;
        this.clear();
    }

    public boolean getStatus(){
        return occupied;
    }

    public void setBeforeMove(){
        this.isAfterMove = false;
    }

    public boolean isAfterMove(){
        return isAfterMove;
    }

    private boolean canChangeLane(ArrayList<Point> neighbors){
        for(int i = 0; i < this.velocity + MAX_VELOCITY; i++)
            if(neighbors.get(i).occupied)
                return false;
        for(int i = 0; i < MAX_VELOCITY; i++)
            if(this.centerNeighbors.get(i).occupied)
                return false;
        return true;
    }

    public enum Lane {
        Left, Center, Right
    }

}