import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.lang.StringBuffer;

public class ClassPanel extends JPanel implements ActionListener {

	private JTextField classInput;

	private JButton searchBtn;
	private JButton createVarsBtn;
	private JButton cancelBtn;

	private Font btnFont;
	
	private StringBuffer fileOutput;
	
	private TabbedPane parentFrame;

	private String[] primitiveVarTypes = {
	"byte",
	"short",
	"int",
	"long",
	"float",
	"double",
	"char",
	"String",
	"boolean",
	};
	
	public ClassPanel(TabbedPane parentFrame) 
	{
		setLayout(new GridBagLayout());

		classInput = new JTextField("Enter class path here..");

		searchBtn = new JButton("Search");
		createVarsBtn = new JButton("Create");
		cancelBtn = new JButton("Exit");
	
		btnFont = new Font("Helvetica", Font.BOLD, 14);

		fileOutput = new StringBuffer();

		this.parentFrame = parentFrame;

		setFonts();
		addBtnListeners();
		setLayout();
	}

	private void setLayout() 
	{
		GridBagConstraints gbCons = new GridBagConstraints();
		gbCons.fill = GridBagConstraints.HORIZONTAL;

		gbCons.anchor = GridBagConstraints.NORTH;
		gbCons.insets = new Insets(10,5,10,5);
		gbCons.gridwidth = 3;
		gbCons.gridx = 0;
		gbCons.gridy = 0; 
		gbCons.ipadx = 200;
		gbCons.ipady = 10;
		add(classInput, gbCons);

		gbCons.anchor = GridBagConstraints.CENTER;
		gbCons.insets = new Insets(10,5,10,5);
		gbCons.gridwidth = 1;
		gbCons.gridheight = 2;
		gbCons.gridx = 0;
		gbCons.gridy = 1; 
		gbCons.ipadx = 30;
		gbCons.ipady = 15;
		add(searchBtn, gbCons);

		gbCons.insets = new Insets(10,5,10,5);
		gbCons.gridwidth = 1;
		gbCons.gridheight = 2;
		gbCons.gridx = 1;
		gbCons.gridy = 1; 
		gbCons.ipadx = 30;
		gbCons.ipady = 15;
		add(createVarsBtn, gbCons);

		gbCons.insets = new Insets(10,5,10,5);
		gbCons.gridwidth = 1;
		gbCons.gridheight = 2;
		gbCons.gridx = 2;
		gbCons.gridy = 1; 
		gbCons.ipadx = 30;
		gbCons.ipady = 15;
		add(cancelBtn, gbCons);
	}

	private void setFonts() 
	{
		searchBtn.setFont(btnFont);	
		createVarsBtn.setFont(btnFont);	
		cancelBtn.setFont(btnFont);	
	}

	private void addBtnListeners() 
	{
		searchBtn.addActionListener(this);
		createVarsBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
	}

	public StringBuffer getFileOutput() {
		return fileOutput;
	}

	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		if (source == searchBtn) 
		{
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter javaFilter = new FileNameExtensionFilter("Java Files", "java");
			chooser.setFileFilter(javaFilter);

        		int status = chooser.showOpenDialog(null);

			if (status == JFileChooser.APPROVE_OPTION)  
			{
				String filename = chooser.getSelectedFile().getAbsolutePath();
				classInput.setText(filename);
			}

		} 
		else if (source == createVarsBtn) 
		{
			try 
			{
				fileOutput.delete(0, fileOutput.length());

				File classFile = new File(classInput.getText());
				BufferedReader br = new BufferedReader(new FileReader(classFile));

				String line;
				while ((line = br.readLine()) != null) {
					fileOutput.append(line);
					fileOutput.append("\n");
				}

				br.close();

				String varBrackets = createString();
				if (varBrackets == null) {
					JOptionPane.showMessageDialog(null, "Something went Wrong.");
					return;
				}

				parseString(varBrackets);
					
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else if (source == cancelBtn)
			System.exit(0);
        }
	
	private String createString() 
	{
		try
		{
			String temp = fileOutput.substring(fileOutput.indexOf("class"), fileOutput.indexOf("{")+1).toString().replaceAll("\\p{Z}","");
			String className = temp.substring(temp.indexOf("class") + 5);
			className = getClassName(className);

			if (fileOutput.indexOf(className, fileOutput.indexOf(className) + className.length() + 1) < 0)
				return fileOutput.substring(fileOutput.indexOf("class") + 5);
			else
				return fileOutput.substring(fileOutput.indexOf("class"), fileOutput.indexOf(className, fileOutput.indexOf(className) + className.length()+1));
		} catch (Exception e) {
			return null;
		}
	}

	private void parseString(String varBrackets)
	{
		varBrackets = varBrackets.trim().replaceAll("\n ", "");
		
		String className = varBrackets.substring(0, varBrackets.indexOf("{"));
		className = getClassName(className.replaceAll("\\s+",""));

		String[] varLines = varBrackets.split(System.getProperty("line.separator"));
		
		String varName = "";
		String varType = "";
		String line = "";
		boolean varStatic = false;
		for (int i = 1; i < varLines.length - 1; i++)
		{
			line = varLines[i];
			varName = "";
			varType = "";
			varStatic = false;
			if (line.replaceAll("\\s+","").length() > 1)
			{
				if (line.contains("(") || line.contains(")"))
					return;

				if (!line.contains(";")) 
				{
					int multiLineVar = 0;
					while (!line.contains(";")) 
					{
						multiLineVar++;
						line += varLines[i + multiLineVar];
					}
					i += multiLineVar;
				}

				if (line.contains("static"))
					varStatic = true;

 				line = line.trim();
				varType = getVarType(line);
				varName = getVarName(line, varType);
					
				if (varName.contains("="))
					varName = varName.substring(0, varName.indexOf("=")).trim();
				if ((!varName.equals("")) && (!varType.equals("")))
					parentFrame.addVariable(varName + ", " + varType + ", " + className + ", " + varStatic);
			}
		}
	}
	
	private String getClassName(String className)
	{
		if (className.contains("class"))
			className = className.substring(5);
		if (className.contains("extends"))
			className = className.substring(0, className.indexOf("extends"));
		else if (className.contains("implements"))
			className = className.substring(0, className.indexOf("implements"));

		return className;
	}

	private String getVarType(String line)
	{
		for (String type : primitiveVarTypes)
			if( line.contains(type)) 
			{
				int index = line.indexOf(type);
				if (index == 0) {
					if (isWSAfter(line, index, type))
						return type;
				} else if (index == (line.length() - type.length())) {
					if (isWSBefore(line, index))
						return type;
				} else if ((index < (line.length() - type.length()))) {
					if (isWSBefore(line, index) && isWSAfter(line, index, type))
						return type;
				}
			}
		return "";
	}
	
	private boolean isWSAfter(String line, int index, String type)
	{
		if (Character.isWhitespace(line.charAt(index + type.length())))
			return true;
		return false;	
	}

	private boolean isWSBefore(String line, int index)
	{
		if (Character.isWhitespace(line.charAt(index - 1)))
			return true;
		return false;
	}
	
	private String getVarName(String line, String varType)
	{
		try {
			return line.substring(line.indexOf(varType) + varType.length() + 1, line.indexOf(';') ).replaceAll("\\s+","");
		} catch (Exception e) {
			return "";
		}
	}

} 
