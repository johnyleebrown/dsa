package graph.hamiltonian_path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TSPTest {

	private static final double EPS = 1e-5;

	@Test
	public void generalTest_checkConstructor_shouldFillClassFieldsCorrectly() {
		double[][] dist = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		TSP tsp = new TSP(dist);

		// check start index
		assertEquals(0, tsp.getStartIndex());

		// check length
		assertEquals(dist.length, tsp.getN());

		// check dp height
		assertEquals(dist.length, tsp.getDp().length);

		// check dp width
		assertEquals(1 << dist.length, tsp.getDp()[0].length);
	}

	@Test
	public void generalTest_checkConstructorWithStartIndex_shouldFillClassFieldsCorrectly() {
		double[][] dist = {{1, 2, 3, 3}, {4, 5, 6, 3}, {7, 8, 9, 3}, {7, 8, 9, 3}};
		int startIndex = 3;
		TSP tsp = new TSP(startIndex, dist);

		// check start index
		assertEquals(startIndex, tsp.getStartIndex());

		// check length
		assertEquals(dist.length, tsp.getN());

		// check dp height
		assertEquals(dist.length, tsp.getDp().length);

		// check dp width
		assertEquals(1 << dist.length, tsp.getDp()[0].length);
	}

//	@Test
//	public void generalTest_fillBaseDistances_shouldFillCorrectStates() {
//		double[][] dist = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
//		TSP tsp = new TSP(dist);
//
//		// startIndex = 0
//		assertThat()
//	}

	@Test
	public void testTspIterativeInvalidStartNode() {
		double[][] dist = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		Assertions.assertThrows(IllegalArgumentException.class, () -> new TSP(321, dist));
	}

	@Test
	public void testTspIterativeNonSquareMatrix() {
		double[][] dist = {{1, 2, 3}, {4, 5, 6}};
		Assertions.assertThrows(IllegalStateException.class, () -> new TSP(dist));
	}

	@Test
	public void testTspIterativeSmallGraph() {
		double[][] dist = {{0, 1}, {1, 0}};
		Assertions.assertThrows(IllegalStateException.class, () -> new TSP(dist));
	}

	/*
	@Test
	public void testTsp_small1() {
		int n = 5;
		double[][] dist = new double[n][n];
		for (double[] row : dist) java.util.Arrays.fill(row, 100);

		// Assume matrix is symmetric for simplicity.
		dist[1][3] = dist[3][1] = 1;
		dist[3][0] = dist[0][3] = 2;
		dist[0][2] = dist[2][0] = 3;
		dist[2][4] = dist[4][2] = 4;
		dist[4][1] = dist[1][4] = 5;

		double expected = 1 + 2 + 3 + 4 + 5;
		double tspIterativeTourCost = new TSP(dist).getTourCost();

		assertThat(tspIterativeTourCost).isWithin(EPS).of(expected);
	}

	@Test
	public void testDpVsBf() {
		for (int n = 3; n <= 9; n++) {
			for (int i = 0; i < 10; i++) {

				double[][] dist = new double[n][n];
				randomFillDistMatrix(dist);

				TSP dpIterativeSolver = new TSP(dist);

				double dp2 = dpIterativeSolver.getTourCost();
				double bf = TspBruteForce.computeTourCost(TspBruteForce.tsp(dist), dist);

				assertThat(dp2).isWithin(EPS).of(bf);
			}
		}
	}

	@Test
	public void testGeneratedTour() {
		for (int n = 3; n <= 9; n++) {
			for (int i = 0; i < 10; i++) {

				double[][] dist = new double[n][n];
				randomFillDistMatrix(dist);

				TSP dpIterativeSolver = new TSP(dist);
				int[] bfPath = TspBruteForce.tsp(dist);

				double dp2 = dpIterativeSolver.getTourCost();
				double bf = TspBruteForce.computeTourCost(bfPath, dist);

				assertThat(dp2).isWithin(EPS).of(bf);

				assertThat(getTourCost(dist, dpIterativeSolver.getTour())).isWithin(EPS).of(bf);
			}
		}
	}

	// Try slightly larger matrices to make sure they run is a reasonable amount of time.
	@Test
	public void testTspIterativePerformance() {
		for (int n = 3; n <= 16; n++) {
			double[][] dist = new double[n][n];
			randomFillDistMatrix(dist);
			TSP solver = new TSP(dist);
			solver.solve();
		}
	}

	public void randomFillDistMatrix(double[][] dist) {
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist.length; j++) {
				if (i == j) continue;

				// Add a random edge value (possibly negative)
				double val = (int) (Math.random() * 1000);
				if (Math.random() < 0.8) dist[i][j] = val;
				else dist[i][j] = -val;
			}
		}
	}

	private double getTourCost(double[][] dist, List<Integer> tour) {
		double total = 0;
		for (int i = 1; i < tour.size(); i++) total += dist[tour.get(i - 1)][tour.get(i)];
		return total;
	}
	*/
}