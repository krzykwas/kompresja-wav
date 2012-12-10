/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.eti.ksd.kompresjawav.engine;

/**
 *
 * @author krzykwas
 */
public class SampleImpl implements Sample {

    private int value;

    public SampleImpl() {
    }

    public SampleImpl(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.value;
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
        final SampleImpl other = (SampleImpl) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }
}
