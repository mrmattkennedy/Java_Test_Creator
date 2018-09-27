package gui;
import java.util.Map;
import java.util.Set;

public class testClass {
	public static void main(String[] args)
	{
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		
		for (int i = 0; i < threadArray.length; i++)
			System.out.println(threadArray[i].getName());
		
		
	}
}
