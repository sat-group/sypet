package cmu.edu.compilation;

import java.io.IOException;

public class Target {
	public static java.time.LocalDate convert(int sypet_arg0) throws Throwable {
		java.time.LocalDate var_0 = java.time.LocalDate.ofYearDay(sypet_arg0,sypet_arg0);
		return var_0;
	}

	@SuppressWarnings("deprecation")
	public static boolean test() throws Throwable {
		java.util.Date date0 = new java.util.Date(1737, 252, 1876);
		int month = date0.getMonth();
		java.time.LocalDate localdate0 = convert(month);
		System.out.println("month = " + month);
		System.out.println("localdate0 = " + localdate0.getMonthValue());
		return localdate0.getMonthValue() == month;
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