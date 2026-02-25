package com.ponggame.game.system;

import javax.sound.sampled.*;
import java.io.InputStream;

public class PixelSound {

    public static void playClick() {

        try {

            InputStream is =
                PixelSound.class.getResourceAsStream("/sound/click.wav");

            if (is == null) {
                System.out.println("❌ click.wav NOT FOUND");
                return;
            }

            AudioInputStream audio =
                AudioSystem.getAudioInputStream(is);

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}