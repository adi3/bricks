import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;

public class Paddle extends GObject{
	
	private GImage paddle;
	
	public Paddle(){
		paddle = new GImage ("rsc/media/plank.png");
	}
	
	public void setSize(int x, int y){
		paddle.setSize(x, y);
	}
	
	public GImage getPaddle(int width, int height){
		paddle.setSize(width, height);
		return paddle;
	}
	
	public double getX(){
		return paddle.getX();
	}
	
	public double getY(){
		return paddle.getY();
	}
	
	public boolean inBounds(MouseEvent e, int width){
		return (e.getX() < (width - paddle.getWidth()/2) 
				&& (e.getX() > paddle.getWidth()/2));
	}
	
	public void move(MouseEvent e, int width, int y_loc){
		if (inBounds(e, width)) {
			paddle.setLocation(e.getX() - paddle.getWidth()/2, y_loc);
		}
	}
	
	@Override
	public GRectangle getBounds() {
		return paddle.getBounds();
	}

	@Override
	public void paint(Graphics arg0) {
		paintObject(arg0);	
	}
}
