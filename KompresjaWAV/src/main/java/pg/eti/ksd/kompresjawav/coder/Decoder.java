/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.List;
import pg.eti.ksd.kompresjawav.engine.CompressedPacket;
import pg.eti.ksd.kompresjawav.engine.Sample;

/**
 *
 * @author krzykwas
 */
public interface Decoder {

    List<Sample> decode(List<CompressedPacket> packets);
    
}
