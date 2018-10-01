package gui;

//todo: add instance variable, then finish adding filler text to all 3.	

import java.util.concurrent.ThreadLocalRandom;

public abstract class TestCaseCreator {
	private static final int reqChar = 0;
	private static final int reqCharCount = 1;
	private static final int reqCharBeginning = 2;
	private static final int reqCharEnd = 3;
	private static final int reqCharBefore = 4;
	private static final int reqCharAfter = 5;
	private static final int reqCharThrows = 6;

	private static final int illChar = 0;
	private static final int illCharAlways = 1;
	private static final int illCharAtMost = 2;
	private static final int illCharBeginning = 3;
	private static final int illCharEnd = 4;
	private static final int illCharThrows = 5;
	
	private static StringBuilder testCases;
	private static String varName;
	private static String fillerText;
	private static String className;
	private static boolean isStatic;
	
	public static void StringTest(String dataString, boolean isPattern, 
			String name, String className, String isStatic) {
		testCases = new StringBuilder();
		varName = name;
		fillerText = "";
		TestCaseCreator.className = className;
		TestCaseCreator.isStatic = Boolean.valueOf(isStatic);
		
		if (!isPattern) {
			
			//For some reason, string.split isn't working, so manually split the string into 2.
			String tempReq = dataString.substring(0, dataString.indexOf("....."));
			String tempIll = dataString.substring(dataString.indexOf(".....") + 5);
			String[] allReq = tempReq.split("-----");
			String[] allIll = tempIll.split("-----");
			if (allIll.length > 1)
				stringCreateIllegalTests(allIll);
			if (allReq.length > 1)
				stringCreateRequiredTests(allReq);
			
		}
	}
	
	private static String stringCreateIllegalTests(String[] allIll) {
		String[][] illegalCharVals = new String[allIll.length][];
		
		for (int i = 0; i < allIll.length; i++)
			illegalCharVals[i] = allIll[i].split(":::::");
		
//		int randomNum;
//		boolean getFillerChar = false;
//		//Get filler char
//		while (!getFillerChar) {
//			fillerText = "";
//			getFillerChar = true;
//			
//			for (int i = 0; i < 8; i++) {
//				randomNum = ThreadLocalRandom.current().nextInt(45, 122 + 1);
//				fillerText += (char)randomNum;
//			}
//			
//			for (String[] illegalChar : illegalCharVals) 
//				for (int i = 0; i < fillerText.length(); i++)
//					if (illegalChar[illChar].equals(fillerText.charAt(i)))
//						getFillerChar = false;
//		}
		//Create test if never allowed.
		for (String[] illegalChar : illegalCharVals) {
			//Never allowed
			if (illegalChar[illCharAlways].equals("true")) {
				stringCreateFalseIllegalTestAlways(illegalChar);
				stringCreateTrueIllegalTestAlways(illegalChar);
			}
			
			//A max amount allowed
			if (Integer.parseInt(illegalChar[illCharAtMost]) > 0) {
				stringCreateFalseIllegalTestAtMost(illegalChar);
				stringCreateTrueIllegalTestAtMost(illegalChar);
				
			}
			
			//Char at beginning.
			if (illegalChar[illCharBeginning].equals("true")) 
				stringCreateIllegalTestAtBeg(illegalChar);
			
			if (illegalChar[illCharEnd].equals("true"))
				stringCreateIllegalTestAtEnd(illegalChar);
		}
		return "";
	}
	
	private static void stringCreateFalseIllegalTestAlways(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		testCase += "public void " + varName + "FalseIllChar" + illegalChar[illChar] + "() {\n" + 
				"    String temp = \"\"\n" + 
				"    for (int i = 0 ; i < fillerText.length(); i++) {\n" + 
				"        temp = fillerText\n" + 
				"        temp = temp.substring(0,i) + \"" + illegalChar[illChar] + "\" + temp.substring(i);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertFalse(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" + 
				"    }\n" + 
				"}\n\n\n";
		testCases.append(testCase);
	}
	
	private static void stringCreateTrueIllegalTestAlways(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		testCase += "public void " + varName + "TrueIllChar" + illegalChar[illChar] + "() {\n" + 
				"    String temp = fillerText;" + 
				"    assertEquals(temp, " + getSetAndGetPrefix() + ".get" + varName + "());\n" +
				"}\n\n\n";
		testCases.append(testCase);
	}
	
	private static void stringCreateFalseIllegalTestAtMost(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		
		testCase += "public void " + varName + "FalseIllChar" + illegalChar[illChar] + "AtMost() {\n" + 
				"    String temp = \"\"\n" + 
				"    int randInt = 0;\n" + 
				"    for (int i = 0 ; i < " + Integer.parseInt(illegalChar[illCharAtMost]) + " + 1; i++) {\n" +
				"        randInt = rand.nextInt(fillerLength);\n" + 
				"        temp = temp.substring(0,randInt) + \"" + illegalChar[illChar] + "\" + temp.substring(randInt);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"    }\n";
		stringNotExpectException(illegalChar);
		testCases.append(testCase);
	}
	
	private static void stringCreateTrueIllegalTestAtMost(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		
		testCase += "public void " + varName + "TrueIllChar" + illegalChar[illChar] + "AtMost() {\n" + 
				"    String temp = \"\"\n" + 
				"    int randInt = 0;\n" + 
				"    for (int i = 0 ; i < " + Integer.parseInt(illegalChar[illCharAtMost]) + "; i++) {\n" +
				"        randInt = rand.nextInt(fillerLength);\n" + 
				"        temp = fillerText\n" + 
				"        temp = temp.substring(0,randInt) + \"" + illegalChar[illChar] + "\" + temp.substring(randInt);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertEquals(temp, " + getSetAndGetPrefix() + ".get" + varName + "());\n" +
				"    }\n" + 
				"}\n\n\n";
		testCases.append(testCase);
	}
	
	private static void stringCreateIllegalTestAtBeg(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		testCase += "public void " + varName + "IllChar" + illegalChar[illChar] + "AtBeg() {\n" + 
				"    String temp = \"" + illegalChar[illChar] + "\" + fillerText;\n" +
				"    " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n";
		testCases.append(testCase);
		stringNotExpectException(illegalChar);
	}
	
	private static void stringCreateIllegalTestAtEnd(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		testCase += "public void " + varName + "IllChar" + illegalChar[illChar] + "AtEnd() {\n" + 
				"    String temp = fillerText + \"" + illegalChar[illChar] + "\";\n" +
				"    " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n";
		testCases.append(testCase);
		stringNotExpectException(illegalChar);
	}
	
	private static String stringCreateRequiredTests(String[] allReq) {
		String[][] requiredCharVals = new String[allReq.length][];
		
		for (int i = 0; i < allReq.length; i++)
			requiredCharVals[i] = allReq[i].split(":::::");
		
//		int randomNum;
//		boolean getFillerChar = false;
//		//Get filler char
//		while (!getFillerChar) {
//			fillerText = "";
//			getFillerChar = true;
//			
//			for (int i = 0; i < 8; i++) {
//				randomNum = ThreadLocalRandom.current().nextInt(45, 122 + 1);
//				fillerText += (char)randomNum;
//			}
//			
//			for (String[] illegalChar : requiredCharVals) 
//				for (int i = 0; i < fillerText.length(); i++)
//					if (illegalChar[illChar].equals(fillerText.charAt(i)))
//						getFillerChar = false;
//		}
		//Create test if never allowed.
		for (String[] requiredChar : requiredCharVals) {
			//Max allowed
			stringCreateFalseReqCount(requiredChar);
			stringCreateTrueReqCount(requiredChar);
						
			//Always required at beginning
			if (requiredChar[reqCharBeginning].equals("true"))
				stringCreateRequiredTestAtBeg(requiredChar);
			
			//Always required at end
			if (requiredChar[reqCharEnd].equals("true")) 
				stringCreateRequiredTestAtEnd(requiredChar);
			
			//Always required char before.
			if (!requiredChar[reqCharBefore].equals("N/A")) 
				stringCreateRequiredTestCharBefore(requiredChar);
			
			//Always required char before.
			if (!requiredChar[reqCharAfter].equals("N/A")) 
				stringCreateRequiredTestCharAfter(requiredChar);
		}
		return "";
	}
	
	private static void stringCreateFalseReqCount(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "FalseReqChar" + requiredChar[reqChar] + "() {\n" + 
				"    String temp = \"\"\n" + 
				"    int randInt = 0;\n" + 
				"    for (int i = 1; i < " + getRemaining(requiredChar) + " + 1; i++) {\n" +
				"        temp = fillerText;\n" +
				"        for (int j = 0 ; j < " + requiredChar[reqCharCount] + " - i; j++) {\n" + 
				"            randInt = rand.nextInt(fillerLength);\n" + 
				"            temp = temp.substring(0,randInt) + \"" + requiredChar[reqChar] + "\" + temp.substring(randInt);\n" +
				"            " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        }\n" +
				"        assertFalse(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" +
				"    }\n" +
				"}\n\n\n";
		testCases.append(testCase);
	}
	
	private static void stringCreateTrueReqCount(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "TrueReqChar" + requiredChar[reqChar] + "() {\n" + 
				"    String temp = fillerText\n" + 
				"    int randInt = 0;\n" + 
				"    for (int i = 0; i < " + getRemaining(requiredChar) + " + 1; i++) {\n" +
				"        randInt = rand.nextInt(fillerLength);\n" + 
				"        temp = temp.substring(0,randInt) + \"" + requiredChar[reqChar] + "\" + temp.substring(randInt);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"    }\n" +
				"    assertEquals(temp, " + getSetAndGetPrefix() + ".get" + varName + "());\n" +
				"}\n\n\n";
		testCases.append(testCase);
	}
	
	private static void stringCreateRequiredTestAtBeg(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "ReqChar" + requiredChar[reqChar] + "AtBeg() {\n" + 
				"    String temp = fillerText\n" + 
				"    for (int i = 1 ; i < fillerText.length(); i++) {\n" + 
				"        temp = fillerText\n" + 
				"        temp = temp.substring(0,i) + \"" + requiredChar[reqChar] + "\" + temp.substring(i);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertFalse(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" +
				"    }\n" +
				"}\n\n\n";
		testCases.append(testCase);
	}
	
	private static void stringCreateRequiredTestAtEnd(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "ReqChar" + requiredChar[reqChar] + "AtBeg() {\n" + 
				"    String temp = fillerText\n" + 
				"    for (int i = 0 ; i < fillerLength - 1; i++) \n" + 
				"        temp = fillerText\n" +  
				"        temp = temp.substring(0, i) + \"" + requiredChar[reqChar] + "\" + temp.substring(i);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertFalse(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" +
				"    \n" +
				"}\n\n\n";
		testCases.append(testCase);
	}
	
	private static void stringCreateRequiredTestCharBefore(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "ReqChar" + requiredChar[reqChar] + "CharBefore() {\n" + 
				"    String temp = fillerText\n" + 
				"    int randInt = 0;\n" + 
				"    for (int i = " + getCategoryStart(requiredChar) + "; i < " + (getCategoryStart(requiredChar) + getCategorySize(requiredChar)) + " - 1; i++) {\n" + 
				"        temp = fillerText\n" + 
				"        randInt = rand.nextInt(fillerLength - 1) + 1;\n" + 
				"        temp = temp.substring(0, randInt) + (char)i + " + requiredChar[reqChar] + "\" + temp.substring(randInt);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertTrue(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" + 
				"    }\n" +
				"}\n\n\n";
		testCases.append(testCase);
	}
	
	private static void stringCreateRequiredTestCharAfter(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "ReqChar" + requiredChar[reqChar] + "CharBefore() {\n" + 
				"    String temp = fillerText\n" + 
				"    int randInt = 0;\n" + 
				"    for (int i = " + getCategoryStart(requiredChar) + "; i < " + (getCategoryStart(requiredChar) + getCategorySize(requiredChar)) + " - 1; i++) {\n" + 
				"        temp = fillerText\n" + 
				"        randInt = rand.nextInt(fillerLength - 1) + 1;\n" + 
				"        temp = temp.substring(0, randInt) + " + requiredChar[reqChar] + "\" + (char)i + temp.substring(randInt);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertTrue(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" + 
				"    }\n" +
				"}\n\n\n";
		testCases.append(testCase);
	}
	
	private static void stringExpectException(String[] charToCheck) {
		if (charToCheck.length == 6)
			if (charToCheck[illCharThrows].equals("true")) {
				testCases.append("@Test(expected = Exception.class)\n");
			} else {
				testCases.append("@Test\n");
			}
		else if (charToCheck.length == 7) 
			if (charToCheck[reqCharThrows].equals("true")) {
				testCases.append("@Test(expected = Exception.class)\n");
			} else {
				testCases.append("@Test\n");
			}
	}
	
	private static void stringNotExpectException(String[] illegalChar) {
		if (!illegalChar[illCharThrows].equals("true")) {
			testCases.append("    assertFalse(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n");
		}
		testCases.append("}\n\n\n");
	}
	
	private static String getSetAndGetPrefix() {
		return (isStatic) ?  className : "obj";	
	}
	
	private static int getRemaining(String[] requiredChar) {
		int remaining = Integer.parseInt(requiredChar[reqCharCount]);
		if (requiredChar[reqCharBeginning].equals("true"))
			remaining--;
		if (requiredChar[reqCharEnd].equals("true"))
			remaining--;
		return remaining;
	}
//
//	private static String getCharBefore(String[] requiredChar) {
//		String temp = requiredChar[reqCharBefore];
//		if (temp.equals("None"))
//			temp = "";
//		else if (temp.equals("Any"))
//			temp = "" + (char)(ThreadLocalRandom.current().nextInt(45, 122 + 1));
//		else if (temp.equals("Any #"))
//			temp = "" + (char)(ThreadLocalRandom.current().nextInt(48, 57 + 1));
//		else if (temp.equals("Any char")) {
//			temp = ThreadLocalRandom.current().nextBoolean() ? 
//					"" + (char)ThreadLocalRandom.current().nextInt(65, 90 + 1) :
//					"" + (char)ThreadLocalRandom.current().nextInt(97, 122 + 1);
//		}
//		
//		return temp;
//	}
	
	private static int getCategoryStart(String[] requiredChar) {
		String temp = requiredChar[reqCharBefore];
		int start = 0;
		if (temp.equals("None"))
			start = 0;
		else if (temp.equals("Any"))
			start = 45;
		else if (temp.equals("Any #"))
			start = 48;
		else if (temp.equals("Any char")) {
			start = ThreadLocalRandom.current().nextBoolean() ? 65 : 97;
		}
		return start;
	}
	
	private static int getCategorySize(String[] requiredChar) {
		String temp = requiredChar[reqCharBefore];
		int size = 0;
		if (temp.equals("None"))
			size = 0;
		else if (temp.equals("Any"))
			size = 78;
		else if (temp.equals("Any #"))
			size = 10;
		else if (temp.equals("Any char")) {
			size = 26;
		}
		
		return size;
	}
	
	public static StringBuilder getTestCases() {
		return testCases;
	}
	
	
}