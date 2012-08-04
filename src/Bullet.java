import java.awt.*;
import java.awt.event.MouseEvent;

import acm.graphics.*;


public class Bullet extends GObject{

	private GImage bullet;
	private boolean isLaunched;
	Paddle paddle = new Paddle();
	
	public boolean getIsLaunched(){
		return isLaunched;
	}
	
	public double getWidth(){
		return bullet.getWidth();
	}
	
	public void setIsLaunched(boolean bool){
		isLaunched = bool;
	}
	
	public void setLocation(double x, double y){
		bullet.setLocation(x, y);
	}
	
	public void remove(){
		bullet.setVisible(false);
		bullet.setSize(0, 0);
		isLaunched = false;
	}
	
	public boolean inBounds(MouseEvent e, int width){
		return (e.getX() < (width - bullet.getWidth()) 
				&& (e.getX() > bullet.getWidth()));
	}
	
	public double getX(){
		return bullet.getX();
	}
	
	public double getY(){
		return bullet.getY();
	}
	
	public void move(double x, double y){
		bullet.move(x,y);
	}
	
	public GImage getBullet(){
		isLaunched = false;
		bullet = new GImage("media/b1.png");
		bullet.setSize(10, 20);
		bullet.setLocation(paddle.getX() + paddle.getWidth()/2, paddle.getY());
		return bullet;
	}

	@Override
	public GRectangle getBounds() {
		return bullet.getBounds();
	}

	@Override
	public void paint(Graphics arg0) {
		paintObject(arg0);
	}
}