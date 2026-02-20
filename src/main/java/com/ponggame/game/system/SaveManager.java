package com.ponggame.game.system;

import java.io.*;

public class SaveManager {

    public static void saveSettings(){
        try(ObjectOutputStream out = 
            new ObjectOutputStream(new FileOutputStream("config.dat"))){

            out.writeObject(Config.masterVolume);
            out.writeObject(Config.bgmVolume);
            out.writeObject(Config.sfxVolume);
            out.writeObject(Config.aiDifficulty);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void loadSettings(){
        try(ObjectInputStream in = 
            new ObjectInputStream(new FileInputStream("config.dat"))){

            Config.masterVolume = (float) in.readObject();
            Config.bgmVolume = (float) in.readObject();
            Config.sfxVolume = (float) in.readObject();
            Config.aiDifficulty = (int) in.readObject();

        }catch(Exception ignored){}
    }
}
