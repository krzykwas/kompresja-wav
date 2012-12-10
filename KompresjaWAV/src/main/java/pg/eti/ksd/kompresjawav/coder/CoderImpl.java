/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.ArrayList;
import java.util.List;
import pg.eti.ksd.kompresjawav.engine.CompressedPacket;
import pg.eti.ksd.kompresjawav.engine.CompressedPacketImpl;
import pg.eti.ksd.kompresjawav.engine.LevinsonDurbin;
import pg.eti.ksd.kompresjawav.engine.LevinsonDurbinImpl;
import pg.eti.ksd.kompresjawav.engine.Sample;
import pg.eti.ksd.kompresjawav.engine.Stream;

/**
 *
 * @author krzykwas
 */
public class CoderImpl implements Coder {

    private final Stream stream;
    private final int filterOrder;
    private final LevinsonDurbin levinsonDurbin = new LevinsonDurbinImpl();

    public CoderImpl(Stream stream, int filterOrder) {
        this.stream = stream;
        this.filterOrder = filterOrder;
    }

    @Override
    public List<CompressedPacket> encode() {
        final List<CompressedPacket> result = new ArrayList<>();

        while (true) {
            List<Sample> window = stream.nextWindow();

            if (window.isEmpty()) {
                break;
            }

            List<Double> coefficients = levinsonDurbin.identifyCoefficients(window, filterOrder);
            List<Double> errors = computeErrors(coefficients, window);

            result.add(new CompressedPacketImpl(coefficients, errors));
        }

        return result;
    }

    double computeError(List<Double> coefficients, List<Sample> samples, int k) {
        return samples.get(k).getValue() - CoderUtilitiesImpl.predictSample(coefficients, samples, k, filterOrder);
    }

    List<Double> computeErrors(List<Double> coefficients, List<Sample> samples) {
        final List<Double> errors = new ArrayList<>();

        for (int i = 0; i < samples.size(); i++) {
            errors.add(computeError(coefficients, samples, i));
        }

        return errors;
    }
}
