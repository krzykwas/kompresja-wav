/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import pg.eti.ksd.kompresjawav.coder.Coder;
import pg.eti.ksd.kompresjawav.coder.CoderImpl;
import pg.eti.ksd.kompresjawav.coder.Decoder;
import pg.eti.ksd.kompresjawav.coder.DecoderImpl;
import pg.eti.ksd.kompresjawav.engine.CompressedPacket;
import pg.eti.ksd.kompresjawav.exception.WavCompressException;
import pg.eti.ksd.kompresjawav.stream.WavInputStream;
import pg.eti.ksd.kompresjawav.stream.WavInputStreamImpl;
import pg.eti.ksd.kompresjawav.stream.WavOutputStream;
import pg.eti.ksd.kompresjawav.stream.WavOutputStreamImpl;

/**
 *
 * @author krzykwas
 */
public class Main {

    public static void main(String[] args) throws WavCompressException {
        WavInputStream inputStream = null;
        WavOutputStream outputStream = null;
        final int FILTER_ORDER = 10;
        final int WINDOW_SIZE = 256;

        try {
            final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(args[0]));
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bis);
            final AudioFormat format = audioInputStream.getFormat();
            inputStream = new WavInputStreamImpl(audioInputStream, WINDOW_SIZE, FILTER_ORDER, format.getFrameSize());
            outputStream = new WavOutputStreamImpl(new FileOutputStream(args[1]), format);

            Coder coder = new CoderImpl(inputStream, FILTER_ORDER);
            Decoder decoder = new DecoderImpl(outputStream, FILTER_ORDER);

            for (CompressedPacket compressedPacket : coder) {
                decoder.decode(compressedPacket);
            }
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
