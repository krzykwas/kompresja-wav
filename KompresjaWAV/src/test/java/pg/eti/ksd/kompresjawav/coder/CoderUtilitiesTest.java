/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import pg.eti.ksd.kompresjawav.stream.Sample;
import pg.eti.ksd.kompresjawav.stream.SampleImpl;

/**
 *
 * @author krzykwas
 */
public class CoderUtilitiesTest {

    private final CoderUtilities coderUtilities = new CoderUtilitiesImpl();

    @Test
    public void test_predictSample_forFirstSample_returns0() {
        List<Double> coefficients = Arrays.asList(1.0, 2.0, 3.0);
        List<Sample> samples = Arrays.asList((Sample) new SampleImpl(1), new SampleImpl(2));
        double actual = coderUtilities.predictSample(0, coefficients, samples);

        double expected = 0;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_predictSample_correctlyPredictsThe5thValueFromAListOf4Samples() {
        List<Double> coefficients = Arrays.asList(1.0, 2.0, 3.0);
        List<Sample> samples = Arrays.asList((Sample) new SampleImpl(1), new SampleImpl(2), new SampleImpl(3), new SampleImpl(4));
        double actual = coderUtilities.predictSample(4, coefficients, samples);

        double expected = 4 * 1 + 3 * 2 + 3 * 2;

        Assert.assertEquals(expected, actual);
    }
}
