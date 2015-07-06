/**
 *
 * FENIX (Food security and Early warning Network and Information Exchange)
 *
 * Copyright (c) 2011, by FAO of UN under the EC-FAO Food Security
 Information for Action Programme
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.fao.fenix.wds.core.datasource;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.constant.DRIVER;
import org.fao.fenix.wds.core.exception.WDSException;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class DatasourcePool {

    private String datasourcePath;

    private Map<String, DatasourceBean> datasources;

    /**
     * @param datasourcePath Directory where the datasources JSON file is stored.
     * @throws WDSException
     *
     * Custom constructor for the class, the <code>datasourcePath</code>
     * is injected by Spring.
     */
    public DatasourcePool(Resource datasourcePath) throws WDSException {
        datasources = new HashMap<String, DatasourceBean>();
        try {
            this.setDatasourcePath(datasourcePath.getFile().getPath());
        } catch (IOException e) {
            throw new WDSException(e.getMessage());
        }
    }

    /**
     * @param id Name of the datasource, e.g. 'FAOSTAT'
     * @return <code>DatasourceBean</code> containing all the parameters for the JDBC connection.
     */
    public DatasourceBean getDatasource(String id) {
        return this.datasources.get(id.toUpperCase());
    }

    /**
     * @throws FileNotFoundException
     * @throws IOException
     *
     * This method is invoked by Spring at the start-up. This function retrieves
     * all the files stored in the <code>datasourcePath</code> and populate the
     * <code>datasources</code> map where the key is the datasource name (e.g. 'FAOSTAT')
     * and the value is the <code>DatasourceBean</code>.
     */
    public void init() throws FileNotFoundException, IOException {
        Gson g = new Gson();
        File root = new File(this.datasourcePath);
        File[] files = root.listFiles();
        for (int i = 0; i < files.length; i++) {
            InputStream is = new FileInputStream(files[i].getAbsoluteFile());
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                DatasourceBean b = readMessage(reader);
                this.datasources.put(b.getId(), b);
            }
            reader.endArray();
        }
    }

    /**
     * @param reader    Google's <code>JsonReader</code>
     * @return <code>DatasourceBean</code> populated out of the JSON file
     * @throws IOException
     *
     * This method reads one of the objects of the JSON array through the
     * <code>JsonReader</code> and creates a <code>DatasourceBean</code>
     * out of it.
     */
    public DatasourceBean readMessage(JsonReader reader) throws IOException {
        DatasourceBean b = new DatasourceBean();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equalsIgnoreCase("driver")) {
                DRIVER d = DRIVER.valueOf(reader.nextString().toUpperCase());
                b.setDriver(d);
            } else if (name.equalsIgnoreCase("id")) {
                b.setId(reader.nextString());
            } else if (name.equalsIgnoreCase("url")) {
                b.setUrl(reader.nextString());
            } else if (name.equalsIgnoreCase("dbName")) {
                b.setDbName(reader.nextString());
            } else if (name.equalsIgnoreCase("username")) {
                b.setUsername(reader.nextString());
            } else if (name.equalsIgnoreCase("password")) {
                b.setPassword(reader.nextString());
            } else if (name.equalsIgnoreCase("create")) {
                b.setCreate(reader.nextBoolean());
            } else if (name.equalsIgnoreCase("retrieve")) {
                b.setRetrieve(reader.nextBoolean());
            } else if (name.equalsIgnoreCase("update")) {
                b.setUpdate(reader.nextBoolean());
            } else if (name.equalsIgnoreCase("delete")) {
                b.setDelete(reader.nextBoolean());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return b;
    }

    public void setDatasourcePath(String excelPath) {
        this.datasourcePath = excelPath;
    }

}