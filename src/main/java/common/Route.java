package common;

import io.Instance;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private final Instance instance;
    /**
     * 节点序列
     */
    private List<Integer> sequence;

    /**
     * 负载
     */
    private int load;

    /**
     * 行驶距离
     */
    private double distance;

    public Route(Instance instance) {
        this.instance = instance;
        sequence = new ArrayList<>();
        sequence.add(0);
        sequence.add(0);
        load = 0;
    }

    public Route deepCopy() {
        Route copy = new Route(instance);
        copy.setDistance(distance);
        copy.setLoad(load);
        copy.setSequence(new ArrayList<>(sequence));
        return copy;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setSequence(List<Integer> sequence) {
        this.sequence = sequence;
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public int getLoad() {
        return load;
    }

    public boolean insertFeasible(int cid) {
        return instance.getDemand().get(cid) + load <= instance.getVehicleCapacity();
    }

    public double calInsertCost(int position, int cid) {
        int pre = sequence.get(position - 1);
        int next = sequence.get(position);
        return instance.getDistance()[pre][cid] + instance.getDistance()[cid][next] - instance.getDistance()[pre][next];
    }

    public boolean exchangeFeasible(int p1, Route r2, int p2) {
        int cid1 = sequence.get(p1);
        int demand1 = instance.getDemand().get(cid1);
        int cid2 = r2.getSequence().get(p2);
        int demand2 = instance.getDemand().get(cid2);
        return load - demand1 + demand2 <= instance.getVehicleCapacity() && r2.getLoad() - demand2 + demand1 <= instance.getVehicleCapacity();
    }

    public double calExchangeCost(int p1, Route r2, int p2) {
        int pre1 = sequence.get(p1 - 1);
        int id1 = sequence.get(p1);
        int next1 = sequence.get(p1 + 1);

        int pre2 = r2.getSequence().get(p2 - 1);
        int id2 = r2.getSequence().get(p2);
        int next2 = r2.getSequence().get(p2 + 1);

        return instance.getDistance()[pre1][id2] + instance.getDistance()[id2][next1] + instance.getDistance()[pre2][id1] +
                instance.getDistance()[id1][next2] - instance.getDistance()[pre1][id1] - instance.getDistance()[id1][next1] -
                instance.getDistance()[pre2][id2] - instance.getDistance()[id2][next2];
    }

    public boolean relocateFeasible(int p1, Route r2) {
        return r2.getLoad() + instance.getDemand().get(sequence.get(p1)) <= instance.getVehicleCapacity();
    }

    public double calRelocateCost(int p1, Route r2, int p2) {
        int pre1 = sequence.get(p1 - 1);
        int cid1 = sequence.get(p1);
        int next1 = sequence.get(p1 + 1);

        int pre2 = r2.getSequence().get(p2 - 1);
        int cid2 = r2.getSequence().get(p2);
        return instance.getDistance()[pre1][next1] - instance.getDistance()[pre1][cid1] - instance.getDistance()[cid1][next1] +
                instance.getDistance()[pre2][cid1] + instance.getDistance()[cid1][cid2] - instance.getDistance()[pre2][cid2];
    }
}
