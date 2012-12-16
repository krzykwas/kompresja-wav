/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.stream;

import java.io.IOException;

/**
 *
 * @author krzykwas
 */
public interface WavOutputStream {

    void close() throws IOException;

    void write(WavWindow window);
}
