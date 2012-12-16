/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pg.eti.ksd.kompresjawav.stream.Sample;
import pg.eti.ksd.kompresjawav.stream.SampleImpl;

/**
 *
 * @author krzykwas
 */
public class LevinsonDurbinImplTest {

    private LevinsonDurbinImpl sut;

    @Before
    public void setUp() {
        sut = new LevinsonDurbinImpl();
    }

    @Test
    public void calculateAutocorrelationCoefficients_correctlyComputesForWindowOfSize5_withSamplesEqualTo10() {
        List<Sample> window = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            window.add(new SampleImpl(10));
        }

        List<Double> expected = new ArrayList<>();
        expected.add(500.0);
        expected.add(400.0);
        expected.add(300.0);

        List<Double> actual = sut.calculateAutocorrelationCoefficients(window, 3);

        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }
}
