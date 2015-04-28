package org.fao.fenix.wds.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class FieldParser {
	
	private static SimpleDateFormat formatterMinus_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	
	private static SimpleDateFormat formatterMinus_ddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");
	
	private static SimpleDateFormat formatterMinus_ddMMyy = new SimpleDateFormat("dd-MM-yy");
	
	private static SimpleDateFormat formatterSlash = new SimpleDateFormat("MM/dd/yyyy");
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");
	
	private static SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");
	
	private static SimpleDateFormat formatterMonthYear = new SimpleDateFormat("MM-yyyy");
	
	private static SimpleDateFormat formatterYearMonth = new SimpleDateFormat("yyyy-MM");
	
	private static SimpleDateFormat longDateFormatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
	
	private static SimpleDateFormat formatterSlashShort = new SimpleDateFormat("dd/MM/yy");
	
	private static SimpleDateFormat FPMU_01 = new SimpleDateFormat("dd/MM/yyyy");
	
	private static SimpleDateFormat FPMU_02 = new SimpleDateFormat("dd/MM/yy");
	
	private static SimpleDateFormat FPMU_03 = new SimpleDateFormat("dd-MMM");
	
	private static SimpleDateFormat FPMU_04 = new SimpleDateFormat("dd-MMM-yy");
	
	private static SimpleDateFormat FPMU_05 = new SimpleDateFormat("MMM-yy");
	
	private static SimpleDateFormat FPMU_06 = new SimpleDateFormat("MMMM-yy");
	
	private static SimpleDateFormat FPMU_07 = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static SimpleDateFormat FPMU_08 = new SimpleDateFormat("dd.MM.yyyy");
	
	private static SimpleDateFormat FENIXAPPS = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

	public static String parseDate(Date date, String dateFormat) {
		if (dateFormat.equals("YYYY-MM-DD")) {
			return formatterMinus_yyyyMMdd.format(date);
		} if (dateFormat.equals("DD-MM-YY")) {
			return formatterMinus_ddMMyy.format(date);
		} if (dateFormat.equals("MM-YYYY")) {
			return formatterMonthYear.format(date);
		} if (dateFormat.equals("YYYY")) {
			return formatterYear.format(date);
		} if (dateFormat.equals("DD/MM/YYYY")) {
			return FPMU_01.format(date);
		} if (dateFormat.equals("DD/MM/YY")) {
			return FPMU_02.format(date);
		} if (dateFormat.equals("DD-Mmm")) {
			return FPMU_03.format(date);
		} if (dateFormat.equals("DD-Mmm-YY")) {
			return FPMU_04.format(date);
		} if (dateFormat.equals("Mmm-YY")) {
			return FPMU_05.format(date);
		} if (dateFormat.equals("Month-YY")) {
			return FPMU_06.format(date);
		} if (dateFormat.equals("DD-Mmm-YYYY")) {
			return FPMU_07.format(date);
		} if (dateFormat.equals("DD.MM.YYYY")) {
			return FPMU_08.format(date);
		} if (dateFormat.equals("FENIXAPPS")) {
			return FENIXAPPS.format(date);
		}
		return null;
	}

	public static Date parseDateFPI(String dateString) {
		Date date = new Date();
		try {
			date = formatterMonthYear.parse(dateString);
		} catch (ParseException e) {
			return parseDate(dateString);
		}
		return date;
	}

	public static Date parseDate(String dateString) {
		try {
			if (dateString != null && !dateString.isEmpty()) {
				Date date = null;
				try {
					if("null".equals(dateString)) {
						return null;
					}
					else if ( dateString.indexOf("-") == 4 && dateString.length() == 7)
						date = formatterYearMonth.parse(dateString);
					else if ( dateString.indexOf("-") == 4 && dateString.length() == 10)
						date = formatterMinus_yyyyMMdd.parse(dateString);
					else if (dateString.indexOf("-") == 4)
						date = formatterMinus_yyyyMMdd.parse(dateString);
					else if (dateString.indexOf("/") > 0)
						date = formatterSlash.parse(dateString);
					// e.g. Wed Jan 01 00:00:00 CST 2003
					else if (dateString.length() == 4)
						date = formatterYear.parse(dateString);
					else if (dateString.indexOf(",") > 0)
						date = longDateFormatter.parse(dateString);
					else if (dateString.indexOf("-") == 2 && dateString.length()==7)
						date = formatterMonthYear.parse(dateString);
					else if (dateString.indexOf("-") == 2 && dateString.length()==10)
						date = formatterMinus_ddMMyyyy.parse(dateString);
					else
						date = formatter.parse(dateString);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return date;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String parseDate(Date date) {
		return formatterMinus_yyyyMMdd.format(date);
	}
	
	public static String date2ymd(Date date) {
		return formatterMinus_yyyyMMdd.format(date);
	}
	
	public static String dateDDMMYYmd(Date date) {
		return formatterMinus_ddMMyyyy.format(date);
	}
	
	public static String parseDDMMYYDate(Date date) {
		return formatterSlashShort.format(date);
	}

}