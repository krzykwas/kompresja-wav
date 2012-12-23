/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import pg.eti.ksd.kompresjawav.TestUtilities;
import pg.eti.ksd.kompresjawav.stream.Sample;

/**
 *
 * @author krzykwas
 */
public class CoderImplTest {

    @Test
    public void computeError_forFirstSample_returnsFirstElement() {
        CoderImpl sut = new CoderImpl(null, 3);

        List<Double> coefficients = Arrays.asList(1.0, 2.0, 3.0);
        List<Sample> samples = TestUtilities.asSamples(7, 2);
        double actual = sut.computeError(0, coefficients, samples);
        double expected = 7;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void computeError_correctlyComputesTheErrorForThe5thElementOfA5SampleList() {
        CoderImpl sut = new CoderImpl(null, 3);

        List<Double> coefficients = Arrays.asList(1.0, 2.0, 3.0);
        List<Sample> samples = TestUtilities.asSamples(1, 2, 3, 4, -10);
        double actual = sut.computeError(4, coefficients, samples);
        double expected = -10 - (-(4 * 1 + 3 * 2 + 3 * 2));

        Assert.assertEquals(expected, actual);
    }
}
