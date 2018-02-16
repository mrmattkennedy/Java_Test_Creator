import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class StringIllTableModel extends AbstractTableModel {
	private final String[] columnNames = {"Char", 
			"Always", 
			"At most", 
			"Beginning", 
			"Ending", 
			"Throws"};

	private ArrayList<Character> entryList;
	private ArrayList<Boolean> alwaysIllegal;
	private ArrayList<String> charCounts;
	private ArrayList<Boolean> charBeginning;
	private ArrayList<Boolean> charEnd;
	private ArrayList<Boolean> expThrows;

	public StringIllTableModel()
	{
		entryList = new ArrayList<Character>();
		alwaysIllegal = new ArrayList<Boolean>();
		charCounts = new ArrayList<String>();
		charBeginning = new ArrayList<Boolean>();
		charEnd = new ArrayList<Boolean>();
		expThrows = new ArrayList<Boolean>();
	}

	public void addEntry(char c)
	{
		entryList.add(c);
		alwaysIllegal.add(true);
		charCounts.add("0");
		charBeginning.add(false);
		charEnd.add(false);
		expThrows.add(false);

		repaintTable();
	}
	
	public void removeAll()
	{
		entryList.clear();
		alwaysIllegal.clear();
		charCounts.clear();
		charBeginning.clear();
		charEnd.clear();
		expThrows.clear();
	}

	private void repaintTable() 
	{
		fireTableRowsInserted(0, entryList.size() - 1);
		fireTableRowsUpdated(0, entryList.size() - 1);
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
			alwaysIllegal.remove(index);
			charCounts.remove(index);
			charBeginning.remove(index);
			charEnd.remove(index);
			expThrows.remove(index);
		}	

		repaintTable();
	}

	@Override
	public int getRowCount() {
		return entryList.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

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
		if (col == 0)
			return false;

		if (col == 1)
			return true;

		if (col == 5)
			return true;

		if ((boolean)getValueAt(row, 1) == true)
			return false;

		return true;
	}


	@Override
	public void setValueAt(Object inValue, int inRow, int inCol)
	{
		int size = entryList.size();
		switch (inCol)
		{
			case(1):
				charBeginning.set(inRow, false);
				charEnd.set(inRow, false);
				alwaysIllegal.set(inRow, !alwaysIllegal.get(inRow));
				
				if (alwaysIllegal.get(inRow))
					charCounts.set(inRow, "0");
				else
					charCounts.set(inRow, "1");
				break;
			case (2):
				if (size - 1 < inRow)
					charCounts.add((String)inValue);
				else
					charCounts.set(inRow, ((String)inValue));
				break;
			case(3):
				charBeginning.set(inRow, !charBeginning.get(inRow));
				break;
			case(4):
				charEnd.set(inRow, !charEnd.get(inRow));
				break;
			case(5):
				expThrows.set(inRow, !expThrows.get(inRow));
				break;
		}
		repaintTable();
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (alwaysIllegal.get(rowIndex) == true)
			switch (columnIndex)
			{
			case (0):
				return entryList.get(rowIndex);
			case (1):
				return alwaysIllegal.get(rowIndex);
			case (2):
				return "0";
			case 3:
				return false;
			case 4:
				return false;
			case (5):
				return expThrows.get(rowIndex);
			}
		else
			switch (columnIndex)
			{
			case(0):
				return entryList.get(rowIndex);
			case(1):
				return alwaysIllegal.get(rowIndex);
			case(2):
				return charCounts.get(rowIndex);
			case(3):
				return charBeginning.get(rowIndex);
			case(4):
				return charEnd.get(rowIndex);
			case(5):
				return expThrows.get(rowIndex);
			}
		return null;
	}

}
