import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public Main() {
        setTitle("Find Shortest Path Game");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Content panel with background and layout
        cardLayout = new CardLayout();
        contentPanel = new BackgroundPanel("src/Assests/bb.png");
        contentPanel.setLayout(cardLayout);

        // Title label
        JLabel titleLabel = new JLabel("Find Shortest Path Game");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(new Color(255, 128, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.add(Box.createVerticalStrut(40));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(50));

        // Buttons
        JButton dijkstraButton = createColorfulButton("Dijkstra's Algorithm");
        JButton saButton = createColorfulButton("Simulated Annealing");
        JButton acButton = createColorfulButton("Ant Colony Optimization");

        menuPanel.add(dijkstraButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        menuPanel.add(saButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        menuPanel.add(acButton);

        menuPanel.add(Box.createVerticalGlue());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(menuPanel);

        contentPanel.add(centerPanel, "Menu");

        // Loading Panel
        JPanel loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.setOpaque(false);
        JPanel loadingInner = new JPanel();
        loadingInner.setLayout(new BoxLayout(loadingInner, BoxLayout.Y_AXIS));
        loadingInner.setOpaque(false);
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel loadingText = new JLabel("Loading... Please wait");
        loadingText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loadingText.setForeground(Color.WHITE);
        loadingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingInner.add(bar);
        loadingInner.add(Box.createRigidArea(new Dimension(0, 10)));
        loadingInner.add(loadingText);
        loadingPanel.add(loadingInner);
        contentPanel.add(loadingPanel, "Loading");

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Actions
        dijkstraButton.addActionListener(e -> loadPanel("Dijkstra"));
        saButton.addActionListener(e -> loadPanel("SimulatedAnnealing"));
        acButton.addActionListener(e -> loadPanel("AntColony"));

        setVisible(true);
        cardLayout.show(contentPanel, "Menu");
    }

    private JButton createColorfulButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color gradientStart = new Color(0, 200, 180);
                Color gradientEnd = new Color(0, 150, 220);
                if (getModel().isRollover()) {
                    gradientStart = new Color(255, 102, 0);
                    gradientEnd = new Color(255, 149, 0);
                }

                GradientPaint paint = new GradientPaint(0, 0, gradientStart, getWidth(), getHeight(), gradientEnd);
                g2.setPaint(paint);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(320, 60));
        button.setMaximumSize(new Dimension(320, 60));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadPanel(String algorithm) {
        cardLayout.show(contentPanel, "Loading");

        Timer timer = new Timer(1000, e -> {
            contentPanel.removeAll();

            JPanel panelToShow;
            switch (algorithm) {
                case "Dijkstra":
                    panelToShow = new Dijkstra().getPanel();
                    break;
                case "SimulatedAnnealing":
                    panelToShow = new SimulatedANL().getPanel();
                    break;
                case "AntColony":
                    panelToShow = new AntColony().getPanel();
                    break;
                default:
                    panelToShow = new JPanel();
                    panelToShow.add(new JLabel("Unknown Algorithm"));
            }

            panelToShow.setOpaque(false);
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(panelToShow, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
