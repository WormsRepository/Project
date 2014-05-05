package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of exceptions signaling an illegal name of worms.
 * Each illegal name exception involves at least the illegal name.
 * 
 * @author Laurens Loots, Pieter Vos
 */
@SuppressWarnings("serial")
public class IllegalNameException extends RuntimeException 
{
	/**
	 * Initialize this new illegal name exception with given name and given worm.
	 * 
	 * @param 	name
	 * 			The name for this new illegal name exception.
	 * @param	worm
	 * 			The Worm for this new illegal name exception.
	 * @post	The name of this new illegal name exception is equal to the given name.
	 * 			| new.getName() == name
	 * @post	The worm of this new illegal name exception is equal to the given worm.
	 * 			| new.getWorm() == worm
	 * @effect	This new illegal name exception is further initialized as a new runtime exception
	 * 			involving no diagnostic message and no cause.
	 * 			| super()
	 */
	public IllegalNameException(String name, Worm worm)
	{
		this.name = name;
		this.worm = worm;
	}
	
	/**
	 * Initialize this new illegal name exception with given name and no worm.
	 * 
	 * @param 	name
	 * 			The name for this new illegal name exception.
	 * @effect	This new illegal name exception is initialized with the given name as its
	 * 			name and null as the worm it references.
	 * 			| this(name, null)
	 */
	public IllegalNameException(String name)
	{
		this(name, null);
	}


	/**
	 * Return the name of this illegal name exception.
	 */
	@Basic @Raw @Immutable
	public String getName()
	{
		return this.name;
	}

	/**
	 * Variable registering the illegal name of this illegal name exception.
	 */
	private final String name;


	/**
	 * Return the Worm of this illegal name exception.
	 */
	@Basic @Raw @Immutable
	public Worm getWorm()
	{
		return this.worm;
	}

	/**
	 * Variable referencing the worm of this illegal name exception.
	 */
	private Worm worm;
}
