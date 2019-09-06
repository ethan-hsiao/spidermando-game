package engine;

public class GameWorld extends World {
	private int layoutx = 0;
	
	public int getLayoutx() {
		return layoutx;
	}

	public void setLayoutx(int layoutX) {
		this.layoutx = getLayoutx() + layoutX;
	}

	@Override
	void act(long now) {
		setLayoutX(layoutx);
	}
	
}