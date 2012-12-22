/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav;

import java.util.ArrayList;
import java.util.List;
import pg.eti.ksd.kompresjawav.stream.Sample;
import pg.eti.ksd.kompresjawav.stream.SampleImpl;

/**
 *
 * @author krzykwas
 */
public class TestUtilities {

    public static List<Sample> asSamples(int... params) {
        List<Sample> result = new ArrayList<>();
        for (int i : params) {
            result.add(new SampleImpl(i));
        }
        return result;
    }
}
