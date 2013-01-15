/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pg.eti.ksd.kompresjawav.TestUtilities;
import pg.eti.ksd.kompresjawav.stream.Sample;
import pg.eti.ksd.kompresjawav.stream.SampleImpl;
import pg.eti.ksd.kompresjawav.stream.WavWindow;
import pg.eti.ksd.kompresjawav.stream.WavWindowImpl;

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
    public void test_calculateAutocorrelationCoefficients_correctlyComputesForWindowOfSize5_withSamplesEqualTo10() {
        List<Sample> window = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            window.add(new SampleImpl(10));
        }

        List<Double> expected = new ArrayList<>();
        expected.add(100.0);
        expected.add(80.0);
        expected.add(60.0);

        List<Double> actual = sut.calculateAutocorrelationCoefficients(window, 3);

        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void test_calculateAutocorrelationCoefficients_correctlyComputesForThisSetOfSamples() {
        List<Sample> window = TestUtilities.asSamples(2, 5, 10, 8, 1, 3, 0, 5, 1, 2);
        List<Double> expected = Arrays.asList(new Double[]{23.3, 15.8, 11.9, 5.9});
        List<Double> actual = sut.calculateAutocorrelationCoefficients(window, 4);

        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void test_identifyCoefficients_correctlyComputes1CoefficientForThisSetOfSamples() {
        WavWindow window = new WavWindowImpl();
        window.getSamples().addAll(TestUtilities.asSamples(2, 5, 10, 8, 1, 3, 0, 5, 1, 2));
        List<Double> expected = Arrays.asList(new Double[]{0.67811158});
        List<Float> actual = sut.identifyCoefficients(window, 1);

        Assert.assertEquals(expected.get(0), actual.get(0), 1e-5);
    }

    @Test
    public void test_identifyCoefficients_correctlyComputes2CoefficientsForThisSetOfSamples() {
        WavWindow window = new WavWindowImpl();
        window.getSamples().addAll(TestUtilities.asSamples(2, 5, 10, 8, 1, 3, 0, 5, 1, 2));
        List<Double> expected = Arrays.asList(new Double[]{0.61421994, 0.09421994});
        List<Float> actual = sut.identifyCoefficients(window, 2);

        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i), 1e-5);
        }
    }

    @Test
    public void test_identifyCoefficients_correctlyComputes3CoefficientsForThisSetOfSamples() {
        WavWindow window = new WavWindowImpl();
        window.getSamples().addAll(TestUtilities.asSamples(2, 5, 10, 8, 1, 3, 0, 5, 1, 2));
        List<Double> expected = Arrays.asList(new Double[]{0.63610843, 0.23691099, -0.23231262});
        List<Float> actual = sut.identifyCoefficients(window, 3);

        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i), 1e-5);
        }
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
        WavWindow window = new WavWindowImpl();
        List<Sample> samples = window.getSamples();
        for (int i = 0; i < 5; i++) {
            samples.add(new SampleImpl(10));
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
}
