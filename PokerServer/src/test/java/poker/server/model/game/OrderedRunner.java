package poker.server.model.game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runners.model.InitializationError;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;


public class OrderedRunner extends BlockJUnit4ClassRunner {

	public OrderedRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

	@Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> list = super.computeTestMethods();
		ArrayList<FrameworkMethod> copy = new ArrayList<FrameworkMethod>(list);
        Collections.sort(copy, new Comparator<Object>(){
			public int compare(Object o1, Object o2) {
                return ((FrameworkMethod) o1).getMethod().getName().compareTo(((FrameworkMethod) o2).getMethod().getName());
			}
        });
        return copy;
    }
}