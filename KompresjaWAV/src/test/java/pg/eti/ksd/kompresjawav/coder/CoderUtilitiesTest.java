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
public class CoderUtilitiesTest {

    private final CoderUtilities coderUtilities = new CoderUtilitiesImpl();

    @Test
    public void test_predictSample_forFirstSample_returns0() {
        List<Float> coefficients = Arrays.asList(1.0f, 2.0f, 3.0f);
        List<Sample> samples = TestUtilities.asSamples(1, 2);
        double actual = coderUtilities.predictSample(0, coefficients, samples);

        double expected = 0;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_predictSample_correctlyPredictsThe5thValueFromAListOf4Samples() {
        List<Float> coefficients = Arrays.asList(1.0f, 2.0f, 3.0f);
        List<Sample> samples = TestUtilities.asSamples(1, 2, 3, 4);
        double actual = coderUtilities.predictSample(4, coefficients, samples);

        double expected = 4 * 1 + 3 * 2 + 3 * 2;

        Assert.assertEquals(expected, actual);
    }
}
