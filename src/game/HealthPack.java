package game;

import engine.*;
import javafx.scene.image.Image;

public class HealthPack extends Actor{
	private Image healthImage;
	
	public HealthPack(int b){
		healthImage = new Image("file:images/health.png");
		setFitHeight(b);
		setFitWidth(b);
		setImage(healthImage);
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
}
