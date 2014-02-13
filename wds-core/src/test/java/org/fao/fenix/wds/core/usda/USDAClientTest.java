package org.fao.fenix.wds.core.usda;

import junit.framework.TestCase;
import org.fao.fenix.wds.core.exception.WDSException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class USDAClientTest extends TestCase {

    private final String WS_URL = "http://apps.fas.usda.gov/wsfapsd/svcPSD_Common.asmx";

    private final String ACTION = "http://www.fas.usda.gov/wsfaspsd/GetCommodities";

    public void testGetCommodities() {
        try {
            HttpURLConnection connection = buildConnection();
            OutputStream out = connection.getOutputStream();
            Writer wout = new OutputStreamWriter(out);
            writeRequest(wout);
            StringBuilder sb = new StringBuilder();
            BufferedReader in = null;
            if (connection.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine);
            in.close();
            System.out.println(sb);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new WDSException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new WDSException(e.getMessage());
        }
    }

    private HttpURLConnection buildConnection() throws MalformedURLException, IOException {
        URL u = new URL(WS_URL);
        URLConnection uc = u.openConnection();
        HttpURLConnection c = (HttpURLConnection) uc;
        c.setDoOutput(true);
        c.setDoInput(true);
        c.setRequestMethod("POST");
        c.setRequestProperty("action", ACTION);
        c.setRequestProperty("Content-Type", "text/xml");
        c.setRequestProperty("charset", "UTF-8");
        return c;
    }

    private void writeRequest(Writer wout) throws IOException {
        wout.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        wout.write("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        wout.write("<soap:Body>");
        wout.write("<GetCommodities xmlns=\"http://www.fas.usda.gov/wsfaspsd/\" />");
        wout.write("</soap:Body>");
        wout.write("</soap:Envelope>");
        wout.flush();
        wout.close();
    }

}