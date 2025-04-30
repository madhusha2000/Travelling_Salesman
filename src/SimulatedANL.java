import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.sql.*;
import java.util.stream.Collectors;

public class SimulatedANL extends JFrame {
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

    public SimulatedANL() {
        setTitle("Traveling Salesman Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.add(new JLabel("Simulated Annealing UI here"));
        
        // Create Cities and Distance Matrix
        generateCities(10); // A to J
        generateDistanceMatrix();

        // Choose a random home city
        homeCity = new Random().nextInt(cities.size());

        // Panel to draw cities
        cityPanel = new CityPanel();
        add(cityPanel, BorderLayout.CENTER);

        // Right Panel
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

        selectedCities = new ArrayList<>();  // User-selected cities

        playerName = JOptionPane.showInputDialog("Enter your name:");
        while (playerName == null || playerName.trim().isEmpty()) {
            if (playerName == null) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to exit?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    playerName = JOptionPane.showInputDialog("Enter your name:");
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Name cannot be empty. Please enter your name.");
                playerName = JOptionPane.showInputDialog("Enter your name:");
            }
        }

        setVisible(true);
    }
public JPanel getPanel() {
        return panel;
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
                    double distance = 50 + rand.nextInt(51); // Distance between 50 and 100 km
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
    optimalRoute = simulatedAnnealingTSP(); // Using Simulated Annealing
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;

    cityPanel.repaint();

    // Convert optimal route to city names (A to J)
    List<String> routeNames = new ArrayList<>();
    for (int index : optimalRoute) {
        routeNames.add(String.valueOf((char) ('A' + index)));
    }

    // Update Route and Distance
    routeLabel.setText("Optimal Route: " + routeNames.toString());
    double totalDistance = calculateTotalDistance(optimalRoute);
    distanceLabel.setText("Total Distance: " + totalDistance);

    // Display the distance details
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

    // Save the game result
    String selectedCitiesStr = selectedCities.stream()
            .map(i -> String.valueOf((char) ('A' + i)))
            .collect(Collectors.toList())
            .toString();

    String shortestRouteStr = routeNames.toString();
    double totalTime = duration / 1000.0; // milliseconds -> seconds
    String algorithmUsed = "Simulated Annealing";

    DatabaseManager.saveGameResults(
            playerName,
            String.valueOf((char) ('A' + homeCity)),
            selectedCitiesStr,
            shortestRouteStr,
            totalDistance,
            totalTime,
            algorithmUsed
    );

    // Show the message after the player has solved the TSP
    JOptionPane.showMessageDialog(this, "Congratulations! You have won the game. The optimal route has been found.");
}


    private List<Integer> simulatedAnnealingTSP() {
        List<Integer> currentRoute = new ArrayList<>(selectedCities);
        if (!currentRoute.contains(homeCity)) {
            currentRoute.add(0, homeCity);
        }
        currentRoute.add(homeCity); // Ensure route returns to home city

        double currentDistance = calculateTotalDistance(currentRoute);
        List<Integer> bestRoute = new ArrayList<>(currentRoute);
        double bestDistance = currentDistance;

        double temperature = 10000;
        double coolingRate = 0.003;

        Random rand = new Random();

        while (temperature > 1) {
            List<Integer> newRoute = new ArrayList<>(currentRoute);

            // Swap two cities (not including start and end home city)
            int swapIndex1 = 1 + rand.nextInt(newRoute.size() - 2);
            int swapIndex2 = 1 + rand.nextInt(newRoute.size() - 2);
            while (swapIndex1 == swapIndex2) {
                swapIndex2 = 1 + rand.nextInt(newRoute.size() - 2);
            }

            int temp = newRoute.get(swapIndex1);
            newRoute.set(swapIndex1, newRoute.get(swapIndex2));
            newRoute.set(swapIndex2, temp);

            double newDistance = calculateTotalDistance(newRoute);

            if (acceptanceProbability(currentDistance, newDistance, temperature) > rand.nextDouble()) {
                currentRoute = new ArrayList<>(newRoute);
                currentDistance = newDistance;
            }

            if (currentDistance < bestDistance) {
                bestRoute = new ArrayList<>(currentRoute);
                bestDistance = currentDistance;
            }

            temperature *= 1 - coolingRate;
        }

        return bestRoute;
    }

    private double acceptanceProbability(double currentDistance, double newDistance, double temperature) {
        if (newDistance < currentDistance) {
            return 1.0;
        }
        return Math.exp((currentDistance - newDistance) / temperature);
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
                            selectedCities.remove(Integer.valueOf(clickedCity));  // Deselect city
                        } else {
                            selectedCities.add(clickedCity);  // Select city
                        }
                        repaint();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(255, 245, 230)); // light background

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));

            for (int i = 0; i < cities.size(); i++) {
                Point city = cities.get(i);

                if (i == homeCity) {
                    g2.setColor(Color.BLUE); // Home city
                } else if (selectedCities.contains(i)) {
                    g2.setColor(Color.GREEN);  // Selected cities
                } else {
                    g2.setColor(Color.RED);  // Regular cities
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
                Point city = cities.get(i);
                if (city.distance(clickPoint) < 10) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimulatedANL::new); // <-- CORRECTED
    }
}

class DatabaseManager {
    public static void saveGameResults(String playerName, String homeCity, String selectedCities,
                                       String shortestRoute, double totalDistance, double totalTime,
                                       String algorithmUsed) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tsp_game",
                "root", "")) {

            String query = "INSERT INTO game_results (player_name, home_city, selected_cities, shortest_route, " +
                    "total_distance, total_time, algorithm_used) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                statement.setString(2, homeCity);
                statement.setString(3, selectedCities);
                statement.setString(4, shortestRoute);
                statement.setDouble(5, totalDistance);
                statement.setDouble(6, totalTime);
                statement.setString(7, algorithmUsed);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
