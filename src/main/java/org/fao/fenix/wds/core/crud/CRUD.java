package org.fao.fenix.wds.core.crud;

import org.fao.fenix.wds.core.bean.DatasourceBean;

import javax.ws.rs.core.StreamingOutput;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public interface CRUD {

    List<String> create(DatasourceBean ds, String documents, String collection) throws Exception;

    StreamingOutput retrieve(final DatasourceBean ds, final String query, final String collection, final String outputType);

    List<String> update(DatasourceBean ds, String query, String collection) throws Exception;

    List<String> delete(DatasourceBean ds, String query, String collection) throws Exception;

}