
public class Vector
{
	public static final double dt = 0.08; // time elapsed between TWO frames
	public double waterV = 0.55934; // meters per second; how fast the swimmer is traveling relative to a stationary observer.
	public double x;
	public double y;
	
	public Vector()
	{
		x = 0;
		y = 0;
	}
	
	public Vector(double tx, double ty)
	{
		x = tx;
		y = ty;
	}
	
	public Vector(Vector v1, Vector v2, boolean velocity)
	{
		x = (v2.x - v1.x) / dt;
		y = (v2.y - v1.y) / dt;
		if (velocity)
		{
			x -= waterV;
		}
	}
	
	public String toString()
	{
		return x + ", " + y;
	}
	
	// Finds the dot product of two vectors (helper method)
	public double dot(Vector v)
	{
		return x * v.x + y * v.y;
	}
	
	// Multiplies the vector by a constant
	public void multiply(double k)
	{
		x *= k;
		y *= k;
	}
	
	// Returns the magnitude of the vector
	public double magnitude()
	{
		return Math.sqrt(x * x + y * y);
	}
	
	// Returns a unit vector in the same direction as the current vector
	public Vector norm()
	{
		return new Vector(x / magnitude(), y / magnitude());
	}
	
	// Returns a vector describing the perpendicular vector to this one 
	// following the right hand rule
	public Vector getN()
	{
		return new Vector(-y, x);
	}
	
	// returns a vector projection of the current vector onto the parameter vector.
	// Projection formula: u dot norm(v) * norm(v)
	public Vector project(Vector onto)
	{
		Vector projected = onto.norm();
		projected.multiply(this.dot(projected));
		return projected;
	}
	
	// Returns x
	public double x()
	{
		return x;
	}
	
	// Returns y
	public double y()
	{
		return y;
	}
}