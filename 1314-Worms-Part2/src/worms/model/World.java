package worms.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of worlds involving a width, a height, a passable map,
 * food rations, worms, teams and projectiles.
 * 
 * @invar	| canHaveAsWidthOrHeight(getWidth())
 * @invar	| canHaveAsWidthOrHeight(getHeight())
 * @invar	| hasProperTeams()
 * @invar	| hasProperFoodRations()
 * @invar	| hasProperWorms()
 * @invar	| hasProperProjectile()
 * 
 * @version 1.0
 * @author 	Laurens Loots, Pieter Vos
 */
//TODO check class invariants
public class World {
	
	/**
	 * Creates a new world.
	 * 
	 * @param 	width 
	 * 			The width of this new world (in meter)
	 * @param 	height 
	 * 			The height of this new world (in meter)
	 * @param 	passableMap A 
	 * 			Rectangular matrix indicating which parts of the terrain are passable and impassable.
	 *  		This matrix is derived from the transparency of the pixels in the image file of the terrain.
	 *  		passableMap[r][c] is true if the location at row r and column c is passable, and false if that location is impassable.
	 *  		The elements in the first row (row 0) represent the pixels at the top of the terrain (i.e., largest y-coordinates).
	 *  		The elements in the last row (row passableMap.length-1) represent pixels at the bottom of the terrain (smallest y-coordinates).
	 *  		The elements in the first column (column 0) represent the pixels at the left of the terrain (i.e., smallest x-coordinates).
	 *  		The elements in the last column (column passableMap[0].length-1) represent the pixels at the right of the terrain (i.e., largest x-coordinates).     
	 * @param 	random A 
	 * 			Random number generator, seeded with the value obtained from the command line or from GUIOptions,
	 *  		that can be used to randomize aspects of the world in a repeatable way.
	 * @post	| new.getwidth = width
	 * @post	| new.getheight = height
	 * @post	| new.getpassableMap = passableMap
	 * @throws	IllegalArgumentException("The given width or height is not valid.")
	 * 			| !canHaveAsWidthOrHeight(width) || !canHaveAsWidthOrHeight(height)
	 *	
	 */
	public World(double width, double height,
			boolean[][] passableMap, Random random){
		if(!canHaveAsWidthOrHeight(width) || !canHaveAsWidthOrHeight(height))
			throw new IllegalArgumentException("The given width or height is invalid.");
		this.width = width;
		this.height = height;
		this.passableMap = passableMap;
		this.random = random;
	}
	
	
	
	/**
	 * Deactivate this world.
	 * 
	 * @post	| ( !new.isActive() )
	 * @post	| for each food in Food
	 * 			|	( if (this.hasAsFood(food))
	 * 			|		then ( !(new food).isActive() ) )
	 * @post	| for each food in Food
	 * 			|	(!this.hasAsFood(food))
	 */
	public void deactivate(){
		for(Food food: this.foodRations){
			food.deactivate();
		}
		this.isActive = false;
	}
	
	/**
	 * Returns whether or not this world is active.
	 */
	@Basic @Raw
	public boolean isActive(){
		return this.isActive;
	}
	
	/**
	 * Variable registering whether or not this world is active.
	 */
	private boolean isActive = true;
	
	
	/**
	 * Returns whether or not the game is started.
	 */
	@Basic @Raw
	public boolean isStarted(){
		return this.isStarted;
	}
	
	/**
	 * Starts a game in the given world.
	 */
	public void startGame(){
		if(worms.size() > 1){
			startNextTurn();
			start(true);
		}
	}
	
	/**
	 * Start or stop the game.
	 * 
	 * @param	started
	 * 			A boolean indicating whether to start or to stop the game.
	 * @post	| new.isStarted() == started
	 */
	@Model @Raw
	private void start(boolean started){
		this.isStarted = started;
	}
	
	/**
	 * Variable registering whether or not the game is started.
	 */
	private boolean isStarted = false;
	
	
	
	/**
	 * 
	 * @param 	x
	 * 			The width or height to check.
	 * @return	| 0 <= x && x <= UPPER_BOUND
	 */
	public boolean canHaveAsWidthOrHeight(double x){
		return 0 <= x && x <= UPPER_BOUND;
	}
	
	/**
	 * Return the width of this world.
	 */
	public double getWidth(){
		return this.width;
	}
	
	/**
	 * returns a new X value.
	 * 
	 * @param 	oldX
	 * 			the old X value on which the new one is based.
	 * @return	returns the new X value.
	 * 			| if(oldx > getWidth()/2)
	 * 			|		then ( return oldX - 0.014)
	 * 			| if(oldX < getWidth()/2 )
	 * 			| 		then ( return oldX + 0.014)
	 */
	private double newX(double oldX)
	{
		if(oldX > this.getWidth()/2)
			return oldX - 0.014;
		if(oldX < this.getWidth()/2)
			return oldX + 0.014;
	
		return oldX;
	}
	/**
	 * returns a random integer between 0 and the width of the world.
	 * 
	 * @return	|random.nextInt((int)getWidth())
	 */
	private int randomStartX()
	{
		return random.nextInt((int)this.getWidth());		
	}
	
	/**
	 * Variable registering the width of this world.
	 */
	private final double width;
	
	
	
	/**
	 * Return the height of this world.
	 */
	@Basic  @Raw
	public double getHeight(){
		return this.height;
	}
	
	/**
	 * returns a new Y value.
	 * 
	 * @param 	oldY
	 * 			the old Y value on which the new one is based.
	 * @return	returns the new Y value.
	 * 			| if(oldY > getHeight()/2)
	 * 			|		then ( return oldY - 0.014)
	 * 			| if(oldY < getHeight()/2 )
	 * 			| 		then ( return oldY + 0.014)
	 */
	private double newY(double oldY)
	{
		if(oldY > this.getHeight()/2)
			return oldY - 0.014;
		if(oldY < this.getHeight()/2)
			return oldY + 0.014;

		return oldY;
	}
	/**
	 * returns a random integer between 0 and the height of the world.
	 * 
	 * @return	|random.nextInt((int)getHeight())
	 */
	private int randomStartY()
	{
		return random.nextInt((int)this.getHeight());
	}
	
	/**
	 * Variable registering the height of this world.
	 */
	private final double height;
	
	
	
	/**
	 * Return the passable map of this world.
	 */
	@Basic @Raw
	public boolean[][] getPassableMap(){
		return passableMap;
	}
	
	/**
	 * Checks whether the given circular region of the given world,
	 * defined by the given center coordinates and radius,
	 * is impassable. 
	 * 
	 * @param 	x 
	 * 			The x-coordinate of the center of the circle to check  
	 * @param 	y 
	 * 			The y-coordinate of the center of the circle to check
	 * @param 	radius 
	 * 			The radius of the circle to check
	 * @return 	True if the given region is impassable, false otherwise.
	 * 			|if(isImpassable(x,y,radius)
	 * 			|	then (return true)
	 * 			|else
	 * 			|	return false;
	 */
	
	//each pixel of an image that is x pixels wide and y pixels high, shall be used to mark a rectangular area
	// of width/x x height/y of the game world as either passable or impassable
	@Raw
	public boolean isImpassable(double x, double y, double radius){
		if(x-radius<0 || x+radius>getWidth() || y-radius<0 || y+radius>getHeight() || radius <= 0)
			return true;
		for(double angle = 0;angle<2*Math.PI;angle+=(Math.PI/180)){
			if (isImpassablePoint(x+(Math.cos(angle)*radius*0.99999999), y+(Math.sin(angle))*radius*0.99999999))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the given circular region of the given world,
	 * defined by the given center coordinates and radius,
	 * is passable and adjacent to impassable terrain. 
	 * 
	 * @param 	x 
	 * 			The x-coordinate of the center of the circle to check  
	 * @param 	y 
	 * 			The y-coordinate of the center of the circle to check
	 * @param 	radius 
	 * 			The radius of the circle to check
	 * 
	 * @return 	True if the given region is passable and adjacent to impassable terrain, false otherwise.
	 */
	@Raw
	public boolean isAdjacent(double x, double y, double radius){
		if(isImpassable(x,y,radius))
			return false;
		else
		{
			for(double angle = 0; angle < 2*Math.PI ; angle = angle + (Math.PI/180))
			{
				if (isImpassablePoint(x+(Math.cos(angle)*radius*1.1), y+(Math.sin(angle))*radius*1.1))
					return true;
			}
			return false;
		}
	}
	
	
	/**
	 * returns if the worm can fall or not.
	 * 
	 * @param 	x
	 * 			the given x position.
	 * @param 	y
	 * 			the given y position.
	 * @param 	radius
	 * 			the given radius.
	 * @return	|if(if(isImpassable(x,y,radius) || isAdjacent(x,y,radius))
				|	return false
				|else
				|	return true
	 */
	public boolean canFall(double x, double y, double radius){
		if(isImpassable(x,y,radius) || isAdjacent(x,y,radius))
			return false;
		return true;
	}

	
	/**
	 * calculates the right positions int he passable map and
	 * returns whether the point is impassable or not.
	 * 
	 * @param	x
	 * 			the given x value
	 * @param 	y
	 * 			the given y value
	 * @return	|!getPassableMap()[intX][intY]
	 */
	@Raw
	private boolean isImpassablePoint(double x, double y){
		int intX, intY;
		double temp = y*getPassableMap().length/getHeight();
		intX = getPassableMap().length - (int)Math.ceil(temp);
		temp = x*getPassableMap()[0].length/getWidth();
		intY = (int)Math.floor(temp);
		if(intX >= getPassableMap().length)
			intX = getPassableMap().length - 1;
		if(intY >= getPassableMap()[0].length)
			intY = getPassableMap()[0].length - 1;
		return !getPassableMap()[intX][intY];
	}
	
	/**
	 * Variable referencing a rectangular matrix indicating which parts 
	 * of the terrain are passable and impassable (true or false).
	 */
	private final boolean[][] passableMap;
	

	
	/**
	 * Variable registering the upper bound for all worlds.
	 */
	private static final double UPPER_BOUND = Double.MAX_VALUE; 
	
	

	
	//start
	

	

	
	/**
	 * Returns whether the game in the given world has finished.
	 * 
	 * @return	| if (worms.size() <= 1)
	 *			|	then(result == true);
	 *			| else if(teams.size() == 1)
	 *			|	then(	for each worm in worms:
	 *			|				(if(worm.getTeam() == null)
	 *			|					then(result == false))
	 *			| 			else result == true)
	 *			| else (result == false)
	*/
	public boolean isGameFinished(){
		if (worms.size() <= 1)
			return true;
		if(teams.size() == 1){
			for(Worm worm: worms){
				if(worm.getTeam() == null)
					return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the name of a single worm if that worm is the winner, or the name
	 * of a team if that team is the winner. This method returns null if there is no winner.
	 * 
	 * @return	| for each worm in worms:
	 * 			|	(if(worm.getTeam() == null)
	 * 			|		then (result == worm.getName())
	 * 			|		else (result == worm.getTeamName())
	 * 			|	)
	 * 			| else (result == null)
	 */
	public String getWinner(){
		for(Worm worm: worms){
			if(worm.getTeam() == null)
				return worm.getName();
			else
				return worm.getTeam().getTeamName();
		}
		return null;
	}
	
	
	

	

	
	
	
	/**
	 * Check whether this world can have the given team 
	 * as one of its teams.
	 * 
	 * @param 	team
	 * 			The team to check.
	 * @return	| for each existingTeam in teams(
	 * 			|	if( existingTeam.getTeamName().equals( team.getTeamName() ))
	 * 			|		then( result == false ) )
	 * 			| if (team == null)
	 * 			|	then result == false
	 * 			| else result ==
	 * 			| 	( team.isActive() && this.isActive() )
	 */
	@Raw
	public boolean canHaveAsTeam(Team team){
		for(Team existingTeam: teams){
			if(existingTeam.getTeamName().equals( team.getTeamName() ))
				return false;
		}
		return team != null && team.isActive() && this.isActive();
	}
	
	/**
	 * Check whether this world has proper teams attached to it.
	 * 
	 * @return	| if(this.teams.size() > 10)
	 * 			|	then( result == false)
	 * 			| else(
	 * 			| 	result ==
	 * 			| 	for each team in Team
	 * 			| 	( 
	 * 			|		if (this.hasAsTeam(team))
	 * 			|			then( canHaveAsTeam(team)
	 * 			|				&& (team.getWorld() == this))
	 *			|	)	
	 * 			| )
	 */
	@Raw
	public boolean hasProperTeams(){
		if(this.teams.size() > 10)
			return false;
		for(Team team: this.teams){
			if(!canHaveAsTeam(team) || team.getWorld() != this)
				return false;
		}
		return true;
	}
	
	/**
	 * Check whether this world has the given team as
	 * one of the teams attached to it.
	 * 
	 * @param 	team
	 * 			The team to check.
	 * @Return	| result == this.teams.contains(team)
	 */
	public boolean hasAsTeam(Team team){
		return this.teams.contains(team);
	}
	
	/**
	 * Create and add an empty team with the given name to the given world.
	 * 
	 * @param 	newName
	 * 			The new name for the new empty team.
	 * @effect	| addAsTeam(new Team(newName))
	 */
	public void addEmptyTeam(String newName) 
			throws IllegalNameException, IllegalArgumentException{
		addAsTeam(new Team(newName));
	}
	
	/**
	 * Add the given team to the set of teams attached to this world.
	 * 
	 * @param 	team
	 * 			The team to be added.
	 * @post	| new.hasAsTeam(team)
	 * @post	| (new team).getWorld() == this
	 * @throws 	IllegalArgumentException("You can't add this team.")
	 * 			| (!canHaveAsTeam(team))
	 * 			| || ( (team != null) && (team.getWorld() != null) )
	 * 			| || (this.teams.size() == 10)
	 */
	public void addAsTeam(Team team) 
			throws IllegalArgumentException{
		if(!canHaveAsTeam(team))
			throw new IllegalArgumentException("You can't add this team.");
		if(team.getWorld() != null)
			throw new IllegalArgumentException("You can't add this team.");
		if(this.teams.size() == 10)
			throw new IllegalArgumentException("You can't add this team.");
		this.teams.add(team);
		team.setWorld(this);
	}
	
	/**
	 * Remove the given team from the set of teams
	 * attached to this world.
	 * 
	 * @param 	team
	 * 			The team to be removed
	 * @post	| !new.hasAsTeam(team)
	 * @post	| if (hasAsTeam(team))
	 * 			|	((new team).getWorld() == null)
	 * @throws	IllegalArgumentException()
	 * 			| !hasAsTeam(team)
	 */
	public void removeAsTeam(Team team){
		if(!hasAsTeam(team))
			throw new IllegalArgumentException();
		this.teams.remove(team);
		team.setWorld(null);
	}
	
	/**
	 * A set collecting references to teams attached to this world.
	 * 
	 * @invar	| teams != null
	 * @invar	| for each team in teams(
	 * 			|	canHaveAsTeam(team))
	 * @invar	| for each team in teams(
	 * 			|	(team.getWorld() == this))
	 * @invar	| teams.size() <= 10
	 */
	private HashSet<Team> teams = new HashSet<Team>();

	
	
	/**
	 * returns the food object if it in within the given radius on the given position.
	 * @param 	x
	 * 			the given x-position
	 * @param 	y
	 * 			the given y-position
	 * @param 	radius
	 * 			the given radius
	 * @return	|if(isImpassable(x,y,radius))
	 *			|	return null;
	 *			|for(Food food: foodRations){
	 *			|		if(Math.pow(Math.pow((food.getX() - x), 2.0) + 
	 *			|			Math.pow((food.getY() - y), 2.0),(1.0/2.0)) <= 
	 *			|				(Food.getRadius() + radius)){
	 *			|			return food;
	 *			|		}
	 *			|	}
	 *			|return null;
	 * 
	 */
	public Food hitAnyFood(double x, double y, double radius){
		if(isImpassable(x,y,radius))
			return null;
		for(Food food: foodRations){
			if(Math.pow(Math.pow((food.getX() - x), 2.0) + 
				Math.pow((food.getY() - y), 2.0),(1.0/2.0)) <= 
					(Food.getRadius() + radius)){
				return food;
			}
		}
		return null;
	}
	
	/**
	 * Check whether this world can have the given food 
	 * as one of its food rations.
	 * 
	 * @param 	food
	 * 			The food to check.
	 * @return	| if (food == null)
	 * 			|	then result == false
	 * 			| else result ==
	 * 			| 	( food.isActive() && this.isActive() )
	 */
	@Raw
	public boolean canHaveAsFood(Food food){
		return food != null && food.isActive() && this.isActive();
	}
	
	/**
	 * Check whether this world has proper food rations attached to it.
	 * 
	 * @return	| result ==
	 * 			| 	for each food in Food
	 * 			|		( if (this.hasAsFood(food))
	 * 			|			then canHaveAsFood(food)
	 * 			|				&& (food.getWorld() == this) )
	 */
	@Raw
	public boolean hasProperFoodRations(){
		for(Food food: this.foodRations){
			if(!canHaveAsFood(food) || food.getWorld() != this)
				return false;
		}
		return true;
	}
	
	/**
	 * Check whether this world has the given food as
	 * one of the food rations attached to it.
	 * 
	 * @param 	food
	 * 			The food to check.
	 * @Return	| result == this.foodRations.contains(food)
	 */
	@Basic @Raw
	public boolean hasAsFood(Food food){
		return this.foodRations.contains(food);
	}
	
	/**
	 * Returns all the food rations in this world.
	 * 
	 * @return	| ! result.contains(null)
	 * @return	| for each foodRations in Food
	 * 			| 	(result.contains(foodRations) ==
	 * 			|		this.hasAsFoodRations(foodRations))
	 */
	public Collection<Food> getFood(){
		return new HashSet<Food>(this.foodRations);
	}
	
	/**
	 * Create and add a new food ration to the given world.
	 * The food must be placed at a random adjacent location.
	 * 
	 * 
	 */
	//TODO moeilijker docu
	public void addNewFood() 
			throws IllegalArgumentException{
		//find a location for the food
				//determine at which wall we will start searching for a proper place
				// 0: left wall  1: right wall,  2: bottom wall,  3: top wall
				double getal = random.nextInt(4);
				double testX = 0, testY = 0;
				if(getal == 0){
					testX = 0;
					testY = randomStartY();
				}
				if(getal == 1){
					testX = this.getWidth();
					testY = randomStartY();
				}
				if(getal == 2){
					testX = randomStartX();
					testY = 0;
				}
				if(getal == 3){
					testX = randomStartX();
					testY = this.getHeight();
				}
				//determine the exact location by constantly checking a place, and going closer to the middle
				// as suggested in the assignment.

				while (! isAdjacent(testX, testY, 0.20 ))
				{
					testX = newX(testX);
					testY = newY(testY);
				}
				Food newFood = new Food(testX, testY);
				this.addAsFood(newFood);
	}
	
	/**
	 * Add the given food to the set of food rations attached to this world.
	 * 
	 * @param 	food
	 * 			The food to be added.
	 * @post	| new.hasAsFood(food)
	 * @post	| (new food).getWorld() == this
	 * @throws 	IllegalArgumentException("You can't add this food.")
	 * 			| !canHaveAsFood(food)
	 * @throws 	IllegalArgumentException("You can't add this food.")
	 * 			| ( (food != null)
	 * 			| && (food.getWorld() != null) )
	 */
	public void addAsFood(Food food) 
			throws IllegalArgumentException{
		if(!canHaveAsFood(food))
			throw new IllegalArgumentException("You can't add this food.");
		if(food.getWorld() != null)
			throw new IllegalArgumentException("You can't add this food.");
		this.foodRations.add(food);
		food.setWorld(this);
	}
	
	/**
	 * Remove the given food from the set of food rations
	 * attached to this world.
	 * 
	 * @param 	food
	 * 			The food to be removed
	 * @post	| !new.hasAsFood(food)
	 * @post	| if (hasAsFood(food))
	 * 			|	((new food).getWorld() == null)
	 * @throws	IllegalArgumentException()
	 * 			| !hasAsFood(food)
	 */
	public void removeAsFood(Food food) 
			throws IllegalArgumentException{
		if(!hasAsFood(food))
			throw new IllegalArgumentException();
		this.foodRations.remove(food);
		food.setWorld(null);
	}
	
	
	/**
	 * A set collecting references to food rations attached to this world.
	 * 
	 * @invar	| foodRations != null
	 * @invar	| for each foodRations in foodRations
	 * 			|	canHaveAsFood(foodRations)
	 * @invar	| for each foodRations in foodRations
	 * 			|	(foodRations.getWorld() == this)
	 */
	private HashSet<Food> foodRations = new HashSet<Food>();
	//FOOD
	
	
	/**
	 * Returns the active worm in the given world (i.e., the worm whose turn it is).
	 */
	@Basic @Raw
	public Worm getCurrentWorm(){
		return this.currentWorm;
	}
	
	/**
	 * Starts the next turn in the given world by finding the current worm in the linkedHashSet
	 * and selecting the next worm as new current Worm
	 */
	//vorige worm checken, zoeken in linkedHashSet, en de volgende in de set nemen als current worm.
	//TODO documentation
	public void startNextTurn(){
		Iterator<Worm> it = worms.iterator();
		if (this.currentWorm == null)
		{
			currentWorm = it.next();
		}
		else
		{
			String lastWormName = currentWorm.getName();
			boolean flag = false;
			Worm testWorm = null;
			while(!flag)
			{
				testWorm = it.next();
				if(testWorm.getName().equals(lastWormName))
					flag = true;
								
			}
			
			if(it.hasNext())
				currentWorm = it.next();
			else
			{
				Iterator<Worm> it2 = worms.iterator();
				currentWorm = it2.next();		
			}
			while(currentWorm == null&& it.hasNext())
			{
				currentWorm = it.next();
			}
			if(currentWorm == null)
			{
				Iterator<Worm> it3 = worms.iterator();
				while(currentWorm == null &&it3.hasNext())
				{
					currentWorm = it3.next();
				}
			}
		}
		
		this.startNextTurn_Aux(currentWorm);
	}
	
	/**
	 * Resets the worms action points and gives it 10 extra hp if possible.
	 * 
	 * @param 	worm
	 * 			The given worm.
	 * 
	 * @post	sets the worms hitpoints to its current hit points incremented with 10
	 * 			or to maximum if and only if its current hitpoints incremented with 10
	 * 			is larger than its maximum hitPoints.
	 * 			| if (worm.getMaxHitPoints() - worm.getCurrentHitPoints() < 10)
	 * 			|	then new.getWorm().getCurrentHitpoints == getWorm().getMaxHitPoints)
	 * 			| else
	 * 			|	new.getWorm().getCurrentHitpoints == this.getWorm().getCurrentHitPoints + 10
	 * @post	sets the worms actionpoints back to the maximum.
	 * 			|new.getWorm().getCurrentActionPoints == this.getWorm().getMaxActionPoints
	 */
	private void startNextTurn_Aux(Worm worm)
	{
		if (worm.getMaxHitPoints() - worm.getCurrentHitPoints() < 10)
			worm.setCurrentHitPoints(worm.getMaxHitPoints());
		else{
			worm.setCurrentHitPoints(worm.getCurrentHitPoints() + 10);
		}
		
		worm.setCurrentActionPoints(worm.getMaxActionPoints());
	}
	
	private Worm currentWorm = null;
	
	
	/**
	 * returns the worm object if it is in within the given radius on the given position.
	 * @param 	x
	 * 			the given x-position
	 * @param 	y
	 * 			the given y-position
	 * @param 	radius
	 * 			the given radius
	 * @return	|if(isImpassable(x,y,radius))
	 *			|	return null;
	 *			|for(Worm worm: worms){
	 *			|		if(Math.pow(Math.pow((worm.getX() - x), 2.0) + 
	 *			|			Math.pow((worm.getY() - y), 2.0),(1.0/2.0)) <= 
	 *			|				(worm.getRadius() + radius)){
	 *			|			return worm;
	 *			|		}
	 *			|	}
	 *			|return null;
	 * 
	 */
	public Worm hitAnyWorm(double x, double y, double radius){
		if(isImpassable(x,y,radius))
			return null;
		for(Worm worm: worms){
			if(Math.pow(Math.pow((worm.getWormPosition().getX() - x), 2) + 
				Math.pow((worm.getWormPosition().getY() - y), 2),(1/2.0)) <= 
					(worm.getRadius() + radius)){
				return worm;
			}
		}
		return null;
	}
	
	/**
	 * Returns whether or not this world can have the given worm 
	 * as one of its worms or not.
	 * 
	 * @param 	worm
	 * 			the given worm.
	 * @return	|(worm != null) && worm.isAlive()
	 */
	public boolean canHaveAsWorm(Worm worm){
		return (worm != null) && worm.isAlive();
	}
	
	/**
	 * Check whether this world has proper worms attached to it.
	 * 
	 * @return	| result ==
	 * 			|	for each worm in Worm:
	 * 			|		( if (this.hasAsWorm(worm))
	 * 			|			then (canHaveAsWorm(worm)
	 * 			|				&& (worm.getWorld() == this)) )
	 */
	public boolean hasProperWorms(){
		for(Worm worm: this.worms){
			if(!canHaveAsWorm(worm) || worm.getWorld() != this)
				return false;
		}
		return true;
	}
	
	/**
	 * check whether this world has the given worm as one
	 * of its worms attached to it.
	 * 
	 * @param 	worm
	 * 			the worm to check
	 * @return	| result == this.worms.contains(worm)
	 */
	@Basic  @Raw
	public boolean hasAsWorm(Worm worm){
		return this.worms.contains(worm);
	}
	
	/**
	 * Returns all the worms in the given world
	 */
	public Collection<Worm> getWorms(){
		return new LinkedHashSet<Worm>(this.worms);
	}
	
	
	
	
	/**
	 * Create and add a new worm to the given world.
	 * 
	 * 
	 */
	//TODO moeilijke docu
	public void addNewWorm() 
			throws IllegalArgumentException{
		//find a location for the worm
		//determine at which wall we will start searching for a proper place
		// 0: left wall  1: right wall,  2: bottom wall,  3: top wall
		double getal = random.nextInt(4);
		double testX = 0, testY = 0;
		if(getal == 0){
			testX = 0;
			testY = randomStartY();
		}
		if(getal == 1){
			testX = this.getWidth();
			testY = randomStartY();
		}
		if(getal == 2){
			testX = randomStartX();
			testY = 0;
		}
		if(getal == 3){
			testX = randomStartX();
			testY = this.getHeight();
		}
		//determine the exact location by constantly checking a place, and going closer to the middle
		// as suggested in the assignment.

		while (! isAdjacent(testX, testY, 0.25 ))
		{
			testX = newX(testX);
			testY = newY(testY);
		}
		int grootte = worms.size() + 1;
		String name = "Not Yet Named " + grootte;
				
		Worm newWorm = new Worm(testX, testY, 0, 0.25, name);
		this.addAsWorm(newWorm);
		
		if(teams.size() > 1){
			Team smallestTeam = null;
			int smallestSize = Integer.MAX_VALUE;
			for(Team team : teams){
				int size = team.getSizeOfTeam();
				if(size < smallestSize){
					smallestSize = size;
					smallestTeam = team;
				}	
			}
			smallestTeam.addAsTeamWorm(newWorm);
		}
	}
	

	
	/**
	 * add the given worm to the set of worms attached to this world.
	 * 
	 * @param 	worm	
	 * 			the worm to be added
	 * @post	this world has the given worm as one of its worms
	 * 			| new.hasAsWorm(worm)
	 * @post 	the given worm references this world as the world is is attached to.
	 * 			| (new.worm).getWorld() == this
	 * @throws	IllegalArgumentException
	 * 			this world cannot have the given worm as one of its worms.
	 * 			|!canHaveAsWorm(worm)
	 * @throws	IllegalArgumentException
	 * 			The given worm is already attached to a world.
	 * 			|( (worm != null) 
	 * 			| && (worm.getWorld() != null) )
	 */
	public void addAsWorm(Worm worm)
		throws IllegalArgumentException
	{
		if(! canHaveAsWorm(worm))
			throw new IllegalArgumentException();
		if(worm.getWorld() != null)
			throw new IllegalArgumentException();
		this.worms.add(worm);
		worm.setWorld(this);
	}
	
	/**
	 * remove the given worm from the set of worms attached to this world
	 * 
	 * @param 	worm
	 * 			the worm to be removed
	 * @post	this world does not have the given worm as one of its worms
	 * 			| !new.hasAsWorm(worm)
	 * @post 	if this world has the given worm as one of its worms,
	 * 			the given worm is no longer attached to any world.
	 * 			|if (hasAsWorm(worm))
	 * 			|	((new worm).getWorld() == null
	 * @throws	IllegalArgumentException()
	 * 			| !hasAsWorm(worm)
	 */
	public void removeAsWorm(Worm worm) 
			throws IllegalArgumentException{
		if(!hasAsWorm(worm))
			throw new IllegalArgumentException();
		this.worms.remove(worm);
		worm.setWorld(null);
	}
	
	/**
	 * Set collecting references to worms attached to the world.
	 * 
	 * @invar	each element in the set of worms references
	 * 			a worm that is a acceptable worm for this world.
	 * 			| for each worm in worms:
	 * 			| 	canHaveAsWorm(worm)
	 * @invar	Each worm in this set of worms references this world
	 * 			as the world to which it is attached.
	 * 			| for each worm in worms:
	 * 			| 	(worm.getWorld() == this)
	 */
	private LinkedHashSet<Worm> worms = new LinkedHashSet<Worm>();

	
	//WORM
	
	/**
	 * Check whether this world can have the given projectile
	 * as its projectile.
	 * 
	 * @param 	projectile
	 * 			The projectile to check.
	 * @return	| result == ( (projectile == null) ||
	 * 			|					(projectile.isActive()) )
	 */
	public boolean canHaveAsProjectile(Projectile projectile){
		return projectile == null || projectile.isActive();
	}
	
	/**
	 * Check whether this world has a proper projectile attached to it.
	 * 
	 * @return	| result == canHaveAsprojectile(this.projectile) && 
	 * 			|				(projectile == null || this.projectile.getWorld() == this)
	 */
	public boolean hasProperProjectile(){
		return canHaveAsProjectile(this.projectile) && 
				(projectile == null || this.projectile.getWorld() == this);
	}
	
	/**
	 * Returns the active projectile in the world, or null if no active projectile exists.
	 */
	public Projectile getProjectile(){
		return this.projectile;
	}
	
	/**
	 * Sets the given projectile as the new active projectile in this world.
	 * 
	 * @pre		| if(projectile != null)
	 * 			|  	then (projectile.getWorld() == this)
	 * @pre		| if(projectile == null && getProjectile() != null)
	 * 			|	then !(getProjectile().getWorld() == this)
	 * @post	| new.projectile == projectile
	 * @throws	IllegalArgumentException("Invalid projectile")
	 * 			| !canHaveAsProjectile(projectile)
	 */
	public void setProjectile(Projectile projectile) 
			throws IllegalArgumentException{
		assert(projectile == null || projectile.getWorld() == this);
		assert(projectile != null || getProjectile() == null || !(getProjectile().getWorld() == this));
		if(!canHaveAsProjectile(projectile))
			throw new IllegalArgumentException("Invalid projectile");
		this.projectile = projectile;
	}
	
	private Projectile projectile = null;
	//PROJECTILE
	/**
	 * return the random in this world
	 */
	@Basic @Raw
	public Random getRandom(){
		return random;
	}
	private Random random = null;
	//RANDOM

	
}
