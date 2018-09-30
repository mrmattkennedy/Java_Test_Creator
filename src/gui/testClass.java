package gui;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class testClass {
	String s1;
	public static void main(String[] args)
	{
		Random rand = new Random();
		String s = "";
		for (int i = 0; i < 100; i++)
			for (int j = 0; j < rand.nextInt(5); j++)
				System.out.println(j);
		System.out.println(s);
	}
}
