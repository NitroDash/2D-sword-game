package game.model;

import game.main.GameMain;
import game.main.Resources;
import game.main.SoundPlayer;
import game.state.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Reed on 7/8/2017.
 */
public class BigSnake extends SnakeSegment {

    private static final BufferedImage head= Resources.loadImage("SnakeHead.png");

    private static int numKilled=0;
    private int killCounter;

    public BigSnake(int x, int y) {
        this.x=x;
        this.y=y;
        this.height=30;
        this.width=40;
        this.damage=5;
        this.maxhp=25;
        this.hp=maxhp;
        dangerous=true;
        vulnerable=false;
        collision=TerrainType.INCORPOREAL;
        direction=0;
        waterLevel=0;
        if (numKilled!=0) {
            hp=0;
        }
    }

    @Override
    public void update() {
        if (hp==0&&numKilled!=0) {
            return;
        }
        if (killCounter>1) {
            killCounter--;
            if (killCounter==1) {
                if (direction==0) {
                    dx=4;
                } else {
                    dx=-4;
                }
            }
        }
        if (knockbackCounter>0) {
            knockbackCounter--;
            if (direction==0) {
                dx=4;
            } else {
                dx=-4;
            }
            vulnerable=false;
            if (hp==0) {
                hp=1;
                knockbackCounter=0;
                killCounter=120;
                image=2;
                dx=0;
            } else {
                image=0;
            }
        }
        if (next==null) {
            pickJumpLocation();
            SoundPlayer.playSong(4);
            next=new SnakeBody(this, 20);
        }
        if (x>peakX+GameMain.GAME_WIDTH||x<peakX-GameMain.GAME_WIDTH) {
            pickJumpLocation();
        }
        setHeightToPath();
        if (dx*(getCenter()-peakX)>0&&(direction-0.5)*dx>0&&Math.abs(y+height-waterLevel)<5) {
            if (GameState.currentState.getTerrain((direction==0?getLeft():getRight())/GameState.tileWidth*2,(y+height)/GameState.tileWidth*2)!=4) {
                dx=0;
                image=1;
                vulnerable=true;
            }
        }
        if (vulnerable) {
            SnakeSegment.moveCenterTowards(getCenter(),y);
        }
        if (killCounter==1&&y>=waterLevel) {
            hp=0;
            SoundPlayer.playSong(1);
        }
    }

    private void pickJumpLocation() {
        do {
            x=(int)(Math.random()*(GameMain.GAME_WIDTH-width));
            y=(int)(Math.random()*(GameMain.GAME_HEIGHT-height));
        } while (!GameState.currentState.onlyOn(this,4));
        if (x<GameMain.GAME_WIDTH/2) {
            direction=1;
            peakX=getCenter()+50;
            x=peakX-GameMain.GAME_WIDTH/2;
            dx=4;
        } else {
            direction=0;
            peakX=getCenter()-50;
            x=peakX+GameMain.GAME_WIDTH/2;
            dx=-4;
        }
        peakY=y-height/2;
        waterLevel=y+height;
        y=GameMain.GAME_HEIGHT;
    }

    @Override
    public void render(Graphics g) {
        if (numKilled!=0) {
            return;
        }
        if (y<waterLevel) {
            int renderX=x;
            int renderWidth=width;
            if (direction==0) {
                renderX+=width;
                renderWidth*=-1;
            }
            g.drawImage(head,renderX,y,renderX+renderWidth,Math.min(y+height,waterLevel),image*40,0,image*40+40,Math.min(30,waterLevel-y),null);
        }
    }

    public Drop getDrop() {
        if (numKilled==0) {
            numKilled++;
            return new HealthExtender(x,y);
        } else {
            return null;
        }
    }
}
