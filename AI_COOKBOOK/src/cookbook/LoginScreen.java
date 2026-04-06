package cookbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginScreen {

    static Color darkGreen = new Color(14, 71, 17);
    static Color formBackground = new Color(255, 255, 255, 245); 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Dirk's CookBook");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(390, 844); 
        frame.setLayout(null); 
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); 

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 390, 844);
        frame.add(layeredPane);

        // --- LAYER 1: Background Image ---
        String backgroundFilename = "BackgroundImage_LoginScreen.jpg";
        try {
            final ImageIcon bgIcon = new ImageIcon(backgroundFilename);
            if (bgIcon.getIconWidth() != -1) {
                JPanel backgroundPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(bgIcon.getImage(), 0, 0, 390, 844, this);
                    }
                };
                backgroundPanel.setBounds(0, 0, 390, 844);
                layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
            } else {
                frame.getContentPane().setBackground(new Color(220, 220, 220));
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading background.");
        }

        // --- LAYER 2: UI Controls ---
        JPanel uiControlsContainer = new JPanel();
        uiControlsContainer.setBounds(0, 0, 390, 844);
        uiControlsContainer.setOpaque(false); 
        uiControlsContainer.setLayout(null); 
        layeredPane.add(uiControlsContainer, JLayeredPane.PALETTE_LAYER); 

        JPanel topBar = new JPanel();
        topBar.setBounds(0, 0, 390, 100);
        topBar.setBackground(Color.WHITE);
        topBar.setLayout(null);

        JLabel menuIcon = new JLabel("≡");
        menuIcon.setFont(new Font("Public Sans", Font.BOLD, 30));
        menuIcon.setForeground(darkGreen);
        menuIcon.setBounds(20, 40, 40, 40);
        topBar.add(menuIcon);

        // Perfectly centered titles using SwingConstants.CENTER
        JLabel titleLabel1 = new JLabel("Dirk's");
        titleLabel1.setFont(new Font("Public Sans", Font.PLAIN, 28));
        titleLabel1.setForeground(darkGreen);
        titleLabel1.setBounds(0, 25, 390, 35); 
        titleLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel2 = new JLabel("CookBook");
        titleLabel2.setFont(new Font("Public Sans", Font.PLAIN, 28));
        titleLabel2.setForeground(darkGreen);
        titleLabel2.setBounds(0, 55, 390, 35); 
        titleLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        topBar.add(titleLabel1);
        topBar.add(titleLabel2);
        uiControlsContainer.add(topBar);

        // Adjusted X coordinate to 37 for perfect centering
        RoundedPanel formCardPanel = new RoundedPanel(20, formBackground);
        formCardPanel.setBounds(37, 280, 300, 160);
        formCardPanel.setLayout(null);

        PlaceholderTextField usernameField = new PlaceholderTextField("Username", 10);
        usernameField.setBounds(20, 20, 260, 40);
        usernameField.setForeground(Color.BLACK); 
        usernameField.setFont(new Font("Public Sans", Font.PLAIN, 14));
        formCardPanel.add(usernameField);

        PlaceholderPasswordField passwordField = new PlaceholderPasswordField("Password", 10);
        passwordField.setBounds(20, 75, 260, 40);
        passwordField.setForeground(Color.BLACK); 
        passwordField.setFont(new Font("Public Sans", Font.PLAIN, 14));
        formCardPanel.add(passwordField);

        JLabel eyeIcon = new JLabel("👁️");
        eyeIcon.setFont(new Font("Public Sans", Font.PLAIN, 18));
        eyeIcon.setForeground(darkGreen);
        eyeIcon.setBounds(255, 85, 25, 20); 
        formCardPanel.add(eyeIcon);

        JLabel forgotPass = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotPass.setFont(new Font("Public Sans", Font.PLAIN, 10));
        forgotPass.setForeground(darkGreen);
        forgotPass.setBounds(185, 120, 100, 20);
        formCardPanel.add(forgotPass);

        uiControlsContainer.add(formCardPanel);

        // 3. Login Button (Adjusted X coordinate to 37)
        RoundedButton loginButton = new RoundedButton("LOGIN", 45);
        loginButton.setBounds(37, 460, 300, 45);
        loginButton.setBackground(darkGreen);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Public Sans", Font.BOLD, 14));
        uiControlsContainer.add(loginButton);

        // Perfectly centered bottom text
        JLabel signUpText = new JLabel("<html>New to Dirk's CookBook? <b>Create an account</b></html>");
        signUpText.setFont(new Font("Public Sans", Font.PLAIN, 12));
        signUpText.setForeground(darkGreen);
        signUpText.setBounds(0, 750, 390, 20);
        signUpText.setHorizontalAlignment(SwingConstants.CENTER);
        uiControlsContainer.add(signUpText);

        frame.setVisible(true);
    }

    // =====================================================================
    // CUSTOM CLASSES FOR ROUNDED SHAPES & ANIMATIONS
    // =====================================================================

    static class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    static class RoundedButton extends JButton {
        private int radius;
        private double currentScale = 1.0;
        private double targetScale = 1.0;
        private Timer animationTimer;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false); 
            setFocusPainted(false);      
            setBorderPainted(false);     
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    targetScale = 0.92; 
                    startAnimation();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    targetScale = 1.0;  
                    startAnimation();
                }
            });
        }

        private void startAnimation() {
            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }
            animationTimer = new Timer(10, e -> {
                currentScale += (targetScale - currentScale) * 0.3; 
                
                if (Math.abs(currentScale - targetScale) < 0.001) {
                    currentScale = targetScale;
                    ((Timer) e.getSource()).stop();
                }
                repaint(); 
            });
            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            g2.translate(centerX, centerY);
            g2.scale(currentScale, currentScale);
            g2.translate(-centerX, -centerY);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            super.paintComponent(g2); 
            
            g2.dispose();
        }
    }

    static class PlaceholderTextField extends JTextField {
        private String placeholder;
        private int radius;

        public PlaceholderTextField(String placeholder, int radius) {
            this.placeholder = placeholder;
            this.radius = radius;
            setOpaque(false); 
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            g2.setColor(darkGreen);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            super.paintComponent(g);

            if (getText().isEmpty()) {
                g2.setColor(Color.GRAY);
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, getInsets().left, y);
            }
            g2.dispose();
        }
    }

    static class PlaceholderPasswordField extends JPasswordField {
        private String placeholder;
        private int radius;

        public PlaceholderPasswordField(String placeholder, int radius) {
            this.placeholder = placeholder;
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 45)); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            g2.setColor(darkGreen);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            super.paintComponent(g);

            if (getPassword().length == 0) {
                g2.setColor(Color.GRAY);
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, getInsets().left, y);
            }
            g2.dispose();
        }
    }
}