/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.stream;

import java.util.Iterator;

/**
 *
 * @author krzykwas
 */
public interface Stream extends Iterable<WavWindow>, Iterator<WavWindow> {

    void close();
}
