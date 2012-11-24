/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav;

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
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new FileInputStream(""));
            stream = new StreamImpl(audioInputStream, 500, 10, audioInputStream.getFormat().getFrameSize());
            List<Sample> probki = stream.nextWindow();
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            stream.close();
        }
    }
}
