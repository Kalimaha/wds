package org.fao.fenix.wds.core.fenix.bean;

import java.util.Map;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public class RetrieveMongoDBBean {

    private Map<String,Object> query;

    private Map<String,Object> filters;

    private int limit;

    private Map<String,Object> sort;

    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Map<String, Object> getSort() {
        return sort;
    }

    public void setSort(Map<String, Object> sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "RetrieveMongoDBBean{" +
                "query=" + query +
                ", filters=" + filters +
                ", limit=" + limit +
                ", sort=" + sort +
                '}';
    }

}