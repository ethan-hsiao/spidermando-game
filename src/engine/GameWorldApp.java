//done :)
package engine;

import java.io.File;
import java.util.ArrayList;

import game.Block;
import game.GreenGoblin;
import game.EndPoint;
import game.Gunner;
import game.BarrelSpawn;
import game.HealthPack;
import game.Hero;
import game.Level1;
import game.Level2;
import game.Munition;
import game.Obstacle;
import game.Venom;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class GameWorldApp extends Application {
	boolean gameOver = false;
	final int SPEED_OF_HERO = 20;
	public static final int BLOCK_SIZE = 64;
	final int SCREEN_WIDTH = BLOCK_SIZE * 10;
	final int SCREEN_HEIGHT = BLOCK_SIZE * 10;
	private int totalOffset = 0;
	int level = 1;
	AnimationTimer timer;
	Scene menuScene;
	
	private ArrayList<Block> steppingBlocks = new ArrayList<Block>();
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

	private Point2D playerVelocity = new Point2D(0, 0);
	boolean canJump = true;
	public static boolean isAlive = true;

	private long last = 0;
	static Stage theStage;

	Button restart;
	
	long waitTime = 0;

	//World
	private GameWorld world = new GameWorld();
	public static StackPane root;
	//Actors
	Hero heroe = new Hero();
	GreenGoblin bossTest = new GreenGoblin();
	Venom boss2 = new Venom();
	Label healthText;
	Label ammoText;

	Media gunSound = new Media(new File(new File("images/pistolsound.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer gunPlayer = new MediaPlayer(gunSound);
	Media tauntSound = new Media(new File(new File("images/comeouttoplay.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer tauntPlayer = new MediaPlayer(tauntSound);
	Media ventauntSound = new Media(new File(new File("images/venomTaunt.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer ventauntPlayer = new MediaPlayer(ventauntSound);
	Media levelTheme = new Media(new File(new File("images/leveltheme.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer lvlThemePlayer = new MediaPlayer(levelTheme);
	Media menuTheme = new Media(new File(new File("images/menuTheme.mp3").getAbsolutePath()).toURI().toString());
	MediaPlayer menuThemePlayer = new MediaPlayer(menuTheme);
	boolean taunt = true;
	boolean levelStart = false;
	boolean bossFightStart = false;
	boolean bossFightWon = false;
	Block sealBlock;
	Block sealBlock2;
	Block sealBlock3;
	Image deathImg = new Image("file:images/gameover.png");
	ImageView deathView = new ImageView();
	Image winImg = new Image("file:images/youwin.png");
	ImageView winView = new ImageView();
	

	Scene startScene;
	Level1 l = new Level1();
	Level2 l2 = new Level2();

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Image menuImage = new Image("file:images/menuBackGround.jpg");
		ImageView menuView = new ImageView(menuImage);
		menuView.setFitWidth(SCREEN_WIDTH + 600);
		menuView.setFitHeight(SCREEN_HEIGHT);
		Pane menuPane = new Pane();
		menuPane.setPrefWidth((SCREEN_WIDTH + 600) * 0.8);
		menuPane.setPrefHeight(SCREEN_HEIGHT * 0.8);
		menuPane.getChildren().add(menuView);
		ImageView play = new ImageView(new Image("file:images/play.png"));
		ImageView howTo = new ImageView(new Image("file:images/howToPlay.png"));
		VBox menuButtons = new VBox();
		menuButtons.getChildren().addAll(play, howTo);
		menuPane.getChildren().add(menuButtons);
		menuButtons.setAlignment(Pos.CENTER);
		menuButtons.setTranslateX((SCREEN_WIDTH + 600) / 2.5);
		menuButtons.setTranslateY(SCREEN_HEIGHT/ 2.5);
		menuThemePlayer.setVolume(0.5);
		menuThemePlayer.setCycleCount(4);
		menuThemePlayer.play();
//		BorderPane ro = new BorderPane();
//		ro.setCenter(menuPane);
		menuScene = new Scene(menuPane,(SCREEN_WIDTH + 600), SCREEN_HEIGHT);
		primaryStage.setScene(menuScene);
		primaryStage.show();
		
		Stage howToPlayStage = new Stage();
		VBox howToPlayRoot = new VBox();
		howToPlayRoot.setAlignment(Pos.BOTTOM_CENTER);
		howToPlayRoot.setStyle("-fx-background-color: grey");
		Scene howToPlayScene = new Scene(howToPlayRoot, 500, 500);
		Button howToPlayCloseButton = new Button("OK");
		howToPlayCloseButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				howToPlayStage.close();
			}
		});
		WebView howToPlayView = new WebView();
		WebEngine webEngine2 = howToPlayView.getEngine();
		File howToPlayFile = new File("index.html");
		webEngine2.load("file:///" + howToPlayFile.getAbsolutePath());
		howToPlayRoot.getChildren().addAll(howToPlayView, howToPlayCloseButton);
		howToPlayStage.setScene(howToPlayScene);
//		//Build Level
//		theStage = primaryStage;
//
//
//		for(int i = 0; i < l.L1.length; i++){
//			String curRow = l.L1[i];
//			for(int j = 0; j < curRow.length();j++){
//				if(curRow.charAt(j)=='1'){
//					Block block;
//					if(i == 0){
//						block = new Block(BLOCK_SIZE, true);
//					}else{
//						block = new Block(BLOCK_SIZE, false);
//					}
//					block.setX(j*BLOCK_SIZE);
//					block.setY(i*BLOCK_SIZE);
//					world.add(block);
//					steppingBlocks.add(block);
//				}
//				if(curRow.charAt(j)=='2'){
//					HealthPack h = new HealthPack(BLOCK_SIZE);
//
//					h.setX(j*BLOCK_SIZE);
//					h.setY(i*BLOCK_SIZE);
//					world.add(h);
//				}
//				if(curRow.charAt(j)=='3'){
//					Munition m = new Munition(BLOCK_SIZE);
//					m.setX(j*BLOCK_SIZE);
//					m.setY(i*BLOCK_SIZE);
//					world.add(m);
//				}
//				if(curRow.charAt(j)=='4'){
//					BarrelSpawn g = new BarrelSpawn(steppingBlocks,BLOCK_SIZE);
//					g.setX(j*BLOCK_SIZE);
//					g.setY(i*BLOCK_SIZE);
//					world.add(g);
//				}
//
//				if(curRow.charAt(j)=='5'){
//					EndPoint g = new EndPoint(BLOCK_SIZE);
//					g.setX(j*BLOCK_SIZE);
//					g.setY(i*BLOCK_SIZE);
//					world.add(g);
//				}
//
//				if(curRow.charAt(j)=='6'){
//					Obstacle s = new Obstacle(BLOCK_SIZE, 'u');
//					s.setX(j*BLOCK_SIZE);
//					s.setY(i*BLOCK_SIZE + 0.5*BLOCK_SIZE);
//					obstacles.add(s);
//					world.add(s);
//				}
//
//				if(curRow.charAt(j)=='7'){
//					Obstacle s = new Obstacle(BLOCK_SIZE, 'd');
//					s.setX(j*BLOCK_SIZE);
//					s.setY(i*BLOCK_SIZE);
//					obstacles.add(s);
//					world.add(s);
//				}
//				if(curRow.charAt(j)=='8'){
//					Obstacle s = new Obstacle(BLOCK_SIZE, 'r');
//					s.setX(j*BLOCK_SIZE - 0.3*BLOCK_SIZE);
//					s.setY(i*BLOCK_SIZE+ 15);
//					obstacles.add(s);
//					world.add(s);
//				}
//				if(curRow.charAt(j)=='9'){
//					Obstacle s = new Obstacle(BLOCK_SIZE, 'l');
//					s.setX(j*BLOCK_SIZE + 0.3*BLOCK_SIZE);
//					s.setY(i*BLOCK_SIZE+15);
//					obstacles.add(s);
//					world.add(s);
//				}
//				if(curRow.charAt(j) == 'G'){
//					Gunner g = new Gunner(steppingBlocks, Level1.L1, i, j);
//					g.setX(j*BLOCK_SIZE);
//					g.setY(i*BLOCK_SIZE);
//					world.add(g);
//				}
//			}
//		}
//		int topY = -4;
//		for(int a = 0; a < Level1.L1[0].length(); a++){
//			Block block = new Block(BLOCK_SIZE, true);
//			block.setX(a*BLOCK_SIZE);
//			block.setY(topY*BLOCK_SIZE);
//			world.add(block);
//			steppingBlocks.add(block);
//		}
//
//		//Background
//		Image background = new Image("file:images/background.png");
//		ImageView view = new ImageView(background);
//		view.setFitWidth(Level1.L1[0].length() * BLOCK_SIZE * 2);
//		view.setFitHeight(SCREEN_HEIGHT);
//		//World
//		root = new StackPane();
//		BorderPane pane = new BorderPane();
//
//		world.setPrefWidth(SCREEN_WIDTH);
//		world.setPrefHeight(SCREEN_HEIGHT);
//		
//		Rectangle bG = new Rectangle(40,40,Color.ALICEBLUE);
//
//		Text title = new Text();
//		title.setText("Spidermmando");
//		title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
//		title.setFill(Color.RED);
//		title.setStroke(Color.web("#0d61e8"));
//
//		healthText = new Label("Health: " + heroe.getHealth());
//		healthText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
//		healthText.setTextFill(Color.RED);
//		//healthText.setStroke(Color.web("#0d61e8"));
//
//		ammoText = new Label("Ammo: " + heroe.getAmmo());
//		ammoText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
//		ammoText.setTextFill(Color.RED);
//		//ammoText.setStroke(Color.web("#0d61e8"));
//
//		VBox infoBox = new VBox();
//		infoBox.getChildren().addAll(healthText, ammoText);
//		infoBox.setAlignment(Pos.TOP_LEFT);
//		pane.setTop(title);
//
//
//
//		world.add(heroe);
//		heroe.setBlocks(steppingBlocks);
//		heroe.setX(50);
//		heroe.setY(100); 
//
//		heroe.translateXProperty().addListener((obs,old,newValue) ->{
//			int offset = newValue.intValue();
//			totalOffset += offset;
//			if(offset>300 && offset<l.L1[0].length() * BLOCK_SIZE - SCREEN_WIDTH - 600 + 300){
//				root.setLayoutX(-1 * offset + 300);
//				world.setLayoutx(-1 * offset + 300);
//			}
//		});
//		//Testing Gunner Class
//
//
//		world.add(bossTest);
//		bossTest.setX(100 * BLOCK_SIZE);
//		bossTest.setY(20);
//
//
//
//
//		Scene scene = new Scene(root, SCREEN_WIDTH + 600, SCREEN_HEIGHT);
//
//		scene.addEventHandler(KeyEvent.KEY_PRESSED, new MyKeyboardHandler());
//
//		primaryStage.setResizable(false);
//		startScene = scene;
//
//		//		Image deathImage = new Image("file:images/gameOver.jpg");
//		//		ImageView newRoot = new ImageView(deathImage);
//		//		newRoot.setFitWidth(l.L1[0].length() * BLOCK_SIZE);
//		//		Scene deathScene = new Scene(root, SCREEN_WIDTH + 600, SCREEN_HEIGHT);
//
//		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
//
//			@Override
//			public void handle(KeyEvent e) {
//				if(e.getCode() == KeyCode.D){
//					//					heroe.setDx(SPEED_OF_HERO);
//					//					heroe.setRotationAxis(Rotate.Y_AXIS);
//					//			    	//heroe.setRotate(360);
//					//			    	heroe.setDirection(true);
//					//			    	//System.out.println(heroe.getTranslateX());
//
//					heroe.setDx(30);
//					heroe.setDirection(true);
//					
//					//moveHeroX(SPEED_OF_HERO);
//				}
//				if(e.getCode() == KeyCode.A){
//					//					heroe.setDx(-1 * SPEED_OF_HERO);
//					//					heroe.setRotationAxis(Rotate.Y_AXIS);
//					//			    	//heroe.setRotate(180);
//					//			    	heroe.setDirection(false);
//					//			    	//System.out.println(heroe.getTranslateX());
//					//moveHeroX(-1 * SPEED_OF_HERO);
//					heroe.setDx(-30);
//					heroe.setDirection(false);
//				}
//				if(e.getCode() == KeyCode.W){
//					//					heroe.setDy(-50);	
//					jumpHero();
//				}
//
//
//			}
//
//		});
//		scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
//
//			@Override
//			public void handle(KeyEvent event) {
//				if(event.getCode() == KeyCode.A || event.getCode() == KeyCode.D){
//					heroe.setDx(0);
//					
//				}
//			}
//
//		});
//		AnimationTimer timer = new AnimationTimer(){
//
//			@Override
//			public void handle(long arg0) {
//				// TODO Auto-generated method stub
//				if(gameOver == false && levelStart){
//					update();
//
//					long timeElapsed = arg0 - last;
//					//fpsString.setValue(Double.toString(1_000_000_000.0/(timeElapsed)));
//
//					last = arg0;
//					//					if(!isAlive){
//					//						primaryStage.setScene(deathScene);
//					//						primaryStage.show();
//					//					}
//					ammoText.setText("Ammo: " + heroe.getAmmo());
//					healthText.setText("Health: " + heroe.getHealth());
//					infoBox.setTranslateX(-1 * root.getLayoutX());
//					if(heroe.getTranslateX() >= 85 * BLOCK_SIZE && heroe.getTranslateY() <= 2 * BLOCK_SIZE && taunt){
//						tauntPlayer.play();
//						taunt = false;
//					}
//					if(heroe.getTranslateX() >= 96 * BLOCK_SIZE && !bossFightStart){
//						sealBlock = new Block(BLOCK_SIZE, true);
//						sealBlock.setX(88 * BLOCK_SIZE);
//						sealBlock.setY(0);
//						world.add(sealBlock);
//						steppingBlocks.add(sealBlock);
//						sealBlock2 = new Block(BLOCK_SIZE, false);
//						sealBlock2.setX(88 * BLOCK_SIZE);
//						sealBlock2.setY(BLOCK_SIZE);
//						world.add(sealBlock2);
//						steppingBlocks.add(sealBlock2);
//						sealBlock3 = new Block(BLOCK_SIZE, true);
//						sealBlock3.setX(112 * BLOCK_SIZE);
//						sealBlock3.setY(0);
//						world.add(sealBlock3);
//						steppingBlocks.add(sealBlock3);
//						bossFightStart = true;
//						bossTest.setFight(true);
//					}
//					if(bossTest.getHealth() <= 0){
//						bossFightWon = true;
//					}
//					if(bossFightWon){
//						bossFightWon = false;
//						world.remove(sealBlock);
//						steppingBlocks.remove(sealBlock);
//						world.remove(sealBlock2);
//						steppingBlocks.remove(sealBlock2);
//						world.remove(sealBlock3);
//						steppingBlocks.remove(sealBlock3);
//					}
//				}
//			}
//
//		};
//		timer.start();
//		root.setOnMouseClicked(new EventHandler<MouseEvent>(){
//
//			@Override
//			public void handle(MouseEvent e) {
//				// TODO Auto-generated method stub
//				//double mouseX = e.getX() + heroe.getTranslateX();
//				if(heroe.getAmmo() > 0){
//					gunPlayer.stop();
//					double mouseX = e.getX();
//					double heroX;
//					if(heroe.isDirection()){
//						heroX = heroe.getTranslateX() + heroe.getImage().getWidth() * 1.6;
//					}else{
//						heroX = heroe.getTranslateX() + heroe.getImage().getWidth() * 0.4;
//					}
//					double mouseY = e.getY();
//					double heroY = heroe.getTranslateY() + heroe.getImage().getHeight() * 2;
//					double speed = 80.0;
//					double tangent = Math.abs(mouseY - heroY) / Math.abs(mouseX - heroX);
//					double angle = Math.atan(tangent);
//					double dx;
//					double dy;
//					if(mouseX < heroX && mouseY > heroY){
//						//dx = -1 * speed * Math.cos(angle);
//						//dy = speed * Math.sin(angle);
//						angle = Math.PI + angle;
//						heroe.setDirection(false);
//						heroe.setRotationAxis(Rotate.Y_AXIS);
//						heroe.setRotate(180);
//					}else if(mouseX > heroX && mouseY > heroY){
//						//dx = speed * Math.cos(angle);
//						//dy = speed * Math.sin(angle);
//						angle = 2 * Math.PI - angle;
//						heroe.setDirection(true);
//						heroe.setRotationAxis(Rotate.Y_AXIS);
//						heroe.setRotate(360);
//					}else if(mouseX < heroX && mouseY < heroY){
//						//dx =  -1 * speed * Math.cos(angle);
//						//dy = -1 * speed * Math.sin(angle);
//						angle = Math.PI - angle;
//						heroe.setDirection(false);
//						heroe.setRotationAxis(Rotate.Y_AXIS);
//						heroe.setRotate(180);
//					}else{
//						//dx =  speed * Math.cos(angle);
//						//dy = -1 * speed * Math.sin(angle);
//						heroe.setDirection(true);
//						heroe.setRotationAxis(Rotate.Y_AXIS);
//						heroe.setRotate(360);
//					}
//					dx = speed * Math.cos(angle);
//					dy = -1 * speed * Math.sin(angle);
//
//					angle *= (180.0 / Math.PI);
//					if(world.getObjects(Hero.class).size() != 0){
//						heroe.shoot(dx, dy, angle);
//					}
//					gunPlayer.play();
//				}
//			}
//		});
//		primaryStage.setTitle("Spidermando");
//		primaryStage.show();
//		File song = new File("images/leveltheme.mp3");
//		String path = song.getAbsolutePath();
//		levelTheme = new Media(new File(path).toURI().toString());
//		lvlThemePlayer = new MediaPlayer(levelTheme);
//		lvlThemePlayer.setVolume(0.1);
//		lvlThemePlayer.setCycleCount(4);
//
//		play.setOnMouseClicked(new EventHandler<MouseEvent>(){
//
//			@Override
//			public void handle(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				primaryStage.setScene(scene);
//				world.start();
//				lvlThemePlayer.play();
//				levelStart = true;
//				root.getChildren().addAll(view, world, infoBox);
//				root.setLayoutX(0);
//				infoBox.setAlignment(Pos.TOP_LEFT);
//			}
//
//		});
		menuButtons.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent e) {
				//Play X: 58-182
				//Play Y: 2- 41
				//HOWTOPLAY X: 1-243
				//HOWTOPLAY Y : 48-105
				// TODO Auto-generated method stub
//				primaryStage.setScene(scene);
//				world.start();
//				lvlThemePlayer.play();
//				levelStart = true;
//				root.getChildren().addAll(view, world, infoBox);
//				root.setLayoutX(0);
//				infoBox.setAlignment(Pos.TOP_LEFT);
				if(e.getX() >= 58 && e.getX() <= 182 && e.getY() >= 2 && e.getY() <= 41){
					if(level == 1){
						genLevel1(primaryStage);
					}else{
						genLevel2(primaryStage);
					}
				}else if(e.getX() >= 1 && e.getX() <= 243 && e.getY() >= 48 && e.getY() <= 105){
					Stage howToPlayStage = new Stage();
					VBox howToPlayRoot = new VBox();
					howToPlayRoot.setAlignment(Pos.BOTTOM_CENTER);
					howToPlayRoot.setStyle("-fx-background-color: grey");
					Scene howToPlayScene = new Scene(howToPlayRoot, 500, 500);
					Button howToPlayCloseButton = new Button("OK");
					howToPlayCloseButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
						@Override
						public void handle(MouseEvent arg0) {
							howToPlayStage.close();
						}
					});
					WebView howToPlayView = new WebView();
					WebEngine webEngine2 = howToPlayView.getEngine();
					File howToPlayFile = new File("index.html");
					webEngine2.load("file:///" + howToPlayFile.getAbsolutePath());
					howToPlayRoot.getChildren().addAll(howToPlayView, howToPlayCloseButton);
					howToPlayStage.setScene(howToPlayScene);
					howToPlayStage.show();
				}
			}

		});
	}


	private void update(){
		if(world.getObjects(Hero.class).size()<=0){
			deathView.setFitWidth(SCREEN_WIDTH + 600);
			deathView.setFitHeight(SCREEN_HEIGHT);
			deathView.setImage(deathImg);
			VBox hi = new VBox();
			hi.setAlignment(Pos.BOTTOM_CENTER);
			gameOver = true;
			Button menuReturn = new Button("Return to Menu");
			menuReturn.setAlignment(Pos.CENTER);
			hi.getChildren().addAll(deathView, menuReturn);
			lvlThemePlayer.stop();
			world.stop();
			timer.stop();
			hi.setStyle("-fx-background-color: grey");
			theStage.setScene(new Scene(hi,SCREEN_WIDTH + 600,SCREEN_HEIGHT));
			menuReturn.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					theStage.setScene(menuScene);
					menuThemePlayer.setVolume(0.5);
					menuThemePlayer.setCycleCount(4);
					menuThemePlayer.play();
				}
			});
		}
		for(Obstacle spike : obstacles){
			if(heroe.getBoundsInParent().intersects(spike.getBoundsInParent())){
				heroe.setHealth(heroe.getHealth()-0.25);
				if(heroe.getHealth() <= 0){
					deathView.setFitWidth(SCREEN_WIDTH + 600);
					deathView.setFitHeight(SCREEN_HEIGHT);
					deathView.setImage(deathImg);
					VBox hi = new VBox();
					hi.setAlignment(Pos.BOTTOM_CENTER);
					gameOver = true;
					Button menuReturn = new Button("Return to Menu");
					hi.getChildren().addAll(deathView, menuReturn);
					lvlThemePlayer.stop();
					world.stop();
					timer.stop();
					hi.setStyle("-fx-background-color: grey");
					theStage.setScene(new Scene(hi,SCREEN_WIDTH + 600,SCREEN_HEIGHT));
					menuReturn.setOnAction(new EventHandler<ActionEvent>(){
						@Override
						public void handle(ActionEvent arg0) {
							theStage.setScene(menuScene);
							menuThemePlayer.setVolume(0.5);
							menuThemePlayer.setCycleCount(4);
							menuThemePlayer.play();
						}
					});
				}

			}
		}
		if(heroe.getTranslateX()>=(l.L1[0].length() - 2)*BLOCK_SIZE){
			if(level == 1){
				gameOver = true;
				lvlThemePlayer.stop();
				world.stop();
				timer.stop();
				genLevel2(theStage);
				level = 2;
			}else{
				winView.setImage(winImg);
				winView.setFitWidth(SCREEN_WIDTH + 600);
				winView.setFitHeight(SCREEN_HEIGHT);
				gameOver = true;
				lvlThemePlayer.stop();
				world.stop();
				timer.stop();
				level = 1;
				VBox hi = new VBox();
				hi.setAlignment(Pos.BOTTOM_CENTER);
				hi.setStyle("-fx-background-color: grey");
				Button menuReturn = new Button("Return to Menu");
				hi.getChildren().addAll(winView, menuReturn);
				theStage.setScene(new Scene(hi,SCREEN_WIDTH + 600,SCREEN_HEIGHT));
				menuReturn.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent arg0) {
						theStage.setScene(menuScene);
						menuThemePlayer.setVolume(0.5);
						menuThemePlayer.setCycleCount(4);
						menuThemePlayer.play();
					}
				});
			}
		}

		if(heroe.getTranslateY() < 550){

			if(playerVelocity.getY() < 10){
				playerVelocity = playerVelocity.add(0, 1);
			}
			moveHeroY((int)playerVelocity.getY());
		}else{
			deathView.setFitWidth(SCREEN_WIDTH + 600);
			deathView.setFitHeight(SCREEN_HEIGHT);
			deathView.setImage(deathImg);
			VBox hi = new VBox();
			hi.setAlignment(Pos.BOTTOM_CENTER);
			gameOver = true;
			Button menuReturn = new Button("Return to Menu");
			menuReturn.setAlignment(Pos.CENTER);
			hi.setStyle("-fx-background-color: grey");
			hi.getChildren().addAll(deathView, menuReturn);
			lvlThemePlayer.stop();
			world.stop();
			timer.stop();
			theStage.setScene(new Scene(hi,SCREEN_WIDTH + 600,SCREEN_HEIGHT));
			menuReturn.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					theStage.setScene(menuScene);
					menuThemePlayer.setVolume(0.5);
					menuThemePlayer.setCycleCount(4);
					menuThemePlayer.play();
				}
			});
		}
	}

	private void moveHeroX(int velocity){
		boolean movingRight = velocity > 0;
		for(int i = 0; i < Math.abs(velocity); i++){
			for(Block block : steppingBlocks){
				if(heroe.getBoundsInParent().intersects(block.getBoundsInParent())){
					if(movingRight){
						if(heroe.getTranslateX() + heroe.getWidth() == block.getX() - BLOCK_SIZE){
							return;
						}
					}else if(heroe.getTranslateX() == block.getX()){
						return;
					}
				}
			}
			heroe.setTranslateX(heroe.getTranslateX() + (movingRight ? 1 : -1));
		}
	}
	
	public void genLevel1(Stage primaryStage){
		menuThemePlayer.stop();
		lvlThemePlayer.stop();
		tauntPlayer.stop();
		theStage = primaryStage;
		world = new GameWorld();
		heroe = new Hero();
		bossTest = new GreenGoblin();
		gameOver = false;
		isAlive = true;
		playerVelocity = new Point2D(0, 0);
		canJump = true;
		steppingBlocks = new ArrayList<Block>();
		obstacles = new ArrayList<Obstacle>();
		taunt = true;
		levelStart = false;
		bossFightStart = false;
		bossFightWon = false;

		for(int i = 0; i < l.L1.length; i++){
			String curRow = l.L1[i];
			for(int j = 0; j < curRow.length();j++){
				if(curRow.charAt(j)=='1'){
					Block block;
					if(i == 0){
						block = new Block(BLOCK_SIZE, true);
					}else{
						block = new Block(BLOCK_SIZE, false);
					}
					block.setX(j*BLOCK_SIZE);
					block.setY(i*BLOCK_SIZE);
					world.add(block);
					steppingBlocks.add(block);
				}
				if(curRow.charAt(j)=='2'){
					HealthPack h = new HealthPack(BLOCK_SIZE);

					h.setX(j*BLOCK_SIZE);
					h.setY(i*BLOCK_SIZE);
					world.add(h);
				}
				if(curRow.charAt(j)=='3'){
					Munition m = new Munition(BLOCK_SIZE);
					m.setX(j*BLOCK_SIZE);
					m.setY(i*BLOCK_SIZE);
					world.add(m);
				}
				if(curRow.charAt(j)=='4'){
					BarrelSpawn g = new BarrelSpawn(steppingBlocks,BLOCK_SIZE);
					g.setX(j*BLOCK_SIZE);
					g.setY(i*BLOCK_SIZE);
					world.add(g);
				}

				if(curRow.charAt(j)=='5'){
					EndPoint g = new EndPoint(BLOCK_SIZE);
					g.setX(j*BLOCK_SIZE);
					g.setY(i*BLOCK_SIZE);
					world.add(g);
				}

				if(curRow.charAt(j)=='6'){
					Obstacle s = new Obstacle(BLOCK_SIZE, 'u');
					s.setX(j*BLOCK_SIZE);
					s.setY(i*BLOCK_SIZE + 0.5*BLOCK_SIZE);
					obstacles.add(s);
					world.add(s);
				}

				if(curRow.charAt(j)=='7'){
					Obstacle s = new Obstacle(BLOCK_SIZE, 'd');
					s.setX(j*BLOCK_SIZE);
					s.setY(i*BLOCK_SIZE);
					obstacles.add(s);
					world.add(s);
				}
				if(curRow.charAt(j)=='8'){
					Obstacle s = new Obstacle(BLOCK_SIZE, 'r');
					s.setX(j*BLOCK_SIZE - 0.3*BLOCK_SIZE);
					s.setY(i*BLOCK_SIZE+ 15);
					obstacles.add(s);
					world.add(s);
				}
				if(curRow.charAt(j)=='9'){
					Obstacle s = new Obstacle(BLOCK_SIZE, 'l');
					s.setX(j*BLOCK_SIZE + 0.3*BLOCK_SIZE);
					s.setY(i*BLOCK_SIZE+15);
					obstacles.add(s);
					world.add(s);
				}
				if(curRow.charAt(j) == 'G'){
					Gunner g = new Gunner(steppingBlocks, Level1.L1, i, j);
					g.setX(j*BLOCK_SIZE);
					g.setY(i*BLOCK_SIZE);
					world.add(g);
				}
			}
		}
		int topY = -4;
		for(int a = 0; a < Level1.L1[0].length(); a++){
			Block block = new Block(BLOCK_SIZE, true);
			block.setX(a*BLOCK_SIZE);
			block.setY(topY*BLOCK_SIZE);
			world.add(block);
			steppingBlocks.add(block);
		}

		//Background
		Image background = new Image("file:images/background.png");
		ImageView view = new ImageView(background);
		view.setFitWidth(Level1.L1[0].length() * BLOCK_SIZE * 2);
		view.setFitHeight(SCREEN_HEIGHT);
		//World
		root = new StackPane();
		BorderPane pane = new BorderPane();

		world.setPrefWidth(SCREEN_WIDTH);
		world.setPrefHeight(SCREEN_HEIGHT);
		
		Rectangle bG = new Rectangle(40,40,Color.ALICEBLUE);

		Text title = new Text();
		title.setText("Spidermmando");
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		title.setFill(Color.RED);
		title.setStroke(Color.web("#0d61e8"));

		healthText = new Label("Health: " + heroe.getHealth());
		healthText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
		healthText.setTextFill(Color.RED);
		//healthText.setStroke(Color.web("#0d61e8"));

		ammoText = new Label("Ammo: " + heroe.getAmmo());
		ammoText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
		ammoText.setTextFill(Color.RED);
		//ammoText.setStroke(Color.web("#0d61e8"));

		VBox infoBox = new VBox();
		infoBox.getChildren().addAll(healthText, ammoText);
		infoBox.setAlignment(Pos.TOP_LEFT);
		pane.setTop(title);



		world.add(heroe);
		heroe.setBlocks(steppingBlocks);
		heroe.setX(50);
		heroe.setY(100); 

		heroe.translateXProperty().addListener((obs,old,newValue) ->{
			int offset = newValue.intValue();
			totalOffset += offset;
			if(offset>300 && offset<l.L1[0].length() * BLOCK_SIZE - SCREEN_WIDTH - 600 + 300){
				root.setLayoutX(-1 * offset + 300);
				world.setLayoutx(-1 * offset + 300);
			}
		});
		//Testing Gunner Class


		world.add(bossTest);
		bossTest.setX(100 * BLOCK_SIZE);
		bossTest.setY(20);




		Scene scene = new Scene(root, SCREEN_WIDTH + 600, SCREEN_HEIGHT);
		root.getChildren().addAll(view, world, infoBox);
		world.start();
		levelStart = true;
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new MyKeyboardHandler());

		primaryStage.setResizable(false);
		startScene = scene;

		//		Image deathImage = new Image("file:images/gameOver.jpg");
		//		ImageView newRoot = new ImageView(deathImage);
		//		newRoot.setFitWidth(l.L1[0].length() * BLOCK_SIZE);
		//		Scene deathScene = new Scene(root, SCREEN_WIDTH + 600, SCREEN_HEIGHT);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.D){
					//					heroe.setDx(SPEED_OF_HERO);
					//					heroe.setRotationAxis(Rotate.Y_AXIS);
					//			    	//heroe.setRotate(360);
					//			    	heroe.setDirection(true);
					//			    	//System.out.println(heroe.getTranslateX());

					heroe.setDx(30);
					heroe.setDirection(true);
					
					//moveHeroX(SPEED_OF_HERO);
				}
				if(e.getCode() == KeyCode.A){
					//					heroe.setDx(-1 * SPEED_OF_HERO);
					//					heroe.setRotationAxis(Rotate.Y_AXIS);
					//			    	//heroe.setRotate(180);
					//			    	heroe.setDirection(false);
					//			    	//System.out.println(heroe.getTranslateX());
					//moveHeroX(-1 * SPEED_OF_HERO);
					heroe.setDx(-30);
					heroe.setDirection(false);
				}
				if(e.getCode() == KeyCode.W){
					//					heroe.setDy(-50);	
					jumpHero();
				}


			}

		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.A || event.getCode() == KeyCode.D){
					heroe.setDx(0);
					
				}
			}

		});
		timer = new AnimationTimer(){

			@Override
			public void handle(long arg0) {
				// TODO Auto-generated method stub
				if(gameOver == false && levelStart){
					update();

					long timeElapsed = arg0 - last;
					//fpsString.setValue(Double.toString(1_000_000_000.0/(timeElapsed)));

					last = arg0;
					//					if(!isAlive){
					//						primaryStage.setScene(deathScene);
					//						primaryStage.show();
					//					}
					ammoText.setText("Ammo: " + heroe.getAmmo());
					healthText.setText("Health: " + heroe.getHealth());
					infoBox.setTranslateX(-1 * root.getLayoutX());
					if(heroe.getTranslateX() >= 83 * BLOCK_SIZE && heroe.getTranslateY() <= 4 * BLOCK_SIZE && taunt){
						tauntPlayer.play();
						taunt = false;
					}
					if(heroe.getTranslateX() >= 96 * BLOCK_SIZE && !bossFightStart){
						sealBlock = new Block(BLOCK_SIZE, true);
						sealBlock.setX(88 * BLOCK_SIZE);
						sealBlock.setY(0);
						world.add(sealBlock);
						steppingBlocks.add(sealBlock);
						sealBlock2 = new Block(BLOCK_SIZE, false);
						sealBlock2.setX(88 * BLOCK_SIZE);
						sealBlock2.setY(BLOCK_SIZE);
						world.add(sealBlock2);
						steppingBlocks.add(sealBlock2);
						sealBlock3 = new Block(BLOCK_SIZE, true);
						sealBlock3.setX(112 * BLOCK_SIZE);
						sealBlock3.setY(0);
						world.add(sealBlock3);
						steppingBlocks.add(sealBlock3);
						bossFightStart = true;
						bossTest.setFight(true);
					}
					if(bossTest.getHealth() <= 0){
						bossFightWon = true;
					}
					if(bossFightWon){
						bossFightWon = false;
						world.remove(sealBlock);
						steppingBlocks.remove(sealBlock);
						world.remove(sealBlock2);
						steppingBlocks.remove(sealBlock2);
						world.remove(sealBlock3);
						steppingBlocks.remove(sealBlock3);
					}
				}
			}

		};
		timer.start();
		root.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent e) {
				// TODO Auto-generated method stub
				//double mouseX = e.getX() + heroe.getTranslateX();
				if(heroe.getAmmo() > 0){
					gunPlayer.stop();
					double mouseX = e.getX();
					double heroX;
					if(heroe.isDirection()){
						heroX = heroe.getTranslateX() + heroe.getImage().getWidth() * 1.6;
					}else{
						heroX = heroe.getTranslateX() + heroe.getImage().getWidth() * 0.4;
					}
					double mouseY = e.getY();
					double heroY = heroe.getTranslateY() + heroe.getImage().getHeight() * 2;
					double speed = 80.0;
					double tangent = Math.abs(mouseY - heroY) / Math.abs(mouseX - heroX);
					double angle = Math.atan(tangent);
					double dx;
					double dy;
					if(mouseX < heroX && mouseY > heroY){
						//dx = -1 * speed * Math.cos(angle);
						//dy = speed * Math.sin(angle);
						angle = Math.PI + angle;
						heroe.setDirection(false);
						heroe.setRotationAxis(Rotate.Y_AXIS);
						heroe.setRotate(180);
					}else if(mouseX > heroX && mouseY > heroY){
						//dx = speed * Math.cos(angle);
						//dy = speed * Math.sin(angle);
						angle = 2 * Math.PI - angle;
						heroe.setDirection(true);
						heroe.setRotationAxis(Rotate.Y_AXIS);
						heroe.setRotate(360);
					}else if(mouseX < heroX && mouseY < heroY){
						//dx =  -1 * speed * Math.cos(angle);
						//dy = -1 * speed * Math.sin(angle);
						angle = Math.PI - angle;
						heroe.setDirection(false);
						heroe.setRotationAxis(Rotate.Y_AXIS);
						heroe.setRotate(180);
					}else{
						//dx =  speed * Math.cos(angle);
						//dy = -1 * speed * Math.sin(angle);
						heroe.setDirection(true);
						heroe.setRotationAxis(Rotate.Y_AXIS);
						heroe.setRotate(360);
					}
					dx = speed * Math.cos(angle);
					dy = -1 * speed * Math.sin(angle);

					angle *= (180.0 / Math.PI);
					if(world.getObjects(Hero.class).size() != 0){
						heroe.shoot(dx, dy, angle);
					}
					gunPlayer.play();
				}
			}
		});
		primaryStage.setTitle("Spidermando");
		primaryStage.show();
		File song = new File("images/leveltheme.mp3");
		String path = song.getAbsolutePath();
//		levelTheme = new Media(new File(path).toURI().toString());
//		lvlThemePlayer = new MediaPlayer(levelTheme);
		lvlThemePlayer.setVolume(0.075);
		lvlThemePlayer.setCycleCount(4);
		lvlThemePlayer.play();
		primaryStage.setScene(scene);
	}
	
	
	//level 2 start heeeeeeeeeeeeeeeeeeeeeeeeeeeeerrrrrrrrrrrrrrrrrrrrrrrrrrrrrreeeeeeeeeeeeeeee
	public void genLevel2(Stage primaryStage){
		menuThemePlayer.stop();
		level = 2;
		ventauntPlayer.stop();
		lvlThemePlayer.stop();
		theStage = primaryStage;
		world = new GameWorld();
		heroe = new Hero();
		boss2 = new Venom();
		gameOver = false;
		isAlive = true;
		playerVelocity = new Point2D(0, 0);
		canJump = true;
		steppingBlocks = new ArrayList<Block>();
		obstacles = new ArrayList<Obstacle>();
		taunt = true;
		levelStart = false;
		bossFightStart = false;
		bossFightWon = false;

		for(int i = 0; i < l2.L2.length; i++){
			String curRow = l2.L2[i];
			for(int j = 0; j < curRow.length();j++){
				if(curRow.charAt(j)=='1'){
					Block block;
					if(i == 0){
						block = new Block(BLOCK_SIZE, true);
					}else{
						block = new Block(BLOCK_SIZE, false);
					}
					block.setX(j*BLOCK_SIZE);
					block.setY(i*BLOCK_SIZE);
					world.add(block);
					steppingBlocks.add(block);
				}
				if(curRow.charAt(j)=='2'){
					HealthPack h = new HealthPack(BLOCK_SIZE);

					h.setX(j*BLOCK_SIZE);
					h.setY(i*BLOCK_SIZE);
					world.add(h);
				}
				if(curRow.charAt(j)=='3'){
					Munition m = new Munition(BLOCK_SIZE);
					m.setX(j*BLOCK_SIZE);
					m.setY(i*BLOCK_SIZE);
					world.add(m);
				}
				if(curRow.charAt(j)=='4'){
					BarrelSpawn g = new BarrelSpawn(steppingBlocks,BLOCK_SIZE);
					g.setX(j*BLOCK_SIZE);
					g.setY(i*BLOCK_SIZE);
					world.add(g);
				}

				if(curRow.charAt(j)=='5'){
					EndPoint g = new EndPoint(BLOCK_SIZE);
					g.setX(j*BLOCK_SIZE);
					g.setY(i*BLOCK_SIZE);
					world.add(g);
				}

				if(curRow.charAt(j)=='6'){
					Obstacle s = new Obstacle(BLOCK_SIZE, 'u');
					s.setX(j*BLOCK_SIZE);
					s.setY(i*BLOCK_SIZE + 0.5*BLOCK_SIZE);
					obstacles.add(s);
					world.add(s);
				}

				if(curRow.charAt(j)=='7'){
					Obstacle s = new Obstacle(BLOCK_SIZE, 'd');
					s.setX(j*BLOCK_SIZE);
					s.setY(i*BLOCK_SIZE);
					obstacles.add(s);
					world.add(s);
				}
				if(curRow.charAt(j)=='8'){
					Obstacle s = new Obstacle(BLOCK_SIZE, 'r');
					s.setX(j*BLOCK_SIZE - 0.3*BLOCK_SIZE);
					s.setY(i*BLOCK_SIZE+ 15);
					obstacles.add(s);
					world.add(s);
				}
				if(curRow.charAt(j)=='9'){
					Obstacle s = new Obstacle(BLOCK_SIZE, 'l');
					s.setX(j*BLOCK_SIZE + 0.3*BLOCK_SIZE);
					s.setY(i*BLOCK_SIZE+15);
					obstacles.add(s);
					world.add(s);
				}
				if(curRow.charAt(j) == 'G'){
					Gunner g = new Gunner(steppingBlocks, Level2.L2, i, j);
					g.setX(j*BLOCK_SIZE);
					g.setY(i*BLOCK_SIZE);
					world.add(g);
				}
			}
		}
		int topY = -4;
		for(int a = 0; a < Level2.L2[0].length(); a++){
			Block block = new Block(BLOCK_SIZE, true);
			block.setX(a*BLOCK_SIZE);
			block.setY(topY*BLOCK_SIZE);
			world.add(block);
			steppingBlocks.add(block);
		}

		//Background
		Image background = new Image("file:images/background.png");
		ImageView view = new ImageView(background);
		view.setFitWidth(Level1.L1[0].length() * BLOCK_SIZE * 2);
		view.setFitHeight(SCREEN_HEIGHT);
		//World
		root = new StackPane();
		BorderPane pane = new BorderPane();

		world.setPrefWidth(SCREEN_WIDTH);
		world.setPrefHeight(SCREEN_HEIGHT);
		
		Rectangle bG = new Rectangle(40,40,Color.ALICEBLUE);
		Text title = new Text();
		title.setText("Spidermmando");
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		title.setFill(Color.RED);
		title.setStroke(Color.web("#0d61e8"));

		healthText = new Label("Health: " + heroe.getHealth());
		healthText.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		healthText.setTextFill(Color.RED);
		//healthText.setStroke(Color.web("#0d61e8"));

		ammoText = new Label("Ammo: " + heroe.getAmmo());
		ammoText.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		ammoText.setTextFill(Color.RED);
		//ammoText.setStroke(Color.web("#0d61e8"));

		VBox infoBox = new VBox();
		infoBox.getChildren().addAll(healthText, ammoText);
		infoBox.setAlignment(Pos.TOP_LEFT);
		pane.setTop(title);



		world.add(heroe);
		heroe.setBlocks(steppingBlocks);
		heroe.setX(50);
		heroe.setY(100); 

		heroe.translateXProperty().addListener((obs,old,newValue) ->{
			int offset = newValue.intValue();
			totalOffset += offset;
			if(offset>300 && offset<l2.L2[0].length() * BLOCK_SIZE - SCREEN_WIDTH - 600 + 300){
				root.setLayoutX(-1 * offset + 300);
				world.setLayoutx(-1 * offset + 300);
			}
		});
		//Testing Gunner Class


		world.add(boss2);
		boss2.setX(100 * BLOCK_SIZE);
		boss2.setY(20);




		Scene scene = new Scene(root, SCREEN_WIDTH + 600, SCREEN_HEIGHT);
		root.getChildren().addAll(view, world, infoBox);
		world.start();
		levelStart = true;
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new MyKeyboardHandler());

		primaryStage.setResizable(false);
		startScene = scene;

		//		Image deathImage = new Image("file:images/gameOver.jpg");
		//		ImageView newRoot = new ImageView(deathImage);
		//		newRoot.setFitWidth(l.L1[0].length() * BLOCK_SIZE);
		//		Scene deathScene = new Scene(root, SCREEN_WIDTH + 600, SCREEN_HEIGHT);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.D){
					//					heroe.setDx(SPEED_OF_HERO);
					//					heroe.setRotationAxis(Rotate.Y_AXIS);
					//			    	//heroe.setRotate(360);
					//			    	heroe.setDirection(true);
					//			    	//System.out.println(heroe.getTranslateX());

					heroe.setDx(30);
					heroe.setDirection(true);
					
					//moveHeroX(SPEED_OF_HERO);
				}
				if(e.getCode() == KeyCode.A){
					//					heroe.setDx(-1 * SPEED_OF_HERO);
					//					heroe.setRotationAxis(Rotate.Y_AXIS);
					//			    	//heroe.setRotate(180);
					//			    	heroe.setDirection(false);
					//			    	//System.out.println(heroe.getTranslateX());
					//moveHeroX(-1 * SPEED_OF_HERO);
					heroe.setDx(-30);
					heroe.setDirection(false);
				}
				if(e.getCode() == KeyCode.W){
					//					heroe.setDy(-50);	
					jumpHero();
				}


			}

		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.A || event.getCode() == KeyCode.D){
					heroe.setDx(0);
					
				}
			}

		});
		timer = new AnimationTimer(){

			@Override
			public void handle(long arg0) {
				// TODO Auto-generated method stub
				if(gameOver == false && levelStart){
					update();

					long timeElapsed = arg0 - last;
					//fpsString.setValue(Double.toString(1_000_000_000.0/(timeElapsed)));

					last = arg0;
					//					if(!isAlive){
					//						primaryStage.setScene(deathScene);
					//						primaryStage.show();
					//					}
					ammoText.setText("Ammo: " + heroe.getAmmo());
					healthText.setText("Health: " + heroe.getHealth());
					infoBox.setTranslateX(-1 * root.getLayoutX());
					if(heroe.getTranslateX() >= 85 * BLOCK_SIZE && heroe.getTranslateY() <= 2 * BLOCK_SIZE && taunt){
						ventauntPlayer.play();
						taunt = false;
					}
					if(heroe.getTranslateX() >= 96 * BLOCK_SIZE && !bossFightStart){
						sealBlock = new Block(BLOCK_SIZE, true);
						sealBlock.setX(86 * BLOCK_SIZE);
						sealBlock.setY(0);
						world.add(sealBlock);
						steppingBlocks.add(sealBlock);
						sealBlock2 = new Block(BLOCK_SIZE, false);
						sealBlock2.setX(86 * BLOCK_SIZE);
						sealBlock2.setY(BLOCK_SIZE);
						world.add(sealBlock2);
						steppingBlocks.add(sealBlock2);
						sealBlock3 = new Block(BLOCK_SIZE, true);
						sealBlock3.setX(112 * BLOCK_SIZE);
						sealBlock3.setY(0);
						world.add(sealBlock3);
						steppingBlocks.add(sealBlock3);
						bossFightStart = true;
						boss2.setFight(true);
					}
					if(boss2.getHealth() <= 0){
						bossFightWon = true;
					}
					if(bossFightWon){
						bossFightWon = false;
						world.remove(sealBlock);
						steppingBlocks.remove(sealBlock);
						world.remove(sealBlock2);
						steppingBlocks.remove(sealBlock2);
						world.remove(sealBlock3);
						steppingBlocks.remove(sealBlock3);
					}
				}
			}

		};
		timer.start();
		root.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent e) {
				// TODO Auto-generated method stub
				//double mouseX = e.getX() + heroe.getTranslateX();
				if(heroe.getAmmo() > 0){
					gunPlayer.stop();
					double mouseX = e.getX();
					double heroX;
					if(heroe.isDirection()){
						heroX = heroe.getTranslateX() + heroe.getImage().getWidth() * 1.6;
					}else{
						heroX = heroe.getTranslateX() + heroe.getImage().getWidth() * 0.4;
					}
					double mouseY = e.getY();
					double heroY = heroe.getTranslateY() + heroe.getImage().getHeight() * 2;
					double speed = 80.0;
					double tangent = Math.abs(mouseY - heroY) / Math.abs(mouseX - heroX);
					double angle = Math.atan(tangent);
					double dx;
					double dy;
					if(mouseX < heroX && mouseY > heroY){
						//dx = -1 * speed * Math.cos(angle);
						//dy = speed * Math.sin(angle);
						angle = Math.PI + angle;
						heroe.setDirection(false);
						heroe.setRotationAxis(Rotate.Y_AXIS);
						heroe.setRotate(180);
					}else if(mouseX > heroX && mouseY > heroY){
						//dx = speed * Math.cos(angle);
						//dy = speed * Math.sin(angle);
						angle = 2 * Math.PI - angle;
						heroe.setDirection(true);
						heroe.setRotationAxis(Rotate.Y_AXIS);
						heroe.setRotate(360);
					}else if(mouseX < heroX && mouseY < heroY){
						//dx =  -1 * speed * Math.cos(angle);
						//dy = -1 * speed * Math.sin(angle);
						angle = Math.PI - angle;
						heroe.setDirection(false);
						heroe.setRotationAxis(Rotate.Y_AXIS);
						heroe.setRotate(180);
					}else{
						//dx =  speed * Math.cos(angle);
						//dy = -1 * speed * Math.sin(angle);
						heroe.setDirection(true);
						heroe.setRotationAxis(Rotate.Y_AXIS);
						heroe.setRotate(360);
					}
					dx = speed * Math.cos(angle);
					dy = -1 * speed * Math.sin(angle);

					angle *= (180.0 / Math.PI);
					if(world.getObjects(Hero.class).size() != 0){
						heroe.shoot(dx, dy, angle);
					}
					gunPlayer.play();
				}
			}
		});
		primaryStage.setTitle("Spidermando");
		primaryStage.show();
		File song = new File("images/leveltheme.mp3");
		String path = song.getAbsolutePath();
//		levelTheme = new Media(new File(path).toURI().toString());
//		lvlThemePlayer = new MediaPlayer(levelTheme);
		lvlThemePlayer.setVolume(0.075);
		lvlThemePlayer.setCycleCount(4);
		lvlThemePlayer.play();
		primaryStage.setScene(scene);
	}

	private void moveHeroY(int velocity){
		boolean movingDown = velocity > 0;
		for(int i = 0; i < Math.abs(velocity); i++){
			for(Block block : steppingBlocks){
				if(heroe.getBoundsInParent().intersects(block.getBoundsInParent())){
					if(movingDown){
						if(heroe.getTranslateY() + heroe.getHeight() <= block.getY() && !block.isCritical()){
							heroe.setTranslateY(heroe.getTranslateY()-1);
							canJump = true;
							return;
						}
					}else if(heroe.getTranslateY() == block.getY()){
						return;
					}
				}
			}
			heroe.setTranslateY(heroe.getTranslateY() + (movingDown ? 1 : -1));
		}
	}

	private void jumpHero(){
		if(canJump){
			playerVelocity = playerVelocity.add(0, -30);
			canJump = false;
		}

	}

	private class MyKeyboardHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent e) {

		}
	}
}
