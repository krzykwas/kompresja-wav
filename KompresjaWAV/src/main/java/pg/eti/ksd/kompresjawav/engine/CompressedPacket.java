/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.List;
import pg.eti.ksd.kompresjawav.stream.Sample;

/**
 *
 * @author krzykwas
 */
public interface CompressedPacket {

    List<Double> getCoefficients();

    List<Double> getErrors();

    List<Sample> getInitialValues();
}
