package game;

import engine.Actor;
import javafx.scene.image.Image;

public class EndPoint extends Actor {
	Image FLAG = new Image("file:images/Flagpole 2.png");
	public EndPoint(int b){
		setFitHeight(b*9);
		setFitWidth(b);
		setImage(FLAG);
	}
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
}
