/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import pg.eti.ksd.kompresjawav.stream.Sample;

/**
 *
 * @author krzykwas
 */
public class CompressedPacketImpl implements CompressedPacket, Serializable {

    private static final int BITS_PER_VALUE = 4;
    public static final int QUANTIZATION_LEVELS = (int) Math.pow(2, BITS_PER_VALUE);
    /**
     * Maximum error
     */
    private float eMax;
    private List<Float> coefficients = new ArrayList<>();
    private List<Long> errors = new ArrayList<>();
    private List<Sample> initialValues = new ArrayList<>();

    public CompressedPacketImpl() {
    }

    public CompressedPacketImpl(List<Float> coefficients, List<Double> errors, List<Sample> initialValues) {
        this.coefficients.addAll(coefficients);
        this.eMax = computeMaxError(errors);
        this.errors.addAll(compressErrors(errors));
        this.initialValues.addAll(initialValues);
    }

    @Override
    public List<Float> getCoefficients() {
        return coefficients;
    }

    @Override
    public List<Double> getErrors() {
        final List<Double> quantizationLevels = computeQuantizationLevels(QUANTIZATION_LEVELS);
        final List<Double> uncompressed = new ArrayList<>();

        for (int i = errors.size() - 1; i >= 0; i--) {
            long packed = errors.get(i);

            for (int j = 0; j < Long.SIZE / BITS_PER_VALUE; j++) {
                long mask = (1L << BITS_PER_VALUE) - 1;
                final Double quantizationLevel = quantizationLevels.get((int) (packed & mask));
                uncompressed.add(quantizationLevel);
                packed >>= BITS_PER_VALUE;
            }
        }

        Collections.reverse(uncompressed);

        return uncompressed;
    }

    @Override
    public List<Sample> getInitialValues() {
        return initialValues;
    }

    static float computeMaxError(List<Double> errors) {
        double max = Float.NEGATIVE_INFINITY;
        for (double error : errors) {
            if (Math.abs(error) > max) {
                max = Math.abs(error);
            }
        }
        return (float) max;
    }

    List<Double> computeQuantizationLevels(final int quantizationLevels) {
        final List<Double> quantizedErrorLevels = new ArrayList<>();

        for (int i = 0; i < quantizationLevels; i++) {
            quantizedErrorLevels.add((double) eMax * (-quantizationLevels + 1 + 2 * i) / quantizationLevels);
        }

        return quantizedErrorLevels;
    }

    final List<Long> compressErrors(List<Double> errors) {
        final List<Long> result = new ArrayList<>();
        final List<Double> quantizationLevels = computeQuantizationLevels(QUANTIZATION_LEVELS);
        long packed = 0;

        for (int i = 0; i < errors.size(); i++) {
            double error = errors.get(i);

            for (int j = 0; j < quantizationLevels.size(); j++) {
                if (error <= -eMax + 2.0 * (j + 1) * eMax / QUANTIZATION_LEVELS) {
                    packed <<= BITS_PER_VALUE;
                    packed += j;
                    break;
                }
            }

            if (i % (Long.SIZE / BITS_PER_VALUE) == (Long.SIZE / BITS_PER_VALUE - 1)) {
                result.add(packed);
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Float.floatToIntBits(this.eMax);
        hash = 97 * hash + Objects.hashCode(this.coefficients);
        hash = 97 * hash + Objects.hashCode(this.errors);
        hash = 97 * hash + Objects.hashCode(this.initialValues);
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
        if (Float.floatToIntBits(this.eMax) != Float.floatToIntBits(other.eMax)) {
            return false;
        }
        if (!Objects.equals(this.coefficients, other.coefficients)) {
            return false;
        }
        if (!Objects.equals(this.errors, other.errors)) {
            return false;
        }
        if (!Objects.equals(this.initialValues, other.initialValues)) {
            return false;
        }
        return true;
    }
//    private void writeObject(ObjectOutputStream out) throws IOException {
//        out.writeFloat(eMax);
//
//        out.writeByte(coefficients.size());
//        for (Float coefficient : coefficients) {
//            out.writeFloat(coefficient);
//        }
//        for (Sample sample : initialValues) {
//            out.writeShort(sample.getValue());
//        }
//
//        out.writeShort(errors.size());
//        for (Long error : errors) {
//            out.writeLong(error);
//        }
//    }
//
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//        init();
//
//        eMax = in.readFloat();
//
//        int n = in.readByte();
//        for (int i = 0; i < n; i++) {
//            coefficients.add(in.readFloat());
//        }
//        for (int i = 0; i < n; i++) {
//            initialValues.add(new SampleImpl(in.readShort()));
//        }
//
//        n = in.readShort();
//        for (int i = 0; i < n; i++) {
//            errors.add(in.readLong());
//        }
//    }
}
