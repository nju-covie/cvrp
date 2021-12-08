package common;

import io.Instance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {
    private final Instance instance;
    private List<Route> routes;
    private double cost;

    public Solution(Instance instance) {
        this.instance = instance;
        routes = new ArrayList<>();
        cost = 0;
    }

    public Solution deepCopy() {
        Solution copy = new Solution(instance);
        List<Route> routesCopy = new ArrayList<>();
        for (Route route : routes) {
            routesCopy.add(route.deepCopy());
        }
        copy.setRoutes(routesCopy);
        copy.setCost(cost);
        return copy;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void addEmptyRoute() {
        routes.add(new Route(instance));
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void check() {
        Set<Integer> customers = new HashSet<>();
        double totalCost = 0;
        for (int i = 1; i < instance.getNodeNum(); i++) {
            customers.add(i);
        }
        for (Route route : routes) {
            if (route.getSequence().get(0) != 0 || route.getSequence().get(route.getSequence().size() - 1) != 0) {
                System.out.println("Error route start or end point ");
                System.exit(0);
            }
            int load = 0;
            for (int i = 1; i < route.getSequence().size(); i++) {
                customers.remove(route.getSequence().get(i));
                load += instance.getDemand().get(route.getSequence().get(i));
                totalCost += instance.getDistance()[route.getSequence().get(i - 1)][route.getSequence().get(i)];
            }
            if (load > instance.getVehicleCapacity()) {
                System.out.println("Exceed capacity");
                System.exit(0);
            }
            if (load != route.getLoad()) {
                System.out.println("Load calculate error");
                System.exit(0);
            }
        }
        if (!customers.isEmpty()) {
            System.out.println("Unvisited customer");
            System.exit(0);
        }
        if (Math.abs(totalCost - cost) > 1e-9) {
            System.out.println("Cost calculate error");
            System.exit(0);
        }
        System.out.println("The solution is feasible");
    }


    public void printInfo() {
        for (int i = 0; i < routes.size(); i++) {
            StringBuilder s1 = new StringBuilder();
            s1.append("Route ").append(i + 1).append(": ");
            for (int j = 0; j < routes.get(i).getSequence().size(); j++) {
                if (j != 0 && j != routes.get(i).getSequence().size()) {
                    s1.append("->");
                }
                s1.append(routes.get(i).getSequence().get(j));
            }
            System.out.println(s1);
        }
        System.out.printf("The total distance is %.2f", cost);
    }
}
