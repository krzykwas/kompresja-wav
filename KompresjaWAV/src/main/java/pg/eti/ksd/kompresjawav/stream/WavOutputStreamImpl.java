/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
        final int sampleSize = audioFormat.getSampleSizeInBits() / 8;
        final int byteSize = Integer.SIZE / 8;

        for (Sample sample : window.getSamples()) {
            int data = sample.getValue();
            data *= (1 << (8 * (byteSize - sampleSize)));

            for (int i = 0; i < byteSize - sampleSize; i++) {
                data >>= 8;
            }

            for (int i = sampleSize - 1; i >= 0; i--) {
                int bajt = data & (0xff << 8 * i);
                bajt >>= 8 * i;

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
}
