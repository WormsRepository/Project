 package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of positions involving the worm to which the position belongs,
 * an x coordinate and and y coordinate.
 * 
 * @version	1.0
 * @author 	Laurens Loots, Pieter Vos
 */
public class Position{
	
	/**
	 * Create a new position that has the given worm as the worm to which 
	 * this position belongs, has the given x coordinate and y-coordinate.
	 * 
	 * @param 	worm
	 * 			The worm to which this position belongs.
	 * @param	x
	 * 			The x-coordinate of the new position of the worm (in meter).
	 * @param 	y
	 * 			The y-coordinate of the new position of the worm (in meter).
	 * @post	The new position has the given worm as its worm.
	 * 			| new.getWorm() == worm
	 * @effect	The x coordinate and y coordinate is set to the given values.
	 * 			| this.setX(x)
	 * 			| this.setY(y)
	 */
	public Position(Worm worm, double x, double y){
		this.worm = worm;
		setX(x);
		setY(y);
	}
	
	protected Worm getWorm(){
		return this.worm;
	}
	
	/**
	 * Variable registering the worm to which this position belongs.
	 */
	private final Worm worm;
	
	
	
	
	/**
	 * Returns whether or not the game is started.
	 */
	@Basic @Raw
	public boolean isStarted(){
		return this.isStarted;
	}
	
	/**
	 * Variable registering whether or not the game is started.
	 */
	private boolean isStarted = false;
	
	
	
	
	/**
	 * Return the x-coordinate of the position of the worm (in meter).
	 */
	@Basic @Raw
	public double getX() {
		return this.x;
	}

	/**
	 * Set the x-coordinate for this position of the worm to the given x-coordinate.
	 * 
	 * @param 	x
	 * 			The new x-coordinate for this worm (in meter).
	 * @post	The new x-coordinate for this worm is equal to the given x-coordinate.
	 * 			| new.getX() == x
	 */
	@Model
	private void setX(double x)
	{
		this.x = x;
	}

	/**
	 * Return the y-coordinate of the position of the worm (in meter).
	 */
	@Basic @Raw
	public double getY() {
		return this.y;
	}

	/**
	 * Set the y-coordinate for this position of the worm to the given y-coordinate.
	 * 
	 * @param 	y
	 * 			The new y-coordinate for this worm (in meter).
	 * @post	The new y-coordinate for this worm is equal to the given y-coordinate.
	 * 			| new.getY() == y
	 */
	@Model
	private void setY(double y)
	{
		this.y = y;
	}
	
	/**
	 * Check whether the given number of steps is a valid amount of steps for the worm.
	 * 
	 * @param 	nbSteps
	 * 			The amount of steps to check.
	 * @return	True if and only if the amount of steps is larger than zero 
	 * 			and there are enough action points available for the move.
	 * 			| nbSteps > 0 && 
	 * 			| worm.getCurrentActionPoints() >= Math.ceil((Math.abs(Math.cos(worm.getDirection())) 
	 * 			|	+ Math.abs(4*Math.sin(worm.getDirection()))) * nbSteps)
	 */
	@Raw
	public boolean canMove(int nbSteps) 
	{
		return nbSteps > 0 && 
				worm.getCurrentActionPoints() >= Math.ceil((Math.abs(Math.cos(worm.getDirection())) + 
						Math.abs(4*Math.sin(worm.getDirection()))) * nbSteps);
	}
	
	/**
	 * Check whether the worm with the current position can fall.
	 * 
	 * @return	True if and only if the lower part of the worm is not adjacent to impassable
	 * 			terrain.
	 * 			| result == this.getWorm().getWorld().canFall(getX(),getY(),this.getWorm().getRadius())
	 */
	@Raw
	public boolean canFall(){
		return this.getWorm().getWorld().canFall(getX(),getY(),this.getWorm().getRadius());
	}
	
	/**
	 * Check whether the worm with the given position can fall.
	 * 
	 * @param 	x
	 * 			The x-coordinate of the position to check.
	 * @param 	y
	 * 			The y-coordinate of the position to check.
	 * @return	True if and only if the lower part of the worm with the given position to
	 * 			check is not adjacent to impassable terrain.
	 * 			| result == this.getWorm().getWorld().canFall(x, y, this.getWorm().getRadius())
	 */
	private boolean canFall(double x, double y){
		return this.getWorm().getWorld().canFall(x, y, this.getWorm().getRadius());
	}
	
	/**
	 * Return the position of the worm at a given time in a jump.
	 * 
	 * @param 	t
	 * 			The time to check the position of the worm.
	 * @return	The position of the worm at the given time in the jump.
	 * @throws	IllegalActionPointsException(0,worm)
	 * 			It is not possible to perform a jump (and have an initial velocity for a jump)
	 * 			if the amount of current action points of the worm is zero.
	 * 			| worm.getCurrentActionPoints() == 0
	 * @throws 	IllegalDirectionException(worm.getDirection(),worm)
	 * 			It is not possible to perform a jump (and have an initial velocity for a jump)
	 * 			if the direction of the worm is greater than pi.
	 * 			| Math.PI < worm.getDirection()
	 */
	public double[] getJumpStep(double t) 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		double horizontalVelocity = getInitialVelocity() * Math.cos(worm.getDirection());
		double xPosition = getX() + horizontalVelocity * t;
		double verticalVelocity = getInitialVelocity() * Math.sin(worm.getDirection());
		double yPosition = getY() + verticalVelocity*t - (STANDARD_ACCELERATION*Math.pow(t,2))/2.0;
		double[] position = {xPosition, yPosition};
		return position;
	}
	
	/**
	 * Calculate the jump time from a jump in the current direction of the worm 
	 * with the number of remaining action points of the worm.
	 * 
	 * @return	The time needed for a jump in the current state of the worm.
	 * 			| if(worm.getDirection() == 0 || worm.getDirection() == Math.PI)
				| 	then result == 0
	 * 			| else result == 2*getInitialVelocity()*Math.sin(worm.getDirection())/STANDARD_ACCELERATION;
	 * @throws	IllegalActionPointsException(0,worm)
	 * 			It is not possible to perform a jump (and have an initial velocity for a jump)
	 * 			if the amount of current action points of the worm is zero.
	 * 			| worm.getCurrentActionPoints() == 0
	 * @throws 	IllegalDirectionException(worm.getDirection(),worm)
	 * 			It is not possible to perform a jump (and have an initial velocity for a jump)
	 * 			if the direction of the worm is greater than pi.
	 * 			| Math.PI < worm.getDirection()
	 */
	public double getJumpTime() 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		if(worm.getDirection() == 0 || worm.getDirection() == Math.PI)
			return 0;
		return 2*getInitialVelocity()*Math.sin(worm.getDirection())/STANDARD_ACCELERATION;
	}
	
	/**
	 * Change the position of the worm as the result of a jump from the current position 
	 * and with respect to the worm's orientation, his mass, the standard acceleration 
	 * and the number of remaining action points.
	 * 
	 * @effect	The x coordinate is set to the old x coordinate incremented 
	 * 			with the distance of the jump.
	 * 			| this.setX(this.getX() + this.getDistance());
	 * @effect	The new amount of current action points is set to zero.
	 * 			| worm.setCurrentActionPoints(0)
	 */
	public void jump() 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		double distance = getDistance();
		setX(getX() + distance);
		worm.setCurrentActionPoints(0);
	}
	
	/**
	 * Moves the worm in the current direction of the worm with the given number of steps.
	 * 
	 * @param 	nbSteps
	 * 			The number of steps to move.
	 * @post 	The x position and the y position are changed based on the number of steps, the radius,
	 * 			the direction and the starting values of x and y of the worm. 
	 * 			The new amount of current action points of the worm is the old amount of 
	 * 			action points minus the used action points.
	 * 			| new.getX() == getX() + Math.cos(worm.getDirection()) * worm.getRadius() * nbSteps
	 * 			| new.getY() == getY() + Math.sin(worm.getDirection()) * worm.getRadius() * nbSteps
	 * 			| new.worm.getCurrentActionPoints() ==
	 * 			|		 worm.getCurrentActionPoints() - (long)Math.ceil((Math.abs(Math.cos(worm.getDirection())) 
	 * 			|			+ Math.abs(4*Math.sin(worm.getDirection()))) * nbSteps)
	 * @Throws	IllegalArgumentException("The argument 'number of steps' is invalid.")
	 * 			The given amount of steps is not a valid amount of steps.
	 * 			| !canMove(nbSteps)
	 */
	public void move(int nbSteps) 
			throws IllegalArgumentException
	{
		if(! canMove(nbSteps))
			throw new IllegalArgumentException("The argument 'number of steps' is invalid.");

		setX(getX() + Math.cos(worm.getDirection()) * worm.getRadius() * nbSteps);
		setY(getY() + Math.sin(worm.getDirection()) * worm.getRadius() * nbSteps);

		long costOfActionPoints = (long)Math.ceil((Math.abs(Math.cos(worm.getDirection())) + 
				Math.abs(4*Math.sin(worm.getDirection()))) * nbSteps);
		worm.setCurrentActionPoints(worm.getCurrentActionPoints() - costOfActionPoints);
	}
	
	//TODO moeilijke documentatie aanvullen lusinvarianten...
	public void fall(){
		double tempY = getY();
		double temp = 10;
		while(canFall(getX(),tempY)){
			while(canFall(getX(),tempY))
				tempY -= temp;
			temp = temp / 3;
			while(!canFall(getX(),tempY) &&
					!!getWorm().getWorld().isAdjacent(getX(), tempY, this.getWorm().getRadius()) )
				tempY += temp;
			temp = temp / 3;
		}
		setY(tempY);
	}
	
	/**
	 * Calculate the distance covered by a jump in the current direction of the worm
	 * and with respect to the worm's mass, the standard acceleration 
	 * and the number of remaining action points.
	 * 
	 * @return	The distance covered by a jump in the current direction and with respect to the worm's
	 * 			mass, the standard acceleration and the number of remaining action points.
	 * 			| (Math.pow(getInitialVelocity(), 2) * Math.sin(2*getDirection()))/STANDARD_ACCELERATION
	 * @throws	IllegalActionPointsException(0,worm)
	 * 			It is not possible to perform a jump (and have an initial velocity for a jump)
	 * 			if the amount of current action points of the worm is zero.
	 * 			| worm.getCurrentActionPoints() == 0
	 * @throws 	IllegalDirectionException(worm.getDirection(),worm)
	 * 			It is not possible to perform a jump (and have an initial velocity for a jump)
	 * 			if the direction of the worm is greater than pi.
	 * 			| Math.PI < worm.getDirection()
	 */
	@Model
	private double getDistance() 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		return (Math.pow(getInitialVelocity(), 2) * Math.sin(2*worm.getDirection()))/STANDARD_ACCELERATION;
	}
	
	/**
	 * Calculate the initial velocity for a jump with the current amount of 
	 * action points of the worm, the mass of the worm and the standard acceleration.
	 * 
	 * @return	The initial velocity with a jump in the current state of the worm.
	 * 			| (((5.0*(double)worm.getCurrentActionPoints()) + (worm.getMass() * STANDARD_ACCELERATION))/
	 * 			| worm.getMass()) * 0.5
	 * @throws	IllegalActionPointsException(0,worm)
	 * 			It is not possible to perform a jump (and have an initial velocity for a jump)
	 * 			if the amount of current action points of the worm is zero.
	 * 			| worm.getCurrentActionPoints() == 0
	 * @throws 	IllegalDirectionException(worm.getDirection(),worm)
	 * 			It is not possible to perform a jump (and have an initial velocity for a jump)
	 * 			if the direction of the worm is greater than pi.
	 * 			| Math.PI < worm.getDirection()
	 */
	@Model
	private double getInitialVelocity() 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		if(worm.getCurrentActionPoints() == 0)
			throw new IllegalActionPointsException(0,worm);
		if(Math.PI < worm.getDirection())
			throw new IllegalDirectionException(worm.getDirection(),worm);
		double force = (5.0*(double)worm.getCurrentActionPoints()) + (worm.getMass() * STANDARD_ACCELERATION);
		return (force/worm.getMass()) * 0.5;
	}
	
	/**
	 * Variable registering the x-coordinate of a position of a worm in meters.
	 */
	private double x;

	/**
	 * Variable registering the y-coordinate of a position of a worm in meters.
	 */
	private double y;
	
	/**
	 * Final class variable registering the standard acceleration (m/(s*s)).
	 */
	private final static double STANDARD_ACCELERATION = 9.80665;

}
