package engine;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;

public abstract class World extends javafx.scene.layout.Pane implements javafx.css.Styleable, javafx.event.EventTarget {
	private AnimationTimer timer;
	
	public World(){
		timer = new AnimationTimer(){
			long old = 0;
			@Override
			public void handle(long now) {
				if(now - old > 50000000){
					act(now);
					for(int i = 0; i < getChildren().size(); i++){
						if(Actor.class.isAssignableFrom(getChildren().get(i).getClass())){
							Actor dude = (Actor) getChildren().get(i);
							if(dude != null){
								dude.act(now);
							}
						}
					}
					old = now;
				}
			}
		};
	}
	
	abstract void act(long now);
	
	public void add(Actor a){
		getChildren().add(a);
	}
	
	public <A extends Actor>java.util.List<A> getObjects(java.lang.Class<A> cls){
		ArrayList<A> l = new ArrayList<A>();
		
		for(Node n : getChildren())
			if(cls.isInstance(n))
				l.add(cls.cast(n));
		
		return l;
	}
	
	public void remove(Actor a){
		if(a != null){
			getChildren().remove(a);
		}
	}
	
	void start(){
		timer.start();
	}
	
	void stop(){
		timer.stop();
	}
}
