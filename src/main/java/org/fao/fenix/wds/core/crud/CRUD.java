package org.fao.fenix.wds.core.crud;

import org.fao.fenix.wds.core.bean.DatasourceBean;

import javax.ws.rs.core.StreamingOutput;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public interface CRUD {

    String ERROR_MESSAGE = "\"The submitted payload is wrong. Please refer to the project's <a target='_blank' href='https://github.com/FENIX-Platform/wds/wiki'>Wiki</a> for further details.\"";

    List<String> create(DatasourceBean ds, String documents, String collection) throws Exception;

    StreamingOutput retrieve(DatasourceBean ds, String query, String collection, String outputType) throws Exception;

    List<String> update(DatasourceBean ds, String query, String collection) throws Exception;

    List<String> delete(DatasourceBean ds, String query, String collection) throws Exception;

}