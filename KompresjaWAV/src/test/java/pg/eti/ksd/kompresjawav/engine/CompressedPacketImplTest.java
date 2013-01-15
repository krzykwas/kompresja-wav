/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import pg.eti.ksd.kompresjawav.stream.Sample;

/**
 *
 * @author krzykwas
 */
public class CompressedPacketImplTest {

    @Test
    public void test_computeMaxError_withNegativeMaximum_returnsCorrectPositiveValue() {
        double actual = CompressedPacketImpl.computeMaxError(Arrays.asList(new Double[]{1.0, 1.0, -5.0}));
        Assert.assertEquals(5.0, actual, 0.0001);
    }

    @Test
    public void test_computeMaxError_withEmptyList_returnsNegativeInfinity() {
        double actual = CompressedPacketImpl.computeMaxError(Arrays.asList(new Double[]{}));
        Assert.assertEquals(Double.NEGATIVE_INFINITY, actual, 0.0001);
    }

    @Test
    public void test_computeMaxError_returnsMaximalValue() {
        double actual = CompressedPacketImpl.computeMaxError(Arrays.asList(new Double[]{0.4, 1.3, 5.6, 0.2, 4.0, 3.1}));
        Assert.assertEquals(5.6, actual, 0.0001);
    }

    @Test
    public void test_computeQuantizationLevels_generatesCorrectNumberOfLevels() {
        CompressedPacketImpl sut = new CompressedPacketImpl(Arrays.asList(new Float[]{1.0f}), Arrays.asList(new Double[]{1.0, 2.0, 3.0}), Arrays.asList(new Sample[]{}));
        List<Double> actual = sut.computeQuantizationLevels(CompressedPacketImpl.QUANTIZATION_LEVELS);
        Assert.assertEquals(CompressedPacketImpl.QUANTIZATION_LEVELS, actual.size());
    }

    @Test
    public void test_computeQuantizationLevels_generatesCorrect4Levels() {
        CompressedPacketImpl sut = new CompressedPacketImpl(Arrays.asList(new Float[]{1.0f}), Arrays.asList(new Double[]{1.0, 2.0}), Arrays.asList(new Sample[]{}));
        List<Double> actual = sut.computeQuantizationLevels(4);
        Assert.assertEquals(-3 / 2.0, actual.get(0));
        Assert.assertEquals(-1 / 2.0, actual.get(1));
        Assert.assertEquals(1 / 2.0, actual.get(2));
        Assert.assertEquals(3 / 2.0, actual.get(3));
    }

    @Test
    public void test_compressErrors_packsValuesIntoOneLong() {
        List<Double> errors = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            errors.add(1.0);
        }

        CompressedPacketImpl sut = new CompressedPacketImpl(Arrays.asList(new Float[]{1.0f}), errors, Arrays.asList(new Sample[]{}));
        List<Long> compressed = sut.compressErrors(errors);
        Assert.assertEquals(1, compressed.size());
    }

    @Test
    public void test_compressErrors_with16ErrorsEqualToZero_returnsZero() {
        List<Double> errors = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            errors.add(0.0);
        }

        CompressedPacketImpl sut = new CompressedPacketImpl(Arrays.asList(new Float[]{1.0f}), errors, Arrays.asList(new Sample[]{}));
        List<Long> compressed = sut.compressErrors(errors);
        Assert.assertEquals(0, (long) compressed.get(0));
    }
}
