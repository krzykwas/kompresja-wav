/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.coder;

import java.util.List;
import pg.eti.ksd.kompresjawav.engine.CompressedPacket;
import pg.eti.ksd.kompresjawav.stream.Sample;
import pg.eti.ksd.kompresjawav.stream.WavOutputStream;
import pg.eti.ksd.kompresjawav.stream.WavWindow;
import pg.eti.ksd.kompresjawav.stream.WavWindowImpl;

/**
 *
 * @author krzykwas
 */
public class DecoderImpl implements Decoder {

    private final int filterOrder;
    private final WavOutputStream outputStream;
    private final CoderUtilities coderUtilities;

    public DecoderImpl(WavOutputStream outputStream, int filterOrder) {
        this.outputStream = outputStream;
        this.filterOrder = filterOrder;
        this.coderUtilities = new CoderUtilitiesImpl();
    }

    @Override
    public WavWindow decode(CompressedPacket packet) {
        final List<Double> coefficients = packet.getCoefficients();
        final List<Double> errors = packet.getErrors();
        final List<Sample> initialValues = packet.getInitialValues();
        final List<Sample> predictedSamples = coderUtilities.predictSamples(coefficients, initialValues, errors);

        WavWindow streamWindow = new WavWindowImpl();
        streamWindow.getSamples().addAll(predictedSamples.subList(filterOrder, predictedSamples.size()));
        outputStream.write(streamWindow);

        return streamWindow;
    }
}
