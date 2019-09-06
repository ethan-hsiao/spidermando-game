package game;
import engine.*;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;

public class Flash extends Actor{
	private Image myImage = new Image("file:images/muzzleFlash.png");
	DeathHandler deathTimer = new DeathHandler();
	boolean death = false;
	FlashType fType;
	
	public enum FlashType{
		HERO, BARREL, BOSS, BOSSDEATH
	}
	
	public Flash(double x, double y, FlashType f){
		setTranslateX(x);
		setTranslateY(y);
		setImage(myImage);
		if(f == FlashType.HERO){
			setFitWidth(myImage.getWidth());
			setFitHeight(myImage.getHeight());
		}else if(f == FlashType.BARREL || f == FlashType.BOSSDEATH){
			setFitWidth(64);
			setFitHeight(64);
		}else if(f == FlashType.BOSS){
			setFitWidth(40);
			setFitHeight(40);
		}
		deathTimer.start();
		fType = f;
	}
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		if(death){
			getWorld().remove(this);
		}
	}
	private class DeathHandler extends AnimationTimer{
		long init = 0;
		@Override
		public void handle(long arg0) {
			if(fType == FlashType.HERO){
				if(arg0 - init >= 20){
					death = true;
				}
			}else if(fType == FlashType.BARREL){
				if(arg0 - init >= 100){
					death = true;
				}
			}else if(fType == FlashType.BOSS){
				if(arg0 - init >= 20){
					death = true;
				}
			}else if(fType == FlashType.BOSSDEATH){
				if(arg0 - init >= 150){
					death = true;
				}
			}
		}
		
	}
}