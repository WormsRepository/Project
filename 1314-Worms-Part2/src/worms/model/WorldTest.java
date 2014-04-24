package worms.model;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WorldTest {
	
	/**
	 * Variable referencing a world.
	 */
	private static World testWorld1;

	/**
	 * Set up a mutable test fixture.
	 * 
	 * @post	The variable testWorld1 references a new world with a width and height of 10 meter,
	 * 			a passable map and a random.
	 */
	@Before
	public void setUpMutableFixture(){
		Random random = new Random();
		boolean[][] passableMap = {{false,false,false,false,false,false},
								   {false,true,true,true,true,false},
								   {false,true,true,true,true,false},
								   {false,true,true,true,true,false},
								   {false,true,true,true,true,false},
								   {false,false,false,false,false,false}};
		testWorld1 = new World(10,10,passableMap,random);
	}

	/**
	 * 
	 */
	@BeforeClass
	public static void setUpImmutableFixture(){
	}
	
	@Test
	public void addNewWorm_legalCase(){
		for(int i = 0;i<10;i++){
			testWorld1.addNewWorm();
		}
		for(Worm worm: testWorld1.getWorms()){
			assertTrue(testWorld1.isAdjacent(worm.getPosition().getX(),
					worm.getPosition().getY(),worm.getRadius()) == true);
			assertTrue(worm.getRadius() == 0.25);
		}
	}
	
	@Test
	public void addNewFood_legalCase(){
		for(int i = 0;i<10;i++){
			testWorld1.addNewFood();
		}
		for(Food food: testWorld1.getFood()){
			assertTrue(testWorld1.isAdjacent(food.getX(),
					food.getY(),Food.getRadius()) == true);
			assertTrue(Food.getRadius() == 0.20);
		}
	}
}
