package algorithm;

import common.Route;
import common.Solution;
import io.Instance;

import java.util.HashSet;
import java.util.Set;

/**
 * @author A
 */
public class GreedyInsertion implements IAlgorithm{
    public static class Opt {
        public int cid;
        public int rid;
        public int p;
        public double c;

        public Opt(int cid, int rid, int p, double c) {
            this.cid = cid;
            this.rid = rid;
            this.p = p;
            this.c = c;
        }
    }

    public Instance inst;

    public GreedyInsertion(Instance inst) {
        this.inst = inst;
    }

    public Opt find(Solution s, Set<Integer> set) {
        Opt bestOpt = null;
        for (int cid : set) {
            for (int j = 0; j < s.getRoutes().size(); j++) {
                Route r = s.getRoutes().get(j);
                for (int k = 1; k < r.getSequence().size(); k++) {
                    if (!r.insertFeasible(cid)) {
                        break;
                    }
                    double insertCost = r.calInsertCost(k, cid);
                    if (bestOpt == null || insertCost < bestOpt.c) {
                        bestOpt = new Opt(cid, j, k, insertCost);
                    }
                }
            }
        }
        return bestOpt;
    }

    public void modify(Solution solution, Opt opt){
        Route r = solution.getRoutes().get(opt.rid);
        r.getSequence().add(opt.p, opt.cid);
        r.setLoad(r.getLoad() + inst.getDemand().get(opt.cid));
        solution.setCost(solution.getCost() + opt.c);
    }

    @Override
    public void opt(Solution s) {
        Set<Integer> set = new HashSet<>();
        for (int i = 1; i < inst.getNodeNum(); i++) {
            set.add(i);
        }
        while(!set.isEmpty()) {
            Opt opt =  find(s, set);
            if (opt == null) {
                s.addEmptyRoute();
                continue;
            }
            modify(s, opt);
            set.remove(opt.cid);
        }
        s.check();
    }
}
