import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;

//Use a Default Cell Editor array in an array list.
//Make th combos, add them to the editors.
//Editor will automatically read the value.
public class StringVarDialogBox extends JDialog implements ActionListener
{
	private final int[] dialogCols = {1, 4, 5};
	//private final String[] dialogColCoords = {"86", "364", "475"};
	private JCheckBox emptyChkBx;
	private JCheckBox numbersAllowedChkBx;
	private JCheckBox lettersAllowedChkBx;

	private JTextField illegalCharsTxt;
	private JTextField requiredCharsTxt;

	private JButton okBtn;
	private JButton helpBtn;
	private JButton cancelBtn;

	private final int numPanels = 3;
	private JPanel[] gridPanels;

	private boolean emptyAllowed;
	private boolean numbersAllowed;
	private boolean lettersAllowed;

	private String requiredChars = "";
	private String illegalChars = "";
	private String prevRequiredChars = "";
	private String prevIllegalChars = "";

	private Font firstChecksFont;

	private VarsPanel paFrame;
	private int row;

	private JTable reqTable;
	private StringReqTableModel reqTableModel;

	private JTable illTable;
	private StringIllTableModel illTableModel;

	private String[] charDropDown;
	private final String[] countDropDown = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

	private List<TableCellEditor[]> reqEditors;
	private JComboBox[] charCombos;

	private List<TableCellEditor> illEditor;

	private JPanel finalPanel;
	
	private String[] reqVarString;
	private String[] illVarString;
	
	public StringVarDialogBox(VarsPanel paFrame, int row) 
	{
		firstChecksFont = new Font("Futura", Font.PLAIN, 12);
		gridPanels = new JPanel[numPanels];
		for (int i = 0; i < numPanels; i++)
			gridPanels[i] = new JPanel();

		charDropDown = new String[100];
		charDropDown[0] = "N/A";
		charDropDown[1] = "None";
		charDropDown[2] = "Any";
		charDropDown[3] = "Any #";
		charDropDown[4] = "Any char";
		for (int i = 32; i < 127; i++)
			charDropDown[i-27] = "'" + Character.toString((char)i) + "'";

		reqEditors = new ArrayList<TableCellEditor[]>();
		charCombos = new JComboBox[3];
		charCombos[0] = new JComboBox<String>(countDropDown);
		charCombos[1] = new JComboBox<String>(charDropDown);
		charCombos[2] = new JComboBox<String>(charDropDown);

		reqTableModel = new StringReqTableModel();
		reqTable = new JTable(reqTableModel)
		{
			//  Determine editor to be used by row
			public TableCellEditor getCellEditor(int row, int column)
			{
				int modelColumn = convertColumnIndexToModel( column );

				if (modelColumn == dialogCols[0])
					return reqEditors.get(row)[0];
				else if (modelColumn == dialogCols[1])
					return reqEditors.get(row)[1];
				else if (modelColumn == dialogCols[2])
					return reqEditors.get(row)[2];
				else
					return super.getCellEditor(row, column);
			}
		};

		illEditor = new ArrayList<TableCellEditor>();

		illTableModel = new StringIllTableModel();
		illTable = new JTable(illTableModel)
		{
			//  Determine editor to be used by row
			public TableCellEditor getCellEditor(int row, int column)
			{
				int modelColumn = convertColumnIndexToModel( column );

				if (modelColumn == 2)
					return illEditor.get(row);

				return super.getCellEditor(row, column);
			}
		};

		reqTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		JScrollPane reqScrollPane = new JScrollPane(reqTable);

		reqTable.setRowHeight(20);
		reqTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		illTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		JScrollPane illScrollPane = new JScrollPane(illTable);

		illTable.setRowHeight(20);
		illTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


		emptyChkBx = new JCheckBox("String can be empty: ");
		numbersAllowedChkBx = new JCheckBox("Numbers allowed: ");
		lettersAllowedChkBx = new JCheckBox("Letters allowed: ");

		okBtn = new JButton("Ok");
		helpBtn = new JButton("Help");
		cancelBtn = new JButton("Cancel");

		okBtn.setPreferredSize(new Dimension(100, 30));
		helpBtn.setPreferredSize(new Dimension(100, 30));
		cancelBtn.setPreferredSize(new Dimension(100, 30));

		okBtn.addActionListener(this);
		helpBtn.addActionListener(this);
		cancelBtn.addActionListener(this);

		emptyChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		numbersAllowedChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		lettersAllowedChkBx.setHorizontalTextPosition(SwingConstants.LEFT);

		emptyChkBx.setFont(firstChecksFont);
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
				//illegalCharsTxt.setText(checkDuplicateChars(illegalCharsTxt.getText()));
				updateReqTable();
			}
		});

		illegalCharsTxt.getDocument().addDocumentListener(new DocumentListener() {
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
				//illegalCharsTxt.setText(checkDuplicateChars(illegalCharsTxt.getText()));
				updateIllTable();
			}
		});
		 
		gridPanels[0].add(emptyChkBx);
		gridPanels[0].add(numbersAllowedChkBx);
		gridPanels[0].add(lettersAllowedChkBx);
		gridPanels[1].add(new JLabel("Illegal chars: "));
		gridPanels[1].add(illegalCharsTxt);
		gridPanels[1].add(new JLabel("Required chars: "));
		gridPanels[1].add(requiredCharsTxt);
		gridPanels[2].add(okBtn);
		gridPanels[2].add(helpBtn);
		gridPanels[2].add(cancelBtn);

		finalPanel = new JPanel();
		finalPanel.setLayout(new GridLayout(numPanels, 1));
		for (int i = 0; i < numPanels; i++)
			finalPanel.add(gridPanels[i]);

		add(finalPanel);
		add(reqScrollPane);
		add(illScrollPane);
		this.paFrame = paFrame;
		this.row = row;

		setLayout(new GridLayout(3, 1));
		setSize(600, 400);
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);	
	}

	private void updateReqTable()
	{
		requiredChars = checkDuplicateChars(requiredCharsTxt.getText());
		String difference = getStringDifference(requiredChars, prevRequiredChars);
		if (difference.equals(":::"))
			return;

		String addChars = difference.substring(0, difference.indexOf(":::"));
		String removeChars = difference.substring(difference.indexOf(":::") + 3);
		int[] removeCharIndeces = new int[removeChars.length()];

		if (addChars.length() != 0)
			for (int i = 0; i < addChars.length(); i++)
			{
				reqTableModel.addEntry(addChars.charAt(i));
				addReqRow();
			}

		for (int i = 0; i < removeCharIndeces.length; i++)
			for (int j = 0; j < reqTableModel.getRowCount(); j++)
				if (removeChars.charAt(i) == (char)(reqTableModel.getValueAt(j, 0)))
				{
					removeCharIndeces[i] = j;
					break;
				}

		if (removeChars.length() != 0)
			for (int i = 0; i < removeCharIndeces.length; i++)
			{
				reqTableModel.removeEntry(removeChars.charAt(i));
				removeReqRow(removeCharIndeces[i] - i);
			}

		prevRequiredChars = requiredChars;
	}

	private void updateIllTable()
	{
		illegalChars = checkDuplicateChars(illegalCharsTxt.getText());
		String difference = getStringDifference(illegalChars, prevIllegalChars);
		if (difference.equals(":::"))
			return;

		String addChars = difference.substring(0, difference.indexOf(":::"));
		String removeChars = difference.substring(difference.indexOf(":::") + 3);
		int[] removeCharIndeces = new int[removeChars.length()];

		if (addChars.length() != 0)
			for (int i = 0; i < addChars.length(); i++)
			{
				illTableModel.addEntry(addChars.charAt(i));
				addIllRow();
			}

		for (int i = 0; i < removeCharIndeces.length; i++)
			for (int j = 0; j < illTableModel.getRowCount(); j++)
				if (removeChars.charAt(i) == (char)(illTableModel.getValueAt(j, 0)))
				{
					removeCharIndeces[i] = j;
					break;
				}

		if (removeChars.length() != 0)
			for (int i = 0; i < removeCharIndeces.length; i++)
			{
				illTableModel.removeEntry(removeChars.charAt(i));
				removeIllRow(removeCharIndeces[i] - i);
			}

		prevIllegalChars = illegalChars;
	}


	private void addReqRow()
	{
		int currSize = reqEditors.size();		
		reqEditors.add(new DefaultCellEditor[3]);
		reqEditors.get(currSize)[0] = new DefaultCellEditor( charCombos[0]);
		reqEditors.get(currSize)[1] = new DefaultCellEditor( charCombos[1]);
		reqEditors.get(currSize)[2] = new DefaultCellEditor( charCombos[2]);
	}

	private void removeReqRow(int row)
	{
		reqEditors.remove(row);
	}

	private void addIllRow()
	{
		illEditor.add(new DefaultCellEditor(charCombos[0]));
	}

	private void removeIllRow(int row)
	{
		illEditor.remove(row);
	}

	private String checkDuplicateChars(String checkStr)
	{
		char temp;
		String retStr = "";
		ArrayList<Integer> badChars = new ArrayList<Integer>();
		for (int i = 0; i < checkStr.length() - 1; i++)
		{
			temp = checkStr.charAt(i);
			for (int j = i + 1; j < checkStr.length(); j++)
				if (temp == checkStr.charAt(j))
					badChars.add(j);
		}

		boolean addChar;
		for (int i = 0; i < checkStr.length(); i++)
		{
			addChar = true;
			for (Integer j : badChars)
				if (i == j)
					addChar = false;

			if (addChar)
				retStr += checkStr.charAt(i);
		}
		return retStr;
	}

	private String getStringDifference(String curr, String prev)
	{
		String retString = "";
		boolean addChar = true;
		//Get chars that are now in curr, and weren't in prev.
		for (int i = 0; i < curr.length(); i++)
		{
			addChar = true;
			for (int j = 0; j < prev.length(); j++)
				if (curr.charAt(i) == prev.charAt(j))
					addChar = false;
			if (addChar)
				retString += curr.charAt(i);
		}

		//Opposite of above. Logic is good because, if in prev and not in curr, then it's not there anymore.
		//Very interesting!! When textfield text is selected, then other text is pasted over,
		//this registers as 2 events. One will remove the highlighted text FIRST, THEN the new text
		//is pasted in.
		retString += ":::";
		for (int i = 0; i < prev.length(); i++)
		{
			addChar = true;
			for (int j = 0; j < curr.length(); j++)
				if (prev.charAt(i) == curr.charAt(j))
					addChar = false;
			if (addChar)
				retString += prev.charAt(i);
		}
		return retString;

	}

	private boolean checkVars()
	{
		emptyAllowed = emptyChkBx.isSelected();
		numbersAllowed = numbersAllowedChkBx.isSelected();
		lettersAllowed = lettersAllowedChkBx.isSelected();

		illegalChars = illegalCharsTxt.getText();
		requiredChars = requiredCharsTxt.getText();

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
		String checkString = createString();
		checkString = checkString.substring(checkString.indexOf(".....") + 5);
		String[] checkStringArray = checkString.split("-----");
		String[][] checkStringArrVars = new String[checkStringArray.length][];
		for (int i = 0; i < checkStringArrVars.length; i++)
			checkStringArrVars[i] = checkStringArray[i].split(":::::");
		
		String temp = "";
		for (int i = 0; i < checkStringArrVars.length; i++)
		{
			for (int j = 0; j < checkStringArrVars[i].length; j++)
				temp += checkStringArrVars[i][j];
			System.out.println("Ill String " + i + ": " + temp);
			temp = "";
		}
		
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
		String checkString = createString();
		checkString = checkString.substring(0, checkString.indexOf("....."));
		String[] checkStringArray = checkString.split("-----");
		String[][] checkStringArrVars = new String[checkStringArray.length][];
		for (int i = 0; i < checkStringArrVars.length; i++)
			checkStringArrVars[i] = checkStringArray[i].split(":::::");
		
		String temp = "";
		for (int i = 0; i < checkStringArrVars.length; i++)
		{
			for (int j = 0; j < checkStringArrVars[i].length; j++)
				temp += checkStringArrVars[i][j];
			System.out.println("Req String " + i + ": " + temp);
			temp = "";
		}
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

	public String getValueAt(int row, int column)
	{
		return charCombos[column].getSelectedItem().toString();
	}


	private String createString()
	{
		String retString = "";
		
		reqVarString = new String[reqTableModel.getRowCount()];
		illVarString = new String[illTableModel.getRowCount()];
		
		for (int i = 0; i < reqVarString.length; i++)
			reqVarString[i] = new String();
		
		for (int i = 0; i < reqVarString.length; i++)
			illVarString[i] = new String();
		
		for (int i = 0; i < reqVarString.length; i++)
		{
			for (int j = 0; j < reqTableModel.getColumnCount(); j++)	
				reqVarString[i] += reqTableModel.getValueAt(i, j) + ":::::";
			reqVarString[i] += "-----";
		}
		
		for (int i = 0; i < illVarString.length; i++)
		{
			for (int j = 0; j < illTableModel.getColumnCount(); j++)
				illVarString[i] += illTableModel.getValueAt(i, j) + ":::::";
			illVarString[i] += "-----";
		}
		
		for (int i = 0; i < reqVarString.length; i++)
			retString += reqVarString[i];
		
		retString += ".....";
		
		for (int i = 0; i < illVarString.length; i++)
			retString += illVarString[i];
		
		return retString;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == okBtn)
			checkVars();
		else if (source == cancelBtn)
			dispose();
		else if (source == helpBtn)
			JOptionPane.showMessageDialog(null, "Adding text to required chars populates table.\n" + 
					"Click on cells in columns labeled \"Count\", \"Char Before\", & \"Char After\" to see combo box.\n" + 
					"Beginning means the character needs to be at the beginning, not just can. Same for end.\n" + 
					"Throws means, if that character isn't following the required rules, an exception of some kind is throw.\n");

	}
}


