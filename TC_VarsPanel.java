import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;


import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TC_VarsPanel extends JPanel implements ActionListener
{
	private TC_TableModel tableModel;
	private JTable table;

	private JButton addBtn;
	private JButton editBtn;
	private JButton deleteBtn;
	private JButton helpBtn;
	
	private Font btnFont;

	public TC_VarsPanel()
	{
		addBtn = new JButton("Add");
		editBtn = new JButton("Edit");
		deleteBtn = new JButton("Delete");
		helpBtn = new JButton("Help");
	
		addBtn.setPreferredSize(new Dimension(100, 40));
		editBtn.setPreferredSize(new Dimension(100, 40));
		deleteBtn.setPreferredSize(new Dimension(100, 40));
		helpBtn.setPreferredSize(new Dimension(100, 40));

		editBtn.addActionListener(this);
		addBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		helpBtn.addActionListener(this);

		btnFont = new Font("Helvetica", Font.BOLD, 14);
		editBtn.setFont(btnFont);
		deleteBtn.setFont(btnFont);
		helpBtn.setFont(btnFont);
		
		tableModel = new TC_TableModel();
		table = new JTable(tableModel);
		addTableMouseListener();
		table.getColumnModel().getColumn(4).setPreferredWidth(15);
		table.getColumnModel().getColumn(3).setPreferredWidth(15);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//table.setDefaultRenderer(Object.class, new TC_TableCellRenderer()); 

		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(575, 90));

		add(new JScrollPane(pane));
		add(addBtn);
		add(editBtn);
		add(deleteBtn);
		add(helpBtn);
	}

	private void addTableMouseListener()
	{
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if (e.getClickCount() == 2) {
					checkValue((table.getValueAt(table.getSelectedRow(), 1).toString()));
				}
			}
		});
	}

	private void checkValue(String varType)
	{
		if (varType.equals("String")) 
		{
			int row = table.getSelectedRow();
			new TC_StringVarDialogBox(this, row);
		}		
	}
	
	public void addVariable(String s)
	{
		tableModel.addVariable(s);
	}

	public void varChecked(int row, String varString)
	{
		tableModel.varChecked(row, varString);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == addBtn) {
			table.getSelectionModel().clearSelection();
			new TC_AddVarDialog(this);

		} else if (source == editBtn) {
			if (table.getSelectedRow() == -1)
				return;

			checkValue((table.getValueAt(table.getSelectedRow(), 1).toString()));
			table.getSelectionModel().clearSelection();
		}

		else if (source == deleteBtn)
		{
			if (table.getSelectedRow() == -1)
				return;

			tableModel.removeVariable(table.getSelectedRow());
			table.getSelectionModel().clearSelection();
		}
		else if (source == helpBtn)
		{
			JOptionPane.showMessageDialog(null, "Add: manually add a variable.\n" +	
								"Edit: Edit a variable. Note: this is how you verify vars.\n" +	
								"Delete: Removes a variable from the table.\n\n" + 
								"You may also double-click on a variable to edit it.");
								
								
		}

	}
}
