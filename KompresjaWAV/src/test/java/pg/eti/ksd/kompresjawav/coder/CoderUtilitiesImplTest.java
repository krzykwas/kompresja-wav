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
public class CoderUtilitiesImplTest {

    @Test
    public void test_predictSample_correctlyPredictValueBasedOnThreePrevious() {
        List<Double> coefficients = Arrays.asList(new Double[]{1.0, 2.0, 3.0});
        List<Sample> samples = Arrays.asList(new Sample[]{new SampleImpl(10), new SampleImpl(11), new SampleImpl(12), new SampleImpl(14), new SampleImpl(15)});

        double actual = CoderUtilitiesImpl.predictSample(coefficients, samples, 5, 3);

        Assert.assertEquals(actual, 1.0 * 15 + 2.0 * 14 + 3.0 * 12);
    }
}
