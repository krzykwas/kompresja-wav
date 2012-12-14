/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import pg.eti.ksd.kompresjawav.engine.CompressedPacket;
import pg.eti.ksd.kompresjawav.stream.WavWindow;

/**
 *
 * @author krzykwas
 */
public interface Decoder {

    WavWindow decode(CompressedPacket packet);
}
