/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package versus.Interface;

import jaco.mp3.player.MP3Player;
import java.awt.Color;
import java.awt.Image;
import java.util.Random;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import versus.CharClasses.*;
import versus.Items.*;

/**
 *
 * @author Louis
 */
public class Preparation extends javax.swing.JPanel {
    private static MP3Player preparationMusic; 
    
    // variables used to manage to choice of items
    int P1_credits = 10;
    int P2_credits = 10;
    
    // variables used to manage the start of the game
    Boolean P1_ready = false;
    Boolean P2_ready = false;

    // variables used to receive the choices of team and inventories of the players
    public static CharClass[] P1_team;
    public static CharClass[] P2_team;
    public static Item[] P1_inventory;
    public static Item[] P2_inventory;
    
    // ===========================
    // ==== MANAGE THE  MUSIC ====
    // ===========================
    public static void playPreparationMusic(){
        
        java.net.URL musicURL = Preparation.class.getClassLoader().getResource("assets/musics/preparation_menu.mp3");
        preparationMusic = new MP3Player(musicURL);

        preparationMusic.setRepeat(true);
        preparationMusic.play();
    }
    
    public static void stopPreparationMusic() {
        if (preparationMusic != null) {
            preparationMusic.stop();
        }
    }
    
    // ===============================================================
    // ==== SCALE THE PANEL'S ELEMENTS TO THE WINDOW'S DIMENSIONS ====
    // ===============================================================
    public void scaleMenu(int frameWidth, int frameHeight){

         // scaling menuBorders
         double widthScale = (double) frameWidth / menuBorders.getIcon().getIconWidth();
         double heightScale = (double) frameHeight / menuBorders.getIcon().getIconHeight();
         double scale = Math.min(widthScale, heightScale);

         Image scaledImage = ((ImageIcon) menuBorders.getIcon()).getImage().getScaledInstance(
             (int) (menuBorders.getIcon().getIconWidth() * scale),
             (int) (menuBorders.getIcon().getIconHeight() * scale),
             Image.SCALE_SMOOTH);

         ImageIcon scaledIcon = new ImageIcon(scaledImage);
         menuBorders.setIcon(scaledIcon);
         menuBorders.setBounds(0, 0, frameWidth, frameHeight); // x, y, width, height
         
         //...
     }

    // ==============================================
    // ==== TO CHOOSE THE CLASS OF A TEAM MEMBER ====
    // ==============================================
    public void selectClass(JLabel member, JComboBox classSelected){
        String imagePath;
        ImageIcon icon;
        
        // This Method is called when the value of a JComboBox is changed
        // Relatively to the value chosen in a JComboBox, which permits to
        // choose a class, the icon of the label representing the team member's 
        // is changed.
        
        if(classSelected.getSelectedItem().equals("Warrior")){
            imagePath = "assets/sprites/warrior_1.png";
        } else if(classSelected.getSelectedItem().equals("Monk")){
            imagePath = "assets/sprites/monk_1.png";
        } else if(classSelected.getSelectedItem().equals("Thief")){
            imagePath = "assets/sprites/thief_1.png";
        } else if(classSelected.getSelectedItem().equals("Red Mage")){
            imagePath = "assets/sprites/redmage_1.png";
        } else if(classSelected.getSelectedItem().equals("White Mage")){
            imagePath = "assets/sprites/whitemage_1.png";
        } else{
            imagePath = "assets/sprites/blackmage_1.png";
        }
        
        // Instead of just doing icon = new ImageIcon("src/assets/sprites/whitemage_1.png");
        // to work the images correctly and to make them able to appear in the built .jar
        // we have to use the classLoader
        java.net.URL imageURL = getClass().getClassLoader().getResource(imagePath);
        icon = new ImageIcon(imageURL);
        
        // the icon is sat
        member.setIcon(icon);
        // The value gathered in the JComboBox is applied to the label
        // it's used in the description (see displayClassInformations() method).
        member.setText(classSelected.getSelectedItem().toString());
    }
    
    // ================================================================
    // ==== TO RANDOMLY CHOOSE THE CLASSES OF ALL THE TEAM MEMBERS ====
    // ================================================================
    public void randomClassChoices(int playerNumber){
        Random random = new Random();
        
        int first = random.nextInt(6); 
        int second = random.nextInt(6); 
        int third = random.nextInt(6); 
        int fourth = random.nextInt(6);
        
        if(playerNumber == 1){
            P1_firstSelection.setSelectedIndex(first);
            P1_secondSelection.setSelectedIndex(second);
            P1_thirdSelection.setSelectedIndex(third);
            P1_fourthSelection.setSelectedIndex(fourth);
        }
        else{
            P2_firstSelection.setSelectedIndex(first);
            P2_secondSelection.setSelectedIndex(second);
            P2_thirdSelection.setSelectedIndex(third);
            P2_fourthSelection.setSelectedIndex(fourth);
        }
    }
    
    // ==============================================================================
    // ==== GETTING THE DESCRIPTION OF A CLASS WHEN THE MOUSE HOVERS ON A SPRITE ====
    // ==============================================================================
    protected void displayClassInformations(JLabel teamMember){
        
        // The JLabel called in the function contains in it's Icon propriety the sprite of the 
        // class chosen by the player. The method checks the value of the text of the label and 
        // shows informations relative to  the class corresponding to the text ; the texts which 
        // always corresponds to the icon.
        
        String descriptionText;
        int HP;
        int MP;
        int strength;
        int agility;
        int intelligence;
        int endurance;
        CharClass example; // an instance of the class chosen is created to get it's stats
        
        if(teamMember.getText().equals("Warrior") || teamMember.getText().equals("")){ // default JLabel is empty
            descriptionText = "The Warrior is a formidable melee combatant with high hit "
                    + "points and exceptional strength and endurance, making them a "
                    + "tough adversary on the battlefield. However, their low intelligence "
                    + "and agility indicate a reliance on brute force rather than finesse. ";
            example = new Warrior();
        } else if(teamMember.getText().equals("Monk")){
            descriptionText = "The Monk is a physical powerhouse with high hit points and exceptional "
                            + "strength and endurance. They lack magical abilities and intelligence but "
                            + "excel in close combat.";
            example = new Monk();
        } else if(teamMember.getText().equals("Thief")){
            descriptionText = "The Thief is agile and stealthy, excelling in agility and "
                            + "endurance, with moderate hit points and low magic points."
                            + "They possess high agility and decent strength, adept at evading "
                            + "foes and picking locks. They also have some illusion spells";
            example = new Thief();
        } else if(teamMember.getText().equals("Red Mage")){
            descriptionText = "The Red Mage is a versatile character with balanced stats. "
                            + "They have moderate hit points and magic points, suitable "
                            + "for both physical and magical challenges. With decent strength, "
                            + "agility, intelligence, and endurance, they adapt well to various "
                            + "combat scenarios.";
            example = new RedMage();
        } else if(teamMember.getText().equals("White Mage")){
            descriptionText = "The White Mage is a dedicated healer and support character "
                            + "with moderate hit points and ample magic points for healing "
                            + "and defense. They have low physical, strength and agility "
                            + "making them better suited for non-combat roles. "
                            + "High intelligence and endurance enable them to aid allies effectively";
            example = new WhiteMage();
        } else{
            descriptionText = "The Black Mage specializes in intelligence and offensive magic, "
                            + "with low hit points and physical strength, leaving them vulnerable"
                            + "in close combat. They excel as spellcasters but have low "
                            + "agility, endurance. Their power lies in their mastery of "
                            + "destructive spells.";
            example = new BlackMage();
        }
        
        int[] classStats = example.getStats(); // the stats are gathered in the an array
        HP = classStats[0];
        MP = classStats[1];
        strength = classStats[2];
        agility = classStats[3];
        intelligence = classStats[4];
        endurance = classStats[5];
            
        // The class' informations are shown on the screen in the proper objects.
        HPLabel.setText("Health Points: "+HP);
        MPLabel.setText("Magic Points: "+MP);
        strengthLabel.setText("Strength: "+strength);
        agilityLabel.setText("Agility: "+agility);
        intelligenceLabel.setText("Intelligence: "+intelligence);
        enduranceLabel.setText("Endurance: "+endurance);
        description.setText(descriptionText);
    }
    
    // ==============================================================================
    // ==== GETTING THE DESCRIPTION OF AN ITEM WHEN THE MOUSE HOVERS ON A SPRITE ====
    // ==============================================================================
    protected void displayItemInformations(String itemSelected){

        HPLabel.setText("");
        MPLabel.setText("");
        strengthLabel.setText("");
        agilityLabel.setText("");
        intelligenceLabel.setText("");
        enduranceLabel.setText("");
        
        String descriptionText;
        String precision; // precise the nature of an item, when it shares similarity with some other items
        String precisionBis; // same
        String preciseEffect = "";

        // LIFE POTIONS
        if(itemSelected.contains("Life Potion")){
            preciseEffect = "HP is restored";
            if(itemSelected.contains("Small")){
                precision = "Small";
                precisionBis = "little amount";
                preciseEffect = "20 "+preciseEffect;
            } else if(itemSelected.contains("Medium")){
                precision = "Medium";
                precisionBis = "a good amount";
                preciseEffect = "40 "+preciseEffect;
            } else{
                precision = "Great";
                precisionBis = "a lot";
                preciseEffect = "60 "+preciseEffect;
            }
            descriptionText = "The "+precision+" Life Potion restores a "
                    + ""+precisionBis+" of HP to the one who drink it";
        // MAGIC POTIONS
        } else if(itemSelected.contains("Magic Potion")){
            preciseEffect = "MP is restored";
            if(itemSelected.contains("Small")){
                precision = "Small";
                precisionBis = "little amount";
                preciseEffect = "20 "+preciseEffect;
            } else if(itemSelected.contains("Medium")){
                precision = "Medium";
                precisionBis = "a good amount";
                preciseEffect = "40 "+preciseEffect;
            } else{
                precision = "Great";
                precisionBis = "a lot";
                preciseEffect = "60 "+preciseEffect;
            }
            descriptionText = "The "+precision+" Magic Potion restores a "
                    + ""+precisionBis+" of MP to the one who drink it";
        // PHOENIX DOWN
        } else if(itemSelected.contains("Phoenix Down")){
            descriptionText = "Item to revive an ally";
        /*// ITEMS TO CURE NEGATIVE EFFECTS
        } else if(itemSelected.contains("Philosopher's Stone") ||
                itemSelected.contains("Antidote") ||
                itemSelected.contains("Glasses") ||
                itemSelected.contains("Talking Grass") ||
                itemSelected.contains("Volatile Water")){

             if(itemSelected.contains("Philosopher's Stone")){
                precision = "Philosopher's Stone";
                precisionBis = "all the effects";
            } else if(itemSelected.contains("Antidote")){
                precision = "Antidote";
                precisionBis = "the poison effect";
            } else if(itemSelected.contains("Glasses")){
                precision = "Glasses";
                precisionBis = "the blindness effect";
            } else if(itemSelected.contains("Talking Grass")){
                precision = "Talking Grass";
                precisionBis = "the silence effect";
            } else{ //if(itemSelected.contains("Volatile Water")){
                precision = "Volatile Water";
                precisionBis = "the petrifying effect";
            }

            descriptionText = "The "+precision+" permits to cure an ally from "+precisionBis;*/
        // ITEMS TO ENHANCE THE STATS
        } else{ // Tasty Spices, Fine Feather, Clay Pomade, Shiny Crystal, Reflecting Hood

             if(itemSelected.contains("Tasty Spices")){
                precision = "Tasty Spices";
                precisionBis = "strength";
                preciseEffect = "The "+precisionBis+ " is increased by 20";
            } else if(itemSelected.contains("Fine Feather")){
                precision = "Fine Feather";
                precisionBis = "agility";
                preciseEffect = "The "+precisionBis+ " is increased by 20";
            } else if(itemSelected.contains("Clay Pomade")){
                precision = "Clay Pomade";
                precisionBis = "endurance";
                preciseEffect = "The "+precisionBis+ " is increased by 20";
            } else{
                precision = "Shiny Crystal";
                precisionBis = "intelligence";
                preciseEffect = "The "+precisionBis+ " is increased by 20";
            }
            descriptionText = "The "+precision+" permits to increase an "
                    + "ally's "+precisionBis+" for 2 turns";
            
        }
        description.setText(descriptionText);
        HPLabel.setText(preciseEffect);
    }
    
    
    // ==============================================================================
    // ==== GETTING THE DESCRIPTION OF AN ITEM WHEN THE MOUSE HOVERS ON A SPRITE ====
    // ==============================================================================
    private CharClass getCharacterClass(String className) {
        switch (className) {
            case "Warrior":
                return new Warrior();
            case "Monk":
                return new Monk();
            case "Thief":
                return new Thief();
            case "Red Mage":
                return new RedMage();
            case "White Mage":
                return new WhiteMage();
            default:
                return new BlackMage();
        }
    }
    
    
    private CharClass[] buildTeams(String player){

        CharClass firstMember;
        CharClass secondMember;
        CharClass thirdMember;
        CharClass fourthMember;
        CharClass[] team;
        
        
        
        if(player.equals("P1")){
            firstMember = getCharacterClass(P1_firstSelection.getSelectedItem().toString());
            secondMember = getCharacterClass(P1_secondSelection.getSelectedItem().toString());
            thirdMember = getCharacterClass(P1_thirdSelection.getSelectedItem().toString());
            fourthMember = getCharacterClass(P1_fourthSelection.getSelectedItem().toString());
        } else{
            firstMember = getCharacterClass(P2_firstSelection.getSelectedItem().toString());
            secondMember = getCharacterClass(P2_secondSelection.getSelectedItem().toString());
            thirdMember = getCharacterClass(P2_thirdSelection.getSelectedItem().toString());
            fourthMember = getCharacterClass(P2_fourthSelection.getSelectedItem().toString());
        }
            
        team = new CharClass[]{firstMember, secondMember, thirdMember, fourthMember};
        
        return team;
        
    }
    
    
    public Item createItem(String itemName) {
        return switch (itemName) {
            case "Small Life Potion (1 credit)" -> new SmallLifePotion();
            case "Medium Life Potion (2 credits)" -> new MediumLifePotion();
            case "Great Life Potion (3 credits)" -> new GreatLifePotion();
            case "Small Magic Potion (2 credits)" -> new SmallMagicPotion();
            case "Medium Magic Potion (3 credits)" -> new MediumMagicPotion();
            case "Great Magic Potion (4 credits)" -> new GreatMagicPotion();
            case "Phoenix Down (6 credits)" -> new PhoenixDown();
            case "Tasty Spices (3 credits)" -> new TastySpices();
            case "Fine Feather (3 credits)" -> new FineFeather();
            case "Clay Pomade (3 credits)" -> new ClayPomade();
            case "Shiny Crystal (3 credits)" -> new ShinyCrystal();
            default -> null;
        };
    }

    
    public Item[] buildInventories(String player){
        JList itemsList;
        JList itemsSelected;
        
        if(player.equals("P1")){
            itemsList = P1_itemsList;
            itemsSelected = P1_itemsSelected;
        } else{
            itemsList = P2_itemsList;
            itemsSelected = P2_itemsSelected;
        }
        Item[] inventory;
        
        AbstractListModel<String> listModel = (AbstractListModel<String>) itemsList.getModel();
        
        inventory = new Item[listModel.getSize()];
        

        int i, j;
        String itemName;
        String selectedName;
        Item item;
        
        for (i = 0; i < listModel.getSize(); i++) {
            itemName = listModel.getElementAt(i);

            // Create an instance of the corresponding item based on its name
            item = createItem(itemName);
            //item.quantity = 2;

            // Store the item in the inventory array
            inventory[i] = item;
            
        }
        

        
        DefaultListModel<String> selectedList = (DefaultListModel<String>) itemsSelected.getModel();
        
        for (i = 0; i < selectedList.getSize(); i++) {
            selectedName = selectedList.getElementAt(i);

            for (j = 0; j < listModel.getSize(); j++) {
                itemName = listModel.getElementAt(j);

                // Compare the selected item with the item in the listModel
                if (itemName.equals(selectedName)) {
                    // Update the quantity of the corresponding item in the inventory
                    inventory[j].quantity++;
                }
            }
        }
        
        return inventory;
        
    }
    
    
    // #####################
    // #### CONSTRUCTOR ####
    // #####################
    public Preparation() {
        initComponents();
        
        // The player select the classes of their fighters before selecting their items,
        // that's why item selection panels are defaultly hidden.
        P1_itemSelection.setVisible(false);
        P2_itemSelection.setVisible(false);
        
        // The cancel button appears only when a player is ready, and they're not ready
        // by default.
        P1_cancelReady.setVisible(false);
        P2_cancelReady.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBorders = new javax.swing.JLabel();
        preparation_P2 = new keeptoo.KGradientPanel();
        P2_label = new javax.swing.JLabel();
        P2_classSelection = new keeptoo.KGradientPanel();
        P2_fourthSelection = new javax.swing.JComboBox<>();
        P2_thirdSelection = new javax.swing.JComboBox<>();
        P2_secondSelection = new javax.swing.JComboBox<>();
        P2_firstSelection = new javax.swing.JComboBox<>();
        P2_firstLabel = new javax.swing.JLabel();
        P2_secondLabel = new javax.swing.JLabel();
        P2_thirdLabel = new javax.swing.JLabel();
        P2_fourthLabel = new javax.swing.JLabel();
        P2_fourth = new javax.swing.JLabel();
        P2_third = new javax.swing.JLabel();
        P2_second = new javax.swing.JLabel();
        P2_first = new javax.swing.JLabel();
        P2_validateButton = new javax.swing.JButton();
        P2_randomChoices = new javax.swing.JButton();
        P2_stepDescription1 = new javax.swing.JLabel();
        P2_itemSelection = new keeptoo.KGradientPanel();
        P2_stepDescription2 = new javax.swing.JLabel();
        P2_backToClassSelection = new javax.swing.JButton();
        P2_randomChoicesItems = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        P2_itemsList = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        P2_itemsSelected = new javax.swing.JList<>();
        P2_creditsRemaining = new javax.swing.JLabel();
        P2_readyButton = new javax.swing.JButton();
        P2_cancelReady = new javax.swing.JButton();
        P2_resetSelected = new javax.swing.JButton();
        preparation_P1 = new keeptoo.KGradientPanel();
        P1_label = new javax.swing.JLabel();
        P1_classSelection = new keeptoo.KGradientPanel();
        P1_validateButton = new javax.swing.JButton();
        P1_randomChoices = new javax.swing.JButton();
        P1_fourthSelection = new javax.swing.JComboBox<>();
        P1_thirdSelection = new javax.swing.JComboBox<>();
        P1_secondSelection = new javax.swing.JComboBox<>();
        P1_firstSelection = new javax.swing.JComboBox<>();
        P1_firstLabel = new javax.swing.JLabel();
        P1_secondLabel = new javax.swing.JLabel();
        P1_thirdLabel = new javax.swing.JLabel();
        P1_fourthLabel = new javax.swing.JLabel();
        P1_fourth = new javax.swing.JLabel();
        P1_third = new javax.swing.JLabel();
        P1_second = new javax.swing.JLabel();
        P1_first = new javax.swing.JLabel();
        P1_stepDescription1 = new javax.swing.JLabel();
        P1_itemSelection = new keeptoo.KGradientPanel();
        P1_stepDescription2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        P1_itemsSelected = new javax.swing.JList<>();
        P1_backToClassSelection = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        P1_itemsList = new javax.swing.JList<>();
        P1_creditsRemaining = new javax.swing.JLabel();
        P1_readyButton = new javax.swing.JButton();
        P1_cancelReady = new javax.swing.JButton();
        P1_randomChoicesItems = new javax.swing.JButton();
        P1_resetSelected = new javax.swing.JButton();
        descriptionPanel = new keeptoo.KGradientPanel();
        preparation_fightButton = new javax.swing.JButton();
        preparation_backToMain = new javax.swing.JButton();
        description = new javax.swing.JTextArea();
        HPLabel = new javax.swing.JLabel();
        MPLabel = new javax.swing.JLabel();
        strengthLabel = new javax.swing.JLabel();
        agilityLabel = new javax.swing.JLabel();
        intelligenceLabel = new javax.swing.JLabel();
        enduranceLabel = new javax.swing.JLabel();
        changeFontColor = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuBorders.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/preparation_border.png"))); // NOI18N
        add(menuBorders, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        preparation_P2.setkEndColor(new java.awt.Color(0, 102, 204));
        preparation_P2.setkStartColor(new java.awt.Color(0, 255, 255));
        preparation_P2.setLayout(null);

        P2_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/P2.png"))); // NOI18N
        preparation_P2.add(P2_label);
        P2_label.setBounds(225, 17, 160, 50);

        P2_classSelection.setkEndColor(new java.awt.Color(0, 102, 204));
        P2_classSelection.setkStartColor(new java.awt.Color(0, 255, 255));
        P2_classSelection.setLayout(null);

        P2_fourthSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Warrior", "Monk", "Thief", "Red Mage", "White Mage", "Black Mage" }));
        P2_fourthSelection.setFocusable(false);
        P2_fourthSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P2_fourthSelectionMouseEntered(evt);
            }
        });
        P2_fourthSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_fourthSelectionActionPerformed(evt);
            }
        });
        P2_classSelection.add(P2_fourthSelection);
        P2_fourthSelection.setBounds(419, 201, 98, 22);

        P2_thirdSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Warrior", "Monk", "Thief", "Red Mage", "White Mage", "Black Mage" }));
        P2_thirdSelection.setFocusable(false);
        P2_thirdSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P2_thirdSelectionMouseEntered(evt);
            }
        });
        P2_thirdSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_thirdSelectionActionPerformed(evt);
            }
        });
        P2_classSelection.add(P2_thirdSelection);
        P2_thirdSelection.setBounds(305, 201, 108, 22);

        P2_secondSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Warrior", "Monk", "Thief", "Red Mage", "White Mage", "Black Mage" }));
        P2_secondSelection.setFocusable(false);
        P2_secondSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P2_secondSelectionMouseEntered(evt);
            }
        });
        P2_secondSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_secondSelectionActionPerformed(evt);
            }
        });
        P2_classSelection.add(P2_secondSelection);
        P2_secondSelection.setBounds(191, 201, 108, 22);

        P2_firstSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Warrior", "Monk", "Thief", "Red Mage", "White Mage", "Black Mage" }));
        P2_firstSelection.setFocusable(false);
        P2_firstSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P2_firstSelectionMouseEntered(evt);
            }
        });
        P2_firstSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_firstSelectionActionPerformed(evt);
            }
        });
        P2_classSelection.add(P2_firstSelection);
        P2_firstSelection.setBounds(77, 201, 108, 22);

        P2_firstLabel.setText("Member 1");
        P2_classSelection.add(P2_firstLabel);
        P2_firstLabel.setBounds(105, 177, 70, 16);

        P2_secondLabel.setText("Member 2");
        P2_classSelection.add(P2_secondLabel);
        P2_secondLabel.setBounds(216, 177, 70, 16);

        P2_thirdLabel.setText("Member 3");
        P2_classSelection.add(P2_thirdLabel);
        P2_thirdLabel.setBounds(331, 177, 70, 16);

        P2_fourthLabel.setText("Member 4");
        P2_classSelection.add(P2_fourthLabel);
        P2_fourthLabel.setBounds(440, 177, 70, 16);

        P2_fourth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P2_fourth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P2_fourthMouseEntered(evt);
            }
        });
        P2_classSelection.add(P2_fourth);
        P2_fourth.setBounds(440, 111, 48, 48);

        P2_third.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P2_third.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P2_thirdMouseEntered(evt);
            }
        });
        P2_classSelection.add(P2_third);
        P2_third.setBounds(337, 111, 48, 48);

        P2_second.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P2_second.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P2_secondMouseEntered(evt);
            }
        });
        P2_classSelection.add(P2_second);
        P2_second.setBounds(222, 111, 48, 48);

        P2_first.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P2_first.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P2_firstMouseEntered(evt);
            }
        });
        P2_classSelection.add(P2_first);
        P2_first.setBounds(111, 111, 48, 48);

        P2_validateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/validate_button.png"))); // NOI18N
        P2_validateButton.setBorder(null);
        P2_validateButton.setBorderPainted(false);
        P2_validateButton.setContentAreaFilled(false);
        P2_validateButton.setFocusPainted(false);
        P2_validateButton.setName("playButton"); // NOI18N
        P2_validateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P2_validateButtonMouseClicked(evt);
            }
        });
        P2_validateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_validateButtonActionPerformed(evt);
            }
        });
        P2_classSelection.add(P2_validateButton);
        P2_validateButton.setBounds(417, 348, 162, 63);

        P2_randomChoices.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        P2_randomChoices.setText("Choose randomly");
        P2_randomChoices.setFocusable(false);
        P2_randomChoices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_randomChoicesActionPerformed(evt);
            }
        });
        P2_classSelection.add(P2_randomChoices);
        P2_randomChoices.setBounds(211, 249, 188, 45);

        P2_stepDescription1.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        P2_stepDescription1.setText("Choose the classes of your team members");
        P2_classSelection.add(P2_stepDescription1);
        P2_stepDescription1.setBounds(30, 22, 530, 30);

        preparation_P2.add(P2_classSelection);
        P2_classSelection.setBounds(0, 110, 600, 430);

        P2_itemSelection.setkEndColor(new java.awt.Color(0, 102, 204));
        P2_itemSelection.setkStartColor(new java.awt.Color(0, 255, 255));
        P2_itemSelection.setLayout(null);

        P2_stepDescription2.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        P2_stepDescription2.setText("Choose your items");
        P2_itemSelection.add(P2_stepDescription2);
        P2_stepDescription2.setBounds(30, 22, 224, 30);

        P2_backToClassSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/back_button.png"))); // NOI18N
        P2_backToClassSelection.setBorder(null);
        P2_backToClassSelection.setBorderPainted(false);
        P2_backToClassSelection.setContentAreaFilled(false);
        P2_backToClassSelection.setFocusPainted(false);
        P2_backToClassSelection.setName("playButton"); // NOI18N
        P2_backToClassSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P2_backToClassSelectionMouseClicked(evt);
            }
        });
        P2_backToClassSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_backToClassSelectionActionPerformed(evt);
            }
        });
        P2_itemSelection.add(P2_backToClassSelection);
        P2_backToClassSelection.setBounds(20, 350, 105, 57);

        P2_randomChoicesItems.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        P2_randomChoicesItems.setText("Choose randomly");
        P2_randomChoicesItems.setFocusable(false);
        P2_randomChoicesItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_randomChoicesItemsActionPerformed(evt);
            }
        });
        P2_itemSelection.add(P2_randomChoicesItems);
        P2_randomChoicesItems.setBounds(83, 250, 210, 45);

        P2_itemsList = new javax.swing.JList<>(new DefaultListModel<>());
        P2_itemsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Small Life Potion (1 credit)", "Medium Life Potion (2 credits)", "Great Life Potion (3 credits)", "Small Magic Potion (2 credits)", "Medium Magic Potion (3 credits)", "Great Magic Potion (4 credits)", "Phoenix Down (6 credits)", "Tasty Spices (3 credits)", "Fine Feather (3 credits)", "Clay Pomade (3 credits)", "Shiny Crystal (3 credits)" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        P2_itemsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P2_itemsListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(P2_itemsList);

        P2_itemSelection.add(jScrollPane3);
        jScrollPane3.setBounds(83, 80, 210, 146);

        P2_itemsSelected = new javax.swing.JList<>(new DefaultListModel<>());
        P2_itemsSelected.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P2_itemsSelectedMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(P2_itemsSelected);

        P2_itemSelection.add(jScrollPane4);
        jScrollPane4.setBounds(320, 80, 210, 146);

        P2_creditsRemaining.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        P2_creditsRemaining.setText("10 credits remaining");
        P2_itemSelection.add(P2_creditsRemaining);
        P2_creditsRemaining.setBounds(330, 40, 200, 40);

        P2_readyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/ready_button.png"))); // NOI18N
        P2_readyButton.setBorder(null);
        P2_readyButton.setBorderPainted(false);
        P2_readyButton.setContentAreaFilled(false);
        P2_readyButton.setFocusPainted(false);
        P2_readyButton.setName("playButton"); // NOI18N
        P2_readyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P2_readyButtonMouseClicked(evt);
            }
        });
        P2_readyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_readyButtonActionPerformed(evt);
            }
        });
        P2_itemSelection.add(P2_readyButton);
        P2_readyButton.setBounds(430, 350, 162, 63);

        P2_cancelReady.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/cancel_button.png"))); // NOI18N
        P2_cancelReady.setBorder(null);
        P2_cancelReady.setBorderPainted(false);
        P2_cancelReady.setContentAreaFilled(false);
        P2_cancelReady.setFocusPainted(false);
        P2_cancelReady.setName("playButton"); // NOI18N
        P2_cancelReady.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_cancelReadyActionPerformed(evt);
            }
        });
        P2_itemSelection.add(P2_cancelReady);
        P2_cancelReady.setBounds(17, 351, 150, 55);

        P2_resetSelected.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        P2_resetSelected.setText("Reset ");
        P2_resetSelected.setFocusable(false);
        P2_resetSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P2_resetSelectedActionPerformed(evt);
            }
        });
        P2_itemSelection.add(P2_resetSelected);
        P2_resetSelected.setBounds(320, 250, 210, 45);

        preparation_P2.add(P2_itemSelection);
        P2_itemSelection.setBounds(0, 110, 600, 430);

        add(preparation_P2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, 600, 540));

        preparation_P1.setkEndColor(new java.awt.Color(255, 102, 102));
        preparation_P1.setkStartColor(new java.awt.Color(255, 204, 204));
        preparation_P1.setLayout(null);

        P1_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/P1.png"))); // NOI18N
        preparation_P1.add(P1_label);
        P1_label.setBounds(234, 17, 153, 50);

        P1_classSelection.setkEndColor(new java.awt.Color(255, 102, 102));
        P1_classSelection.setkStartColor(new java.awt.Color(255, 204, 204));
        P1_classSelection.setLayout(null);

        P1_validateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/validate_button.png"))); // NOI18N
        P1_validateButton.setBorder(null);
        P1_validateButton.setBorderPainted(false);
        P1_validateButton.setContentAreaFilled(false);
        P1_validateButton.setFocusPainted(false);
        P1_validateButton.setName("playButton"); // NOI18N
        P1_validateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P1_validateButtonMouseClicked(evt);
            }
        });
        P1_validateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_validateButtonActionPerformed(evt);
            }
        });
        P1_classSelection.add(P1_validateButton);
        P1_validateButton.setBounds(417, 348, 162, 63);

        P1_randomChoices.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        P1_randomChoices.setText("Choose randomly");
        P1_randomChoices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_randomChoicesActionPerformed(evt);
            }
        });
        P1_classSelection.add(P1_randomChoices);
        P1_randomChoices.setBounds(211, 249, 188, 45);

        P1_fourthSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Warrior", "Monk", "Thief", "Red Mage", "White Mage", "Black Mage" }));
        P1_fourthSelection.setFocusable(false);
        P1_fourthSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P1_fourthSelectionMouseEntered(evt);
            }
        });
        P1_fourthSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_fourthSelectionActionPerformed(evt);
            }
        });
        P1_classSelection.add(P1_fourthSelection);
        P1_fourthSelection.setBounds(424, 200, 98, 22);

        P1_thirdSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Warrior", "Monk", "Thief", "Red Mage", "White Mage", "Black Mage" }));
        P1_thirdSelection.setFocusable(false);
        P1_thirdSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P1_thirdSelectionMouseEntered(evt);
            }
        });
        P1_thirdSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_thirdSelectionActionPerformed(evt);
            }
        });
        P1_classSelection.add(P1_thirdSelection);
        P1_thirdSelection.setBounds(310, 200, 108, 22);

        P1_secondSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Warrior", "Monk", "Thief", "Red Mage", "White Mage", "Black Mage" }));
        P1_secondSelection.setFocusable(false);
        P1_secondSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P1_secondSelectionMouseEntered(evt);
            }
        });
        P1_secondSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_secondSelectionActionPerformed(evt);
            }
        });
        P1_classSelection.add(P1_secondSelection);
        P1_secondSelection.setBounds(196, 200, 108, 22);

        P1_firstSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Warrior", "Monk", "Thief", "Red Mage", "White Mage", "Black Mage" }));
        P1_firstSelection.setFocusable(false);
        P1_firstSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P1_firstSelectionMouseEntered(evt);
            }
        });
        P1_firstSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_firstSelectionActionPerformed(evt);
            }
        });
        P1_classSelection.add(P1_firstSelection);
        P1_firstSelection.setBounds(82, 200, 108, 22);

        P1_firstLabel.setText("Member 1");
        P1_classSelection.add(P1_firstLabel);
        P1_firstLabel.setBounds(110, 178, 70, 16);

        P1_secondLabel.setText("Member 2");
        P1_classSelection.add(P1_secondLabel);
        P1_secondLabel.setBounds(221, 178, 70, 16);

        P1_thirdLabel.setText("Member 3");
        P1_classSelection.add(P1_thirdLabel);
        P1_thirdLabel.setBounds(336, 178, 70, 16);

        P1_fourthLabel.setText("Member 4");
        P1_classSelection.add(P1_fourthLabel);
        P1_fourthLabel.setBounds(445, 178, 70, 16);

        P1_fourth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P1_fourth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P1_fourthMouseEntered(evt);
            }
        });
        P1_classSelection.add(P1_fourth);
        P1_fourth.setBounds(445, 112, 48, 48);

        P1_third.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P1_third.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P1_thirdMouseEntered(evt);
            }
        });
        P1_classSelection.add(P1_third);
        P1_third.setBounds(342, 112, 48, 48);

        P1_second.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P1_second.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P1_secondMouseEntered(evt);
            }
        });
        P1_classSelection.add(P1_second);
        P1_second.setBounds(227, 112, 48, 48);

        P1_first.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sprites/warrior_1.png"))); // NOI18N
        P1_first.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                P1_firstMouseEntered(evt);
            }
        });
        P1_classSelection.add(P1_first);
        P1_first.setBounds(116, 112, 48, 48);

        P1_stepDescription1.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        P1_stepDescription1.setText("Choose the classes of your team members");
        P1_classSelection.add(P1_stepDescription1);
        P1_stepDescription1.setBounds(30, 22, 530, 30);

        preparation_P1.add(P1_classSelection);
        P1_classSelection.setBounds(0, 110, 600, 430);

        P1_itemSelection.setkEndColor(new java.awt.Color(255, 102, 102));
        P1_itemSelection.setkStartColor(new java.awt.Color(255, 204, 204));
        P1_itemSelection.setLayout(null);

        P1_stepDescription2.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        P1_stepDescription2.setText("Choose your items");
        P1_itemSelection.add(P1_stepDescription2);
        P1_stepDescription2.setBounds(30, 22, 224, 30);

        P1_itemsSelected = new javax.swing.JList<>(new DefaultListModel<>());
        P1_itemsSelected.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P1_itemsSelectedMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(P1_itemsSelected);

        P1_itemSelection.add(jScrollPane2);
        jScrollPane2.setBounds(320, 80, 210, 146);

        P1_backToClassSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/back_button.png"))); // NOI18N
        P1_backToClassSelection.setBorder(null);
        P1_backToClassSelection.setBorderPainted(false);
        P1_backToClassSelection.setContentAreaFilled(false);
        P1_backToClassSelection.setFocusPainted(false);
        P1_backToClassSelection.setName("playButton"); // NOI18N
        P1_backToClassSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P1_backToClassSelectionMouseClicked(evt);
            }
        });
        P1_backToClassSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_backToClassSelectionActionPerformed(evt);
            }
        });
        P1_itemSelection.add(P1_backToClassSelection);
        P1_backToClassSelection.setBounds(20, 350, 105, 57);

        P1_itemsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Small Life Potion (1 credit)", "Medium Life Potion (2 credits)", "Great Life Potion (3 credits)", "Small Magic Potion (2 credits)", "Medium Magic Potion (3 credits)", "Great Magic Potion (4 credits)", "Phoenix Down (6 credits)", "Tasty Spices (3 credits)", "Fine Feather (3 credits)", "Clay Pomade (3 credits)", "Shiny Crystal (3 credits)" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        P1_itemsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P1_itemsListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(P1_itemsList);

        P1_itemSelection.add(jScrollPane1);
        jScrollPane1.setBounds(83, 80, 210, 146);

        P1_creditsRemaining.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        P1_creditsRemaining.setText("10 credits remaining");
        P1_itemSelection.add(P1_creditsRemaining);
        P1_creditsRemaining.setBounds(330, 40, 200, 40);

        P1_readyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/ready_button.png"))); // NOI18N
        P1_readyButton.setBorder(null);
        P1_readyButton.setBorderPainted(false);
        P1_readyButton.setContentAreaFilled(false);
        P1_readyButton.setFocusPainted(false);
        P1_readyButton.setName("playButton"); // NOI18N
        P1_readyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                P1_readyButtonMouseClicked(evt);
            }
        });
        P1_readyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_readyButtonActionPerformed(evt);
            }
        });
        P1_itemSelection.add(P1_readyButton);
        P1_readyButton.setBounds(430, 350, 162, 63);

        P1_cancelReady.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/cancel_button.png"))); // NOI18N
        P1_cancelReady.setBorder(null);
        P1_cancelReady.setBorderPainted(false);
        P1_cancelReady.setContentAreaFilled(false);
        P1_cancelReady.setFocusPainted(false);
        P1_cancelReady.setName("playButton"); // NOI18N
        P1_cancelReady.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_cancelReadyActionPerformed(evt);
            }
        });
        P1_itemSelection.add(P1_cancelReady);
        P1_cancelReady.setBounds(17, 351, 150, 55);

        P1_randomChoicesItems.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        P1_randomChoicesItems.setText("Choose randomly");
        P1_randomChoicesItems.setFocusable(false);
        P1_randomChoicesItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_randomChoicesItemsActionPerformed(evt);
            }
        });
        P1_itemSelection.add(P1_randomChoicesItems);
        P1_randomChoicesItems.setBounds(80, 250, 210, 45);

        P1_resetSelected.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        P1_resetSelected.setText("Reset ");
        P1_resetSelected.setFocusable(false);
        P1_resetSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                P1_resetSelectedActionPerformed(evt);
            }
        });
        P1_itemSelection.add(P1_resetSelected);
        P1_resetSelected.setBounds(320, 250, 210, 45);

        preparation_P1.add(P1_itemSelection);
        P1_itemSelection.setBounds(0, 110, 600, 430);

        add(preparation_P1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 600, 540));

        descriptionPanel.setLayout(null);

        preparation_fightButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/fight_button.png"))); // NOI18N
        preparation_fightButton.setBorder(null);
        preparation_fightButton.setBorderPainted(false);
        preparation_fightButton.setContentAreaFilled(false);
        preparation_fightButton.setEnabled(false);
        preparation_fightButton.setFocusPainted(false);
        preparation_fightButton.setName("playButton"); // NOI18N
        preparation_fightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preparation_fightButtonActionPerformed(evt);
            }
        });
        descriptionPanel.add(preparation_fightButton);
        preparation_fightButton.setBounds(910, 30, 144, 61);

        preparation_backToMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/menus/back_button.png"))); // NOI18N
        preparation_backToMain.setBorder(null);
        preparation_backToMain.setBorderPainted(false);
        preparation_backToMain.setContentAreaFilled(false);
        preparation_backToMain.setFocusPainted(false);
        preparation_backToMain.setName("playButton"); // NOI18N
        preparation_backToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preparation_backToMainActionPerformed(evt);
            }
        });
        descriptionPanel.add(preparation_backToMain);
        preparation_backToMain.setBounds(1080, 30, 105, 57);

        description.setEditable(false);
        description.setColumns(20);
        description.setFont(new java.awt.Font("Yu Gothic UI", 0, 13)); // NOI18N
        description.setForeground(new java.awt.Color(255, 255, 255));
        description.setLineWrap(true);
        description.setRows(5);
        description.setText("The Warrior is a formidable melee combatant with high hit points and exceptional strength and endurance, making them a tough adversary on the battlefield. However, their low intelligence and agility indicate a reliance on brute force rather than finesse.");
        description.setWrapStyleWord(true);
        description.setFocusable(false);
        description.setName(""); // NOI18N
        description.setOpaque(false);
        descriptionPanel.add(description);
        description.setBounds(15, 7, 365, 110);

        HPLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        HPLabel.setForeground(new java.awt.Color(255, 255, 255));
        HPLabel.setText("Health Points: 85");
        descriptionPanel.add(HPLabel);
        HPLabel.setBounds(460, 20, 340, 22);

        MPLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        MPLabel.setForeground(new java.awt.Color(255, 255, 255));
        MPLabel.setText("Magic Points: 10");
        descriptionPanel.add(MPLabel);
        MPLabel.setBounds(460, 50, 340, 22);

        strengthLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        strengthLabel.setForeground(new java.awt.Color(255, 255, 255));
        strengthLabel.setText("Strength: 20");
        descriptionPanel.add(strengthLabel);
        strengthLabel.setBounds(460, 80, 120, 22);

        agilityLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        agilityLabel.setForeground(new java.awt.Color(255, 255, 255));
        agilityLabel.setText("Agility: 12");
        descriptionPanel.add(agilityLabel);
        agilityLabel.setBounds(610, 20, 120, 22);

        intelligenceLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        intelligenceLabel.setForeground(new java.awt.Color(255, 255, 255));
        intelligenceLabel.setText("Intelligence: 5");
        descriptionPanel.add(intelligenceLabel);
        intelligenceLabel.setBounds(610, 50, 120, 25);

        enduranceLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        enduranceLabel.setForeground(new java.awt.Color(255, 255, 255));
        enduranceLabel.setText("Endurance: 25");
        descriptionPanel.add(enduranceLabel);
        enduranceLabel.setBounds(610, 80, 120, 25);

        changeFontColor.setFocusable(false);
        changeFontColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeFontColorActionPerformed(evt);
            }
        });
        descriptionPanel.add(changeFontColor);
        changeFontColor.setBounds(770, 70, 40, 40);

        add(descriptionPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 1240, 130));
    }// </editor-fold>//GEN-END:initComponents

    private void preparation_backToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preparation_backToMainActionPerformed
        MainMenu.buttonSound();
        
        // to make the panel reseted when the user come back
        P1_firstSelection.setSelectedIndex(0);
        P1_secondSelection.setSelectedIndex(0);
        P1_thirdSelection.setSelectedIndex(0);
        P1_fourthSelection.setSelectedIndex(0);
        P2_firstSelection.setSelectedIndex(0);
        P2_secondSelection.setSelectedIndex(0);
        P2_thirdSelection.setSelectedIndex(0);
        P2_fourthSelection.setSelectedIndex(0);
        
        displayClassInformations(P1_first);
        
        Versus.getPreparation().P1_readyButton.setEnabled(true);
        Versus.getPreparation().P2_readyButton.setEnabled(true);
        Versus.getPreparation().preparation_fightButton.setEnabled(false);
        Versus.getPreparation().P1_itemSelection.setVisible(false);
        Versus.getPreparation().P2_itemSelection.setVisible(false);
        Versus.getPreparation().P1_classSelection.setVisible(true);
        Versus.getPreparation().P2_classSelection.setVisible(true);
        
        P1_readyButton.setEnabled(true);
        P2_readyButton.setEnabled(true);
        preparation_fightButton.setEnabled(false);
        P1_itemSelection.setVisible(false);
        P2_itemSelection.setVisible(false);
        P1_classSelection.setVisible(true);
        P2_classSelection.setVisible(true);
        
        // show the main menu
        Versus.getMain().setVisible(true);
        this.setVisible(false);
        this.stopPreparationMusic();
        MainMenu.playMainMusic();
    }//GEN-LAST:event_preparation_backToMainActionPerformed

    private void P2_firstSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_firstSelectionActionPerformed
        selectClass(P2_first, P2_firstSelection);
    }//GEN-LAST:event_P2_firstSelectionActionPerformed

    private void P2_secondSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_secondSelectionActionPerformed
        selectClass(P2_second, P2_secondSelection);
    }//GEN-LAST:event_P2_secondSelectionActionPerformed

    private void P2_thirdSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_thirdSelectionActionPerformed
        selectClass(P2_third, P2_thirdSelection);
    }//GEN-LAST:event_P2_thirdSelectionActionPerformed

    private void P2_fourthSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_fourthSelectionActionPerformed
        selectClass(P2_fourth, P2_fourthSelection);
    }//GEN-LAST:event_P2_fourthSelectionActionPerformed

    private void P2_backToClassSelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_backToClassSelectionMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_P2_backToClassSelectionMouseClicked

    private void P2_backToClassSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_backToClassSelectionActionPerformed
        P2_itemSelection.setVisible(false);
        menuBorders.setVisible(false);
        P2_classSelection.setVisible(true);
        menuBorders.setVisible(true);
    }//GEN-LAST:event_P2_backToClassSelectionActionPerformed

    private void P1_secondMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_secondMouseEntered
        displayClassInformations(P1_second);
    }//GEN-LAST:event_P1_secondMouseEntered

    private void P1_firstSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_firstSelectionActionPerformed
        selectClass(P1_first, P1_firstSelection);
    }//GEN-LAST:event_P1_firstSelectionActionPerformed

    private void P1_secondSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_secondSelectionActionPerformed
        selectClass(P1_second, P1_secondSelection);
    }//GEN-LAST:event_P1_secondSelectionActionPerformed

    private void P1_thirdSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_thirdSelectionActionPerformed
        selectClass(P1_third, P1_thirdSelection);
    }//GEN-LAST:event_P1_thirdSelectionActionPerformed

    private void P1_fourthSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_fourthSelectionActionPerformed
        selectClass(P1_fourth, P1_fourthSelection);
    }//GEN-LAST:event_P1_fourthSelectionActionPerformed

    private void P1_randomChoicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_randomChoicesActionPerformed
        randomClassChoices(1);
    }//GEN-LAST:event_P1_randomChoicesActionPerformed

    private void P1_validateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_validateButtonActionPerformed
        P1_classSelection.setVisible(false);
        menuBorders.setVisible(false);
        P1_itemSelection.setVisible(true);
        menuBorders.setVisible(true);
    }//GEN-LAST:event_P1_validateButtonActionPerformed

    private void P1_validateButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_validateButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_P1_validateButtonMouseClicked

    private void P1_backToClassSelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_backToClassSelectionMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_P1_backToClassSelectionMouseClicked

    private void P1_backToClassSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_backToClassSelectionActionPerformed
        P1_itemSelection.setVisible(false);
        menuBorders.setVisible(false);
        P1_classSelection.setVisible(true);
        menuBorders.setVisible(true);
    }//GEN-LAST:event_P1_backToClassSelectionActionPerformed

    private void P2_randomChoicesItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_randomChoicesItemsActionPerformed
        
        Random random = new Random();
        int itemSelection;
        String itemToAdd;
        int creditValue;
        boolean stop = false;
        
        // The random choice of items begins with the cleaning of the selected items
        
        // The list model of the selected items of the P2 is retrived
        DefaultListModel<String> listModel = (DefaultListModel<String>) P2_itemsSelected.getModel();
        // All the elements of the model are removed
        listModel.removeAllElements();
        // The amount of credits available is updated
        P2_credits = 10;
        
        // Then begins the random choice operation
        
        // Retrieve the list of the items available
        AbstractListModel<String> listModel_bis = (AbstractListModel<String>) P2_itemsList.getModel();
        // Counting the number of elements of the list
        int numberOfElements = listModel_bis.getSize();
        
        // The loop will stop when the item that will be selected will be too
        // expensive for the remaining credits of the player
        
        while(!stop){
            // a random value is selected, corresponding to an item in the list of the items available
            itemSelection = random.nextInt(numberOfElements);
            itemToAdd = listModel_bis.getElementAt(itemSelection);
            // the credit value of the item is retrieved
            creditValue = Integer.parseInt(itemToAdd.replaceAll("[^0-9]", ""));
            // If the player still has enought credits, the item is added to his selection, in P2_itemsSelected
            if((P2_credits - creditValue) >= 0){
                listModel.addElement(itemToAdd);
                // new credits amount available
                P2_credits-= creditValue;
            // if the player hasn't enought credit, the loop stops and his random selection is finished
            } else{
                stop = true;
            }
        }
        // The label showing the amount of credits remaining is updated
        P2_creditsRemaining.setText(P2_credits + " credits remaining"); 
    }//GEN-LAST:event_P2_randomChoicesItemsActionPerformed

    private void P2_validateButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_validateButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_P2_validateButtonMouseClicked

    private void P2_validateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_validateButtonActionPerformed
        P2_classSelection.setVisible(false);
        menuBorders.setVisible(false);
        P2_itemSelection.setVisible(true);
        menuBorders.setVisible(true);
    }//GEN-LAST:event_P2_validateButtonActionPerformed

    private void P2_randomChoicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_randomChoicesActionPerformed
        randomClassChoices(2);
    }//GEN-LAST:event_P2_randomChoicesActionPerformed

    private void P1_resetSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_resetSelectedActionPerformed
        // The list model of the selected items of the P1 is retrived
        DefaultListModel<String> listModel = (DefaultListModel<String>) P1_itemsSelected.getModel();
        // All the elements of the model are removed
        listModel.removeAllElements();
        // The amount of credits available is updated
        P1_credits = 10;
        // The label showing the amount of credits remaining is updated
        P1_creditsRemaining.setText("10 credits remaining"); 

    }//GEN-LAST:event_P1_resetSelectedActionPerformed

    private void P1_firstMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_firstMouseEntered
        displayClassInformations(P1_first);
    }//GEN-LAST:event_P1_firstMouseEntered

    private void P1_thirdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_thirdMouseEntered
        displayClassInformations(P1_third);
    }//GEN-LAST:event_P1_thirdMouseEntered

    private void P1_fourthMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_fourthMouseEntered
        displayClassInformations(P1_fourth);
    }//GEN-LAST:event_P1_fourthMouseEntered

    private void P2_firstMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_firstMouseEntered
        displayClassInformations(P2_first);
    }//GEN-LAST:event_P2_firstMouseEntered

    private void P2_secondMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_secondMouseEntered
        displayClassInformations(P2_second);
    }//GEN-LAST:event_P2_secondMouseEntered

    private void P2_thirdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_thirdMouseEntered
        displayClassInformations(P2_third);
    }//GEN-LAST:event_P2_thirdMouseEntered

    private void P2_fourthMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_fourthMouseEntered
        displayClassInformations(P2_fourth);
    }//GEN-LAST:event_P2_fourthMouseEntered

    private void P2_readyButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_readyButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_P2_readyButtonMouseClicked

    private void P2_readyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_readyButtonActionPerformed
        // When P1_ready and P2_ready will be true, preparation_fightButton will be enabled
        P2_ready = true;
        // The ready button is no longer clickable since the player is ready
        P2_readyButton.setEnabled(false);
        // The player cannot go back to the selection of the classes when he's ready but he can cancel his ready state
        P2_backToClassSelection.setVisible(false);
        // The cancelReady button allow the player to be no longer ready
        P2_cancelReady.setVisible(true);
        // If the program reaches there, it means P2 is ready, and if P1 is ready
        // it means both player are ready: the fight button becomes enable to
        // allow them to start he combat.
        if(P1_ready){
            preparation_fightButton.setEnabled(true);
        }
    }//GEN-LAST:event_P2_readyButtonActionPerformed

    private void P1_readyButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_readyButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_P1_readyButtonMouseClicked

    private void P1_readyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_readyButtonActionPerformed
        // When P1_ready and P2_ready will be true, preparation_fightButton will be enabled
        P1_ready = true;
        // The ready button is no longer clickable since the player is ready
        P1_readyButton.setEnabled(false);
        // The player cannot go back to the selection of the classes when he's ready but he can cancel his ready state
        P1_backToClassSelection.setVisible(false);
        // The cancelReady button allow the player to be no longer ready
        P1_cancelReady.setVisible(true);
        // If the program reaches there, it means P1 is ready, and if P2 is ready
        // it means both player are ready: the fight button becomes enable to
        // allow them to start he combat.
        if(P2_ready){
            preparation_fightButton.setEnabled(true);
        }
    }//GEN-LAST:event_P1_readyButtonActionPerformed

    private void P1_cancelReadyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_cancelReadyActionPerformed
        // The player is no longer ready after he clicked on the cancel button
        P1_ready = false;
        // The player's ready button becomes enabled too
        P1_readyButton.setEnabled(true);
        // He can go back to class selection too since he's no longer ready
        P1_backToClassSelection.setVisible(true);
        // The cancel button disappears
        P1_cancelReady.setVisible(false);
        // The fight can no longer start because all the player have to be ready for it
        preparation_fightButton.setEnabled(false);
    }//GEN-LAST:event_P1_cancelReadyActionPerformed

    private void P2_cancelReadyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_cancelReadyActionPerformed
        // The player is no longer ready after he clicked on the cancel button
        P2_ready = false;
        // The player's ready button becomes enabled too
        P2_readyButton.setEnabled(true);
        // He can go back to class selection too since he's no longer ready
        P2_backToClassSelection.setVisible(true);
        // The cancel button disappears
        P2_cancelReady.setVisible(false);
        // The fight can no longer start because all the player have to be ready for it
        preparation_fightButton.setEnabled(false);
    }//GEN-LAST:event_P2_cancelReadyActionPerformed

    private void P1_itemsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_itemsListMouseClicked
        // The value selected in P1_itemsList is retrieved
        String itemToAdd = P1_itemsList.getSelectedValue();
        
        // The condition prevents some errors when the user clicks on the
        // list without any selection.
        
        if(itemToAdd != null){
            // The credit value of the item is retrieved
            int creditValue = Integer.parseInt(itemToAdd.replaceAll("[^0-9]", ""));

            // If the player has enought credits, the item is brought to his selection, in P1_itemsSelected
            if((P1_credits - creditValue) >= 0){
                // The P1_itemsSelected's list model is retrieved
                DefaultListModel<String> listModel = (DefaultListModel<String>) P1_itemsSelected.getModel();
                // add the selected item to the list model
                listModel.addElement(itemToAdd); 
                // new credits amount available
                P1_credits-= creditValue;
                // The label showing the amount of credits remaining is updated
                P1_creditsRemaining.setText(P1_credits + " credits remaining"); 
            }
            // Display the informations of the item selected in the description panel
            displayItemInformations(itemToAdd);
        }
    }//GEN-LAST:event_P1_itemsListMouseClicked

    private void P2_itemsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_itemsListMouseClicked
        // The value selected in P2_itemsList is retrieved
        String itemToAdd = P2_itemsList.getSelectedValue();
        
        // The condition prevents some errors when the user clicks on the
        // list without any selection.
        
        if(itemToAdd != null){
            // The credit value of the item is retrieved
            int creditValue = Integer.parseInt(itemToAdd.replaceAll("[^0-9]", ""));

            // if the player has enought credits, the item is brought to it's selection, in P2_itemsSelected
            if((P2_credits - creditValue) >= 0){
                // The P2_itemsSelected's list model is retrieved
                DefaultListModel<String> listModel = (DefaultListModel<String>) P2_itemsSelected.getModel();
                // add the selected item to the list model
                listModel.addElement(itemToAdd); 
                // new credits amount available
                P2_credits-= creditValue;
                // The label showing the amount of credits remaining is updated
                P2_creditsRemaining.setText(P2_credits + " credits remaining"); 
            }
            // Display the informations of the item selected in the description panel
            displayItemInformations(itemToAdd);
        }
    }//GEN-LAST:event_P2_itemsListMouseClicked

    private void P1_itemsSelectedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_itemsSelectedMouseClicked
        // The value selected in P1_itemsSelected is retrieved
        String itemToRemove = P1_itemsSelected.getSelectedValue();
        
        // The condition prevents some errors when the user clicks on the
        // list without any selection.
        
        if(itemToRemove != null){
            // The credit value of the item is retrieved
            int creditValue = Integer.parseInt(itemToRemove.replaceAll("[^0-9]", ""));
            // The P1_itemsSelected's list model is retrieved
            DefaultListModel<String> listModel = (DefaultListModel<String>) P1_itemsSelected.getModel();
            // The item is removed from the model
            listModel.removeElement(itemToRemove);
            // The credit value of the item is gaven back to P1's credits
            P1_credits+= creditValue;
            // The label showing the amount of credits remaining is updated
            P1_creditsRemaining.setText(P1_credits + " credits remaining");
            // Display the informations of the item selected in the description panel
            displayItemInformations(itemToRemove);
        }
    }//GEN-LAST:event_P1_itemsSelectedMouseClicked

    private void P2_itemsSelectedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_itemsSelectedMouseClicked
        // The value selected in P2_itemsSelected is retrieved
        String itemToRemove = P2_itemsSelected.getSelectedValue();
        
        // The condition prevents some errors when the user clicks on the
        // list without any selection.
        
        if(itemToRemove != null){
            // The credit value of the item is retrieved
            int creditValue = Integer.parseInt(itemToRemove.replaceAll("[^0-9]", ""));
            // The P2_itemsSelected's list model is retrieved
            DefaultListModel<String> listModel = (DefaultListModel<String>) P2_itemsSelected.getModel();
            // The item is removed from the model
            listModel.removeElement(itemToRemove);
            // The credit value of the item is gaven back to P2's credits
            P2_credits+= creditValue;
            // The label showing the amount of credits remaining is updated
            P2_creditsRemaining.setText(P2_credits + " credits remaining");
            // Display the informations of the item selected in the description panel
            displayItemInformations(itemToRemove);
        }

    }//GEN-LAST:event_P2_itemsSelectedMouseClicked

    private void P1_randomChoicesItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1_randomChoicesItemsActionPerformed
        
        Random random = new Random();
        int itemSelection;
        String itemToAdd;
        int creditValue;
        boolean stop = false;
        
        // The random choice of items begins with the cleaning of the selected items
        
        // The list model of the selected items of the P1 is retrived
        DefaultListModel<String> listModel = (DefaultListModel<String>) P1_itemsSelected.getModel();
        // All the elements of the model are removed
        listModel.removeAllElements();
        // The amount of credits available is updated
        P1_credits = 10;
        
        // Then begins the random choice operation
        
        // Retrieve the list of the items available
        AbstractListModel<String> listModel_bis = (AbstractListModel<String>) P1_itemsList.getModel();
        // Counting the number of elements of the list
        int numberOfElements = listModel_bis.getSize();
        
        // The loop will stop when the item that will be selected will be too
        // expensive for the remaining credits of the player
        
        while(!stop){
            // a random value is selected, corresponding to an item in the list of the items available
            itemSelection = random.nextInt(numberOfElements);
            itemToAdd = listModel_bis.getElementAt(itemSelection);
            // the credit value of the item is retrieved
            creditValue = Integer.parseInt(itemToAdd.replaceAll("[^0-9]", ""));
            // If the player still has enought credits, the item is added to his selection, in P1_itemsSelected
            if((P1_credits - creditValue) >= 0){
                listModel.addElement(itemToAdd);
                // new credits amount available
                P1_credits-= creditValue;
            // if the player hasn't enought credit, the loop stops and his random selection is finished
            } else{
                stop = true;
            }
        }
        // The label showing the amount of credits remaining is updated
        P1_creditsRemaining.setText(P1_credits + " credits remaining"); 
    }//GEN-LAST:event_P1_randomChoicesItemsActionPerformed

    private void P2_resetSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2_resetSelectedActionPerformed
        // The list model of the selected items of the P2 is retrived
        DefaultListModel<String> listModel = (DefaultListModel<String>) P2_itemsSelected.getModel();
        // All the elements of the model are removed
        listModel.removeAllElements();
        // The amount of credits available is updated
        P2_credits = 10;
        // The label showing the amount of credits remaining is updated
        P2_creditsRemaining.setText("10 credits remaining"); 
    }//GEN-LAST:event_P2_resetSelectedActionPerformed

    private void preparation_fightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preparation_fightButtonActionPerformed
            MainMenu.buttonSound();
            this.setVisible(false);
            this.stopPreparationMusic();
            Versus.getCombat().setVisible(true);
            
            P1_inventory = buildInventories("P1");
            P2_inventory = buildInventories("P2");
            P1_team = buildTeams("P1");
            P2_team = buildTeams("P2");
           
            Versus.getCombat().gameSetUp();
            
    }//GEN-LAST:event_preparation_fightButtonActionPerformed

    private void changeFontColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeFontColorActionPerformed
        Color color = HPLabel.getForeground();
        Color black = new Color(0, 0, 0);
        Color white = new Color(255, 255, 255);

        if (color.equals(black)){
            changeFontColor.setBackground(white);
            HPLabel.setForeground(white);
            MPLabel.setForeground(white);
            strengthLabel.setForeground(white);
            agilityLabel.setForeground(white);
            intelligenceLabel.setForeground(white);
            enduranceLabel.setForeground(white);
            description.setForeground(white);
        } else{
            changeFontColor.setBackground(black);
            HPLabel.setForeground(black);
            MPLabel.setForeground(black);
            strengthLabel.setForeground(black);
            agilityLabel.setForeground(black);
            intelligenceLabel.setForeground(black);
            enduranceLabel.setForeground(black);
            description.setForeground(black);

        }
    }//GEN-LAST:event_changeFontColorActionPerformed

    private void P2_fourthSelectionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_fourthSelectionMouseEntered
        displayClassInformations(P2_fourth);
    }//GEN-LAST:event_P2_fourthSelectionMouseEntered

    private void P2_thirdSelectionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_thirdSelectionMouseEntered
        displayClassInformations(P2_third);
    }//GEN-LAST:event_P2_thirdSelectionMouseEntered

    private void P2_secondSelectionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_secondSelectionMouseEntered
        displayClassInformations(P2_second);
    }//GEN-LAST:event_P2_secondSelectionMouseEntered

    private void P2_firstSelectionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P2_firstSelectionMouseEntered
        displayClassInformations(P2_first);
    }//GEN-LAST:event_P2_firstSelectionMouseEntered

    private void P1_fourthSelectionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_fourthSelectionMouseEntered
        displayClassInformations(P1_fourth);
    }//GEN-LAST:event_P1_fourthSelectionMouseEntered

    private void P1_thirdSelectionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_thirdSelectionMouseEntered
        displayClassInformations(P1_third);
    }//GEN-LAST:event_P1_thirdSelectionMouseEntered

    private void P1_secondSelectionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_secondSelectionMouseEntered
        displayClassInformations(P1_second);
    }//GEN-LAST:event_P1_secondSelectionMouseEntered

    private void P1_firstSelectionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_P1_firstSelectionMouseEntered
        displayClassInformations(P1_first);
    }//GEN-LAST:event_P1_firstSelectionMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel HPLabel;
    protected javax.swing.JLabel MPLabel;
    protected javax.swing.JButton P1_backToClassSelection;
    protected javax.swing.JButton P1_cancelReady;
    protected keeptoo.KGradientPanel P1_classSelection;
    private javax.swing.JLabel P1_creditsRemaining;
    protected javax.swing.JLabel P1_first;
    private javax.swing.JLabel P1_firstLabel;
    protected javax.swing.JComboBox<String> P1_firstSelection;
    private javax.swing.JLabel P1_fourth;
    private javax.swing.JLabel P1_fourthLabel;
    protected javax.swing.JComboBox<String> P1_fourthSelection;
    protected keeptoo.KGradientPanel P1_itemSelection;
    private javax.swing.JList<String> P1_itemsList;
    private javax.swing.JList<String> P1_itemsSelected;
    private javax.swing.JLabel P1_label;
    private javax.swing.JButton P1_randomChoices;
    private javax.swing.JButton P1_randomChoicesItems;
    protected javax.swing.JButton P1_readyButton;
    private javax.swing.JButton P1_resetSelected;
    private javax.swing.JLabel P1_second;
    private javax.swing.JLabel P1_secondLabel;
    protected javax.swing.JComboBox<String> P1_secondSelection;
    private javax.swing.JLabel P1_stepDescription1;
    private javax.swing.JLabel P1_stepDescription2;
    private javax.swing.JLabel P1_third;
    private javax.swing.JLabel P1_thirdLabel;
    protected javax.swing.JComboBox<String> P1_thirdSelection;
    protected javax.swing.JButton P1_validateButton;
    protected javax.swing.JButton P2_backToClassSelection;
    protected javax.swing.JButton P2_cancelReady;
    protected keeptoo.KGradientPanel P2_classSelection;
    private javax.swing.JLabel P2_creditsRemaining;
    private javax.swing.JLabel P2_first;
    private javax.swing.JLabel P2_firstLabel;
    private javax.swing.JComboBox<String> P2_firstSelection;
    private javax.swing.JLabel P2_fourth;
    private javax.swing.JLabel P2_fourthLabel;
    private javax.swing.JComboBox<String> P2_fourthSelection;
    protected keeptoo.KGradientPanel P2_itemSelection;
    private javax.swing.JList<String> P2_itemsList;
    private javax.swing.JList<String> P2_itemsSelected;
    private javax.swing.JLabel P2_label;
    private javax.swing.JButton P2_randomChoices;
    private javax.swing.JButton P2_randomChoicesItems;
    protected javax.swing.JButton P2_readyButton;
    private javax.swing.JButton P2_resetSelected;
    private javax.swing.JLabel P2_second;
    private javax.swing.JLabel P2_secondLabel;
    private javax.swing.JComboBox<String> P2_secondSelection;
    private javax.swing.JLabel P2_stepDescription1;
    private javax.swing.JLabel P2_stepDescription2;
    private javax.swing.JLabel P2_third;
    private javax.swing.JLabel P2_thirdLabel;
    private javax.swing.JComboBox<String> P2_thirdSelection;
    protected javax.swing.JButton P2_validateButton;
    protected javax.swing.JLabel agilityLabel;
    private javax.swing.JButton changeFontColor;
    protected javax.swing.JTextArea description;
    protected keeptoo.KGradientPanel descriptionPanel;
    protected javax.swing.JLabel enduranceLabel;
    protected javax.swing.JLabel intelligenceLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel menuBorders;
    protected keeptoo.KGradientPanel preparation_P1;
    protected keeptoo.KGradientPanel preparation_P2;
    protected javax.swing.JButton preparation_backToMain;
    protected javax.swing.JButton preparation_fightButton;
    protected javax.swing.JLabel strengthLabel;
    // End of variables declaration//GEN-END:variables
}
