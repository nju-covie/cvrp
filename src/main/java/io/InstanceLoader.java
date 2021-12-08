package io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author A
 * @since 2021-12-7
 */
public class InstanceLoader {
    private InstanceLoader() {}

    public static Instance load(String path) throws FileNotFoundException {
        Instance instance = new Instance();
        try (Scanner cin = new Scanner(new FileReader(path))) {
            for (int i = 0; i < 4; i++) {
                cin.nextLine();
            }
            String line = cin.nextLine();
            String[] lineSplit = line.trim().split("\\s+");
            instance.setVehicleNum(Integer.parseInt(lineSplit[0]));
            instance.setVehicleCapacity(Integer.parseInt(lineSplit[1]));
            for (int i = 0; i < 4; i++) {
                cin.nextLine();
            }
            List<Integer> x = new ArrayList<>();
            List<Integer> y = new ArrayList<>();
            List<Integer> demand = new ArrayList<>();
            while (cin.hasNext()) {
                line = cin.nextLine();
                lineSplit = line.trim().split("\\s+");
                x.add(Integer.parseInt(lineSplit[1]));
                y.add(Integer.parseInt(lineSplit[2]));
                demand.add(Integer.parseInt(lineSplit[3]));
            }
            instance.setX(x);
            instance.setY(y);
            instance.setDemand(demand);
            instance.setNodeNum(x.size());
            instance.calDistance();
        }
        return instance;
    }
}
