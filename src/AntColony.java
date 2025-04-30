import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class AntColony extends JFrame {
    private CityPanel cityPanel;
    private JButton solveButton;
    private JLabel routeLabel, distanceLabel, distanceDetailsLabel;
    private List<Point> cities;
    private List<Integer> optimalRoute;
    private List<List<Double>> distanceMatrix;
    private int homeCity;
    private List<Integer> selectedCities;
    private String playerName;
    private JPanel panel;

    public AntColony() {
        setTitle("Traveling Salesman Game - Ant Colony Optimization");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.add(new JLabel("Ant Colony Optimization UI here"));
        // build your Ant Colony UI inside 'panel'
    

        generateCities(10); // A to J
        generateDistanceMatrix();
        homeCity = new Random().nextInt(cities.size());

        cityPanel = new CityPanel();
        add(cityPanel, BorderLayout.CENTER);

         JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, getHeight())); // wider panel
        rightPanel.setBackground(new Color(201, 87, 146));

        solveButton = new JButton("Solve TSP");
        routeLabel = new JLabel("Optimal Route: []");
        distanceLabel = new JLabel("Total Distance: 0");
        distanceDetailsLabel = new JLabel("<html>Distance Details:<br></html>");
        distanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
       
        
        distanceDetailsLabel.setOpaque(true); 
        distanceDetailsLabel.setBackground(Color.BLACK);
        distanceLabel.setForeground(new Color(201, 87, 146));
        distanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        distanceDetailsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        distanceDetailsLabel.setVerticalAlignment(SwingConstants.TOP);
        distanceDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        distanceDetailsLabel.setForeground(new Color(201, 87, 146));
        solveButton.addActionListener(e -> solveTSP());

        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding
        rightPanel.add(solveButton);
        rightPanel.add(Box.createVerticalStrut(30)); // Larger space after button

        rightPanel.add(routeLabel);
        rightPanel.add(Box.createVerticalStrut(15));

        rightPanel.add(distanceLabel);
        distanceLabel.setForeground(new Color(0, 0, 139));
        distanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(Box.createVerticalStrut(15));

        rightPanel.add(new JScrollPane(distanceDetailsLabel));
        rightPanel.add(Box.createVerticalStrut(30)); // More space before timer

      

        add(rightPanel, BorderLayout.EAST);

        selectedCities = new ArrayList<>();
        playerName = promptForPlayerName();

        setVisible(true);
    }
public JPanel getPanel() {
        return panel;
    }
    private String promptForPlayerName() {
        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        while (name == null || name.trim().isEmpty()) {
            if (name == null) {
                int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?",
                        "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
            JOptionPane.showMessageDialog(this, "Name cannot be empty. Please enter your name.");
            name = JOptionPane.showInputDialog(this, "Enter your name:");
        }
        return name;
    }

    private void generateCities(int n) {
        cities = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int x = rand.nextInt(400) + 50;
            int y = rand.nextInt(400) + 50;
            cities.add(new Point(x, y));
        }
    }

    private void generateDistanceMatrix() {
        distanceMatrix = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < cities.size(); i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < cities.size(); j++) {
                if (i == j) {
                    row.add(0.0);
                } else {
                    double distance = 50 + rand.nextInt(51);
                    row.add(distance);
                }
            }
            distanceMatrix.add(row);
        }
    }

    private void solveTSP() {
        if (selectedCities.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select cities to visit by clicking on them.");
            return;
        }

        long startTime = System.currentTimeMillis();
        optimalRoute = antColonyOptimizationTSP();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        cityPanel.repaint();

        List<String> routeNames = optimalRoute.stream()
                .map(i -> String.valueOf((char) ('A' + i)))
                .collect(Collectors.toList());

        routeLabel.setText("Optimal Route: " + routeNames.toString());
        double totalDistance = calculateTotalDistance(optimalRoute);
        distanceLabel.setText("Total Distance: " + totalDistance + " km");

        StringBuilder distanceDetails = new StringBuilder("<html>Distance Details:<br>");
        for (int i = 0; i < optimalRoute.size() - 1; i++) {
            int fromCity = optimalRoute.get(i);
            int toCity = optimalRoute.get(i + 1);
            double dist = distanceMatrix.get(fromCity).get(toCity);
            distanceDetails.append(String.format("%s -> %s: %.2f km<br>",
                    (char) ('A' + fromCity), (char) ('A' + toCity), dist));
        }
        distanceDetails.append("</html>");
        distanceDetailsLabel.setText(distanceDetails.toString());

        // Save to database
        DatabaseManager.saveGameResults(
                playerName,
                String.valueOf((char) ('A' + homeCity)),
                selectedCities.stream()
                        .map(i -> String.valueOf((char) ('A' + i)))
                        .collect(Collectors.toList())
                        .toString(),
                routeNames.toString(),
                totalDistance,
                duration / 1000.0,
                "Ant Colony Optimization"
        );
        JOptionPane.showMessageDialog(this, "You won! TSP solved successfully.");
    }

    private List<Integer> antColonyOptimizationTSP() {
        int numAnts = 30;
        int iterations = 100;
        double alpha = 1.0;
        double beta = 5.0;
        double evaporation = 0.5;
        double Q = 500;

        int n = cities.size();
        double[][] pheromones = new double[n][n];
        double[][] heuristic = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pheromones[i][j] = 1.0;
                if (i != j) {
                    heuristic[i][j] = 1.0 / distanceMatrix.get(i).get(j);
                }
            }
        }

        List<Integer> bestRoute = null;
        double bestDistance = Double.MAX_VALUE;

        for (int iter = 0; iter < iterations; iter++) {
            List<List<Integer>> allRoutes = new ArrayList<>();
            List<Double> allDistances = new ArrayList<>();

            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> route = new ArrayList<>();
                Set<Integer> toVisit = new HashSet<>(selectedCities);
                toVisit.remove(homeCity);
                int current = homeCity;
                route.add(current);

                while (!toVisit.isEmpty()) {
                    double sum = 0;
                    Map<Integer, Double> probabilities = new HashMap<>();
                    for (int next : toVisit) {
                        double p = Math.pow(pheromones[current][next], alpha) *
                                   Math.pow(heuristic[current][next], beta);
                        probabilities.put(next, p);
                        sum += p;
                    }

                    double r = Math.random() * sum;
                    double cumulative = 0;
                    int nextCity = -1;
                    for (Map.Entry<Integer, Double> entry : probabilities.entrySet()) {
                        cumulative += entry.getValue();
                        if (r <= cumulative) {
                            nextCity = entry.getKey();
                            break;
                        }
                    }

                    if (nextCity == -1) {
                        nextCity = toVisit.iterator().next();
                    }

                    route.add(nextCity);
                    toVisit.remove(nextCity);
                    current = nextCity;
                }

                route.add(homeCity); // Return home

                double distance = calculateTotalDistance(route);
                allRoutes.add(route);
                allDistances.add(distance);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestRoute = new ArrayList<>(route);
                }
            }

            // Evaporate pheromones
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    pheromones[i][j] *= (1 - evaporation);
                }
            }

            // Deposit pheromones
            for (int k = 0; k < numAnts; k++) {
                List<Integer> route = allRoutes.get(k);
                double dist = allDistances.get(k);
                double deposit = Q / dist;
                for (int i = 0; i < route.size() - 1; i++) {
                    int from = route.get(i);
                    int to = route.get(i + 1);
                    pheromones[from][to] += deposit;
                    pheromones[to][from] += deposit;
                }
            }
        }
        return bestRoute;
    }

    private double calculateTotalDistance(List<Integer> route) {
        double sum = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            sum += distanceMatrix.get(route.get(i)).get(route.get(i + 1));
        }
        return Math.round(sum);
    }

    class CityPanel extends JPanel {
        public CityPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int clickedCity = getClickedCity(e.getPoint());
                    if (clickedCity != -1) {
                        if (selectedCities.contains(clickedCity)) {
                            selectedCities.remove(Integer.valueOf(clickedCity));
                        } else {
                            selectedCities.add(clickedCity);
                        }
                        repaint();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(255, 245, 230));

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));

            for (int i = 0; i < cities.size(); i++) {
                Point city = cities.get(i);
                if (i == homeCity) {
                    g2.setColor(Color.BLUE);
                } else if (selectedCities.contains(i)) {
                    g2.setColor(Color.GREEN);
                } else {
                    g2.setColor(Color.RED);
                }
                g2.fillOval(city.x - 8, city.y - 8, 16, 16);
                g2.setColor(Color.BLACK);
                g2.drawString(String.valueOf((char) ('A' + i)), city.x + 10, city.y);
            }

            if (optimalRoute != null) {
                g2.setColor(Color.MAGENTA);
                for (int i = 0; i < optimalRoute.size() - 1; i++) {
                    Point from = cities.get(optimalRoute.get(i));
                    Point to = cities.get(optimalRoute.get(i + 1));
                    g2.drawLine(from.x, from.y, to.x, to.y);
                }
            }
        }

        private int getClickedCity(Point clickPoint) {
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).distance(clickPoint) < 10) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AntColony::new);
    }
}
