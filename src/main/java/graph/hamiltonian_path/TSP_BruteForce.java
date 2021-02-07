package graph.hamiltonian_path;

/**
 * https://github.com/williamfiset/Algorithms/blob/master/src/main/java/com/williamfiset/algorithms/graphtheory/TspBruteForce.java
 */
public class TSP_BruteForce {

	private double cost;
	private int[] bestTour;
	private double bestTourCost;

	public TSP_BruteForce(double[][] matrix) {
		cost = 0;
		tsp(matrix);
	}

	// Given an nxn complete graph represented as an adjacency
	// matrix this method finds the best tour that visits all
	// the nodes while minimizing the overall visit cost.
	private void tsp(double[][] matrix) {

		int n = matrix.length;
		int[] permutation = new int[n];
		for (int i = 0; i < n; i++) permutation[i] = i;

		int[] bestTour = permutation.clone();
		double bestTourCost = Double.POSITIVE_INFINITY;

		// Try all n! tours
		do {

			double tourCost = computeTourCost(permutation, matrix);

			if (tourCost < bestTourCost) {
				bestTourCost = tourCost;
				bestTour = permutation.clone();
			}

		} while (nextPermutation(permutation));

		this.bestTour = bestTour;
		this.bestTourCost = bestTourCost;
	}

	private double computeTourCost(int[] tour, double[][] matrix) {

		// Compute the cost of going to each city
		for (int i = 1; i < matrix.length; i++) {
			int from = tour[i - 1];
			int to = tour[i];
			cost += matrix[from][to];
		}

		// Compute the cost to return to the starting city
		int last = tour[matrix.length - 1];
		int first = tour[0];

		return cost + matrix[last][first];
	}

	// Generates the next ordered permutation in-place (skips repeated permutations).
	// Calling this when the array is already at the highest permutation returns false.
	// Recommended usage is to start with the smallest permutations and use a do while
	// loop to generate each successive permutations (see main for example).
	private boolean nextPermutation(int[] sequence) {
		int first = getFirst(sequence);
		if (first == -1) return false;
		int toSwap = sequence.length - 1;
		while (sequence[first] >= sequence[toSwap]) --toSwap;
		swap(sequence, first++, toSwap);
		toSwap = sequence.length - 1;
		while (first < toSwap) swap(sequence, first++, toSwap--);
		return true;
	}

	private int getFirst(int[] sequence) {
		for (int i = sequence.length - 2; i >= 0; --i) if (sequence[i] < sequence[i + 1]) return i;
		return -1;
	}

	private void swap(int[] sequence, int i, int j) {
		int tmp = sequence[i];
		sequence[i] = sequence[j];
		sequence[j] = tmp;
	}

	public double getBestTourCost() {
		return bestTourCost;
	}

	public int[] getBestTour() {
		return bestTour;
	}
}
