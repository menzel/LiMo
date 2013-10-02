package de.thm.bi.utils;

/**
 * Vector class to manage vectors in 3D space.
 * 
 * @author Artur Klos
 * 
 */
public class Vector3D {
	/**
	 * Add two vectors
	 * 
	 * @param v1
	 *            a vector
	 * @param v2
	 *            another vector
	 * @return a new vector that is the sum of v1 and v2
	 */
	public static Vector3D add(Vector3D v1, Vector3D v2) {
		Vector3D v = new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
		return v;
	}

	/**
	 * Copy the vector
	 * 
	 * @param v
	 *            the vector to be copied
	 * @return a copy of the vector
	 */
	public static Vector3D copy(Vector3D v) {
		return new Vector3D(v.x, v.y, v.z);
	}

	/**
	 * Calculate the Euclidean distance between two points (considering a point
	 * as a vector object)
	 * 
	 * @param v1
	 *            a vector
	 * @param v2
	 *            another vector
	 * @return the Euclidean distance between v1 and v2
	 */
	public static float distance(Vector3D v1, Vector3D v2) {
		float dx = v1.x - v2.x;
		float dy = v1.y - v2.y;
		float dz = v1.z - v2.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Divide a vector by a scalar
	 * 
	 * @param v1
	 *            a vector
	 * @param n
	 *            scalar
	 * @return a new vector that is v1 / n
	 */
	public static Vector3D div(Vector3D v1, float n) {
		Vector3D v = new Vector3D(v1.x / n, v1.y / n, v1.z / n);
		return v;
	}

	/**
	 * Multiply a vector by a scalar
	 * 
	 * @param v1
	 *            a vector
	 * @param n
	 *            scalar
	 * @return a new vector that is v1 * n
	 */
	public static Vector3D mult(Vector3D v1, float n) {
		Vector3D v = new Vector3D(v1.x * n, v1.y * n, v1.z * n);
		return v;
	}

	/**
	 * Calculate the angle between two vectors, using the dot product
	 * 
	 * @param v1
	 *            a vector
	 * @param v2
	 *            another vector
	 * @return the angle between the vectors
	 */
	public static float org(Vector3D v1, Vector3D v2) {
		float dot = v1.dot(v2);
		float theta = (float) Math.acos(dot / (v1.magnitude() * v2.magnitude()));
		return theta;
	}

	/**
	 * Rotates a 2D Vector
	 * 
	 * @param theta
	 *            , angle in radians to rotate vector
	 * @return a new Vector object, rotated by theta
	 */
	public static Vector3D rotate2D(Vector3D v, float theta) {
		// What is my current heading
		float currentTheta = v.heading2D();
		// What is my current speed
		float mag = v.magnitude();
		// Turn me
		currentTheta += theta;
		// Look, polar coordinates to cartesian!!
		Vector3D newV = new Vector3D((float) (mag * Math.cos(currentTheta)), (float) (mag * Math.cos(currentTheta)));
		return newV;
	}

	/**
	 * Subtract one vector from another
	 * 
	 * @param v1
	 *            a vector
	 * @param v2
	 *            another vector
	 * @return a new vector that is v1 - v2
	 */
	public static Vector3D sub(Vector3D v1, Vector3D v2) {
		Vector3D v = new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
		return v;
	}

	public float x;

	public float y;

	public float z;

	/**
	 * Constructor for an empty vector: x, y, and z are set to 0.
	 */

	public Vector3D() {
		this.x = 0f;
		this.y = 0f;
		this.z = 0f;
	}

	/**
	 * Constructor for a 2D vector: z coordinate is set to 0.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 */

	public Vector3D(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 0f;
	}

	/**
	 * Constructor for a 3D vector.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the y coordinate.
	 */

	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Add a vector to this vector
	 * 
	 * @param v
	 *            the vector to be added
	 */
	public Vector3D add(Vector3D v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	/**
	 * Copy the vector
	 * 
	 * @return a copy of the vector
	 */
	public Vector3D copy() {
		return new Vector3D(x, y, z);
	}

	/**
	 * Calculate the cross product with another vector
	 * 
	 * @return the cross product
	 */
	public Vector3D cross(Vector3D v) {
		float crossX = y * v.z - v.y * z;
		float crossY = z * v.x - v.z * x;
		float crossZ = x * v.y - v.x * y;
		return (new Vector3D(crossX, crossY, crossZ));
	}

	/**
	 * Divide this vector by a scalar
	 * 
	 * @param n
	 *            the value to divide by
	 */
	public Vector3D div(float n) {
		x /= n;
		y /= n;
		z /= n;
		return this;
	}

	/**
	 * Calculate the dot product with another vector
	 * 
	 * @return the dot product
	 */
	public float dot(Vector3D v) {
		float dot = x * v.x + y * v.y + z * v.z;
		return dot;
	}

	/**
	 * Calculate the angle of rotation for this vector (only 2D vectors)
	 * 
	 * @return the angle of rotation
	 */
	public float heading2D() {
		float angle = (float) Math.atan2(-y, x);
		return -1 * angle;
	}

	/**
	 * Limit the magnitude of this vector
	 * 
	 * @param max
	 *            the maximum length to limit this vector
	 */
	public Vector3D limit(float max) {
		if (magnitude() > max) {
			normalize();
			mult(max);
		}
		return this;
	}

	/**
	 * Calculate the magnitude (length) of the vector
	 * 
	 * @return the magnitude of the vector
	 */
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Multiply this vector by a scalar
	 * 
	 * @param n
	 *            the value to multiply by
	 */
	public Vector3D mult(float n) {
		x *= n;
		y *= n;
		z *= n;
		return this;
	}

	/**
	 * Normalize the vector to length 1 (make it a unit vector)
	 */
	public Vector3D normalize() {
		float m = magnitude();
		if (m > 0) {
			div(m);
		}
		return this;
	}

	/**
	 * Rotates a 2D Vector
	 * 
	 * @param theta
	 *            , angle in radians to rotate vector
	 */
	public Vector3D rotate2D(float theta) {
		float currentTheta = heading2D();
		float mag = magnitude();
		currentTheta += theta;
		x = (float) (mag * Math.cos(currentTheta));
		y = (float) (mag * Math.sin(currentTheta));
		return this;
	}

	/**
	 * Set the x coordinate.
	 * 
	 * @param x
	 *            the x coordinate.
	 */

	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Set x,y, and z coordinates.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 */
	public Vector3D setXYZ(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/**
	 * Set x,y, and z coordinates from a Vector3D object.
	 * 
	 * @param v
	 *            the Vector3D object to be copied
	 */
	public Vector3D setXYZ(Vector3D v) {
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}

	/**
	 * Set the y coordinate.
	 * 
	 * @param y
	 *            the y coordinate.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Set the z coordinate.
	 * 
	 * @param z
	 *            the z coordinate.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Subtract a vector from this vector
	 * 
	 * @param v
	 *            the vector to be subtracted
	 */
	public Vector3D sub(Vector3D v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

}
