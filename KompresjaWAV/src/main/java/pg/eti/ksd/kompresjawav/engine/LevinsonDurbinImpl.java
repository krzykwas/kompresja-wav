/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public List<Double> identifyCoefficients(List<Sample> window, int filterOrder) {
        final List<Sample> flattened = flattenWindow(window);
        final List<Double> r = calculateAutocorrelationCoefficients(flattened, filterOrder);
        final Double[] coefficients = new Double[filterOrder];

        coefficients[0] = 1.0;
        double k = -r.get(1) / r.get(0);
        coefficients[1] = k;
        double a = r.get(0) * (1 - k * k);

        for (int i = 2; i < filterOrder; i++) {
            double s = r.get(i);

            for (int j = 1; j <= i - 1; j++) {
                s += r.get(j) * coefficients[i - j];
            }

            k = -s / a;

            final Double[] newCoefficients = new Double[filterOrder];

            for (int j = 1; j <= i - 1; j++) {
                newCoefficients[j] += coefficients[j] + k * coefficients[i - j];
            }

            newCoefficients[i] = k;
            a *= 1 - k * k;
            System.arraycopy(newCoefficients, 0, coefficients, 0, newCoefficients.length);
        }

        return new ArrayList<>(Arrays.asList(coefficients));
    }

    /**
     * Creates a formatting window of size size that flattens the samples'
     * window at its boundaries.
     *
     * @param size
     * @return formatting window
     */
    List<Double> createFormattingWindow(int size) {
        final List<Double> formattingWindow = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            formattingWindow.add(0.5 * (1 - Math.cos(2 * Math.PI * i / (size + 1))));
        }

        return formattingWindow;
    }

    List<Sample> flattenWindow(List<Sample> window) {
        final List<Sample> flattened = new ArrayList<>(window);
        final List<Double> formattingWindow = createFormattingWindow(window.size());

        for (int i = 0; i < flattened.size(); i++) {
            double value = flattened.get(i).getValue();
            flattened.get(i).setValue((int) Math.round(value * formattingWindow.get(i)));
        }

        return flattened;
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
