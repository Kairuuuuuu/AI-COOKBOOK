package cookbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dirk's CookBook");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(390, 844); 
        frame.setLayout(null); 
        frame.setLocationRelativeTo(null); 

        Color darkGreen = new Color(14, 71, 17);

        // 1. Top Navigation Bar
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
        title1.setFont(new Font("SansSerif", Font.PLAIN, 28));
        title1.setForeground(darkGreen);
        title1.setBounds(0, 25, 390, 35);
        topBar.add(title1);

        JLabel title2 = new JLabel("CookBook", SwingConstants.CENTER);
        title2.setFont(new Font("SansSerif", Font.PLAIN, 28));
        title2.setForeground(darkGreen);
        title2.setBounds(0, 55, 390, 35);
        topBar.add(title2);

        frame.add(topBar);

        // 2. The Form Card 
        RoundPanel formCard = new RoundPanel();
        formCard.setBounds(37, 280, 300, 160);
        formCard.setLayout(null);

        // Username Field (Now uses professional placeholder!)
        RoundTextField usernameField = new RoundTextField("Username");
        usernameField.setBounds(20, 20, 260, 40);
        usernameField.setForeground(Color.BLACK); // Real text will be black
        formCard.add(usernameField);

        // Password Field (Now uses professional placeholder!)
        RoundPasswordField passwordField = new RoundPasswordField("Password");
        passwordField.setBounds(20, 75, 260, 40);
        passwordField.setForeground(Color.BLACK); // Real text will be black
        formCard.add(passwordField);

        JLabel forgotPass = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotPass.setFont(new Font("SansSerif", Font.PLAIN, 10));
        forgotPass.setForeground(darkGreen);
        forgotPass.setBounds(185, 120, 100, 20);
        formCard.add(forgotPass);

        frame.add(formCard);

        // 3. Login Button (Animated)
        AnimatedButton loginButton = new AnimatedButton("LOGIN");
        loginButton.setBounds(37, 460, 300, 45);
        loginButton.setBackground(darkGreen);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        frame.add(loginButton);
        
        loginButton.addActionListener(e -> {
            frame.dispose(); // This permanently closes the Login window
            MainMenu.showMenu(); // This opens the new Main Menu window!
        });

        // 4. Bottom Text
        JLabel signUpText = new JLabel("<html>New to Dirk's CookBook? <b>Create an account</b></html>", SwingConstants.CENTER);
        signUpText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        signUpText.setForeground(darkGreen);
        signUpText.setBounds(0, 750, 390, 20);
        frame.add(signUpText);

        // 5. Background Image 
        try {
            ImageIcon bgIcon = new ImageIcon("BackgroundImage_LoginScreen.jpg");
            Image scaledImage = bgIcon.getImage().getScaledInstance(390, 844, Image.SCALE_SMOOTH);
            JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
            backgroundLabel.setBounds(0, 0, 390, 844);
            frame.add(backgroundLabel);
        } catch (Exception e) {
            System.out.println("Could not load image.");
        }

        frame.setVisible(true);
    }

    // =====================================================================
    // THE "KEEP IT SIMPLE" CUSTOM SHAPES
    // =====================================================================

    static class RoundPanel extends JPanel {
        public RoundPanel() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(255, 255, 255, 245));
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        }
    }

    // Upgraded: Rounded text field that draws the gray placeholder if empty
    static class RoundTextField extends JTextField {
        private String placeholder;
        
        public RoundTextField(String placeholder) { 
            this.placeholder = placeholder; 
            setOpaque(false); 
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); 
        }
        
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw the white box and green border
            g.setColor(Color.WHITE);
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
            g.setColor(new Color(14, 71, 17)); 
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
            
            // Draw the actual user text
            super.paintComponent(g);
            
            // Draw the gray placeholder ONLY if the user hasn't typed anything yet
            if (getText().isEmpty()) {
                g.setColor(Color.GRAY);
                int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                g.drawString(placeholder, getInsets().left, y);
            }
        }
    }

    // Upgraded: Rounded password field that draws the gray placeholder if empty
    static class RoundPasswordField extends JPasswordField {
        private String placeholder;
        
        public RoundPasswordField(String placeholder) { 
            this.placeholder = placeholder; 
            setOpaque(false); 
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 45)); 
        }
        
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw the white box and green border
            g.setColor(Color.WHITE);
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
            g.setColor(new Color(14, 71, 17)); 
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
            
            // Draw the actual user text
            super.paintComponent(g);
            
            // Draw the gray placeholder ONLY if the user hasn't typed a password yet
            if (getPassword().length == 0) {
                g.setColor(Color.GRAY);
                int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                g.drawString(placeholder, getInsets().left, y);
            }
        }
    }

    static class AnimatedButton extends JButton {
        int shrink = 0; 
        public AnimatedButton(String text) {
            super(text); setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) { shrink = 2; repaint(); } 
                public void mouseReleased(MouseEvent e) { shrink = 0; repaint(); } 
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (shrink > 0) g2.translate(shrink, shrink);
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth()-1-(shrink*2), getHeight()-1-(shrink*2), 40, 40);
            super.paintComponent(g);
            if (shrink > 0) g2.translate(-shrink, -shrink);
        }
    }
}
