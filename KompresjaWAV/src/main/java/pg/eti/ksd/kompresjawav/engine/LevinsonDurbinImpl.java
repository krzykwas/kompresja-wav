/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author krzykwas
 */
public class LevinsonDurbinImpl {

    /**
     * Identifies coefficients of a linear forward predictor of order
     * filterOrder for a given window.
     *
     * @param window
     * @param filterOrder
     * @return list of coefficients of size equal to filterOrder
     */
    public List<Double> identifyCoefficients(List<Sample> window, int filterOrder) {
        List<Sample> flattened = flattenWindow(window);

        return new ArrayList<>();
    }

    /**
     * Creates a formatting window of size size that flattens the samples'
     * window at its boundaries.
     *
     * @param size
     * @return formatting window
     */
    List<Double> createFormattingWindow(int size) {
        List<Double> formattingWindow = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            formattingWindow.add(0.5 * (1 - Math.cos(2 * Math.PI * i / (size + 1))));
        }

        return formattingWindow;
    }

    private List<Sample> flattenWindow(List<Sample> window) {
        final List<Sample> flattened = new ArrayList<>(window);
        final List<Double> formattingWindow = createFormattingWindow(window.size());

        for (int i = 0; i < flattened.size(); i++) {
            double value = flattened.get(i).getValue();
            flattened.get(i).setValue((int) (value * formattingWindow.get(i)));
        }

        return flattened;
    }
}
