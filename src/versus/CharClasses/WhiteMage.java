/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package versus.CharClasses;

import jaco.mp3.player.MP3Player;
import static versus.CharClasses.Mage.healingSound;

/**
 *
 * @author Louis
 */
public class WhiteMage extends Mage {
    
    public WhiteMage() {
        HP = 80;
        MP = 50;
        actualHP = HP;
        actualMP = MP;
        
        strength = 3;
        agility = 5;
        intelligence = 22;
        endurance = 14;
    
        java.net.URL songURL = Mage.class.getClassLoader().getResource("assets/sounds/healing.mp3");
        healingSound = new MP3Player(songURL);
    }
    
    public static int[] raiseStrength(){
        int bonus;
        int MPused = 5;
        bonus = 10;
        int bonusAndMP[] = {bonus, MPused};
        return bonusAndMP;
    }
    
    public static int[] raiseEndurance(){
        int bonus;
        int MPused = 5;
        bonus = 10;
        int bonusAndMP[] = {bonus, MPused};
        return bonusAndMP;
    }
    
    public static int[] raiseMP(){
        int bonus;
        int MPused = 5;
        bonus = 10;
        int bonusAndMP[] = {bonus, MPused};
        return bonusAndMP;
    }
}
