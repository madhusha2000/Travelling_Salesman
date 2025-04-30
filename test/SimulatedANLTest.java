import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatedANLTest {
    private SimulatedANL simulatedANL;

    @BeforeEach
    public void setup() {
        simulatedANL = new SimulatedANL();
    }

    @Test
    public void testCalculateTotalDistance() {
        List<List<Double>> matrix = Arrays.asList(
            Arrays.asList(0.0, 60.0, 70.0),
            Arrays.asList(60.0, 0.0, 80.0),
            Arrays.asList(70.0, 80.0, 0.0)
        );

        List<Integer> route = Arrays.asList(0, 1, 2, 0);  // A -> B -> C -> A
        simulatedANL.distanceMatrix = matrix;

        double total = simulatedANL.calculateTotalDistance(route);
        assertEquals(60 + 80 + 70, total);
    }

    @Test
    public void testAcceptanceProbabilityBetterDistance() {
        double result = simulatedANL.acceptanceProbability(200, 180, 1000);
        assertEquals(1.0, result);
    }

    @Test
    public void testAcceptanceProbabilityWorseDistance() {
        double result = simulatedANL.acceptanceProbability(180, 200, 1000);
        assertTrue(result < 1.0 && result > 0.0);
    }

    @Test
    public void testSimulatedAnnealingRouteStartsAndEndsAtHome() {
        simulatedANL.selectedCities = Arrays.asList(1, 2, 3, 4);
        simulatedANL.homeCity = 0;

        // Mock a distance matrix (symmetric random-like values)
        List<List<Double>> matrix = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(i == j ? 0.0 : 50 + rand.nextInt(51));
            }
            matrix.add(row);
        }
        simulatedANL.distanceMatrix = matrix;

        List<Integer> route = simulatedANL.simulatedAnnealingTSP();

        assertEquals(simulatedANL.homeCity, route.get(0));
        assertEquals(simulatedANL.homeCity, route.get(route.size() - 1));
        assertTrue(route.containsAll(simulatedANL.selectedCities));
    }
}
