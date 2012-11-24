/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import pg.eti.ksd.kompresjawav.engine.Sample;
import pg.eti.ksd.kompresjawav.engine.Stream;
import pg.eti.ksd.kompresjawav.engine.StreamImpl;

/**
 *
 * @author krzykwas
 */
public class Main {

    public static void main(String[] args) {
        Stream stream = null;
        try {
            stream = new StreamImpl(AudioSystem.getAudioInputStream(new FileInputStream("")), 500, 10);
            List<Sample> probki = stream.nextWindow();
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            stream.close();
        }
    }
}
