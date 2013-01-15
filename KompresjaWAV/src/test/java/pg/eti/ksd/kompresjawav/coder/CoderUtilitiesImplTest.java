/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import pg.eti.ksd.kompresjawav.TestUtilities;
import pg.eti.ksd.kompresjawav.stream.Sample;

/**
 *
 * @author krzykwas
 */
public class CoderUtilitiesImplTest {

    private CoderUtilitiesImpl sut;

    @Before
    public void setUp() {
        sut = new CoderUtilitiesImpl();
    }

    @Test
    public void test_predictSample_correctlyPredictsValueBasedOnThreePrevious() {
        List<Float> coefficients = Arrays.asList(1.0f, 2.0f, 3.0f);
        List<Sample> samples = TestUtilities.asSamples(10, 11, 12, 14, 15);

        double actual = sut.predictSample(5, coefficients, samples);

        Assert.assertEquals(1.0 * 15 + 2.0 * 14 + 3.0 * 12, actual);
    }

    @Test
    public void test_predictSample_correctlyPredictsValueIfThereAreMoreCoefficientsThanSamples() {
        List<Float> coefficients = Arrays.asList(1.0f, 2.0f, 3.0f, 5.0f, -1.0f);
        List<Sample> samples = TestUtilities.asSamples(10, 11, 12);

        double actual = sut.predictSample(3, coefficients, samples);

        Assert.assertEquals(1.0 * 12 + 2.0 * 11 + 3.0 * 10, actual);
    }
}
