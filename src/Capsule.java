import java.awt.*;
import acm.graphics.*;

public class Capsule extends GObject{

	private GImage capsule;
	
	public GImage getCapsule(String str){
		capsule = new GImage(str);
		capsule.setSize(12, 20);
		return capsule;
	}
	
	public void move(double x, double y){
		capsule.move(x,y);
	}

	public double getX(){
		return capsule.getX();
	}
	
	public double getY(){
		return capsule.getY();
	}

	public void remove(){
		capsule.setVisible(false);
		capsule.setSize(0, 0);
	}
	@Override
	public GRectangle getBounds() {
		return capsule.getBounds();
	}

	@Override
	public void paint(Graphics arg0) {
		paintObject(arg0);	
	}
}
