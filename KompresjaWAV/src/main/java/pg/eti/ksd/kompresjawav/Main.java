/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import pg.eti.ksd.kompresjawav.coder.Coder;
import pg.eti.ksd.kompresjawav.coder.CoderImpl;
import pg.eti.ksd.kompresjawav.coder.Decoder;
import pg.eti.ksd.kompresjawav.coder.DecoderImpl;
import pg.eti.ksd.kompresjawav.engine.CompressedPacket;
import pg.eti.ksd.kompresjawav.exception.WavCompressException;
import pg.eti.ksd.kompresjawav.stream.Stream;
import pg.eti.ksd.kompresjawav.stream.StreamImpl;
import pg.eti.ksd.kompresjawav.stream.WavWindow;

/**
 *
 * @author krzykwas
 */
public class Main {

    public static void main(String[] args) throws WavCompressException {
        Stream stream = null;
        try {
            final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(args[0]));
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bis);
            stream = new StreamImpl(audioInputStream, 256, 10, audioInputStream.getFormat().getFrameSize());
            Coder coder = new CoderImpl(stream, 10);
            Decoder decoder = new DecoderImpl(10);

            for (CompressedPacket compressedPacket : coder) {
                WavWindow uncompressed = decoder.decode(compressedPacket);
            }
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
