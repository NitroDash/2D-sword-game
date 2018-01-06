package game.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

public class SoundPlayer {
	
	static Sequencer sequencer;
	static int music=Integer.MIN_VALUE;
	
	public static boolean playPiano=false;
	
	public static void playSong(String filename) {
		if (playPiano) {
			filename+="_piano.mid";
		} else {
			filename+=".mid";
		}
		try {
			if (sequencer==null) {
				sequencer=MidiSystem.getSequencer();
				sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
				sequencer.open();
			} else {
				sequencer.stop();
			}
			InputStream is=new BufferedInputStream(SoundPlayer.class.getResourceAsStream("/Music/"+filename));
			sequencer.setSequence(is);
			sequencer.start();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	public static void playSong(int songID) {
		if (songID==music) {
			return;
		}
		music=songID;
		if (sequencer!=null) {
			if (songID<0) {
				sequencer.setLoopCount(0);
			} else {
				sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			}
		}
		switch (songID) {
		case -1:
			playSong("GameOver");
			break;
		case 0:
			playSong("Overworld");
			break;
		case 1:
			playSong("Dungeon");
			break;
		case 2:
			playSong("Water");
			break;
		case 3:
			playSong("Ice");
			break;
		case 4:
			if (Math.random()<0.2) {
				playSong("RickrollBoss");
			} else {
				playSong("Boss");
			}
			break;
		case 5:
			playSong("Desert");
			break;
		case 6:
			playSong("Cave");
			break;
		}
	}
}
