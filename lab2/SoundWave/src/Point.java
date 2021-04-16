public class Point {

	public Point nNeighbor;
	public Point wNeighbor;
	public Point eNeighbor;
	public Point sNeighbor;
	public float nVel;
	public float eVel;
	public float wVel;
	public float sVel;
	public float pressure;
	public final double CONST_C = 0.5;
	public static Integer[] types ={ 0, 1, 2};
	public int type;
	private int sinInput;

	public Point() {
		this.type = 0;
		clear();
	}

	public void clicked() {
		this.pressure = 1;
	}

	public void clear() {
		this.nVel = 0;
		this.eVel = 0;
		this.wVel = 0;
		this.sVel = 0;
		this.pressure = 0;
		this.sinInput = 0;
	}

	public void updateVelocity() {
		this.nVel = this.nVel - (this.nNeighbor.pressure - this.pressure);
		this.wVel = this.wVel - (this.wNeighbor.pressure - this.pressure);
		this.eVel = this.eVel - (this.eNeighbor.pressure - this.pressure);
		this.sVel = this.sVel - (this.sNeighbor.pressure - this.pressure);
	}

	public void updatePressure() {
		if(this.type == 2) {
			int a = 10, b = 2;
			sinInput += a;
			double radians = Math.toRadians(b*sinInput);
			pressure = (float) (Math.sin(radians));
		}
		else
			this.pressure = this.pressure - (float)CONST_C*(nVel + wVel + eVel + sVel);
	}

	public float getPressure() {
		return pressure;
	}
}