package worms.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

public class Projectile {
	
	public boolean isActive() {
		// TODO methode maken
	}
	

	public void jump(double timeStep) {
		// TODO methode maken
		
	}

	@Basic @Raw
	public double getX() {
		return this.x;
	}
	
	@Basic @Raw
	public double getY() {
		return this.y;
	}
	
	@Model @Raw
	private void setPosition(double x double y){
		//TODO methode maken
	}
	
	/**
	 * Variable registering the x-coordinate of a position of a projectile in meters.
	 */
	private double x;

	/**
	 * Variable registering the y-coordinate of a position of a projectile in meters.
	 */
	private double y;
	
	
	
	@Basic @Raw
	public double getRadius() {
		return this.radius;
	}
	
	@Model @Raw
	private void setRadius(){
		if(radius == 0)
			//TODO setRadius
	}
	
	
	
	private final double radius;
	
	
	
	public double getJumpTime(double timeStep) {
		// TODO methode maken
	}



}
