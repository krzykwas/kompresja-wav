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
public class CoderUtilitiesImpl implements CoderUtilities {

    @Override
    public double predictSample(int k, List<Double> a, List<Sample> y) {
        List<Double> reversedA = new ArrayList<>(a);
        Collections.reverse(reversedA);
        final int n = reversedA.size();

        double value = 0;
        for (int i = Math.max(n - k, 0); i < n; i++) {
            value -= y.get(k - n + i).getValue() * reversedA.get(i);
        }

        return value;
    }

    @Override
    public List<Sample> predictSamples(List<Double> a, List<Sample> initialValues, List<Double> errors) {
        final List<Sample> samples = new ArrayList<>(initialValues);

        for (int i = a.size(); i < errors.size(); i++) {
            double value = predictSample(i, a, samples);
            value += errors.get(i);
            samples.add(new SampleImpl((int) Math.round(value)));
        }

        return samples;
    }
}
