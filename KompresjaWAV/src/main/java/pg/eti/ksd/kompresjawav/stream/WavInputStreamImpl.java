/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.stream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pg.eti.ksd.kompresjawav.exception.WavCompressException;

/**
 *
 * @author krzykwas
 */
public class WavInputStreamImpl implements WavInputStream {

    private final BufferedInputStream stream;
    private final int windowWidth;
    private final int frameSize;
    private final int overlapSize;
    private final List<Sample> overlap = new ArrayList<>();

    /**
     * Przetwarza strumień i dzieli go na okna o szerokości windowWidth. Dwa
     * kolejne okna nakrywają się o overlap próbek.
     *
     * @param inputStream strumień wejściowy
     * @param windowWidth szerokość okna (liczba próbek)
     * @param overlapSize liczba próbek, o którą nakrywają się kolejne okna
     * @param frameSize szerokość próbki (liczba bajtów na jedną próbkę)
     * @throws WavCompressException jeżeli otrzyma niepoprawne argumenty
     */
    public WavInputStreamImpl(InputStream inputStream, int windowWidth, int overlapSize, int frameSize) throws WavCompressException {
        this.stream = new BufferedInputStream(inputStream);
        this.windowWidth = windowWidth;
        this.frameSize = frameSize;
        this.overlapSize = overlapSize;

        if (overlapSize > windowWidth || overlapSize <= 0 || windowWidth <= 0 || frameSize <= 0) {
            throw new WavCompressException("Illegal arguments.");
        }

        for (int i = 0; i < overlapSize; i++) {
            overlap.add(new SampleImpl(0));
        }
    }

    protected Sample nextSample() {
        List<Integer> bytes = new ArrayList<>();

        try {
            for (int i = 0; i < frameSize; i++) {
                int bajt = stream.read();

                if (bajt == -1) {
                    return null;
                }

                bytes.add(bajt);
            }
        } catch (IOException ex) {
            Logger.getLogger(WavInputStreamImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        Collections.reverse(bytes);

        return asSample(bytes);
    }

    static Sample asSample(List<Integer> bytes) {
        int data = 0;
        int firstByte = bytes.get(0);

        if ((firstByte & 0x80) >> 7 == 1) {
            data = -1;
        }

        for (int i = 0; i < bytes.size(); i++) {
            data <<= 8;
            data += bytes.get(i) & 0xff;
        }

        return new SampleImpl(data);
    }

    /**
     * Zamyka strumień.
     */
    @Override
    public void close() {
        try {
            stream.close();
        } catch (IOException ex) {
            Logger.getLogger(WavInputStreamImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Iterator<WavWindow> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        stream.mark(10000);
        boolean hasNext = nextSample() != null;

        try {
            stream.reset();
        } catch (IOException ex) {
            Logger.getLogger(WavInputStreamImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return hasNext;
    }

    @Override
    public WavWindow next() {
        WavWindow window = new WavWindowImpl();
        List<Sample> samples = window.getSamples();
        samples.addAll(overlap);

        while (samples.size() < windowWidth) {
            Sample sample = nextSample();

            if (sample == null) {
                break;
            }

            samples.add(sample);
        }

        overlap.clear();
        overlap.addAll(new ArrayList<>(samples.subList(Math.max(samples.size() - overlapSize, 0), samples.size())));

        return window;
    }

    @Override
    public void remove() {
    }
}
