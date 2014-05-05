package worms.model;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of exceptions signaling an illegal position.
 * 	Each illegal position exception involves
 *  the x-coordinate and y-coordinate.
 * 
 * @version	1.0
 * @author Laurens Loots, Pieter Vos
 */
@SuppressWarnings("serial")
public class IllegalPositionException extends RuntimeException{
	
	/**
	 * Initialize this new illegal position exception with given
	 * x-coordinate and y-coordinate.
	 * 
	 * @param 	x
	 * 			The x-coordinate for this new illegal position exception.
	 * @param 	y
	 * 			The y-coordinate for this new illegal position exception.
	 * @post	The x-coordinate of this new illegal position exception 
	 * 			is equal to the given x-coordinate.
	 * 			| new.getX() == x;
	 * @post	The y-coordinate of this new illegal position exception 
	 * 			is equal to the given y-coordinate.
	 * 			| new.getY() == y;
	 * @effect	This new illegal position exception is further initialized as a new runtime exception
	 * 			involving no diagnostic message and no cause.
	 * 			| super()
	 */
	public IllegalPositionException(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	
	
	
	/**
	 * Return the x-coordinate of this illegal position exception.
	 */
	@Basic @Raw @Immutable
	public double getX(){
		return this.x;
	}
	
	/**
	 * Variable registering the x-coordinate of this 
	 * illegal position exception in meters.
	 */
	private final double x;

	
	
	
	/**
	 * Return the y-coordinate of this illegal position exception.
	 */
	@Basic @Raw @Immutable
	public double getY(){
		return this.y;
	}
	
	/**
	 * Variable registering the y-coordinate of this 
	 * illegal position exception in meters.
	 */
	private final double y;
}
