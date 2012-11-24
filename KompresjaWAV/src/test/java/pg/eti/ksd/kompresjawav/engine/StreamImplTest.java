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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author krzykwas
 */
public class StreamImplTest {

    private class StreamMock extends InputStream {

        private final Logger logger = Logger.getLogger(StreamMock.class.getName());
        private InputStream stream = null;
        private boolean closed = false;

        public StreamMock(int[] data) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                for (int d : data) {
                    dos.writeInt(d);
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

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_StreamImpl_ifOverlapBiggerThanWindow_throws_IllegalArgumentException() {
        Stream stream = new StreamImpl(new StreamMock(new int[]{1}), 10, 20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_StreamImpl_ifOverlapNonPositive_throws_IllegalArgumentException() {
        Stream stream = new StreamImpl(new StreamMock(new int[]{1}), 10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_StreamImpl_ifWindowNonPositive_throws_IllegalArgumentException() {
        Stream stream = new StreamImpl(new StreamMock(new int[]{1}), 0, 10);
    }

    @Test
    public void test_close_closesUnderlyingStream() {
        final StreamMock streamMock = new StreamMock(new int[]{1});
        Stream stream = new StreamImpl(streamMock, 50, 10);
        stream.close();

        Assert.assertTrue(streamMock.isClosed());
    }
}
