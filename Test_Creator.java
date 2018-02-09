import javax.swing.JFrame;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

class Test_Creator {

	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Metal".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unable to load Look and Feel, using default.", "Error", JOptionPane.INFORMATION_MESSAGE);
			       		        			
		}
			
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createFrame();
			}
		});
	}
	
	public static void createFrame() {
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(400, 205));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		TabbedPane tabbedPane = new TabbedPane();
		frame.add(tabbedPane);
	}
}
