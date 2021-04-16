import java.util.ArrayList;
import java.util.Arrays;

public class  Point {
	private ArrayList<Point> neighbors;
	private int currentState;
	private int nextState;
	private int numStates = 6;
	
	public Point() {
		currentState = 0;
		nextState = 0;
		neighbors = new ArrayList<Point>();
	}

	public void clicked() {
		currentState=(++currentState)%numStates;	
	}
	
	public int getState() {
		return currentState;
	}

	public void setState(int s) {
		currentState = s;
	}

	public void calculateNewState() {
		//TODO: insert logic which updates according to currentState
		if(currentState > 0)
			nextState = currentState - 1;
		else if(this.neighbors.size() > 0){
			if(this.neighbors.get(0).currentState > 0)
				nextState = 6;
		}
	}

	public void changeState() {
		currentState = nextState;
	}
	
	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	private int countAliveNeighbor(){
		//TODO: write method counting all active neighbors of THIS point
		int count = 0;
		for (Point nei : this.neighbors)
			if(nei.currentState == 1)
				count++;
		return count;
	}

	public void drop(){
		double x = Math.random();
		if(x <= 0.03)
			this.nextState = 6;
	}
}
