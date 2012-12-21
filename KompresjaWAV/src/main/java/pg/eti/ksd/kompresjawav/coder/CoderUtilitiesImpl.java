/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pg.eti.ksd.kompresjawav.stream.Sample;
import pg.eti.ksd.kompresjawav.stream.SampleImpl;

/**
 *
 * @author krzykwas
 */
public class CoderUtilitiesImpl {

    static double predictSample(List<Double> a, List<Sample> y, int filterOrder, int k) {
        List<Double> reversedA = new ArrayList<>(a);
        Collections.reverse(reversedA);

        double value = 0;
        for (int i = Math.max(filterOrder - k, 0); i < filterOrder; i++) {
            value += y.get(k - filterOrder + i).getValue() * reversedA.get(i);
        }
        return value;
    }

    static List<Sample> predictSamples(List<Double> a, List<Sample> initialValues, final int filterOrder, List<Double> errors) {
        final List<Sample> samples = new ArrayList<>(initialValues);

        for (int i = filterOrder; i < errors.size(); i++) {
            double value = predictSample(a, samples, filterOrder, i);
            value += errors.get(i);
            samples.add(new SampleImpl((int) Math.round(value)));
        }

        return samples;
    }
}
