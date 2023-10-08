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
public abstract class Mage extends CharClass {
    private static Random random = new Random();
    public static MP3Player healingSound;
    
    public int[] raiseHP(){
        int bonus;
        int MPused = 10;
        bonus = 10;
        int HPAndMP[] = {bonus, MPused};
        return HPAndMP;
    }
    
    public int[] smallHeal(){
        int HPhealed = (random.nextInt(2)+1)*this.intelligence;
        int MPused = 10;
        int HPAndMP[] = {HPhealed, MPused};
        return HPAndMP;
    }
    public int[] mediumHeal(){
        int HPhealed = (random.nextInt(3)+1)*this.intelligence;
        int MPused = 20;
        int HPAndMP[] = {HPhealed, MPused};
        return HPAndMP;
    }
    public int[] greatHeal(){
        int HPhealed = (random.nextInt(4)+1)*this.intelligence;
        int MPused = 40;
        int HPAndMP[] = {HPhealed, MPused};
        return HPAndMP;
    }
    
    
    
    public Mage(){
        java.net.URL songURL = Mage.class.getClassLoader().getResource("assets/sounds/healing.mp3");
        healingSound = new MP3Player(songURL);
    }
}
