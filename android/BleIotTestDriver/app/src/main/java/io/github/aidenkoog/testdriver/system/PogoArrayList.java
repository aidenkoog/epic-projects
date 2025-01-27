package io.github.aidenkoog.testdriver.system;

import java.util.ArrayList;
import java.util.List;

interface PogoList extends List<Pogo> {

    public boolean addPogo(Pogo pogo);
}

public class PogoArrayList extends ArrayList<Pogo> implements PogoList {

    private static final String TAG = PogoArrayList.class.getSimpleName();

    public boolean addPogo(Pogo pogo) {
        for (Pogo p : PogoArrayList.this) {
            if (p.getAddress().equals(pogo.getAddress())) {
                p.setRssi(pogo.getRssi());
                return false;
            }
        }

        boolean ret = add(pogo);
        pogo.added();
        return ret;
    }

    public boolean removePogo(Object object) {
        boolean ret = remove(object);
        ((Pogo)object).removed();
        return ret;
    }

    @Override
    public void clear() {
        PogoArrayList pogos = new PogoArrayList();
        for (Pogo p : PogoArrayList.this) {
            pogos.add(p);
        }
        for (Pogo p : pogos) {
            removePogo(p);
        }
    }
}
