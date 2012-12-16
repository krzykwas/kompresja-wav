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
        CompressedPacketImpl sut = new CompressedPacketImpl(Arrays.asList(new Double[]{1.0}), Arrays.asList(new Double[]{1.0, 2.0, 3.0}));
        List<Double> actual = sut.computeQuantizationLevels();
        Assert.assertEquals(CompressedPacketImpl.QUANTIZATION_LEVELS, actual.size());
    }

    @Test
    public void test_compressErrors_packs32ValuesIntoOneLong() {
        List<Double> errors = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            errors.add(1.0);
        }

        CompressedPacketImpl sut = new CompressedPacketImpl(Arrays.asList(new Double[]{1.0}), errors);
        List<Long> compressed = sut.compressErrors(errors);
        Assert.assertEquals(1, compressed.size());
    }

    @Test
    public void test_compressErrors_with32ErrorsEqualToZero_returnsZero() {
        List<Double> errors = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            errors.add(0.0);
        }

        CompressedPacketImpl sut = new CompressedPacketImpl(Arrays.asList(new Double[]{1.0}), errors);
        List<Long> compressed = sut.compressErrors(errors);
        Assert.assertEquals(0, (long) compressed.get(0));
    }

    @Test
    public void test_getErrors_with32EqualErrors_returnsAListOf32eMax() {
        List<Double> errors = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            errors.add(2.0);
        }

        CompressedPacketImpl sut = new CompressedPacketImpl(Arrays.asList(new Double[]{1.0}), errors);
        final List<Double> actual = sut.getErrors();

        Assert.assertEquals(32, actual.size());
        for (int i = 0; i < 32; i++) {
            Assert.assertEquals(2.0, actual.get(i));
        }
    }
}
