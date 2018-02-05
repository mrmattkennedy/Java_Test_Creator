/*
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import java.awt.Color;

public class TC_TableCellRenderer extends DefaultTableCellRenderer
{
	private String[] primitiveVarTypes = {"byte",
	"byte[]",
	"short",
	"short[]",
	"int",
	"int[]",
	"long",
	"long[]",
	"float",
	"float[]",
	"double",
	"double[]",
	"char",
	"char[]",
	"String",
	"String[]",
	"boolean",
	"boolean[]"
	};

	private int test;
	
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component c = (TC_TableCellRenderer)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		String varType = (String)value;

		if (column == 1 && isPrimitive(varType))
		{
			setBackground(Color.GREEN);
			c.setBackground(Color.GREEN);
		} else {
			setBackground(Color.WHITE);
			c.setBackground(Color.WHITE);
		
		} 
		return this;
	}

	private boolean isPrimitive(String varType)
	{
		for (String type : primitiveVarTypes)
			if (varType.equals(type))
				return true;

		return false;
	}
}
*/
