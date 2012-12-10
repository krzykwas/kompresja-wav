/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import pg.eti.ksd.kompresjawav.engine.Sample;
import pg.eti.ksd.kompresjawav.engine.SampleImpl;

/**
 *
 * @author krzykwas
 */
public class CoderImplTest {

    @Test
    public void computeError_forFirstSample_returnsFirstElement() {
        CoderImpl sut = new CoderImpl(null, 3);

        List<Double> coefficients = Arrays.asList(1.0, 2.0, 3.0);
        List<Sample> samples = Arrays.asList((Sample) new SampleImpl(7), new SampleImpl(2));
        double actual = sut.computeError(coefficients, samples, 0);

        double expected = 7;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void computeError_correctlyComputesTheErrorForThe5thElementOfA5SampleList() {
        CoderImpl sut = new CoderImpl(null, 3);

        List<Double> coefficients = Arrays.asList(1.0, 2.0, 3.0);
        List<Sample> samples = Arrays.asList((Sample) new SampleImpl(1), new SampleImpl(2), new SampleImpl(3), new SampleImpl(4), new SampleImpl(17));
        double actual = sut.computeError(coefficients, samples, 4);

        double expected = 17 - (4 * 1 + 3 * 2 + 3 * 2);

        Assert.assertEquals(expected, actual);
    }
}
