import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Component;

public class TC_StringVarDialogBox extends JDialog implements ActionListener
{
	private JCheckBox emptyChkBx;
	//private JCheckBox canBeDefaultChkBx;
	private JCheckBox numbersAllowedChkBx;
	private JCheckBox lettersAllowedChkBx;

	private JTextField illegalCharsTxt;
	private JTextField requiredCharsTxt;
	//ivate JComboBox<String> spaceCountCombo;

	//private JCheckBox spaceAtBeginningChkBx;
	//private JCheckBox spaceAtEndChkBx;

	private JButton okBtn;
	private JButton cancelBtn;

	private final int numPanels = 3;
	private JPanel[] gridPanels;

	private final int spaceCountCapacity = 20;
	private String[] spaceCountField;

	private boolean emptyAllowed;
	//private boolean canBeDefault;
	private boolean numbersAllowed;
	private boolean lettersAllowed;

	private String illegalChars = "";
	private String requiredChars = "";
	//ivate int spaceCount;
	
	//private boolean spaceAtBeginning;
	//ivate boolean spaceAtEnd;

	private Font firstChecksFont;

	private TC_VarsPanel paFrame;
	private int row;

	private JTable table;
	private TC_StringTableModel tableModel;
	private TableColumn[] columns;
	
	private final String[] dropDownCount = {"1",
			"2",
			"3",
			"4",
			"5",
			"6",
			"7",
			"8",
			"9",
			"10"
	};
	
	private String[] charDropDown;
	private JComboBox<String> countCombo;
	private JComboBox<String> charCombo1;
	private JComboBox<String> charCombo2;
	
	private JPanel finalPanel;
	public TC_StringVarDialogBox(TC_VarsPanel paFrame, int row) 
	{
		firstChecksFont = new Font("Futura", Font.PLAIN, 12);
		gridPanels = new JPanel[numPanels];
		for (int i = 0; i < numPanels; i++)
			gridPanels[i] = new JPanel();

		spaceCountField = new String[spaceCountCapacity];
		//for (int i = 0; i < spaceCountCapacity; i++)
	//		spaceCountField[i] = Intege.toString(i);
		tableModel = new TC_StringTableModel(this);
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(15);
		table.getColumnModel().getColumn(2).setPreferredWidth(25);
		table.getColumnModel().getColumn(3).setPreferredWidth(25);
		table.getColumnModel().getColumn(4).setPreferredWidth(40);
		table.getColumnModel().getColumn(5).setPreferredWidth(40);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		emptyChkBx = new JCheckBox("String can be empty: ");
		//canBeDefaultChkBx = new JCheckBox("String can be default value: ");
		numbersAllowedChkBx = new JCheckBox("Numbers allowed: ");
		lettersAllowedChkBx = new JCheckBox("Letters allowed: ");

		okBtn = new JButton("Ok");
		cancelBtn = new JButton("Cancel");

		okBtn.setPreferredSize(new Dimension(100, 30));
		cancelBtn.setPreferredSize(new Dimension(100, 30));

		okBtn.addActionListener(this);
		cancelBtn.addActionListener(this);

		emptyChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		//canBeDefaultChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		numbersAllowedChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		lettersAllowedChkBx.setHorizontalTextPosition(SwingConstants.LEFT);

		emptyChkBx.setFont(firstChecksFont);
		//canBeDefaultChkBx.setFont(firstChecksFont);
		numbersAllowedChkBx.setFont(firstChecksFont);
		lettersAllowedChkBx.setFont(firstChecksFont);

		illegalCharsTxt = new JTextField("");
		illegalCharsTxt.setPreferredSize(new Dimension(150, 20));
		requiredCharsTxt = new JTextField("");
		requiredCharsTxt.setPreferredSize(new Dimension(150, 20));
		
		requiredCharsTxt.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    updateTableInner();
			  }
			  public void removeUpdate(DocumentEvent e) {
				  updateTableInner();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  updateTableInner();
			  }

			  public void updateTableInner() {
				  updateTable();
			  }
			});
		//spaceCountCombo = new JComboBox<String>(spaceCountField);
//		spaceCountTxt.setPreferredSize(new Dimension(80, 20));

		//spaceAtBeginningChkBx = new JCheckBox("Space at beginning: ");
		//spaceAtEndChkBx = new JCheckBox("Space at end: ");

		//spaceAtBeginningChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		//spaceAtEndChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		
		//spaceAtBeginningChkBx.setFont(firstChecksFont);
		//spaceAtEndChkBx.setFont(firstChecksFont);

		gridPanels[0].add(emptyChkBx);
		//gridPanels[0].add(canBeDefaultChkBx);
		gridPanels[0].add(numbersAllowedChkBx);
		gridPanels[0].add(lettersAllowedChkBx);
		gridPanels[1].add(new JLabel("Illegal chars: "));
		gridPanels[1].add(illegalCharsTxt);
		gridPanels[1].add(new JLabel("Required chars: "));
		gridPanels[1].add(requiredCharsTxt);
	//	gridPanels[4].add(new JLabel("Number of spaces: "));
	//	gridPanels[4].add(spaceCountCombo);
	//	gridPanels[5].add(spaceAtBeginningChkBx);
	//	gridPanels[5].add(spaceAtEndChkBx);
		gridPanels[2].add(okBtn);
		gridPanels[2].add(cancelBtn);

		finalPanel = new JPanel();
		finalPanel.setLayout(new GridLayout(numPanels, 1));
		for (int i = 0; i < numPanels; i++)
			finalPanel.add(gridPanels[i]);
		
		charDropDown = new String[96];
		charDropDown[0] = "None";
		for (int i = 32; i < 127; i++)
			charDropDown[i-31] = "'" + Character.toString ((char) i) + "'";
		countCombo = new JComboBox<String>(dropDownCount);
		charCombo1 = new JComboBox<String>(charDropDown);
		charCombo2 = new JComboBox<String>(charDropDown);
		
		setupTable();
		add(finalPanel);
		add(scrollPane);
		this.paFrame = paFrame;
		this.row = row;
		

		setLayout(new GridLayout(2, 1));
		setSize(600, 350);
        setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);	
	}
	
	private void updateTable()
	{
		System.out.println(countCombo.getSelectedIndex());
		requiredChars = requiredCharsTxt.getText();
		char temp;
		for (int i = 0; i < requiredChars.length(); i++)
		{
			temp = requiredChars.charAt(i);
			if (tableModel.noEntry(temp))
				tableModel.addEntry(temp);
		}
	}
	
	private void setupTable()
	{
		
        
		columns = new TableColumn[3];
		columns[0] = table.getColumnModel().getColumn(1);
		columns[1] = table.getColumnModel().getColumn(4);
		columns[2] = table.getColumnModel().getColumn(5);
		
		columns[0].setCellEditor(new DefaultCellEditor(countCombo));
		columns[1].setCellEditor(new DefaultCellEditor(charCombo1));
		columns[2].setCellEditor(new DefaultCellEditor(charCombo2));
		
		 DefaultTableCellRenderer renderer =
	                new DefaultTableCellRenderer();
		 renderer.setToolTipText("Click for combo box");
		 columns[0].setCellRenderer(renderer);
		 columns[1].setCellRenderer(renderer);
		 columns[2].setCellRenderer(renderer);
		
	        
	}
	
	private boolean checkVars()
	{
		emptyAllowed = emptyChkBx.isSelected();
		//canBeDefault = canBeDefaultChkBx.isSelected();
		numbersAllowed = numbersAllowedChkBx.isSelected();
		lettersAllowed = lettersAllowedChkBx.isSelected();
		//spaceAtBeginning = spaceAtBeginningChkBx.isSelected();
	//	spaceAtEnd = spaceAtEndChkBx.isSelected();

		illegalChars = illegalCharsTxt.getText();
		requiredChars = requiredCharsTxt.getText();

		//spaceCount = spaceCountCombo.getSelectedIndex();
		

		if (!checkIllegalCharsTxt())
		{
			illegalCharsTxt.setBackground(new Color(255, 69, 0));
			return false;
		}
		else
			illegalCharsTxt.setBackground(new Color(255, 255, 255));

		if (!checkRequiredCharsTxt())
		{
			requiredCharsTxt.setBackground(new Color(255, 69, 0));
			return false;
		}
		else
			requiredCharsTxt.setBackground(new Color(255, 255, 255));
		return true;
	}

	private boolean checkIllegalCharsTxt()
	{
		/*
		if (illegalChars.contains(" "))
		{
			if (spaceAtBeginning || spaceAtEnd)
				return false;
			
			if (spaceCount != 0)
				return false;
		}

		if (spaceCount == 1)
		{
			if (spaceAtBeginning && spaceAtEnd)
				return false;

			
		}
		*/
/*
		if (numbersAllowed)
		{
			for (int i = 0; i < illegalChars.length(); i++) {
				if (Character.isDigit(illegalChars.charAt(i)))
					return false;
			}
		}
		
		if (lettersAllowed)
		{
			for (int i = 0; i < illegalChars.length(); i++) {
				if (Character.isLetter(illegalChars.charAt(i)))
					return false;
			}
		}
*/
		for (int i = 0; i < illegalChars.length(); i++)
		{
			for (int j = 0; j < requiredChars.length(); j++)
			{
				if (illegalChars.charAt(i) == requiredChars.charAt(j))
					return false;
			}
		}

		for (int i = 0; i < illegalChars.length(); i++)
		{
			if(illegalChars.indexOf(illegalChars.charAt(i)) != illegalChars.lastIndexOf(illegalChars.charAt(i)))
				return false;
		}	

		return true;
	}

	private boolean checkRequiredCharsTxt()
	{
		
		//if (requiredChars.contains(" ") && spaceCount == 0)
		//	return false;

		if (!numbersAllowed)
		{
			for (int i = 0; i < requiredChars.length(); i++) {
				if (Character.isDigit(requiredChars.charAt(i)))
					return false;
			}
		}

		if (!lettersAllowed)
		{
			for (int i = 0; i < requiredChars.length(); i++) {
				if (Character.isLetter(requiredChars.charAt(i)))
					return false;
			}
		}

		for (int i = 0; i < requiredChars.length(); i++)
		{
			if (requiredChars.indexOf(requiredChars.charAt(i)) != requiredChars.lastIndexOf(requiredChars.charAt(i)))
				return false;
		}
		return true;
	}
	
	public String getDropDownValue(int menuIndex)
	{
		switch (menuIndex)
		{
		case (0):
			return countCombo.getSelectedItem().toString();
		case (1):
			return charCombo1.getSelectedItem().toString();
		case (2):
			return charCombo2.getSelectedItem().toString();
		}
		return null;
	}

	private String createString()
	{
		//return illegalChars + "---" + requiredChars + "---" + emptyAllowed + "---" + canBeDefault + "---" + spaceAtBeginning + "---" + spaceAtEnd + "---" + spaceCount;
		return "";
	}
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		Object source = arg0.getSource();

		if (source == okBtn && checkVars()) {
			//paFrame.varChecked(row, createString());
			dispose();		
		} else if (source == cancelBtn)
			dispose();
	}
}
