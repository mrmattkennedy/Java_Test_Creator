import javax.swing.JTabbedPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Dimension;

public class TC_TabbedPane extends JTabbedPane {

	private TC_ClassPanel classPanel;
	private TC_VarsPanel varsPanel;
	private JFrame paFrame;

	public TC_TabbedPane() 
	{
		classPanel = new TC_ClassPanel(this);
		varsPanel = new TC_VarsPanel();

		addTab("Class", classPanel);
		addTab("Variables", varsPanel);

		addTabChangeListener();
		paFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
	}
	
	private void addTabChangeListener()
	{
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				paFrame = (JFrame) SwingUtilities.getWindowAncestor(TC_TabbedPane.this);
				if (getSelectedIndex() == 1)
					paFrame.setSize(new Dimension(600, 205));
				else
					paFrame.setSize(new Dimension(400, 205));
				paFrame.setLocationRelativeTo(null);
					
		    	}
		});
	}
	
	public void addVariable(String var)
	{
		varsPanel.addVariable(var);
	}

}
