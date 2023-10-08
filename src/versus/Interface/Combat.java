/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package versus.Interface;
import jaco.mp3.player.MP3Player;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import versus.CharClasses.CharClass;
import versus.CharClasses.*;
import versus.Items.*;

/**
 *
 * @author Louis
 */
public class Combat extends javax.swing.JPanel {
    
    // Music and sounds
    private static Random random = new Random();
    private static MP3Player combatMusic;
    private static MP3Player victorySong;
    private static MP3Player attackSong;

    // Teams and inventories of the players
    CharClass[] P1_team;
    CharClass[] P2_team;
    Item[] P1_inventory;
    Item[] P2_inventory;
    
    // Array to keep the original positions of the label 
    Rectangle[] originalPositions = new Rectangle[8];
    
    // List containing the names of all the items of the game, to manage inventories
    String[] itemList = { "Small Life Potion",
                         "Medium Life Potion",
                         "Great Life Potion", 
                         "Small Magic Potion",
                         "Medium Magic Potion", 
                         "Great Magic Potion", 
                         "Phoenix Down",
                         "Tasty Spices", 
                         "Fine Feather", 
                         "Clay Pomade", 
                         "Shiny Crystal"};
    
    // The game is a succession of turns between the two players. A variable tells
    // which player is playing: playingPlayer ; and during each turn, there is four
    // sub-turns, which correspond to the characters of the playing player. The
    // character who is gonna make an action is called the playing character and
    // he's called in a reference named playingCharacter.
    // The team of the playing player if stored in playingTeam while the one of
    // the other player is stored in adverseTeam. We know which character have hasPlayed
    // in a turn via the array hasPlayed[]
    
    boolean victory; // becomes true when all the characters of a player are defeated
    String playingPlayer; // contains P1 or P2
    CharClass[] playingTeam; // contains the team of a player
    CharClass playingCharacter; // contains the character that has to do an action
    boolean[] hasPlayed = {false, false, false, false}; // control the sub-turns of characters
    CharClass[] adverseTeam; // contains the team of the player that is not playing
    
    // There is different types of actions the player can do with a character.
    // He can do a simple attack, use a spell (or, as we call it, an ability,
    // in the cases of the warrior, thief or monk), defend another character or
    // use an item. Each of the actions is managed by some booleans. These booleans
    // allow the player to do the action when he's clicking on the character he's
    // targeting. It tells the program he wants to do something, and not something
    // else. It's happening in the method which control what's happening when
    // the player clicks on a character: targetedCharacter().
    // When we talk about clicking on a character, we mean clicking on the JLabel
    // which visually represents the character (P1_M1, P1_M2, P1_M3, P1_M4 and
    // P2_M1, P2_M2, P2_M3, P2_M4).
    
    boolean isAttacking; // becomes true when the player clicks on the attackButton
    boolean isCasting; // becomes true when the player clicks on the button of a spell
    boolean isDefending; // becomes true when the player clicks on the defenseButton
    boolean isUsingItem; // becomes true when the player clicks on the itemsButton
    
    String itemSelected; // keeps the item a character is gonna use
    
    // When the player wants to cast a spell, when he clicks on magicButton, it doesn't
    // set isCasting on true, because he still have to choose which spell he wants to use.
    // There are 9 spells buttons available. Each turn, they will be configured relatively
    // to the spells a character can cast, with the method setSpells().
    // Each spell is relative to a button, and each button is relative to a castingCode,
    // that tells in targetedCharacter() which spell will be used.
    int castingCode;
    
    // Contains a reference to the character who is defended (index 0)
    // and a reference to the character who is defending (index 1).
    // Four characterDefense variables are created because four character can be
    // defended in the same time (two in each team).
    CharClass[] characterDefense1 = {null, null};
    CharClass[] characterDefense2 = {null, null};
    CharClass[] characterDefense3 = {null, null};
    CharClass[] characterDefense4 = {null, null};
    
    
    // ==========================
    // ==== SETTING A  MUSIC ====
    // ==========================
    private void playCombatMusic(){
         // A value between 1 and 4 is genered
        int musicChoice = random.nextInt(4) + 1;
        String musicPath;
        // A music is selected relatively to the value generated
        musicPath = switch (musicChoice) {
            case 1 -> "assets/musics/combat1.mp3";
            case 2 -> "assets/musics/combat2.mp3";
            case 3 -> "assets/musics/combat3.mp3";
            default -> "assets/musics/combat4.mp3";
        };
        // The music is loaded
        java.net.URL musicURL = getClass().getClassLoader().getResource(musicPath);
        combatMusic = new MP3Player(musicURL);
        // The music will play as long as the fight goes on
        combatMusic.setRepeat(true);
        combatMusic.play();
    }
    
    // ==============================
    // ==== SETTING A BACKGROUND ====
    // ==============================
    private void randomBattlefield(){
        int battlefieldRand = random.nextInt(33) + 1; // generates a value between 1 and 33
        // a background is chosen relatively to the value generated
        String battlefieldPath = "assets/backgrounds/"+battlefieldRand+".png";
        // the background is loaded
        java.net.URL battlefieldURL = getClass().getClassLoader().getResource(battlefieldPath);
        ImageIcon battlefieldSelected = new ImageIcon(battlefieldURL);
        // the background is set as a battlefield
        battlefield.setIcon(battlefieldSelected);
    }
    
    // ===============================================================
    // ==== SCALE THE PANEL'S ELEMENTS TO THE WINDOW'S DIMENSIONS ====
    // ===============================================================
    public void scaleMenu(int frameWidth, int frameHeight){
        // void
     }
    
    // =============================================
    // ==== CONTROL THE LABELS OF THE CHARACTER ====
    // =============================================
    
    // The combat panel contains the following JLabels: P1_M1label, P1_M2label, 
    // P1_M2label, P1_M2label, P2_M2label, P2_M2label and P2_M2label. Each one
    // is relative to the class of a character, and permits to see what is
    // the class of each chosen character. What contains these JLabels
    // is set by this method.
    private void setClassLabel(CharClass character, JLabel classLabel){
        if(character instanceof Warrior){
            classLabel.setText("Warrior");
        } else if(character instanceof Monk){
            classLabel.setText("Monk");
        } else if(character instanceof Thief){
            classLabel.setText("Thief");
        } else if(character instanceof RedMage){
            classLabel.setText("Red Mage");
        } else if(character instanceof WhiteMage){
            classLabel.setText("White Mage");
        } else{
            classLabel.setText("Black Mage");
        }
    }
    
    // The combat panel contains a JLabel named turnOfPlayer, which tells which
    // player is playing. This method changes the color of police relatively
    // to the color of the player.
    private void setColorTurnLabel(){
        Properties properties = Versus.getProperties();
        String playerColorStr;
        String[] playerColorArr;
        Color playerColor;
        if(playingPlayer.equals("P1")){
            playerColorStr = properties.getProperty("player1StartColor");
        } else{
            playerColorStr = properties.getProperty("player2StartColor");
        }
        playerColorArr = playerColorStr.split(",");
        playerColor = new Color(Integer.parseInt(playerColorArr[0]), Integer.parseInt(playerColorArr[1]), Integer.parseInt(playerColorArr[2]));
        turnOfPlayer.setForeground(playerColor);
    }
    
    // =====================================================
    // ==== GATHER THE LABELS RELATIVE TO THE CHARACTRS ====
    // =====================================================
    private JLabel getCharacterLabel(CharClass character){
        if(character == P1_team[0]){
            return P1_M1;
        } else if(character == P1_team[1]){
            return P1_M2;
        } else if(character == P1_team[2]){
            return P1_M3;
        } else if(character == P1_team[3]){
            return P1_M4;
        } else if(character == P2_team[0]){
            return P2_M1;
        } else if(character == P2_team[1]){
            return P2_M2;
        } else if(character == P2_team[2]){
            return P2_M3;
        } else{
            return P2_M4;
        }
    }
    
    private JLabel getClassLabel(CharClass character){
        if(character == P1_team[0]){
            return P1_M1label;
        } else if(character == P1_team[1]){
            return P1_M2label;
        } else if(character == P1_team[2]){
            return P1_M3label;
        } else if(character == P1_team[3]){
            return P1_M4label;
        } else if(character == P2_team[0]){
            return P2_M1label;
        } else if(character == P2_team[1]){
            return P2_M2label;
        } else if(character == P2_team[2]){
            return P2_M3label;
        } else{
            return P2_M4label;
        }
    }
    
    private JLabel getHPLabel(CharClass character){
        if(character == P1_team[0]){
            return P1_M1_HPlabel;
        } else if(character == P1_team[1]){
            return P1_M2_HPlabel;
        } else if(character == P1_team[2]){
            return P1_M3_HPlabel;
        } else if(character == P1_team[3]){
            return P1_M4_HPlabel;
        } else if(character == P2_team[0]){
            return P2_M1_HPlabel;
        } else if(character == P2_team[1]){
            return P2_M2_HPlabel;
        } else if(character == P2_team[2]){
            return P2_M3_HPlabel;
        } else{
            return P2_M4_HPlabel;
        }
    }
    
    private JLabel getMPLabel(CharClass character){
        if(character == P1_team[0]){
            return P1_M1_MPlabel;
        } else if(character == P1_team[1]){
            return P1_M2_MPlabel;
        } else if(character == P1_team[2]){
            return P1_M3_MPlabel;
        } else if(character == P1_team[3]){
            return P1_M4_MPlabel;
        } else if(character == P2_team[0]){
            return P2_M1_MPlabel;
        } else if(character == P2_team[1]){
            return P2_M2_MPlabel;
        } else if(character == P2_team[2]){
            return P2_M3_MPlabel;
        } else{
            return P2_M4_MPlabel;
        }
    }
    
    // =====================================================
    // ==== MANAGING THE ICONS OF THE CHARACTERS LABELS ====
    // =====================================================
    
    // This method permits to reverse symmetrically the JLabel of a character.
    // It is used very often for the JLabels of player's 1 characters, because natively,
    // all the characters are looking to the left side (It is how the PNG files loaded
    // are made).
    private void flipIcon(JLabel label) {
        ImageIcon originalIcon = (ImageIcon) label.getIcon();
        if (originalIcon != null) {
            // Get the original image from the ImageIcon
            ImageIcon flippedIcon = new ImageIcon(originalIcon.getImage());

            int width = flippedIcon.getIconWidth();
            int height = flippedIcon.getIconHeight();
            BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = flippedImage.createGraphics();
            g.drawImage(originalIcon.getImage(), width, 0, -width, height, null);
            g.dispose();
            flippedIcon = new ImageIcon(flippedImage);

            // Set the flipped icon on the label
            label.setIcon(flippedIcon);
        }
    }
    
    // This method set the default image loaded in the JLabel of a character, which
    // depends of the class of this character.
    private void setUnfocusedIcon(CharClass character){
        JLabel characterLabel;
        String imagePath;
        ImageIcon icon;
        java.net.URL imageURL;
        // Retriving the path of the image corresponding to the character's class
        if(character instanceof Warrior){
            imagePath = "assets/sprites/warrior_1.png";
        } else if(character instanceof Monk){
            imagePath = "assets/sprites/monk_1.png";
        } else if(character instanceof Thief){
            imagePath = "assets/sprites/thief_1.png";
        } else if(character instanceof RedMage){
            imagePath = "assets/sprites/redmage_1.png";
        } else if(character instanceof WhiteMage){
            imagePath = "assets/sprites/whitemage_1.png";
        } else{
            imagePath = "assets/sprites/blackmage_1.png";
        }
        imageURL = getClass().getClassLoader().getResource(imagePath);
        // Setting the icon to the character's label
        icon = new ImageIcon(imageURL);
        characterLabel = getCharacterLabel(character);
        characterLabel.setIcon(icon);
        // If the character belongs to player 1, it's flipped
        if(character == P1_team[0] || character == P1_team[1] || character == P1_team[2] || character == P1_team[3]){
            flipIcon(characterLabel);
        }
    }

    // Works similarly as the previous method, but is dedicated to add a focused
    // image of the playing character. It means that an image with a glowing yellow halo
    // will be loaded in the playing character's label
    private void setFocusedIcon(){
        JLabel characterLabel;
        String imagePath;
        ImageIcon icon;
        java.net.URL imageURL;
        // Retriving the path of the image corresponding to the character's class
        if(playingCharacter instanceof Warrior){
            imagePath = "assets/sprites/warrior_1_active.png";
        } else if(playingCharacter instanceof Monk){
            imagePath = "assets/sprites/monk_1_active.png";
        } else if(playingCharacter instanceof Thief){
            imagePath = "assets/sprites/thief_1_active.png";
        } else if(playingCharacter instanceof RedMage){
            imagePath = "assets/sprites/redmage_1_active.png";
        } else if(playingCharacter instanceof WhiteMage){
            imagePath = "assets/sprites/whitemage_1_active.png";
        } else{
            imagePath = "assets/sprites/blackmage_1_active.png";
        }
        imageURL = getClass().getClassLoader().getResource(imagePath);
        // Setting the icon to the character's label
        icon = new ImageIcon(imageURL);
        characterLabel = getCharacterLabel(playingCharacter);
        characterLabel.setIcon(icon);
        // If the character belongs to player 1, it's flipped
        if(playingPlayer.equals("P1")){
            flipIcon(characterLabel);
        }
    }
    
    // set a character label with a green halo. Used when the player have
    // to target the character he wants to defend
    private void setDefendableIcon(CharClass character){
        JLabel characterLabel;
        String imagePath;
        ImageIcon icon;
        java.net.URL imageURL;
        // Retriving the path of the image corresponding to the character's class
        if(character instanceof Warrior){
            imagePath = "assets/sprites/warrior_1_defendable.png";
        } else if(character instanceof Monk){
            imagePath = "assets/sprites/monk_1_defendable.png";
        } else if(character instanceof Thief){
            imagePath = "assets/sprites/thief_1_defendable.png";
        } else if(character instanceof RedMage){
            imagePath = "assets/sprites/redmage_1_defendable.png";
        } else if(character instanceof WhiteMage){
            imagePath = "assets/sprites/whitemage_1_defendable.png";
        } else{
            imagePath = "assets/sprites/blackmage_1_defendable.png";
        }
        imageURL = getClass().getClassLoader().getResource(imagePath);
        // Setting the icon to the character's label
        icon = new ImageIcon(imageURL);
        characterLabel = getCharacterLabel(character);
        characterLabel.setIcon(icon);
        // If the character belongs to player 1, it's flipped
        if(character == P1_team[0] || character == P1_team[1] || character == P1_team[2] || character == P1_team[3]){
            flipIcon(characterLabel);
        }
    }
    
    // set a character label with a green halo. Used when the player have
    // to target the character he wants to resurrect
    private void setResurrectableIcon(CharClass character){
        JLabel characterLabel;
        String imagePath;
        ImageIcon icon;
        java.net.URL imageURL;
        // Retriving the path of the image corresponding to the character's class
        if(character instanceof Warrior){
            imagePath = "assets/sprites/warrior_11_resurrectable.png";
        } else if(character instanceof Monk){
            imagePath = "assets/sprites/monk_11_resurrectable.png";
        } else if(character instanceof Thief){
            imagePath = "assets/sprites/thief_11_resurrectable.png";
        } else if(character instanceof RedMage){
            imagePath = "assets/sprites/redmage_11_resurrectable.png";
        } else if(character instanceof WhiteMage){
            imagePath = "assets/sprites/whitemage_11_resurrectable.png";
        } else{
            imagePath = "assets/sprites/blackmage_11_resurrectable.png";
        }
        imageURL = getClass().getClassLoader().getResource(imagePath);
        // Setting the icon to the character's label
        icon = new ImageIcon(imageURL);
        characterLabel = getCharacterLabel(character);
        characterLabel.setIcon(icon);
        // If the character belongs to player 1, it's flipped
        if(character == P1_team[0] || character == P1_team[1] || character == P1_team[2] || character == P1_team[3]){
            flipIcon(characterLabel);
        }
    }
    
    // set a character label with a red halo. Used when the player have
    // to target the character he wants to attack
    private void setTargetableIcon(CharClass character){
        JLabel characterLabel;
        String imagePath;
        ImageIcon icon;
        java.net.URL imageURL;
        // Retriving the path of the image corresponding to the character's class
        if(character instanceof Warrior){
            imagePath = "assets/sprites/warrior_1_targetable.png";
        } else if(character instanceof Monk){
            imagePath = "assets/sprites/monk_1_targetable.png";
        } else if(character instanceof Thief){
            imagePath = "assets/sprites/thief_1_targetable.png";
        } else if(character instanceof RedMage){
            imagePath = "assets/sprites/redmage_1_targetable.png";
        } else if(character instanceof WhiteMage){
            imagePath = "assets/sprites/whitemage_1_targetable.png";
        } else{
            imagePath = "assets/sprites/blackmage_1_targetable.png";
        }
        imageURL = getClass().getClassLoader().getResource(imagePath);
        // Setting the icon to the character's label
        icon = new ImageIcon(imageURL);
        characterLabel = getCharacterLabel(character);
        characterLabel.setIcon(icon);
        // If the character belongs to player 1, it's flipped
        if(character == P1_team[0] || character == P1_team[1] || character == P1_team[2] || character == P1_team[3]){
            flipIcon(characterLabel);
        }
    }
    
    // set an image for the dead characters
    private void setDeathIcon(CharClass character){
        JLabel characterLabel;
        String imagePath;
        ImageIcon icon;
        java.net.URL imageURL;
        // Retriving the path of the image corresponding to the character's class
        if(character instanceof Warrior){
            imagePath = "assets/sprites/warrior_11.png";
        } else if(character instanceof Monk){
            imagePath = "assets/sprites/monk_11.png";
        } else if(character instanceof Thief){
            imagePath = "assets/sprites/thief_11.png";
        } else if(character instanceof RedMage){
            imagePath = "assets/sprites/redmage_11.png";
        } else if(character instanceof WhiteMage){
            imagePath = "assets/sprites/whitemage_11.png";
        } else{
            imagePath = "assets/sprites/blackmage_11.png";
        }
        imageURL = getClass().getClassLoader().getResource(imagePath);
        // Setting the icon to the character's label
        icon = new ImageIcon(imageURL);
        characterLabel = getCharacterLabel(character);
        characterLabel.setIcon(icon);
         // If the character belongs to player 1, it's flipped
        if(character == P1_team[0] || character == P1_team[1] || character == P1_team[2] || character == P1_team[3]){
            flipIcon(characterLabel);
        }
    }
    
    // to retrieve the default position of the JLabels. to move back the defending
    // characters
    private void defaultLabelPosition(CharClass character){
        JLabel characterLabel = getCharacterLabel(character);
        
        if(character == P1_team[0]){
            characterLabel.setBounds(originalPositions[0]);
        } else if(character == P1_team[1]){
            characterLabel.setBounds(originalPositions[1]);
        } else if(character == P1_team[2]){
            characterLabel.setBounds(originalPositions[2]);
        } else if(character == P1_team[3]){
            characterLabel.setBounds(originalPositions[3]);
        } else if(character == P2_team[0]){
            characterLabel.setBounds(originalPositions[4]);
        } else if(character == P2_team[1]){
            characterLabel.setBounds(originalPositions[5]);
        } else if(character == P2_team[2]){
            characterLabel.setBounds(originalPositions[6]);
        } else{
            characterLabel.setBounds(originalPositions[7]);
        }
    }
    
    // ============================
    // ==== TO SET UP THE GAME ====
    // ============================
    public void gameSetUp(){
        
        // The game is set up each time the player clicks on the fightButton of Preparation
        // so the players can play again without restarting the game. That's the reason
        // why the game is not set up inside Combat's constructor.
        
        // load the game's background
        randomBattlefield();
        // load the game's music
        playCombatMusic();
        
        // set the UI
        attackButton.setEnabled(true);
        magicButton.setEnabled(true);
        itemsButton.setEnabled(true);
        defenseButton.setEnabled(true);
        leaveCombatPanel.setVisible(false);
        leaveCombatPanel.setBackground(new Color(0, 0, 0, 128));
        victoryLabel.setVisible(false);
        pauseButton.setVisible(true);

        // retrieve the teams created in Preparation
        P1_team = Preparation.P1_team;
        P2_team = Preparation.P2_team;
        
        // retrieve the inventories created in Preparation
        P1_inventory = Preparation.P1_inventory;
        P2_inventory = Preparation.P2_inventory;
        
        // set the icons of the characters relatively to the classes chosen by the players
        setUnfocusedIcon(P1_team[0]);
        setUnfocusedIcon(P1_team[1]);
        setUnfocusedIcon(P1_team[2]);
        setUnfocusedIcon(P1_team[3]);
        setUnfocusedIcon(P2_team[0]);
        setUnfocusedIcon(P2_team[1]);
        setUnfocusedIcon(P2_team[2]);
        setUnfocusedIcon(P2_team[3]);
        
        // set the class labels of the characters relatively to the classes chosen by the players
        for(int i = 0; i < 4; i++){
            defaultLabelPosition(P1_team[i]);
            setClassLabel(P1_team[i], getClassLabel(P1_team[i]));
        }
        for(int i = 0; i < 4; i++){
            defaultLabelPosition(P2_team[i]);
            setClassLabel(P2_team[i], getClassLabel(P2_team[i]));
        }
        
        // set the HP and MP of the characters relatively to the classes chosen by the players
        P1_M1_HPlabel.setText(P1_team[0].HP+"/"+P1_team[0].actualHP+" HP");
        P1_M1_MPlabel.setText(P1_team[0].MP+"/"+P1_team[0].actualMP+" MP");
        P1_M2_HPlabel.setText(P1_team[1].HP+"/"+P1_team[1].actualHP+" HP");
        P1_M2_MPlabel.setText(P1_team[1].MP+"/"+P1_team[1].actualMP+" MP");
        P1_M3_HPlabel.setText(P1_team[2].HP+"/"+P1_team[2].actualHP+" HP");
        P1_M3_MPlabel.setText(P1_team[2].MP+"/"+P1_team[2].actualMP+" MP");
        P1_M4_HPlabel.setText(P1_team[3].HP+"/"+P1_team[3].actualHP+" HP");
        P1_M4_MPlabel.setText(P1_team[3].MP+"/"+P1_team[3].actualMP+" MP");
        P2_M1_HPlabel.setText(P2_team[0].HP+"/"+P2_team[0].actualHP+" HP");
        P2_M1_MPlabel.setText(P2_team[0].MP+"/"+P2_team[0].actualMP+" MP");
        P2_M2_HPlabel.setText(P2_team[1].HP+"/"+P2_team[1].actualHP+" HP");
        P2_M2_MPlabel.setText(P2_team[1].MP+"/"+P2_team[1].actualMP+" MP");
        P2_M3_HPlabel.setText(P2_team[2].HP+"/"+P2_team[2].actualHP+" HP");
        P2_M3_MPlabel.setText(P2_team[2].MP+"/"+P2_team[2].actualMP+" MP");
        P2_M4_HPlabel.setText(P2_team[3].HP+"/"+P2_team[3].actualHP+" HP");
        P2_M4_MPlabel.setText(P2_team[3].MP+"/"+P2_team[3].actualMP+" MP");
        
        // initializing the game's functional variables
        victory = false;
        isAttacking = false;
        isCasting = false;
        isUsingItem = false;
        for(int i = 0; i < 4; i++){
            hasPlayed[i] = false;
        }
        characterDefense1[0] = null;
        characterDefense1[1] = null;
        characterDefense2[0] = null;
        characterDefense2[1] = null;
        characterDefense3[0] = null;
        characterDefense3[1] = null;
        characterDefense4[0] = null;
        characterDefense4[1] = null;
        
        // the player who begins to play is chosen randomly
        playingPlayer = "P"+(random.nextInt(2)+1);
        
        if(playingPlayer.equals("P1")){
            turnOfPlayer.setText("PLAYER 1'S TURN");
            playingTeam = P1_team;
            updateInventory();
            playingCharacter = P1_team[0];
            adverseTeam = P2_team;
        } else{
            turnOfPlayer.setText("PLAYER 2'S TURN");
            playingTeam = P2_team;
            updateInventory();
            playingCharacter = P2_team[0];
            adverseTeam = P1_team;
        }
        // set the spells of the playing character
        setSpells();
        // set the color of the playing player on the turnOfPlayer
        setColorTurnLabel();
        // set a halo on the playing character
        setFocusedIcon();
    }
    
    private void setSpell(JButton button, String spellName, boolean isNegative){
        button.setVisible(true);
        button.setText(spellName);
        if(isNegative){
            button.setName("negative");
        } else{
            button.setName("positive");
        }
    }
    private void setSpells(){
        // All characters don't need 9 buttons since they don't all have
        // 9 spells
        spellButton1.setVisible(false);
        spellButton2.setVisible(false);
        spellButton3.setVisible(false);
        spellButton4.setVisible(false);
        spellButton5.setVisible(false);
        spellButton6.setVisible(false);
        spellButton7.setVisible(false);
        spellButton8.setVisible(false);
        spellButton9.setVisible(false);
        
        // If the playing character is a Red Mage, White Mage or Black Mage
        if(playingCharacter instanceof Mage){
            magicButton.setText("Magic");
            // Spells for all the mage classes
            setSpell(spellButton4, "Strong Life", false);
            // Spells for White Mage and Red Mage only
            if(!(playingCharacter instanceof BlackMage)){
                setSpell(spellButton7, "Small Heal", false); //
                setSpell(spellButton8, "Medium Heal", false);
                setSpell(spellButton9, "Great Heal", false);
            }
            // Spells for Black Mage and Red Mage only
            if(playingCharacter instanceof OffensiveMage){
                setSpell(spellButton1, "Flashbolt", true);
                setSpell(spellButton2, "Thunderbolt", true);
                setSpell(spellButton3, "Stormbolt", true);
            // Spells for White Mage only
            } else{
                setSpell(spellButton1, "Strength+", false);
                setSpell(spellButton2, "Endurance+", false);
                setSpell(spellButton3, "MP+", false);
            }
        // If the playing character is a Warrior, Monk of Thief
        } else{
            magicButton.setText("Ability");
            // Spells for Warrior
            if(playingCharacter instanceof Warrior){
                setSpell(spellButton1, "Sword Slash", true);
            // Spells for Monk
            } else if(playingCharacter instanceof Monk){
                setSpell(spellButton1, "Destruction", true);
            } else{ // Spells for Thief
                setSpell(spellButton1, "Stab", true);
                //setSpell(spellButton1, "Illusion");
            }
        }
    }
    
    // =================================================================
    // ==== UPDATE THE GAME'S PROGRESS WHEN AN ACTION HAS BEEN MADE ====
    // =================================================================
    private void triggerGameUpdate() {
        
        choicePanel.setVisible(true);
        backButton.setVisible(false);
        attackPanel.setVisible(false);
        magicPanel.setVisible(false);
        menuBorders.setVisible(false);
        menuBorders.setVisible(true);
        
        // check is a player has won. To win, all the characters of a player
        // have to be defeated.
        victory = true;
        for(CharClass member : adverseTeam){
            if(member.actualHP != 0){
                victory = false; // all characters aren't defeated
                break;
            }
        }
        if(!victory){
            // checking if the player who has not played this turn has won.
            // may happen if the last character alive of the playing player was
            // a monk who used destruction, and didn't succeeded in killing the
            // adverse team
            victory = true;
            boolean adverseVictory = true;
            for(CharClass member : playingTeam){
                if(member.actualHP != 0){
                    victory = false; // all characters aren't defeated
                    adverseVictory = false;
                    break;
                }
            }
            if(adverseVictory){
                if(playingPlayer.equals("P1")){
                    playingPlayer = "P2";
                } else{
                    playingPlayer = "P1";
                }
            }
        }
        
        // if a player has won
        if(victory){
            System.out.println("Victory of "+playingPlayer); // console
            leaveCombatPanel.setVisible(true);
            attackButton.setEnabled(false);
            magicButton.setEnabled(false);
            itemsButton.setEnabled(false);
            defenseButton.setEnabled(false);
            pauseButton.setVisible(false);
            // loading an image relatively to the winner
            String imagePath = "assets/menus/"+playingPlayer+"_victory.png";
            java.net.URL imageURL = getClass().getClassLoader().getResource(imagePath);
            ImageIcon imageIcon = new ImageIcon(imageURL);
            victoryLabel.setIcon(imageIcon);
            victoryLabel.setVisible(true);
            combatMusic.stop();
            victorySong.setRepeat(true);
            victorySong.play();
        // if there is still not a winner
        } else{
            // remove the halo of the character that just did an action
            if(playingCharacter.actualHP > 0){
                setUnfocusedIcon(playingCharacter);
            }
            int i;
            // checking the deaths in the playing player's team
            for(i = 0; i < 4; i++){
                if(playingTeam[i].actualHP == 0){
                     // if a character is dead, we consider he has hasPlayed
                     // because he cannot do anything
                    hasPlayed[i] = true;
                }
            }
            // If the four characters of a the playing player have hasPlayed
            // it becomes the turn of the other player to play
            if(hasPlayed[0] && hasPlayed[1] && hasPlayed[2] && hasPlayed[3]){
                // role reversal
                if(playingTeam == P1_team){
                    turnOfPlayer.setText("PLAYER 2'S TURN");
                    playingPlayer = "P2";
                    updateInventory();
                    playingTeam = P2_team;
                    adverseTeam = P1_team;
                } else{
                    turnOfPlayer.setText("PLAYER 1'S TURN");
                    playingPlayer = "P1";
                    updateInventory();
                    playingTeam = P1_team;
                    adverseTeam = P2_team;
                }
                setColorTurnLabel();
                // hasPlayed[] is reinitialized with false value, so the characters
                // of the new playing team can do their action
                for(i = 0; i < 4; i++){
                    hasPlayed[i] = false;
                }
            }
            // checking if there are dead characters in the playing team
            for(i = 0; i < 4; i++){
                if(playingTeam[i].actualHP == 0){
                    hasPlayed[i] = true;
                }
            }

            // if the first member of the team hasn't hasPlayed, it's his turn
            if(hasPlayed[0] == false){
                playingCharacter = playingTeam[0];
            } else if(hasPlayed[1] == false){ // else, it's the second member
                playingCharacter = playingTeam[1];
            } else if(hasPlayed[2] == false){ // etc.
                playingCharacter = playingTeam[2];
            } else if(hasPlayed[3] == false){
                playingCharacter = playingTeam[3];
            }
            // update magic button
            setSpells();
            // set a halo on the playing character
            setFocusedIcon();
        }
    }

    // =====================================================
    // ==== UPDATE THE INVENTORY WHEN A NEW TURN BEGINS ====
    // =====================================================
    private void updateInventory(){
       // getting the inventories list's model
       DefaultListModel<String> inventoryList = (DefaultListModel<String>) itemsInventory.getModel();
       // retriving playing player's inventory
       Item[] inventory;
       if(playingPlayer.equals("P1")){
           inventory = P1_inventory;
       } else{
           inventory = P2_inventory;
       }
       // removing all the elements, to avoid that a player can use the items
       // of the other player
       inventoryList.removeAllElements();
       
       // if an item exists in the inventor of the playing player, has a quantity
       // superior to 0, it's added to the list
       for(int i = 0; i < itemList.length; i++){
           if(inventory[i].quantity != 0){
               inventoryList.addElement(itemList[i]+" x"+inventory[i].quantity);
           }
       }
    }
    
    // ============================================
    // ==== METHOD MANAGING THE USE OF AN ITEM ====
    // ============================================
    private void useItem(CharClass character){
        Item[] playingInventory;
        if(playingPlayer.equals("P1")){
            playingInventory = P1_inventory;
        } else{
            playingInventory = P2_inventory;
        }
        // The playing character has casted their spell and made his action for the turn
        if(playingCharacter == playingTeam[0]){
            hasPlayed[0] = true;
        } else if(playingCharacter == playingTeam[1]){
            hasPlayed[1] = true;
        } else if(playingCharacter == playingTeam[2]){
            hasPlayed[2] = true;
        } else if(playingCharacter == playingTeam[3]){
            hasPlayed[3] = true;
        }
        if(itemSelected.equals("Small Life Potion") 
        || itemSelected.equals("Medium Life Potion") 
        || itemSelected.equals("Great Life Potion")){
            int healthRecovered;
            if(itemSelected.equals("Small Life Potion")){
                healthRecovered = 20;
                playingInventory[0].quantity--;
            } else if(itemSelected.equals("Medium Life Potion")){
                healthRecovered = 40;
                playingInventory[1].quantity--;
            } else{
                healthRecovered = 60;
                playingInventory[2].quantity--;
            }
            if(character.actualHP + healthRecovered > character.HP){
                character.actualHP = character.HP;
            } else{
                character.actualHP += healthRecovered;
            }
            getHPLabel(character).setText(character.actualHP+"/"+character.HP+" HP");
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    setUnfocusedIcon(playingTeam[i]);
                }
            }
        } else if(itemSelected.equals("Small Magic Potion") 
        || itemSelected.equals("Medium Magic Potion") 
        || itemSelected.equals("Great Magic Potion")){
            int MPrecovered;
            if(itemSelected.equals("Small Magic Potion")){
                MPrecovered = 20;
                playingInventory[3].quantity--;
            } else if(itemSelected.equals("Medium Magic Potion")){
                MPrecovered = 40;
                playingInventory[4].quantity--;
            } else{
                MPrecovered = 60;
                playingInventory[5].quantity--;
            }
            if(character.actualMP + MPrecovered > character.MP){
                character.actualMP = character.MP;
            } else{
                character.actualMP += MPrecovered;
            }
            getMPLabel(character).setText(character.actualMP+"/"+character.MP+" MP");
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    setUnfocusedIcon(playingTeam[i]);
                }
            }
        } else if(itemSelected.equals("Phoenix Down")){
            setUnfocusedIcon(character);
            character.actualHP = character.HP/2;
            getHPLabel(character).setText(character.actualHP+"/"+character.HP+" HP");
            playingInventory[6].quantity--;
            // set back the death icon instead of the resurrectable icon to the
            // dead characters that weren't resurrected
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP == 0){
                    setDeathIcon(playingTeam[i]);
                }
            }
        } else if(itemSelected.equals("Tasty Spices")){
            character.strength += 20;
            playingInventory[07].quantity--;
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    setUnfocusedIcon(playingTeam[i]);
                }
            }
        } else if(itemSelected.equals("Fine Feather")){
            character.agility += 20;
           playingInventory[8].quantity--;
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    setUnfocusedIcon(playingTeam[i]);
                }
            }
        } else if(itemSelected.equals("Clay Pomade")){
            character.endurance += 20;
            playingInventory[9].quantity--;
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    setUnfocusedIcon(playingTeam[i]);
                }
            }
        } else{ // if(itemSelected.equals("Shiny Crystal"))
            character.intelligence += 20;
            playingInventory[10].quantity--;
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    setUnfocusedIcon(playingTeam[i]);
                }
            }
        }
        itemSelected = null;
        updateInventory();
        triggerGameUpdate();
    }

    // ===============================================
    // ==== METHOD ALLOWING A CHARACTER TO ATTACK ====
    // ===============================================
    private void attackedCharacter(CharClass character){
        // The playing character has attacked and made his action for the turn
        if(playingCharacter == playingTeam[0]){
            hasPlayed[0] = true;
        } else if(playingCharacter == playingTeam[1]){
            hasPlayed[1] = true;
        } else if(playingCharacter == playingTeam[2]){
            hasPlayed[2] = true;
        } else if(playingCharacter == playingTeam[3]){
            hasPlayed[3] = true;
        }
        // sound of the attack
        attackSong.play();
        
        // resistance to physical attack of the attacked character
        int physicalResistance = character.endurance/6;
        if(physicalResistance < 0){
            physicalResistance = 0;
        }
        // damages inflicted by the playing character
        int physicalDamages = ((playingCharacter.strength / (random.nextInt(2)+1)));
        if(character.actualHP - (physicalDamages - physicalResistance) < character.actualHP){
            physicalDamages -= physicalResistance;
        }
        
        // the thief may hit two times if he's agile enought on the turn
        int agilityDice = random.nextInt(playingCharacter.agility)+1;
        if(agilityDice > 10){
            physicalDamages *= 2;
            
            // terminal information
            System.out.println("The fighter is agile! He hit twice!");
            Timer anotherAttack = new Timer(500, (ActionEvent e) -> { // the message appears for 1.5 sec
                attackSong.play();
            });
            anotherAttack.setRepeats(false); // the timer has to run only once
            anotherAttack.start(); // the timer starts
            
        }
        
        // terminal informations
        System.out.println(physicalDamages+" damages inflicted");
        // If the targeted character if defended, the defending character
        // takes all the damages, and his character label returns to their
        // initial position
        if(characterDefense1[0] == character){
            character = characterDefense1[1];
            // The character has be defended and is no longer defended
            characterDefense1[0] = null;
            characterDefense1[1] = null;
            // the defending character goes back to their default place
            defaultLabelPosition(character);
        } else if(characterDefense2[0] == character){
            character = characterDefense2[1];
            // The character has be defended and is no longer defended
            characterDefense2[0] = null;
            characterDefense2[1] = null;
            // the defending character goes back to their default place
            defaultLabelPosition(character);
        } else if(characterDefense3[0] == character){
            character = characterDefense3[1];
            // The character has be defended and is no longer defended
            characterDefense3[0] = null;
            characterDefense3[1] = null;
            // the defending character goes back to their default place
            defaultLabelPosition(character);
        } else if(characterDefense4[0] == character){
            character = characterDefense4[1];
            // The character has be defended and is no longer defended
            characterDefense4[0] = null;
            characterDefense4[1] = null;
            // the defending character goes back to their default place
            defaultLabelPosition(character);
        }
        // The HP of the targeted characters are updated
        if(character.actualHP - physicalDamages > 0){
            character.actualHP -= physicalDamages;
        } else{
            character.actualHP = 0;
            setDeathIcon(character);
        }
        getHPLabel(character).setText(character.actualHP+"/"+character.HP+" HP");
        // The character has made is action and is no longer attacking
        isAttacking = false;
        // The targetable characters are unfocused because the playing character
        // is no longer looking for a target
        for(int i = 0; i < 4; i++){
            if(adverseTeam[i].actualHP > 0){
                setUnfocusedIcon(adverseTeam[i]);
            }
        }
        // The game's state is updated
        triggerGameUpdate();
    }

    // =====================================================
    // ==== METHOD ALLOWING A CHARACTER TO CAST A SPELL ====
    // =====================================================
    private void castSpell(CharClass character, int[] spell, MP3Player spellSong, boolean isNegative, String statToIncrease) {
        // The spell will be casted if the character has enought MP for it
        if(playingCharacter.actualMP >= spell[1]){
            // The playing character has casted their spell and made his action for the turn
            if(playingCharacter == playingTeam[0]){
                hasPlayed[0] = true;
            } else if(playingCharacter == playingTeam[1]){
                hasPlayed[1] = true;
            } else if(playingCharacter == playingTeam[2]){
                hasPlayed[2] = true;
            } else if(playingCharacter == playingTeam[3]){
                hasPlayed[3] = true;
            }
            // sound of the spell
            spellSong.play();
            // retrieving the spell's stats
            int Damages = spell[0];
            int MPused = spell[1];
            // The amount of MP required to cast the spell is deducted of the caster's MP
            playingCharacter.actualMP -= MPused;
            // terminal informations
            System.out.println(Damages+" damages inflicted");
            System.out.println(MPused+" MP used");
            int i;
            // If the spell used hits all the characters
            if(character == null){
                // The MP of the playing character are updated on the MP label
                JLabel playingLabel = getMPLabel(playingCharacter);
                playingLabel.setText(playingCharacter.actualMP+"/"+playingCharacter.MP+" MP");
                // If the spell is decreasing a stat
                if(isNegative){ //
                    // If a character was defending anotheer character, there is
                     // no longer defense. The defending character's character label
                     // returns to their initial position and both characters get 
                     // damages from the spell
                     for(i = 0; i < 4; i++){
                         if(characterDefense1[0] == adverseTeam[i]){
                             defaultLabelPosition(characterDefense1[1]);
                             characterDefense1[0] = null;
                             characterDefense1[1] = null;
                         }
                         if(characterDefense2[0] == adverseTeam[i]){
                             defaultLabelPosition(characterDefense2[1]);
                             characterDefense2[0] = null;
                             characterDefense2[1] = null;
                         }
                         if(characterDefense3[0] == adverseTeam[i]){
                             defaultLabelPosition(characterDefense3[1]);
                             characterDefense3[0] = null;
                             characterDefense3[1] = null;
                         }
                         if(characterDefense4[0] == adverseTeam[i]){
                             defaultLabelPosition(characterDefense4[1]);
                             characterDefense4[0] = null;
                             characterDefense4[1] = null;
                         }
                     } 
                     // The HP of the targeted characters are updated
                    for(i = 0; i < 4; i++){
                        character = adverseTeam[i];
                        if(character.actualHP - Damages > 0){
                            character.actualHP -= Damages;
                        } else{
                            character.actualHP = 0;
                            setDeathIcon(character);
                        }
                        getHPLabel(character).setText(character.actualHP+"/"+character.HP+" HP");
                    }
                // if the spell increases a stat
                } else{
                    if(statToIncrease.equals("HP")){
                        for(i = 0; i < 4; i++){
                            character = playingTeam[i];
                            if(character.actualHP > 0)
                            {
                                character.HP += Damages;
                                getHPLabel(character).setText(character.actualHP+"/"+character.HP+" HP");
                            }
                        }
                    } else if(statToIncrease.equals("actualHP")){
                        for(i = 0; i < 4; i++){
                            character = playingTeam[i];
                            if(character.actualHP > 0)
                            {
                                if(character.actualHP + Damages > character.HP){
                                    character.actualHP = character.HP;
                                } else{
                                    character.actualHP += Damages;
                                }
                                getHPLabel(character).setText(character.actualHP+"/"+character.HP+" HP");
                            }
                        }
                    } else if(statToIncrease.equals("MP")){
                        for(i = 0; i < 4; i++){
                            character = playingTeam[i];
                            if(character.actualHP > 0)
                            {
                                character.MP += Damages;
                                getMPLabel(character).setText(character.actualMP+"/"+character.MP+" MP");
                            }
                        }
                    } else if(statToIncrease.equals("strength")){
                        for(i = 0; i < 4; i++){
                            character = playingTeam[i];
                            if(character.actualHP > 0)
                            {
                                character.strength += Damages;
                            }
                        }
                    } else if(statToIncrease.equals("endurance")){
                        for(i = 0; i < 4; i++){
                            character = playingTeam[i];
                            if(character.actualHP > 0)
                            {
                                character.endurance += Damages;
                            }
                        }
                    }
                }
        // If the spell hit only one character
        } else{
            // The MP of the playing character are updated on the MP label
            JLabel playingLabel = getMPLabel(playingCharacter);
            playingLabel.setText(playingCharacter.actualMP+"/"+playingCharacter.MP+" MP");
            if(isNegative){
                // If the targeted character if defended, the defending character
                // takes all the damages, and his character label returns to their
                // initial position
                if(characterDefense1[0] == character){
                    character = characterDefense1[1];
                    // The character has be defended and is no longer defended
                    characterDefense1[0] = null;
                    characterDefense1[1] = null;
                    // the defending character goes back to their default place
                    defaultLabelPosition(character);
                } else if(characterDefense2[0] == character){
                    character = characterDefense2[1];
                    // The character has be defended and is no longer defended
                    characterDefense2[0] = null;
                    characterDefense2[1] = null;
                    // the defending character goes back to their default place
                    defaultLabelPosition(character);
                } else if(characterDefense3[0] == character){
                    character = characterDefense3[1];
                    // The character has be defended and is no longer defended
                    characterDefense3[0] = null;
                    characterDefense3[1] = null;
                    // the defending character goes back to their default place
                    defaultLabelPosition(character);
                } else if(characterDefense4[0] == character){
                    character = characterDefense4[1];
                    // The character has be defended and is no longer defended
                    characterDefense4[0] = null;
                    characterDefense4[1] = null;
                    // the defending character goes back to their default place
                    defaultLabelPosition(character);
                }

                // The HP of the targeted characters are updated
                if(character.actualHP - Damages > 0){
                    character.actualHP -= Damages;
                } else{
                    character.actualHP = 0;
                    System.out.println("The attacked character has died!");
                    setDeathIcon(character);
                }
                getHPLabel(character).setText(character.actualHP+"/"+character.HP+" HP");
            } else{
                if(statToIncrease.equals("HP")){
                    if(character.actualHP > 0){
                        character.HP += Damages;
                        getHPLabel(character).setText(character.actualHP+"/"+character.HP+" HP");
                    }
                } else if(statToIncrease.equals("actualHP")){
                    if(character.actualHP > 0){
                        if(character.actualHP + Damages > character.HP){
                            character.actualHP = character.HP;
                        } else{
                            character.actualHP += Damages;
                        }
                        getHPLabel(character).setText(character.actualHP+"/"+character.HP+" HP");
                    }
                } else if(statToIncrease.equals("MP")){
                    if(character.actualHP > 0){
                        character.MP += Damages;
                        getMPLabel(character).setText(character.actualMP+"/"+character.MP+" MP");
                    }
                } else if(statToIncrease.equals("strength")){
                    if(character.actualHP > 0){
                        System.out.println("Before strength: "+character.strength);
                        character.strength += Damages;
                        System.out.println("After strength: "+character.strength);
                    }
                } else if(statToIncrease.equals("endurance")){
                    if(character.actualHP > 0){
                        System.out.println("Before endurance: "+character.endurance);
                        character.endurance += Damages;
                        System.out.println("After endurance: "+character.endurance);
                    }
                }
            }

        }
        // The character has made is action and is no longer casting
        isCasting = false;
        // The targetable characters are unfocused because the playing character
        // is no longer looking for a target
        CharClass[] teamToUnfocus;
        if(isNegative){
            teamToUnfocus = adverseTeam;
        } else{
            teamToUnfocus = playingTeam;
        }
        for(i = 0; i < 4; i++){
            if(teamToUnfocus[i].actualHP > 0){
                setUnfocusedIcon(teamToUnfocus[i]);
            }
        }
        // The game's state is updated
        triggerGameUpdate();
        // A message appears if the character doesn't have enought mana to cast the spell
        } else{
            messagePanel.setVisible(true);
            Timer showMessage = new Timer(1500, (ActionEvent e) -> { // the message appears for 1.5 sec
                messagePanel.setVisible(false);
            });
            showMessage.setRepeats(false); // the timer has to run only once
            showMessage.start(); // the timer starts
        }
    }
    

    // ==================================
    // ==== METHOD TO DEFEND AN ALLY ====
    // ==================================
    private void defendAlly(CharClass defendedCharacter){
        // checking which character has already played
        for(int i = 0; i < 4; i++){
            if(playingCharacter == playingTeam[i]){
                hasPlayed[i] = true;
            }
        }
        // retrieving the labels of the playing and defended character
        JLabel defendedCharacterLabel = getCharacterLabel(defendedCharacter);
        JLabel playingCharacterLabel = getCharacterLabel(playingCharacter);
        // getting the position of the defended character
        Point labelPosition = defendedCharacterLabel.getLocation();
        int x = labelPosition.x;
        int y = labelPosition.y;
        // setting the position of the defending character next to the defended one
        if(playingTeam == P1_team){
            x += 60;
        } else{
            x -= 60;
        }
        playingCharacterLabel.setBounds(x, y, playingCharacterLabel.getWidth(), playingCharacterLabel.getHeight());
        // looking for a free characterDefense variable to manage the defense
        if(characterDefense1[0] == null){
            characterDefense1[0] = defendedCharacter;
            characterDefense1[1] = playingCharacter;
        } else if (characterDefense2[0] == null){
            characterDefense2[0] = defendedCharacter;
            characterDefense2[1] = playingCharacter;
        } else if (characterDefense3[0] == null){
            characterDefense3[0] = defendedCharacter;
            characterDefense3[1] = playingCharacter;
        } else if (characterDefense4[0] == null){
            characterDefense4[0] = defendedCharacter;
            characterDefense4[1] = playingCharacter;
        } 
        // unfocusing the defendable characters
        for(int i = 0; i < 4; i++){
            if(playingTeam[i].actualHP > 0){
                    setUnfocusedIcon(playingTeam[i]);
            }
        }
        isDefending = false;
        // update the game to the next turn/sub-turn
        triggerGameUpdate();
    }
    
    // ====================================================================
    // ==== METHOD TO MANAGE WHAT HAPPENS WHEN A CHARACTER IS TARGETED ====
    // ====================================================================
    private void targetedCharacter(CharClass character){
        boolean allyTarget = false;
        for(int i = 0; i < playingTeam.length; i++){
            if(playingTeam[i] == character){
                allyTarget = true;
                break; 
            }
        }
        if(isAttacking && !allyTarget && character.actualHP > 0){
            attackedCharacter(character);
        } else if(isCasting && character.actualHP > 0){
            
            if(playingCharacter instanceof Warrior && !allyTarget){
                if(castingCode == 1){
                    castSpell(character,  Warrior.swordSlash(), Warrior.swordSound, true, null);
                } else if(castingCode == 2){
                    // castSpell(character, P2_M1_HPlabel,  Warrior.mortalStrike());
                } else{
                    // castSpell(character, P2_M1_HPlabel,  Warrior.execution());
                }
            } else if(playingCharacter instanceof Monk && !allyTarget){
                if(castingCode == 1){
                    playingCharacter.actualHP = 0;
                    getHPLabel(playingCharacter).setText(playingCharacter.actualHP+"/"+playingCharacter.HP+" HP");
                    setDeathIcon(playingCharacter);
                    castSpell(null,  Monk.destruction(), Monk.explosionSound, true, null);

                }
            } else if(playingCharacter instanceof Mage){
                Mage mage = (Mage) playingCharacter;
                // All mages
                if (castingCode == 4 && allyTarget) { // 5, 6
                    castSpell(character, mage.raiseHP(), Mage.healingSound, false, "HP");
                // White Mage
                } else if(playingCharacter instanceof WhiteMage && (castingCode == 1 || castingCode == 2 || castingCode == 3) && allyTarget){
                    if(castingCode == 1){
                        castSpell(character, WhiteMage.raiseStrength(), Mage.healingSound, false, "strength");
                    } else if(castingCode == 2){
                        castSpell(character, WhiteMage.raiseEndurance(), Mage.healingSound, false, "endurance");
                    } else{ // if(castingCode == 3){
                        castSpell(character, WhiteMage.raiseMP(), Mage.healingSound, false, "MP");
                    }
                // White Mage and Black Mage
                } else if(((castingCode == 7 || castingCode == 8 || castingCode == 9 && playingCharacter instanceof RedMage) || playingCharacter instanceof WhiteMage) && allyTarget){
                    if(castingCode == 7){
                        castSpell(character, mage.smallHeal(), Mage.healingSound, false, "actualHP");
                    } else if(castingCode == 8){
                        castSpell(character, mage.mediumHeal(), Mage.healingSound, false, "actualHP");
                    } else{ // castingCode == 9
                        castSpell(null, mage.greatHeal(), Mage.healingSound, false, "actualHP");
                    } 
                // Black Mage and Red Mage
                } else if(playingCharacter instanceof OffensiveMage && !allyTarget){
                    OffensiveMage offensiveMage = (OffensiveMage) playingCharacter;
                    if(castingCode == 1) {
                        castSpell(character, offensiveMage.flashbolt(), OffensiveMage.lightningSound, true, null);
                    } else if(castingCode == 2){
                        castSpell(character, offensiveMage.thunderbolt(), OffensiveMage.lightningSound, true, null);
                    } else if(castingCode == 3){
                        castSpell(null, offensiveMage.stormbolt(), OffensiveMage.lightningSound, true, null);
                    }
                // Spells usable by healing mages only (white mage)
                }
            } else if(playingCharacter instanceof Thief && !allyTarget){
                castSpell(character, Thief.stab(), Thief.stabSound, true, null);
            }
        // To character than is targeted to be defended has to be : an ally, be alive,
        // not being already defended or defending. The character can't target himself too
        } else if(isDefending && allyTarget && character.actualHP > 0 && character != playingCharacter 
                && characterDefense1[0] != character & characterDefense1[1] != character 
                && characterDefense2[0] != character & characterDefense2[1] != character
                && characterDefense3[0] != character & characterDefense3[1] != character
                && characterDefense4[0] != character & characterDefense4[1] != character){
            defendAlly(character);
        } else if(isUsingItem && itemSelected != null && allyTarget){
            useItem(character);
        }
    }

    // #####################
    // #### CONSTRUCTOR ####
    // #####################
    public Combat() {
        initComponents();
        
        java.net.URL songURL = getClass().getClassLoader().getResource("assets/sounds/punch.mp3");
        attackSong = new MP3Player(songURL);
        
        songURL = getClass().getClassLoader().getResource("assets/musics/victory.mp3");
        victorySong = new MP3Player(songURL);
        
        messagePanel.setVisible(false);
        
        magicPanel.setVisible(false);
        
        
        originalPositions[0] = P1_M1.getBounds();
        originalPositions[1] = P1_M2.getBounds();
        originalPositions[2] = P1_M3.getBounds();
        originalPositions[3] = P1_M4.getBounds();
        
        originalPositions[4] = P2_M1.getBounds();
        originalPositions[5] = P2_M2.getBounds();
        originalPositions[6] = P2_M3.getBounds();
        originalPositions[7] = P2_M4.getBounds();
        
        // spell buttons are hidden by default and we be set up relatively
        // to the class of the playing character
        spellButton1.setVisible(false);
        spellButton2.setVisible(false);
        spellButton3.setVisible(false);
        spellButton4.setVisible(false);
        spellButton5.setVisible(false);
        spellButton6.setVisible(false);
        spellButton7.setVisible(false);
        spellButton8.setVisible(false);
        spellButton9.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leaveCombatPanel = new javax.swing.JPanel();
        backToPreparationMenu = new javax.swing.JButton();
        victoryLabel = new javax.swing.JLabel();
        messagePanel = new javax.swing.JPanel();
        message = new javax.swing.JLabel();
        turnOfPlayer = new javax.swing.JLabel();
        menuBorders = new javax.swing.JLabel();
        pauseButton = new javax.swing.JButton();
        P1_teamStats = new keeptoo.KGradientPanel();
        P1_M1label = new javax.swing.JLabel();
        P1_M1_HPlabel = new javax.swing.JLabel();
        P1_M1_MPlabel = new javax.swing.JLabel();
        P1_M3label = new javax.swing.JLabel();
        P1_M3_HPlabel = new javax.swing.JLabel();
        P1_M3_MPlabel = new javax.swing.JLabel();
        P1_M2label = new javax.swing.JLabel();
        P1_M2_HPlabel = new javax.swing.JLabel();
        P1_M2_MPlabel = new javax.swing.JLabel();
        P1_M4label = new javax.swing.JLabel();
        P1_M4_HPlabel = new javax.swing.JLabel();
        P1_M4_MPlabel = new javax.swing.JLabel();
        actionPanel = new keeptoo.KGradientPanel();
        choicePanel = new keeptoo.KGradientPanel();
        itemsButton = new javax.swing.JButton();
        attackButton = new javax.swing.JButton();
        defenseButton = new javax.swing.JButton();
        magicButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        magicPanel = new keeptoo.KGradientPanel();
        spellButton1 = new javax.swing.JButton();
        spellButton2 = new javax.swing.JButton();
        spellButton3 = new javax.swing.JButton();
        spellButton4 = new javax.swing.JButton();
        spellButton5 = new javax.swing.JButton();
        spellButton6 = new javax.swing.JButton();
        spellButton7 = new javax.swing.JButton();
        spellButton8 = new javax.swing.JButton();
        spellButton9 = new javax.swing.JButton();
        itemsPanel = new keeptoo.KGradientPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemsInventory = new javax.swing.JList<>();
        defensePanel = new keeptoo.KGradientPanel();
        attackPanel = new keeptoo.KGradientPanel();
        P2_teamStats = new keeptoo.KGradientPanel();
        P2_M1label = new javax.swing.JLabel();
        P2_M1_HPlabel = new javax.swing.JLabel();
        P2_M1_MPlabel = new javax.swing.JLabel();
        P2_M3label = new javax.swing.JLabel();
        P2_M3_HPlabel = new javax.swing.JLabel();
        P2_M3_MPlabel = new javax.swing.JLabel();
        P2_M2label = new javax.swing.JLabel();
        P2_M2_HPlabel = new javax.swing.JLabel();
        P2_M2_MPlabel = new javax.swing.JLabel();
        P2_M4label = new javax.swing.JLabel();
        P2_M4_HPlabel = new javax.swing.JLabel();
        P2_M4_MPlabel = new javax.swing.JLabel();
        P1_M4 = new javax.swing.JLabel();
        P1_M2 = new javax.swing.JLabel();
        P1_M3 = new javax.swing.JLabel();
        P1_M1 = new javax.swing.JLabel();
        P2_M1 = new javax.swing.JLabel();
        P2_M2 = new javax.swing.JLabel();
        P2_M3 = new javax.swing.JLabel();
        P2_M4 = new javax.swing.JLabel();
        battlefield = new javax.swing.JLabel();

        setLayout(null);

        backToPreparationMenu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        backToPreparationMenu.setText("Back to Preparation Menu");
        backToPreparationMenu.setFocusable(false);
        backToPreparationMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToPreparationMenuActionPerformed(evt);
            }
        });

        victoryLabel.setIcon(new javax.swing.ImageIcon("E:\\Versus\\Versus\\src\\assets\\menus\\P1_victory.png")); // NOI18N

        javax.swing.GroupLayout leaveCombatPanelLayout = new javax.swing.GroupLayout(leaveCombatPanel);
        leaveCombatPanel.setLayout(leaveCombatPanelLayout);
        leaveCombatPanelLayout.setHorizontalGroup(
            leaveCombatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leaveCombatPanelLayout.createSequentialGroup()
                .addGroup(leaveCombatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leaveCombatPanelLayout.createSequentialGroup()
                        .addGap(510, 510, 510)
                        .addComponent(backToPreparationMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leaveCombatPanelLayout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(victoryLabel)))
                .addContainerGap(224, Short.MAX_VALUE))
        );
        leaveCombatPanelLayout.setVerticalGroup(
            leaveCombatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leaveCombatPanelLayout.createSequentialGroup()
                .addContainerGap(184, Short.MAX_VALUE)
                .addComponent(victoryLabel)
                .addGap(18, 18, 18)
                .addComponent(backToPreparationMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(195, 195, 195))
        );

        add(leaveCombatPanel);
        leaveCombatPanel.setBounds(0, 0, 1280, 720);

        message.setBackground(new java.awt.Color(255, 255, 255));
        message.setFont(new java.awt.Font("Source Sans Pro ExtraLight", 0, 24)); // NOI18N
        message.setText("Not enought Magic Points!");

        javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(message)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(message, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        add(messagePanel);
        messagePanel.setBounds(500, 110, 280, 50);

        turnOfPlayer.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        turnOfPlayer.setForeground(new java.awt.Color(0, 255, 255));
        turnOfPlayer.setText("PLAYER 1'S TURN");
        add(turnOfPlayer);
        turnOfPlayer.setBounds(547, 487, 210, 30);

        menuBorders.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/combat_border.png"))); // NOI18N
        add(menuBorders);
        menuBorders.setBounds(0, 462, 1280, 258);

        pauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/pause_button.png"))); // NOI18N
        pauseButton.setBorder(null);
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusPainted(false);
        pauseButton.setName("playButton"); // NOI18N
        pauseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pauseButtonMouseClicked(evt);
            }
        });
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });
        add(pauseButton);
        pauseButton.setBounds(575, 0, 133, 60);

        P1_teamStats.setkEndColor(new java.awt.Color(255, 102, 102));
        P1_teamStats.setkStartColor(new java.awt.Color(255, 204, 204));
        P1_teamStats.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        P1_M1label.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        P1_M1label.setText("Warrior");
        P1_M1label.setToolTipText("");
        P1_teamStats.add(P1_M1label, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 110, -1));

        P1_M1_HPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P1_M1_HPlabel.setText("85/85 HP");
        P1_teamStats.add(P1_M1_HPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 110, -1));

        P1_M1_MPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P1_M1_MPlabel.setText("60/60 MP");
        P1_teamStats.add(P1_M1_MPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 110, -1));

        P1_M3label.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        P1_M3label.setText("Warrior");
        P1_M3label.setToolTipText("");
        P1_teamStats.add(P1_M3label, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 110, -1));

        P1_M3_HPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P1_M3_HPlabel.setText("85/85 HP");
        P1_teamStats.add(P1_M3_HPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 110, -1));

        P1_M3_MPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P1_M3_MPlabel.setText("60/60 MP");
        P1_teamStats.add(P1_M3_MPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 110, -1));

        P1_M2label.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        P1_M2label.setText("Warrior");
        P1_M2label.setToolTipText("");
        P1_teamStats.add(P1_M2label, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 110, -1));

        P1_M2_HPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P1_M2_HPlabel.setText("85/85 HP");
        P1_teamStats.add(P1_M2_HPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 110, -1));

        P1_M2_MPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P1_M2_MPlabel.setText("60/60 MP");
        P1_teamStats.add(P1_M2_MPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 110, -1));

        P1_M4label.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        P1_M4label.setText("Warrior");
        P1_M4label.setToolTipText("");
        P1_teamStats.add(P1_M4label, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 110, -1));

        P1_M4_HPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P1_M4_HPlabel.setText("85/85 HP");
        P1_teamStats.add(P1_M4_HPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 110, -1));

        P1_M4_MPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P1_M4_MPlabel.setText("60/60 MP");
        P1_teamStats.add(P1_M4_MPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, 110, -1));

        add(P1_teamStats);
        P1_teamStats.setBounds(20, 555, 320, 150);

        actionPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        choicePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        itemsButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        itemsButton.setText("Items");
        itemsButton.setFocusable(false);
        itemsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsButtonActionPerformed(evt);
            }
        });
        choicePanel.add(itemsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 81, 246, 60));

        attackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        attackButton.setText("Attack");
        attackButton.setFocusable(false);
        attackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attackButtonActionPerformed(evt);
            }
        });
        choicePanel.add(attackButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 12, 246, 60));

        defenseButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        defenseButton.setText("Defense");
        defenseButton.setFocusable(false);
        defenseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defenseButtonActionPerformed(evt);
            }
        });
        choicePanel.add(defenseButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 12, 246, 60));

        magicButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        magicButton.setText("Magic");
        magicButton.setFocusable(false);
        magicButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                magicButtonActionPerformed(evt);
            }
        });
        choicePanel.add(magicButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 81, 246, 60));

        actionPanel.add(choicePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 160));

        backButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/back_button.png"))); // NOI18N
        backButton.setBorder(null);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setName("playButton"); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        actionPanel.add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 110, 60));

        magicPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        spellButton1.setText("jButton1");
        spellButton1.setFocusable(false);
        spellButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spellButton1ActionPerformed(evt);
            }
        });
        magicPanel.add(spellButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 17, 106, 32));

        spellButton2.setText("jButton1");
        spellButton2.setFocusable(false);
        spellButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spellButton2ActionPerformed(evt);
            }
        });
        magicPanel.add(spellButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 17, 106, 32));

        spellButton3.setText("jButton1");
        spellButton3.setFocusable(false);
        spellButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spellButton3ActionPerformed(evt);
            }
        });
        magicPanel.add(spellButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(392, 17, 106, 32));

        spellButton4.setText("jButton1");
        spellButton4.setFocusable(false);
        spellButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spellButton4ActionPerformed(evt);
            }
        });
        magicPanel.add(spellButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 61, 106, 32));

        spellButton5.setText("jButton1");
        spellButton5.setFocusable(false);
        spellButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spellButton5ActionPerformed(evt);
            }
        });
        magicPanel.add(spellButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 61, 106, 32));

        spellButton6.setText("jButton2");
        spellButton6.setFocusable(false);
        spellButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spellButton6ActionPerformed(evt);
            }
        });
        magicPanel.add(spellButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(392, 61, 106, 32));

        spellButton7.setText("jButton1");
        spellButton7.setFocusable(false);
        spellButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spellButton7ActionPerformed(evt);
            }
        });
        magicPanel.add(spellButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 106, 106, 32));

        spellButton8.setText("jButton1");
        spellButton8.setFocusable(false);
        spellButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spellButton8ActionPerformed(evt);
            }
        });
        magicPanel.add(spellButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 106, 106, 32));

        spellButton9.setText("jButton3");
        spellButton9.setFocusable(false);
        spellButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spellButton9ActionPerformed(evt);
            }
        });
        magicPanel.add(spellButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(392, 106, 106, 32));

        actionPanel.add(magicPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 160));

        itemsInventory = new javax.swing.JList<>(new DefaultListModel<>());
        itemsInventory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemsInventoryMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(itemsInventory);

        javax.swing.GroupLayout itemsPanelLayout = new javax.swing.GroupLayout(itemsPanel);
        itemsPanel.setLayout(itemsPanelLayout);
        itemsPanelLayout.setHorizontalGroup(
            itemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, itemsPanelLayout.createSequentialGroup()
                .addContainerGap(243, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        itemsPanelLayout.setVerticalGroup(
            itemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemsPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        actionPanel.add(itemsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 160));

        javax.swing.GroupLayout defensePanelLayout = new javax.swing.GroupLayout(defensePanel);
        defensePanel.setLayout(defensePanelLayout);
        defensePanelLayout.setHorizontalGroup(
            defensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );
        defensePanelLayout.setVerticalGroup(
            defensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        actionPanel.add(defensePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 160));

        javax.swing.GroupLayout attackPanelLayout = new javax.swing.GroupLayout(attackPanel);
        attackPanel.setLayout(attackPanelLayout);
        attackPanelLayout.setHorizontalGroup(
            attackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );
        attackPanelLayout.setVerticalGroup(
            attackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        actionPanel.add(attackPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 160));

        add(actionPanel);
        actionPanel.setBounds(370, 555, 540, 160);

        P2_teamStats.setkEndColor(new java.awt.Color(0, 102, 204));
        P2_teamStats.setkStartColor(new java.awt.Color(0, 255, 255));
        P2_teamStats.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        P2_M1label.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        P2_M1label.setText("Warrior");
        P2_M1label.setToolTipText("");
        P2_teamStats.add(P2_M1label, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 110, -1));

        P2_M1_HPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P2_M1_HPlabel.setText("85/85 HP");
        P2_teamStats.add(P2_M1_HPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 110, -1));

        P2_M1_MPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P2_M1_MPlabel.setText("60/60 MP");
        P2_teamStats.add(P2_M1_MPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 110, -1));

        P2_M3label.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        P2_M3label.setText("Warrior");
        P2_M3label.setToolTipText("");
        P2_teamStats.add(P2_M3label, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 110, -1));

        P2_M3_HPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P2_M3_HPlabel.setText("85/85 HP");
        P2_teamStats.add(P2_M3_HPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 110, -1));

        P2_M3_MPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P2_M3_MPlabel.setText("60/60 MP");
        P2_teamStats.add(P2_M3_MPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 110, -1));

        P2_M2label.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        P2_M2label.setText("Warrior");
        P2_M2label.setToolTipText("");
        P2_teamStats.add(P2_M2label, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 110, -1));

        P2_M2_HPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P2_M2_HPlabel.setText("85/85 HP");
        P2_teamStats.add(P2_M2_HPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 110, -1));

        P2_M2_MPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P2_M2_MPlabel.setText("60/60 MP");
        P2_teamStats.add(P2_M2_MPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 110, -1));

        P2_M4label.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        P2_M4label.setText("Warrior");
        P2_M4label.setToolTipText("");
        P2_teamStats.add(P2_M4label, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 110, -1));

        P2_M4_HPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P2_M4_HPlabel.setText("85/85 HP");
        P2_teamStats.add(P2_M4_HPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 110, -1));

        P2_M4_MPlabel.setFont(new java.awt.Font("Yu Gothic", 0, 15)); // NOI18N
        P2_M4_MPlabel.setText("60/60 MP");
        P2_teamStats.add(P2_M4_MPlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, 110, -1));

        add(P2_teamStats);
        P2_teamStats.setBounds(940, 555, 320, 150);

        P1_M4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P1_M4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P1_M4MouseClicked(evt);
            }
        });
        add(P1_M4);
        P1_M4.setBounds(280, 390, 70, 100);

        P1_M2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P1_M2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P1_M2MouseClicked(evt);
            }
        });
        add(P1_M2);
        P1_M2.setBounds(170, 230, 70, 100);

        P1_M3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P1_M3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P1_M3MouseClicked(evt);
            }
        });
        add(P1_M3);
        P1_M3.setBounds(170, 340, 70, 100);

        P1_M1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P1_M1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P1_M1MouseClicked(evt);
            }
        });
        add(P1_M1);
        P1_M1.setBounds(280, 150, 70, 100);

        P2_M1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P2_M1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P2_M1MouseClicked(evt);
            }
        });
        add(P2_M1);
        P2_M1.setBounds(960, 150, 70, 100);

        P2_M2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P2_M2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P2_M2MouseClicked(evt);
            }
        });
        add(P2_M2);
        P2_M2.setBounds(1070, 210, 70, 100);

        P2_M3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P2_M3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P2_M3MouseClicked(evt);
            }
        });
        add(P2_M3);
        P2_M3.setBounds(1070, 330, 80, 100);

        P2_M4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P2_M4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P2_M4MouseClicked(evt);
            }
        });
        add(P2_M4);
        P2_M4.setBounds(960, 390, 70, 100);

        battlefield.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/backgrounds/1.png"))); // NOI18N
        add(battlefield);
        battlefield.setBounds(0, 0, 1280, 720);
    }// </editor-fold>//GEN-END:initComponents

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        if(!leaveCombatPanel.isVisible()){
            leaveCombatPanel.setVisible(true);
            attackButton.setEnabled(false);
            magicButton.setEnabled(false);
            itemsButton.setEnabled(false);
            defenseButton.setEnabled(false);
        } else{
            leaveCombatPanel.setVisible(false);
            attackButton.setEnabled(true);
            magicButton.setEnabled(true);
            itemsButton.setEnabled(true);
            defenseButton.setEnabled(true);
        }
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void pauseButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pauseButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pauseButtonMouseClicked

    private void backToPreparationMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToPreparationMenuActionPerformed
        this.setVisible(false);
        Versus.getPreparation().setVisible(true);
        combatMusic.stop();
        victorySong.stop();
        Preparation.playPreparationMusic();
        
        magicPanel.setVisible(false);
        choicePanel.setVisible(true);
        
        Versus.getPreparation().P1_readyButton.setEnabled(true);
        Versus.getPreparation().P2_readyButton.setEnabled(true);
        Versus.getPreparation().preparation_fightButton.setEnabled(false);
        Versus.getPreparation().P1_itemSelection.setVisible(false);
        Versus.getPreparation().P2_itemSelection.setVisible(false);
        Versus.getPreparation().P1_classSelection.setVisible(true);
        Versus.getPreparation().P2_classSelection.setVisible(true);
        Versus.getPreparation().displayClassInformations(Versus.getPreparation().P1_first);
    }//GEN-LAST:event_backToPreparationMenuActionPerformed

    private void itemsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsButtonActionPerformed
        isAttacking = false;
        isDefending = false;
        
        backButton.setVisible(true);
        attackPanel.setVisible(false);
        magicPanel.setVisible(false);
        itemsPanel.setVisible(true);
        defensePanel.setVisible(false);
        choicePanel.setVisible(false);
        menuBorders.setVisible(false);
        menuBorders.setVisible(true);
    }//GEN-LAST:event_itemsButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        if(isAttacking || isCasting){
            for(int i = 0; i < 4; i++){
                if(adverseTeam[i].actualHP > 0){
                    setUnfocusedIcon(adverseTeam[i]);
                }
            }
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    if(playingTeam[i] == playingCharacter){
                        setFocusedIcon();
                    } else{
                        setUnfocusedIcon(playingTeam[i]);
                    }
                }
            }
        } else if(isDefending){
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    // The playingCharacter keeps hi
                    if(playingTeam[i] != playingCharacter){
                        setUnfocusedIcon(playingTeam[i]);
                    }
                }
            }
        } else{
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    // The playingCharacter keeps hi
                    if(playingTeam[i] != playingCharacter){
                        setUnfocusedIcon(playingTeam[i]);
                    }
                } else{
                    setDeathIcon(playingTeam[i]);
                }
            }
        }
        isAttacking = false;
        isCasting = false;
        isUsingItem = false;
        backButton.setVisible(false);
        attackPanel.setVisible(false);
        magicPanel.setVisible(false);
        itemsPanel.setVisible(false);
        defensePanel.setVisible(false);
        choicePanel.setVisible(true);
        menuBorders.setVisible(false);
        menuBorders.setVisible(true);
    }//GEN-LAST:event_backButtonActionPerformed

    private void attackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attackButtonActionPerformed
        isAttacking = true;
        
        // in case the player would have clicked on defenseButton before
        isDefending = false;
        for(int i = 0; i < 4; i++){
            if(playingTeam[i].actualHP > 0){
                // A character can only defend another one
                if(playingTeam[i] != playingCharacter){
                    setUnfocusedIcon(playingTeam[i]);
                }
            }
        }
        
        for(int i = 0; i < 4; i++){
            if(adverseTeam[i].actualHP > 0){
                setTargetableIcon(adverseTeam[i]);
            }
        }
    }//GEN-LAST:event_attackButtonActionPerformed

    private void P2_M1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_M1MouseClicked
        targetedCharacter(P2_team[0]);
    }//GEN-LAST:event_P2_M1MouseClicked

    private void magicButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_magicButtonActionPerformed
        isAttacking = false;
        isDefending = false;
        for(int i = 0; i < 4; i++){
            if(playingCharacter != playingTeam[i] && playingTeam[i].actualHP > 0){
                setUnfocusedIcon(playingTeam[i]);
            }
        }
        for(int i = 0; i < 4; i++){
            if(adverseTeam[i].actualHP > 0){
                setUnfocusedIcon(adverseTeam[i]);
            }
        }

        backButton.setVisible(true);
        attackPanel.setVisible(false);
        magicPanel.setVisible(true);
        itemsPanel.setVisible(false);
        defensePanel.setVisible(false);
        choicePanel.setVisible(false);
        menuBorders.setVisible(false);
        menuBorders.setVisible(true);
    }//GEN-LAST:event_magicButtonActionPerformed

    private void defenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defenseButtonActionPerformed
        // If the playing character is not being defended, he can defend
        if(characterDefense1[0] != playingCharacter && characterDefense2[0] != playingCharacter && characterDefense3[0] != playingCharacter && characterDefense4[0] != playingCharacter){
            isDefending = true;
            
            // in case the player would have clicked on defenseButton before
            isAttacking = false;
            for(int i = 0; i < 4; i++){
                if(adverseTeam[i].actualHP > 0){
                    setUnfocusedIcon(adverseTeam[i]);
                }
            }
            
            
            for(int i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    // A character can only defend another one
                    if(playingTeam[i] != playingCharacter){
                        if(playingTeam[i] != characterDefense1[0] && playingTeam[i] != characterDefense1[1]
                        && playingTeam[i] != characterDefense2[0] && playingTeam[i] != characterDefense2[1]
                        && playingTeam[i] != characterDefense3[0] && playingTeam[i] != characterDefense3[1]
                        && playingTeam[i] != characterDefense4[0] && playingTeam[i] != characterDefense4[1]){
                            setDefendableIcon(playingTeam[i]);
                        }
                        
                    }
                }
            }
            
        }
    }//GEN-LAST:event_defenseButtonActionPerformed

    private void P2_M2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_M2MouseClicked
        targetedCharacter(P2_team[1]);
    }//GEN-LAST:event_P2_M2MouseClicked

    private void P2_M3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_M3MouseClicked
        targetedCharacter(P2_team[2]);
    }//GEN-LAST:event_P2_M3MouseClicked

    private void P2_M4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_M4MouseClicked
        targetedCharacter(P2_team[3]);
    }//GEN-LAST:event_P2_M4MouseClicked

    private void P1_M2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_M2MouseClicked
        targetedCharacter(P1_team[1]);
    }//GEN-LAST:event_P1_M2MouseClicked

    private void P1_M1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_M1MouseClicked
        targetedCharacter(P1_team[0]);
    }//GEN-LAST:event_P1_M1MouseClicked

    private void P1_M3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_M3MouseClicked
        targetedCharacter(P1_team[2]);
    }//GEN-LAST:event_P1_M3MouseClicked

    private void P1_M4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_M4MouseClicked
        targetedCharacter(P1_team[3]);
    }//GEN-LAST:event_P1_M4MouseClicked

    // ========================================================
    // ==== SETTING PROPER UI MODIFICATIONS TO CAST SPELLS ====
    // ========================================================
    // the code taken in argument will determine which spell is casted with the button
    private void setUIcastingSpell(int code, JButton button){
        int i;
        isCasting = true;
        castingCode = code;
        backButton.setVisible(true);
        attackPanel.setVisible(false);
        magicPanel.setVisible(true);
        itemsPanel.setVisible(false);
        defensePanel.setVisible(false);
        choicePanel.setVisible(false);
        menuBorders.setVisible(false);
        menuBorders.setVisible(true);

        // Setting the default icon, for the cases where the player could
        // have clicked on a negative spell effect, and then a good one, and
        // vice-versa
        for(i = 0; i < 4; i++){
            if(playingTeam[i].actualHP > 0){
                if(playingTeam[i] != playingCharacter){
                    setUnfocusedIcon(playingTeam[i]);
                }
            }
        }
        for(i = 0; i < 4; i++){
            if(adverseTeam[i].actualHP > 0){
                setUnfocusedIcon(adverseTeam[i]);
            }
        }
        
        // setting the fitting halo to the characters
        if(button.getName().equals("negative")){
            for(i = 0; i < 4; i++){
                if(adverseTeam[i].actualHP > 0){
                    setTargetableIcon(adverseTeam[i]);
                }
            }
        } else{
            for(i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    if(playingTeam[i] != playingCharacter){
                        setDefendableIcon(playingTeam[i]);
                    }
                }
            }
        }
    }
    
    private void spellButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellButton5ActionPerformed
        setUIcastingSpell(5, spellButton5);
    }//GEN-LAST:event_spellButton5ActionPerformed

    private void spellButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellButton1ActionPerformed
        setUIcastingSpell(1, spellButton1);
    }//GEN-LAST:event_spellButton1ActionPerformed

    private void spellButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellButton2ActionPerformed
        setUIcastingSpell(2, spellButton2);
    }//GEN-LAST:event_spellButton2ActionPerformed

    private void spellButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellButton3ActionPerformed
        setUIcastingSpell(3, spellButton3);
    }//GEN-LAST:event_spellButton3ActionPerformed

    private void spellButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellButton4ActionPerformed
        setUIcastingSpell(4, spellButton4);
    }//GEN-LAST:event_spellButton4ActionPerformed

    private void spellButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellButton6ActionPerformed
        setUIcastingSpell(6, spellButton6);
    }//GEN-LAST:event_spellButton6ActionPerformed

    private void spellButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellButton7ActionPerformed
        setUIcastingSpell(7, spellButton7);
    }//GEN-LAST:event_spellButton7ActionPerformed

    private void spellButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellButton8ActionPerformed
        setUIcastingSpell(8, spellButton8);
    }//GEN-LAST:event_spellButton8ActionPerformed

    private void spellButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spellButton9ActionPerformed
        setUIcastingSpell(9, spellButton9);
    }//GEN-LAST:event_spellButton9ActionPerformed

    private void itemsInventoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemsInventoryMouseClicked
        isUsingItem = true;
        itemSelected = itemsInventory.getSelectedValue();
        itemSelected = itemSelected.replaceAll(" x\\d+", "");
        int i;
        // ùùùùù
        
        if(itemSelected.equals("Small Life Potion")
                || itemSelected.equals("Small Life Potion") 
                || itemSelected.equals("Medium Life Potion")
                || itemSelected.equals("Great Life Potion")
                || itemSelected.equals("Small Magic Potion")
                || itemSelected.equals("Medium Magic Potion")
                || itemSelected.equals("Great Magic Potion")
                || itemSelected.equals("Tasty Spices")
                || itemSelected.equals("Fine Feather")
                || itemSelected.equals("Clay Pomade")
                || itemSelected.equals("Shiny Crystal")
                ){
            
            for(i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    if(playingTeam[i] != playingCharacter){
                        setUnfocusedIcon(playingTeam[i]);
                    }
                } else{
                    setDeathIcon(playingTeam[i]);
                }
            }
            
            for(i = 0; i < 4; i++){
                if(playingTeam[i].actualHP > 0){
                    if(playingTeam[i] != playingCharacter){
                        setDefendableIcon(playingTeam[i]);
                    }
                } else{
                    setDeathIcon(playingTeam[i]);
                }
            }
            
        } else{ // itemSelected.equals("Phoenix Down")
            for(i = 0; i < 4; i++){
                if(playingTeam[i].actualHP == 0){
                    if(playingTeam[i] != playingCharacter){
                        setResurrectableIcon(playingTeam[i]);
                    }
                } else{
                    if(playingTeam[i] == playingCharacter){
                        setFocusedIcon();
                    } else{
                        setUnfocusedIcon(playingTeam[i]);
                    }
                }
            }
        }
    }//GEN-LAST:event_itemsInventoryMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel P1_M1;
    private javax.swing.JLabel P1_M1_HPlabel;
    private javax.swing.JLabel P1_M1_MPlabel;
    private javax.swing.JLabel P1_M1label;
    protected javax.swing.JLabel P1_M2;
    private javax.swing.JLabel P1_M2_HPlabel;
    private javax.swing.JLabel P1_M2_MPlabel;
    private javax.swing.JLabel P1_M2label;
    protected javax.swing.JLabel P1_M3;
    private javax.swing.JLabel P1_M3_HPlabel;
    private javax.swing.JLabel P1_M3_MPlabel;
    private javax.swing.JLabel P1_M3label;
    protected javax.swing.JLabel P1_M4;
    private javax.swing.JLabel P1_M4_HPlabel;
    private javax.swing.JLabel P1_M4_MPlabel;
    private javax.swing.JLabel P1_M4label;
    protected keeptoo.KGradientPanel P1_teamStats;
    private javax.swing.JLabel P2_M1;
    private javax.swing.JLabel P2_M1_HPlabel;
    private javax.swing.JLabel P2_M1_MPlabel;
    private javax.swing.JLabel P2_M1label;
    private javax.swing.JLabel P2_M2;
    private javax.swing.JLabel P2_M2_HPlabel;
    private javax.swing.JLabel P2_M2_MPlabel;
    private javax.swing.JLabel P2_M2label;
    private javax.swing.JLabel P2_M3;
    private javax.swing.JLabel P2_M3_HPlabel;
    private javax.swing.JLabel P2_M3_MPlabel;
    private javax.swing.JLabel P2_M3label;
    private javax.swing.JLabel P2_M4;
    private javax.swing.JLabel P2_M4_HPlabel;
    private javax.swing.JLabel P2_M4_MPlabel;
    private javax.swing.JLabel P2_M4label;
    protected keeptoo.KGradientPanel P2_teamStats;
    protected keeptoo.KGradientPanel actionPanel;
    protected javax.swing.JButton attackButton;
    protected keeptoo.KGradientPanel attackPanel;
    protected javax.swing.JButton backButton;
    protected javax.swing.JButton backToPreparationMenu;
    private javax.swing.JLabel battlefield;
    protected keeptoo.KGradientPanel choicePanel;
    private javax.swing.JButton defenseButton;
    protected keeptoo.KGradientPanel defensePanel;
    private javax.swing.JButton itemsButton;
    protected javax.swing.JList<String> itemsInventory;
    protected keeptoo.KGradientPanel itemsPanel;
    private javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JPanel leaveCombatPanel;
    private javax.swing.JButton magicButton;
    protected keeptoo.KGradientPanel magicPanel;
    private javax.swing.JLabel menuBorders;
    private javax.swing.JLabel message;
    protected javax.swing.JPanel messagePanel;
    protected javax.swing.JButton pauseButton;
    private javax.swing.JButton spellButton1;
    private javax.swing.JButton spellButton2;
    private javax.swing.JButton spellButton3;
    private javax.swing.JButton spellButton4;
    private javax.swing.JButton spellButton5;
    private javax.swing.JButton spellButton6;
    private javax.swing.JButton spellButton7;
    private javax.swing.JButton spellButton8;
    private javax.swing.JButton spellButton9;
    private javax.swing.JLabel turnOfPlayer;
    private javax.swing.JLabel victoryLabel;
    // End of variables declaration//GEN-END:variables
}
