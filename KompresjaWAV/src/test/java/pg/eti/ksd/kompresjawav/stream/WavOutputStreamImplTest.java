/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.stream;

import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author krzykwas
 */
public class WavOutputStreamImplTest {

    private AudioFormat getAudioFormat() {
        return new AudioFormat(1.0f, 16, 1, true, true);
    }

    private <T> void assertListsEqual(List<T> expected, List<T> actual) {
        Assert.assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void test_asBytes_with0_returnsAListOfZeros() {
        WavOutputStreamImpl sut = new WavOutputStreamImpl(null, getAudioFormat());
        List<Integer> actual = sut.asBytes(0);
        List<Integer> expected = Arrays.asList(new Integer[]{0, 0});

        assertListsEqual(expected, actual);
    }

    @Test
    public void test_asBytes_with3_returnsAListOfZeroAnd3() {
        WavOutputStreamImpl sut = new WavOutputStreamImpl(null, getAudioFormat());
        List<Integer> actual = sut.asBytes(3);
        List<Integer> expected = Arrays.asList(new Integer[]{0, 3});

        assertListsEqual(expected, actual);
    }

    @Test
    public void test_asBytes_withMinus5_returnsAListOf255And251() {
        WavOutputStreamImpl sut = new WavOutputStreamImpl(null, getAudioFormat());
        List<Integer> actual = sut.asBytes(-5);
        List<Integer> expected = Arrays.asList(new Integer[]{255, 251});

        assertListsEqual(expected, actual);
    }

    @Test
    public void test_asBytes_with256_returnsAListOf1And0() {
        WavOutputStreamImpl sut = new WavOutputStreamImpl(null, getAudioFormat());
        List<Integer> actual = sut.asBytes(256);
        List<Integer> expected = Arrays.asList(new Integer[]{1, 0});

        assertListsEqual(expected, actual);
    }

    @Test
    public void test_asBytes_withMinus256_returnsAListOf255And0() {
        WavOutputStreamImpl sut = new WavOutputStreamImpl(null, getAudioFormat());
        List<Integer> actual = sut.asBytes(-256);
        List<Integer> expected = Arrays.asList(new Integer[]{255, 0});

        assertListsEqual(expected, actual);
    }
}
