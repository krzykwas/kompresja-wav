/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.ArrayList;
import java.util.List;
import pg.eti.ksd.kompresjawav.engine.CompressedPacket;
import pg.eti.ksd.kompresjawav.stream.Sample;
import pg.eti.ksd.kompresjawav.stream.SampleImpl;
import pg.eti.ksd.kompresjawav.stream.WavWindow;
import pg.eti.ksd.kompresjawav.stream.WavWindowImpl;

/**
 *
 * @author krzykwas
 */
public class DecoderImpl implements Decoder {

    private final int filterOrder;

    public DecoderImpl(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    @Override
    public WavWindow decode(CompressedPacket packet) {
        WavWindow window = new WavWindowImpl();
        List<Double> coefficients = packet.getCoefficients();
        List<Double> errors = packet.getErrors();
        window.getSamples().addAll(predictSamples(coefficients, errors));

        return window;
    }

    List<Sample> predictSamples(List<Double> coefficients, List<Double> errors) {
        final List<Sample> samples = new ArrayList<>();

        for (int i = 0; i < errors.size(); i++) {
            double value = CoderUtilitiesImpl.predictSample(coefficients, samples, i, filterOrder);
            value += errors.get(i);

            samples.add(new SampleImpl((int) Math.round(value)));
        }

        return samples;
    }
}
