import java.awt.*;

import acm.graphics.*;

public class Brick extends GObject{
	
	private int START_X;
	private int START_Y;
	private int WIDTH;
	private int HEIGHT;
	GRect brick;
	
	public Brick(int startX, int startY, int width, int height){
		START_X = startX;
		START_Y = startY;
		WIDTH = width;
		HEIGHT = height;
	}
	
	public GRect getBrick(Color color){
		brick = new GRect(START_X, START_Y, WIDTH, HEIGHT);
		brick.setFilled(true);
		brick.setColor(color);
		brick.setFillColor(color);
		return brick;
	}
	
	
	public void remove(){
		brick.setVisible(false);
		brick.setSize(0, 0);
	}
	

	@Override
	public GRectangle getBounds() {
		return brick.getBounds();
	}

	@Override
	public void paint(Graphics arg0) {
		paintObject(arg0);	
	}
}
