package cmu.edu.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RefactorExample {

	public static void main(String[] args) throws IOException {
		
		if (args.length != 1) {
			System.out.println("Error: wrong number of arguments= " + args.length);
			System.out.println("Usage: ant refactor -Dargs=\"<int>\"");
			System.out.println("int = {2,3,4} -> number of fixed loc");
			System.exit(0);
		}

		ArrayList<String> packages = new ArrayList<String>(Arrays.asList("java.time"));
		ArrayList<String> libs = new ArrayList<String>(Arrays.asList("./lib/rt8.jar"));
		UISyPet sypet = new UISyPet(packages, libs);
		String methodName = "refactor";
		ArrayList<String> paramNames = new ArrayList<String>(Arrays.asList("sypet_arg0", "sypet_arg1", "sypet_arg2"));

		ArrayList<String> srcTypes = new ArrayList<String>(
				Arrays.asList("java.lang.String", "java.lang.String", "long"));

		String tgtType = "java.lang.String";
		
		String testCode = "public static boolean test0() throws Throwable {\n"+
			"return (refactor(\"2015/10/21\", \"yyyy/MM/dd\",5).equals(\"2015/10/26\"));}\n"+
			"public static boolean test1() throws Throwable {\n"+
			"return (refactor(\"2013/04/28\", \"yyyy/MM/dd\",7).equals(\"2013/05/05\"));}\n"+
			"public static boolean test() throws Throwable {\n"+
			"return test0() && test1();}\n";

		sypet.setSignature(methodName, paramNames, srcTypes, tgtType, testCode);
		if (Integer.valueOf(args[0]) >= 2){
			System.out.println("c Fixing method= \"ofPattern\"");
			System.out.println("c Fixing method= \"parse\"");
			sypet.addAtLeastK("(static)java.time.format.DateTimeFormatter.ofPattern(java.lang.String )java.time.format.DateTimeFormatter",1);
			sypet.addAtLeastK("(static)java.time.LocalDate.parse(java.lang.CharSequence java.time.format.DateTimeFormatter )java.time.LocalDatePoly:(java.lang.String java.time.format.DateTimeFormatter )", 1);
		}
		if (Integer.valueOf(args[0]) >= 3){
			System.out.println("c Fixing method= \"format\"");
			sypet.addAtLeastK("java.time.LocalDate.format(java.time.LocalDate java.time.format.DateTimeFormatter )java.lang.String", 1);	
		}
		if (Integer.valueOf(args[0]) >= 4){
			System.out.println("c Fixing method= \"plusDays\"");
			sypet.addAtLeastK("java.time.LocalDate.plusDays(java.time.LocalDate long )java.time.LocalDate", 1);
		}
		
		//sypet.addAtLeastK("(static)java.time.format.DateTimeFormatter.ofPattern(java.lang.String )java.time.format.DateTimeFormatter",1);
		//sypet.addAtLeastK("(static)java.time.LocalDate.parse(java.lang.CharSequence java.time.format.DateTimeFormatter )java.time.LocalDatePoly:(java.lang.String java.time.format.DateTimeFormatter )", 1);
		//sypet.addAtLeastK("java.time.LocalDate.plusDays(java.time.LocalDate long )java.time.LocalDate", 1);
		//sypet.addAtLeastK("java.time.LocalDate.format(java.time.LocalDate java.time.format.DateTimeFormatter )java.lang.String", 1);
		String code = sypet.synthesize(5);
		System.out.println("code = " + code);

	}

}
