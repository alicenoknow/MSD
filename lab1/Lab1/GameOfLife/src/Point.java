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

		// 23/3
		//ArrayList<Integer> toStayAlive = new ArrayList<>(Arrays.asList(2, 3));
		//ArrayList<Integer> toGetAlive = new ArrayList<>(Arrays.asList(3));

		// 2345/45678 - cities
		//ArrayList<Integer> toStayAlive = new ArrayList<>(Arrays.asList(2, 3, 4, 5));
		//ArrayList<Integer> toGetAlive = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8));

		//45678/3 - coral
		ArrayList<Integer> toStayAlive = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8));
		ArrayList<Integer> toGetAlive = new ArrayList<>(Arrays.asList(3));

		int aliveNeighbors = this.countAliveNeighbor();
		if(this.currentState == 1) {
			if (toStayAlive.contains(aliveNeighbors)){
				this.nextState = 1;
				return;
			}
		}
		else if(toGetAlive.contains(aliveNeighbors)){
				this.nextState = 1;
				return;
			}
		this.nextState = 0;
	}

	public void changeState() {
		currentState = nextState;
	}
	
	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	private int countAliveNeighbor(){
		int count = 0;
		for (Point nei : this.neighbors)
			if(nei.currentState == 1)
				count++;
		return count;
	}
	//TODO: write method counting all active neighbors of THIS point
}
