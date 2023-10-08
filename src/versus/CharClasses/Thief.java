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
public class Thief extends CharClass {
    private static Random random = new Random();
    public static MP3Player stabSound;

    public Thief() {
        HP = 100;
        MP = 20;
        actualHP = HP;
        actualMP = MP;
        
        strength = 23;
        agility = 32;
        intelligence = 15;
        endurance = 22;
        
        java.net.URL songURL = getClass().getClassLoader().getResource("assets/sounds/stab.mp3");
        stabSound = new MP3Player(songURL);
    }
    
    public static int[] stab(){
        int Damages;
        int MPused = 5;
        Damages = 18 + (random.nextInt(35));
        int damagesAndMP[] = {Damages, MPused};
        return damagesAndMP;
    }
}
