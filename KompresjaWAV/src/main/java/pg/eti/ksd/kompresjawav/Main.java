/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private static final int FILTER_ORDER = 10;
    private static final int WINDOW_SIZE = 256;

    public static void main(String[] args) throws WavCompressException {
        if (args[0].equalsIgnoreCase("--kompresja")) {
            kompresuj(args[1], args[2]);
        } else if (args[0].equalsIgnoreCase("--dekompresja")) {
            dekompresuj(args[1], args[2]);
        } else {
            System.err.println("Wybierz którąś z opcji: --kompresja albo --dekompresja");
        }
    }

    private static void kompresuj(String wejscie, String wyjscie) throws WavCompressException {
        WavInputStream inputStream = null;
        ObjectOutputStream oos = null;

        try {
            final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(wejscie));
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bis);
            final AudioFormat format = audioInputStream.getFormat();

            inputStream = new WavInputStreamImpl(audioInputStream, WINDOW_SIZE, FILTER_ORDER, format.getFrameSize());
            oos = new ObjectOutputStream(new FileOutputStream(wyjscie));

            Coder coder = new CoderImpl(inputStream, FILTER_ORDER);

            for (CompressedPacket compressedPacket : coder) {
                oos.writeObject(compressedPacket);
            }
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static void dekompresuj(String wejscie, String wyjscie) {
        ObjectInputStream ois = null;
        WavOutputStream outputStream = null;

        try {
            ois = new ObjectInputStream(new FileInputStream(wejscie));

            AudioFormat audioFormat = new AudioFormat(11025, 16, 1, true, false);
            outputStream = new WavOutputStreamImpl(new FileOutputStream(wyjscie), audioFormat);

            Decoder decoder = new DecoderImpl(outputStream, FILTER_ORDER);

            while (true) {
                CompressedPacket compressedPacket = (CompressedPacket) ois.readObject();
                decoder.decode(compressedPacket);
            }
        } catch (EOFException ex) {
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
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
