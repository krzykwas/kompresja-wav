/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.List;

/**
 *
 * @author krzykwas
 */
public class CompressedPacketImpl implements CompressedPacket {

    private final List<Double> coefficients;
    private final List<Double> errors;

    public CompressedPacketImpl(List<Double> coefficients, List<Double> errors) {
        this.coefficients = coefficients;
        this.errors = errors;
    }

    @Override
    public List<Double> getCoefficients() {
        return coefficients;
    }

    @Override
    public List<Double> getErrors() {
        return errors;
    }
}
