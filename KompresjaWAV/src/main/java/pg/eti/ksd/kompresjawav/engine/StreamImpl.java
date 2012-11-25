/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pg.eti.ksd.kompresjawav.exception.WavCompressException;

/**
 *
 * @author krzykwas
 */
public class StreamImpl implements Stream {

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
     * @param frameSize szerokość ramki (liczba bajtów na jedną próbkę)
     * @throws WavCompressException jeżeli otrzyma niepoprawne argumenty
     */
    public StreamImpl(InputStream inputStream, int windowWidth, int overlapSize, int frameSize) throws WavCompressException {
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

    /**
     * Zwraca kolejne okno.
     *
     * @return lista próbek kolejnego okna
     */
    @Override
    public List<Sample> nextWindow() {
        List<Sample> window = new ArrayList<>(overlap);

        for (int i = 0; i < windowWidth - overlap.size(); i++) {
            Sample sample = nextSample();

            if (sample == null) {
                break;
            }

            window.add(sample);
        }

        overlap.clear();

        try {
            for (int i = 0; i < overlapSize; i++) {
                overlap.add(window.get(window.size() - overlapSize + i));
            }
        } catch (Exception w) {
        }

        //Return an empty list if there is no more data, otherwise a new window.
        return window.size() > overlapSize ? window : new ArrayList<Sample>();
    }

    protected Sample nextSample() {
        int data = 0;

        try {
            for (int i = 0; i < frameSize; i++) {
                int bajt = stream.read();
                if (bajt == -1) {
                    return null;
                }

                data = (data << 8) + bajt;
            }
        } catch (IOException ex) {
            Logger.getLogger(StreamImpl.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StreamImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
