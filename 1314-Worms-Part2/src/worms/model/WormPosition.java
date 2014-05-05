package worms.model;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of worm positions involving the worm to which the position belongs,
 * an x coordinate and an y coordinate.
 * 
 * @version	1.0
 * @author 	Laurens Loots, Pieter Vos
 */
public class WormPosition extends Position{
	
	/**
	 * Create a new worm position that has the given worm as the worm to which 
	 * this position belongs, has the given x coordinate and y-coordinate.
	 * 
	 * @param 	worm
	 * 			The worm to which this position belongs.
	 * @param	x
	 * 			The x-coordinate of the new position of the worm (in meter).
	 * @param 	y
	 * 			The y-coordinate of the new position of the worm (in meter).
	 * @post	The new worm position has the given worm as its worm.
	 * 			| new.getWorm() == worm
	 * @effect	This new worm position is initialized as a position with
	 * 			the given x-coordinate and y-coordinate
	 * 			| super(x,y)
	 */
	public WormPosition(Worm worm, double x, double y){
		super(x,y);
		this.worm = worm;
	}
	
	/**
	 * Return the worm to which this position is attached.
	 */
	@Basic @Raw
	protected Worm getWorm(){
		return this.worm;
	}
	
	/**
	 * Variable registering the worm to which this position belongs.
	 */
	private final Worm worm;
	
	
	
	
	/**
	 * Check whether the worm can move or not.
	 */
	//TODO documentation
	@Raw
	public boolean canMove() 
	{
		double testDirection = this.getWorm().getDirection() + 0.7875;
		
		while(testDirection >= this.getWorm().getDirection() - 0.7875)
		{
			if(this.move_newDistance(testDirection) != -2){
				return true;
			}
			testDirection -= 0.0175;
		}
		return false;
	}
	
	/**
	 * Check whether the worm with the current position can fall.
	 * 
	 * @return	True if and only if the worm is not adjacent to impassable
	 * 			terrain and the worm itself must be on passable terrain.
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
	 * 			| result == 
	 * 			|	{getX() + getInitialVelocity() * Math.cos(this.getWorm().getDirection()) * t,
	 * 			| 	getY() + getInitialVelocity() * Math.sin(this.getWorm().getDirection())*t - 
	 * 			|		(STANDARD_ACCELERATION*Math.pow(t,2))/2.0}
	 */
	public double[] getJumpStep(double t) 
			throws IllegalActionPointsException, IllegalDirectionException
	{
		//max horizontalVelocity/verticalVelocity = 
		//7.5 * 1 = 7.5 (see getinitialVelocity for more explanation)
		double horizontalVelocity = getInitialVelocity() * Math.cos(this.getWorm().getDirection());
		double xPosition = getX() + horizontalVelocity * t;
		double verticalVelocity = getInitialVelocity() * Math.sin(this.getWorm().getDirection());
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
		
		// if 'temp' is smaller than 1/8100 the worm will leave the world because there is no
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
		if(!this.getWorm().getWorld().isAdjacent(tempXY[0], tempXY[1], radius)){
			if(tempTime < Math.PI)
				return Math.PI;
			else
				return 2*Math.PI;
		}
		
		if(Math.pow(Math.pow(tempXY[0] - getX(),2.0) + 
				Math.pow(tempXY[1] - getY(), 2.0), (1.0/2.0))
				< radius){
			throw new IllegalDirectionException(this.getWorm().getDirection(),this.getWorm());
		}
		
		return tempTime;
	}


	
	/**
	 * Change the position of the worm as the result of a jump from the current position 
	 * and with respect to the worm's orientation, his mass, the standard acceleration 
	 * and the number of remaining action points.
	 * 
	 * 
	 * @effect	If the time needed for the jump does not equal pi or
	 * 			two times pi the x-coordinate and y-coordinate are
	 * 			changed as an effect of the jump and checks if any
	 * 			food can be eaten.
	 * 			| if(getJumpTime() != Math.PI && getJumpTime() != 2*Math.PI)
	 * 			| 	then (
	 * 			|		setX(getJumpStep(getJumpTime())[0]) &&
	 * 			| 		setY(getJumpStep(getJumpTime())[1]) &&
	 * 			| 		eatPossibleFood())
	 * 			|	)
	 * @effect	If the time needed for the jump does equal pi or two
	 * 			times pi the worm dies.
	 * 			| if(time == Math.PI || time == 2*Math.PI)
	 * 			| 	then (this.getWorm().wormDeath())
	 * @effect	The new amount of current action points is set to zero.
	 * 			| worm.setCurrentActionPoints(0)
	 */
	public void jump() 
			throws IllegalActionPointsException, IllegalDirectionException, IllegalPositionException
	{
		double time = getJumpTime();
		double[] tempXY = getJumpStep(time);
		if(time != Math.PI && time != 2*Math.PI){
			setPosition(tempXY[0], tempXY[1]);
			eatPossibleFood();
		}
		this.getWorm().setCurrentActionPoints(0);
		if(time == Math.PI || time == 2*Math.PI)
			this.getWorm().wormDeath();
	}
	
	/**
	 * Moves the worm in the current direction of the worm. 
	 */
	//TODO documentation
	public void move()
		throws IllegalDirectionException, IllegalPositionException{
		//find the highest possible distance in a valid direction.
			//use tempDirection to cycle through all the valid directions
			// and store the direction with the highest distance in bestDirection
				//initiate the bestDirection with an invalid direction.
		double bestDirection = -1; 
		double tempDirection = getWorm().getDirection() - 0.7875;
		double tempDistance = -1;
		double bestDistance = -1;
	
		
		//check whether the worm can Move
		if(!this.canMove())
			throw new IllegalDirectionException(this.getWorm().getDirection(),this.getWorm());;
		
		//cycle through the different possible directions.
		while(tempDirection <= getWorm().getDirection() + 0.7875)
		{
			tempDistance = move_newDistance(tempDirection);
			if( tempDistance > bestDistance )
			{
				bestDistance = tempDistance; 
				bestDirection = tempDirection ;
			}
			if(bestDistance == tempDistance && (Math.abs(getWorm().getDirection() - bestDirection) > Math.abs(getWorm().getDirection() - tempDirection)))
				bestDirection = tempDirection;	
							
			tempDirection += 0.0175;
		}
		//throw exception if no proper distance was found.
		if(bestDistance == -1)
			throw new IllegalDirectionException(this.getWorm().getDirection(),this.getWorm());
	
		//find the proper x and y coordinate and move the worm to this place.
		
		setPosition(this.move_CalculateX(bestDirection, bestDistance), 
						this.move_CalculateY(bestDirection, bestDistance));
		this.getWorm().reduceCurrentActionPoints((int) (Math.ceil(Math.abs(Math.cos(bestDirection))
				+ Math.abs(Math.sin(4*bestDirection)))));
		eatPossibleFood();
	}
	
	//TODO moeilijke documentatie aanvullen lusinvarianten...
	public void fall() 
			throws IllegalPositionException{
		if(!canFall())
			throw new IllegalPositionException(getX(), getY());
		double radius = this.getWorm().getRadius();
		double tempY = getY();
		double temp = 0.5;
		while(canFall(getX(),tempY) && temp >= (1.0/500.0)){
			while(inMap(getX(),tempY) && canFall(getX(),tempY))
				tempY -= temp;
			if(!inMap(getX(),tempY))
				tempY += temp;
			temp = temp / 3.0;
			while(!canFall(getX(),tempY) &&
					!getWorm().getWorld().isAdjacent(getX(), tempY, radius) )
				tempY += temp;
			temp = temp / 3.0;
		}
		// if 'temp' is smaller than 1/300 there will be no adjacent position after the fall,
		// the worm will fall of the world and die.
		if(canFall(getX(),tempY)){
			setY(radius + 0.01);
			this.getWorm().wormDeath();
		}
		else{
			if(this.getWorm().getWorld().isStarted())
			this.getWorm().setCurrentHitPoints(this.getWorm().getCurrentHitPoints() - 
					(int)Math.floor(3.0 * (getY() - tempY)));
			setY(tempY);
			eatPossibleFood();
		}
	}
	
	/**
	 * check whether a worm has enough action points to move to the given location.
	 */
	//TODO documentation
	@Model
	private boolean canMove_Aux(double direction)
	{
		if (  (Math.abs(Math.cos(direction)) + Math.abs(Math.sin(direction)*4)  ) >
								this.getWorm().getCurrentActionPoints() )
			return false;
		return true;
	}
	
	/**
	 * Check whether the worm with the given position can fall.
	 * 
	 * @param 	x
	 * 			The x-coordinate of the position to check.
	 * @param 	y
	 * 			The y-coordinate of the position to check.
	 * @return	True if and only if the worm with the given position to check
	 * 			is not adjacent to impassable terrain but the worm itself
	 * 			lies on a passable place.
	 * 			| result == this.getWorm().getWorld().canFall(x, y, this.getWorm().getRadius())
	 */
	@Model
	private boolean canFall(double x, double y){
		return this.getWorm().getWorld().canFall(x, y, this.getWorm().getRadius());
	}
	
	/**
	 * Check if the given position lies fully within the boundaries of the world.
	 * 
	 * @param 	x
	 * 			The x position to check
	 * @param 	y
	 * 			The y position to check
	 * @return	If there is no world where this position can be placed, we assumed
	 * 			that the position is always a valid postion.
	 * 			| if(this.getWorm().getWorld() == null)
	 * 			| 	then (result == true)
	 * 			If there is a world we take the maximum and minimum x and y position 
	 * 			of the worm, the radius taking into account, and check if these
	 * 			coordinates lie fully within the world.
	 * 			| else(
	 * 			|	result == x>this.getWorm().getRadius() && 
	 * 			|		x<this.getWorm().getWorld().getWidth() - this.getWorm().getRadius() &&
	 * 			|		y>this.getWorm().getRadius() && 
	 * 			|		y<this.getWorm().getWorld().getHeight() - this.getWorm().getRadius()
	 * 			| )
	 */
	@Model
	private boolean inMap(double x, double y){
		if(this.getWorm() == null || this.getWorm().getWorld() == null)
			return x>=0 && y>=0;
		double radius = this.getWorm().getRadius();
		return x>radius && x<this.getWorm().getWorld().getWidth() - radius &&
				y>radius && y<this.getWorm().getWorld().getHeight() - radius;
	}
	
	/**
	 * Calculates the proper x-coordinate for a gives direction and distance.
	 * 
	 * @return	| result == this.getX() + (Math.cos(direction)*distance)
	 */
	@Model
	private double move_CalculateX(double direction, double distance)
	{
		return this.getX() + (Math.cos(direction)*distance);
	}
	
	/**
	 * Calculates the proper y-coordinate for a gives direction and distance.
	 * 
	 * @return	| result == this.getY() + (Math.sin(direction)*distance)
	 */
	@Model
	private double move_CalculateY(double direction, double distance)
	{
		return this.getY() + (Math.sin(direction)*distance);
	}
	
	/**
	 * Calculates the next possible distance for the move method in a certain direction.
	 * Returns -2 if there is no possible distance.
	 */
	//TODO documentation
	@Model
	private double move_newDistance(double direction)
	{
		//find the highest possible distance which is in a passable location.
			//initialize the testDistance with the maximum distance
		
			//keep reducing the testDistance by a small amount
			//untill it finds a passable location.
		boolean flag = false;
		double testX = 0, testY = 0;
		double testDistance;
		for(testDistance = this.getWorm().getRadius(); testDistance >= 0.1 && flag == false ; testDistance -= 0.02)
		{
			testX = move_CalculateX(direction, testDistance);
			testY = move_CalculateY(direction, testDistance);
			if(canMove_Aux (direction) && !this.getWorm().getWorld().isImpassable(testX, testY, this.getWorm().getRadius()))
			{
				flag = true;
				return testDistance;
			}
		}
		if(flag == false)
			return -2;
		if(canMove_Aux(direction))
			return testDistance;
		return -2;
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
	 * Lets a worm eat a food ration if the worm hitted any as a result of a move, jump or fall.
	 * 
	 * @effect	If the worm hitted a food ration as a result of a move, jump or fall, the worm
	 * 			will grow in size and the food ration has to be deactivated.
	 * 			| if(this.getWorm().getWorld().hitAnyFood(getX(),getY(),this.getWorm().getRadius()) != null)
	 * 			| 	then(
	 * 			| 	this.getWorm().getWorld().hitAnyFood(getX(),getY(),this.getWorm().getRadius()).deactivate()
	 * 			|	&& this.getWorm().growInRadius()
	 * 			|	)	
	 */
	@Model
	private void eatPossibleFood(){
		if(this.getWorm().getWorld().hitAnyFood(getX(),getY(),this.getWorm().getRadius()) != null){
			Food food = this.getWorm().getWorld().hitAnyFood(getX(),getY(),this.getWorm().getRadius());
			food.deactivate();
			this.getWorm().growInRadius();
		}
	}
	
	
	
	
	/**
	 * Final class variable registering the standard acceleration (m/(s*s)).
	 */
	private final static double STANDARD_ACCELERATION = 9.80665;



	/**
	 * Check if the given position is a valid position for any worm.
	 * 
	 * @return	The worm must lie fully within the borders of the world.
	 * 			| result == inMap(x, y)
	 */
	@Override @Model
	protected boolean isValidPosition(double x, double y) {
		return inMap(x, y);
	}
}
