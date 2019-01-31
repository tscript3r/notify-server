package pl.tscript3r.notify2.server.dataflow;

import org.apache.log4j.Logger;
import pl.tscript3r.notify2.server.domain.AdPackage;

import java.util.ArrayDeque;

public class AdQueue extends ArrayDeque<AdPackage> {

    private static final Logger log = Logger.getLogger(AdQueue.class);
    private static final long serialVersionUID = 1L;

    private static final AdQueue adQueue = new AdQueue();

    private AdQueue() {
    }

    public static AdQueue getInstance() {
        return adQueue;
    }

}