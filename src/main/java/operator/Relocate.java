package operator;

import common.Route;
import common.Solution;
import io.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Relocate {
    public static class Opt {
        public int cid;
        public int rid1;
        public int p1;
        public int rid2;
        public int p2;
        public double c;

        public Opt(int cid, int rid1, int p1, int rid2, int p2, double c) {
            this.cid = cid;
            this.rid1 = rid1;
            this.p1 = p1;
            this.rid2 = rid2;
            this.p2 = p2;
            this.c = c;
        }
    }

    public Random rand;
    public Instance inst;
    public Relocate(Instance inst) {
        this.inst = inst;
        rand = new Random(1);
    }

    public Opt explore(boolean random, Solution s, int[][] tabuList, int tabuTenure, int iter, double best) {
        Opt bestOpt = null;
        double cost = s.getCost();
        List<Opt> list = new ArrayList<>();
        for (int i = 0; i < s.getRoutes().size(); i++) {
            Route r1 = s.getRoutes().get(i);
            for(int j = 1; j < r1.getSequence().size() - 1; j++) {
                int cid = r1.getSequence().get(j);
                for (int k = 0; k < s.getRoutes().size(); k++) {
                    if (k == i) {
                        continue;
                    }
                    Route r2 = s.getRoutes().get(k);
                    for(int h = 1; h < r2.getSequence().size(); h++) {
                        if (!r1.relocateFeasible(j, r2)) {
                            continue;
                        }

                        double relocateCost = r1.calRelocateCost(j, r2, h);

                        int pre = r1.getSequence().get(j - 1);
                        int next = r1.getSequence().get(j + 1);

                        Opt opt = new Opt(cid, i, j, k, h, relocateCost);
                        list.add(opt);

                        if((iter - tabuList[pre][next] <= tabuTenure ||
                                iter - tabuList[cid][next] <= tabuTenure) &&
                                cost + relocateCost > best) {
                            continue;
                        }

                        if(bestOpt == null || relocateCost <bestOpt.c) {
                            bestOpt = opt;
                        }
                    }
                }
            }
        }

        if(!random) {
            return bestOpt;
        }
        else {
            if (!list.isEmpty()) {
                return list.get(rand.nextInt(list.size()));
            }
            else {
                return null;
            }
        }
    }

    public void modify(Solution s, Opt opt) {
        Route r1 = s.getRoutes().get(opt.rid1);
        Route r2 = s.getRoutes().get(opt.rid2);

        r1.getSequence().remove(opt.p1);
        if (r1.getSequence().isEmpty()) {
            s.getRoutes().remove(opt.rid1);
        }
        r2.getSequence().add(opt.p2, opt.cid);

        r1.setLoad(r1.getLoad() - inst.getDemand().get(opt.cid));
        r2.setLoad(r2.getLoad() + inst.getDemand().get(opt.cid));
        s.setCost(s.getCost() + opt.c);
    }

    public void updateList(Solution s, Opt opt, int[][] tabuList, int iter) {
        Route r = s.getRoutes().get(opt.rid2);
        int pre = r.getSequence().get(opt.p2 - 1);
        int next = r.getSequence().get(opt.p2);
        tabuList[pre][opt.cid] = iter;
        tabuList[opt.cid][next] = iter;

//        Route r2 = s.getRoutes().get(opt.rid1);
//        int pre2 = r2.getSequence().get(opt.p1 - 1);
//        int next2 = r2.getSequence().get(opt.p1 + 1);
//        tabuList[pre2][next2] = iter;
    }
}
