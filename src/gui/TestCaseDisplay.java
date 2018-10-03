package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TestCaseDisplay extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JScrollPane areaScrollPane;
	private Font textAreaFont;
	
	
	public TestCaseDisplay(StringBuilder dataToDisplay) {
		textArea = new JTextArea();
		textAreaFont = new Font("Helvetica", 0, 12);
		
		areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea.setBackground(Color.decode("#232334"));
		textArea.setForeground(Color.decode("#AACCFF"));
		
		displayData(dataToDisplay);
		add(areaScrollPane);
		setLayout(new GridLayout(1, 1, 5, 5));
		setSize(800, 667);
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);	
	}
	
	private void displayData(StringBuilder dataToDisplay) {
		String temp = dataToDisplay.toString().replaceAll("_-_-_", "\n\n\n");
		textArea.setText(temp);
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
