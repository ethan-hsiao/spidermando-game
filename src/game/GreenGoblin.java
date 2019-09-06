package game;

import java.io.File;
import java.util.ArrayList;

import engine.*;
import game.Flash.FlashType;
import game.Projectile.ProjType;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;

public class GreenGoblin extends Actor{
	private Image mySprite = new Image("file:images/ethanBoss.jpg");
	private double dx;
	private double dy;
	private boolean turn = true;
	private boolean fight = false;
	int seconds = 2;
	long prev = 0;
	long laughUpdate = 0;
	int leftBound = 89 * GameWorldApp.BLOCK_SIZE;
	int rightBound = 110 * GameWorldApp.BLOCK_SIZE;
	int health = 100;
	Media laugh1 = new Media(new File(new File("images/laugh1.mp3").getAbsolutePath()).toURI().toString());
	Media laugh2 = new Media(new File(new File("images/laugh2.mp3").getAbsolutePath()).toURI().toString());
	Media laugh3 = new Media(new File(new File("images/laugh3.mp3").getAbsolutePath()).toURI().toString());
	Media laugh4 = new Media(new File(new File("images/laugh4.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer laughPlayer = new MediaPlayer(laugh1);
	ArrayList<Media> laughList = new ArrayList<Media>();
	Media deathSound = new Media(new File(new File("images/deathnoise.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer deathPlayer = new MediaPlayer(deathSound);
	Media fireShoot = new Media(new File(new File("images/firethrow.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer firePlayer = new MediaPlayer(fireShoot);
	int count = 0;
	
	public GreenGoblin(){
		setImage(mySprite);
		dx = 40;
		dy = 0;
		setRotationAxis(Rotate.Y_AXIS);
		setRotate(180);
		laughList.add(laugh1);
		laughList.add(laugh2);
		laughList.add(laugh3);
		laughList.add(laugh4);
		firePlayer.setVolume(0.2);
	}

	@Override
	public void act(long now) {
		if(fight){
			Hero heroe;
			boolean heroCreated = false;
			try{
				heroe = getWorld().getObjects(Hero.class).get(0);
				heroCreated = true;
			}
			catch(IndexOutOfBoundsException e){
				heroe = null;
			}
			double myX = getX() + getImage().getWidth() / 2;
			if(heroCreated){
				setRotationAxis(Rotate.Y_AXIS);
				if(myX > heroe.getTranslateX()){
					setRotate(180);
				}else{
					setRotate(360);
				}
				// TODO Auto-generated method stub
				if(getX() + getWidth() >= rightBound){
					dx *= -1;
	//				setRotationAxis(Rotate.Y_AXIS);
	//				setRotate(getRotate() + 180);
				}else if(getX() <= leftBound){
					dx *= -1;
	//				setRotationAxis(Rotate.Y_AXIS);
	//				setRotate(getRotate() + 180);
				}
			}
			setX(getX() + dx);
			if(heroCreated){
				if(now - prev >= 500000000){
					firePlayer.stop();
					double myY = getY() + getImage().getHeight() / 1.75;
					double heroX = heroe.getTranslateX();
					double heroY = heroe.getTranslateY() + heroe.getImage().getHeight() * 2;
					double speed = 90.0;
					double tangent = Math.abs(heroY - myY) / Math.abs(myX - heroX);
					double angle = Math.atan(tangent);
					double dx;
					double dy;
					if(myX < heroX && myY < heroY){
						//dx =  -1 * speed * Math.cos(angle);
						//dy = -1 * speed * Math.sin(angle);
						angle =  2 * Math.PI - angle;
					}else{
						//dx =  speed * Math.cos(angle);
						//dy = -1 * speed * Math.sin(angle);
						angle = Math.PI + angle;
					}
					dx =  1 * speed * Math.cos(angle);
					dy = -1 * speed * Math.sin(angle);
					
					angle *= (180.0 / Math.PI);
					shoot(dx, dy, angle);
					prev = now;
					firePlayer.play();
				}
			}
			if(now - laughUpdate >= 1000000000){
				seconds++;
				laughUpdate = now;
			}
			
			if(seconds >= 5){
				laughPlayer.stop();
				count++;
				if(count == 4){
					count = 0;
				}
				laughPlayer = new MediaPlayer(laughList.get(count));
				laughPlayer.play();
				seconds = 0;
			}
			
			for(Projectile proj : getIntersectingObjects(Projectile.class)){
				if(proj.getT() == ProjType.HERO){
					health -= 5;
					if(proj != null){
						try{
							getWorld().remove(proj);
						}catch(NullPointerException e){
							
						}
					}
					if(health <= 0){
						laughPlayer.stop();
						deathPlayer.play();
						try{
							getWorld().add(new Flash(getX(), getY(), FlashType.BARREL));
							getWorld().remove(this);
						}
						catch(NullPointerException e){
							
						}
					}
				}
			}
		}
	}
	
	void shoot(double dx, double dy, double angle){
		Projectile proj = new Projectile(ProjType.BOSS);
		proj.setX(getX() + getImage().getWidth() / 2);
		//proj.setRotate(-1 * (angle - 90)); 
		proj.setRotate(-1 * angle); 
		proj.setY(getY() + getImage().getHeight() / 1.75);
		proj.setDx(dx);
		proj.setDy(dy);
		getWorld().add(proj);
		//getWorld().add(new Flash(x, getY() + getImage().getHeight() / 2.1));
	}

	public boolean isFight() {
		return fight;
	}

	public void setFight(boolean fight) {
		this.fight = fight;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	
}
