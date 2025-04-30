import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DijkstraTest {

    private Dijkstra dijkstra;

    @BeforeEach
    public void setup() {
        dijkstra = new Dijkstra();
    }

    @Test
    public void testCalculateTotalDistance() {
        // Manually set distanceMatrix
        List<List<Double>> distanceMatrix = new ArrayList<>();
        distanceMatrix.add(Arrays.asList(0.0, 10.0, 20.0));
        distanceMatrix.add(Arrays.asList(10.0, 0.0, 15.0));
        distanceMatrix.add(Arrays.asList(20.0, 15.0, 0.0));
        dijkstra.distanceMatrix = distanceMatrix;

        List<Integer> route = Arrays.asList(0, 1, 2, 0);  // A -> B -> C -> A
        double expected = 10 + 15 + 20;

        double actual = dijkstra.calculateTotalDistance(route);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testFindClosestCityDijkstra() {
        dijkstra.generateCities(3);
        dijkstra.distanceMatrix = new ArrayList<>();
        dijkstra.distanceMatrix.add(Arrays.asList(0.0, 5.0, 8.0));  // from city 0
        dijkstra.distanceMatrix.add(Arrays.asList(5.0, 0.0, 2.0));  // from city 1
        dijkstra.distanceMatrix.add(Arrays.asList(8.0, 2.0, 0.0));  // from city 2

        List<Integer> toVisit = Arrays.asList(1, 2);
        int closest = dijkstra.findClosestCityDijkstra(0, toVisit);  // from 0, 1 is closer (5.0)

        assertEquals(1, closest);
    }

    @Test
    public void testGenerateDistanceMatrix() {
        dijkstra.generateCities(5);
        dijkstra.generateDistanceMatrix();

        assertEquals(5, dijkstra.distanceMatrix.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(5, dijkstra.distanceMatrix.get(i).size());
            assertEquals(0.0, dijkstra.distanceMatrix.get(i).get(i));
        }
    }

    @Test
    public void testCityGenerationWithinBounds() {
        dijkstra.generateCities(10);
        for (Point city : dijkstra.cities) {
            assertTrue(city.x >= 50 && city.x <= 450);
            assertTrue(city.y >= 50 && city.y <= 450);
        }
    }
}
