package game;
import java.io.File;
import java.util.ArrayList;

import engine.Actor;
import engine.GameWorldApp;
import game.Flash.FlashType;
import game.Projectile.ProjType;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Hero extends Actor {

	//	private int speed = 100;

	private int weapon;
	private int ammo = 10;
	private double dx = 0;
	private double health = 100;
	private ArrayList<Block> blocks = new ArrayList<Block>();
	public ArrayList<Block> getBlocks() {
		return blocks;
	}
	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}

	//	private int dy = 0;
	//	private int gravity = 10;
	private boolean direction;
	public double getDx() {
		return dx;
	}
	public void setDx(double dx) {
		this.dx = dx;
	}

	private Image hero1 = new Image("file:images/hero1.png");
	private Image hero2 = new Image("file:images/hero2.png");
	private Image hero3 = new Image("file:images/hero3.png");
	private int imageCount = 0;
//	private ImageView imageView = new ImageView(myImage);
	int count = 3;
	int columns = 3;
	int offsetX = 50;
	int offsetY = 100;
	int width = 63;
	int height = 500;
	int ex = 50;
	Media fireImpact = new Media(new File(new File("images/firethrow.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer impactPlayer = new MediaPlayer(fireImpact);
	//	public int getDx() {
	//		return dx;
	//	}
	//	public void setDx(int dx) {
	//		this.dx = dx;
	//	}
	public Hero(){
		weapon = 1;
		//		dx = 0;
		
		setImage(hero1);
//		imageView.setFitHeight(height);
//		imageView.setFitWidth(width);
//		imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
//		animation = new SpriteAnimation(this.imageView,Duration.millis(200),count,columns,offsetX,offsetY,width,height);
		impactPlayer.setVolume(0.5);
		direction = true;
	}
	@Override
	public void act(long now) {
		 
		

		//		if(getY() < 300){
		//			dy += gravity;
		//		}
		//		if(getY() > 300){
		//			setY(300);
		//			dy = 0;
		//		}
		//		if(getY() + dy <= 301){
		//			move(0, dy);
		//		}else{
		//			move(0, 301 - getY());
		//		}
		boolean movingRight = dx > 0;
		
		for(int i = 0; i < Math.abs(dx); i++){
			for(Block block : blocks){
				if(getBoundsInParent().intersects(block.getBoundsInParent())){
					if(movingRight){
						if(getTranslateX() + getWidth() == block.getX() - GameWorldApp.BLOCK_SIZE){
							dx = 0;
							return;
						}
					}else if(getTranslateX() == block.getX()){
						return;
					}
				}
			}
			setTranslateX(getTranslateX() + (movingRight ? 1 : -1));
			ex += (movingRight ? 1 : -1); 
		}
		if(Math.floor((double)(this.getTranslateX() / 30)) % 4 == 0){
			setImage(hero1);
		}else if(Math.floor((double)(this.getTranslateX() / 30)) % 4 == 1){
			setImage(hero2);
		}else if(Math.floor((double)(this.getTranslateX() / 30)) % 4 == 2){
			setImage(hero1);
		}else {
			setImage(hero3);
		}
//		setImage(hero2);
		for(Projectile proj : getIntersectingObjects(Projectile.class)){
			if(proj.getT() == ProjType.ENEMY){
				getWorld().remove(proj);
				health -= 4;
				if(health <= 0){
					if(getWorld().getObjects(Gunner.class).size() != 0){
						for(Gunner g : getWorld().getObjects(Gunner.class)){
							g.machinegunPlayer.stop();
						}
					}
					
					this.getWorld().remove(this);
					return ;
				}
			}else if(proj.getT() == ProjType.BOSS){
				impactPlayer.stop();
				getWorld().remove(proj);
				health -= 20;
				getWorld().add(new Flash(getEx(), getTranslateY() + 100, FlashType.BOSS));
				impactPlayer.play();
				if(health <= 0){
					this.getWorld().remove(this);
					return ;
				}
			}else if(proj.getT() == ProjType.BOSSVEN){
				impactPlayer.stop();
				getWorld().remove(proj);
				health -= 5;
				getWorld().add(new Flash(getEx(), getTranslateY() + 100, FlashType.BOSS));
				impactPlayer.play();
				if(health <= 0){
					this.getWorld().remove(this);
					return ;
				}
			}
		}
//		System.out.println(imageCount);
		for(HealthPack healthP : getIntersectingObjects(HealthPack.class)){
			if(health + 25 <= 100){
				getWorld().remove(healthP);
				health += 25;
				Media a = new Media(new File(new File("images/regen.mp3").getAbsolutePath()).toURI().toString());
				MediaPlayer p = new MediaPlayer(a);
				p.setVolume(0.3);
				p.play();
			}else if(health < 100){
				getWorld().remove(healthP);
				health = 100;
				Media a = new Media(new File(new File("images/regen.mp3").getAbsolutePath()).toURI().toString());
				MediaPlayer p = new MediaPlayer(a);
				p.setVolume(0.3);
				p.play();
			}
			
		}
		for(Munition munition : getIntersectingObjects(Munition.class)){
			if(ammo < 90){
				ammo += 10;
				Media a = new Media(new File(new File("images/ammoPickUp.mp3").getAbsolutePath()).toURI().toString());
				MediaPlayer p = new MediaPlayer(a);
				p.play();
				getWorld().remove(munition);
			}else if(ammo < 100){
				ammo = 100;
				Media a = new Media(new File(new File("images/ammoPickUp.mp3").getAbsolutePath()).toURI().toString());
				MediaPlayer p = new MediaPlayer(a);
				p.play();
				getWorld().remove(munition);
			}
		}
		for(Barrel b : getIntersectingObjects(Barrel.class)){
			health -= 10;
			b.die();
			if(health <= 0){
				getWorld().remove(this);
				return ;
			}
		}
	}

	
	public void shoot(double dx, double dy, double angle){
		Projectile proj = new Projectile(ProjType.HERO);
		double x;
		if(direction){
			x = getTranslateX() + getImage().getWidth() * 1.8;
			proj.setTranslateX(x);
		}else{
			x = getTranslateX() + getImage().getWidth() * 0.62;
			proj.setTranslateX(x);
		}
		double y = getTranslateY() + getImage().getHeight() * 1.75;
		proj.setRotate(-1 * (angle - 90)); 
		proj.setY(y);
		proj.setDx(dx);
		proj.setDy(dy);
		getWorld().add(proj);
		getWorld().add(new Flash(x, y, FlashType.HERO));
		ammo--;
	}

	public void changeWeapon(String key){
		if(key.equals("E")){
			weapon++;
		}else if(key.equals("Q")){
			weapon--;
		}
		if(weapon == 9){
			weapon = 0;
		}
		if(weapon == -1){
			weapon = 9;
		}
	}

	public double getHealth() {
		return health;
	}
	public void setHealth(double d) {
		this.health = d;
	}
	
	public int getAmmo() {
		return ammo;
	}
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	public boolean isDirection() {
		return direction;
	}
	public void setDirection(boolean direction) {
		this.direction = direction;
	}
	public int getEx() {
		return ex;
	}
	public void setEx(int ex) {
		this.ex = ex;
	}
	
	
}