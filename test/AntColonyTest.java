/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */



/**
 *
 * @author USER
 */
import org.junit.jupiter.api.*;
import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AntColonyTest {

    private AntColony antColony;

    @BeforeEach
    public void setUp() {
        antColony = new AntColony();
    }

    @Test
    public void testGenerateDistanceMatrixSize() {
        // Matrix should be n x n
        int size = 10;
        antColony.generateCities(size);
        antColony.generateDistanceMatrix();

        assertEquals(size, antColony.distanceMatrix.size(), "Row count should match city count");
        for (List<Double> row : antColony.distanceMatrix) {
            assertEquals(size, row.size(), "Column count should match city count");
        }
    }

    @Test
    public void testCalculateTotalDistanceCorrectness() {
        List<Integer> route = Arrays.asList(0, 1, 2, 0);
        antColony.generateCities(3);

        // Manually define matrix
        List<List<Double>> matrix = Arrays.asList(
                Arrays.asList(0.0, 50.0, 60.0),
                Arrays.asList(50.0, 0.0, 70.0),
                Arrays.asList(60.0, 70.0, 0.0)
        );
        antColony.distanceMatrix = matrix;

        double total = antColony.calculateTotalDistance(route);
        assertEquals(180.0, total, "Distance should be 50+70+60 = 180");
    }

    @Test
    public void testAntColonyReturnsValidRoute() {
        antColony.generateCities(5);
        antColony.generateDistanceMatrix();
        antColony.selectedCities = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        antColony.homeCity = 0;

        List<Integer> route = antColony.antColonyOptimizationTSP();

        assertNotNull(route, "Route should not be null");
        assertEquals(antColony.selectedCities.size() + 2, route.size(), "Route should start and end at home");
        assertEquals(route.get(0), antColony.homeCity, "Route should start at home");
        assertEquals(route.get(route.size() - 1), antColony.homeCity, "Route should end at home");

        // Ensure all selected cities are visited
        Set<Integer> uniqueVisited = new HashSet<>(route);
        assertTrue(uniqueVisited.containsAll(antColony.selectedCities), "All selected cities must be in the route");
    }
}
