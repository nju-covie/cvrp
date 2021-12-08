package solver;

import algorithm.GreedyInsertion;
import algorithm.IAlgorithm;
import algorithm.TabuSearch;
import common.Solution;
import io.Instance;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    public static Solution solve(Instance instance) {
        List<IAlgorithm> algorithms = new ArrayList<>();
        GreedyInsertion greedyInsertion = new GreedyInsertion(instance);
        TabuSearch tabuSearch = new TabuSearch(instance);
        algorithms.add(greedyInsertion);
        algorithms.add(tabuSearch);

        Solution initial = new Solution(instance);
        for (IAlgorithm algorithm : algorithms) {
            algorithm.opt(initial);
        }
        return initial;
    }
}
