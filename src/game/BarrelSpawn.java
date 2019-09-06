package game;

import java.util.ArrayList;

import engine.Actor;
import javafx.scene.image.Image;

public class BarrelSpawn extends Actor{

	ArrayList steppingBlocks;
	public BarrelSpawn(ArrayList b, int a){
		steppingBlocks = b;
		Image munitionImage = new Image("file:images/launcher.png");
		setFitHeight(a);
		setFitWidth(a);
		setImage(munitionImage);

	}
	long latestUpdate = 0;
	int seconds = 0;
	int count = 0;
	@Override
	public void act(long now) {
		if(now - latestUpdate >= 1000000000){
			latestUpdate = now;
			seconds++;
			//this.getWorld().getObjects(Hero.class).get(0).getTranslateX() - this.getTranslateX()<0
			if(seconds==6 && getCount() <= 20){
				seconds = 0;
				Barrel e = new Barrel(steppingBlocks);
				e.setX(getX() - 64);
				e.setY(getY());
				getWorld().add(e);			
			}else if(getCount() > 20){
				for(int i = 0; i<15; i++){
					this.getWorld().remove(this.getWorld().getObjects(Barrel.class).get(0));
				}
			}
		}
	}
	public int getCount() {
		return this.getWorld().getObjects(Barrel.class).size();
	}
	public void setCount(int count) {
		this.count = count;
	}

}
