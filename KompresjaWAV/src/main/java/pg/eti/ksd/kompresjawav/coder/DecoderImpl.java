/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.ArrayList;
import java.util.List;
import pg.eti.ksd.kompresjawav.engine.CompressedPacket;
import pg.eti.ksd.kompresjawav.engine.Sample;
import pg.eti.ksd.kompresjawav.engine.SampleImpl;

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
    public List<Sample> decode(List<CompressedPacket> packets) {
        final List<Sample> result = new ArrayList<>();

        for (CompressedPacket packet : packets) {
            List<Double> coefficients = packet.getCoefficients();
            List<Double> errors = packet.getErrors();

            result.addAll(predictSamples(coefficients, errors));
        }

        return result;
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
