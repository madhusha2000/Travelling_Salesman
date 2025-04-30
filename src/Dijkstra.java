import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.sql.*;
import java.util.stream.Collectors;

public class Dijkstra extends JFrame {
    private CityPanel cityPanel;
    private JButton solveButton;
    private JLabel routeLabel, distanceLabel, distanceDetailsLabel, timerLabel;
    private List<Point> cities;
    private List<Integer> optimalRoute;
    private List<List<Double>> distanceMatrix;
    private int homeCity;
    private List<Integer> selectedCities;
    private String playerName;
    private JPanel panel;
    private Timer countdownTimer;
    private int timeLeft = 500; //

    public Dijkstra() {
        setTitle("Traveling Salesman Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        panel = new JPanel();
        panel.add(new JLabel("Dijkstra's Algorithm UI here"));

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
        timerLabel = new JLabel("Time Left: 10:00");
        
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

        rightPanel.add(timerLabel);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));


        add(rightPanel, BorderLayout.EAST);

        selectedCities = new ArrayList<>();

        playerName = getCustomInput("Enter your name:", "Player Name");

        while (playerName == null || playerName.trim().isEmpty()) {
            if (playerName == null) { // User clicked Cancel
                int choice = getCustomConfirm("Are you sure you want to exit and cancel it?", "Confirm Exit");
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    playerName = getCustomInput("Enter your name:", "Player Name");
                }
            } else { // Empty string
                showCustomMessage("Name cannot be empty. Please enter your name.", "Invalid Name");
                playerName = getCustomInput("Enter your name:", "Player Name");
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
                    double distance = 50 + rand.nextInt(51);
                    row.add(distance);
                }
            }
            distanceMatrix.add(row);
        }
    }

    private void solveTSP() {
        if (selectedCities.isEmpty()) {
            showCustomMessage("Please select cities to visit by clicking on them.", "No Cities Selected");
            return;
        }

        long startTime = System.currentTimeMillis();
        optimalRoute = dijkstraTSP();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        cityPanel.repaint();

        List<String> routeNames = new ArrayList<>();
        for (int index : optimalRoute) {
            routeNames.add(String.valueOf((char) ('A' + index)));
        }

        routeLabel.setText("Optimal Route: " + routeNames.toString());
        double totalDistance = calculateTotalDistance(optimalRoute);
        distanceLabel.setText("Total Distance: " + totalDistance + " km");

        StringBuilder distanceDetails = new StringBuilder("<html>Distance Details:<br>");
        for (int i = 0; i < optimalRoute.size() - 1; i++) {
            int fromCity = optimalRoute.get(i);
            int toCity = optimalRoute.get(i + 1);
            double dist = distanceMatrix.get(fromCity).get(toCity);
            distanceDetails.append(String.format("%s -> %s: %.2f km<br>", (char) ('A' + fromCity), (char) ('A' + toCity), dist));
        }
        distanceDetails.append("</html>");
        distanceDetailsLabel.setText(distanceDetails.toString());

        String selectedCitiesStr = selectedCities.stream()
                .map(i -> String.valueOf((char) ('A' + i)))
                .collect(Collectors.toList())
                .toString();

        String shortestRouteStr = routeNames.toString();
        double totalTime = duration / 1000.0;
        String algorithmUsed = "Dijkstra Adapted";

        DatabaseManager.saveGameResults(
                playerName,
                String.valueOf((char) ('A' + homeCity)),
                selectedCitiesStr,
                shortestRouteStr,
                totalDistance,
                totalTime,
                algorithmUsed
        );

        // Stop the countdown timer when the game is solved
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
            showCustomMessage("You won! Time's up, and the shortest path is found!", "Congratulations");
        }
    }

    private List<Integer> dijkstraTSP() {
        List<Integer> route = new ArrayList<>();
        boolean[] visited = new boolean[cities.size()];
        route.add(homeCity);
        visited[homeCity] = true;
        int current = homeCity;

        List<Integer> citiesToVisit = new ArrayList<>(selectedCities);
        citiesToVisit.remove(Integer.valueOf(homeCity));

        while (!citiesToVisit.isEmpty()) {
            int nextCity = findClosestCityDijkstra(current, citiesToVisit);
            route.add(nextCity);
            visited[nextCity] = true;
            current = nextCity;
            citiesToVisit.remove(Integer.valueOf(nextCity));
        }

        route.add(homeCity);
        return route;
    }

    private int findClosestCityDijkstra(int startCity, List<Integer> citiesToVisit) {
        double[] dist = new double[cities.size()];
        boolean[] visited = new boolean[cities.size()];

        for (int i = 0; i < cities.size(); i++) {
            dist[i] = Double.MAX_VALUE;
        }
        dist[startCity] = 0;

        for (int count = 0; count < cities.size() - 1; count++) {
            int u = minDistance(dist, visited);
            if (u == -1) break;
            visited[u] = true;

            for (int v = 0; v < cities.size(); v++) {
                if (!visited[v] && distanceMatrix.get(u).get(v) != 0 &&
                        dist[u] + distanceMatrix.get(u).get(v) < dist[v]) {
                    dist[v] = dist[u] + distanceMatrix.get(u).get(v);
                }
            }
        }

        int closestCity = -1;
        double minDist = Double.MAX_VALUE;
        for (int city : citiesToVisit) {
            if (dist[city] < minDist) {
                minDist = dist[city];
                closestCity = city;
            }
        }

        return closestCity;
    }

    private int minDistance(double[] dist, boolean[] visited) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < dist.length; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }

        return minIndex;
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
                        startTimer();
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
            g2.setStroke(new BasicStroke(3));

            for (int i = 0; i < cities.size(); i++) {
                Point city = cities.get(i);
 
                if (i == homeCity) {
                    g2.setColor(Color.BLUE);
                     
                } else if (selectedCities.contains(i)) {
                    g2.setColor(Color.GREEN);
                } else {
                    g2.setColor(Color.RED);
//                     ImageIcon icon = new ImageIcon("src/Assests/A.png");
//                     Image image = icon.getImage();
//                     
//                     g2.drawImage(image, city.x - 8, city.y - 8, this); 
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

    // -------- Custom Dialog Methods --------
    private String getCustomInput(String message, String title) {
        JLabel label = new JLabel(message);
        label.setForeground(Color.BLUE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return JOptionPane.showInputDialog(this, label, title, JOptionPane.PLAIN_MESSAGE);
    }

    private int getCustomConfirm(String message, String title) {
        JLabel label = new JLabel(message);
        label.setForeground(Color.RED);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return JOptionPane.showConfirmDialog(this, label, title, JOptionPane.YES_NO_OPTION);
    }

    private void showCustomMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void startTimer() {
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    int minutes = timeLeft / 60;
                    int seconds = timeLeft % 60;
                    timerLabel.setText(String.format("Time Left: %02d:%02d", minutes, seconds));
                } else {
                    countdownTimer.stop();
                    showCustomMessage("Time's up!", "Game Over");
                }
            }
        });
        countdownTimer.start();
    }

    public static void main(String[] args) {
        new Dijkstra();
    }
}

class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/tsp_game";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void saveGameResults(String playerName, String homeCity, String selectedCities,
                                       String shortestRoute, double totalDistance, double totalTime,
                                       String algorithmUsed) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO game_results (player_name, home_city, selected_cities, shortest_route, " +
                    "total_distance, total_time, algorithm_used) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, playerName);
                stmt.setString(2, homeCity);
                stmt.setString(3, selectedCities);
                stmt.setString(4, shortestRoute);
                stmt.setDouble(5, totalDistance);
                stmt.setDouble(6, totalTime);
                stmt.setString(7, algorithmUsed);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
