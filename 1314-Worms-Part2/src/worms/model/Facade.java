package worms.model;

import java.util.Collection;
import java.util.Random;

public class Facade implements IFacade {

	@Override
	public void addEmptyTeam(World world, String newName) {
		world.addEmptyTeam(newName);
	}

	@Override
	public void addNewFood(World world) 
			throws ModelException{
		try{
			world.addNewFood();
		}
		catch(IllegalArgumentException x){
			throw new ModelException("IllegalArgumentException");
		}
	}

	@Override
	public void addNewWorm(World world) 
			throws ModelException{
		try{
			world.addNewWorm();
		}
		catch(IllegalArgumentException x){
			throw new ModelException("IllegalArgumentException");
		}
	}

	@Override
	public boolean canFall(Worm worm) {
		return worm.getPosition().canFall();
	}

	@Override
	public boolean canMove(Worm worm) {
		//TODO implement...
		return false;
	}

	@Override
	public boolean canTurn(Worm worm, double angle) {
		return worm.canTurn(angle);
	}

	@Override
	public Food createFood(World world, double x, double y) 
			throws ModelException{
		try{
			Food food = new Food(x,y);
			world.addAsFood(food);
			return food;
		}
		catch(IllegalArgumentException z){
			throw new ModelException("IllegalArgumentException");
		}
	}

	@Override
	public World createWorld(double width, double height,
			boolean[][] passableMap, Random random) 
					throws ModelException{
		try{
			World world = new World(width, height, passableMap, random);
			return world;
		}
		catch(IllegalArgumentException x){
			throw new ModelException("IllegalArgumentException");
		}
	}

	@Override
	public Worm createWorm(World world, double x, double y, double direction,
			double radius, String name) throws ModelException{
		try{
			Worm worm = new Worm(x,y,direction,radius,name);
			world.addAsWorm(worm);
			return worm;
		}
		catch(IllegalRadiusException ex){
			throw new ModelException("IllegalRadiusException");
		}
		catch(IllegalNameException ex){
			throw new ModelException("IllegalNameException");
		}
	}

	@Override
	public void fall(Worm worm) 
			throws ModelException{
		try{
			worm.getPosition().fall();
		}
		catch(RuntimeException ex){
			throw new ModelException("RuntimeException");
		}
		
	}

	@Override
	public int getActionPoints(Worm worm) {
		return worm.getCurrentActionPoints();
	}

	@Override
	public Projectile getActiveProjectile(World world) {
		return world.getProjectile();
	}

	@Override
	public Worm getCurrentWorm(World world) {
		return world.getCurrentWorm();
	}

	@Override
	public Collection<Food> getFood(World world) {
		return world.getFood();
	}

	@Override
	public int getHitPoints(Worm worm) {
		return worm.getCurrentHitPoints();
	}

	@Override
	public double[] getJumpStep(Projectile projectile, double t) {
		return projectile.getJumpStep(t);
	}

	@Override
	public double[] getJumpStep(Worm worm, double t) 
			throws ModelException{
		try{
			return worm.getPosition().getJumpStep(t);
		}
		catch(IllegalActionPointsException x){
			throw new ModelException("IllegalActionPointsException");
		}
		catch(IllegalDirectionException x){
			throw new ModelException("IllegalDirectionException");
		}
	}

	@Override
	public double getJumpTime(Projectile projectile, double timeStep) {
		try{
			return projectile.getJumpTime(timeStep);
		}
		catch(NullPointerException exc){
			exc.printStackTrace(System.out);
			throw new ModelException("NullPointerException");
		}
	}

	@Override
	public double getJumpTime(Worm worm, double timeStep) 
			throws ModelException{
		try{
			return worm.getPosition().getJumpTime();
		}
		catch(IllegalActionPointsException x){
			throw new ModelException("IllegalActionPointsException");
		}
		catch(IllegalDirectionException x){
			throw new ModelException("IllegalDirectionException");
		}
	}

	@Override
	public double getMass(Worm worm) {
		return worm.getMass();
	}

	@Override
	public int getMaxActionPoints(Worm worm) {
		return worm.getMaxActionPoints();
	}

	@Override
	public int getMaxHitPoints(Worm worm) {
		return worm.getMaxHitPoints();
	}

	@Override
	public double getMinimalRadius(Worm worm) {
		return worm.getMinimalRadius();
	}

	@Override
	public String getName(Worm worm) {
		return worm.getName();
	}

	@Override
	public double getOrientation(Worm worm) {
		return worm.getDirection();
	}

	@Override
	public double getRadius(Food food) {
		return Food.getRadius();
	}

	@Override
	public double getRadius(Projectile projectile) {
		return projectile.getRadius();
	}

	@Override
	public double getRadius(Worm worm) {
		return worm.getRadius();
	}

	@Override
	public String getSelectedWeapon(Worm worm) {
		return worm.getWeapon().getCurrentWeapon();
	}

	@Override
	public String getTeamName(Worm worm) {
		return worm.getTeamName();
	}

	@Override
	public String getWinner(World world) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Worm> getWorms(World world) {
		return world.getWorms();
	}

	@Override
	public double getX(Food food) {
		return food.getX();
	}

	@Override
	public double getX(Projectile projectile) {
		return projectile.getX();
	}

	@Override
	public double getX(Worm worm) {
		return worm.getPosition().getX();
	}

	@Override
	public double getY(Food food) {
		return food.getY();
	}

	@Override
	public double getY(Projectile projectile) {
		return projectile.getY();
	}

	@Override
	public double getY(Worm worm) {
		return worm.getPosition().getY();
	}

	@Override
	public boolean isActive(Food food) {
		return food.isActive();
	}

	@Override
	public boolean isActive(Projectile projectile) {
		return projectile.isActive();
	}

	@Override
	public boolean isAdjacent(World world, double x, double y, double radius) {
		return world.isAdjacent(x, y, radius);
	}

	@Override
	public boolean isAlive(Worm worm) {
		return worm.isAlive();
	}

	@Override
	public boolean isGameFinished(World world) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isImpassable(World world, double x, double y, double radius) {
		return world.isImpassable(x, y, radius);
	}

	@Override
	public void jump(Projectile projectile, double timeStep) {
		try{
			projectile.jump(timeStep);
		}
		catch(NullPointerException exc){
			throw new ModelException("NullPointerException");
		}
	}

	@Override
	public void jump(Worm worm, double timeStep) 
			throws ModelException{
		try{
			worm.getPosition().jump();
		}
		catch(IllegalActionPointsException x){
			throw new ModelException("IllegalActionPointsException");
		}
		catch(IllegalDirectionException x){
			throw new ModelException("IllegalDirectionException");
		}
	}

	@Override
	public void move(Worm worm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rename(Worm worm, String newName) 
			throws ModelException{
		try{
			worm.setName(newName);
		}
		catch(IllegalNameException x){
			throw new ModelException("IllegalNameException");
		}
	}

	@Override
	public void selectNextWeapon(Worm worm) {
		worm.getWeapon().selectNextWeapon();
	}

	@Override
	public void setRadius(Worm worm, double newRadius) 
			throws ModelException{
		try{
			worm.setRadius(newRadius);
		}
		catch(IllegalRadiusException x){
			throw new ModelException("IllegalRadiusException");
		}
	}

	@Override
	public void shoot(Worm worm, int yield){
		worm.getWeapon().shoot(yield);
	}

	@Override
	public void startGame(World world) {
		world.startGame();
	}

	@Override
	public void startNextTurn(World world) {
		world.startNextTurn();
	}

	@Override
	public void turn(Worm worm, double angle) {
		if(worm.canTurn(angle))
			worm.turn(angle);
	}

}
