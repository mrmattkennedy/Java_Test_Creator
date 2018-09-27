package gui;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TableModel extends AbstractTableModel
{

	private ArrayList<VariableObj> vars;
	private String[] columnNames = {"Variable name", "Variable type", "Class", "Static", "Verified"};

	public TableModel()
	{
		vars = new ArrayList<VariableObj>();
	}
	
	public void addVariable(String var)
	{
		for(VariableObj name : vars) 
			if(var.substring(0, var.indexOf(", ")).equals(name.getVarName()))
				return;

		String[] temp = var.split(", ");
		vars.add(new VariableObj(temp[0], temp[1], temp[2], Boolean.parseBoolean(temp[3])));
		
		fireTableRowsInserted(vars.size()-1, vars.size()-1);
	}

	public void removeVariable(int index)
	{
		vars.remove(index);
		repaintTable();
	}

	public void varChecked(int row, String varString)
	{
		vars.get(row).setVarVerified(true);
		vars.get(row).createJUnitString(varString);
	}

	private void repaintTable() 
	{
		fireTableRowsInserted(0, vars.size() - 1);
		fireTableRowsUpdated(0, vars.size() - 1);
	}

	@Override
	public int getRowCount()
	{
		return vars.size();
	}
	
	@Override
	 public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	 public Object getValueAt(int row, int column)
	{
		VariableObj currentVar = vars.get(row);

		switch (column)
		{
			case 0:
				return currentVar.getVarName();
			case 1:
				return currentVar.getVarType();
			case 2:
				return currentVar.getVarClass();
			case 3:
				if (currentVar.isVarStatic())
					return "True";
				return "False";
			case 4:
				if (currentVar.isVerified())
					return "Yes";
				return "No";
		}
		return null;
	}
/*
	private String getVarName(String var)
	{
		return var.substring(0, var.indexOf(", "));
	}

	private String getVarType(String var)
	{
		return var.substring(var.indexOf(", ") + 2);
	}
*/
}
