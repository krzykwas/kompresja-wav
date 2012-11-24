/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.List;

/**
 *
 * @author krzykwas
 */
public interface Stream {

    void close();

    List<Sample> nextWindow();
}
