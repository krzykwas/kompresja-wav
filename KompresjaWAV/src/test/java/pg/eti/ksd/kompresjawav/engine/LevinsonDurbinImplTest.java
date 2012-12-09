/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krzykwas
 */
public class LevinsonDurbinImplTest {

    private LevinsonDurbinImpl sut;

    @Before
    public void setUp() {
        sut = new LevinsonDurbinImpl();
    }

    @Test
    public void createFormattingWindow_withSize0_returnsAnEmptyList() {
        List<Double> formattingWindow = sut.createFormattingWindow(0);
        assertSame(0, formattingWindow.size());
    }

    @Test
    public void createFormattingWindow_withSize1_returnsAListOfSize1() {
        List<Double> formattingWindow = sut.createFormattingWindow(1);
        assertSame(1, formattingWindow.size());
    }
}
