package com.ponggame.game.system;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {

    private Clip clip;
    private FloatControl volumeControl;

    public SoundManager() {
        // Constructor
    }

    public void playLoop(String filePath) {
        try {
            // Stop any currently playing music
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            // Load the sound file
            URL soundURL = getClass().getResource(filePath);
            
            if (soundURL == null) {
                System.err.println("Sound file not found: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Optional: Set Volume (0.0 to 1.0)
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // Set volume to 80% (approx -6.0 decibels)
                // You can change -6.0f to adjust volume
                float range = volumeControl.getMaximum() - volumeControl.getMinimum();
                float gain = (range * 0.8f) + volumeControl.getMinimum();
                volumeControl.setValue(gain);
            }

            // Loop continuously
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start(); // Important: Must call start!

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
    
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}