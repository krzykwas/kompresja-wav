/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author krzykwas
 */
public class CompressedPacketImpl implements CompressedPacket {

    public static final int QUANTIZATION_LEVELS = 4;
    /**
     * Maximum error
     */
    private final double eMax;
    private final List<Double> coefficients = new ArrayList<>();
    private final List<Long> errors = new ArrayList<>();

    public CompressedPacketImpl(List<Double> coefficients, List<Double> errors) {
        this.coefficients.addAll(coefficients);
        this.eMax = computeMaxError(errors);
        this.errors.addAll(compressErrors(errors));
    }

    @Override
    public List<Double> getCoefficients() {
        return coefficients;
    }

    @Override
    public List<Double> getErrors() {
        List<Double> quantizationLevels = computeQuantizationLevels();
        List<Double> uncompressed = new ArrayList<>();

        for (int i = errors.size() - 1; i >= 0; i--) {
            long packed = errors.get(i);

            for (int j = 0; j < Long.SIZE / 2; j++) {
                uncompressed.add(quantizationLevels.get((int) (packed & 0x3)));
                packed >>= 2;
            }
        }

        Collections.reverse(uncompressed);

        return uncompressed;
    }

    static double computeMaxError(List<Double> errors) {
        double max = Double.NEGATIVE_INFINITY;
        for (Double error : errors) {
            if (Math.abs(error) > max) {
                max = Math.abs(error);
            }
        }
        return max;
    }

    List<Double> computeQuantizationLevels() {
        List<Double> quantizedErrorLevels = new ArrayList<>();

        for (int i = 0; i < QUANTIZATION_LEVELS; i++) {
            quantizedErrorLevels.add(-eMax + i * 2.0 * eMax / (QUANTIZATION_LEVELS - 1));
        }

        return quantizedErrorLevels;
    }

    final List<Long> compressErrors(List<Double> errors) {
        final List<Long> result = new ArrayList<>();
        final List<Double> quantizationLevels = computeQuantizationLevels();
        long packed = 0;

        for (int i = 0; i < errors.size(); i++) {
            double error = errors.get(i);

            for (int j = 0; j < quantizationLevels.size(); j++) {
                if (Math.abs(error - quantizationLevels.get(j)) <= 2.0 * eMax / (QUANTIZATION_LEVELS - 1) / 2.0) {
                    packed <<= 2;
                    packed += j;
                    break;
                }
            }

            if (i % (Long.SIZE / 2) == (Long.SIZE / 2 - 1)) {
                result.add(packed);
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.eMax) ^ (Double.doubleToLongBits(this.eMax) >>> 32));
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
        if (Double.doubleToLongBits(this.eMax) != Double.doubleToLongBits(other.eMax)) {
            return false;
        }
        if (!Objects.equals(this.coefficients, other.coefficients)) {
            return false;
        }
        if (!Objects.equals(this.errors, other.errors)) {
            return false;
        }
        return true;
    }
}
