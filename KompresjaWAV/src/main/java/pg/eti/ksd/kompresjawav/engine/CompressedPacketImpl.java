/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author krzykwas
 */
public class CompressedPacketImpl implements CompressedPacket {

    private final List<Double> coefficients = new ArrayList<>();
    private final List<Double> errors = new ArrayList<>();

    public CompressedPacketImpl(List<Double> coefficients, List<Double> errors) {
        this.coefficients.addAll(coefficients);
        this.errors.addAll(errors);
    }

    @Override
    public List<Double> getCoefficients() {
        return coefficients;
    }

    @Override
    public List<Double> getErrors() {
        return errors;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.coefficients);
        hash = 79 * hash + Objects.hashCode(this.errors);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompressedPacketImpl other = (CompressedPacketImpl) obj;
        if (!Objects.equals(this.coefficients, other.coefficients)) {
            return false;
        }
        if (!Objects.equals(this.errors, other.errors)) {
            return false;
        }
        return true;
    }
}
