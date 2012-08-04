import java.awt.*;
import acm.graphics.*;
import acm.util.*;

public class Ball extends GObject{

	GOval ball;
	private double xVel;
	private double yVel;

	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	public Ball(double win_width, double win_height, double radius){
		ball = new GOval ((win_width - radius)/2,
				(win_height - radius)/2, 2 * radius, 2 * radius);
		ball.setFilled(true);
		ball.setFillColor(Color.RED);
		ball.setColor(Color.RED);
		setBallVelocity();
	}
	
	public GOval getBall(){
		return ball;
	}
	
	// Determines horizontal and vertical velocities of ball.
	private void setBallVelocity() {
		xVel = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) {
			xVel = -xVel;
		}
		yVel = 2.5;
	}
	
	// Moves the ball around the screen.
	public void move() {
		ball.move(xVel, yVel);
	}
	
	public void remove(){
		ball.setVisible(false);
		ball.setSize(0, 0);
	}
	
	public void reverseX() {
		xVel = -xVel;
	}
	
	public void reverseY() {
		yVel = -yVel;
	}
	
	public void slow(){
		xVel = xVel / 1.3;
		yVel = yVel / 1.3;
	}
	
	public void fast(){
		xVel = 1.3 * xVel;
		yVel = 1.3 * yVel;
	}
	
	public double getX(){
		return ball.getX();
	}
	
	public void solidColor(){
		ball.setColor(Color.YELLOW);
		ball.setFillColor(Color.YELLOW);
	}
	
	public double getY(){
		return ball.getY();
	}

	public double getXVel(){
		return xVel;
	}
	
	public double getYVel(){
		return yVel;
	}

	@Override
	public GRectangle getBounds() {
		return null;
	}

	@Override
	public void paint(Graphics arg0) {
		paintObject(arg0);	
	}
}
