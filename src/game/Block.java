package game;
import engine.*;
import javafx.scene.image.Image;
public class Block extends Actor{
	Image BLOCK = new Image("file:images/blank.gif");
	private boolean critical;
	public Block(int blockSize, boolean isCritical){
		setImage(BLOCK);
		setFitWidth(blockSize);
		setFitHeight(blockSize);
		critical = isCritical;
	}
	
	@Override
	public void act(long now) {
		//do nothing ever
	}
	
	
	public boolean isCritical(){
		return critical;
	}
}
