/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import pg.eti.ksd.kompresjawav.exception.WavCompressException;

/**
 *
 * @author krzykwas
 */
public class WavInputStreamImplTest {

    private class StreamMock extends InputStream {

        protected final Logger logger = Logger.getLogger(StreamMock.class.getName());
        protected InputStream stream = null;
        protected boolean closed = false;

        public StreamMock(byte[] data) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                for (byte d : data) {
                    dos.write(d);
                }

                stream = new ByteArrayInputStream(baos.toByteArray());
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage());
            }
        }

        public boolean isClosed() {
            return closed;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }
    }

    private class UncloseableStreamMock extends InputStream {

        @Override
        public int read() throws IOException {
            return -1;
        }

        @Override
        public void close() throws IOException {
            throw new IOException("Thrown for a test.");
        }
    }

    private List<Sample> asList(int[] tab) {
        List<Sample> list = new ArrayList<>();

        for (int t : tab) {
            list.add(new SampleImpl(t));
        }

        return list;
    }

    @Test(expected = WavCompressException.class)
    public void test_StreamImpl_ifOverlapBiggerThanWindow_throws_WavCompressException() throws WavCompressException {
        WavInputStream stream = new WavInputStreamImpl(new StreamMock(new byte[]{1}), 10, 20, 2);
    }

    @Test(expected = WavCompressException.class)
    public void test_StreamImpl_ifOverlapNonPositive_throws_WavCompressException() throws WavCompressException {
        WavInputStream stream = new WavInputStreamImpl(new StreamMock(new byte[]{1}), 10, 0, 2);
    }

    @Test(expected = WavCompressException.class)
    public void test_StreamImpl_ifWindowNonPositive_throws_WavCompressException() throws WavCompressException {
        WavInputStream stream = new WavInputStreamImpl(new StreamMock(new byte[]{1}), 0, 10, 2);
    }

    @Test(expected = WavCompressException.class)
    public void test_StreamImpl_ifFrameNonPositive_throws_WavCompressException() throws WavCompressException {
        WavInputStream stream = new WavInputStreamImpl(new StreamMock(new byte[]{1}), 20, 10, 0);
    }

    @Test
    public void test_close_closesUnderlyingStream() throws WavCompressException {
        final StreamMock streamMock = new StreamMock(new byte[]{1});
        WavInputStream stream = new WavInputStreamImpl(streamMock, 50, 10, 2);
        stream.close();

        Assert.assertTrue(streamMock.isClosed());
    }

    @Test
    public void test_close_whenUnderlyingStreamThrowsAnException_onlyLogsTheException() throws WavCompressException {
        final UncloseableStreamMock streamMock = new UncloseableStreamMock();
        WavInputStream stream = new WavInputStreamImpl(streamMock, 50, 10, 2);
        stream.close();
    }

    @Test
    public void test_next_invokedTheFirstTime_returnsDataWithOverlap() throws WavCompressException {
        final WavInputStreamImpl sut = new WavInputStreamImpl(new StreamMock(new byte[]{1, 2, 3}), 3, 1, 1);
        Assert.assertEquals(asList(new int[]{0, 1, 2}), sut.next().getSamples());
    }

    @Test
    public void test_next_maintainsProperOverlapBetweenInvocations() throws WavCompressException {
        final WavInputStreamImpl sut = new WavInputStreamImpl(new StreamMock(new byte[]{1, 2, 3, 4}), 3, 1, 1);
        Assert.assertEquals(asList(new int[]{0, 1, 2}), sut.next().getSamples());
        Assert.assertEquals(asList(new int[]{2, 3, 4}), sut.next().getSamples());
    }

    @Test
    public void test_next_withFrame2_producesCorrectData() throws WavCompressException {
        final WavInputStreamImpl sut = new WavInputStreamImpl(new StreamMock(new byte[]{0, 1, 1, 0, 1, 1, 1, 0}), 3, 1, 2);
        Assert.assertEquals(asList(new int[]{0, 1, 256}), sut.next().getSamples());
        Assert.assertEquals(asList(new int[]{256, 257, 256}), sut.next().getSamples());
    }

    @Test
    public void test_hasNext_withEmptyStream_returnsFalse() throws WavCompressException {
        final WavInputStreamImpl sut = new WavInputStreamImpl(new StreamMock(new byte[]{}), 2, 1, 2);
        Assert.assertFalse(sut.hasNext());
    }

    @Test
    public void test_hasNext_withNonEmptyStream_returnsTrue() throws WavCompressException {
        final WavInputStreamImpl sut = new WavInputStreamImpl(new StreamMock(new byte[]{1, 2, 3, 4, 5, 6, 7}), 2, 1, 2);
        Assert.assertTrue(sut.hasNext());
    }

    @Test
    public void test_iterator_returnsThis() throws WavCompressException {
        final WavInputStreamImpl sut = new WavInputStreamImpl(new StreamMock(new byte[]{}), 2, 1, 2);
        Assert.assertSame(sut, sut.iterator());
    }

    @Test
    public void test_asSample_withAListOfZeros_returns0() {
        Sample actual = WavInputStreamImpl.asSample(Arrays.asList(new Integer[]{0, 0}));
        Assert.assertEquals(0, actual.getValue());
    }

    @Test
    public void test_asSample_withAListOfZeroAnd3_returns3() {
        Sample actual = WavInputStreamImpl.asSample(Arrays.asList(new Integer[]{0, 3}));
        Assert.assertEquals(3, actual.getValue());
    }

    @Test
    public void test_asSample_withAListOf255And251_returnsMinus5() {
        Sample actual = WavInputStreamImpl.asSample(Arrays.asList(new Integer[]{255, 251}));
        Assert.assertEquals(-5, actual.getValue());
    }

    @Test
    public void test_asSample_withAListOf1And0_returns256() {
        Sample actual = WavInputStreamImpl.asSample(Arrays.asList(new Integer[]{1, 0}));
        Assert.assertEquals(256, actual.getValue());
    }

    @Test
    public void test_asSample_withAListOf255And0_returnsMinus256() {
        Sample actual = WavInputStreamImpl.asSample(Arrays.asList(new Integer[]{255, 0}));
        Assert.assertEquals(-256, actual.getValue());
    }
}
