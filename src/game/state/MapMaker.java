package game.state;

import framework.util.TextFileHandler;
import game.main.GameMain;
import game.main.Resources;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MapMaker extends State{

	private BufferedImage[] tilesets;
	private int[] tilesetIDs;
	private int[][] tiles, terrain, copied,extraData;
	private BufferedImage background;
	
	private final Color LIGHT_YELLOW=new Color(1,1,0,0.5f);
	private final Color[] terrainColors={null, new Color(1,0,0,0.5f), new Color(1,1,0,0.5f), new Color(0.5f,0.5f,0.5f,0.5f), new Color(0,0,1,0.5f), new Color(1,0,1,0.5f),Color.BLACK,Color.BLUE,Color.BLUE,Color.CYAN,Color.BLACK,Color.BLACK};
	
	private boolean[] keys;
	
	private int cursorX, cursorY, tile, otherX, otherY;
	private boolean copying, tileMode;
	
	@Override
	public void init() {
		tilesets=Resources.loadTilesets();
		keys=new boolean[19];
		tilesetIDs=new int[2];
		tiles=new int[GameMain.GAME_WIDTH/GameState.tileWidth*2][GameMain.GAME_HEIGHT/GameState.tileWidth*2];
		terrain=new int[GameMain.GAME_WIDTH/GameState.tileWidth*2][GameMain.GAME_HEIGHT/GameState.tileWidth*2];
		background=new BufferedImage(GameMain.GAME_WIDTH, GameMain.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
		int[][] prevMap=TextFileHandler.readFileInt(0, "MapMakerOutput");
		if (prevMap.length>0) {
			int index=0;
			for (int x=0; x<tiles.length; x++) {
				for (int y=0; y<tiles[0].length; y++) {
					tiles[x][y]=prevMap[1][index]%20+20*prevMap[0][prevMap[1][index]/20];
					index++;
				}
			}
			if (prevMap[1].length<tiles.length*tiles[0].length+terrain.length*terrain[0].length) {
				for (int x=0; x<terrain.length; x+=2) {
					for (int y=0; y<terrain[0].length; y+=2) {
						terrain[x][y]=prevMap[1][index];
						terrain[x+1][y]=prevMap[1][index];
						terrain[x][y+1]=prevMap[1][index];
						terrain[x+1][y+1]=prevMap[1][index];
						index++;
					}
				}
			} else {
				for (int x=0; x<terrain.length; x++) {
					for (int y=0; y<terrain[0].length; y++) {
						terrain[x][y]=prevMap[1][index];
						index++;
					}
				}
			}
			tilesetIDs[0]=prevMap[0][0];
			if (prevMap[0].length==1) {
				tilesetIDs[1]=1;
			} else {
				tilesetIDs[1]=prevMap[0][1];
			}
			extraData=new int[prevMap.length-2][];
			for (int i=0; i<extraData.length; i++) {
				extraData[i]=prevMap[i+2];
			}
		} else {
			tilesetIDs[0]=0;
			tilesetIDs[1]=1;
			extraData=new int[0][0];
		}
		redrawBackground();
		copied=new int[0][0];
		tileMode=true;
	}
	
	private void redrawBackground() {
		Graphics g=background.getGraphics();
		for (int x=0; x<tiles.length; x++) {
			for (int y=0; y<tiles[0].length; y++) {
				updateTile(x,y,g);
			}
		}
	}

	private void updateTile(int x, int y, Graphics g) {
		g.drawImage(tilesets[tiles[x][y]/20], x*GameState.tileWidth/2, y*GameState.tileWidth/2, (x+1)*GameState.tileWidth/2, (y+1)*GameState.tileWidth/2, (tiles[x][y]%20)*GameState.tileWidth/2, 0, (tiles[x][y]%20+1)*GameState.tileWidth/2, GameState.tileWidth/2, null);	
	}
	
	@Override
	public void update(float delta) {
		if (keys[9]) {
			keys[9]=false;
			saveMap();
		}
		if (!tileMode) {
			moveCursor();
			if (keys[5]) {
				terrain[cursorX][cursorY]++;
				terrain[cursorX][cursorY]%=terrainColors.length;
				keys[5]=false;
			}
			if (keys[10]) {
				terrain[cursorX][cursorY]=0;
			}
			if (keys[11]) {
				terrain[cursorX][cursorY]=1;
			}
			if (keys[12]) {
				terrain[cursorX][cursorY]=2;
			}
			if (keys[13]) {
				terrain[cursorX][cursorY]=3;
			}
			if (keys[14]) {
				terrain[cursorX][cursorY]=4;
			}
			if (keys[15]) {
				terrain[cursorX][cursorY]=5;
			}
			if (keys[16]) {
				terrain[cursorX][cursorY]=6;
			}
			if (keys[17]) {
				terrain[cursorX][cursorY]=7;
			}
			if (keys[18]) {
				terrain[cursorX][cursorY]=8;
			}
			if (keys[4]) {
				for (int x=0; x<terrain.length; x++) {
					for (int y=0; y<terrain[0].length; y++) {
						terrain[x][y]=0;
					}
				}
				keys[4]=false;
			}
			if (keys[8]) {
				tileMode=true;
				keys[8]=false;
			}
		} else if (copying) {
			moveCursor();
			if (!keys[6]) {
				copied=new int[Math.abs(otherX-cursorX)+1][Math.abs(otherY-cursorY)+1];
				for (int x=0; x<copied.length; x++) {
					for (int y=0; y<copied[0].length; y++) {
						copied[x][y]=tiles[Math.min(otherX, cursorX)+x][Math.min(otherY, cursorY)+y];
					}
				}
				copying=false;
			}
		} else if (keys[4]) {
			if (keys[2]) {
				tile--;
				if (tile<0) {
					tile=39;
				}
				keys[2]=false;
			}
			if (keys[3]) {
				tile++;
				if (tile>=40) {
					tile=0;
				}
				keys[3]=false;
			} 
			if (keys[0]) {
				tilesetIDs[tile/20]--;
				if (tilesetIDs[tile/20]<0) {
					tilesetIDs[tile/20]=Resources.NUM_TILESETS-1;
				}
				keys[0]=false;
			}
			if (keys[1]) {
				tilesetIDs[tile/20]++;
				if (tilesetIDs[tile/20]>=Resources.NUM_TILESETS) {
					tilesetIDs[tile/20]=0;
				}
				keys[1]=false;
			}
		} else {
			moveCursor();
			if (keys[5]) {
				tiles[cursorX][cursorY]=tilesetIDs[tile/20]*20+tile%20;
				updateTile(cursorX,cursorY,background.getGraphics());
				keys[5]=false;
			}
			if (keys[7]) {
				for (int x=0; x<Math.min(copied.length,tiles.length-cursorX); x++) {
					for (int y=0; y<Math.min(copied[0].length,tiles[0].length-cursorY); y++) {
						tiles[x+cursorX][y+cursorY]=copied[x][y];
					}
				}
				redrawBackground();
				keys[7]=false;
			} else if (keys[6]) {
				copying=true;
				otherX=cursorX;
				otherY=cursorY;
			} else if (keys[8]) {
				tileMode=false;
				keys[8]=false;
			}
		}
	}

	private void saveMap() {
		int numSets=0;
		boolean[] setsUsed=new boolean[tilesets.length];
		int[] setLocations=new int[tilesets.length];
		for (int x=0; x<tiles.length; x++) {
			for (int y=0; y<tiles[0].length; y++) {
				if (!setsUsed[tiles[x][y]/20]) {
					setsUsed[tiles[x][y]/20]=true;
					numSets++;
				}
			}
		}
		int[][] data=new int[2+extraData.length][];
		data[0]=new int[numSets];
		int index=0;
		for (int i=0; i<numSets; i++) {
			while (!setsUsed[index]) {
				index++;
			}
			data[0][i]=index;
			setLocations[index]=i;
			index++;
		}
		data[1]=new int[tiles.length*tiles[0].length+terrain.length*terrain[0].length];
		index=0;
		for (int x=0; x<tiles.length; x++) {
			for (int y=0; y<tiles[0].length; y++) {
				data[1][index]=20*setLocations[tiles[x][y]/20]+tiles[x][y]%20;
				index++;
			}
		}
		for (int x=0; x<terrain.length; x++) {
			for (int y=0; y<terrain[0].length; y++) {
				data[1][index]=terrain[x][y];
				index++;
			}
		}
		for (int i=2; i<data.length; i++) {
			data[i]=extraData[i-2];
		}
		TextFileHandler.writeFile(data, "MapMakerOutput");
	}
	
	private void moveCursor() {
		if (keys[0]) {
			cursorY--;
			keys[0]=false;
		}
		if (keys[1]) {
			cursorY++;
			keys[1]=false;
		}
		if (keys[2]) {
			cursorX--;
			keys[2]=false;
		}
		if (keys[3]) {
			cursorX++;
			keys[3]=false;
		}
		cursorX+=tiles.length;
		cursorY+=tiles[0].length;
		cursorX%=tiles.length;
		cursorY%=tiles[0].length;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(background,0,0,null);
		g.setColor(LIGHT_YELLOW);
		for (int x=0; x<terrain.length; x++) {
			g.drawLine(x*GameState.tileWidth, 0, x*GameState.tileWidth, GameMain.GAME_HEIGHT);
		}
		for (int y=0; y<terrain[0].length; y++) {
			g.drawLine(0, y*GameState.tileWidth, GameMain.GAME_WIDTH, y*GameState.tileWidth);
		}
		if (!tileMode) {
			for (int x=0; x<terrain.length; x++) {
				for (int y=0; y<terrain[0].length; y++) {
					if (terrain[x][y]>0) {
						g.setColor(terrainColors[terrain[x][y]]);
						if (terrain[x][y]==6) {
							g.drawLine(x*GameState.tileWidth/2, y*GameState.tileWidth/2, (x+1)*GameState.tileWidth/2, (y+1)*GameState.tileWidth/2);
							g.drawLine(x*GameState.tileWidth/2, (y+1)*GameState.tileWidth/2, (x+1)*GameState.tileWidth/2, y*GameState.tileWidth/2);
						} else if (terrain[x][y]==7) {
							g.fillRect(x*GameState.tileWidth/2+GameState.tileWidth/2/3,y*GameState.tileWidth/2,GameState.tileWidth/2/3,GameState.tileWidth/2);
						} else if (terrain[x][y]==8) {
							g.fillOval(x*GameState.tileWidth/2, y*GameState.tileWidth/2, GameState.tileWidth/2,GameState.tileWidth/2);
						} else if (terrain[x][y]==10) {
							g.fillOval(x*GameState.tileWidth/2,y*GameState.tileWidth/2,GameState.tileWidth/2,GameState.tileWidth/2/2);
							g.fillRect(x*GameState.tileWidth/2+6,y*GameState.tileWidth/2+6,GameState.tileWidth/2-12,GameState.tileWidth/2-6);
						} else {
							g.fillRect(x*GameState.tileWidth/2, y*GameState.tileWidth/2, GameState.tileWidth/2, GameState.tileWidth/2);
						}
					}
				}
			}
		}
		g.setColor(LIGHT_YELLOW);
		if (copying) {
			g.fillRect(Math.min(cursorX, otherX)*GameState.tileWidth/2,Math.min(cursorY,otherY)*GameState.tileWidth/2,(Math.abs(cursorX-otherX)+1)*GameState.tileWidth/2,(Math.abs(cursorY-otherY)+1)*GameState.tileWidth/2);
		} else {
			g.fillRect(cursorX*GameState.tileWidth/2,cursorY*GameState.tileWidth/2,GameState.tileWidth/2,GameState.tileWidth/2);
		}
		g.setColor(Color.BLACK);
		g.fillRect(0, GameMain.GAME_HEIGHT, GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT-GameMain.GAME_HEIGHT);
		g.drawImage(tilesets[tilesetIDs[0]], 0, GameMain.WINDOW_HEIGHT-GameState.tileWidth/2, null);
		g.drawImage(tilesets[tilesetIDs[1]], GameState.tileWidth*10,GameMain.WINDOW_HEIGHT-GameState.tileWidth/2,null);
		g.setColor(Color.RED);
		g.fillRect(tile*GameState.tileWidth/2, GameMain.WINDOW_HEIGHT-GameState.tileWidth/2-5, GameState.tileWidth/2, 5);
	}

	@Override
	public void onClick(MouseEvent e) {
		
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		int key=getKeyCode(e.getKeyCode());
		if (key>=0) {
			keys[key]=true;
		}
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		int key=getKeyCode(e.getKeyCode());
		if (key>=0) {
			keys[key]=false;
		}
	}

	private int getKeyCode(int key) {
		switch (key) {
		case KeyEvent.VK_UP:
			return 0;
		case KeyEvent.VK_DOWN:
			return 1;
		case KeyEvent.VK_LEFT:
			return 2;
		case KeyEvent.VK_RIGHT:
			return 3;
		case KeyEvent.VK_SHIFT:
			return 4;
		case KeyEvent.VK_SPACE:
			return 5;
		case KeyEvent.VK_C:
			return 6;
		case KeyEvent.VK_V:
			return 7;
		case KeyEvent.VK_T:
			return 8;
		case KeyEvent.VK_ENTER:
			return 9;
		case KeyEvent.VK_1:
			return 10;
		case KeyEvent.VK_2:
			return 11;
		case KeyEvent.VK_3:
			return 12;
		case KeyEvent.VK_4:
			return 13;
		case KeyEvent.VK_5:
			return 14;
		case KeyEvent.VK_6:
			return 15;
		case KeyEvent.VK_7:
			return 16;
		case KeyEvent.VK_8:
			return 17;
		case KeyEvent.VK_9:
			return 18;
		default:
			return -1;
		}
	}
	
}
