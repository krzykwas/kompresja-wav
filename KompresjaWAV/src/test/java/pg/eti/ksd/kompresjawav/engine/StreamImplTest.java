/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
public class StreamImplTest {

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
        Stream stream = new StreamImpl(new StreamMock(new byte[]{1}), 10, 20, 2);
    }

    @Test(expected = WavCompressException.class)
    public void test_StreamImpl_ifOverlapNonPositive_throws_WavCompressException() throws WavCompressException {
        Stream stream = new StreamImpl(new StreamMock(new byte[]{1}), 10, 0, 2);
    }

    @Test(expected = WavCompressException.class)
    public void test_StreamImpl_ifWindowNonPositive_throws_WavCompressException() throws WavCompressException {
        Stream stream = new StreamImpl(new StreamMock(new byte[]{1}), 0, 10, 2);
    }

    @Test(expected = WavCompressException.class)
    public void test_StreamImpl_ifFrameNonPositive_throws_WavCompressException() throws WavCompressException {
        Stream stream = new StreamImpl(new StreamMock(new byte[]{1}), 20, 10, 0);
    }

    @Test
    public void test_close_closesUnderlyingStream() throws WavCompressException {
        final StreamMock streamMock = new StreamMock(new byte[]{1});
        Stream stream = new StreamImpl(streamMock, 50, 10, 2);
        stream.close();

        Assert.assertTrue(streamMock.isClosed());
    }

    @Test
    public void test_close_whenUnderlyingStreamThrowsAnException_onlyLogsTheException() throws WavCompressException {
        final UncloseableStreamMock streamMock = new UncloseableStreamMock();
        Stream stream = new StreamImpl(streamMock, 50, 10, 2);
        stream.close();
    }

    @Test
    public void test_nextWindow_onEmptyStream_returnsEmptyCollection() throws WavCompressException {
        final StreamImpl sut = new StreamImpl(new StreamMock(new byte[]{}), 10, 1, 2);
        List<Sample> window = sut.nextWindow();
        Assert.assertTrue(window.isEmpty());
    }

    @Test
    public void test_nextWindow_invokedTheFirstTime_returnsDataWithOverlap() throws WavCompressException {
        final StreamImpl sut = new StreamImpl(new StreamMock(new byte[]{1, 2, 3}), 3, 1, 1);
        Assert.assertEquals(asList(new int[]{0, 1, 2}), sut.nextWindow());
    }

    @Test
    public void test_nextWindow_invokedTwice_onStreamOfLength_equalToWindowWidthMinus1_returnsEmptyCollection() throws WavCompressException {
        final StreamImpl sut = new StreamImpl(new StreamMock(new byte[]{1, 2}), 3, 1, 1);
        sut.nextWindow();
        Assert.assertTrue(sut.nextWindow().isEmpty());
    }

    @Test
    public void test_nextWindow_maintainsProperOverlapBetweenInvocations() throws WavCompressException {
        final StreamImpl sut = new StreamImpl(new StreamMock(new byte[]{1, 2, 3, 4}), 3, 1, 1);
        Assert.assertEquals(asList(new int[]{0, 1, 2}), sut.nextWindow());
        Assert.assertEquals(asList(new int[]{2, 3, 4}), sut.nextWindow());
    }

    @Test
    public void test_nextWindow_withFrame2_producesCorrectData() throws WavCompressException {
        final StreamImpl sut = new StreamImpl(new StreamMock(new byte[]{0, 1, 1, 0, 1, 1, 1, 0}), 3, 1, 2);
        Assert.assertEquals(asList(new int[]{0, 1, 256}), sut.nextWindow());
        Assert.assertEquals(asList(new int[]{256, 257, 256}), sut.nextWindow());
    }
}
