/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.ArrayList;
import java.util.List;
import pg.eti.ksd.kompresjawav.stream.Sample;
import pg.eti.ksd.kompresjawav.stream.WavWindow;

/**
 * Implemenetation of a Levinson-Durbin algorithm.
 *
 * @author krzykwas
 */
public class LevinsonDurbinImpl implements LevinsonDurbin {

    /**
     * Identifies coefficients of a linear forward predictor of order
     * filterOrder for a given window.
     *
     * @param window
     * @param filterOrder
     * @return list of coefficients of size equal to filterOrder
     */
    @Override
    public List<Double> identifyCoefficients(WavWindow window, int filterOrder) {
        final List<Sample> samples = window.getSamples();
        final List<Double> p = calculateAutocorrelationCoefficients(samples, filterOrder);
        final List<Double> a = new ArrayList<>();

        for (int i = 0; i < filterOrder; i++) {
            a.add(0.0);
        }

        a.set(0, 1.0);
        double k = -p.get(1) / p.get(0);
        a.set(1, k);

        double b = p.get(0) * (1 - k * k);

        for (int i = 2; i < filterOrder; i++) {
            double s = p.get(i);

            for (int j = 1; j <= i - 1; j++) {
                s += p.get(j) * a.get(i - j);
            }

            k = -s / b;

            List<Double> newA = new ArrayList<>();
            for (int j = 1; j <= i - 1; j++) {
                newA.add(a.get(j) + k * a.get(i - j));
            }
            newA.add(k);

            for (int j = 0; j < newA.size(); j++) {
                a.set(j, newA.get(j));
            }

            b *= 1 - k * k;
        }

        return a;
    }

    List<Double> calculateAutocorrelationCoefficients(List<Sample> window, int filterOder) {
        final List<Double> coefficients = new ArrayList<>();

        for (int i = 0; i < filterOder; i++) {
            double coefficient = 0;

            for (int j = 0; j < window.size() - i; j++) {
                coefficient += window.get(j).getValue() * window.get(j + i).getValue();
            }

            coefficients.add(coefficient);
        }

        return coefficients;
    }
}
