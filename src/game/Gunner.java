package game;
import java.io.File;
import java.util.ArrayList;

import engine.Actor;
import engine.GameWorldApp;
import game.Projectile.ProjType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;

public class Gunner extends Actor{
	Image FLAG = new Image("file:images/gunner.png");
	Image DEATH_SPRITE = new Image("file:enemydoge.png");
	ImageView sprite;
	double GUNNER_RANGE = 200;
	long latestUpdate = 0;
	boolean direction = true; // true is right, false is left
	int life = 10;
	int dx = 4;
	ArrayList<Block> steppingBlocks;
	String[] level;
	int row;
	int col;
	int rightBound;
	int leftBound;
	Media machinegunSound = new Media(new File(new File("images/machinegunsound.mp3").getAbsolutePath()).toURI().toString());
	public MediaPlayer machinegunPlayer = new MediaPlayer(machinegunSound);
	long playerUpdate = 0;
	
	public Gunner(ArrayList<Block> b, String[] level, int row, int col){
		steppingBlocks = b;
		setFitWidth(90);
		setFitHeight(64);
		setImage(FLAG);
		this.level = level;
		this.row = row;
		this.col = col;
		for(int i = col + 1; i < level[row].length(); i++){
			if(level[row].charAt(i) != '0' && level[row].charAt(i) != '2' && level[row].charAt(i) != '3' && level[row].charAt(i) != 'G'){
				rightBound = (i - 1) * GameWorldApp.BLOCK_SIZE;
				break;
			}else if(level[row].charAt(i) == '0' && level[row + 1].charAt(i) == '0'){
				rightBound = (i - 1) * GameWorldApp.BLOCK_SIZE;
				break;
			}
		}
		for(int i = col - 1; i >= 0; i--){
			if(level[row].charAt(i) != '0' && level[row].charAt(i) != '2' && level[row].charAt(i) != '3' && level[row].charAt(i) != 'G'){
				leftBound = (i + 1) * GameWorldApp.BLOCK_SIZE;
				break;
			}else if(level[row].charAt(i) == '0' && (level[row + 1].charAt(i) == '0' || level[row + 1].charAt(i) == '6')){
				leftBound = (i + 1) * GameWorldApp.BLOCK_SIZE;
				break;
			}
		}
		machinegunPlayer.setVolume(0.25);
	}

	public void act(long now) {
//		moveGY(20);
//		for(Projectile proj : getIntersectingObjects(Projectile.class)){
//			if(proj.getT() == ProjType.HERO){
//				life--;
//				getWorld().remove(proj);
//				if(life <= 0){
//					Media a = new Media(new File(new File("images/pain.mp3").getAbsolutePath()).toURI().toString());
//					MediaPlayer p = new MediaPlayer(a);
//					p.play();
//					getWorld().remove(this);
//					return ;
//				}
//			}
//		}
//
//		if(Math.abs(this.getTranslateX() - this.getWorld().getObjects(Hero.class).get(0).getTranslateX())>GUNNER_RANGE){
//			if(this.getTranslateX() - this.getWorld().getObjects(Hero.class).get(0).getTranslateX()>0){
//				if(direction){
//					setRotationAxis(Rotate.Y_AXIS);
//					setRotate(180);
//					direction = false;
//				}
//				moveGX(5);
//			}else{
//				if(!direction){
//					setRotationAxis(Rotate.Y_AXIS);
//					setRotate(360);
//					direction = true;
//				}
//				moveGX(-5);
//				
//			}
//		}else{
//			if(now - latestUpdate >= 1000000000){
//
//				latestUpdate = now;
//				shoot();
//			}
//		}
		boolean heroAlive = getWorld().getObjects(Hero.class).size() > 0;
		Hero h;
		if(heroAlive){
			h = getWorld().getObjects(Hero.class).get(0);
		}else{
			h = null;
		}
		if(heroAlive && Math.abs(h.getTranslateY() + 100 - getY()) <= 25 && Math.abs(h.getTranslateX() - getX()) < 9 * GameWorldApp.BLOCK_SIZE){
			if(h.getTranslateX() > getX()){
				setRotationAxis(Rotate.Y_AXIS);
				setRotate(180);
				direction = true;
			}else{
				setRotationAxis(Rotate.Y_AXIS);
				setRotate(360);
				direction = false;
			}
			if(now - latestUpdate >= 500000000){
				shoot();
				if(now - playerUpdate >= (15 * 1000000000)){
					machinegunPlayer.stop();
					machinegunPlayer.play();
					playerUpdate = now;
				}			
				latestUpdate = now;
			}
		}else{
			machinegunPlayer.stop();
			setX(getX() + dx);
			setRotationAxis(Rotate.Y_AXIS);
			setRotate(dx > 0 ? 180 : 360);
			if(getX() >= rightBound || getX() <= leftBound){
				dx *= -1;
				setRotationAxis(Rotate.Y_AXIS);
				setRotate(getRotate() + 180);
			}
		}
		for(Projectile proj : getIntersectingObjects(Projectile.class)){
			if(proj.getT() == ProjType.HERO){
				life--;
				if(proj != null){
					try{
						getWorld().remove(proj);
					}
					catch(NullPointerException e){
						
					}
				}
				if(life <= 0){
					Media a = new Media(new File(new File("images/pain.mp3").getAbsolutePath()).toURI().toString());
					MediaPlayer p = new MediaPlayer(a);
					p.play();
					machinegunPlayer.stop();
					try{
						getWorld().remove(this);
						return ;
					}
					catch(NullPointerException e){
						
					}
				}
			}
		}
		if(getWorld().getObjects(Hero.class).size() <= 0){
			machinegunPlayer.stop();
			System.out.println("time to stop");
		}
		//System.out.println(leftBound + "           " + getX() + "          " + rightBound);
	}
	public void shoot(){
		Projectile proj = new Projectile(ProjType.ENEMY);
		if(direction){
			proj.setX(getX() + getFitWidth() / 1.55);
		}else{
			proj.setX(getX() + getFitWidth() * 0.2);
		}
		proj.setY(getY() + getFitHeight() / 1.55);
		getWorld().add(proj);
	}
	
	private void moveGX(int velocity){
		boolean movingRight = velocity > 0;
		for(int i = 0; i < Math.abs(velocity); i++){
			for(Block block : steppingBlocks){
				if(this.getBoundsInParent().intersects(block.getBoundsInParent())){
					if(movingRight){
						if(this.getTranslateX() + this.getWidth() == block.getX() - 64){
							return;
						}
					}else if(this.getTranslateX() == block.getX()){
						return;
					}
				}
			}
			this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
		}
	}

	private void moveGY(int velocity){
		boolean movingDown = velocity > 0;
		for(int i = 0; i < Math.abs(velocity); i++){
			for(Block block : steppingBlocks){
				if(this.getBoundsInParent().intersects(block.getBoundsInParent())){
					if(movingDown){
						if(this.getTranslateY() + this.getHeight() <= block.getY()){
							this.setTranslateY(this.getTranslateY()-1);
							return;
						}
					}else if(this.getTranslateY() == block.getY()){
						return;
					}
				}
			}
			this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
		}
	}
}
