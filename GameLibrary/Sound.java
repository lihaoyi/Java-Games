import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.util.*;
import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.lang.Character;
import javax.imageio.*;
import java.applet.*;
import java.net.*;
class LoopingMidi implements ActionListener{
    Sequencer sequencer;
    Sequence[] sequences;
    Timer restarter = new Timer(100, this);
    boolean Repeat = true;
    int RestartDelay;
    int RestartCounter;
    char PlaySequence;
    int CurrentTrack;
    public LoopingMidi(String filename, float trestartdelay){
        RestartDelay = (int)(trestartdelay * 1000);
        try{
            sequences = new Sequence[1];
            sequences[0] = MidiSystem.getSequence(new File(System.getProperty("user.dir"), "Sounds\\" +filename));   
            sequencer = MidiSystem.getSequencer();
        }catch(Exception e){}
        play(PlaySequence);
    }
    public LoopingMidi(String[] filenames, float trestartdelay){
        RestartDelay = (int)(trestartdelay * 1000);
        sequences = new Sequence[filenames.length];
        for(int i = 0; i < sequences.length; i++){
            try{
                sequences[i] = MidiSystem.getSequence(new File(System.getProperty("user.dir"), "Sounds\\" +filenames[i]));   
                
            }catch(Exception e){}
        }
        
        try{
            sequencer = MidiSystem.getSequencer();
            play(PlaySequence);
        }catch(Exception e){}
    }
    public void actionPerformed(ActionEvent e){
        if(sequencer.isRunning() == false && Repeat == true){
            RestartCounter = RestartCounter + 100;
            if(RestartCounter > RestartDelay){
                play(PlaySequence);
                RestartCounter = 0;
            }
        }
    }
    public void stop(){
        sequencer.stop();
        restarter.stop();
        Repeat = false;
    }
    public void lastTrack(){
        Repeat = false;
        restarter.stop();
    }
    
    public void play(char tPlaySequence){
        PlaySequence = tPlaySequence;
        try{
            int INTA = 0;
            if(PlaySequence == 'r'){
                INTA = 0;
                do{
                    INTA = GUI.randomInt(0, sequences.length);
                }while(INTA == CurrentTrack && sequences.length > 1);
                
            }else if(PlaySequence == 's'){
                INTA = CurrentTrack + 1;
                if(INTA == sequences.length){
                    INTA = 0;
                }
            }
            sequencer.open();
            restarter.start();
            sequencer.setSequence(sequences[INTA]);
            CurrentTrack = INTA;
            sequencer.start();
            Repeat = true;
        }catch(Exception e){}
    }
    
}
