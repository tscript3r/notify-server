package pl.tscript3r.notify.server.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoader {

    public String readFromJARFile(String filename) throws IOException {
        InputStream is = getClass().getResourceAsStream(filename);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();
        isr.close();
        is.close();
        return sb.toString();
    }

}
