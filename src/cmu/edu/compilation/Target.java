package cmu.edu.compilation;

import java.io.IOException;

public class Target {
	public static boolean test() throws Throwable{
        java.util.Date date0 = new java.util.Date((-1), (-301), 0);
        int arg0 = date0.getYear();
        int arg1 = date0.getDate();
        int arg2 = date0.getMonth();
        System.out.println("getYear = " + arg0);
        System.out.println("getDate = " + arg1);
        System.out.println("getMonth = " + arg2);
        java.time.OffsetDateTime offsetdatetime0 = refactor(arg0, arg1, arg2);
        boolean b0 = offsetdatetime0.getDayOfYear() == arg0;
        boolean b1 = offsetdatetime0.getHour() == arg1;
        boolean b2 = offsetdatetime0.hashCode() == arg2;
        return b0 && b1 && b2;
    }

     public static java.time.OffsetDateTime refactor(int sypet_arg0, int sypet_arg1, int sypet_arg2) throws Throwable{
     java.time.LocalDateTime var_0 = java.time.LocalDateTime.now();
     java.time.ZoneOffset var_1 = java.time.ZoneOffset.ofHoursMinutesSeconds(sypet_arg0,sypet_arg1,sypet_arg2);
     java.time.OffsetDateTime var_2 = var_0.atOffset(var_1);
     return var_2;
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