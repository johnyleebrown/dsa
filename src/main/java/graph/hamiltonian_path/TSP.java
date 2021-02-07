package graph.hamiltonian_path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Travelling Salesman Problem
 *
 * Given a list of cities and the distances between each pair of cities, what is the shortest
 * possible route that visits each city exactly once and returns to the origin city?
 */
public class TSP {

	private boolean solved = false;
	// graph size
	private final int n;
	private final int startIndex;
	private final int endSubset;

	private double minTotalDistance;

	// memo table of size <N*(2^N)> <last_index, path_subset>
	// n because any index could be last
	// 2^N because subset of chosen vertices could get up to 2^N, N=2 max_subset=11 2^N=4=100
	// (binary)
	private Double[][] dp;
	private double[][] graph;

	public TSP(double[][] graph) {
		this.n = graph.length;
		this.startIndex = 0;
		this.graph = graph;
		checkInputParams();
		this.dp = new Double[n][1 << n];
		this.endSubset = (1 << n) - 1;
	}

	public TSP(int startIndex, double[][] graph) {
		this.n = graph.length;
		this.startIndex = startIndex;
		this.graph = graph;
		checkInputParams();
		this.dp = new Double[n][1 << n];
		this.endSubset = (1 << n) - 1;
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

	public void solve() {
		if (solved) {
			return;
		}

		fillBaseDistances();

		calculateMinDistances();

		calculateMinTotalDistance();

		calculateTour();

		solved = true;
	}

	/**
	 * reconstruct tsp path from dp table
	 * start from the start node and go backwards
	 * in the end we will reverse the tour
	 */
	private void calculateTour() {

		int next = startIndex;
		int curSubset = endSubset;
		List<Integer> tour = new ArrayList<>();
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

	/**
	 * base
	 * fill the distances from start to all other nodes
	 */
	public void fillBaseDistances() {
		for (int i = 0; i < graph.length; i++) {
			if (i == startIndex) {
				continue;
			}

			// state
			// last chosen node = current node
			// subset = start node & current node
			dp[i][(1 << startIndex) | (1 << i)] = graph[startIndex][i];
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

					int subsetWithoutEnd = subset ^ (1 << next);
					double minDistance = Double.POSITIVE_INFINITY;

					for (int last = 0; last < n; last++) {
						if (last == startIndex || last == next || notIn(last, subset)) {
							continue;
						}
						double newDistance = dp[last][subsetWithoutEnd] + graph[last][next];
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
		double minTotalDistance = Double.POSITIVE_INFINITY;
		for (int last = 0; last < n; last++) {
			if (last == startIndex) continue;
			double minDistance = dp[last][endSubset] + graph[last][startIndex];
			minTotalDistance = Math.min(minDistance, minTotalDistance);
		}
		this.minTotalDistance = minTotalDistance;
	}

	private boolean notIn(int startIndex, int combination) {
		return (combination << startIndex) == 0;
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

	private void setup() {

	}

	/**
	 * @return shortest in weight hamiltonian path
	 */
	public List<Integer> getMinimumHamiltonianPath() {
		return Collections.emptyList();
	}

	/**
	 * @return MinimumHamiltonianCycle
	 */
	public List<Integer> getMinimumTourCost() {
		return Collections.emptyList();
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
}
