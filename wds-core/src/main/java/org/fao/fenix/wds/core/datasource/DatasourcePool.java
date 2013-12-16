package org.fao.fenix.wds.core.datasource;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.exception.WDSException;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class DatasourcePool {

    private String datasourcePath;

    public DatasourcePool(Resource datasourcePath) throws WDSException {
        try {
            this.setDatasourcePath(datasourcePath.getFile().getPath());
        } catch (IOException e) {
            throw new WDSException(e.getMessage());
        }
    }

    public void init() throws WDSException {
        File root = new File(this.datasourcePath);
        File[] files = root.listFiles();
        List<DatasourceBean> beans = new ArrayList<DatasourceBean>();
        Gson gson = new Gson();
        for (int i = 0 ; i < files.length; i++) {
            try {
                InputStream is = new FileInputStream(files[i].getAbsoluteFile());
                JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                reader.beginArray();
                while (reader.hasNext())
                    beans.add(readMessage(reader));
                reader.endArray();
            } catch (FileNotFoundException e) {
                throw new WDSException(e.getMessage());
            } catch (IOException e) {
                throw new WDSException(e.getMessage());
            }
        }
        for (int i = 0; i < beans.size(); i++) {
            DatasourceBean datasourceBean =  beans.get(i);
            System.out.println(datasourceBean);
        }
    }

    public DatasourceBean readMessage(JsonReader reader) throws IOException {
        DatasourceBean b = new DatasourceBean();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equalsIgnoreCase("driver")) {
                b.setDriver(reader.nextString());
            } else if (name.equalsIgnoreCase("url")) {
                b.setUrl(reader.nextString());
            } else if (name.equalsIgnoreCase("dbName")) {
                b.setDbName(reader.nextString());
            } else if (name.equalsIgnoreCase("username")) {
                b.setUsername(reader.nextString());
            } else if (name.equalsIgnoreCase("password")) {
                b.setPassword(reader.nextString());
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
