/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author krzykwas
 */
public class WavOutputStreamImpl implements WavOutputStream {

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final OutputStream outputStream;
    private final AudioFormat audioFormat;

    public WavOutputStreamImpl(OutputStream outputStream, AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
        this.outputStream = outputStream;
    }

    @Override
    public void write(WavWindow window) {
        for (Sample sample : window.getSamples()) {
            final int data = sample.getValue();
            List<Integer> bytes = asBytes(data);

            Collections.reverse(bytes);

            for (Integer bajt : bytes) {
                byteArrayOutputStream.write(bajt);
            }
        }
    }

    @Override
    public void close() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, byteArrayOutputStream.size());
        AudioSystem.write(audioInputStream, Type.WAVE, outputStream);
    }

    @Override
    public List<Integer> asBytes(int data) {
        final int sampleSize = audioFormat.getSampleSizeInBits() / 8;
        final List<Integer> bytes = new ArrayList<>();

        int oldestBit = (data & (1 << (Integer.SIZE - 1))) >>> (Integer.SIZE - 1);
        int oldestByteMask = 0x7f << ((sampleSize - 1) * 8);
        int oldestByte = (data & oldestByteMask) >> ((sampleSize - 1) * 8);
        int bajt = (oldestBit << 7) | oldestByte;
        bytes.add(bajt);

        for (int i = sampleSize - 2; i >= 0; i--) {
            int mask = 0xff << (8 * i);
            bytes.add((data & mask) >> (8 * i));
        }

        return bytes;
    }
}
