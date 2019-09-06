package game;

import engine.Actor;
import javafx.scene.image.Image;

public class Obstacle extends Actor{
	public Obstacle(int b, Character u){
		Image s = new Image("file:images/spikes.png");
		setFitHeight(b/2);
		setFitWidth(b);
		if(u == 'd'){
			setRotate(180);
		}
		if(u == 'l'){
			setRotate(270);
		}
		if(u == 'r'){
			setRotate(90);
		}
		setImage(s);
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub

	}
}
