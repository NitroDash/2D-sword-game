package game.state;

import framework.util.TextFileHandler;
import game.main.GameMain;
import game.main.Resources;
import game.main.SoundPlayer;
import game.model.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameState extends State{

	private BufferedImage background=new BufferedImage(GameMain.GAME_WIDTH,GameMain.GAME_HEIGHT,BufferedImage.TYPE_INT_RGB);
	private BufferedImage newBackground=new BufferedImage(GameMain.GAME_WIDTH,GameMain.GAME_HEIGHT,BufferedImage.TYPE_INT_RGB);
	
	private static BufferedImage[] tilesets=new BufferedImage[6];
	public static final int tileWidth=20;
	private int[][] tiles, terrain, newTiles, newTerrain;
	public static GameState currentState;
	public static InventoryState inventory;
	
	private int areaX, areaY, areaFile;
	public int money,displayMoney;
	
	private Warp respawn;
	
	public static final int originX=3;
	public static final int originY=1;
	
	private int transDX, transDY, transX, transY, treasureOpenCounter, fadeCounter,gameOverCounter, darknessOpacity, ddark, shopSpot;
	private boolean transition, gameOver, fadeTransition, initd, shopping;
	public static boolean barOnTop;
	
	private Drop[] shop={new Heart(0,0),new StaminaExtender(0,0),new HealthExtender(0,0),new WeaponDrop(new Boomerang(),0,0)};
	private int[] prices={5,150,200,100};
	
	public static int MAP_WIDTH;
	
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Entity> staticEntities;
	private ArrayList<Drop> drops;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Warp> warps;
	
	private Warp currentWarp;
	
	private boolean[] keys=new boolean[13];
	
	@Override
	public void init() {
		if (!initd) {
			SoundPlayer.playSong(0);
			ChestData.initChestContents();
			areaX=0;
			areaY=0;
			areaFile=0;
			MAP_WIDTH=Integer.parseInt(TextFileHandler.readFileLine(0, "MapData"));
			tiles=new int[GameMain.GAME_WIDTH/tileWidth*2][GameMain.GAME_HEIGHT/tileWidth*2];
			newTiles=new int[tiles.length][tiles[0].length];
			terrain=new int[GameMain.GAME_WIDTH/tileWidth*2][GameMain.GAME_HEIGHT/tileWidth*2];
			enemies=new ArrayList<Enemy>(0);
			staticEntities=new ArrayList<Entity>(0);
			drops=new ArrayList<Drop>(0);
			projectiles=new ArrayList<Projectile>(0);
			warps=new ArrayList<Warp>(0);
			newTerrain=new int[terrain.length][terrain[0].length];
			loadNewTiles(areaX, areaY, areaFile, tiles, terrain, background);
			player=new Player(GameMain.GAME_WIDTH/2,GameMain.GAME_HEIGHT/2);
			int[] respawnData={0,0,0,10,6};
			respawn=new Warp(respawnData,0,0);
			inventory=new InventoryState();
			initd=true;
		} else {
			areaX=respawn.destAreaX;
			areaY=respawn.destAreaY;
			areaFile=respawn.destAreaFile;
			loadNewTiles(areaX,areaY,areaFile,tiles,terrain,background);
			player.hp=player.maxhp;
			player.x=respawn.destX*tileWidth;
			player.y=respawn.destY*tileWidth;
		}
		gameOverCounter=0;
		gameOver=false;
		transition=false;
		treasureOpenCounter=0;
		ddark=0;
		darknessOpacity=0;
	}

	@Override
	public void update(float delta) {
		if (displayMoney<money) {
			displayMoney++;
		} else if (displayMoney>money) {
			displayMoney--;
		}
		darknessOpacity+=ddark;
		if (darknessOpacity%60==0) {ddark=0;}
		if (gameOver) {
			if (gameOverCounter==0) {
				SoundPlayer.playSong(-1);
			}
			gameOverCounter++;
			if (gameOverCounter>240) {
				init();
			}
		} else if (treasureOpenCounter>0) {
			treasureOpenCounter--;
			if (treasureOpenCounter==0) {
				if (!drops.get(drops.size()-1).getCollected(player)) {
					((Chest)staticEntities.get(0)).open=false;
				} else {
					ChestData.setChestID(areaX,areaY,areaFile,0);
				}
				drops.remove(drops.size()-1);
			}
		} else if (fadeTransition) {
			fadeCounter+=3;
			if (fadeCounter>=90) {
				fadeCounter=-90;
				player.x=tileWidth*currentWarp.destX;
				player.y=tileWidth*currentWarp.destY;
				copyOldTilesToNew();
			}
			if (fadeCounter==0) {
				fadeTransition=false;
			}
		} else if (transition) {
			if (ddark<=0) {
				continueTransition();
				if (ddark==-1) {
					darknessOpacity++;
				}
			}
		} else if (shopping) {
			if (keys[0]) {
				shopSpot--;
				if (shopSpot<0) {
					shopSpot=shop.length-1;
				}
				keys[0]=false;
			}
			if (keys[1]) {
				shopSpot++;
				if (shopSpot>=shop.length) {
					shopSpot=0;
				}
				keys[1]=false;
			}
			if (keys[6]) {
				if (money>=prices[shopSpot]&&shop[shopSpot].getCollected(player)) {
					money-=prices[shopSpot];
				}
				keys[6]=false;
			}
			if (keys[5]) {
				shopping=false;
				keys[5]=false;
			}
		} else {
			if (player.hp<=0) {
				gameOver=true;
				player.knockbackCounter=0;
				player.resetWeapon();
				return;
			}
			player.update(keys);
			moveEntity(player);
			for (int i=0; i<enemies.size(); i++) {
				enemies.get(i).update();
				moveEntity(enemies.get(i));
				if (player.knockbackCounter==0&&enemies.get(i).intersects(player)&&enemies.get(i).dangerous) {
					player.addKnockback(enemies.get(i),enemies.get(i).damage);
				}
				if (enemies.get(i).hp<=0&&enemies.get(i).knockbackCounter==0) {
					Drop drop=enemies.get(i).getDrop();
					if (drop!=null) {drops.add(drop);}
					enemies.remove(i);
					i--;
					if (enemies.size()==0) {
						unlockBossWarps();
					}
				}
			}
			for (int i=0; i<drops.size(); i++) {
				if (drops.get(i).intersects(player)) {
					if (drops.get(i).getCollected(player)) {
						drops.remove(i);
						i--;
					}
				}
			}
			for (int i=0; i<projectiles.size(); i++) {
				projectiles.get(i).update();
				if (projectiles.get(i).enemy){
					if (player.knockbackCounter==0&&projectiles.get(i).intersects(player)) {
						player.addKnockback(projectiles.get(i), projectiles.get(i).damage);
						if (projectiles.get(i).vanishOnContact){
							projectiles.remove(i);
							i--;
							break;
						}
					}
				}
				if (projectiles.get(i).hp==0) {
					projectiles.remove(i);
					i--;
					break;
				}
			}
			for (int i=0; i<staticEntities.size(); i++) {
				if (staticEntities.get(i) instanceof PushBlock) {
					((PushBlock)staticEntities.get(i)).update(player);
					moveEntity(staticEntities.get(i));
				} else if (staticEntities.get(i) instanceof LockBlock) {
					((LockBlock)staticEntities.get(i)).update(player);
				} else if (staticEntities.get(i) instanceof ShadyDealer) {
					((ShadyDealer)staticEntities.get(i)).update();
				}
			}
			if (keys[6]) {
				for (int i=0; i<staticEntities.size(); i++) {
					if (player.direction==0&&staticEntities.get(i) instanceof Chest) {
						player.y-=tileWidth;
						if (player.intersects(staticEntities.get(i))) {
							staticEntities.add(0,staticEntities.remove(i));
							openChest((Chest)staticEntities.get(0));
							player.y+=tileWidth;
							break;
						}
						player.y+=tileWidth;
					} else if (staticEntities.get(i) instanceof ShadyDealer) {
						if (player.distTo(staticEntities.get(i))<tileWidth*3/2) {
							initShop();
							keys[6]=false;
							break;
						}
					}
				}
			}
			for (int i=0; i<5; i++) {
				if (keys[i+8]) {
					if (inventory.currentWeapon!=i) {
						inventory.currentWeapon=i;
						player.resetWeapon();
						try {
							player.weapon=inventory.weapons.get(i);
						} catch (IndexOutOfBoundsException e) {
							player.weapon=null;
						}
					}
				}
			}
			if ((currentWarp=getWarp(player))!=null) {
				startFadeTransition();
			}
			checkForTransition();
		}
	}

	private void initShop() {
		int left=GameMain.GAME_WIDTH/2-player.x/(GameMain.GAME_WIDTH/2);
		for (int i=0; i<shop.length; i++) {
			shop[i].center(left+tileWidth*5/4, tileWidth*5/4+tileWidth*i);
		}
		shopping=true;
	}
	
	private void unlockBossWarps() {
		boolean addedWarp=false;
		for (int x=0; x<terrain.length; x++) {
			for (int y=0; y<terrain[0].length; y++) {
				if (terrain[x][y]==9) {
					terrain[x][y]=5;
					if (!addedWarp) {
						staticEntities.add(new BossWarp(x*tileWidth/2,y*tileWidth/2));
						addedWarp=true;
					}
				}
			}
		}
	}
	
	private void openChest(Chest chest) {
		if (!chest.open){
			chest.open=true;
			Drop drop=ChestData.getChestContents(ChestData.getChestID(areaX,areaY,areaFile), chest);
			drop.center(chest.x+chest.width/2,chest.y);
			drops.add(drop);
			treasureOpenCounter=60;
		}
	}
	
	public Warp getWarp(Entity check) {
		if (transition||fadeTransition) {return null;}
		for (int x=Math.max(0,check.x*2/tileWidth); x<=Math.min(terrain.length-1,(check.x+check.width-1)*2/tileWidth); x++) {
			for (int y=Math.max(0,check.y*2/tileWidth); y<=Math.min(terrain[0].length-1,(check.y+check.height-1)*2/tileWidth); y++) {
				if (terrain[x][y]==5) {
					for (int i=0; i<warps.size(); i++) {
						if (warps.get(i).x==x&&warps.get(i).y==y) {
							return warps.get(i);
						}
					}
				}
			}
		}
		return null;
	}
	
	public boolean onlyOn(Entity check,int tile) {
		if (transition) {return false;}
		for (int x=Math.max(0,2*check.x/tileWidth); x<=Math.min(terrain.length-1,(check.x+check.width-1)*2/tileWidth); x++) {
			for (int y=Math.max(0,2*check.y/tileWidth); y<=Math.min(terrain[0].length-1,(check.y+check.height-1)*2/tileWidth); y++) {
				if (terrain[x][y]!=tile) {
					return false;
				}
			}
		}
		return true;
	}
	
	public int getTerrain(int x, int y) {
		return getArrayEntry(x,y,terrain);
	}

	public int getArrayEntry(int x, int y,int[][] array) {
		if (x>=0&&x<array.length&&y>=0&&y<array[0].length) {
			return array[x][y];
		} else {
			return Integer.MIN_VALUE;
		}
	}
	
	public void addProjectile(Projectile add) {
		projectiles.add(add);
	}
	
	public void addEnemy(Enemy add) {
		enemies.add(add);
	}
	
	private void checkForTransition() {
		if (player.x+player.width>GameMain.GAME_WIDTH) {
			startTransition(1,0);
		} else if (player.x<0) {
			startTransition(-1,0);
		} else if (player.y<0) {
			startTransition(0,-1);
		} else if (player.y+player.height>GameMain.GAME_HEIGHT) {
			startTransition(0,1);
		}
	}
	
	private void startTransition(int dx, int dy) {
		areaX+=dx;
		areaY+=dy;
		try {
			loadNewTiles(areaX,areaY, areaFile, newTiles, newTerrain, newBackground);
		} catch (ArrayIndexOutOfBoundsException e) {
			areaX-=dx;
			areaY-=dy;
			player.x=putInRange(player.x,0,GameMain.GAME_WIDTH-player.width);
			player.y=putInRange(player.y,0,GameMain.GAME_HEIGHT-player.height);
			return;
		}
		transDX=dx*-5;
		transDY=dy*-5;
		transX=0;
		transY=0;
		transition=true;
		player.x=putInRange(player.x,0,GameMain.GAME_WIDTH-player.width);
		player.y=putInRange(player.y,0,GameMain.GAME_HEIGHT-player.height);
		player.resetWeapon();
	}
	
	private void startFadeTransition() {
		loadNewTiles(currentWarp.destAreaX,currentWarp.destAreaY,currentWarp.destAreaFile,newTiles,newTerrain,newBackground);
		fadeTransition=true;
		player.resetWeapon();
	}
	
	private void continueTransition() {
		transX+=transDX;
		transY+=transDY;
		if (Math.abs(transX)<=GameMain.GAME_WIDTH-player.width&&Math.abs(transY)<=GameMain.GAME_HEIGHT-player.height){
			player.x+=transDX;
			player.y+=transDY;
		}
		if (transX%GameMain.GAME_WIDTH==0&&transY%GameMain.GAME_HEIGHT==0) {
			transition=false;
			copyOldTilesToNew();
		}
	}
	
	private void copyOldTilesToNew() {
		for (int x=0; x<tiles.length; x++) {
			for (int y=0; y<tiles[0].length; y++) {
				tiles[x][y]=newTiles[x][y];
			}
		}
		for (int x=0; x<terrain.length; x++) {
			for (int y=0; y<terrain[0].length; y++) {
				terrain[x][y]=newTerrain[x][y];
			}
		}
		background.getGraphics().drawImage(newBackground, 0, 0, null);
	}
	
	private int putInRange(int value, int lower,int upper) {
		return Math.max(Math.min(value, upper), lower);
	}
	
	private void loadNewTiles(int loadX, int loadY, int loadFile, int[][] loadTiles, int[][] loadTerrain, BufferedImage loadImage) throws ArrayIndexOutOfBoundsException{
		if (tilesets[0]==null) {
			tilesets=Resources.loadTilesets();
		}
		Graphics g=loadImage.getGraphics();
		int[][] tileData;
		if (loadFile==0) {
			tileData=TextFileHandler.readFileInt(1+(loadX+originX)+(loadY+originY)*MAP_WIDTH, "MapData");
		} else if (loadFile==1){
			tileData=TextFileHandler.readFileInt(loadX+loadY*MAP_WIDTH, "DungeonMaps");
		} else {
			tileData=TextFileHandler.readFileInt((loadX+originX)+(loadY+originY)*MAP_WIDTH, "CaveMaps");
		}
		areaX=loadX;
		areaY=loadY;
		areaFile=loadFile;
		int index=0;
		int setFrom;
		for (int x=0; x<loadTiles.length; x++) {
			for (int y=0; y<loadTiles[0].length; y++) {
				loadTiles[x][y]=tileData[1][index]%20;
				setFrom=tileData[0][tileData[1][index]/20];
				index++;
				g.drawImage(tilesets[setFrom],x*tileWidth/2, y*tileWidth/2, (x+1)*tileWidth/2, (y+1)*tileWidth/2, loadTiles[x][y]*tileWidth/2, 0, (loadTiles[x][y]+1)*tileWidth/2, tileWidth/2, null);
				loadTiles[x][y]+=20*setFrom;
			}
		}
		enemies.clear();
		staticEntities.clear();
		drops.clear();
		projectiles.clear();
		warps.clear();
		int warpIndex=2;
		if (tileData[1].length<loadTiles.length*loadTiles[0].length+loadTerrain.length*loadTerrain[0].length) {
			for (int x=0; x<loadTerrain.length; x+=2) {
				for (int y=0; y<loadTerrain[0].length; y+=2) {
					loadTerrain[x][y]=tileData[1][index];
					loadTerrain[x+1][y]=tileData[1][index];
					loadTerrain[x][y+1]=tileData[1][index];
					loadTerrain[x+1][y+1]=tileData[1][index];
					if (tileData[1][index]==2||tileData[1][index]==8) {
						enemies.add(makeEnemyFromSpawnRates(tileData[tileData.length-1],tileWidth*x/2,tileWidth*y/2));
					} else if (tileData[1][index]==3) {
						if (ChestData.getChestID(areaX,areaY,areaFile)==0) {
							staticEntities.add(new Chest(tileWidth/2*x,tileWidth/2*y,true));
						} else {
							staticEntities.add(new Chest(tileWidth/2*x,tileWidth/2*y));
						}
					} else if (tileData[1][index]==5) {
						warps.add(new Warp(tileData[warpIndex],x,y));
						warpIndex++;
					} else if (tileData[1][index]==9) {
						warps.add(new Warp(tileData[warpIndex],x,y));
						warpIndex++;
					} else if (tileData[1][index]==6) {
						staticEntities.add(new PushBlock(tileWidth*x/2,tileWidth*y/2));
					} else if (tileData[1][index]==10) {
						staticEntities.add(new LockBlock(tileWidth*x/2,tileWidth*y/2,loadX,loadY,loadFile));
					}
					index++;
				}
			}
		} else {
			for (int x=0; x<loadTerrain.length; x++) {
				for (int y=0; y<loadTerrain[0].length; y++) {
					loadTerrain[x][y]=tileData[1][index];
					if (tileData[1][index]==2||tileData[1][index]==8) {
						enemies.add(makeEnemyFromSpawnRates(tileData[tileData.length-1],tileWidth/2*x,tileWidth/2*y));
					} else if (tileData[1][index]==3) {
						if (ChestData.getChestID(areaX,areaY,areaFile)==0) {
							staticEntities.add(new Chest(tileWidth/2*x,tileWidth/2*y,true));
						} else {
							staticEntities.add(new Chest(tileWidth/2*x,tileWidth/2*y));
						}
					} else if (tileData[1][index]==5) {
						warps.add(new Warp(tileData[warpIndex],x,y));
						warpIndex++;
					} else if (tileData[1][index]==9) {
						warps.add(new Warp(tileData[warpIndex],x,y));
						warpIndex++;
					} else if (tileData[1][index]==6) {
						staticEntities.add(new PushBlock(tileWidth/2*x,tileWidth/2*y));
					} else if (tileData[1][index]==10) {
						staticEntities.add(new LockBlock(tileWidth/2*x,tileWidth/2*y,loadX,loadY,loadFile));
					} else if (tileData[1][index]==11) {
						staticEntities.add(new ShadyDealer(tileWidth/2*x,tileWidth/2*y));
					}
					index++;
				}
			}
		}
		boolean dark=false;
		if (warpIndex<tileData.length-2) {
			if (tileData[warpIndex].length==1) {
				dark=true;
			} else {
				respawn=new Warp(tileData[warpIndex],0,0);
			}
			warpIndex++;
		}
		if (dark&&darknessOpacity==0) {
			ddark=1;
		} else if (!dark&&darknessOpacity==60) {
			ddark=-1;
		}
		for (int x=0; x<loadTerrain.length; x++) {
			for (int y=0; y<loadTerrain[0].length; y++) {
				if (loadTerrain[x][y]==8||(loadTerrain[x][y]==2&&borders(loadTerrain,x,y,4))) {
					loadTerrain[x][y]=4;
				}
			}
		}
		for (int i=0; i<enemies.size(); i++) {
			if (!enemies.get(i).collision.getPassable(getArrayEntry(enemies.get(i).x/tileWidth*2,enemies.get(i).y/tileWidth*2,loadTerrain))) {
				enemies.remove(i);
				i--;
			}
		}
		SoundPlayer.playSong(tileData[warpIndex][0]);
	}
	
	private boolean borders(int[][] terrain, int x, int y, int check) {
		if (x>0&&terrain[x-1][y]!=check) {
			return false;
		} else if (x<terrain.length-1&&terrain[x+1][y]!=check) {
			return false;
		} else if (y>0&&terrain[x][y-1]!=check) {
			return false;
		} else if (y<terrain[0].length-1&&terrain[x][y+1]!=check) {
			return false;
		}
		return true;
	}
	
	private Enemy makeEnemyFromSpawnRates(int[] enemies, int x, int y) {
		int choice=enemies[(int)Math.floor(Math.random()*enemies.length)];
		switch (choice) {
		case 0:
			return new Blob(x,y);
		case 1:
			return new Squirrel(x,y);
		case 2:
			return new SpikeTrap(x,y);
		case 3:
			return new Crab(x,y);
		case 4:
			return new Shark(x,y);
		case 5:
			return new BlockWizard(x,y);
		case 6:
			return new Snowman(x,y);
		case 7:
			return new BigSnowman(x,y);
		case 8:
			return new Mimic(x,y);
		case 9:
			return new Cactus(x,y);
		case 10:
			return new Tank(x,y);
		case 11:
			return new BigSnake(x,y);
		default:
			return new Blob(x,y);
		}
	}
	
	private void moveEntity(Entity thing) {
		thing.x+=thing.dx;
		ejectFromVertWall(thing);
		thing.y+=thing.dy;
		ejectFromHorizWall(thing);
	}
	
	private void ejectFromVertWall(Entity thing) {
		for (int y=0; y<terrain[0].length; y++) {
			if ((y+1)*tileWidth/2>thing.y&&y*tileWidth/2<thing.y+thing.height){
				for (int x=0; x<terrain.length; x++) {
					if (!thing.collision.getPassable(terrain[x][y])&&(x+1)*tileWidth/2>thing.x&&x*tileWidth/2<thing.x+thing.width) {
						thing.x=(thing.x<x*tileWidth/2) ? x*tileWidth/2-thing.width : (x+1)*tileWidth/2;
					}
				}
			}
		}
	}
	
	private void ejectFromHorizWall(Entity thing) {
		for (int x=0; x<terrain.length; x++) {
			if ((x+1)*tileWidth/2>thing.x&&x*tileWidth/2<thing.x+thing.width){
				for (int y=0; y<terrain[0].length; y++) {
					if (!thing.collision.getPassable(terrain[x][y])&&(y+1)*tileWidth/2>thing.y&&y*tileWidth/2<thing.y+thing.height) {
						thing.y=(thing.y<y*tileWidth/2) ? y*tileWidth/2-thing.height : (y+1)*tileWidth/2;
					}
				}
			}
		}
	}
	
	public void playerDamageBox(int x, int y, int width, int height) {
		for (int i=0; i<enemies.size(); i++) {
			if (enemies.get(i).knockbackCounter==0&&enemies.get(i).vulnerable&&enemies.get(i).intersects(x,y,width,height)) {
				enemies.get(i).addKnockback(player, 5);
			}
		}
	}
	
	public boolean isClear(int checkx, int checky, int width, int height) {
		for (int i=0; i<staticEntities.size(); i++) {
			if (staticEntities.get(i).intersects(checkx,checky,width,height)) {
				return false;
			}
		}
		return true;
	}
	
	private int getUnitSign(int n) {
		return (n==0) ? 0 : (n>0) ? 1 : -1;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public void render(Graphics g) {
		if (barOnTop) {
			g.translate(0,tileWidth*2);
		}
		if (transition) {
			g.drawImage(background, transX, transY, null);
			g.drawImage(newBackground, transX-GameMain.GAME_WIDTH*getUnitSign(transDX), transY-GameMain.GAME_HEIGHT*getUnitSign(transDY),null);
			g.translate(transX-GameMain.GAME_WIDTH*getUnitSign(transDX), transY-GameMain.GAME_HEIGHT*getUnitSign(transDY));
			for (int i=0; i<staticEntities.size(); i++) {
				staticEntities.get(i).render(g);
			}
			g.translate(-transX+GameMain.GAME_WIDTH*getUnitSign(transDX), -transY+GameMain.GAME_HEIGHT*getUnitSign(transDY));
		} else {
			g.drawImage(background, 0, 0, null);
			if (!(fadeTransition&&fadeCounter>=0))
			for (int i=0; i<staticEntities.size(); i++) {
				staticEntities.get(i).render(g);
			}
			if (!fadeTransition) {
				for (int i=0; i<enemies.size(); i++) {
					enemies.get(i).render(g);
				}
				for (int i=0; i<drops.size(); i++) {
					drops.get(i).render(g);
				}
				for (int i=0; i<projectiles.size(); i++) {
					projectiles.get(i).render(g);
				}
			}
		}
		player.render(g);
		if (darknessOpacity>0) {
			Path2D p=new Path2D.Double(Path2D.WIND_EVEN_ODD);
			p.append(new Rectangle(0,-1,GameMain.GAME_WIDTH,GameMain.GAME_HEIGHT+2), false);
			int radius=7;
			p.append(new Ellipse2D.Double(player.x+player.width/2-radius*tileWidth/2,player.y+player.height/2-radius*tileWidth/2,radius*tileWidth,radius*tileWidth), false);
			Graphics g2=g.create();
			if (darknessOpacity==60) {
				g2.setColor(Color.BLACK);
			} else {
				g2.setColor(new Color(0,0,0,darknessOpacity/60f));
			}
			((Graphics2D)g2).clip(p);
			g2.fillRect(0, 0, GameMain.GAME_WIDTH, GameMain.GAME_HEIGHT);
			g2.dispose();
		}
		if (fadeTransition) {
			g.setColor(new Color(0,0,0,Math.min(Math.abs(fadeCounter)/60.0f, 1)));
			g.fillRect(0,0,GameMain.GAME_WIDTH,GameMain.GAME_HEIGHT);
		} else if (gameOver) {
			g.setColor(new Color(0,0,0,Math.min(gameOverCounter/120.0f, 1)));
			g.fillRect(0,0,GameMain.GAME_WIDTH,GameMain.GAME_HEIGHT);
			g.drawImage(Resources.gameOver,GameMain.GAME_WIDTH/2-56,GameMain.GAME_HEIGHT/2,112,14,null);
		}
		if (shopping) {
			if (player.x<GameMain.GAME_WIDTH/2) {
				renderShop(g,GameMain.GAME_WIDTH/2);
			} else {
				renderShop(g,0);
			}
		}
		if (barOnTop) {
			g.translate(0,-tileWidth*2);
			renderStatusBar(g, 0);
		} else {
			renderStatusBar(g, GameMain.GAME_HEIGHT);
		}
	}
	
	private void renderStatusBar(Graphics g, int top) {
		g.setColor(Color.BLACK);
		g.fillRect(0, top, GameMain.GAME_WIDTH, tileWidth*2);
		g.setColor(Color.GREEN);
		g.fillRect(2, top+2, player.stamina/2, 5);
		g.setColor(Color.RED);
		g.fillRect(2, top+9, player.hp/2, 5);
		g.setColor(Color.GRAY);
		g.fillRect(2, top+17, tileWidth*5, tileWidth+1);
		for (int i=0; i<inventory.weapons.size(); i++) {
			inventory.weapons.get(i).renderDrop(g, 2+tileWidth*i, top+17);
		}
		g.setColor(Color.YELLOW);
		g.drawRect(2+tileWidth*inventory.currentWeapon, top+17, tileWidth, tileWidth);
		g.drawImage(Key.keyIcon, GameMain.GAME_WIDTH-tileWidth*5/2,top+tileWidth, null);
		drawNumber(inventory.keys,g,GameMain.GAME_WIDTH-tileWidth*2,top+tileWidth,2);
		g.drawImage(Money.money, GameMain.GAME_WIDTH-tileWidth*5/2, top+tileWidth*3/2, GameMain.GAME_WIDTH-tileWidth*2, top+tileWidth*2, 0, 0, tileWidth/2, tileWidth/2, null);
		drawNumber(displayMoney,g,GameMain.GAME_WIDTH-tileWidth*2,top+tileWidth*3/2,4);
	}
	
	private void renderShop(Graphics g, int left) {
		g.setColor(Color.BLACK);
		g.fillRect(left, 0, GameMain.GAME_WIDTH/2, GameMain.GAME_HEIGHT);
		for (int i=0; i<shop.length; i++) {
			shop[i].render(g);
			drawNumber(prices[i],g,left+tileWidth*2,tileWidth+tileWidth*i,1+(int)Math.floor(Math.log10(prices[i])));
		}
		g.setColor(Color.YELLOW);
		g.drawRect(left+tileWidth*3/4,shopSpot*tileWidth+tileWidth*3/4,tileWidth*4,tileWidth);
	}

	public static void drawNumber(int num, Graphics g, int x, int y, int numDigits) {
		for (int i=numDigits-1; i>=0; i--) {
			g.drawImage(Resources.numbers,i*tileWidth/2+x,y,(i+1)*tileWidth/2+x,y+tileWidth/2,(num%10)*tileWidth/2,0,(num%10+1)*tileWidth/2,tileWidth/2,null);
			num/=10;
		}
	}
	
	public static void drawString(String text, Graphics g, int x, int y, int fontSize) {
		for (int i=0; i<text.length(); i++) {
			if (text.charAt(i)!=' ') {
				int letter=text.charAt(i)-65;
				g.drawImage(Resources.letters,x+i*fontSize,y,x+(i+1)*fontSize,y+fontSize,letter*10,0,(letter+1)*10,10,null);
			}
		}
	}
	
	public static void drawCenteredString(String text, Graphics g, int x, int y, int fontSize) {
		drawString(text,g,x-(text.length()*fontSize/2),y,fontSize);
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
		case KeyEvent.VK_X:
			return 5;
		case KeyEvent.VK_Z:
		case KeyEvent.VK_SPACE:
			return 6;
		case KeyEvent.VK_ENTER:
			return 7;
		case KeyEvent.VK_Q:
			return 8;
		case KeyEvent.VK_W:
			return 9;
		case KeyEvent.VK_E:
			return 10;
		case KeyEvent.VK_R:
			return 11;
		case KeyEvent.VK_T:
			return 12;
		default:
			return -1;
		}
	}

}
