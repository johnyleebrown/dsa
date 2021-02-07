package graph.hamiltonian_path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.google.common.truth.Truth.assertThat;
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

	@Test
	public void generalTest_fillBaseDistances_shouldFillCorrectStates() {
		double[][] graph = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		TSP tsp = new TSP(graph);
		tsp.fillBaseDistances();

		int n = graph.length;
		BitSet bitSet = new BitSet();

		bitSet.set(tsp.getStartIndex());
		Double[][] dp = new Double[n][1 << n];

		for (int i = 0; i < n; i++) {
			if (i == tsp.getStartIndex()) continue;
			bitSet.set(i);
			int subset = getBitMaskFromBitSet(bitSet);
			dp[i][subset] = graph[tsp.getStartIndex()][i];
			bitSet.clear(i);
		}

		for (int i = 0; i < dp.length; i++) {
			System.out.println();
			for (int j = 0; j < dp[0].length; j++) {
				assertEquals(dp[i][j], tsp.getDp()[i][j]);
			}
		}
	}

	private int getBitMaskFromBitSet(BitSet bitSet) {
		int ans = 0;
		for (int i : bitSet.stream().toArray()) {
			ans |= (1 << i);
		}
		return ans;
	}

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
		assertThat(ans).containsExactlyElementsIn(expected);
		List<Integer> expected2 = generateCombinations2(2, graph.length);
		assertThat(expected).containsExactlyElementsIn(expected2);
		assertThat(expected2).containsExactlyElementsIn(ans);
	}

	public static List<Integer> generateCombinations2(int r, int n) {
		List<Integer> subsets = new ArrayList<>();
		combinations(0, 0, r, n, subsets);
		return subsets;
	}

	// To find all the combinations of size r we need to recurse until we have
	// selected r elements (aka r = 0), otherwise if r != 0 then we still need to select
	// an element which is found after the position of our last selected element
	private static void combinations(int set, int at, int r, int n, List<Integer> subsets) {

		// Return early if there are more elements left to select than what is available.
		int elementsLeftToPick = n - at;
		if (elementsLeftToPick < r) return;

		// We selected 'r' elements so we found a valid subset!
		if (r == 0) {
			subsets.add(set);
		} else {
			for (int i = at; i < n; i++) {
				// Try including this element
				set ^= (1 << i);

				combinations(set, i + 1, r - 1, n, subsets);

				// Backtrack and try the instance where we did not include this element
				set ^= (1 << i);
			}
		}
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
		TSP tsp = new TSP(graph).solve();

		double tsp_minDist = tsp.getMinTotalDistance();
		assertThat(tsp_minDist).isWithin(EPS).of(expected);
	}

	@Test
	public void testDpVsBf() {
		for (int n = 3; n <= 9; n++) {
			for (int i = 0; i < 10; i++) {

				double[][] graph = new double[n][n];
				randomFillDistMatrix(graph);

				TSP tsp = new TSP(graph).solve();
				TSP_BruteForce tsp_bruteForce = new TSP_BruteForce(graph);

				double tsp_minDist = tsp.getMinTotalDistance();
				double tsp_bruteForce_minDist = tsp_bruteForce.getBestTourCost();

				assertThat(tsp_minDist).isWithin(EPS).of(tsp_bruteForce_minDist);
			}
		}
	}

	@Test
	public void testGeneratedTour() {
		for (int n = 3; n <= 9; n++) {
			for (int i = 0; i < 10; i++) {

				double[][] graph = new double[n][n];
				randomFillDistMatrix(graph);

				TSP tsp = new TSP(graph).solve();
				TSP_BruteForce tsp_bf = new TSP_BruteForce(graph);

				double tsp_minDist = tsp.getMinTotalDistance();
				double tsp_bf_minDist = tsp_bf.getBestTourCost();

				List<Integer> x = new ArrayList<>();
				Arrays.stream(tsp_bf.getBestTour()).forEach(x::add);

				assertThat(tsp.getTour()).containsExactlyElementsIn(x);
				assertThat(tsp_minDist).isWithin(EPS).of(tsp_bf_minDist);
//				assertThat(getTourCost(graph, tsp.getTour())).isWithin(EPS).of(tsp_bf_minDist);
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
}