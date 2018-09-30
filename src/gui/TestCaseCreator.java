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
	
	public static void StringTest(String dataString, boolean isPattern, String name) {
		testCases = new StringBuilder();
		varName = name;
		fillerText = "";
		if (!isPattern) {
			
			//For some reason, string.split isn't working, so manually split the string into 2.
			String tempReq = dataString.substring(0, dataString.indexOf("....."));
			String tempIll = dataString.substring(dataString.indexOf(".....") + 5);
			String[] allReq = tempReq.split("-----");
			String[] allIll = tempIll.split("-----");
			stringCreateIllegalTests(allIll);
			
		}
	}
	
	private static String stringCreateIllegalTests(String[] allIll) {
		String[][] illegalCharVals = new String[allIll.length][];
		
		for (int i = 0; i < allIll.length; i++)
			illegalCharVals[i] = allIll[i].split(":::::");
		
		int randomNum;
		boolean getFillerChar = false;
		//Get filler char
		while (!getFillerChar) {
			fillerText = "";
			getFillerChar = true;
			
			for (int i = 0; i < 8; i++) {
				randomNum = ThreadLocalRandom.current().nextInt(32, 122 + 1);
				fillerText += (char)randomNum;
			}
			
			for (String[] illegalChar : illegalCharVals) 
				for (int i = 0; i < fillerText.length(); i++)
					if (illegalChar[illChar].equals(fillerText.charAt(i)))
						getFillerChar = false;
		}
		//Create test if never allowed.
		for (String[] illegalChar : illegalCharVals) {
			
			//Never allowed
			if (illegalChar[illCharAlways].equals("true")) {
				stringCreateIllegalTestsAlways(illegalChar);
			}
			
			//A max amount allowed
			if (Integer.parseInt(illegalChar[illCharAtMost]) > 0) {
				//check to make sure it's not just required at beginning or end.
				if (getRemaining(illegalChar) > 0)
					stringCreateIllegalTestsAtMost(illegalChar);
			}
			
			if (illegalChar[illCharBeginning].equals("true")) {
				stringCreateIllegalTestsAtBeg(illegalChar);
			}
		}
		System.out.println(testCases);
		return "";
	}
	
	private static void stringCreateIllegalTestsAlways(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		testCase += "public void " + varName + "IllChar" + illegalChar[illChar] + "() {\n" + 
				"    String temp = \"\"\n" + 
				"    for (int i = 0 ; i < " + fillerText + ".length(); i++) {\n" + 
				"        temp += \"" + fillerText + "\"\n" + 
				"        temp = temp.substring(0,i) + \"" + illegalChar[illChar] + "\" + temp.substring(i);\n" +
				"        set" + varName + "(temp);\n" +
				"        assertFails(temp, get" + varName + "());\n";
		testCases.append(testCase);
		stringNotExpectException(illegalChar);
	}
	
	private static void stringCreateIllegalTestsAtMost(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		int remaining = getRemaining(illegalChar);
		
		testCase += "public void " + varName + "IllChar" + illegalChar[illChar] + "AtMost() {\n" + 
				"    String temp = \"\"\n" + 
				"    for (int i = 0 ; i < " + remaining + "; i++) {\n" + 
				"        temp += \"" + fillerText + "\"\n" + 
				"        temp += " + illegalChar[illChar] + ";\n" +
				"    }\n" +
				"    set" + varName + "(temp);\n\n";
		testCases.append(testCase);
		stringNotExpectException(illegalChar);
	}
	
	private static void stringCreateIllegalTestsAtBeg(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		int remaining = getRemaining(illegalChar);
		testCase += "public void " + varName + "IllChar" + illegalChar[illChar] + "AtBeg() {\n" + 
				"    String temp = \"" + illegalChar[illChar] + "\"\n" +
				"    for (int j = 0; j < rand.nextInt(5); j++)\n" +
				"            temp += (char)(rand.nextInt(122-32) + 32 + 1);\n\n" +
				"    for (int i = 0 ; i < " + remaining + "; i++) {\n" + 
				"        for (int j = 0; j < rand.nextInt(5); j++)\n" +
				"            temp += (char)(rand.nextInt(122-32) + 32 + 1);\n" + 
				"        temp += " + illegalChar[illChar] + ";\n" +
				"    }\n" +
				"    set" + varName + "(temp);\n\n";
		testCases.append(testCase);
		stringNotExpectException(illegalChar);
	}
	
	private static void stringExpectException(String[] illegalChar) {
		if (illegalChar[illCharThrows].equals("true")) {
			testCases.append("@Test(expected = Exception.class)\n");
		} else {
			testCases.append("@Test\n");
		}
	}
	
	private static void stringNotExpectException(String[] illegalChar) {
		if (!illegalChar[illCharThrows].equals("true")) {
			testCases.append("    assertFails(temp, get" + varName + "());\n");
		}
		testCases.append("}\n\n");
	}
	
	private static int getRemaining(String[] illegalChar) {
		int remaining = 0;
		if (illegalChar[illCharBeginning].equals("true")) 
			remaining++;
		if (illegalChar[illCharEnd].equals("true")) 
			remaining++;
		return Integer.parseInt(illegalChar[illCharAtMost]) - remaining;
	}
}