package operator;

import common.Route;
import common.Solution;
import io.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Exchange {
    public static class Opt {
        public int rid1;
        public int p1;
        public int rid2;
        public int p2;
        public double c;

        public Opt(int rid1, int p1, int rid2, int p2, double c) {
            this.rid1 = rid1;
            this.p1 = p1;
            this.rid2 = rid2;
            this.p2 = p2;
            this.c = c;
        }
    }

    public Instance inst;
    public Random rand;

    public Exchange(Instance inst) {
        this.inst = inst;
        rand = new Random(1);
    }

    public Opt explore(boolean random, Solution s, int[][] tabuList, int tabuTenure, int iter, double best) {
        Opt bestOpt = null;
        double cost = s.getCost();
        List<Opt> list = new ArrayList<>();

        for(int i = 0; i < s.getRoutes().size(); i++) {
            Route r1 = s.getRoutes().get(i);
            for (int j = 1; j < r1.getSequence().size() - 1; j++) {
                for (int k = 0; k < s.getRoutes().size(); k++) {
                    if (k == i) {
                        continue;
                    }
                    Route r2 = s.getRoutes().get(k);
                    for (int h = 1; h < r2.getSequence().size() - 1; h++) {
                        if (!r1.exchangeFeasible(j, r2, h)) {
                            continue;
                        }

                        int id1 = r1.getSequence().get(j);
                        int pre1 = r1.getSequence().get(j - 1);
                        int next1 = r1.getSequence().get(j + 1);

                        int id2 = r2.getSequence().get(h);
                        int pre2 = r2.getSequence().get(h - 1);
                        int next2 = r2.getSequence().get(h + 1);

                        double exchangeCost = r1.calExchangeCost(j, r2, h);
                        Opt opt = new Opt(i, j, k, h, exchangeCost);
                        list.add(opt);

                        if((iter - tabuList[pre1][id1] <= tabuTenure
                                || iter - tabuList[id1][next1] <= tabuTenure)
                                || iter - tabuList[pre2][id2] <= tabuTenure
                                || iter - tabuList[id2][next2] <= tabuTenure
                                && cost + exchangeCost > best) {
                            continue;
                        }

                        if (bestOpt == null || exchangeCost < bestOpt.c) {
                            bestOpt = opt;
                        }
                    }
                }
            }
        }
        if (!random) {
            return bestOpt;
        }
        else {
            int p = rand.nextInt(list.size());
            return list.get(p);
        }
    }

    public void modify(Solution s, Opt opt) {
        Route r1 = s.getRoutes().get(opt.rid1);
        Route r2 = s.getRoutes().get(opt.rid2);
        int id1 = r1.getSequence().get(opt.p1);
        int id2 = r2.getSequence().get(opt.p2);

        r1.setLoad(r1.getLoad() + inst.getDemand().get(id2) - inst.getDemand().get(id1));
        r2.setLoad(r2.getLoad() + inst.getDemand().get(id1) - inst.getDemand().get(id2));

        r1.getSequence().set(opt.p1, id2);
        r2.getSequence().set(opt.p2, id1);
        s.setCost(s.getCost() + opt.c);
    }

    public void updateList(Solution s, Opt opt, int[][] tabuList, int iter) {
        Route r1 = s.getRoutes().get(opt.rid1);
        Route r2 = s.getRoutes().get(opt.rid2);
        int pre1 = r1.getSequence().get(opt.p1 - 1);
        int id1 = r1.getSequence().get(opt.p1);
        int next1 = r1.getSequence().get(opt.p1 + 1);
        int pre2 = r2.getSequence().get(opt.p2 - 1);
        int id2 = r2.getSequence().get(opt.p2);
        int next2 = r2.getSequence().get(opt.p2 + 1);

        tabuList[pre1][id2] = iter;
        tabuList[id2][next1] = iter;
        tabuList[pre2][id1] = iter;
        tabuList[id1][next2] = iter;
    }
}