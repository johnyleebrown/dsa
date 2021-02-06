package graph.hamiltonian_path;

import java.util.Collections;
import java.util.List;

/**
 * Travelling Salesman Problem
 *
 * Given a list of cities and the distances between each pair of cities, what is the shortest
 * possible route that visits each city exactly once and returns to the origin city?
 */
public class TSP {

	// graph size
	private final int n;
	private final int startIndex;

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
	}

	public TSP(int startIndex, double[][] graph) {
		this.n = graph.length;
		this.startIndex = startIndex;
		this.graph = graph;
		checkInputParams();
		this.dp = new Double[n][1 << n];
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

	private void solve() {

		fillBaseDistances();

		// fill the dp table
		// find min distances for each {last_node, chosen_nodes_subset}

		// connect tour back to starting node and find min cost

		// reconstruct tsp path from dp table
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
}
