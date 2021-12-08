import common.Solution;
import io.Instance;
import io.InstanceLoader;
import org.junit.Test;
import solver.Solver;

import java.io.FileNotFoundException;
import java.util.Objects;

public class TabuTest {
    @Test
    public void test() throws FileNotFoundException {
        Instance instance = InstanceLoader.load(Objects.requireNonNull(TabuTest.class.getClassLoader().getResource("c102.txt")).getPath());
        Solution solution = Solver.solve(instance);
        solution.printInfo();
    }
}
