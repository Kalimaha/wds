package org.fao.fenix.wds.core.exception;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 *
 * This class is used to write exception on output streams of
 * REST services.
 *
 */
public class WDSExceptionStreamWriter {

    private static final Logger LOGGER = Logger.getLogger(WDSExceptionStreamWriter.class);

    public static void streamException(OutputStream os, String msg) throws IOException {
        LOGGER.error(msg);
        Writer writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(msg);
        writer.flush();
    }

}
