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
	
	//TODO documentation
	@Basic @Raw
	protected Worm getWorm(){
		return this.worm;
	}
	
	/**
	 * Variable registering the worm to which this position belongs.
	 */
	private final Worm worm;
	
	
	
	
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
		//max horizontalVelocity/verticalVelocity = 
		//7.5 * 1 = 7.5 (see getinitialVelocity for more explanation)
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
	//TODO documentatie veranderen!!!
	public double getJumpTime() 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		double[] tempXY = {getX(),getY()};
		double radius = this.getWorm().getRadius();
		
		// This temporary variable has to be incremented with a really small value, 
		// with this part of the method we make sure the tempX and tempY are no longer
		// at a adjacent location so we can detect to real collision in the next part 
		// and not the initial position. We also check if the jump in the current 
		// direction is worth to do it, if not, this method throws
		// an IllegalDirectionException.
		
		// We have chosen (1/4)/getInitialVelocity() because with the maximum horizontal or
		// vertical velocity the worm can only move +- 0.25 meter in the horizontal or vertical
		// direction per step, this equals the minimum radius, so we will probably not skip
		// any impassable point of the map.
		double temp = (1/4.0)/getInitialVelocity();
		double tempTime = 0.0;
		while(this.getWorm().getWorld().isAdjacent(tempXY[0], tempXY[1], radius) && tempTime < (1/2.0)){
			tempTime = tempTime + temp;
			tempXY = getJumpStep(tempTime);
		}
		if(this.getWorm().getWorld().isImpassable(tempXY[0], tempXY[1], radius))
			throw new IllegalDirectionException(this.getWorm().getDirection(),this.getWorm());
		//TODO nakijken of deze exception te snel gegooid word?
		
		// if 'temp' is smaller than 1/300 the worm will leave the world because there is no
		// possible adjacent position.
		while(!this.getWorm().getWorld().isAdjacent(tempXY[0], tempXY[1], radius) && temp >= (1/8100.0)){
			while(!this.getWorm().getWorld().isImpassable(tempXY[0], tempXY[1], radius)){
				tempTime = tempTime + temp;
				tempXY = getJumpStep(tempTime);
			}
			temp = temp / 3.0;
			while(this.getWorm().getWorld().isImpassable(tempXY[0], tempXY[1], radius)){
				tempTime = tempTime - temp;
				tempXY = getJumpStep(tempTime);
			}
			temp = temp / 3.0;
		}
		if(temp < (1/8100.0)){
			if(tempTime < Math.PI)
				return Math.PI;
			else
				return 2*Math.PI;
		}
		
		return tempTime;
	}


	
	/**
	 * Change the position of the worm as the result of a jump from the current position 
	 * and with respect to the worm's orientation, his mass, the standard acceleration 
	 * and the number of remaining action points.
	 * 
	 * @effect	The x-coordinate is changed as an effect of the jump.
	 * 			| setX(getJumpStep(getJumpTime())[0])
	 * @effect	The y-coordinate is changed as an effect of the jump.
	 * 			| setY(getJumpStep(getJumpTime())[1])
	 * @effect	The new amount of current action points is set to zero.
	 * 			| worm.setCurrentActionPoints(0)
	 */
	public void jump() 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		double time = getJumpTime();
		double[] tempXY = getJumpStep(time);
		if(time != Math.PI || time != 2*Math.PI){
			setX(tempXY[0]);
			setY(tempXY[1]);
		}
		try{
			this.fall();
		}
		catch (RuntimeException x){}
		this.getWorm().setCurrentActionPoints(0);
		this.getWorm().getWorld().startNextTurn();
		if(time == Math.PI || time == 2*Math.PI)
			this.getWorm().wormDeath();
	}
	
	/**
	 * Moves the worm in the current direction of the worm. 
	 */
	public void move()
		throws IllegalDirectionException
	{
		//find the highest possible distance in a valid direction.
			//use tempDirection to cycle through all the valid directions
			// and store the direction with the highest distance in bestDirection
				//initiate the bestDirection with an invalid direction.
		double bestDirection = -1; 
		double tempDirection = worm.getDirection() - 0.7875;
		double tempDistance = -1;
		double bestDistance = -1;
		//cycle through the different possible directions.
		
		while(tempDirection <= worm.getDirection() + 0.7875)
		{
			tempDistance = move_newDistance(tempDirection);
			if( tempDistance > bestDistance )
			{
				bestDistance = tempDistance; 
				bestDirection = tempDirection ;
			}
			if(bestDistance == tempDistance && (Math.abs(worm.getDirection() - bestDirection) > Math.abs(worm.getDirection() - tempDirection)))
				bestDirection = tempDirection;	
							
			tempDirection += 0.0175;
		}
		//throw exception if no proper distance was found.
		if(bestDistance == -1)
			throw new IllegalDirectionException(this.getWorm().getDirection(),this.getWorm());
	
		//find the proper x and y coordinate and move the worm to this place.
		
		setX(this.move_CalculateX(bestDirection, bestDistance));
		setY(this.move_CalculateY(bestDirection, bestDistance));
		this.getWorm().setCurrentActionPoints((int) (this.getWorm().getCurrentActionPoints() - (Math.abs(Math.cos(bestDirection))+ Math.abs(Math.sin(4*bestDirection)))));
	}
	
	
	/**
	 * calculates the proper x-coordinate for a gives direction and distance
	 */
	private double move_CalculateX(double direction, double distance)
	{
		return this.getX() + (Math.cos(direction)*distance);
	}
	
	/**
	 * calculates the proper y-coordinate for a gives direction and distance
	 */
	private double move_CalculateY(double direction, double distance)
	{
		return this.getY() + (Math.sin(direction)*distance);
	}
	/**
	 * calculates the next possible distance for the move method in a certain direction.
	 * returns 0 if there is no possible distance
	 */
	
	private double move_newDistance(double direction)
	{
		//find the highest possible distance which is in a passable location.
			//initialize the testDistance with the maximum distance
		double testDistance = this.getWorm().getRadius();
			//keep reducing the testDistance by a small amount
			//untill it finds a passable location.
		boolean flag = false;
		double testX = 0, testY = 0;
		while(testDistance >= 0.1 && flag == false)
		{
			testX = move_CalculateX(direction, testDistance);
			testY = move_CalculateY(direction, testDistance);
			if(! this.getWorm().getWorld().isImpassable(testX, testY, this.getWorm().getRadius()))
				flag = true;
			testDistance -= 0.02;
		}
		if(flag == false)
			return 0;
		
		return testDistance;
	}
	
	//TODO moeilijke documentatie aanvullen lusinvarianten...
	public void fall() throws RuntimeException{
		if(!canFall())
			throw new RuntimeException();
		double tempY = getY();
		double temp = 0.5;
		while(canFall(getX(),tempY) && temp >= (1/300)){
			while(inMap(getX(),tempY) && canFall(getX(),tempY))
				tempY -= temp;
			if(!inMap(getX(),tempY))
				tempY += temp;
			temp = temp / 3;
			while(!canFall(getX(),tempY) &&
					!getWorm().getWorld().isAdjacent(getX(), tempY, this.getWorm().getRadius()) )
				tempY += temp;
			temp = temp / 3;
		}
		// if 'temp' is smaller than 1/300 there will be no adjacent position after the fall,
		// the worm will fall of the world and die.
		if(temp < (1/300))
			this.getWorm().wormDeath();
			
		if(this.getWorm().getWorld().isStarted())
			this.getWorm().setCurrentHitPoints(this.getWorm().getCurrentHitPoints() - 
					(int)Math.floor(3 * (getY() - tempY)));
		setY(tempY);
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
		// The highest possible double to return shall always be lower than 7.5,
		// because if the current action points equals the max action points it 
		// shall also be equal to the mass of the worm, in that case, the formula
		// is simplified to (5.0+10)*0.5 = 7.5, if we take the standard acceleration
		// as 10.
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
	@Model
	private boolean canFall(double x, double y){
		return this.getWorm().getWorld().canFall(x, y, this.getWorm().getRadius());
	}
	
	//TODO documentation
	private boolean inMap(double x, double y){
		double radius = this.getWorm().getRadius();
		return x>radius && x<this.getWorm().getWorld().getWidth() - radius &&
				y>radius && y<this.getWorm().getWorld().getHeight() - radius;
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
