/*//////////////////////////////80 chars////////////////////////////////////////
 * Anirudh Avadhani, 4/2/17, Per 4
 * 
 * This program took 35 minutes to make
 * 
 * This program makes use of the actor and world model classes in the engine 
 * package to create a simple demo which creates a world of moving balls. This 
 * program was interesting to make as I learned about designing an engine for 
 * games which all game classes extend and are children of. This program was not 
 * very difficult to make and I only had a few problems while making it. My 
 * first problem happened when I was writing the BouncyBall class and I 
 * accidentally forgot to set fit sizes for the imageView. This resulted in the 
 * ball not displayed properly and moving oddly. Other problems I had were due 
 * to wrong implementation of the Actor and World classes and not using the 
 * right syntax. This engine demo was overall quite fun to make and I was able 
 * to see how to use engine classes.
 */

package engine;

import java.util.*;

import javafx.css.Styleable;
import javafx.event.EventTarget;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Actor extends ImageView implements Styleable, EventTarget{
	
	public void move(double dx, double dy){
		setTranslateY(getTranslateY() + dy);
		setTranslateX(getTranslateX() + dx);
		//setX(getTranslateX());
//		setTranslateY(getTranslateY() + dy);
	}
	
	public World getWorld(){
		return (World)(getParent());
	}
	
	public double getWidth(){
		return this.getFitWidth();
	}
	
	public double getHeight(){
		return this.getFitHeight();
	}
	
	public <A extends Actor> java.util.List<A> getIntersectingObjects(java.lang.Class<A> cls){
		 List<A> list = new ArrayList<A>();
		 for(A child : getWorld().getObjects(cls)){
			 if(this != child && this.getBoundsInParent().intersects(child.getBoundsInParent())){
				 list.add(child);
			 }
		 }
		 return list;
	}
	
	public <A extends Actor>A getOneIntersectingObject(Class<A> cls){ 
		for(A child : getWorld().getObjects(cls)){
			if(this != child && this.getBoundsInParent().intersects(child.getBoundsInParent())){
				return child;
			}
		}
		return null;
	}
	
	public abstract void act(long now);
}