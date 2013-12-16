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
package org.fao.fenix.wds.web.services;

import org.apache.axiom.om.*;
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.bean.WBBean;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.fao.fenix.wds.core.jdbc.WSConnector;
import org.fao.fenix.wds.core.utils.Wrapper;
import org.fao.fenix.wds.core.xml.XML2Bean;
import org.fao.fenix.wds.core.xml.XML2WorldBank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WDSWebService {

	private static final String NAMESPACE = "http://services.web.wds.fenix.fao.org/xsd";
	
	public OMElement querySynch(OMElement request) {
		
		try {
			
			// timing
			long t0 = System.currentTimeMillis();
			
			// extract payload
			String payloadRequest  = extractPayload(request);
//            System.out.println(payloadRequest);

            // create the beans for connection and query
			DBBean db = XML2Bean.convertDB(payloadRequest);
			SQLBean sql = XML2Bean.convertSQL(payloadRequest);
			
			// query the database
			List<List<String>> table = new ArrayList<List<String>>();
			switch (db.getConnection()) {
				case JDBC:
					table = JDBCConnector.query(db, sql, false);
				break;
				case WS: 
					String worldBankRest = XML2WorldBank.convertWorldBank(payloadRequest).toString();
					WBBean selects = XML2WorldBank.convertWorldBankSelects(payloadRequest);
					table = WSConnector.queryWorldBank(worldBankRest, selects); 
				break;
			}
			
			// wrap the result
			String payloadResponse = Wrapper.wrapAsXML(table).toString();
//            System.out.println(payloadResponse);

            // prepare the response
			OMFactory fac = OMAbstractFactory.getOMFactory();
			OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "ns");
			OMElement response = fac.createOMElement("querySynchResponse", omNs);
			OMElement value = fac.createOMElement("payload", omNs);
			value.addChild(fac.createOMText(value, payloadResponse));
			response.addChild(value);
			
			// timing
			long t1 = System.currentTimeMillis();
//			System.out.println("[WDS] - SERVICE - SOAP - " + (t1 - t0) + " - " + FieldParser.parseDate(new Date(), "FENIXAPPS"));
			
			// send the response
			return response;
			
		} catch (IllegalAccessException e) {
			return handleException(e.getMessage());
		} catch (InstantiationException e) {
			return handleException(e.getMessage());
		} catch (SQLException e) {
			return handleException(e.getMessage());
		} catch (ClassNotFoundException e) {
			return handleException(e.getMessage());
		}
		
	}
	
	private OMElement handleException(String message) {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "ns");
		OMElement response = fac.createOMElement("querySynchResponse", omNs);
		OMElement value = fac.createOMElement("payload", omNs);
		List<List<String>> table = new ArrayList<List<String>>();
		List<String> row = new ArrayList<String>();
		row.add(message);
		table.add(row);
		String payloadResponse = Wrapper.wrapAsXML(table).toString();
		value.addChild(fac.createOMText(value, payloadResponse));
		return response;
	}
	
	private String extractPayload(OMElement element) throws OMException {
		element.build();
		element.detach();
		OMElement payloadElement = element.getFirstElement();
		return payloadElement.getText();
	}
	
}