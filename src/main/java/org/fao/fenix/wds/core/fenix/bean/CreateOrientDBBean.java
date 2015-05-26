package org.fao.fenix.wds.core.fenix.bean;

import java.util.Map;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public class CreateOrientDBBean {

    private Map<String, Object>[] query;

    public Map<String, Object>[] getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object>[] query) {
        this.query = query;
    }

}