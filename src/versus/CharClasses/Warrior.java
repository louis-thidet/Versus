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
public class Warrior extends CharClass {
    private static Random random = new Random();
    public static MP3Player swordSound;

    public Warrior() {
        HP = 170;
        MP = 15;
        actualHP = HP;
        actualMP = MP;
        
        strength = 32;
        agility = 12;
        intelligence = 5;
        endurance = 25;
        
        java.net.URL songURL = Warrior.class.getClassLoader().getResource("assets/sounds/sword.mp3");
        swordSound = new MP3Player(songURL);
    }
    
    public static int[] swordSlash(){
        int Damages;
        int MPused = 5;
        Damages = 20 + (random.nextInt(25));
        int damagesAndMP[] = {Damages, MPused};
        return damagesAndMP;
    }
}
