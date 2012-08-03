import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

public class Breakout extends GraphicsProgram {
	
	private static final long serialVersionUID = 5542887389048921770L;
	
/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 6;

/** Number of turns */
	private static final int NTURNS = 3;
	private static final int STATUS_X1 = 10;
	private static final int STATUS_X2 = 280;
	private static final int STATUS_Y1 = 27;
	private static final int STATUS_Y2 = 50;
	
/** Time delay between successive loops */
	private final static int DELAY = 10;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	/** Total number of bricks */
	private final static int TBRICKS = NBRICKS_PER_ROW * NBRICK_ROWS;

	
/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (APPLICATION_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Width of a row */
	private static final int TOTAL_WIDTH =
					BRICK_WIDTH * NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Offset of the top brick row from the top */
	public static final int BRICK_Y_OFFSET = 70;
	
/** Offset of the paddle from the bottom */
	public static final int PADDLE_OFFSET = 70;
	public static final int PADDLE_WIDTH = 60;
	public static final int PADDLE_HEIGHT = 10;
	public static final int PADDLE_X = (APPLICATION_WIDTH - PADDLE_WIDTH) / 2;;
	public static final int PADDLE_Y = APPLICATION_HEIGHT - PADDLE_OFFSET;
	
/** Background Color */
	private static final int TOTAL_BG_OPTIONS = 10;
	private int turnNum;
	private int bricksHit;
	private int score;
	private int looper;
	private int bricksRemoved;
	
	private Paddle paddle;
	private Ball ball;
	private Status status = new Status();
	private GImage bg;
	private GLabel alert;
	private ArrayList<GPoint> toRemove;

	private Capsule capsule = new Capsule();
	private GImage plank;
	
	private int time_extraBall;
	private boolean bool_extraBall;
	
	private int time_fastBall;
	private boolean bool_fastBall;
	
	private int time_slowBall;
	private boolean bool_slowBall;
	
	private int time_solidBall;
	private boolean bool_solidBall;
	private boolean isSolid;
	
	private int time_longBoard;
	private boolean bool_longBoard;
	
	private int time_shortBoard;
	private boolean bool_shortBoard;
	
	private int time_bullet;
	private boolean bool_bullet;
	private Bullet bullet = null;
	
	private int round = 1;
	AudioClip capTone = MediaTools.loadAudioClip("media/click.au");
	AudioClip shoot = MediaTools.loadAudioClip("media/gun.au");
	
	private void setRandomsToPowers(){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(rgen.nextInt(500, 1000));
		temp.add(rgen.nextInt(2000, 2300));
		temp.add(rgen.nextInt(3000, 3800));
		temp.add(rgen.nextInt(4200, 4400));
		temp.add(rgen.nextInt(5500, 6000));
		temp.add(rgen.nextInt(7000, 7300));
		temp.add(rgen.nextInt(8000, 8400));
		
		int time;
		time = temp.get(rgen.nextInt(0, temp.size()-1));
		time_extraBall = time;
		temp.remove(temp.indexOf(time));
		
		time = temp.get(rgen.nextInt(0, temp.size()-1));
		time_fastBall = time;
		temp.remove(temp.indexOf(time));
		
		time = temp.get(rgen.nextInt(0, temp.size()-1));
		time_slowBall = time;
		temp.remove(temp.indexOf(time));

		time = temp.get(rgen.nextInt(0, temp.size()-1));
		time_solidBall = time;
		temp.remove(temp.indexOf(time));
		
		time = temp.get(rgen.nextInt(0, temp.size()-1));
		time_longBoard = time;
		temp.remove(temp.indexOf(time));
		
		time = temp.get(rgen.nextInt(0, temp.size()-1));
		time_shortBoard = time;
		temp.remove(temp.indexOf(time));
		
		time = temp.get(rgen.nextInt(0, temp.size()-1));
		time_bullet = time;
		temp.remove(temp.indexOf(time));		
	}
	
	
	 public static void main(String[] args){
	      new Breakout().start();
	   }
	 
	 public void init(){
		 ProgramMenuBar menu = getMenuBar();
		 menu.setVisible(false);
		 repaint();
	 }
	 
	public void run(){
		while(true){
			setTitle("Badass Breakout | Adi, Inc. - Round: " + round);
			setGame();
			runGame();
			endGame();
			waitForClick();
		}
	}
		
	private void addStatus(){
		add(status.updateScore(bricksHit), STATUS_X2, STATUS_Y1);
		add(status.turnsLeft(NTURNS - turnNum), STATUS_X2, STATUS_Y2);
		
		double diff = TBRICKS - bricksRemoved;
		double level = 10*(diff/TBRICKS);
		DecimalFormat twoDForm = new DecimalFormat("#.#");
        double normalLevel = Double.valueOf(twoDForm.format(level));
		add(status.getLevel(normalLevel), STATUS_X1, STATUS_Y1);
	}
	
	private void runGame(){
		while (bricksHit < (TBRICKS - bricksRemoved)) {
			ball.move();
			looper++;
			dropPower();
			renderPower();
			if(bullet != null && bullet.getIsLaunched()) firebullet();
			checkWallCollision();
			checkOtherCollision();
			if (turnNum == NTURNS) break;
			pause(DELAY);
		}
	}
	
	private void firebullet(){
		bullet.move(0, -5);
		GObject target = getElementAt (bullet.getX()+ (bullet.getWidth()/2), bullet.getY() - 1);
		if (target != null && target.getClass().getName() == "acm.graphics.GRect") {
			remove(target);
			bricksHit++;
			remove(status);
			score += getAddedScore(target);
			add(status.updateScore(score*10), STATUS_X2, STATUS_Y1);
		}
		if(bullet.getY() < 0) {
			bullet.remove();
			bullet = null;
		}
	}
	
	private void renderPower(){
		if(capsule != null) {
			GObject collider = getElementAt(capsule.getX() - 1, (capsule.getY() + 11) );
			if(capsule.getY() + 15 > APPLICATION_HEIGHT || collider == null) {
				removeCapsule();
				return;
			}
			if (collider != bg && collider != plank && collider.getClass().getName() == "acm.graphics.GImage") {
				if(bool_extraBall){
					turnNum--;
					add(status.turnsLeft(NTURNS - turnNum), STATUS_X2, STATUS_Y2);					
					bool_extraBall = displayPowerResult("Extra Ball Awarded!");
				}
				if(bool_fastBall){
					ball.fast();
					bool_fastBall = displayPowerResult("Ball Speed Increased!");
				}
				if(bool_slowBall){
					ball.slow();
					bool_slowBall = displayPowerResult("Ball Speed Decreased!");
				}
				if(bool_solidBall){
					isSolid = true;
					ball.solidColor();
					bool_solidBall = displayPowerResult("Ball Turned Solid!");
				}
				if(bool_longBoard){
					paddle.setSize(100,10);
					bool_longBoard = displayPowerResult("Paddle Made Long!");
				}
				if(bool_shortBoard){
					paddle.setSize(30,10);
					bool_shortBoard = displayPowerResult("Paddle Made Short!");
				}
				if(bool_bullet){
					bullet = new Bullet();
					add(bullet.getBullet(), -15, -15);
					bool_bullet = displayPowerResult("Bullet Loaded. Click to Fire!");
				}
				return;	
			}
		}	
	}
	
	private boolean displayPowerResult(String str){
		capsule.remove();
		capsule = null;
		alert.setLabel(str);
		pause(1000);
		alert.setVisible(false);
		return false;
	}
	
	private void removeCapsule(){
		alert.setVisible(false);
		capsule.remove();
		capsule = null;
		setAllFlagsFalse();
	}
	
	private void setAllFlagsFalse(){
		bool_extraBall = bool_fastBall = bool_slowBall 
		= bool_solidBall = bool_longBoard = bool_shortBoard
		= bool_bullet = false;
	}
	
	private boolean buildPower(String str){
		capsule = new Capsule();
		capTone.play();
		add(capsule.getCapsule(str), rgen.nextInt(10, APPLICATION_WIDTH-10), -10);
		alert.setLabel("Collect Capsule!");
		alert.setVisible(true);
		return true;
	}
	
	private void dropPower(){
		if(capsule != null) capsule.move(0, 3);
		if(looper == time_extraBall) bool_extraBall = buildPower("cap/red.png");
		if(looper == time_fastBall)	bool_fastBall = buildPower("cap/orange.png");
		if(looper == time_slowBall)	bool_slowBall = buildPower("cap/green.png");
		if(looper == time_solidBall) bool_solidBall = buildPower("cap/yellow.png");
		if(looper == time_longBoard) bool_longBoard = buildPower("cap/blue.png");
		if(looper == time_shortBoard) bool_shortBoard = buildPower("cap/lilac.png");
		if(looper == time_bullet) bool_bullet = buildPower("cap/pink.png");
	}
	
	private void setCapAlertLabel(){
		alert = new GLabel("Collect Capsule!");
		alert.setFont("Verdana-bold-14");
		alert.setColor(Color.WHITE);
		alert.setVisible(false);
		add(alert, STATUS_X1, STATUS_Y2);
	}
	
/** This section informs user if game has been won or lost */
	
	// Displays result (win or lose) once game finishes.
	private void endGame() {
		ball.remove();
		if(bullet != null) {
			bullet.remove();
			bullet = null;
		}
		if(capsule != null) {
			capsule.remove();
			capsule = null;
		}
		if (bricksHit == (TBRICKS - bricksRemoved)) {
			round++;
			GImage win = status.winGame();
			add(win, (APPLICATION_WIDTH - win.getWidth())/2, 
						APPLICATION_HEIGHT/2);
		} else {
			GImage lose = status.loseGame();
			add(lose, (APPLICATION_WIDTH - lose.getWidth())/2, 
					APPLICATION_HEIGHT/2);
		}
	}	
	
	
/** This section defines ball behaviour on brick or paddle collision */
	
	// Checks if ball has collided with anything.
	private void checkOtherCollision(){
		GObject collidingObject = getCollidingObject();
		if(collidingObject != null) executeCollisionResult(collidingObject);
	}
	
	// Determines what should be done after collision occurs
	// based on what object the ball has collided with.
	private void executeCollisionResult(GObject collider) {
		if(collider.getClass().getName() == "acm.graphics.GRect") {
			remove (collider);
			if(!isSolid) ball.reverseY();
			bricksHit++;
			remove(status);
			score += getAddedScore(collider);
			add(status.updateScore(score*10), STATUS_X2, STATUS_Y1);
		} else if (collider.getY() != 0 && collider != plank &&
				collider.getClass().getName() != "acm.graphics.GLabel") {
			if (ball.getYVel() > 0) ball.reverseY();
			double position = ball.getX() - paddle.getX() - paddle.getWidth()/2;
			if(ball.getXVel() > 0 && position < 0) ball.reverseX();
			if(ball.getXVel() < 0 && position > 0) ball.reverseX();
		}
	}
	
	private int getAddedScore(GObject collider){
		Color temp = collider.getColor();
		if(temp == Color.GRAY) return 1;
		if(temp == Color.GREEN) return 2;
		if(temp == Color.YELLOW) return 3;
		if(temp == Color.ORANGE) return 4;
		if(temp == Color.RED) return 5;
		return 0;
	}
	
	// Determines what object the ball has collided with.
		private GObject getCollidingObject() {
			GObject atCorner;
			atCorner = getElementAt (ball.getX(), ball.getY() + (2 * BALL_RADIUS));
			if (atCorner != null && !atCorner.equals(bg)) return atCorner;
			atCorner = getElementAt (ball.getX() + (2 * BALL_RADIUS), ball.getY());
			if (atCorner != null && !atCorner.equals(bg)) return atCorner;
			atCorner = getElementAt (ball.getX(), ball.getY());
			if (atCorner != null && !atCorner.equals(bg)) return atCorner;
			atCorner = getElementAt (ball.getX() + (2 * BALL_RADIUS), 
															ball.getY() + (2 * BALL_RADIUS));
			return atCorner;
		}
	
/** This section defines ball behaviour on wall collision */
	
	// Determines what happens when ball hits any wall.
	private void checkWallCollision() {
		double a = ball.getX() + (2 * BALL_RADIUS);
		double b = ball.getY() + (2 * BALL_RADIUS);
		// For bouncing off side walls.
		if (a > APPLICATION_WIDTH && ball.getXVel() > 0) ball.reverseX();
		if (a < (2 * BALL_RADIUS) && ball.getXVel() < 0) ball.reverseX();
		// For bouncing off top wall.
		if (b < (2 * BALL_RADIUS) && ball.getYVel() < 0) ball.reverseY();
			
		// For ending turn when ball hits bottom wall.
		if (b > APPLICATION_HEIGHT) {
			ball.remove();
			turnNum++;
			remove(status);
			add(status.turnsLeft(NTURNS - turnNum), STATUS_X2, STATUS_Y2);
			if (turnNum < NTURNS) {
				setBall();
				paddle.setSize(PADDLE_WIDTH, PADDLE_HEIGHT);
				waitForClick();
			}
		}
	}
	
	private void setGame(){
		setInstanceVars();
		setBackground();
		selectBricksToRemove();
		setBricks();
		setPaddle();
		setBall();
		setCapAlertLabel();
		setRandomsToPowers();
		setStartLabels();
	}
	
	private void setInstanceVars(){
		turnNum = bricksHit = score = looper = 0;
		bricksRemoved = rgen.nextInt(10, 90);
		setAllFlagsFalse();
		capsule = null;
	}
	
	private void setStartLabels(){
		addStatus();
		GImage start = new GImage ("media/start.png");
		add (start, (APPLICATION_WIDTH - start.getWidth())/2, 
				20+APPLICATION_HEIGHT/2);
		waitForClick();
		remove(start);
	}
	
	private void setBall(){
		ball = new Ball(APPLICATION_WIDTH, APPLICATION_HEIGHT, BALL_RADIUS);
		add (ball.getBall());
		isSolid = false;
	}
	
	private void setPaddle(){
		paddle = new Paddle();
		add (paddle.getPaddle(PADDLE_WIDTH, PADDLE_HEIGHT), PADDLE_X, PADDLE_Y);
		addMouseListeners();
	}
	
	// Attaches horizontal paddle location to mouse movement.
	// Also attaches loaded bullet to paddle.
	public void mouseMoved(MouseEvent e) {
		paddle.move(e, APPLICATION_WIDTH, PADDLE_Y);
		if(bullet != null && !bullet.getIsLaunched()
		&& bullet.inBounds(e, APPLICATION_WIDTH)) bullet.setLocation(e.getX()-5, PADDLE_Y-5);
	}
	
	public void mouseClicked(MouseEvent e) {
		if(bullet != null && !bullet.getIsLaunched()) {
			bullet.setIsLaunched(true);
			shoot.play();
		}
	}
	
	private void setBackground(){
		int temp = rgen.nextInt(1,TOTAL_BG_OPTIONS);
		removeAll();
		bg = new GImage("bg/bg" + temp + ".jpg");
		bg.scale(1.03,1.05);
		add(bg);
		plank = new GImage("media/plank.png");
		plank.scale(1.05,1.1);
		add(plank, 5, 5);
	}
	
	private void setBricks() {
		// Builds block by specifying the starting coordinates of each brick.
		for (int j = 0; j < NBRICK_ROWS; j++){
			int startY = BRICK_Y_OFFSET + (j * (BRICK_HEIGHT + BRICK_SEP));	
			for (int i = 0; i < NBRICKS_PER_ROW; i++){
				int startX = BRICK_SEP + ((APPLICATION_WIDTH - TOTAL_WIDTH - BRICK_WIDTH) / 2) 
					+ (i * (BRICK_WIDTH + BRICK_SEP));
				GPoint pt = new GPoint(i,j);
				if (!toRemove.contains(pt)) {
				
					Brick brick = new Brick(startX, startY, BRICK_WIDTH, BRICK_HEIGHT);
					if (j < 2) { add(brick.getBrick(Color.RED));
					} else if (j < 4) { add(brick.getBrick(Color.ORANGE));
					} else if (j < 6){ add(brick.getBrick(Color.YELLOW));				
					} else if (j < 8){ add(brick.getBrick(Color.GREEN));			
					} else { add(brick.getBrick(Color.GRAY));
					}
				}
			}
		}
	}
	
	private void selectBricksToRemove(){
		toRemove = new ArrayList<GPoint>();
		int cycles = bricksRemoved;
		while(cycles > 0){
			int i = rgen.nextInt(0, NBRICKS_PER_ROW-1);
			int j = rgen.nextInt(0, NBRICK_ROWS-1);
			GPoint pt = new GPoint(i,j);
			if(!toRemove.contains(pt)){
				toRemove.add(pt);
				cycles--;
			}
		}
	}
}