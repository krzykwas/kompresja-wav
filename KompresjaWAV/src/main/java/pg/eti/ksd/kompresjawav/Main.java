/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import pg.eti.ksd.kompresjawav.engine.Sample;
import pg.eti.ksd.kompresjawav.engine.Stream;
import pg.eti.ksd.kompresjawav.engine.StreamImpl;
import pg.eti.ksd.kompresjawav.exception.WavCompressException;

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

            do {
                List<Sample> samples = stream.nextWindow();

                if (samples.isEmpty()) {
                    break;
                }

                for (Sample sample : samples) {
                    System.out.print(sample.getValue() + " ");
                }
                System.out.println();
            } while (true);
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
