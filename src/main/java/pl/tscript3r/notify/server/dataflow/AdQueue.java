package pl.tscript3r.notify.server.dataflow;

import pl.tscript3r.notify.server.domain.AdPackage;

import java.util.ArrayDeque;

public class AdQueue extends ArrayDeque<AdPackage> {

    private static final long serialVersionUID = 1L;

    private static final AdQueue adQueue = new AdQueue();

    private AdQueue() {
    }

    public static AdQueue getInstance() {
        return adQueue;
    }

    protected Object readResolve() {
        return getInstance();
    }

}