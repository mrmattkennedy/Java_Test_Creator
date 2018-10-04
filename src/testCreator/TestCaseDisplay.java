package testCreator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TestCaseDisplay extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JScrollPane areaScrollPane;
	private JMenuBar menuBar;
	private JMenu fMenu;
	private JMenuItem okItem;
	
	
	public TestCaseDisplay(StringBuilder dataToDisplay) {
		textArea = new JTextArea();
		areaScrollPane = new JScrollPane(textArea);
		menuBar = new JMenuBar();
		fMenu = new JMenu("Actions");
		okItem = new JMenuItem("Done");
		
		menuBar.add(fMenu);
		fMenu.add(okItem);
		
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea.setBackground(Color.decode("#232334"));
		textArea.setForeground(Color.decode("#AACCFF"));
		okItem.addActionListener(this);
		
		displayData(dataToDisplay);
		add(areaScrollPane);
		setJMenuBar(menuBar);
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
		Object source = arg0.getSource();
		if (source == okItem)
			dispose();
		
	}
	
}
