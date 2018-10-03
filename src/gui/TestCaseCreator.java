package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//todo: add method to get key from here, send to varspanel, varspanel can check the key values here.

import java.util.concurrent.ThreadLocalRandom;
public class TestCaseCreator {
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
	private static Map<Integer, String> allTestCases = new HashMap<Integer, String>();
	
	private StringBuilder tempTestCases;
	private String varName;
	private String className;
	private boolean isStatic;
	
	public TestCaseCreator(String dataString, boolean isPattern, 
			String name, String className, String isStatic, int row) {
		
		tempTestCases = new StringBuilder();
		varName = name;
		this.className = className;
		this.isStatic = Boolean.valueOf(isStatic);
		String tempIll;
		String tempReq;
		if (valueAlreadyExists(row))
			allTestCases.remove(row);
		
		
		if (!isPattern) {
			//For some reason, string.split isn't working, so manually split the string into 2.
			if (dataStringOnlyIllOrReq(dataString)) {
				tempReq = dataString.substring(0, dataString.indexOf("....."));
				tempIll = dataString.substring(dataString.indexOf(".....") + 5);
				stringCreateIllegalTests(tempIll.split("-----"));
				stringCreateRequiredTests(tempReq.split("-----"));
			} else {
				if (dataString.indexOf(".....") == 0)
					dataString = dataString.substring(5);
				else
					dataString = dataString.substring(0, dataString.indexOf("....."));
				
				String[] temp = dataString.split("-----");
				String[] tempSplit = temp[0].split(":::::");
				if (tempSplit.length == 6) 
					stringCreateIllegalTests(temp);
				else if (tempSplit.length == 7)
					stringCreateRequiredTests(temp);
			}
		}
//		Object[] arr = allTestCases.keySet().toArray();
//		for (int i = 0; i < arr.length; i++)
//			System.out.println(arr.toString());
			
		allTestCases.put(row, tempTestCases.toString());
	}
		

	private static boolean dataStringOnlyIllOrReq(String dataString) {
		if (dataString.indexOf(".....") == 0 || dataString.indexOf(".....") >= dataString.length() - 10)
			return false;
		return true;
	}
	
	private String stringCreateIllegalTests(String[] allIll) {
		String[][] illegalCharVals = new String[allIll.length][];
		for (int i = 0; i < allIll.length; i++)
			illegalCharVals[i] = allIll[i].split(":::::");
		
//		for (int i = 0; i < allIll.length; i++)
//			illegalCharVals[i] = allIll[i].split(":::::");
		
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
	
	private void stringCreateFalseIllegalTestAlways(String[] illegalChar) {
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
		tempTestCases.append(testCase);
	}
	
	private void stringCreateTrueIllegalTestAlways(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		testCase += "public void " + varName + "TrueIllChar" + illegalChar[illChar] + "() {\n" + 
				"    String temp = fillerText;" + 
				"    assertEquals(temp, " + getSetAndGetPrefix() + ".get" + varName + "());\n" +
				"}\n\n\n";
		tempTestCases.append(testCase);
	}
	
	private void stringCreateFalseIllegalTestAtMost(String[] illegalChar) {
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
		tempTestCases.append(testCase);
		stringNotExpectException(illegalChar);
	}
	
	private void stringCreateTrueIllegalTestAtMost(String[] illegalChar) {
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
		tempTestCases.append(testCase);
	}
	
	private void stringCreateIllegalTestAtBeg(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		testCase += "public void " + varName + "IllChar" + illegalChar[illChar] + "AtBeg() {\n" + 
				"    String temp = \"" + illegalChar[illChar] + "\" + fillerText;\n" +
				"    " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n";
		tempTestCases.append(testCase);
		stringNotExpectException(illegalChar);
	}
	
	private void stringCreateIllegalTestAtEnd(String[] illegalChar) {
		String testCase = "";
		stringExpectException(illegalChar);
		testCase += "public void " + varName + "IllChar" + illegalChar[illChar] + "AtEnd() {\n" + 
				"    String temp = fillerText + \"" + illegalChar[illChar] + "\";\n" +
				"    " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n";
		tempTestCases.append(testCase);
		stringNotExpectException(illegalChar);
	}
	
	private String stringCreateRequiredTests(String[] allReq) {
		String[][] requiredCharVals = new String[allReq.length][];
		
		for (int i = 0; i < allReq.length; i++)
			requiredCharVals[i] = allReq[i].split(":::::");
		
		//Create test if never allowed.
		for (String[] requiredChar : requiredCharVals) {
			//Max allowed
			stringCreateFalseReqCount(requiredChar);
			stringCreateTrueReqCount(requiredChar);
						
			//Always required at beginning
			if (requiredChar[reqCharBeginning].equals("true")) {
				stringCreateFalseRequiredTestAtBeg(requiredChar);
				stringCreateTrueRequiredTestAtBeg(requiredChar);
			}
			
			//Always required at end
			if (requiredChar[reqCharEnd].equals("true")) {
				stringCreateFalseRequiredTestAtEnd(requiredChar);
				stringCreateTrueRequiredTestAtEnd(requiredChar);
			}
			
			//Always required char before.
			if (!requiredChar[reqCharBefore].equals("N/A")) {
				stringCreateFalseRequiredTestCharBefore(requiredChar);
				stringCreateTrueRequiredTestCharBefore(requiredChar);
			}
			
			//Always required char before.
			if (!requiredChar[reqCharAfter].equals("N/A")) {
				stringCreateFalseRequiredTestCharAfter(requiredChar);
				stringCreateTrueRequiredTestCharAfter(requiredChar);
			}
		}
		return "";
	}
	
	private void stringCreateFalseReqCount(String[] requiredChar) {
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
		tempTestCases.append(testCase);
	}
	
	private void stringCreateTrueReqCount(String[] requiredChar) {
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
		tempTestCases.append(testCase);
	}
	
	private void stringCreateFalseRequiredTestAtBeg(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "FalseReqChar" + requiredChar[reqChar] + "AtBeg() {\n" + 
				"    String temp = fillerText\n" + 
				"    for (int i = 1 ; i < fillerText.length(); i++) {\n" + 
				"        temp = fillerText\n" + 
				"        temp = temp.substring(0,i) + \"" + requiredChar[reqChar] + "\" + temp.substring(i);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertFalse(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" +
				"    }\n" +
				"}\n\n\n";
		tempTestCases.append(testCase);
	}
	
	private void stringCreateTrueRequiredTestAtBeg(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "TrueReqChar" + requiredChar[reqChar] + "AtBeg() {\n" + 
				"    String temp = \"" + requiredChar[reqChar] + "\" + fillerText\n" +
				"    assertEquals(temp, " + getSetAndGetPrefix() + ".get" + varName + "());\n" +
				"}\n\n\n";
		tempTestCases.append(testCase);
	}
	
	private void stringCreateFalseRequiredTestAtEnd(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "FalseReqChar" + requiredChar[reqChar] + "AtBeg() {\n" + 
				"    String temp = fillerText\n" + 
				"    for (int i = 0 ; i < fillerLength - 1; i++) \n" + 
				"        temp = fillerText\n" +  
				"        temp = temp.substring(0, i) + \"" + requiredChar[reqChar] + "\" + temp.substring(i);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertFalse(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" +
				"    \n" +
				"}\n\n\n";
		tempTestCases.append(testCase);
	}
	
	private void stringCreateTrueRequiredTestAtEnd(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "TrueTrueReqChar" + requiredChar[reqChar] + "AtBeg() {\n" + 
				"    String temp = fillerText\n + \"" + requiredChar[reqChar] + "\"\n" +
				"    assertEquals(temp, " + getSetAndGetPrefix() + ".get" + varName + "());\n" +
				"}\n\n\n";
		tempTestCases.append(testCase);
	}
	
	private void stringCreateFalseRequiredTestCharBefore(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "FalseReqChar" + requiredChar[reqChar] + "CharBefore() {\n" + 
				"    String temp = fillerText\n" + 
				"    int randInt = 0;\n" + 
				"    for (int i = " + getBadCategoryStart(requiredChar) + "; i < " + (getBadCategoryStart(requiredChar) + getBadCategorySize(requiredChar)) + " - 1; i++) {\n" + 
				"        temp = fillerText\n" + 
				"        randInt = rand.nextInt(fillerLength - 1) + 1;\n" + 
				"        temp = temp.substring(0, randInt) + (char)i + " + requiredChar[reqChar] + "\" + temp.substring(randInt);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertTrue(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" + 
				"    }\n" +
				"}\n\n\n";
		tempTestCases.append(testCase);
	}
	
	private void stringCreateTrueRequiredTestCharBefore(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "TrueReqChar" + requiredChar[reqChar] + "CharBefore() {\n" + 
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
		tempTestCases.append(testCase);
	}
	
	private void stringCreateFalseRequiredTestCharAfter(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "FalseReqChar" + requiredChar[reqChar] + "CharBefore() {\n" + 
				"    String temp = fillerText\n" + 
				"    int randInt = 0;\n" + 
				"    for (int i = " + getBadCategoryStart(requiredChar) + "; i < " + (getBadCategoryStart(requiredChar) + getBadCategorySize(requiredChar)) + " - 1; i++) {\n" + 
				"        temp = fillerText\n" + 
				"        randInt = rand.nextInt(fillerLength - 1) + 1;\n" + 
				"        temp = temp.substring(0, randInt) + " + requiredChar[reqChar] + "\" + (char)i + temp.substring(randInt);\n" +
				"        " + getSetAndGetPrefix() + ".set" + varName + "(temp);\n" +
				"        assertTrue(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n" + 
				"    }\n" +
				"}\n\n\n";
		tempTestCases.append(testCase);
	}
	
	private void stringCreateTrueRequiredTestCharAfter(String[] requiredChar) {
		String testCase = "";
		stringExpectException(requiredChar);
		testCase += "public void " + varName + "TrueReqChar" + requiredChar[reqChar] + "CharBefore() {\n" + 
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
		tempTestCases.append(testCase);
	}
	
	private void stringExpectException(String[] charToCheck) {
		if (charToCheck.length == 6)
			if (charToCheck[illCharThrows].equals("true")) {
				tempTestCases.append("@Test(expected = Exception.class)\n");
			} else {
				tempTestCases.append("@Test\n");
			}
		else if (charToCheck.length == 7) 
			if (charToCheck[reqCharThrows].equals("true")) {
				tempTestCases.append("@Test(expected = Exception.class)\n");
			} else {
				tempTestCases.append("@Test\n");
			}
	}
	
	private void stringNotExpectException(String[] illegalChar) {
		if (!illegalChar[illCharThrows].equals("true")) {
			tempTestCases.append("    assertFalse(temp.equals(" + getSetAndGetPrefix() + ".get" + varName + "()));\n");
		}
		tempTestCases.append("}\n\n\n");
	}
	
	private String getSetAndGetPrefix() {
		return (isStatic) ?  className : "obj";	
	}
	
	private int getRemaining(String[] requiredChar) {
		int remaining = Integer.parseInt(requiredChar[reqCharCount]);
		if (requiredChar[reqCharBeginning].equals("true"))
			remaining--;
		if (requiredChar[reqCharEnd].equals("true"))
			remaining--;
		return remaining;
	}

	private int getBadCategoryStart(String[] requiredChar) {
		String temp = requiredChar[reqCharBefore];
		int start = 0;
		if (temp.equals("None"))
			start = 45;
		else if (temp.equals("Any"))
			start = 0;
		else if (temp.equals("Any #"))
			start = ThreadLocalRandom.current().nextBoolean() ? 65 : 97;
		else if (temp.equals("Any char")) {
			start = 48;
		}
		
		return start;
	}
	
	private int getBadCategorySize(String[] requiredChar) {
		String temp = requiredChar[reqCharBefore];
		int size = 0;
		if (temp.equals("None"))
			size = 78;
		else if (temp.equals("Any"))
			size = 0;
		else if (temp.equals("Any #"))
			size = 26;
		else if (temp.equals("Any char")) {
			size = 10;
		}
		
		return size;
	}
	
	private int getCategoryStart(String[] requiredChar) {
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
	
	private int getCategorySize(String[] requiredChar) {
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
	
	public static boolean valueAlreadyExists(int row)
	{
		return (allTestCases.containsKey(row) ? true : false);
	}
	
	public static StringBuilder getTestCases() {
		StringBuilder temp = new StringBuilder();
		Iterator<String> iter = allTestCases.values().iterator();
		while (iter.hasNext())
			temp.append(iter.next());
		return temp;
	}
	
	
}