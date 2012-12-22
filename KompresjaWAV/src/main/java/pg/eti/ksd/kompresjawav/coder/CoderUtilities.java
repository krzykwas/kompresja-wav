/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.List;
import pg.eti.ksd.kompresjawav.stream.Sample;

/**
 *
 * @author krzykwas
 */
interface CoderUtilities {

    double predictSample(int k, List<Double> a, List<Sample> y);

    List<Sample> predictSamples(List<Double> a, List<Sample> initialValues, List<Double> errors);
}
