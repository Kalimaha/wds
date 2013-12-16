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
package org.fao.fenix.wds.core.utils;

import org.fao.fenix.wds.core.bean.WBBean;
import org.fao.fenix.wds.core.constant.ERROR;
import org.fao.fenix.wds.core.constant.PAYLOAD;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.xml.XMLTools;
import org.json.simple.JSONArray;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class Wrapper {
	
//	private final static String CSS = "<style type='text/css'>#hor-minimalist-a {background: none repeat scroll 0 0 #FFFFFF;border-collapse: collapse;font-family: 'Lucida Sans Unicode','Lucida Grande',Sans-Serif;font-size: 12px;margin: 20px;text-align: left;width: 480px;}#hor-minimalist-a th {border-bottom: 2px solid #6678B1;color: #003399;font-size: 14px;font-weight: normal;padding: 10px 8px;}#hor-minimalist-a td {color: #666699;padding: 9px 8px 0;}#hor-minimalist-a tbody tr:hover td {color: #000099;}#hor-minimalist-b {background: none repeat scroll 0 0 #FFFFFF;border-collapse: collapse;font-family: 'Lucida Sans Unicode','Lucida Grande',Sans-Serif;font-size: 12px;margin: 20px;text-align: left;width: 480px;}#hor-minimalist-b th {border-bottom: 2px solid #6678B1;color: #003399;font-size: 14px;font-weight: normal;padding: 10px 8px;}#hor-minimalist-b td {border-bottom: 1px solid #CCCCCC;color: #666699;padding: 6px 8px;}#hor-minimalist-b tbody tr:hover td {color: #000099;}</style>";
	
	private final static String CSS = "<style type='text/css'>#hor-minimalist-b {background: none repeat scroll 0 0 #FFFFFF;border-collapse: collapse;font-family: 'Lucida Sans Unicode','Lucida Grande',Sans-Serif;font-size: 12px;margin: 20px;text-align: left;width: 480px; cursor: pointer;}#hor-minimalist-b th {border-bottom: 2px solid #6678B1;color: #003399;font-size: 14px;font-weight: normal;padding: 10px 8px;}#hor-minimalist-b td {border-bottom: 1px solid #CCCCCC;color: #666699;padding: 6px 8px;}#hor-minimalist-b tbody tr:hover td {color: #000099;}.hor-minimalist-b_row1 {background-color: #FFFFFF;color: #15428B;}.hor-minimalist-b_row2 {background-color: #FFFFFF;color: #15428B;}</style>";

//	private final static String ANALYTICS = "<script type=\"text/javascript\">var _gaq = _gaq || [];_gaq.push(['_setAccount', 'UA-29242261-1']);_gaq.push(['_trackPageview']);(function() {var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);})();</script>";
	
	private final static String ANALYTICS = "<script type=\"text/javascript\">var _gaq=_gaq||[];_gaq.push(['_setAccount','UA-30463742-1']);_gaq.push(['_trackPageview']);(function(){var ga=document.createElement('script');ga.type='text/javascript';ga.async=true;ga.src=('https:'==document.location.protocol?'https://ssl':'http://www')+'.google-analytics.com/ga.js';var s=document.getElementsByTagName('script')[0];s.parentNode.insertBefore(ga,s)})();</script>";
	
	private String excelsPath;
	
	private String ip;
	
	private String port;

    private boolean usePublicFolder;

    private String publicFolder;
	
	public Wrapper(Resource excel) throws WDSException {
		try {
			this.setExcelPath(excel.getFile().getPath());
		} catch (IOException e) {
			throw new WDSException(e.getMessage());
		}
	}

    public StringBuilder wrapAsExcel(List<List<String>> table, String filename) throws WDSException {
        try {
//            System.out.println("[wrapAsExcel] - START...");
            StringBuilder sb = new StringBuilder();
			ExcelFactory ef = new ExcelFactory();
            if (usePublicFolder) {
//                System.out.println("use public folder: " + publicFolder);
                sb.append(ef.createExcel(table, publicFolder, filename, this.getIp(), this.getPort()));
            } else {
//                System.out.println("standard implementation: " + excelsPath);
                sb.append(ef.createExcel(table, excelsPath, filename, this.getIp(), this.getPort()));
            }
//            System.out.println("[wrapAsExcel] - END");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<List<String>> wrapWorldBank(List<WBBean> bs, WBBean selects) throws WDSException {
		try {
			List<List<String>> ll = new ArrayList<List<String>>();
			if (selects.showHeaders())
				ll.add(buildWorldBankHeaders(selects));
			for (WBBean b : bs) {
				List<String> l = new ArrayList<String>();
				if (selects.showCountry())
					l.add(b.getCountryLabel());
				if (selects.showIndicator())
					l.add(b.getIndicatorCode());
				if (selects.showValue())
					l.add(b.getValue());
				if (selects.showDate())
					l.add(b.getDate());
				ll.add(l);
			}
			return ll;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<List<String>> wrapWorldBankCountries(List<WBBean> bs, WBBean selects) throws WDSException {
		try {
			List<List<String>> ll = new ArrayList<List<String>>();
			if (selects.showHeaders())
				ll.add(buildWorldBankHeaders(selects));
			for (WBBean b : bs) {
				List<String> l = new ArrayList<String>();
				l.add(b.getCountryCode());
				l.add(b.getCountryLabel());
				ll.add(l);
			}
			return ll;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<List<String>> wrapWorldBankIndicators(List<WBBean> bs, WBBean selects) throws WDSException {
		try {
			List<List<String>> ll = new ArrayList<List<String>>();
			if (selects.showHeaders())
				ll.add(buildWorldBankHeaders(selects));
			for (WBBean b : bs) {
				List<String> l = new ArrayList<String>();
				l.add(b.getIndicatorCode());
				l.add(b.getIndicatorLabel());
				ll.add(l);
			}
			return ll;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<String> buildWorldBankHeaders(WBBean selects) throws WDSException {
		try {
			List<String> l = new ArrayList<String>();
			if (selects.showCountry())
				l.add("Country");
			if (selects.showIndicator())
				l.add("Indicator");
			if (selects.showValue())
				l.add("Value");
			if (selects.showDate())
				l.add("Year");
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public StringBuilder wrapAsCSV(List<List<String>> table, String cellDelimiter, String rowDelimiter, boolean wrap, WrapperConfigurations c) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			DecimalFormat df = buildDecimalFormat(c);
			for (int i = 0 ; i < table.size() ; i++) {
				for (int j = 0 ; j < table.get(i).size() ; j++) {
					if (wrap) {
						sb.append("\"").append(table.get(i).get(j)).append("\"");
					} else {
//						sb.append(table.get(i).get(j));
						if (i > 0 && c.getValueColumnIndex() != null && j == c.getValueColumnIndex().intValue()) {
							Double value = Double.valueOf(table.get(i).get(j));
							sb.append("\"").append(df.format(value)).append("\"");
						} else {
							sb.append("\"").append(table.get(i).get(j)).append("\"");
						}
					}
					if (j < table.get(i).size() - 1)
						sb.append(cellDelimiter);
				}
				if (i < table.size() - 1)
					sb.append(rowDelimiter);
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder wrapAsXML(String message) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(PAYLOAD.PAYLOAD.name()).append(">");
			sb.append(XMLTools.wrap(message, PAYLOAD.ERROR.name()));
			sb.append("</").append(PAYLOAD.PAYLOAD.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder wrapAsHTML(String message) throws WDSException {
		return wrapAsHTML(message, "Error");
	}
	
	public static StringBuilder wrapAsHTML(String message, String title) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(PAYLOAD.HTML.name()).append(">");
			sb.append("<").append(PAYLOAD.HEAD.name()).append(">");
			sb.append(CSS);
			sb.append("<link rel='icon' type='image/png' href='http://ldvapp07.fao.org:8030/downloads/fao.png'></link>");
			sb.append("<TITLE>FENIX Web Data Server</TITLE>");
//			System.out.println(sb);
			sb.append(ANALYTICS);
			sb.append("</").append(PAYLOAD.HEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.BODY.name()).append(">");
			sb.append("<").append(PAYLOAD.TABLE.name()).append(" id='hor-minimalist-b'>");
			sb.append("<").append(PAYLOAD.THEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.TR.name()).append(">");
			sb.append("<").append(PAYLOAD.TH.name());
			sb.append(" scope='col'>").append(title);
			sb.append("</").append(PAYLOAD.TH.name()).append(">");
			sb.append("</").append(PAYLOAD.TR.name()).append(">");
			sb.append("</").append(PAYLOAD.THEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.TBODY.name()).append(">");
			sb.append("<").append(PAYLOAD.TR.name()).append(">");
			sb.append("<TD");
			sb.append(">").append(message).append("</TD>");
			sb.append("</").append(PAYLOAD.TR.name()).append(">");
			sb.append("</").append(PAYLOAD.TBODY.name()).append(">");
			sb.append("</").append(PAYLOAD.TABLE.name()).append(">");
			sb.append("</").append(PAYLOAD.BODY.name()).append(">");
			sb.append("</").append(PAYLOAD.HTML.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public StringBuilder wrapAsCSV(List<List<String>> table, WrapperConfigurations c) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			DecimalFormat df = buildDecimalFormat(c);
			for (int i = 0 ; i < table.size() ; i++) {
				for (int j = 0 ; j < table.get(i).size() ; j++) {
//					sb.append("\"").append(table.get(i).get(j)).append("\"");
					if (i > 0 && c.getValueColumnIndex() != null && j == c.getValueColumnIndex().intValue()) {
						Double value = Double.valueOf(table.get(i).get(j));
						sb.append("\"").append(df.format(value)).append("\"");
					} else {
						sb.append("\"").append(table.get(i).get(j)).append("\"");
					}
					if (j < table.get(i).size() - 1)
						sb.append(",");
				}
				if (i < table.size() - 1)
					sb.append("\n");
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static StringBuilder wrapAsJSON(List<List<String>> table) throws WDSException {
		try {
			JSONArray json = new JSONArray();
			for (List<String> l : table) {
				JSONArray row = new JSONArray();
				row.addAll(l);
				json.add(row);
			}
			return new StringBuilder(json.toJSONString());
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder wrapAsXML(List<List<String>> table) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(PAYLOAD.PAYLOAD.name()).append(">");
			sb.append("<").append(PAYLOAD.ROWS.name()).append(">");
			for (int i = 0 ; i < table.size() ; i++) {
				sb.append("<").append(PAYLOAD.ROW.name()).append(">");
				sb.append("<").append(PAYLOAD.CELLS.name()).append(">");
				for (int j = 0 ; j < table.get(i).size() ; j++) 
					sb.append(XMLTools.wrap(table.get(i).get(j).replace("&", "and"), PAYLOAD.CELL.name()));
				sb.append("</").append(PAYLOAD.CELLS.name()).append(">");
				sb.append("</").append(PAYLOAD.ROW.name()).append(">");
			}
			sb.append("</").append(PAYLOAD.ROWS.name()).append(">");
			sb.append("</").append(PAYLOAD.PAYLOAD.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	/**
	 * Mostly used for CountrySTAT, the XML is wrapped using column headers as tags.
	 */
	public static StringBuilder wrapAsXML2(List<List<String>> table) throws WDSException {
		try {
			List<String> headers = table.get(0);
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version='1.0'?>");
			sb.append("<").append(PAYLOAD.PAYLOAD.name()).append(">");
			sb.append("<").append(PAYLOAD.ROWS.name()).append(">");
			for (int i = 1 ; i < table.size() ; i++) {
				sb.append("<").append(PAYLOAD.ROW.name()).append(">");
				for (int j = 0 ; j < table.get(i).size() ; j++) {
					sb.append("<").append(headers.get(j)).append(">");
					sb.append(table.get(i).get(j).replace("&", "and"));
					sb.append("</").append(headers.get(j)).append(">");
				}
				sb.append("</").append(PAYLOAD.ROW.name()).append(">");
			}
			sb.append("</").append(PAYLOAD.ROWS.name()).append(">");
			sb.append("</").append(PAYLOAD.PAYLOAD.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public DecimalFormat buildDecimalFormat(WrapperConfigurations c) {
		if (c != null) {
			StringBuilder pattern = new StringBuilder();
			pattern.append("#,###");
			if (c.getDecimalSeparator() != null) {
				if (c.getDecimalNumbers() > 0)
					pattern.append(".");
				for (int i = 0 ; i < c.getDecimalNumbers() ; i++)
					pattern.append("0");
			}
			DecimalFormatSymbols customSymbols = new DecimalFormatSymbols();
			customSymbols.setDecimalSeparator(c.getDecimalSeparator().charAt(0));
			customSymbols.setGroupingSeparator(c.getThousandSeparator().charAt(0));
			DecimalFormat df = new DecimalFormat(pattern.toString(), customSymbols);
			return df;
		} else {
			return new DecimalFormat("#,###.00");
		}
	}
	
	public StringBuilder wrapAsHTML(List<List<String>> table, boolean nowrap, WrapperConfigurations c) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			DecimalFormat df = buildDecimalFormat(c);
			sb.append("<").append(PAYLOAD.HTML.name()).append(">");
			sb.append("<").append(PAYLOAD.HEAD.name()).append(">");
			// CSS
			if (c != null) {
				sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
				sb.append("http://").append(this.getIp()).append(":").append(this.getPort());
				sb.append("/wds/css/").append(c.getCssName().toLowerCase()).append(".css");
				sb.append("\">");
			} else {
				sb.append(CSS);
			}
			sb.append("<link rel='icon' type='image/png' href='http://ldvapp07.fao.org:8030/downloads/fao.png'></link>");
			sb.append("<TITLE>FENIX Web Data Server</TITLE>");
			sb.append(ANALYTICS);
			sb.append("</").append(PAYLOAD.HEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.BODY.name()).append(">");
			sb.append("<").append(PAYLOAD.TABLE.name()).append(" id='hor-minimalist-b'>");
			sb.append("<").append(PAYLOAD.THEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.TR.name()).append(">");
			for (int j = 0 ; j < table.get(0).size() ; j++) {
				sb.append("<").append(PAYLOAD.TH.name());
				if (nowrap)
					sb.append(" nowrap=''");
				if (c != null && c.getValueColumnIndex() != null && j == c.getValueColumnIndex().intValue()) {
					sb.append(" style='text-align: right;'");
				} else {
					sb.append(" style='text-align: left;'");
				}
				sb.append(" scope='col'>").append(table.get(0).get(j).replaceAll("_", " ")).append("</").append(PAYLOAD.TH.name()).append(">");
			}
			sb.append("</").append(PAYLOAD.TR.name()).append(">");
			sb.append("</").append(PAYLOAD.THEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.TBODY.name()).append(">");
			for (int i = 1 ; i < table.size() ; i++) {
				if (i % 2 == 0) {
					sb.append("<").append(PAYLOAD.TR.name()).append(" class='").append("hor-minimalist-b_row2").append("'>");
				} else {
					sb.append("<").append(PAYLOAD.TR.name()).append(" class='").append("hor-minimalist-b_row1").append("'>");
				}
				for (int j = 0 ; j < table.get(i).size() ; j++) {
					sb.append("<TD");
					if (nowrap)
						sb.append(" nowrap=''");
					if (c != null && c.getValueColumnIndex() != null && j == c.getValueColumnIndex().intValue()) {
						Double value = Double.valueOf(table.get(i).get(j));
						sb.append(" style='text-align: right;'>").append(df.format(value)).append("</TD>");
					} else {
						sb.append(" style='text-align: left;'>").append(table.get(i).get(j)).append("</TD>");
					}
				}
				sb.append("</").append(PAYLOAD.TR.name()).append(">");
			}
			sb.append("</").append(PAYLOAD.TBODY.name()).append(">");
			sb.append("</").append(PAYLOAD.TABLE.name()).append(">");
			sb.append("</").append(PAYLOAD.BODY.name()).append(">");
			sb.append("</").append(PAYLOAD.HTML.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public StringBuilder wrapAsHTML4FAOSTAT(List<List<String>> table, boolean nowrap, WrapperConfigurations c) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			DecimalFormat df = buildDecimalFormat(c);
			sb.append("<").append(PAYLOAD.HTML.name()).append(">");
			sb.append("<").append(PAYLOAD.HEAD.name()).append(">");
			// CSS
			if (c != null) {
				sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
				sb.append("http://").append(this.getIp()).append(":").append(this.getPort());
				sb.append("/wds/css/").append(c.getCssName().toLowerCase()).append(".css");
				sb.append("\">");
			} else {
				sb.append(CSS);
			}
			sb.append("<link rel='icon' type='image/png' href='http://ldvapp07.fao.org:8030/downloads/fao.png'></link>");
			sb.append("<TITLE>FENIX Web Data Server</TITLE>");
			sb.append(ANALYTICS);
			sb.append("</").append(PAYLOAD.HEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.BODY.name()).append(">");
			sb.append("<").append(PAYLOAD.TABLE.name()).append(" id='data' class='dataTable'>");
			sb.append("<").append(PAYLOAD.THEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.TR.name()).append(">");
			for (int j = 0 ; j < table.get(0).size() ; j++) {
				sb.append("<").append(PAYLOAD.TH.name());
				if (nowrap)
					sb.append(" nowrap=''");
				if (c.getValueColumnIndex() != null && j == c.getValueColumnIndex().intValue()) {
					sb.append(" style='text-align: center; font-weight: bold;'");
				} else {
					sb.append(" style='text-align: center; font-weight: bold;'");
				}
				sb.append(" scope='col'>").append(table.get(0).get(j).replaceAll("_", " ")).append("</").append(PAYLOAD.TH.name()).append(">");
			}
			sb.append("</").append(PAYLOAD.TR.name()).append(">");
			sb.append("</").append(PAYLOAD.THEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.TBODY.name()).append(">");
//			System.out.println("TABLE SIZE? " + table.size());
			if (table.size() > 1) {
				for (int i = 1 ; i < table.size() ; i++) {
					if (i % 2 == 0) {
						sb.append("<").append(PAYLOAD.TR.name()).append(" class='").append("hor-minimalist-b_row2").append("'>");
					} else {
						sb.append("<").append(PAYLOAD.TR.name()).append(" class='").append("hor-minimalist-b_row1").append("'>");
					}
					for (int j = 0 ; j < table.get(i).size() ; j++) {
						sb.append("<TD");
						if (nowrap)
							sb.append(" nowrap=''");
						if (c.getValueColumnIndex() != null && j == c.getValueColumnIndex().intValue()) {
							Double value = Double.valueOf(table.get(i).get(j));
							sb.append(" style='text-align: right;'>").append(df.format(value)).append("</TD>");
						} else {
							sb.append(" style='text-align: center;'>").append(table.get(i).get(j)).append("</TD>");
						}
					}
					sb.append("</").append(PAYLOAD.TR.name()).append(">");
				}
			} else {
				int span = table.get(0).size();
				sb.append("<").append(PAYLOAD.TR.name()).append(" class='").append("hor-minimalist-b_row2").append("'>");
				
				sb.append("<TD colspan='").append(span).append("' ");
				if (nowrap)
					sb.append(" nowrap=''");
				sb.append(" style='text-align: center;'>").append("No data available for your selection.").append("</TD>");
				
				sb.append("</").append(PAYLOAD.TR.name()).append(">");
			}
			sb.append("</").append(PAYLOAD.TBODY.name()).append(">");
			sb.append("</").append(PAYLOAD.TABLE.name()).append(">");
			sb.append("</").append(PAYLOAD.BODY.name()).append(">");
			sb.append("</").append(PAYLOAD.HTML.name()).append(">");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	public StringBuilder wrapAsHTML4FAOSTAT2(List<List<String>> table, boolean nowrap, WrapperConfigurations c) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			DecimalFormat df = buildDecimalFormat(c);
			sb.append("<").append(PAYLOAD.HTML.name()).append(">");
			sb.append("<").append(PAYLOAD.HEAD.name()).append(">");
			// CSS
			if (c != null) {
				sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
				sb.append("http://").append(this.getIp()).append(":").append(this.getPort());
				sb.append("/wds/css/").append(c.getCssName().toLowerCase()).append(".css");
				sb.append("\">");
			} else {
				sb.append(CSS);
			}
			sb.append("<link rel='icon' type='image/png' href='http://ldvapp07.fao.org:8030/downloads/fao.png'></link>");
			sb.append("<TITLE>FENIX Web Data Server</TITLE>");
			sb.append(ANALYTICS);
			sb.append("</").append(PAYLOAD.HEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.BODY.name()).append(">");
			sb.append("<").append(PAYLOAD.TABLE.name()).append(" id='data' class='dataTable'>");
			sb.append("<").append(PAYLOAD.THEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.TR.name()).append(">");
			for (int j = 0 ; j < table.get(0).size() ; j++) {
				sb.append("<").append(PAYLOAD.TH.name());
				if (nowrap)
					sb.append(" nowrap=''");
				if (c.getValuesColumnIndex() != null && checkValue(c.getValuesColumnIndex(), j)) {
					sb.append(" style='text-align: center; font-weight: bold;'");
				} else {
					sb.append(" style='text-align: center; font-weight: bold;'");
				}
				sb.append(" scope='col'>").append(table.get(0).get(j).replaceAll("_", " ")).append("</").append(PAYLOAD.TH.name()).append(">");
			}
			sb.append("</").append(PAYLOAD.TR.name()).append(">");
			sb.append("</").append(PAYLOAD.THEAD.name()).append(">");
			sb.append("<").append(PAYLOAD.TBODY.name()).append(">");
//			System.out.println("TABLE SIZE? " + table.size());
			if (table.size() > 1) {
				for (int i = 1 ; i < table.size() ; i++) {
					if (i % 2 == 0) {
						sb.append("<").append(PAYLOAD.TR.name()).append(" class='").append("hor-minimalist-b_row2").append("'>");
					} else {
						sb.append("<").append(PAYLOAD.TR.name()).append(" class='").append("hor-minimalist-b_row1").append("'>");
					}
					for (int j = 0 ; j < table.get(i).size() ; j++) {
						sb.append("<TD");
						if (nowrap)
							sb.append(" nowrap=''");
						// TODO: here is wrong
						if (c.getValuesColumnIndex() != null && checkValue(c.getValuesColumnIndex(), j)) {
							Double value = Double.valueOf(table.get(i).get(j));
							sb.append(" style='text-align: right;'>").append(df.format(value)).append("</TD>");
						} else {
							sb.append(" style='text-align: center;'>").append(table.get(i).get(j)).append("</TD>");
						}
					}
					sb.append("</").append(PAYLOAD.TR.name()).append(">");
				}
			} else {
				int span = table.get(0).size();
				sb.append("<").append(PAYLOAD.TR.name()).append(" class='").append("hor-minimalist-b_row2").append("'>");
				
				sb.append("<TD colspan='").append(span).append("' ");
				if (nowrap)
					sb.append(" nowrap=''");
				sb.append(" style='text-align: center;'>").append("No data available for your selection.").append("</TD>");
				
				sb.append("</").append(PAYLOAD.TR.name()).append(">");
			}
			sb.append("</").append(PAYLOAD.TBODY.name()).append(">");
			sb.append("</").append(PAYLOAD.TABLE.name()).append(">");
			sb.append("</").append(PAYLOAD.BODY.name()).append(">");
			sb.append("</").append(PAYLOAD.HTML.name()).append(">");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	private boolean checkValue(List<Integer> indexes, Integer value) {
		boolean check = false;
		for(Integer i : indexes) {
			if ( i == value ) {
				check =true;
				break;
			}
		}
		return check;
	}
	
	public StringBuilder wrapErrorAsHTML(List<List<String>> table) throws WDSException {
		
		try {
			
			// initiate result
			StringBuilder sb = new StringBuilder();
			
			// CSS
			sb.append("<html><header><title>R Web</title>").append(CSS).append("<link rel='icon' type='image/png' href='http://ldvapp07.fao.org:8030/downloads/fao.png'></head><body>");
			sb.append("<table class='general' id='hor-minimalist-b'>");
			
			for (int i = 0 ; i < table.size() ; i++) {
				ERROR output = ERROR.valueOf(table.get(i).get(0));
				switch (output) {
					case ERROR_HEADERS:
						sb.append("</").append(PAYLOAD.THEAD.name()).append(">");
						sb.append("<tr>");
						for (int j = 1 ; j < table.get(i).size() ; j++)
							sb.append("<th scope='col'>").append(table.get(i).get(j)).append("</th>");
						sb.append("</tr>");
						sb.append("<").append(PAYLOAD.THEAD.name()).append(">");
					break;
				}
			}
			
			sb.append("<").append(PAYLOAD.TBODY.name()).append(">");
			for (int i = 0 ; i < table.size() ; i++) {
				ERROR output = ERROR.valueOf(table.get(i).get(0));
				switch (output) {
					case HEADER:
						sb.append("<tr>");
						for (int j = 1 ; j < table.get(i).size() ; j++)
							sb.append("<td class='cell'><b>").append(table.get(i).get(j)).append("</b></td>");
						sb.append("</tr>");
					break;
					case CELL:
						sb.append("<tr>");
						for (int j = 1 ; j < table.get(i).size() ; j++)
							sb.append("<td class='cell'>").append(table.get(i).get(j)).append("</td>");
						sb.append("</tr>");
					break;
				}
			}
			sb.append("</").append(PAYLOAD.TBODY.name()).append(">");
			
			// close table
			sb.append("</table>");
			
			// close HTML
			sb.append("</body>");
			
			// return result
			return sb;
			
		} catch (Exception e) {
			return wrapAsHTML(table, false, null);
		}
		
	}
	
	public static List<List<String>> unwrapFromCSV(String csv, String cellDelimiter, String rowDelimiter) throws WDSException {
		try {
			List<List<String>> table = new ArrayList<List<String>>();
			StringTokenizer rows = new StringTokenizer(csv, rowDelimiter);
			while (rows.hasMoreTokens()) {
				List<String> rowList = new ArrayList<String>();
				String row = rows.nextToken();
				StringTokenizer cells = new StringTokenizer(row, cellDelimiter);
				while (cells.hasMoreTokens()) 
					rowList.add(cells.nextToken());
				table.add(rowList);
			}
			return table;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<List<String>> unwrapFromXML(String xml) throws WDSException {
		try {
			List<List<String>> table = new ArrayList<List<String>>();
			String[] tags = new String[]{PAYLOAD.ROWS.name(), PAYLOAD.ROW.name()};
			List<String> subs = XMLTools.subXML(xml, tags, PAYLOAD.PAYLOAD.name());
			for (String sub : subs) {
				List<String> row = new ArrayList<String>();
				List<String> ins = XMLTools.readSubCollection(sub, new String[]{PAYLOAD.ROW.name(), PAYLOAD.CELLS.name(), PAYLOAD.CELL.name()}, PAYLOAD.PAYLOAD.name());
				for (String in : ins)
					row.add(in);
				table.add(row);
			}
			return table;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static void print(List<List<String>> t) {
//		for (int i = 0 ; i < t.size() ; i++) {
//			for (int j = 0 ; j < t.get(i).size() ; j++) {
//				System.out.print(t.get(i).get(j));
//				if (j < t.get(i).size() - 1)
//					System.out.print(" | ");
//			}
//			System.out.println();
//		}
//		System.out.println();
	}
	
	public void setExcelPath(String excelPath) {
		this.excelsPath = excelPath;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getExcelsPath() {
		return excelsPath;
	}

    public void setPublicFolder(String publicFolder) {
        this.publicFolder = publicFolder;
    }

    public void setUsePublicFolder(boolean usePublicFolder) {
        this.usePublicFolder = usePublicFolder;
    }

}