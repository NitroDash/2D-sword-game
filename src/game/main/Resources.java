package game.main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Resources {
	
	public static BufferedImage[] weapons;
	public static BufferedImage gameOver,numbers,letters;
	
	public static final int NUM_TILESETS=9;
	
	public static void load() {
		weapons=new BufferedImage[2];
		weapons[0]=loadImage("Sword.png");
		weapons[1]=loadImage("Boomerang.png");
		
		gameOver=loadImage("GameOver.png");
		numbers=loadImage("Numbers.png");
		letters=loadImage("Letters.png");
	}
	
	public static BufferedImage loadTileset(int ID) {
		switch (ID) {
		case 0:
			return loadImage("GrassPathTiles.png");
		case 1:
			return loadImage("WaterTiles.png");
		case 2:
			return loadImage("DungeonTiles.png");
		case 3:
			return loadImage("DungeonTiles2.png");
		case 4:
			return loadImage("Ice1.png");
		case 5:
			return loadImage("Ice2.png");
		case 6:
			return loadImage("Desert.png");
		case 7:
			return loadImage("Cave.png");
		case 8:
			return loadImage("Cave2.png");
		default:
			return null;
		}
	}
	
	public static BufferedImage[] loadTilesets() {
		BufferedImage[] tilesets=new BufferedImage[NUM_TILESETS];
		for (int i=0; i<tilesets.length; i++) {
			tilesets[i]=loadTileset(i);
		}
		return tilesets;
	}
	
	
	public static BufferedImage loadImage(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(Resources.class.getResourceAsStream("/resources/"+filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
}
