package gui;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

class TestCases {
	String fillerText = "ABC";
	Random rand = new Random();
	testClass obj = new testClass();
	int fillerLength = fillerText.length();

	@Test
	public void testStringFalseIllChara() {
	    String temp = "";
	    for (int i = 0 ; i < fillerText.length(); i++) {
	        temp = fillerText;
	        temp = temp.substring(0,i) + "a" + temp.substring(i);
	        obj.settestString(temp);
	        assertFalse(temp.equals(obj.gettestString()));
	    }
	}


	@Test
	public void testStringTrueIllChara() {
	    String temp = fillerText;
	    assertEquals(temp, obj.gettestString());
	}


	@Test
	public void testStringFalseIllCharb() {
	    String temp = "";
	    for (int i = 0 ; i < fillerText.length(); i++) {
	        temp = fillerText;
	        temp = temp.substring(0,i) + "b" + temp.substring(i);
	        obj.settestString(temp);
	        assertFalse(temp.equals(obj.gettestString()));
	    }
	}


	@Test
	public void testStringTrueIllCharb() {
	    String temp = fillerText;
	    assertEquals(temp, obj.gettestString());
	}


	@Test
	public void testStringFalseReqCharc() {
	    String temp = "";
	    int randInt = 0;
	    for (int i = 1; i < 1 + 1; i++) {
	        temp = fillerText;
	        for (int j = 0 ; j < 1 - i; j++) {
	            randInt = rand.nextInt(fillerLength);
	            temp = temp.substring(0,randInt) + "c" + temp.substring(randInt);
	            obj.settestString(temp);
	        }
	        assertFalse(temp.equals(obj.gettestString()));
	    }
	}


	@Test
	public void testStringTrueReqCharc() {
	    String temp = fillerText;
	    int randInt = 0;
	    for (int i = 0; i < 1 + 1; i++) {
	        randInt = rand.nextInt(fillerLength);
	        temp = temp.substring(0,randInt) + "c" + temp.substring(randInt);
	        obj.settestString(temp);
	    }
	    assertEquals(temp, obj.gettestString());
	}


	@Test
	public void testStringFalseReqChard() {
	    String temp = "";
	    int randInt = 0;
	    for (int i = 1; i < 1 + 1; i++) {
	        temp = fillerText;
	        for (int j = 0 ; j < 1 - i; j++) {
	            randInt = rand.nextInt(fillerLength);
	            temp = temp.substring(0,randInt) + "d" + temp.substring(randInt);
	            obj.settestString(temp);
	        }
	        assertFalse(temp.equals(obj.gettestString()));
	    }
	}


	@Test
	public void testStringTrueReqChard() {
	    String temp = fillerText;
	    int randInt = 0;
	    for (int i = 0; i < 1 + 1; i++) {
	        randInt = rand.nextInt(fillerLength);
	        temp = temp.substring(0,randInt) + "d" + temp.substring(randInt);
	        obj.settestString(temp);
	    }
	    assertEquals(temp, obj.gettestString());
	}


	@Test
	public void testStringFalseReqChare() {
	    String temp = "";
	    int randInt = 0;
	    for (int i = 1; i < 1 + 1; i++) {
	        temp = fillerText;
	        for (int j = 0 ; j < 1 - i; j++) {
	            randInt = rand.nextInt(fillerLength);
	            temp = temp.substring(0,randInt) + "e" + temp.substring(randInt);
	            obj.settestString(temp);
	        }
	        assertFalse(temp.equals(obj.gettestString()));
	    }
	}


	@Test
	public void testStringTrueReqChare() {
	    String temp = fillerText;
	    int randInt = 0;
	    for (int i = 0; i < 1 + 1; i++) {
	        randInt = rand.nextInt(fillerLength);
	        temp = temp.substring(0,randInt) + "e" + temp.substring(randInt);
	        obj.settestString(temp);
	    }
	    assertEquals(temp, obj.gettestString());
	}





}
