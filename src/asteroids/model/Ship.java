package asteroids.model;

import java.util.*;

import be.kuleuven.cs.som.annotate.*;
/**
 * A class of ships involving a position, velocity, radius and orientation.
 * @Invar  isValidOrientation(getOrientation())
 * @version 3.1
 * @author  Sander Leyssens & Sarah Joseph
 */

// Name: Sarah Joseph
// Course: Informatica
// Repository: https://github.com/sjoseph22/OGP.git

public class Ship extends Entity {
	
	/**
	 * Create a new ship with the given position, velocity, radius and
	 * orientation (in radians).
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
	 * @param  orientation
	 *         The orientation of this new ship.
	 * @post   The new position of the ship is equal to the given position.
	 *         | new.getPosition().equals({x,y})
	 * @post   The new velocity of the ship is equal to the given velocity.
	 *         | new.getVelocity().equals({xVelocity,yVelocity})
	 * @post   The new radius of the ship is equal to the given radius.
	 *         | new.getRadius() == radius
	 * @post   The new orientation of the ship is equal to the given orientation.
	 *         | new.getOrientation() == orientation
	 * @throws IllegalArgumentException
	 *         The given initial position or radius is not valid.
	 *         | !isValidPosition({x,y})||!isValidRadius(radius)
	 */
	public Ship(double x, double y, double xVelocity,
			double yVelocity, double radius, double orientation, double mass) throws IllegalArgumentException {
		super(x,y,xVelocity,yVelocity,radius);
		this.setOrientation(orientation);
		this.setMass(mass);
	}
	
	public double getMinRadius(){
		return minRadius;
	}
	
	private static final double minRadius = 10;

	/**
	 * Return the validity of the given orientation for this ship. The orientation is a
	 * type double between 0 and 2*pi.
	 * @param  orientation
	 * 	       The given orientation.
	 * @return Returns the validity of the given orientation.
	 *         | result == (orientation >=0 && orientation < 2*Math.PI)
	 */
	@Raw
	public static boolean isValidOrientation(double orientation) {
		return (orientation >=0 && orientation < 2*Math.PI);
	}

	/**
	 * Return the orientation of this entity as type double between 0 and 2*pi.
	 * @return Returns the orientation of this entity.
	 *         | result == this.orientation
	 */
	@Basic
	@Raw
	public double getOrientation() {
		return orientation;
	}

	/**
	 * Set the orientation of this ship to the given angle.
	 * @param orientation
	 * 	      The orientation of this entity
	 * @Pre   The given orientation is valid.
	 * 	      | isValidOrientation(orientation)
	 * @post  The new orientation of this entity is equal to the given orientation.
	 *        | new.getOrientation() == orientation
	 */
	@Raw
	protected void setOrientation(double orientation) {
		assert(isValidOrientation(orientation));
		this.orientation = orientation;
	}
	
	/**
	 * Set the mass of this ship to the given weight.
	 * @param mass
	 * 	      The mass of this entity
	 * @post   If the given mass is smaller than the minimum mass, the new mass of this entity is equal to the minimum mass.
	 *        | double minMass = 4/3*Math.PI*Math.pow(this.getRadius(),3)*minDensity;
	 * 	      | if (mass >= minMass) new.getMass() == mass
	 * @post  The new mass of this entity is equal to the given mass.
	 *        | new.getMass() == minMass
	 */
	private void setMass(double mass) {
		double minMass = 4/3.*Math.PI*Math.pow(this.getRadius(),3)*minDensity;
		if (mass >= minMass) this.mass = mass;
		else this.mass = minMass;
	}
	
	private double mass;

	private static final double minDensity = 1.42E12;
	
	@Override
	public double getDensity() {
		return getMass()/(4/3.*Math.pow(getRadius(), 3));
	}

	/**
	 * Return the mass of this ship.
	 * @return Returns the mass of this ship type object with the loaded bullets
	 *         | result == totalMass
	 */
	@Override
	public double getMass(){
		return getBullets().isEmpty() ? this.mass : this.mass + this.getBullets().stream().map(t -> t.getMass()).reduce((u, t) -> u + t).get();
	}
	
	/**
	 * Update the ship's velocity based on its current velocity, its
	 * direction and the given <code>amount</code>.
	 * @param  amount
	 * 	       The thrust of the ship.
	 * @post   The thrust is larger than 0. TODO: may need editing
	 * 	       | if (amount < 0 || Double.isNaN(amount)) amount = 0
	 * @effect The new velocity of the ship is set to the calculated velocity.
	 *         | setVelocity({this.getVelocity()[0]+amount*Math.cos(this.getOrientation()),this.getVelocity()[1]+amount*Math.sin(this.getOrientation())});
	 */
	public void thrust(double amount) {
		if (amount < 0 || Double.isNaN(amount)) amount = 0;
		double[] velocity = getVelocity();
		double orientation = getOrientation();
		setVelocity(new double[] {velocity[0]+amount*Math.cos(orientation),velocity[1]+amount*Math.sin(orientation)});
	}

	/**
	 * Return thrust status
	 * @return Returns activity status of the ship's thruster
	 *         | result == thrusterEnabled
	 */
	public boolean isThrusterActive() {
		return thrustEnabled;
	}

	/**
	 * Set the thruster status of type boolean of this ship to on.
	 * @post   The new status of the thruster is on
	 *         | thrustEnabled = true
	 */
	public void thrustOn() {
		thrustEnabled = true;
	}

	/**
	 * Set the thruster status of type boolean of this ship to off.
	 * @post   The new status of the thruster is on
	 *         | thrustEnabled = true
	 */
	public void thrustOff() {
		thrustEnabled = false;
	}
	
	private boolean thrustEnabled;

	/**
	 * Return the acceleration of the ship as type double.
	 * @return Returns the acceleration of this ship.
	 *         | result == acceleration
	 */
	public double getAcceleration() {
		if (!thrustEnabled) return 0;
		double force = 1.1E18;
		double acceleration = force/getMass();
		return acceleration;
	}

	/**
	 * Update the direction of the ship by adding <code>angle</code>
	 * (in radians) to its current direction. <code>angle</code> may be
	 * negative.
	 * @Pre   The given angle is valid.
	 * 	      | isValidOrientation(getOrientation()+angle)
	 * @param angle
	 * 	      The ship's angle of deviation from its original orientation.
	 * @post  The new orientation of the ship is equal to the angle added 
	 * 	      to the initial orientation.
	 *        | new.getOrientation() = this.getOrientation()+angle
	 */
	public void turn(double angle) {
		assert(isValidOrientation(getOrientation()+angle));
		setOrientation(getOrientation()+angle);
	}

	private double orientation;

	/**
	 * Return the bullets loaded on this ship as a hashed set.
	 * @return Returns the bullets of this ship
	 *         | result == this.bullets
	 */
	public Set<Bullet> getBullets() {
		return new HashSet<Bullet>(bullets);
	}

	private Set<Bullet> bullets = new HashSet<Bullet>();

	/**
	 * Return the number of bullets loaded on this ship as type double.
	 * @return Returns the number of bullets on this ship
	 *         | result == getBullets().size()
	 */
	public int getNbBullets() {
		return getBullets().size();
	}
	
	/**
	 * Load a bullet on this ship.
	 * @param bullet
	 * 	      The given bullet
	 * @post  The given bullet is loaded on this ship
	 *        | bullet.setShip(this)
	 * @post  The position of his bullet is equal to the position of this ship
	 *        | new.bullet.getPosition() == this.getPosition()
	 * @throws IllegalArgumentException
	 *         The given bullet is invalid for this ship.
	 *         | !this.canHaveAsBullet(bullet)
	 */
	public void loadBullet(Bullet bullet) throws IllegalArgumentException {
		if(!canHaveAsBullet(bullet)) throw new IllegalArgumentException("The given bullet is invalid.");
		bullets.add(bullet);
		if(bullet.getWorld() != null) bullet.getWorld().removeEntity(bullet);
		bullet.setPosition(this.getPosition());
		bullet.setShip(this);
	}

	/**
	 * Return if this ship can have the given bullet as bullet. The ship must thereby be in significant overlap with the bullet.
	 * @param bullet
	 * 	      The given bullet
	 * @return  Returns false if the given bullet is null
	 * 		  | if (bullet == null) result == false
	 * @return  Returns false if the bullet belongs to a different ship
	 *        | if (bullet.getShip() != null && bullet.getShip() != this) result == false
	 * @return  Returns false if the bullet originates from a different ship.
	 *        | if (bullet.getSource() != null && bullet.getSource() != this) result == false
	 * @return Returns if there is significant overlap between the ship and bullet
	 *         |significantOverlap = (this.getDistanceBetweenCenters(bullet) < 0.99*(this.getRadius() - bullet.getRadius()))
	 */
	private boolean canHaveAsBullet(Bullet bullet) {
		if (bullet == null) return false;
		if (bullet.isTerminated()) return false;
		if (bullet.getShip() != null && bullet.getShip() != this) return false;
		if (bullet.getSource() != null && bullet.getSource() != this) return false;
		return true;
	}

	/**
	 * Load bullets of this ship.
	 * @param  bullets
	 * 	       The bullets of this ship
	 * @post   The bullet that are part of the ship are loaded.
	 *         | for(Bullet bullet : bullets) this.loadBullet(bullet)
	 * @throws IllegalArgumentException
	 *         At least one of the bullets is invalid for this ship.
	 *         | for some bullet in bullets: !this.canHaveAsBullet(bullet)
	 *
	 */
	public void loadBullet(Bullet... bullets) throws IllegalArgumentException {
		for(Bullet bullet : bullets) if (!canHaveAsBullet(bullet)) throw new IllegalArgumentException("One of the bullets is invalid.");
		for(Bullet bullet : bullets) this.loadBullet(bullet);
	}
	
	/**
	 * Remove a bullet from this ship.
	 * @param  bullets
	 * 	       A given bullet
	 * @effect The bullet is removed from this ship and this ship is removed from the bullet.
	 *         | bullets.remove(bullet) && bullet.setShip(null)
	 * @throws IllegalArgumentException
	 *         The given bullet is not part of this ship
	 *         | bullet.getShip() != this
	 */
	public void removeBullet(Bullet bullet) throws IllegalArgumentException {
		if (bullet.getShip() != this) throw new IllegalArgumentException("The given bullet is not part of this ship");
		bullets.remove(bullet);
		bullet.setShip(null);
	}
	
	/**
	 * The ship moves <code>dt</code> seconds at its thrust acceleration.
	 * @param  dt
	 * 	       The time of movement of this ship.
	 * @effect The ship accelerates for the given period of dt.
	 * 	       | thrust(getAcceleration()*dt)   
	 */
	public void move(double dt){
		super.move(dt);
		thrust(getAcceleration()*dt);
	}

	/**
	 * This ship fires a bullet
	 * @effect The bullet is removed from this ship
	 * 		   | this.removeBullet(bullet)
	 * @effect The source of the bullet is set to this ship
	 *         | bullet.setSource(this)
	 * @effect The bullet is set in the world which contains this ship
	 *         | this.getWorld().addBullet(bullet)
	 * @effect The bullet has a new velocity set to the calculated velocity
	 *         | bullet.setVelocity(new double[]{xVelocity, yVelocity})
	 * @effect The bullet has a new position set to the calculated position
	 *         | bullet.setPosition(new double[]{xPosition, yPosition}) 
	 */
	public void fireBullet(){
		if (this.getWorld() == null) return;
		Bullet bullet = getBullets().iterator().next();
		double orientationFire = this.getOrientation();
		double[] positionShip = this.getPosition();
		double distanceBetweenCenters = (bullet.getRadius() + this.getRadius());
		double xPosition = positionShip[0] + distanceBetweenCenters * Math.cos(orientationFire);
		double yPosition = positionShip[1] + distanceBetweenCenters * Math.sin(orientationFire);
		double xVelocity = 250 * Math.cos(orientationFire);
		double yVelocity = 250 * Math.sin(orientationFire);
		bullet.setSource(this);
		this.removeBullet(bullet);
		bullet.setPosition(new double[]{xPosition, yPosition});
		bullet.setVelocity(new double[]{xVelocity, yVelocity});
		try{
			this.getWorld().addEntity(bullet);
		} catch (IllegalArgumentException exc){
			for (Entity entity: this.getWorld().getEntities()){
				if (entity.overlap(bullet)) {
					bullet.collide(entity);
					
				}
			}
		}
		double distanceToEdge = getWorld().getSize()[0] - bullet.getPosition()[0];
		distanceToEdge = Math.min(distanceToEdge, bullet.getPosition()[0]);
		distanceToEdge = Math.min(distanceToEdge, getWorld().getSize()[1] - bullet.getPosition()[1]);
		distanceToEdge = Math.min(distanceToEdge, bullet.getPosition()[1]);
		if(distanceToEdge < 0) bullet.terminate();
	}

	
	/**
	 * Resolve collisions between this ship and colliding ship or a colliding bullet
	 * 
	 * @param  entity
	 * 	       The entity named entity
	 * @effect If this ship collides with a bullet and the bullet was fired by the ship, it will be reloaded
	 * 	       | if (bullet.getSource() == this) this.loadBullet(bullet)
	 * @effect If this ship collides with a bullet and the bullet was fired by another ship, both entities will be terminated
	 *         | bullet.terminate() && this.terminate()
	 * @effect If this ship collides with another ship, they bounce off each other with adjusted velocities
	 *         | this.setVelocity(newVelocityi);entity.setVelocity(newVelocityj)
	 */
	@Override
	public void collide(Entity entity) {
		if (entity instanceof Ship) {
			double mi = this.getMass();double mj = entity.getMass();
			double[] deltaR = this.getPositionDifference(entity);
			double[] deltaV = this.getVelocityDifference(entity);
			double sigma = Math.sqrt(dotProduct(deltaR,deltaR));
			double j = 2*mi*mj*(dotProduct(deltaV,deltaR))/(sigma*(mi+mj));
			double jx = j*deltaR[0]/sigma;double jy = j*deltaR[1]/sigma;
			double[] oldVelocityi = this.getVelocity();
			double[] oldVelocityj = entity.getVelocity();
			double[] newVelocityi = new double[]{oldVelocityi[0]+jx/mi,oldVelocityi[1]+jy/mi};
			double[] newVelocityj = new double[]{oldVelocityj[0]-jx/mj,oldVelocityj[1]-jy/mj};
			this.setVelocity(newVelocityi);entity.setVelocity(newVelocityj);
		}
		else entity.collide(this);
		
	}

	public Program getProgram() {
		// TODO Auto-generated method stub
		return this.program;
	}

	public void loadProgram(Program program) {
		this.program = program;
	}

	public List<Object> executeProgram(double dt) {
		// TODO Auto-generated method stub
		return null;
		
	}

	private Program program;



}
