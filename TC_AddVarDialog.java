import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Arrays;

public class TC_AddVarDialog extends JDialog implements ActionListener
{
	private JTextField varNameTxt;
	private JComboBox<String> varTypeCombo;
	private JTextField varClassNameTxt;
	private JCheckBox varStaticChkBx;

	private String varName = "";
	private String varType = "";
	private String varClass = "";
	private boolean varStatic = false;

	private JButton okBtn;
	private JButton cancelBtn;

	private final String[] primitiveVarTypes = {
		"byte",
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

	private static final String keywords[] = { "abstract", "assert", "boolean",
	"break", "byte", "case", "catch", "char", "class", "const",
	"continue", "default", "do", "double", "else", "extends", "false",
	"final", "finally", "float", "for", "goto", "if", "implements",
	"import", "instanceof", "int", "interface", "long", "native",
	"new", "null", "package", "private", "protected", "public",
	"return", "short", "static", "strictfp", "super", "switch",
	"synchronized", "this", "throw", "throws", "transient", "true",
	"try", "void", "volatile", "while" };

	
	private final int numPanels = 5;
	private JPanel[] gridPanel;

	private final TC_VarsPanel paFrame;
	
	public TC_AddVarDialog(TC_VarsPanel paFrame)
	{

		gridPanel = new JPanel[numPanels];
		for (int i = 0; i < numPanels; i++)
			gridPanel[i] = new JPanel();

		varNameTxt = new JTextField("");
		varTypeCombo = new JComboBox<String>(primitiveVarTypes);
		varClassNameTxt = new JTextField("");
		varStaticChkBx = new JCheckBox("");
	

		varNameTxt.setPreferredSize(new Dimension(100, 20));
		varClassNameTxt.setPreferredSize(new Dimension(100, 20));
		
		okBtn = new JButton("Ok");
		cancelBtn = new JButton("Cancel");

		okBtn.addActionListener(this);
		cancelBtn.addActionListener(this);

		gridPanel[0].add(new JLabel("Enter the variable name: "));
		gridPanel[0].add(varNameTxt);
		gridPanel[1].add(new JLabel("Select the variable type: "));
		gridPanel[1].add(varTypeCombo);
		gridPanel[2].add(new JLabel("Enter the variable class name: "));
		gridPanel[2].add(varClassNameTxt);
		gridPanel[3].add(new JLabel("Is the variable static: "));
		gridPanel[3].add(varStaticChkBx);
		gridPanel[4].add(okBtn);
		gridPanel[4].add(cancelBtn);

		for (int i = 0; i < numPanels; i++)
			add(gridPanel[i]);

		this.paFrame = paFrame;

		setLayout(new GridLayout(numPanels, 1));
		setSize(350, 200);
        	setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}

	private boolean varsGood()
	{
		varName = varNameTxt.getText();
		varType = varTypeCombo.getSelectedItem().toString();
		varClass = varClassNameTxt.getText();
		varStatic = varStaticChkBx.isSelected();

		if (!nameGood()) {
			varNameTxt.setBackground(new Color(255, 69, 0));
			return false;
		} else
			varNameTxt.setBackground(new Color(255, 255, 255));
		if (!classGood()){
			varClassNameTxt.setBackground(new Color(255, 69, 0));
			return false;
		} else
			varClassNameTxt.setBackground(new Color(255, 255, 255));
		return true;
	}

	private boolean nameGood()
	{
		if (varName.equals(""))
			return false;

		if (varName.length() - varName.replaceAll("\\s+", "").length() != 0)
			return false;
		
		if (!Character.isJavaIdentifierStart(varName.charAt(0)))
			return false;
			
		for (int i = 1; i < varName.length(); i++) 
		{
			if (!Character.isJavaIdentifierPart(varName.charAt(i)))
				return false;
		}

		if (isJavaKeyword(varName))
			return false;

		if (varName.equals(varClass))
			return false;

		return true;
	}


	private boolean classGood()
	{
		if (varClass.equals(""))
			return false;
		
		if (varClass.length() - varClass.replaceAll("\\s+", "").length() != 0)
			return false;

		if (!Character.isJavaIdentifierStart(varClass.charAt(0)))
			return false;
			
		for (int i = 1; i < varClass.length(); i++) 
		{
			if (!Character.isJavaIdentifierPart(varClass.charAt(i)))
				return false;
		}

		if (isJavaKeyword(varClass))
			return false;
		return true;
	}

	public void actionPerformed(ActionEvent arg0) 
	{
		Object source = arg0.getSource();

		if (source == okBtn && varsGood()) {
			paFrame.addVariable(varName + ", " + varType + ", " + varClass + ", " + varStatic);
			dispose();
		}
		else if (source == cancelBtn)
			dispose();
	}


        public static boolean isJavaKeyword(String keyword) 
	{
            return (Arrays.binarySearch(keywords, keyword) >= 0);
        }
}
