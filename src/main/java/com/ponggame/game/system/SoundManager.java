package com.ponggame.game.system;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {

    private Clip clip;
    private FloatControl volumeControl;

    private float volume = 0.8f; // default 80%
    private float lastVolume = 0.8f;
    private boolean muted = false;

    public SoundManager() {
    }

    public void playLoop(String filePath) {
        try {

            // Stop old clip
            if (clip != null) {
                clip.stop();
                clip.close();
            }

            URL soundURL = getClass().getResource(filePath);

            if (soundURL == null) {
                System.err.println("Sound file not found: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

            applyVolume();

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            e.printStackTrace();
        }
    }

    private void applyVolume() {
        if (volumeControl == null)
            return;

        if (muted) {
            volumeControl.setValue(volumeControl.getMinimum());
        } else {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float gain = min + (max - min) * volume;
            volumeControl.setValue(gain);
        }
    }

    // ===== PUBLIC CONTROLS =====

    public void setVolume(float value) {
        volume = Math.max(0f, Math.min(1f, value));
        lastVolume = volume; // ⭐ เก็บไว้
        applyVolume();
    }

    public float getVolume() {
        return volume;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;

        if (volumeControl == null)
            return;

        if (muted) {
            volumeControl.setValue(volumeControl.getMinimum());
        } else {
            // คืนค่า volume ตาม slider
            setVolume(lastVolume);
        }
    }

    public boolean isMuted() {
        return muted;
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}