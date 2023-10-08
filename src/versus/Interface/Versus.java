package versus.Interface;

// ============================
// ==== LOADING LIBRAIRIES ====
// ============================

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.io.File;
import java.awt.CardLayout;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.FileWriter;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import keeptoo.KGradientPanel;
import versus.CharClasses.CharClass;
import versus.Items.Item;

/**
 *
 * @author Louis
 */

public class Versus extends javax.swing.JFrame {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // crucial object to resize the window
    
    public static CharClass[] P1_team;
    public static CharClass[] P2_team;
    public static Item[] P1_inventory;
    public static Item[] P2_inventory;
    
    
    public static Dimension getScreenSize(){
        return screenSize;
    }
    
    // ===============================================
    // ==== CREATION OF THE PANELS OF THE PROGRAM ====
    // ===============================================
    
    // Each menu of the game corresponds to a panel which is instanced statically
    // in the main class which is Versus. This way, it's possible to call a menu
    // from another, and it's required when a menu has to disappear to let place
    // to another one. For example, it permits to setVisible(true) the Preparation
    // Menu from the Main Menu.
    
    private static MainMenu main = new MainMenu();
    private static Options options = new Options();
    private static Preparation preparation = new Preparation();
    private static Combat combat = new Combat(); 

    public static MainMenu getMain(){
        return main;
    }
    public static Options getOptions(){
        return options;
    }
    public static Preparation getPreparation(){
        return preparation;
    }
    public static Combat getCombat(){
        return combat;
    }
    
    // ==========================================
    // ==== CREATION OF A CONFIGURATION FILE ====
    // ==========================================
    
    // The program is bringing an .ini file that allows the user to
    // keep some settings, such as the UI's colors and the dimensions
    // of the game's window, but the location where this .ini file is stored
    // necessarily varies between the OS. We only configured the program
    // for the most used OS: Linux, Windows and MacOS.
    
    public static void createIniFile() {
       
        String osName = System.getProperty("os.name"); // getting the OS' name
        String directoryPath;
        String filePath = "";
        
        // CASE WHERE THE PROGRAM IS RAN ON A LINUX SYSTEM
        if(osName.toLowerCase().contains("linux")){ // the OS' name can change relatively to the distribution
            directoryPath = System.getProperty("user.home") + "/.versus";
            File versusDirectory = new File(directoryPath);
            if (!versusDirectory.exists() && versusDirectory.mkdirs()) {
                System.out.println("Versus directory created successfully.");
            } else {
                System.err.println("Failed to create the Versus directory or it already exists.");
            }
            filePath = directoryPath + "/versus.ini";
            
        // CASE WHERE THE PROGRAM IS RAN ON A WINDOWS SYSTEM
        } else if(osName.toLowerCase().contains("windows")){ // same as for Linux, and for MacOS too
            directoryPath = System.getProperty("user.home") + "/Documents/Versus";
            File versusDirectory = new File(directoryPath);
            if (!versusDirectory.exists() && versusDirectory.mkdir()) {
                System.out.println("Versus directory created successfully.");
            } else {
                System.out.println("Failed to create the Versus directory or it already exists.");
            }
            filePath = directoryPath + "/versus.ini";
            
        // CASE WHERE THE PROGRAM IS RAN ON A MACOS SYSTEM
        } else if(osName.toLowerCase().contains("mac")){
            directoryPath = System.getProperty("user.home") + "/.versus";
            File versusDirectory = new File(directoryPath);
            if (!versusDirectory.exists() && versusDirectory.mkdirs()) {
                System.out.println("Versus directory created successfully.");
            } else {
                System.err.println("Failed to create the Versus directory or it already exists.");
            }
            filePath = directoryPath + "/versus.ini";
            
        // CASE WHERE THE PROGRAM IS RAN ON ANOTHER KIND OF SYSTEM
        } else {
            System.err.println("Unsupported operating system");
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("[Display]");
            writer.println("fullscreen=false");
            writer.println("resolution=1280x720");
            writer.println();
            writer.println("[Interface]");
            writer.println("player1StartColor=255,204,204");
            writer.println("player1EndColor=255,102,102");
            writer.println("player2StartColor=0,255,255");
            writer.println("player2EndColor=0,102,204");
            writer.println("GeneralStartColor=255,0,255");
            writer.println("GeneralEndColor=0,0,255");

            System.out.println("versus.ini file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while creating versus.ini.");
        }
    }
    
    // =============================================================
    // ==== GET THE PROPERTIES STORED IN THE CONFIGURATION FILE ====
    // =============================================================
    public static Properties getProperties(){
        String osName = System.getProperty("os.name"); // The OS' name is retrieved
        String filePath = "";
        // The place where the configuration file is stored depends of the system
        if(osName.toLowerCase().contains("linux")){
            filePath = System.getProperty("user.home") + "/.versus/versus.ini";
        } else if(osName.toLowerCase().contains("windows")){
            filePath = System.getProperty("user.home") + "/Documents/Versus/versus.ini";
        } else if(osName.toLowerCase().contains("mac")){
            filePath = System.getProperty("user.home") + "/.versus/versus.ini";
        // The program is configured to create a configuration file only the most used OS
        } else {
            System.err.println("Unsupported operating system");
            System.exit(0);
        }
        
        // Loading the properties...
        Properties properties = new Properties(); 
        try (FileInputStream inputStream = new FileInputStream(filePath)) { // opening the path to where versus.ini should be
            properties.load(inputStream); // loading versus.ini
        } catch (IOException e) { // if versus.ini isn't found, then...
            e.printStackTrace();  // an error appears
            createIniFile(); // a new versus.ini file is produced
            properties = getProperties(); // the properties of the new file are gathered
        }
        return properties;
    }
    
    // ================================================================
    // ==== UPDATE THE PROPERTIES STORES IN THE CONFIGURATION FILE ====
    // ================================================================
    public static void updateProperties(String update, String property){
        
        String osName = System.getProperty("os.name");
        String filePath = "";
        
        if(osName.toLowerCase().contains("linux")){
            filePath = System.getProperty("user.home") + "/.versus/versus.ini";
        } else if(osName.toLowerCase().contains("windows")){
            filePath = System.getProperty("user.home") + "/Documents/Versus/versus.ini";
        } else if(osName.toLowerCase().contains("mac")){
            filePath = System.getProperty("user.home") + "/.versus/versus.ini";
        } else {
            System.err.println("Unsupported operating system");
            System.exit(0);
        }
        
        Properties properties = getProperties();
        
        String fullscreen;
        String resolution;
        String player1StartColor;
        String player1EndColor;
        String player2StartColor;
        String player2EndColor;
        String GeneralStartColor;
        String GeneralEndColor;
        
        if(property.equals("fullscreen")){
            fullscreen = update;
        } else{
            fullscreen = properties.getProperty("fullscreen");
        }
        if(property.equals("resolution")){
            resolution = update;
        } else{
            resolution = properties.getProperty("resolution");
        }
        if(property.equals("player1StartColor")){
            player1StartColor = update;
        } else{
            player1StartColor = properties.getProperty("player1StartColor");
        }
        if(property.equals("player1EndColor")){
            player1EndColor = update;
        } else{
            player1EndColor = properties.getProperty("player1EndColor");
        }
        if(property.equals("player2StartColor")){
            player2StartColor = update;
        } else{
            player2StartColor = properties.getProperty("player2StartColor");
        }
        if(property.equals("player2EndColor")){
            player2EndColor = update;
        } else{
            player2EndColor = properties.getProperty("player2EndColor");
        }
        if(property.equals("GeneralStartColor")){
            GeneralStartColor = update;
        } else{
            GeneralStartColor = properties.getProperty("GeneralStartColor");
        }
        if(property.equals("GeneralEndColor")){
            GeneralEndColor = update;
        } else{
            GeneralEndColor = properties.getProperty("GeneralEndColor");
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("[Display]");
            writer.println("fullscreen="+fullscreen);
            writer.println("resolution="+resolution);
            writer.println();
            writer.println("[Interface]");
            writer.println("player1StartColor="+player1StartColor);
            writer.println("player1EndColor="+player1EndColor);
            writer.println("player2StartColor="+player2StartColor);
            writer.println("player2EndColor="+player2EndColor);
            writer.println("GeneralStartColor="+GeneralStartColor);
            writer.println("GeneralEndColor="+GeneralEndColor);

            System.out.println(property+" setting has been updated in versus.ini");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while updating versus.ini.");
        }
    }
    
    // =======================================================
    // ==== TO SET THE JFRAME IN THE MIDDLE OF THE SCREEN ====
    // =======================================================
    private void frameMiddleOfScreen(){
        // The width and height of the screen running the program are retrieved
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        // The width and height of the JFrame of the program are retrieved
        int frameWidth = this.getWidth();
        int frameHeight = this.getHeight();
        // The middle of the screen is calculated
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        // The JFrame is sat on the middle of the screen
        this.setLocation(x, y);
    }
    
    // ============================================
    // ==== TO SET THE DIMENSION OF THE WINDOW ====
    // ============================================
    private void setDimensionOfFrame(int width, int height){
        Insets insets = this.getInsets();
        int new_width = width + insets.left + insets.right;
        int new_height = height + insets.top + insets.bottom;
        this.setSize(new_width, new_height);
        frameMiddleOfScreen();
    }
    
    // ==================================================================
    // ==== TO SCALE THE CONTENT OF THE PROGRAM TO THE WINDOW'S SIZE ====
    // ==================================================================
    private void scaleContent(){
        
        int frameWidth;
        int frameHeight;
        
        if(this.isUndecorated()){
            frameWidth = this.getWidth();
            frameHeight = this.getHeight();
        }
        else{
            Insets insets = this.getInsets();
            frameWidth = this.getWidth() - insets.left - insets.right;
            frameHeight = this.getHeight() - insets.top - insets.bottom;
        }
        
        main.scaleMenu(frameWidth, frameHeight);
        options.scaleMenu(frameWidth, frameHeight);
        preparation.scaleMenu(frameWidth, frameHeight);
        combat.scaleMenu(frameWidth, frameHeight);
 
    }
    
    // ==================================================
    // ==== TO SET THE COLORS OF THE KGRADIENTPANELS ====
    // ==================================================
    public void setColorProperties(String startColorStr, String endColorStr, KGradientPanel gradientPanel) {
        String[] startColorArray = startColorStr.split(",");
        String[] endColorArray = endColorStr.split(",");

        if (startColorArray.length == 3 && endColorArray.length == 3) {
            Color startColor = new Color(Integer.parseInt(startColorArray[0]), Integer.parseInt(startColorArray[1]), Integer.parseInt(startColorArray[2]));
            Color endColor = new Color(Integer.parseInt(endColorArray[0]), Integer.parseInt(endColorArray[1]), Integer.parseInt(endColorArray[2]));

            gradientPanel.kStartColor = startColor;
            gradientPanel.kEndColor = endColor;
        }
    }
    
    // #####################
    // #### CONSTRUCTOR ####
    // #####################
    
    // acting as the program's main
    public Versus() {
        initComponents();
        
        // =========================================
        // ==== SETTING THE ICON OF THE PROGRAM ====
        // =========================================
        Image icon = new ImageIcon(this.getClass().getResource("/assets/icons/logo.png")).getImage();
        this.setIconImage(icon);
        
        // ==========================================
        // ==== ADDING THE PANELS TO THE PROGRAM ====
        // ==========================================
        
        // CardLayout allows multiple JPanels to share the same display place
        CardLayout cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        
        // Adding the JPanels to Versus' JFrame
        this.add(main, "mainPanel");
        this.add(options, "optionsPanel");
        this.add(preparation, "preparationPanel");
        this.add(combat, "combatPanel");
        
        // Defaultly, only the main menu is visible
        main.setVisible(true);
        options.setVisible(false);
        preparation.setVisible(false);
        combat.setVisible(false);
        

        // ====================================
        // ==== LISTENERS FOR OPTIONS MENU ====
        // ====================================
        
        // These listeners are placed there instead of inside the Options class
        // because it uses the dimensions of the JFrame, which are more easily
        // accessible directly in the JFrame's class.
        
        options.options_button_fullscreen.addActionListener((ActionEvent e) -> {
            //Insets insets = this.getInsets();
            //setDimensionOfFrame(screenSize.width + insets.left + insets.right, screenSize.width + insets.top + insets.bottom);
            
            setDimensionOfFrame(screenSize.width, screenSize.width);
            ImageIcon newBackground = new ImageIcon("src/assets/menus/background_animation.gif"); // Replace with the actual path
            main.main_backgroundAnimation.setIcon(newBackground);
            ImageIcon newForeground = new ImageIcon("src/assets/menus/main_menu.png"); // Replace with the actual path
            main.main_foreground.setIcon(newForeground);
            scaleContent();
            this.dispose();
            this.setUndecorated(true);
            this.setVisible(true);
            scaleContent();
            
            String newDim = Integer.toString(screenSize.width)+"x"+Integer.toString(screenSize.height);
            updateProperties(newDim,"resolution");
            updateProperties("true","fullscreen");
            options.options_button_fullscreen.setEnabled(false);
            options.options_button_1080p.setEnabled(true);
            options.options_button_720p.setEnabled(true);
            options.options_button_autodim.setEnabled(true);
            //this.setExtendedState(this.MAXIMIZED_BOTH);
        });
        
        options.options_button_1080p.addActionListener((ActionEvent e) -> {
            setDimensionOfFrame(1920, 1080);
            ImageIcon newBackground = new ImageIcon("src/assets/menus/background_animation.gif"); // Replace with the actual path
            main.main_backgroundAnimation.setIcon(newBackground);
            ImageIcon newForeground = new ImageIcon("src/assets/menus/main_menu.png"); // Replace with the actual path
            main.main_foreground.setIcon(newForeground);
            scaleContent();
            this.dispose();
            this.setUndecorated(false);
            this.setVisible(true);
            scaleContent();
            
            updateProperties("1920x1080","resolution");
            updateProperties("false","fullscreen");
            options.options_button_1080p.setEnabled(false);
            options.options_button_fullscreen.setEnabled(true);
            options.options_button_720p.setEnabled(true);
            options.options_button_autodim.setEnabled(true);
            //this.setExtendedState(this.MAXIMIZED_BOTH);
        });
        
        options.options_button_720p.addActionListener((ActionEvent e) -> {
            setDimensionOfFrame(1280, 720);
            ImageIcon newBackground = new ImageIcon("src/assets/menus/background_animation.gif"); // Replace with the actual path
            main.main_backgroundAnimation.setIcon(newBackground);
            ImageIcon newForeground = new ImageIcon("src/assets/menus/main_menu.png"); // Replace with the actual path
            main.main_foreground.setIcon(newForeground);
            scaleContent();
            this.dispose();
            this.setUndecorated(false);
            this.setVisible(true);
            scaleContent();
            
            updateProperties("1280x720","resolution");
            updateProperties("false","fullscreen");
            options.options_button_720p.setEnabled(false);
            options.options_button_1080p.setEnabled(true);
            options.options_button_fullscreen.setEnabled(true);
            options.options_button_autodim.setEnabled(true);
        });
        
        options.options_button_autodim.addActionListener((ActionEvent e) -> {
            int originalScreenWidth = screenSize.width;
            int autoWidth = originalScreenWidth - 200;
            double aspectRatio = 16.0 / 9.0;
            int autoHeight = (int) (autoWidth / aspectRatio);
            setDimensionOfFrame(autoWidth, autoHeight);

            ImageIcon newBackground = new ImageIcon("src/assets/menus/background_animation.gif"); // Replace with the actual path
            main.main_backgroundAnimation.setIcon(newBackground);
            ImageIcon newForeground = new ImageIcon("src/assets/menus/main_menu.png"); // Replace with the actual path
            main.main_foreground.setIcon(newForeground);
            scaleContent();
            this.dispose();
            this.setUndecorated(false);
            this.setVisible(true);
            scaleContent();

            String newDim = Integer.toString(autoWidth)+"x"+Integer.toString(autoHeight);
            updateProperties(newDim,"resolution");
            updateProperties("false","fullscreen");
            options.options_button_autodim.setEnabled(false);
            options.options_button_720p.setEnabled(true);
            options.options_button_1080p.setEnabled(true);
            options.options_button_fullscreen.setEnabled(true);
        });
        
        // =====================================================
        // ==== LOADING THE CONFIGURATION FILE'S PROPERTIES ====
        // =====================================================
        
         // getting the properties from versus.ini
        Properties properties = getProperties();
        // gathering the properties in String variables
        String fullscreen = properties.getProperty("fullscreen");
        String resolution = properties.getProperty("resolution");
        String player1StartColor = properties.getProperty("player1StartColor");
        String player1EndColor = properties.getProperty("player1EndColor");
        String player2StartColor = properties.getProperty("player2StartColor");
        String player2EndColor = properties.getProperty("player2EndColor");
        String GeneralStartColor = properties.getProperty("GeneralStartColor");
        String GeneralEndColor = properties.getProperty("GeneralEndColor");
        
        // ============================================
        // ==== CONFIGURE THE PROGRAM'S DIMENSIONS ====
        // ============================================
        
        // CHECKING FULLSCREEN PROPERTY
        if(fullscreen.equals("true")){ // if true is the value of the property fullscreen, then...
            this.dispose(); // the JFrame of the program disappears
            this.setUndecorated(true); // the JFrame is configured so that its border disappears
            this.setVisible(true); // the JFrame reappears, without it's border
            this.setExtendedState(this.MAXIMIZED_BOTH); // the JFrame is extended to the edge of the screen it's hasPlayed one
        }
         // CHECKING DIMENSION PROPERTY
        String[] res_settings = resolution.split("x"); // getting the resolution settings
        
        // if two parameters are found (width and length), then...
        if(res_settings.length == 2){

            int width = Integer.parseInt(res_settings[0]); // we keep width in a new String named width
            int height = Integer.parseInt(res_settings[1]); // we keep height in a new String named width 
            setDimensionOfFrame(width, height); // setting the JFrame's dimension
            scaleContent(); // scale the elements with the JPanel's dimensions
            
            if(fullscreen.equals("true")){
                options.options_button_fullscreen.setEnabled(false);
            } else if(width == 1920 && height == 1080){
                options.options_button_1080p.setEnabled(false);
            } else if(width == 1280 && height == 720){
                options.options_button_720p.setEnabled(false);
            } else{
                options.options_button_autodim.setEnabled(false);
            }
        // default, in the case where it's wrongly defined
        } else 
        {
            int width = 1280;// we keep width in a new String named width
            int height = 720; // we keep height in a new String named width
            // setting the JFrame's dimension
            setDimensionOfFrame(width, height);
        }
        
        // ========================================
        // ==== CONFIGURE THE PROGRAM'S COLORS ====
        // ========================================
        
        // setting the colors of player 1
        setColorProperties(player1StartColor, player1EndColor, options.options_colorExampleP1);
        setColorProperties(player1StartColor, player1EndColor, preparation.preparation_P1);
        setColorProperties(player1StartColor, player1EndColor, preparation.P1_classSelection);
        setColorProperties(player1StartColor, player1EndColor, preparation.P1_itemSelection);
        setColorProperties(player1StartColor, player1EndColor, combat.P1_teamStats);

        // setting the colors of player 2
        setColorProperties(player2StartColor, player2EndColor, options.options_colorExampleP2);
        setColorProperties(player2StartColor, player2EndColor, preparation.preparation_P2);
        setColorProperties(player2StartColor, player2EndColor, preparation.P2_classSelection);
        setColorProperties(player2StartColor, player2EndColor, preparation.P2_itemSelection);
        setColorProperties(player2StartColor, player2EndColor, combat.P2_teamStats);

        // setting the general colors
        setColorProperties(GeneralStartColor, GeneralEndColor, options.options_panel);
        setColorProperties(GeneralStartColor, GeneralEndColor, preparation.descriptionPanel);
        setColorProperties(GeneralStartColor, GeneralEndColor, combat.actionPanel);
        setColorProperties(GeneralStartColor, GeneralEndColor, combat.choicePanel);
        setColorProperties(GeneralStartColor, GeneralEndColor, combat.magicPanel);
        setColorProperties(GeneralStartColor, GeneralEndColor, combat.itemsPanel);
        setColorProperties(GeneralStartColor, GeneralEndColor, combat.defensePanel);
        setColorProperties(GeneralStartColor, GeneralEndColor, combat.attackPanel);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Versus");
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    


    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) { // L&F choice between the quotes
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Versus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Versus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Versus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Versus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Versus().setVisible(true);
            }
        });
    }


}
