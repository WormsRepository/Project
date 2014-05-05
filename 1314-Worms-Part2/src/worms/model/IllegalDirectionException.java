package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of exceptions signaling an illegal direction for a jump.
 * 	Each illegal direction exception involves at least the illegal direction.
 * 
 * @author Laurens Loots, Pieter Vos
 */
@SuppressWarnings("serial")
public class IllegalDirectionException extends RuntimeException
{
	/**
	 * Initialize this new illegal direction exception with given direction and given worm.
	 * 
	 * @param	direction
	 * 			The direction for this new illegal direction exception.
	 * @param	worm
	 * 			The Worm for this new illegal direction exception.
	 * @post	The direction of this new illegal direction exception is equal to the given direction.
	 * 			| new.getDirection() == direction
	 * @post	The worm of this new illegal direction exception is equal to the given worm.
	 * 			| new.getWorm() == worm
	 * @effect	This new illegal direction exception is further initialized as a new runtime exception
	 * 			involving no diagnostic message and no cause.
	 * 			| super()
	 */
	public IllegalDirectionException(double direction, Worm worm)
	{
		this.direction = direction;
		this.worm = worm;
	}
	
	/**
	 * Initialize this new illegal direction exception with given direction and no worm.
	 * 
	 * @param	direction
	 * 			The direction for this new illegal direction exception.
	 * @effect	This new illegal direction exception is initialized with the given direction
	 * 			as its direction and null as the worm it references.
	 * 			| this(direction, null)
	 */
	public IllegalDirectionException(double direction)
	{
		this(direction, null);
	}


	/**
	 * Return the direction of this illegal direction exception.
	 */
	@Basic @Raw @Immutable
	public double getDirection()
	{
		return this.direction;
	}

	/**
	 * Variable registering the direction of this illegal direction exception.
	 */
	private final double direction;


	/**
	 * Return the Worm of this illegal direction exception.
	 */
	@Basic @Raw @Immutable
	public Worm getWorm()
	{
		return this.worm;
	}

	/**
	 * Variable referencing the worm of this illegal direction exception.
	 */
	private final Worm worm;
}
