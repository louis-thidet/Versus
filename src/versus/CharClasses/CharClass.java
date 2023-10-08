/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package versus.CharClasses;


/**
 *
 * @author Louis
 */
public abstract class CharClass{
    
    public int actualHP;
    public int actualMP;
    public int HP;
    public int MP;
    public int strength;
    public int agility;
    public int intelligence;
    public int endurance;
    
    public int[] getStats() {
        int[] stats = {HP, MP, strength, agility, intelligence, endurance};
        return stats;
    }
}