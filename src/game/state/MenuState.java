package game.state;

import game.main.GameMain;
import game.main.Resources;
import game.main.SoundPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MenuState extends State {

	int menuSpot,menu;
	
	private boolean[] keys;
	
	private static final String[][] menus={{"START","OPTIONS"/*,"SECRET MAP EDITOR","WORLD VIEW"*/},{"BACK","MUSIC","HUD"},{"BACK","PIANO","EIGHT BIT"},{"BACK","TOP","BOTTOM"}};
	
	@Override
	public void init() {
		menuSpot=0;
		menu=0;
		keys=new boolean[3];
	}

	@Override
	public void update(float delta) {
		if (keys[0]) {
			menuSpot--;
			if (menuSpot<0) {
				menuSpot=menus[menu].length-1;
			}
			keys[0]=false;
		}
		if (keys[1]) {
			menuSpot++;
			if (menuSpot>=menus[menu].length) {
				menuSpot=0;
			}
			keys[1]=false;
		}
		if (keys[2]) {
			keys[2]=false;
			switch(menu) {
			case 0:
				switch(menuSpot) {
				case 0:
					GameState.currentState=new GameState();
					setCurrentState(GameState.currentState);
					return;
				case 1:
					menu=1;
					menuSpot=0;
					return;
				case 2:
					setCurrentState(new MapMaker());
					return;
				case 3:
					setCurrentState(new WorldViewState());
					return;
				}
			case 1:
				switch(menuSpot) {
					case 0:
						menu = 0;
						menuSpot = 0;
						return;
					case 1:
						menu = 2;
						menuSpot = 0;
						return;
					case 2:
						menu = 3;
						menuSpot = 0;
						return;
					case 3:
						menu = 4;
						menuSpot = 0;
						return;
				}
			case 2:
				switch(menuSpot) {
				case 0:
					menu=1;
					menuSpot=0;
					return;
				case 1:
					SoundPlayer.playPiano=true;
					menu=1;
					menuSpot=0;
					return;
				case 2:
					SoundPlayer.playPiano=false;
					menu=1;
					menuSpot=0;
					return;
				}
			case 3:
				switch(menuSpot) {
				case 0:
					menu=1;
					menuSpot=0;
					return;
				case 1:
					GameState.barOnTop=true;
					menu=1;
					menuSpot=0;
					return;
				case 2:
					GameState.barOnTop=false;
					menu=1;
					menuSpot=0;
					return;
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0,GameMain.GAME_WIDTH, GameMain.WINDOW_HEIGHT);
		g.setColor(Color.WHITE);
		GameState.drawCenteredString("INSERT TITLE HERE",g,GameMain.GAME_WIDTH/2,10,20);
		for (int i=0; i<menus[menu].length; i++) {
			GameState.drawCenteredString(menus[menu][i], g, GameMain.GAME_WIDTH/2, 100+20*i, 10);
		}
		g.drawRect(100, menuSpot*20+95, GameMain.GAME_WIDTH-200, 20);
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
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			return 2;
		default:
			return -1;
		}
	}

}
