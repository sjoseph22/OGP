package asteroids.model;

import be.kuleuven.cs.som.annotate.*;

public class Bullet extends Entity {

	/**
	 * Create a new bullet with the given position, velocity and radius.
	 * @param  x
	 *         The x-coordinate of the position of this new ship.
	 * @param  y
	 *         The y-coordinate of the position of this new ship.
	 * @param  xVelocity
	 *         The x-coordinate of the velocity of this new ship.
	 * @param  yVelocity
	 *         The y-coordinate of the velocity of this new ship.
	 * @param  radius
	 *         The radius of this new ship.
	 * @post   The new position of the ship is equal to the given position.
	 *         | new.getPosition().equals({x,y})
	 * @post   The new velocity of the ship is equal to the given velocity.
	 *         | new.getVelocity().equals({xVelocity,yVelocity})
	 * @post   The new radius of the ship is equal to the given radius.
	 *         | new.getRadius() == radius
	 */
	public Bullet(double x, double y, double xVelocity, double yVelocity,
			double radius) throws IllegalArgumentException {
		super(x, y, xVelocity, yVelocity, radius);
	}
	
	/**
	 * Return the remaining bounces the bullet can have with a boundary
	 * @return Returns the remaining bounces
	 *         | result = this.remainingBounces
	 */
	public int getRemainingBounces(){
		return this.remainingBounces;
	}
	
	/**
	 * Decrement the remaining bounces by one. 
	 * @post   The new value of the remaining bounces is the old value minus one
	 *         | remainingBounces--
	 */
	private void removeABounce(){
		remainingBounces--;
	}
	
	private int remainingBounces = 2;

	/**
	 * Set the ship of this bullet to the given ship.
	 * @param ship
	 * 	      The ship this bullet belongs to
	 * @Pre   The given orientation is valid.
	 * 	      | isValidOrientation(orientation)
	 * @post  The given ship contains this bullet
	 *        | this.ship = ship
	 */
	@Raw
	public void setShip(@Raw Ship ship) {
		if (ship == null || ship.getBullets().contains(this)){
			this.ship = ship;
		}
	}
	
	private Ship ship;
	
	/**
	 * Return the ship that this bullet belongs to 
	 * @return returns this bullet's ship
	 *         | result == this.ship
	 */

	public Ship getShip() {
		return this.ship;
	}

	/**
	 * Return the source of this bullet
	 * @return returns the source of this bullet
	 *         | result == this.source
	 */
	public Ship getSource() {
		return this.source;
	}
	
	/**
	 * Set the source for the given ship
	 * @post sets the source of this bullet to the given ship
	 *       | this.source = ship
	 */
	public void setSource(Ship ship) {
		this.source = ship;
	}
	
	private Ship source;

	/**
	 * This bullet is fired from its ship
	 * @post   The bullet is removed from its previous ship now set as source
	 * 		   | source.removeBullet(this);
	 * @post   The bullet is set in the world which contains its source
	 *         | source.getWorld().addBullet(this);
	 * @post   The bullet has a new velocity set to the calculated velocity
	 *         | this.setVelocity(new double[]{xVelocity, yVelocity})
	 * @post   The bullet has a new position set to the calculated position
	 *         | this.setPosition(new double[]{xPosition, yPosition})
	 * @effect The bullet is fired from its ship and its velocity is set to the calculated velocity     
	 */
	public void fire() {
		Ship source = this.getShip();
		double orientationFire = source.getOrientation();
		double[] positionShip = source.getPosition();
		double distanceBetweenCenters = (this.getRadius() + source.getRadius());
		double xPosition = positionShip[0] + distanceBetweenCenters * Math.cos(orientationFire);
		double yPosition = positionShip[1] + distanceBetweenCenters * Math.sin(orientationFire);
		double xVelocity = 250 * Math.cos(orientationFire);
		double yVelocity = 250 * Math.sin(orientationFire);
		this.setSource(source);
		source.removeBullet(this);
		source.getWorld().addBullet(this);
		this.setPosition(new double[]{xPosition, yPosition});
		this.setVelocity(new double[]{xVelocity, yVelocity});
	}

	/**
	 * Return the mass of this bullet.
	 * @return Returns the mass of this 
	 *         | result == mass
	 */
	@Override
	public double getMass() {
		double radius = getRadius();
		double mass = 3/4 * Math.PI * Math.pow(radius, 3) * getDensity();
		return mass;
		
	}

	/**
	 * Return the density of this bullet.
	 * @return Returns the density of this bullet.
	 *         | result == minDensity
	 */
	public double getDensity(){
		return minDensity;
	}
	
	/**
	 * Return the validity of the given radius for this bullet. The radius is type double and larger than 1.
	 * @param radius
	 * 	      The bullet's radius.
	 * @return Returns the validity of the given radius
	 * 		   | result == radius > minRadius
	 */
	@Override
	public boolean isValidRadius(double radius) {
		return (radius > minRadius);
	}
	
	
	private static final double minDensity = 7.8*Math.pow(10, 12);
	
	private static final double minRadius = 1;


	/**
	 * Resolve collisions between this bullet and another entity
	 * @param  entity
	 * 	       The given entity
	 * @post   If this bullet collides with another entity both will be terminated
	 * 	       | entity.terminate() && this.terminate()
	 */
	@Override
	public void collide(Entity entity) {
		entity.terminate();
		this.terminate();
	}
	
	/**
	 * Resolves collisions between this bullet and one of it's world's boundaries
	 * @post  The boundary is terminated if remainingBounces is lower than zero
	 *        | if(this.getRemainingBounces() < 0) this.terminate()
	 */
	public void collideBoundary() {
		super.collideBoundary();
		this.removeABounce();
		if(this.getRemainingBounces() < 0) this.terminate();
	}
	
}
