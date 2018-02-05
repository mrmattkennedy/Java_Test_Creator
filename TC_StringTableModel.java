import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class TC_StringTableModel extends AbstractTableModel {
	private final String[] columnNames = {"Char", "Quantity", "Char Before?", "Char After?", "Specific Char Before?", "Specific Char After?"};
	private ArrayList<Character> entryList;
	private ArrayList<Boolean> charBefore;
	private ArrayList<Boolean> charAfter;
	private final int columnCount = 6;
	private TC_StringVarDialogBox paFrame;

	public TC_StringTableModel(TC_StringVarDialogBox paFrame)
	{
		this.paFrame = paFrame;
		entryList = new ArrayList<Character>();
		charBefore = new ArrayList<Boolean>();
		charAfter = new ArrayList<Boolean>();
	}
	
	public void addRow()
	{
		entryList.add(null);
	}
	
	public boolean noEntry(char checkChar)
	{
		for (int i = 0; i < entryList.size(); i++)
			if (entryList.get(i) == checkChar)
				return false;
		return true;
	}
	
	public void addEntry(Character c)
	{
		entryList.add(c);
		charBefore.add(false);
		charAfter.add(false);
		repaintTable();
	}
	
	private void repaintTable() 
	{
		fireTableRowsInserted(0, entryList.size() - 1);
		fireTableRowsUpdated(0, entryList.size() - 1);
	}

	
	public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return entryList.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 1) {
            return false;
        } else {
            return true;
        }
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex)
		{
			case(0):
				return entryList.get(rowIndex);
			case(1):
				return paFrame.getDropDownValue(0);
			case(2):
				return charBefore.get(rowIndex);
			case(3):
				return charAfter.get(rowIndex);
			case(4):
				return paFrame.getDropDownValue(1);
			case(5):
				return paFrame.getDropDownValue(2);
		}
		return null;
	}

}
