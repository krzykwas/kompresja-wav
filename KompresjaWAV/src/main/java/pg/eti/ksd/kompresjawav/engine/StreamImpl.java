/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krzykwas
 */
public class StreamImpl implements Stream {

    private final InputStream inputStream;
    private final int windowWidth;
    private final int overlap;

    /**
     * Przetwarza strumień i dzieli go na okna o szerokości windowWidth. Dwa
     * kolejne okna nakrywają się o overlap próbek.
     *
     * @param inputStream strumień wejściowy
     * @param windowWidth szerokość okna
     * @param overlap liczba próbek, o którą nakrywają się kolejne okna
     */
    public StreamImpl(InputStream inputStream, int windowWidth, int overlap) {
        this.inputStream = inputStream;
        this.windowWidth = windowWidth;
        this.overlap = overlap;

        if (overlap > windowWidth || overlap <= 0 || windowWidth <= 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Zwraca kolejne okno.
     *
     * @return lista próbek kolejnego okna
     */
    @Override
    public List<Sample> nextWindow() {
        return null;
    }

    /**
     * Zamyka strumień.
     */
    @Override
    public void close() {
        try {
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(StreamImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
