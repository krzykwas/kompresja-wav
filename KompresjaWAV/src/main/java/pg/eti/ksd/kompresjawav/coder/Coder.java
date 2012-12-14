/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.Iterator;
import pg.eti.ksd.kompresjawav.engine.CompressedPacket;

/**
 *
 * @author krzykwas
 */
public interface Coder extends Iterable<CompressedPacket>, Iterator<CompressedPacket> {
}
