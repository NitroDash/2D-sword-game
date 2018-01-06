package game.model;

public class ChestData {
	
	public static int[][][][][] chestContents=new int[2][2][3][20][20];
	
	public static boolean[][][][][] locks=new boolean[2][2][3][20][20];
	
	public static void initChestContents() {
		setChestID(0,0,0,7);//Starting area
		setChestID(0,2,0,2);//Lake w/ sword
		setChestID(-1,2,0,1);//Smiley isles
		setChestID(-1,-1,0,1);//Icy chest puzzle
		setChestID(2,2,0,1);//Riverside chest puzzle
		setChestID(3,-1,0,2);//Forest fork
		setChestID(2,0,0,1);//Maze
		setChestID(0,0,1,3);//Sword chamber
		setChestID(1,0,1,1);//Shark maze
		setChestID(2,0,1,1);//Squirrel hideout
		setChestID(2,2,1,5);//Dungeon 2 key 1
		setChestID(3,2,1,5);//Dungeon 2 key 2
		setChestID(4,2,0,1);//Post-maze
		setChestID(4,1,0,5);//Cave top
		setChestID(1,3,1,5);//Dungeon 3 key 1
		setChestID(2,3,1,5);//Dungeon 3 key 2
		setChestID(5,2,0,8);//Ruins chest
		setChestID(7,2,1,1);//Dungeon 4 money chest
		setChestID(8,2,1,5);//Dungeon 4 key
	}
	
	public static int getChestID(int x, int y, int file) {
		int xSector=0;
		if (x<0) {
			xSector=1;
			x*=-1;
		}
		int ySector=0;
		if (y<0) {
			ySector=1;
			y*=-1;
		}
		return chestContents[xSector][ySector][file][x][y];
	}
	
	public static void setChestID(int x, int y, int file, int value) {
		int xSector=0;
		if (x<0) {
			xSector=1;
			x*=-1;
		}
		int ySector=0;
		if (y<0) {
			ySector=1;
			y*=-1;
		}
		chestContents[xSector][ySector][file][x][y]=value;
	}
	
	public static boolean getUnlocked(int x, int y, int file) {
		int xSector=0;
		if (x<0) {
			xSector=1;
			x*=-1;
		}
		int ySector=0;
		if (y<0) {
			ySector=1;
			y*=-1;
		}
		return locks[xSector][ySector][file][x][y];
	}
	
	public static void unlock(int x, int y, int file) {
		int xSector=0;
		if (x<0) {
			xSector=1;
			x*=-1;
		}
		int ySector=0;
		if (y<0) {
			ySector=1;
			y*=-1;
		}
		locks[xSector][ySector][file][x][y]=true;
	}
	
	public static Drop getChestContents(int id, Chest opened) {
		switch (id) {
		case 1:
			return new Money(0,0,4);
		case 2:
			return new Heart(0,0);
		case 3:
			return new WeaponDrop(new Sword(),0,0);
		case 4:
			return new WeaponDrop(new Boomerang(),0,0);
		case 5:
			return new Key(0,0);
		case 6:
			return new WeaponDrop(new BallNChain(),0,0);
		case 7:
			return new Money(0,0,3);
		case 8:
			return new Money(0,0,5);
		}
		return null;
	}
}
