package graph.hamiltonian_path;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TSPTest {

	private static final double EPS = 1e-5;

	@Test
	public void generalTest_checkConstructor_shouldFillClassFieldsCorrectly() {
		double[][] graph = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		TSP tsp = new TSP(graph);

		// check start index
		assertEquals(0, tsp.getStartIndex());

		// check length
		assertEquals(graph.length, tsp.getN());

		// check dp height
		assertEquals(graph.length, tsp.getDp().length);

		// check dp width
		assertEquals(1 << graph.length, tsp.getDp()[0].length);
	}

	@Test
	public void generalTest_checkConstructorWithStartIndex_shouldFillClassFieldsCorrectly() {
		double[][] graph = {{1, 2, 3, 3}, {4, 5, 6, 3}, {7, 8, 9, 3}, {7, 8, 9, 3}};
		int startIndex = 3;
		TSP tsp = new TSP(startIndex, graph);

		// check start index
		assertEquals(startIndex, tsp.getStartIndex());

		// check length
		assertEquals(graph.length, tsp.getN());

		// check dp height
		assertEquals(graph.length, tsp.getDp().length);

		// check dp width
		assertEquals(1 << graph.length, tsp.getDp()[0].length);
	}

//	@Test
//	public void generalTest_fillBaseDistances_shouldFillCorrectStates() {
//		double[][] graph = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
//		TSP tsp = new TSP(graph);
//
//		// startIndex = 0
//		assertThat()
//	}

	@Test
	public void testTspIterativeInvalidStartNode() {
		double[][] graph = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		Assertions.assertThrows(IllegalArgumentException.class, () -> new TSP(321, graph));
	}

	@Test
	public void testTspIterativeNonSquareMatrix() {
		double[][] graph = {{1, 2, 3}, {4, 5, 6}};
		Assertions.assertThrows(IllegalStateException.class, () -> new TSP(graph));
	}

	@Test
	public void testTspIterativeSmallGraph() {
		double[][] graph = {{0, 1}, {1, 0}};
		Assertions.assertThrows(IllegalStateException.class, () -> new TSP(graph));
	}

	@Test
	void getCombinations() {
		double[][] graph = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		TSP tsp = new TSP(graph);
		List<Integer> ans = tsp.getCombinations(2);
		Set<Integer> expected = new HashSet<>();
		generateCombinations(expected, 2, new boolean[graph.length], 0);
		Truth.assertThat(ans).containsExactlyElementsIn(expected);
	}

	private void generateCombinations(Set<Integer> ans, int k, boolean[] seen, int cur) {
		if (k == 0) {
			ans.add(cur);
		} else {
			for (int i = 0; i < seen.length; i++) {
				if (seen[i]) continue;
				seen[i] = true;
				generateCombinations(ans, k - 1, seen, cur | (1 << i));
				seen[i] = false;
			}
		}
	}

	/*
	@Test
	public void testTsp_small1() {
		int n = 5;
		double[][] graph = new double[n][n];
		for (double[] row : graph) java.util.Arrays.fill(row, 100);

		// Assume matrix is symmetric for simplicity.
		graph[1][3] = graph[3][1] = 1;
		graph[3][0] = graph[0][3] = 2;
		graph[0][2] = graph[2][0] = 3;
		graph[2][4] = graph[4][2] = 4;
		graph[4][1] = graph[1][4] = 5;

		double expected = 1 + 2 + 3 + 4 + 5;
		double tspIterativeTourCost = new TSP(graph).getTourCost();

		assertThat(tspIterativeTourCost).isWithin(EPS).of(expected);
	}

	@Test
	public void testDpVsBf() {
		for (int n = 3; n <= 9; n++) {
			for (int i = 0; i < 10; i++) {

				double[][] graph = new double[n][n];
				randomFillDistMatrix(graph);

				TSP dpIterativeSolver = new TSP(graph);

				double dp2 = dpIterativeSolver.getTourCost();
				double bf = TspBruteForce.computeTourCost(TspBruteForce.tsp(graph), graph);

				assertThat(dp2).isWithin(EPS).of(bf);
			}
		}
	}

	@Test
	public void testGeneratedTour() {
		for (int n = 3; n <= 9; n++) {
			for (int i = 0; i < 10; i++) {

				double[][] graph = new double[n][n];
				randomFillDistMatrix(graph);

				TSP dpIterativeSolver = new TSP(graph);
				int[] bfPath = TspBruteForce.tsp(graph);

				double dp2 = dpIterativeSolver.getTourCost();
				double bf = TspBruteForce.computeTourCost(bfPath, graph);

				assertThat(dp2).isWithin(EPS).of(bf);

				assertThat(getTourCost(graph, dpIterativeSolver.getTour())).isWithin(EPS).of(bf);
			}
		}
	}

	// Try slightly larger matrices to make sure they run is a reasonable amount of time.
	@Test
	public void testTspIterativePerformance() {
		for (int n = 3; n <= 16; n++) {
			double[][] graph = new double[n][n];
			randomFillDistMatrix(graph);
			TSP solver = new TSP(graph);
			solver.solve();
		}
	}

	public void randomFillDistMatrix(double[][] graph) {
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				if (i == j) continue;

				// Add a random edge value (possibly negative)
				double val = (int) (Math.random() * 1000);
				if (Math.random() < 0.8) graph[i][j] = val;
				else graph[i][j] = -val;
			}
		}
	}

	private double getTourCost(double[][] graph, List<Integer> tour) {
		double total = 0;
		for (int i = 1; i < tour.size(); i++) total += graph[tour.get(i - 1)][tour.get(i)];
		return total;
	}
	*/
}