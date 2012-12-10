/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.List;
import pg.eti.ksd.kompresjawav.engine.Sample;

/**
 *
 * @author krzykwas
 */
public class CoderUtilitiesImpl {

    static double predictSample(List<Double> coefficients, List<Sample> samples, int k, int filterOrder) {
        double value = 0;
        for (int i = k - 1, j = 0; j < filterOrder; j++, i--) {
            value += (i >= 0 ? samples.get(i).getValue() : 0) * coefficients.get(j);
        }
        return value;
    }
}
