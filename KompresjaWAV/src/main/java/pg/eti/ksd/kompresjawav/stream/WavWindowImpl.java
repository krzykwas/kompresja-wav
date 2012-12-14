/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.stream;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author krzykwas
 */
public class WavWindowImpl implements WavWindow {

    private final List<Sample> samples = new ArrayList<>();

    @Override
    public List<Sample> getSamples() {
        return samples;
    }
}
