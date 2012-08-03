import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRectangle;
import acm.util.MediaTools;


public class Status extends GObject {

	private GLabel turnsLeft = new GLabel("");
	private GLabel score = new GLabel("");
	private GImage over;
	
	//Keeps track of turns left for the user.
	public GLabel turnsLeft(int turnsUsed) {
		turnsLeft.setLabel("Balls Left: " + turnsUsed);
		turnsLeft.setFont("Verdana-bold-14");
		turnsLeft.setColor(Color.WHITE);
		return turnsLeft;
	}

	// Keeps track of user's score.
	public GLabel updateScore(int totalScore) {
		score.setLabel("Score: " + totalScore);
		score.setFont("Verdana-bold-16");
		score.setColor(Color.WHITE);
		return score;
	}
	
	public GLabel getLevel(double level) {
		GLabel lev = new GLabel("Level: " + level);
		lev.setFont("Verdana-bold-16");
		lev.setColor(Color.WHITE);
		return lev;
	}
	
	
	// Displays the result if player wins.
		public GImage winGame() {
			over = new GImage("media/win.png");
			AudioClip win = MediaTools.loadAudioClip("media/win.au");
			win.play();
			return over;
		}
		
		// Displays the result if player loses.
		public GImage loseGame() {
			over = new GImage("media/lose.png");
			AudioClip lose = MediaTools.loadAudioClip("media/witchcackle.au");
			lose.play();
			return over;
		}

	@Override
	public GRectangle getBounds() {
		return over.getBounds();
	}

	@Override
	public void paint(Graphics arg0) {
		paintObject(arg0);
	}
	
}
