package asteroids.model;

<<<<<<< HEAD
import asteroids.util.ModelException;
=======
import be.kuleuven.cs.som.annotate.*;
>>>>>>> refs/remotes/origin/master

public class Bullet extends Entity {

<<<<<<< HEAD
	protected Bullet(double x, double y, double xVelocity, double yVelocity,
			double radius, double orientation) throws IllegalArgumentException {
		super(x, y, xVelocity, yVelocity, radius, orientation);
		// TODO Auto-generated constructor stub
		this.setMassBullet(mass);
	}

	public void setShip(Ship ship) {
		if ((this.getWorld() == null) || (ship == null)){
			this.ship = ship;
		}
	}
	
	private Ship ship;
	

	public Ship getShip() {
		return this.ship;
	}

	public Ship getSource() {
		return this.source;
	}
	
	public void setSource(Ship ship) {
		this.source = ship;
	}
	
	private Ship source;

	public void fire() {
		double orientationFire = this.getShip().getOrientation();
		double distanceBetweenCenters = (this.getRadius() + this.getShip().getRadius());
		double xPosition = distanceBetweenCenters * Math.cos(orientationFire);
		double yPosition = distanceBetweenCenters * Math.sin(orientationFire);		
		this.setPosition(new double[]{xPosition, yPosition});
		double xVelocity = 250 * Math.cos(orientationFire);
		double yVelocity = 250 * Math.sin(orientationFire);
		this.setVelocity(new double[]{xVelocity, yVelocity});
		this.setSource(this.getShip());
		this.setWorld(this.getWorld());
		this.setShip(null);
		
	}

	
	public double getMassBullet() {
		return this.mass;
		
	}
	
	private double mass;
	
	
	public double setMassBullet(double mass) {
		double radius = getRadius();
		double minMass = 3/4 * Math.PI * Math.pow(radius, 3) * minDensity;
		return mass;
		
	}

	public double getDensity(){
		return minDensity;
	}
	
	@Override
	public boolean isValidRadius(double radius) {
		return (radius > minRadius);
	}
	
	
	private static final double minDensity = 7.8*Math.pow(10, 12);
	
	private static final double minRadius = 1;
=======
	


	public Bullet(double x, double y, double xVelocity, double yVelocity,
			double radius) throws IllegalArgumentException {
		super(x, y, xVelocity, yVelocity, radius);
	}
	
	public int getRemainingBounces(){
		return this.remainingBounces;
	}
	
	private void removeABounce(){
		remainingBounces--;
	}
	
	private int remainingBounces = 2;

	@Raw
	public void setShip(@Raw Ship ship) {
		if (ship == null || ship.getBullets().contains(this)){
			this.ship = ship;
		}
	}
	
	private Ship ship;
	

	public Ship getShip() {
		return this.ship;
	}

	public Ship getSource() {
		return this.source;
	}
	
	public void setSource(Ship ship) {
		this.source = ship;
	}
	
	private Ship source;

	public void fire() {
		Ship source = this.getShip();
		double orientationFire = source.getOrientation();
		double distanceBetweenCenters = (this.getRadius() + source.getRadius());
		double xPosition = distanceBetweenCenters * Math.cos(orientationFire);
		double yPosition = distanceBetweenCenters * Math.sin(orientationFire);
		double xVelocity = 250 * Math.cos(orientationFire);
		double yVelocity = 250 * Math.sin(orientationFire);
		this.setSource(source);
		source.removeBullet(this);
		source.getWorld().addBullet(this);
		this.setPosition(new double[]{xPosition, yPosition});
		this.setVelocity(new double[]{xVelocity, yVelocity});
	}

	@Override
	public double getMass() {
		double radius = getRadius();
		double mass = 3/4 * Math.PI * Math.pow(radius, 3) * getDensity();
		return mass;
		
	}

	public double getDensity(){
		return minDensity;
	}
	
	@Override
	public boolean isValidRadius(double radius) {
		return (radius > minRadius);
	}
	
	
	private static final double minDensity = 7.8*Math.pow(10, 12);
	
	private static final double minRadius = 1;


	@Override
	public void collide(Entity entity) {
		entity.terminate();
		this.terminate();
	}
	
	public void collideBoundary() {
		super.collideBoundary();
		this.removeABounce();
		if(this.getRemainingBounces() < 0) this.terminate();
	}
>>>>>>> refs/remotes/origin/master
	
}
