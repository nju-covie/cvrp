package io;

import util.Util;

import java.util.List;

/**
 * @author yugaowei
 * @date 2021-12-8
 * @version 1.0
 */
public class Instance {
    /**
     * 节点数
     */
    private int nodeNum;

    /**
     * 车辆数
     */
    private int vehicleNum;

    /**
     * 车辆容量
     */
    private int vehicleCapacity;

    /**
     * 节点x坐标
     */
    private List<Integer> x;

    /**
     * 节点y坐标
     */
    private List<Integer> y;

    /**
     * 节点需求
     */
    private List<Integer> demand;

    /**
     * 距离矩阵
     */
    private double[][] distance;

    /**
     * set x coordinate list
     * @param x list of x coordinates
     */
    public void setX(List<Integer> x) {
        this.x = x;
    }

    public void setY(List<Integer> y) {
        this.y = y;
    }

    public List<Integer> getDemand() {
        return demand;
    }

    public void setDemand(List<Integer> demand) {
        this.demand = demand;
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
    }

    public void setVehicleNum(int vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public int getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(int vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    public double[][] getDistance() {
        return distance;
    }

    public void calDistance() {
        distance = new double[nodeNum][nodeNum];
        for (int i = 0; i < nodeNum; i++) {
            for (int j = i + 1; j < nodeNum; j++) {
                distance[i][j] = distance[j][i] = Util.calDis(x.get(i), y.get(i), x.get(j), y.get(j));
            }
        }
    }
}
