import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class StringReqTableModel extends AbstractTableModel {
	private final String[] columnNames = {"Char", 
			"Quantity", 
			"Beginning", 
			"Ending", 
			"Char Before", 
			"Char After",
			"Throws"};
	
	private ArrayList<Character> entryList;
	private ArrayList<String> charCounts;	
	private ArrayList<Boolean> charBeginning;
	private ArrayList<Boolean> charEnd;
	private ArrayList<String> charBefore;
	private ArrayList<String> charAfter;
	private ArrayList<Boolean> expThrows;

	public StringReqTableModel()
	{
		entryList = new ArrayList<Character>();
		charCounts = new ArrayList<String>();
		charBeginning = new ArrayList<Boolean>();
		charEnd = new ArrayList<Boolean>();
		charBefore = new ArrayList<String>();
		charAfter = new ArrayList<String>();
		expThrows = new ArrayList<Boolean>();
	}
	
	public boolean noEntry(char checkChar)
	{
		for (int i = 0; i < entryList.size(); i++)
			if (entryList.get(i) == checkChar)
				return false;
		return true;
	}
	
	public void addEntry(char c)
	{
		entryList.add(c);
		charCounts.add("1");
		charBeginning.add(false);
		charEnd.add(false);
		charBefore.add("N/A");
		charAfter.add("N/A");
		expThrows.add(false);
		repaintTable();
	}
	
	public void addPatternChar(char c, String before, String after)
	{
		entryList.add(c);
		charCounts.add("1");
		charBeginning.add(false);
		charEnd.add(false);
		charBefore.add(before);
		charAfter.add(after);
		expThrows.add(false);
		repaintTable();
	}
	
	public void removeEntry(char c)
	{
		int index = -1;
		for (int i = 0; i < entryList.size(); i++)
			if (c == entryList.get(i))
			{
				index = i;
				break;
			}
		if (index != -1)
		{
			entryList.remove(index);
			charCounts.remove(index);
			charBeginning.remove(index);
			charEnd.remove(index);
			charBefore.remove(index);
			charAfter.remove(index);
			expThrows.remove(index);
		}	
		
		repaintTable();
	}
	
	private void repaintTable() 
	{
		fireTableRowsInserted(0, entryList.size() - 1);
		fireTableRowsUpdated(0, entryList.size() - 1);
	}

	@Override
	public int getColumnCount() {
        return columnNames.length;
    }

	@Override
    public int getRowCount() {
        return entryList.size();
    }

	@Override
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
        if (col == 0) {
            return false;
        } else {
            return true;
        }
    }
    
   
    @Override
    public void setValueAt(Object inValue, int inRow, int inCol)
    {
    	int size = entryList.size();
    	switch (inCol)
    	{
    	case (1):
    		if (size - 1 < inRow)
    			charCounts.add((String)inValue);
    		else
    			charCounts.set(inRow, ((String)inValue));
    		break;
    	case(2):
    		charBeginning.set(inRow, !charBeginning.get(inRow));
    		break;
    	case(3):
    		charEnd.set(inRow, !charEnd.get(inRow));
    		break;
    	case (4):
    		if (size - 1 < inRow)
    			charBefore.add((String)inValue);
    		else
    			charBefore.set(inRow, ((String)inValue));
    		break;
    	case (5):
    		if (size - 1 < inRow)
    			charAfter.add((String)inValue);
    		else
    			charAfter.set(inRow, ((String)inValue));
    		break;
    	case(6):
    		expThrows.set(inRow, !expThrows.get(inRow));
    		break;
    	}
    }
   

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex)
		{
			case(0):
				return entryList.get(rowIndex);
			case(1):
				return charCounts.get(rowIndex);
			case(2):
				return charBeginning.get(rowIndex);
			case(3):
				return charEnd.get(rowIndex);
			case(4):
				return charBefore.get(rowIndex);
			case(5):
				return charAfter.get(rowIndex);
			case(6):
				return expThrows.get(rowIndex);
		}
		return null;
	}

}
