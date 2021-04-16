import java.util.ArrayList;
import java.util.Random;

public class Point {

    // TODO
    private int status; // 0 - empty, 1 - occupied
    public int velocity;
    private ArrayList<Point> neighbors = new ArrayList<>();
    private int MAX_VELOCITY;
    private final double probabilityOfSlowdown = 0.5;
    private Random generator = new Random();
    private boolean isAfterMove = false;


    public Point(int maxV) {
        this.MAX_VELOCITY = maxV;
        this.clear();
    }

    public void clicked() {
        // TODO
        this.status = 1;
    }

    public void clear() {
        // TODO
        this.status = 0;
        this.velocity = 0;
        this.isAfterMove = false;
    }

    public void addNeighbor(Point next){
        this.neighbors.add(next);
    }

    // Stage I
    public void acceleration(){
        if(this.status == 1 && this.velocity < MAX_VELOCITY)
            velocity++;
    }

    // Stage II
    public void slowingDown(){
        for(int i = 0; i < this.velocity; i++)
            if(this.neighbors.get(i).status == 1) {
                this.velocity = i;
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
        Point next = this.neighbors.get(this.velocity-1);
        next.status = 1;
        next.velocity = this.velocity;
        next.isAfterMove = true;

        this.velocity = 0;
        this.status = 0;
    }

    public int getStatus(){
        return status;
    }

    public void setBeforeMove(){
        this.isAfterMove = false;
    }

    public boolean isAfterMove(){
        return isAfterMove;
    }

}