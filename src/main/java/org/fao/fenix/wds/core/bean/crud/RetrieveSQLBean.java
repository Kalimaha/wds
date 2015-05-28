package org.fao.fenix.wds.core.bean.crud;

import java.util.Map;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public class RetrieveSQLBean {

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "RetrieveSQLBean{" +
                "query='" + query + '\'' +
                '}';
    }

}