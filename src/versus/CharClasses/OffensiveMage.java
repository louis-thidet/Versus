/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package versus.CharClasses;

import jaco.mp3.player.MP3Player;
import java.util.Random;

/**
 *
 * @author Louis
 */
public abstract class OffensiveMage extends Mage {
    private static Random random = new Random();
    public static MP3Player lightningSound;
    
    public int[] flashbolt(){
        int Damages;
        int MPused = 8;
        Damages = (this.intelligence + (random.nextInt(5)+1));
        int damagesAndMP[] = {Damages, MPused};
        return damagesAndMP;
    }
    
    public int[] thunderbolt(){
        int Damages;
        int MPused = 14;
        Damages = (this.intelligence * (random.nextInt(5)+1)/3);
        int damagesAndMP[] = {Damages, MPused};
        return damagesAndMP;
    }
    
    public int[] stormbolt(){
        int Damages;
        int MPused = 25;
        Damages = (this.intelligence * (random.nextInt(5)+1)/2);
        int damagesAndMP[] = {Damages, MPused};
        return damagesAndMP;
    }
    
    public OffensiveMage(){
        java.net.URL songURL = OffensiveMage.class.getClassLoader().getResource("assets/sounds/lightning.mp3");
        lightningSound = new MP3Player(songURL);
    }
}
