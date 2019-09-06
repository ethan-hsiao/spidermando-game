package game;

import engine.*;
import javafx.scene.image.Image;

public class Munition extends Actor{
	private Image munitionImage;

	public Munition(int b){
		munitionImage = new Image("file:images/ammo.PNG");
		setFitHeight(b);
		setFitWidth(b);
		setImage(munitionImage);
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub

	}
}
