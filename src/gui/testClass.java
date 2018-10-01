package gui;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class testClass {
	String s1;

	public static void main(String[] args) {
		Random rand = new Random();
		String s = "abcdef";
		String b = "A" + s;
		for (int i = 0; i < 1000; i++)
			System.out.println(rand.nextInt(s.length() - 1) + 1);
	}
}
