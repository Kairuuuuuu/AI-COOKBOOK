package cookbook;

import javax.swing.*;

import cookbook.MainMenu.RoundDollarIcon;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class PantryScreen {

    // Keep track of how many items we add so we know where to place the next one!
    static int itemCount = 0; 
    static JPanel pantryGrid; 
    static JFrame frame;

    public static void showPantry() {
        frame = new JFrame("Dirk's CookBook - My Pantry");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(390, 844); 
        frame.setLayout(null); 
        frame.setLocationRelativeTo(null); 

        Color darkGreen = new Color(14, 71, 17);

        // ==========================================
        // 1. THE GLASS PANE (For the darkened effect)
        // ==========================================
        JPanel glassPane = new JPanel();
        glassPane.setOpaque(true);
        glassPane.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
        frame.setGlassPane(glassPane);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 390, 844);
        frame.add(layeredPane);

        JPanel uiContainer = new JPanel();
        uiContainer.setBounds(0, 0, 390, 844);
        uiContainer.setOpaque(false);
        uiContainer.setLayout(null);
        layeredPane.add(uiContainer, JLayeredPane.MODAL_LAYER); 

        // ==========================================
        // 2. TOP NAVIGATION BAR
        // ==========================================
        JPanel topBar = new JPanel();
        topBar.setBounds(0, 0, 390, 100);
        topBar.setBackground(Color.WHITE);
        topBar.setLayout(null);

        JLabel menuIcon = new JLabel("≡");
        menuIcon.setFont(new Font("SansSerif", Font.BOLD, 30));
        menuIcon.setForeground(darkGreen);
        menuIcon.setBounds(20, 40, 40, 40);
        topBar.add(menuIcon);

        JLabel title1 = new JLabel("Dirk's", SwingConstants.CENTER);
        title1.setFont(new Font("Serif", Font.PLAIN, 28));
        title1.setForeground(darkGreen);
        title1.setBounds(0, 25, 390, 35);
        topBar.add(title1);

        JLabel title2 = new JLabel("CookBook", SwingConstants.CENTER);
        title2.setFont(new Font("Serif", Font.PLAIN, 28));
        title2.setForeground(darkGreen);
        title2.setBounds(0, 55, 390, 35);
        topBar.add(title2);

        RoundDollarIcon dollarIcon = new RoundDollarIcon();
        dollarIcon.setBounds(330, 45, 30, 30);
        dollarIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        dollarIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 1. Show the darkening effect
                frame.getGlassPane().setVisible(true);
                
                // 2. Show the separate AddBudget popup
                AddBudget.showBudgetMenu(frame); 
                
                // 3. Hide the darkening effect after the popup is closed
                frame.getGlassPane().setVisible(false);
            }
        });
        topBar.add(dollarIcon);

        uiContainer.add(topBar);

        // ==========================================
        // 3. SEARCH BAR
        // ==========================================
        RoundTextField searchBar = new RoundTextField("🔍 Search Pantry");
        searchBar.setBounds(25, 115, 330, 40);
        searchBar.setForeground(Color.BLACK);
        uiContainer.add(searchBar);

        // ==========================================
        // 4. DYNAMIC PANTRY GRID
        // ==========================================
        // Instead of hardcoding cards, we create an empty container
        pantryGrid = new JPanel();
        pantryGrid.setBounds(0, 175, 390, 545);
        pantryGrid.setOpaque(false);
        pantryGrid.setLayout(null); // We will calculate positions manually
        uiContainer.add(pantryGrid);

        // ==========================================
        // 5. FLOATING ADD BUTTON (+)
        // ==========================================
        FloatingAddButton fab = new FloatingAddButton();
        fab.setBounds(285, 630, 65, 65);
        uiContainer.add(fab);

        // MAGIC: Click the + button to open the popup!
        fab.addActionListener(e -> {
            glassPane.setVisible(true); // Turn on the dark background
            showAddMenu(); // Open the popup
        });
        
     // ==========================================
        // 6. BOTTOM NAVIGATION BAR 
        // ==========================================
        JPanel bottomNav = new JPanel();
        bottomNav.setBounds(0, 720, 390, 90);
        bottomNav.setBackground(darkGreen);
        bottomNav.setLayout(null);

        NavItem homeTab = new NavItem("🏠", "Home", false);  
        homeTab.setBounds(45, 10, 60, 60);
        
        homeTab.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                frame.dispose(); 
                MainMenu.showMenu(); 
            }
        });
        bottomNav.add(homeTab);

        NavItem pantryTab = new NavItem("📋", "My Pantry", true); // Active
        pantryTab.setBounds(165, 10, 60, 60);
        bottomNav.add(pantryTab);

        NavItem chatTab = new NavItem("💬", "AI Chat", false);
        chatTab.setBounds(280, 10, 60, 60);
        bottomNav.add(chatTab);

        uiContainer.add(bottomNav);

        // ==========================================
        // 7. BACKGROUND & OVERLAY
        // ==========================================
        JPanel fadeOverlay = new JPanel();
        fadeOverlay.setBounds(0, 0, 390, 844);
        fadeOverlay.setBackground(new Color(245, 245, 245, 120)); 
        layeredPane.add(fadeOverlay, JLayeredPane.PALETTE_LAYER);

        try {
            ImageIcon bgIcon = new ImageIcon("BackgroundImage_LoginScreen.png");
            if (bgIcon.getIconWidth() != -1) {
                JPanel backgroundPanel = new JPanel() {
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(bgIcon.getImage(), 0, 0, 390, 844, this);
                    }
                };
                backgroundPanel.setBounds(0, 0, 390, 844);
                layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER); 
            }
        } catch (Exception e) {}

        frame.setVisible(true);
    }

    // =====================================================================
    // THE POPUP MENU LOGIC (Add New Item)
    // ==========================================
    private static void showAddMenu() {
        // Create a floating dialog box over our main frame
        JDialog dialog = new JDialog(frame, true);
        dialog.setUndecorated(true); // Remove default windows borders
        dialog.setBackground(new Color(0, 0, 0, 0)); // Make it fully transparent
        dialog.setSize(300, 470);
        dialog.setLocationRelativeTo(frame); // Center it on the app

        // The white rounded background for the popup
        RoundPanel popupPanel = new RoundPanel();
        popupPanel.setBackground(Color.WHITE);
        popupPanel.setOpaque(false);
        popupPanel.setLayout(null);
        dialog.add(popupPanel);

        Color darkGreen = new Color(14, 71, 17);

        // Title
        JLabel title = new JLabel("Add New Item to Pantry", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.PLAIN, 18));
        title.setForeground(darkGreen);
        title.setBounds(0, 20, 300, 25);
        popupPanel.add(title);

        // Image Insert Box (Placeholder)
        JPanel imageBox = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(new Color(230, 230, 230)); // Light Gray
                g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g.setColor(Color.GRAY);
                g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        imageBox.setOpaque(false);
        imageBox.setBounds(25, 55, 250, 120);
        imageBox.setLayout(new BorderLayout());
        JLabel imgText = new JLabel("<html><div style='text-align:center;'>📷<br>Insert Image</div></html>", SwingConstants.CENTER);
        imgText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        imageBox.add(imgText, BorderLayout.CENTER);
        popupPanel.add(imageBox);

        // Form Fields
        JLabel nameLabel = new JLabel("Item Name");
        nameLabel.setBounds(25, 185, 250, 15);
        popupPanel.add(nameLabel);
        RoundTextField nameField = new RoundTextField("Enter Item Name...");
        nameField.setBounds(25, 200, 250, 35);
        popupPanel.add(nameField);

        JLabel qtyLabel = new JLabel("Quantity");
        qtyLabel.setBounds(25, 245, 250, 15);
        popupPanel.add(qtyLabel);
        RoundTextField qtyField = new RoundTextField("Enter Quantity...");
        qtyField.setBounds(25, 260, 250, 35);
        popupPanel.add(qtyField);

        JLabel expLabel = new JLabel("Expiry Date");
        expLabel.setBounds(25, 305, 250, 15);
        popupPanel.add(expLabel);
        RoundTextField expField = new RoundTextField("MM/DD/YYYY   📅");
        expField.setBounds(25, 320, 250, 35);
        popupPanel.add(expField);

        // Buttons
        AnimatedButton cancelBtn = new AnimatedButton("Cancel", false);
        cancelBtn.setBounds(25, 390, 115, 35);
        cancelBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        popupPanel.add(cancelBtn);

        AnimatedButton addBtn = new AnimatedButton("Add to Pantry", true);
        addBtn.setBounds(150, 390, 125, 35);
        addBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        popupPanel.add(addBtn);

        // BUTTON ACTIONS
        cancelBtn.addActionListener(e -> {
            frame.getGlassPane().setVisible(false); // Turn off dark overlay
            dialog.dispose(); // Close popup
        });

        addBtn.addActionListener(e -> {
            // 1. Get the text the user typed
            String newName = nameField.getText().isEmpty() || nameField.getText().equals("Enter Item Name...") ? "New Food" : nameField.getText();
            String newQty = qtyField.getText().isEmpty() || qtyField.getText().equals("Enter Quantity...") ? "Quantity: ?" : "Quantity: " + qtyField.getText();
            
            // 2. Add it to the screen!
            addItemToGrid(newName, newQty);
            
            // 3. Clean up
            frame.getGlassPane().setVisible(false);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    // This calculates exactly where the new box should go based on how many exist!
    private static void addItemToGrid(String name, String qty) {
        // Calculate X and Y mathematically to form a 2-column grid
        int xPos = 25 + (itemCount % 2) * 170; 
        int yPos = 0 + (itemCount / 2) * 170;  

        // Create the card (using default green status for now)
        PantryCard newCard = new PantryCard("default_food.jpg", name, qty, "Fresh", new Color(40, 167, 69));
        newCard.setBounds(xPos, yPos, 155, 155);
        
        pantryGrid.add(newCard);
        pantryGrid.revalidate(); // Tell Java the layout changed
        pantryGrid.repaint();    // Redraw the screen

        itemCount++; // Increase the tracker for next time
    }

    // =====================================================================
    // REUSABLE SIMPLE CUSTOM SHAPES
    // =====================================================================

    static class PantryCard extends JPanel {
        Color statusColor;
        public PantryCard(String imgPath, String title, String qty, String status, Color statusColor) {
            this.statusColor = statusColor;
            setLayout(null);
            setOpaque(false);
            RoundImagePanel img = new RoundImagePanel(imgPath);
            img.setBounds(0, 0, 155, 95);
            add(img);
            JLabel tLabel = new JLabel(title);
            tLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            tLabel.setBounds(10, 100, 135, 20);
            add(tLabel);
            JLabel qLabel = new JLabel(qty);
            qLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            qLabel.setForeground(Color.DARK_GRAY);
            qLabel.setBounds(10, 118, 135, 15);
            add(qLabel);
            JLabel sLabel = new JLabel(status);
            sLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            sLabel.setForeground(statusColor);
            sLabel.setBounds(22, 133, 120, 15);
            add(sLabel);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            g2.setColor(statusColor);
            g2.fillOval(10, 136, 8, 8);
            super.paintComponent(g);
        }
    }

    static class FloatingAddButton extends JButton {
        public FloatingAddButton() {
            super("+");
            setFont(new Font("SansSerif", Font.PLAIN, 40));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(14, 71, 17));
            g2.fillOval(0, 0, getWidth()-1, getHeight()-1);
            super.paintComponent(g);
        }
    }

    static class NavItem extends JPanel {
        boolean isActive;
        public NavItem(String iconText, String titleText, boolean isActive) {
            this.isActive = isActive;
            setOpaque(false);
            setLayout(null);
            JLabel icon = new JLabel(iconText, SwingConstants.CENTER);
            icon.setFont(new Font("SansSerif", Font.PLAIN, 24));
            icon.setForeground(Color.WHITE);
            icon.setBounds(0, 8, 60, 30);
            add(icon);
            JLabel title = new JLabel(titleText, SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.PLAIN, 10));
            title.setForeground(Color.WHITE);
            title.setBounds(0, 38, 60, 15);
            add(title);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isActive) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 60)); 
                g2.fillOval(3, 3, 54, 54); 
            }
        }
    }

    static class RoundPanel extends JPanel {
        public RoundPanel() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        }
    }

    static class RoundDollarIcon extends JPanel {
        public RoundDollarIcon() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(14, 71, 17));
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("$", 10, 21);
        }
    }

    static class RoundImagePanel extends JPanel {
        Image image;
        public RoundImagePanel(String imagePath) {
            setOpaque(false);
            try { 
                ImageIcon icon = new ImageIcon(imagePath);
                if (icon.getIconWidth() != -1) image = icon.getImage();
            } catch (Exception e) {}
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            if (image != null) {
                g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    static class RoundTextField extends JTextField {
        private String placeholder;
        public RoundTextField(String placeholder) { 
            this.placeholder = placeholder; 
            setOpaque(false); 
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); 
            
            // Remove placeholder when clicked
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            g.setColor(new Color(14, 71, 17)); 
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                g.setColor(Color.GRAY);
                int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                g.drawString(placeholder, getInsets().left, y);
            }
        }
    }

    static class AnimatedButton extends JButton {
        int shrink = 0; 
        boolean isSolid;
        public AnimatedButton(String text, boolean isSolid) {
            super(text); 
            this.isSolid = isSolid;
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            if(isSolid) { setForeground(Color.WHITE); } else { setForeground(new Color(14, 71, 17)); }
            
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) { shrink = 2; repaint(); } 
                public void mouseReleased(MouseEvent e) { shrink = 0; repaint(); } 
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (shrink > 0) g2.translate(shrink, shrink);
            
            if (isSolid) {
                g.setColor(new Color(14, 71, 17)); 
                g.fillRoundRect(0, 0, getWidth()-1-(shrink*2), getHeight()-1-(shrink*2), 30, 30);
            } else {
                g.setColor(Color.WHITE); 
                g.fillRoundRect(0, 0, getWidth()-1-(shrink*2), getHeight()-1-(shrink*2), 30, 30);
                g.setColor(new Color(14, 71, 17)); 
                g.drawRoundRect(0, 0, getWidth()-1-(shrink*2), getHeight()-1-(shrink*2), 30, 30);
            }
            super.paintComponent(g);
            if (shrink > 0) g2.translate(-shrink, -shrink);
        }
    }
}
