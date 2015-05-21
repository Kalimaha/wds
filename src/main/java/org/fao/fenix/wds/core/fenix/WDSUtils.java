package org.fao.fenix.wds.core.fenix;

import org.fao.fenix.wds.core.bean.DatasourceBean;

import javax.ws.rs.core.StreamingOutput;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public interface WDSUtils {

    List<String> create(DatasourceBean ds, String documents, String collection) throws Exception;

    StreamingOutput retrieve(final DatasourceBean ds, final String query, final String collection, final String outputType);

}