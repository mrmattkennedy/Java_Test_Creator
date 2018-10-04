package gui;

import java.awt.Dimension;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class testClass {
	String s1;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JEditorPane pane = new JEditorPane();
		
		pane.setText("<html><font color=\"red\">hello world!</font></html>");
		panel.add(pane);
		frame.getContentPane().add(panel);
		frame.setSize(new Dimension(400, 205));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public void settestString(String data) {}
	public String gettestString() {return "";}
}
