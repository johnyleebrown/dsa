package graph.hamiltonian_path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Travelling Salesman Problem
 *
 * Given a list of cities and the distances between each pair of cities, what is the shortest
 * possible route that visits each city exactly once and returns to the origin city?
 *
 * Bellman–Held–Karp algorithm
 * Time Complexity: O(n^2 * 2^n)
 * Space Complexity: O(n * 2^n)
 */
public class TSP {

	// if the program has already computed
	private boolean solved = false;
	// graph size
	private final int n;
	// start node for the tour
	private final int startIndex;
	// the number that represents all selected indexes
	private final int endSubset;
	// total distance of the min tour
	private double minTotalDistance = Double.POSITIVE_INFINITY;
	// memo table of size <N*(2^N)> <last_index, path_subset>
	// n because any index could be last
	// 2^N because subset of chosen vertices could get up to 2^N, N=2 max_subset=11 2^N=4=100
	// (binary)
	private Double[][] dp;
	// input graph NxN
	private final double[][] graph;
	// shortest hamiltonian cycle - min dist tour
	private final List<Integer> tour = new ArrayList<>();

	public TSP(double[][] graph) {
		this.n = graph.length;
		this.startIndex = 0;
		this.graph = graph;
		this.dp = new Double[n][1 << n];
		this.endSubset = (1 << n) - 1;

		checkInputParams();
	}

	public TSP(int startIndex, double[][] graph) {
		this.n = graph.length;
		this.startIndex = startIndex;
		this.graph = graph;
		this.dp = new Double[n][1 << n];
		this.endSubset = (1 << n) - 1;

		checkInputParams();
	}

	private void checkInputParams() {
		if (n <= 2)
			throw new IllegalStateException("N <= 2 not yet supported.");
		if (n != graph[0].length)
			throw new IllegalStateException("Matrix must be square (n x n)");
		if (startIndex < 0 || startIndex >= n)
			throw new IllegalArgumentException("Invalid start node.");
		if (n > 32)
			throw new IllegalArgumentException(
			"Matrix too large! A matrix that size for the DP TSP problem with a time complexity of"
			+ "O(n^2*2^n) requires way too much computation for any modern home computer to handle");
	}

	public TSP solve() {
		if (solved) {
			return this;
		}

		fillBaseDistances();

		calculateMinDistances();

		calculateMinTotalDistance();

		calculateTour();

		solved = true;
		return this;
	}

	/**
	 * base
	 * fill the distances from start to all other nodes
	 */
	public void fillBaseDistances() {
		for (int end = 0; end < graph.length; end++) {
			if (end == startIndex) {
				continue;
			}

			// state
			// last chosen node = current node
			// subset = start node & current node
			dp[end][(1 << startIndex) | (1 << end)] = graph[startIndex][end];
		}
	}

	/**
	 * fill the dp table
	 * find min distances for each {last_node, chosen_nodes_subset}
	 * for each size of subset, from 3, bc we have for 2 already
	 */
	private void calculateMinDistances() {
		for (int subsetSize = 3; subsetSize <= n; subsetSize++) {

			List<Integer> combinations = getCombinations(subsetSize);
			for (int subset : combinations) {
				if (notIn(startIndex, subset)) {
					continue;
				}

				for (int next = 0; next < n; next++) {
					if (notIn(next, subset) || next == startIndex) {
						continue;
					}

					int subsetWithoutNext = subset ^ (1 << next);
					double minDistance = Double.POSITIVE_INFINITY;


					for (int last = 0; last < n; last++) {
						if (last == startIndex || last == next || notIn(last, subset)) {
							continue;
						}

						double newDistance = dp[last][subsetWithoutNext] + graph[last][next];
						minDistance = Math.min(newDistance, minDistance);
					}

					dp[next][subset] = minDistance;
				}
			}
		}
	}

	/**
	 * connect tour back to starting node and find min cost
	 * loop through all the end states and get find min
	 * for final total distance we need to connect last node to first node
	 */
	private void calculateMinTotalDistance() {
		for (int last = 0; last < n; last++) {
			if (last == startIndex) {
				continue;
			}

			double minDistance = dp[last][endSubset] + graph[last][startIndex];
			minTotalDistance = Math.min(minDistance, minTotalDistance);
		}
	}

	/**
	 * reconstruct tsp path from dp table
	 * start from the start node and go backwards
	 * in the end we will reverse the tour
	 */
	private void calculateTour() {

		System.out.println("");

		int next = startIndex;
		int curSubset = endSubset;

		tour.add(startIndex);

		for (int count = 1; count < n; count++) {

			int bestIndex = -1;
			double bestDistance = Double.POSITIVE_INFINITY;

			for (int last = 0; last < n; last++) {
				if (last == startIndex || notIn(last, curSubset)) {
					continue;
				}

				double dist = dp[last][curSubset] + graph[last][next];
				if (dist < bestDistance) {
					bestIndex = last;
					bestDistance = dist;
				}
			}

			next = bestIndex;
			curSubset = curSubset ^ (1 << next);
			tour.add(bestIndex);
		}

		Collections.reverse(tour);
	}

	/**************************************************************************/

	private boolean notIn(int startIndex, int combination) {
		return ((1 << startIndex) & combination) == 0;
	}

	/**
	 * Get permutations of subsetSize selected nodes from n
	 * n=3, subsetSize = 2: 011, 101, 110
	 */
	public List<Integer> getCombinations(int subsetSize) {
		List<Integer> ans = new ArrayList<>();
		backtrack(ans, subsetSize, 0, 0);
		return ans;
	}

	/**
	 * permutations = choose k out of n
	 */
	private void backtrack(List<Integer> ans, int k, int cur, int start) {
		if (k == 0) {
			ans.add(cur);
		} else {
			for (int i = start; i < n; i++) {
				backtrack(ans, k - 1, cur | (1 << i), i + 1);
			}
		}
	}

	public int getN() {
		return n;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public Double[][] getDp() {
		return dp;
	}

	public double getMinTotalDistance() {
		return minTotalDistance;
	}

	public List<Integer> getTour() {
		return tour;
	}

	public void printDpTable() {
		for (Double[] doubles : dp) {
			System.out.println();
			for (int j = 0; j < dp[0].length; j++) {
				System.out.print((doubles[j] == null ? "n" : doubles[j]) + " ");
			}
		}
	}
}
