package game.state;

import framework.util.TextFileHandler;
import game.main.GameMain;
import game.main.Resources;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class WorldViewState extends State{

	private BufferedImage world, caves, area, dungeon;
	private boolean switchWorld;
	private int display;
	private static final int tileWidth=GameState.tileWidth;
	private static final int pixelsPerTile=2;
	private BufferedImage[] tilesets;
	private static int MAP_WIDTH=Integer.parseInt(TextFileHandler.readFileLine(0, "MapData"));
	
	@Override
	public void init() {
		area=new BufferedImage(GameMain.GAME_WIDTH,GameMain.GAME_HEIGHT,BufferedImage.TYPE_INT_RGB);
		caves=new BufferedImage(GameMain.GAME_WIDTH,GameMain.WINDOW_HEIGHT,BufferedImage.TYPE_INT_RGB);
		dungeon=new BufferedImage(GameMain.GAME_WIDTH,GameMain.WINDOW_HEIGHT,BufferedImage.TYPE_INT_RGB);
		world=new BufferedImage(GameMain.GAME_WIDTH,GameMain.WINDOW_HEIGHT,BufferedImage.TYPE_INT_RGB);
		int WORLD_HEIGHT=0;
		while (TextFileHandler.readFileLine(1+WORLD_HEIGHT*MAP_WIDTH, "MapData")!=null) {
			WORLD_HEIGHT++;
		}
		tilesets=Resources.loadTilesets();
		display=0;
		Graphics g=world.getGraphics();
		for (int x=0; x<MAP_WIDTH; x++) {
			for (int y=0; y<WORLD_HEIGHT; y++) {
				drawArea(x,y,area.getGraphics());
				g.drawImage(area, x*20*pixelsPerTile, y*13*pixelsPerTile, 20*pixelsPerTile, 13*pixelsPerTile, null);
			}
		}
		display=1;
		g=caves.getGraphics();
		for (int x=0; x<MAP_WIDTH; x++) {
			for (int y=0; y<WORLD_HEIGHT; y++) {
				drawArea(x,y,area.getGraphics());
				g.drawImage(area, x*20*pixelsPerTile, y*13*pixelsPerTile, 20*pixelsPerTile, 13*pixelsPerTile, null);
			}
		}
		display=2;
		g=dungeon.getGraphics();
		for (int x=0; x<MAP_WIDTH; x++) {
			for (int y=0; y<WORLD_HEIGHT; y++) {
				drawArea(x,y,area.getGraphics());
				g.drawImage(area, x*20*pixelsPerTile, y*13*pixelsPerTile, 20*pixelsPerTile, 13*pixelsPerTile, null);
			}
		}
		display=0;
	}
	
	private void drawArea(int areaX, int areaY, Graphics g) {
		int[][] tileData;
		if (display==0) {
			tileData=TextFileHandler.readFileInt(areaX+areaY*MAP_WIDTH+1, "MapData");
		} else if (display==1){
			tileData=TextFileHandler.readFileInt(areaX+areaY*MAP_WIDTH, "CaveMaps");
		} else {
			tileData=TextFileHandler.readFileInt(areaX+areaY*MAP_WIDTH, "DungeonMaps");
		}
		if (tileData.length<=1) {
			g.clearRect(0, 0, GameMain.GAME_WIDTH, GameMain.GAME_HEIGHT);
			return;
		}
		int index=0;
		int setFrom;
		for (int x=0; x<2*GameMain.GAME_WIDTH/tileWidth; x++) {
			for (int y=0; y<2*GameMain.GAME_HEIGHT/tileWidth; y++) {
				setFrom=tileData[0][tileData[1][index]/20];
				g.drawImage(tilesets[setFrom],x*tileWidth/2, y*tileWidth/2, (x+1)*tileWidth/2, (y+1)*tileWidth/2, (tileData[1][index]%20)*tileWidth/2, 0, (tileData[1][index]%20+1)*tileWidth/2, tileWidth/2, null);
				index++;
			}
		}
	}

	@Override
	public void update(float delta) {
		if (switchWorld) {
			display++;
			if (display>2) {
				display=0;
			}
			switchWorld=false;
		}
	}

	@Override
	public void render(Graphics g) {
		if (display==0) {
			g.drawImage(world,0,0,null);
		} else if (display==1){
			g.drawImage(caves,0,0,null);
		} else {
			g.drawImage(dungeon,0,0,null);
		}
	}

	@Override
	public void onClick(MouseEvent e) {
		
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		switchWorld=true;
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		switchWorld=false;
	}

}
