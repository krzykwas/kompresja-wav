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
    public void createFormattingWindow_withSize0_returnsAnEmptyList() {
        List<Double> formattingWindow = sut.createFormattingWindow(0);
        Assert.assertEquals(0, formattingWindow.size());
    }

    @Test
    public void createFormattingWindow_withSize1_returnsAListOfSize1() {
        List<Double> formattingWindow = sut.createFormattingWindow(1);
        Assert.assertEquals(1, formattingWindow.size());
    }

    @Test
    public void flattenWindow_correctlyFlattensWindowOfSize5_withSamplesEqualTo10() {
        List<Sample> window = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            window.add(new SampleImpl(10));
        }

        List<Sample> expected = new ArrayList<>();
        expected.add(new SampleImpl(2));
        expected.add(new SampleImpl(7));
        expected.add(new SampleImpl(10));
        expected.add(new SampleImpl(8));
        expected.add(new SampleImpl(2));

        List<Sample> actual = sut.flattenWindow(window);

        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
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
