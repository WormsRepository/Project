package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of exceptions signaling an illegal amount of action points.
 * Each illegal action points exception involves the illegal amount and the worm.
 * 
 * @author Laurens Loots, Pieter Vos
 */
@SuppressWarnings("serial")
public class IllegalActionPointsException extends RuntimeException 
{
	/**
	 * Initialize this new illegal action points exception with the given amount of action points 
	 * and the given worm.
	 * 
	 * @param 	actionPoints
	 * 			The amount of action points for this new illegal action points exception.
	 * @param	worm
	 * 			The Worm for this new illegal action points exception.
	 * @post	The action points of this new illegal action points exception is equal to the given amount.
	 * 			| new.getActionPoints() == actionPoints
	 * @post	The worm of this new illegal action points exception is equal to the given worm.
	 * 			| new.getWorm() == worm
	 * @effect	This new illegal action points exception is further initialized as a new runtime exception
	 * 			involving no diagnostic message and no cause.
	 * 			| super()
	 */
	public IllegalActionPointsException(int actionPoints, Worm worm)
	{
		this.actionPoints= actionPoints;
		this.worm = worm;
	}


	/**
	 * Return the action points of this illegal action points exception.
	 */
	@Basic @Raw @Immutable
	public int getActionPoints()
	{
		return this.actionPoints;
	}

	/**
	 * Variable registering the illegal action points of this illegal action points exception.
	 */
	private final int actionPoints;


	/**
	 * Return the Worm of this illegal action points exception.
	 */
	@Basic @Raw @Immutable
	public Worm getWorm()
	{
		return this.worm;
	}

	/**
	 * Variable referencing the worm of this illegal action points exception.
	 */
	private final Worm worm;
}
