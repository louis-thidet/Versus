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
public class Monk extends CharClass{
    private static Random random = new Random();
    public static MP3Player explosionSound;

    public Monk() {
        HP = 130;
        MP = 0;
        actualHP = HP;
        actualMP = MP;
        
        strength = 40;
        agility = 8;
        intelligence = 0;
        endurance = 15;
        
        java.net.URL songURL = Warrior.class.getClassLoader().getResource("assets/sounds/explosion.mp3");
        explosionSound = new MP3Player(songURL);
    }
    
    public static int[] destruction(){
        int Damages;
        int MPused = 0;
        Damages = 10 * (random.nextInt(5)+1);
        int damagesAndMP[] = {Damages, MPused};
        return damagesAndMP;
    }
}
