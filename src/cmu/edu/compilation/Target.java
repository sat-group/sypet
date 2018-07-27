package cmu.edu.compilation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;

public class Target {

//	public static String refactor(java.lang.String arg0, java.lang.String arg1, long arg2) {
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(arg1);
//		LocalDate localdate = LocalDate.parse(arg0, dtf);
//		LocalDate newdate = localdate.plusDays(arg2);
//		String output = newdate.format(dtf);
//		return output;
//	}
//	
	public static java.lang.String refactor(java.lang.String sypet_arg0, java.lang.String sypet_arg1, long sypet_arg2) throws Throwable{
	     java.time.format.DateTimeFormatter var_0 = java.time.format.DateTimeFormatter.ofPattern(sypet_arg1);
	     java.time.LocalDate var_1 = java.time.LocalDate.parse(sypet_arg0,var_0);
	     java.time.LocalDate var_2 = var_1.plusDays(sypet_arg2);
	     java.lang.String var_3 = var_2.format(var_0);
	     return var_3;
	}

	public static boolean test0() throws Throwable {
		return (refactor("2015/10/21", "yyyy/MM/dd",5).equals("2015/10/26"));
	}

	public static boolean test1() throws Throwable {
		return (refactor("2013/04/28", "yyyy/MM/dd",7).equals("2013/05/05"));
	}

	public static boolean test() throws Throwable {
		return test0() && test1();
	}

	public static void main(String[] args) {
		try {
			System.out.println("test = " + Target.test());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}