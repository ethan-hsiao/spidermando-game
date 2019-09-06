package game;
import java.io.File;
import java.util.ArrayList;

import engine.Actor;
import game.Flash.FlashType;
import game.Projectile.ProjType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;

public class Barrel extends Actor{
	Image FLAG = new Image("file:images/barrel.png");
	Image DEATH_SPRITE = new Image("file:enemydoge.png");
	ImageView sprite;
	double GUNNER_RANGE = 200;
	long latestUpdate = 0;
	boolean direction = true; // true is right, false is left
	int life = 2;
	ArrayList<Block> steppingBlocks;
	Media explosionSound = new Media(new File(new File("images/barrelexplosion.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer explosionPlayer = new MediaPlayer(explosionSound);
	
	public Barrel(ArrayList<Block> b){
		steppingBlocks = b;
		setFitWidth(60);
		setFitHeight(60);
		setImage(FLAG);
		explosionPlayer.setVolume(0.25);
	}

	public void act(long now) {
		if(this.getTranslateY()>550){
			this.getWorld().remove(this);			
			return ;
		}
		moveGY(20);
		for(Projectile proj : getIntersectingObjects(Projectile.class)){
			if(proj.getT() == ProjType.HERO){
				life--;
				getWorld().remove(proj);
				if(life <= 0){
					die();
					return ;
				}
			}
		}

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
//		}
		moveGX(-5);
		setRotationAxis(Rotate.Z_AXIS);
		setRotate(getRotate() - 30);
	}
//	public void shoot(){
//		Projectile proj = new Projectile(ProjType.ENEMY);
//		proj.setX(getTranslateX());
//		proj.setY(getY());
//		getWorld().add(proj);
//	}
	
	public void die(){
		explosionPlayer.play();
		getWorld().add(new Flash(getX(), getY(), FlashType.BARREL));
		getWorld().remove(this);
	}
	
	private void moveGX(int velocity){
		boolean movingRight = velocity > 0;
		for(int i = 0; i < Math.abs(velocity); i++){
			for(Block block : steppingBlocks){
				if(this.getBoundsInParent().intersects(block.getBoundsInParent())){
					if(movingRight){
						if(this.getX() + this.getWidth() == block.getX() - 64){
							return;
						}
					}else if(this.getX() == block.getX()){
						return;
					}
				}
			}
			this.setX(this.getX() + (movingRight ? 1 : -1));
		}
	}

	private void moveGY(int velocity){
		boolean movingDown = velocity > 0;
		for(int i = 0; i < Math.abs(velocity); i++){
			for(Block block : steppingBlocks){
				if(this.getBoundsInParent().intersects(block.getBoundsInParent())){
					if(movingDown){
						if(this.getY() + this.getHeight() <= block.getY()){
							this.setY(this.getY()-1);
							return;
						}
					}else if(this.getY() == block.getY()){
						return;
					}
				}
			}
			this.setY(this.getY() + (movingDown ? 1 : -1));
		}
	}
}