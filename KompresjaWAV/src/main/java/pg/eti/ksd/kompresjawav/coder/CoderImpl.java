/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pg.eti.ksd.kompresjawav.engine.CompressedPacket;
import pg.eti.ksd.kompresjawav.engine.CompressedPacketImpl;
import pg.eti.ksd.kompresjawav.engine.LevinsonDurbin;
import pg.eti.ksd.kompresjawav.engine.LevinsonDurbinImpl;
import pg.eti.ksd.kompresjawav.stream.Sample;
import pg.eti.ksd.kompresjawav.stream.WavInputStream;
import pg.eti.ksd.kompresjawav.stream.WavWindow;

/**
 *
 * @author krzykwas
 */
public class CoderImpl implements Coder {

    private final WavInputStream stream;
    private final int filterOrder;
    private final LevinsonDurbin levinsonDurbin = new LevinsonDurbinImpl();
    private final CoderUtilities coderUtilities = new CoderUtilitiesImpl();

    public CoderImpl(WavInputStream stream, int filterOrder) {
        this.stream = stream;
        this.filterOrder = filterOrder;
    }

    double computeError(int i, List<Float> coefficients, List<Sample> samples) {
        return samples.get(i).getValue() - coderUtilities.predictSample(i, coefficients, samples);
    }

    List<Double> computeErrors(List<Float> coefficients, List<Sample> samples) {
        final List<Double> errors = new ArrayList<>();

        for (int i = 0; i < samples.size(); i++) {
            double error = computeError(i, coefficients, samples);
            errors.add(error);
        }

        return errors;
    }

    @Override
    public Iterator<CompressedPacket> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return stream.hasNext();
    }

    @Override
    public CompressedPacket next() {
        WavWindow window = stream.next();
        List<Float> coefficients = levinsonDurbin.identifyCoefficients(window, filterOrder);
        List<Double> errors = computeErrors(coefficients, window.getSamples());
        List<Sample> initialValues = window.getSamples().subList(0, filterOrder);

        return new CompressedPacketImpl(coefficients, errors, initialValues);
    }

    @Override
    public void remove() {
    }
}
