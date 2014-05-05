 package worms.model;

import be.kuleuven.cs.som.annotate.*;

/**
 * An abstract class of positions involving an x coordinate and an y coordinate.
 * 
 * @invar	Each position must have a valid position.
 * 			| isValidPosition(getX(), getY())
 * 
 * @version	1.0
 * @author 	Laurens Loots, Pieter Vos
 */
public abstract class Position{
	
	/**
	 * Create a new position that has the given x-coordinate and y-coordinate.
	 * 
	 * @param	x
	 * 			The x-coordinate of the new position (in meter).
	 * @param 	y
	 * 			The y-coordinate of the new position (in meter).
	 * @effect	The x coordinate and y coordinate is set to the given values.
	 * 			| this.setPosition(x,y)
	 */
	@Raw
	public Position(double x, double y) 
			throws IllegalPositionException{
		this.setPosition(x,y);
	}
	
	/**
	 * Create a new position without initializing the coordinates.
	 */
	@Raw
	public Position(){}
	
	
	
	/**
	 * Return the x-coordinate of the position (in meter).
	 */
	@Basic @Raw
	public double getX() {
		return this.x;
	}

	/**
	 * Set the x-coordinate for this position to the given x-coordinate.
	 * 
	 * @param 	x
	 * 			The new x-coordinate (in meter).
	 * @post	The new x-coordinate is equal to the given x-coordinate.
	 * 			| new.getX() == x
	 * @throws	IllegalPositionException(x, getY())
	 * 			| !isValidPosition(x, getY())
	 */
	@Model
	protected void setX(double x)
			throws IllegalPositionException{
		if(!isValidPosition(x, getY()))
			throw new IllegalPositionException(x, getY());
		this.x = x;
	}

	/**
	 * Return the y-coordinate of the position (in meter).
	 */
	@Basic @Raw
	public double getY() {
		return this.y;
	}

	/**
	 * Set the y-coordinate for this position to the given y-coordinate.
	 * 
	 * @param 	y
	 * 			The new y-coordinate (in meter).
	 * @post	The new y-coordinate is equal to the given y-coordinate.
	 * 			| new.getY() == y
	 * @throws	IllegalPositionException(getX(), y)
	 * 			| !isValidPosition(getX(), y)
	 */
	@Model
	protected void setY(double y)
			throws IllegalPositionException{
		if(!isValidPosition(getX(), y))
			throw new IllegalPositionException(getX(), y);
		this.y = y;
	}
	
	/**
	 * Check if the given position is a valid position.
	 * 
	 * @param 	x
	 * 			The x-coordinate to check.
	 * @param 	y
	 * 			The y-coordinate to check.
	 * @Return	The coordinates will not be negative
	 * 			| x >= 0 && y >= 0
	 */
	@Model
	protected abstract boolean isValidPosition(double x, double y);
	
	/**
	 * Set the x-coordinate and y-coordinate to the given coordinates.
	 * 
	 * @param 	x
	 * 			The new x-coordinate (in meter).
	 * @param 	y
	 * 			The new y-coordinate (in meter).
	 * @post	The new x-coordinate is equal to the given x-coordinate.
	 * 			| new.getX() == x
	 * @post	The new y-coordinate is equal to the given y-coordinate.
	 * 			| new.getY() == y
	 * @throws 	IllegalPositionException(x, y)
	 * 			| !isValidPosition(x, y)
	 */
	@Model
	protected void setPosition(double x, double y) 
			throws IllegalPositionException{
		if(!isValidPosition(x, y))
			throw new IllegalPositionException(x, y);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Variable registering the x-coordinate of a position in meters.
	 */
	private double x = 0;

	/**
	 * Variable registering the y-coordinate of a position in meters.
	 */
	private double y = 0;
}
