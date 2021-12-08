package algorithm;

import common.Parameter;
import common.Solution;
import io.Instance;
import operator.Exchange;
import operator.Relocate;

public class TabuSearch implements IAlgorithm{
    private final Instance instance;
    public TabuSearch(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void opt(Solution s) {
        int[][] tabuList = new int[instance.getNodeNum()][instance.getNodeNum()];
        for (int i = 0; i < instance.getNodeNum(); i++) {
            for (int j = 0; j < instance.getNodeNum(); j++) {
                tabuList[i][j] = -1000000;
            }
        }

        Relocate relocate = new Relocate(instance);
        Exchange exchange = new Exchange(instance);

        int iter = 0;
        int noImprove = 0;
        boolean random = false;
        int shakeIter = 0;
        Solution bs = s.deepCopy();

        System.out.println("Initial cost " + bs.getCost());
        int relocateFreq = 0;
        int exchangeFreq = 0;
        double best = s.getCost();
        while (iter < Parameter.MAX_ITER) {
            iter++;
            Relocate.Opt opt1 = relocate.explore(random, s, tabuList, Parameter.TABU_TENURE, iter, best);
            Exchange.Opt opt2 = exchange.explore(random, s, tabuList, Parameter.TABU_TENURE, iter, best);

            if (opt1 == null && opt2 == null) {
                break;
            }

            double opt1Cost = opt1 == null ? Double.MAX_VALUE : opt1.c;
            double opt2Cost = opt2 == null ? Double.MAX_VALUE : opt2.c;

            if (opt1Cost <= opt2Cost) {
                assert opt1 != null;
                relocate.updateList(s, opt1, tabuList, iter);
                relocate.modify(s, opt1);
                relocateFreq++;
            }
            else {
                assert opt2 != null;
                exchange.updateList(s, opt2, tabuList, iter);
                exchange.modify(s, opt2);
                exchangeFreq++;
            }

            double currentCost = s.getCost();
            if (currentCost < best) {
                best = currentCost;
                noImprove = 0;
                bs = s.deepCopy();
            }
            else {
                noImprove++;
            }
            if (random) {
                shakeIter++;
            }
            if (noImprove >= Parameter.MAX_NON_IMPROVE) {
                random = true;
                shakeIter = 0;
                noImprove = 0;
            }
            System.out.println("iter " + iter + " best cost " + bs.getCost() + " current cost " + s.getCost());
            if (shakeIter >= Parameter.SHAKE_TENURE) {
                random = false;
                shakeIter = 0;
            }
        }
        bs.check();
        s.setRoutes(bs.getRoutes());
        s.setCost(bs.getCost());
        System.out.printf("Use relocate operator %d times, exchange operator %d times \n", relocateFreq, exchangeFreq);
    }
}
