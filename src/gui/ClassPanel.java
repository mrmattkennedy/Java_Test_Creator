package gui;
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

		classInput = new JTextField("H:\\My Documents\\JTC\\Java_Test_Creator-master\\Test_File.java");

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
					System.out.println(line);
					fileOutput.append(line);
					fileOutput.append("\n");
				}

				br.close();

				String varBrackets = createString();
				if (varBrackets == null) {
					JOptionPane.showMessageDialog(null, "Something went wrong.");
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
			//Temp is the first line, where the class name is held.
			String temp = fileOutput.substring(fileOutput.indexOf("class"), fileOutput.indexOf("{")+1).toString().replaceAll("\\p{Z}","");
			
			//Trims everything off prior to the start of the class name, then sends this to a new variable.
			String className = temp.substring(temp.indexOf("class") + 5);
			
			//className will just be the name of the class.
			className = getClassName(className);

			//If there is no instance of the class name after the first class name
			if (fileOutput.indexOf(className, fileOutput.indexOf(className) + className.length() + 1) < 0)
				//Return what is left after the word "class".
				return fileOutput.substring(fileOutput.indexOf("class") + 5);
			else
				/* Search through the string, starting at the word "class".
				 * Go through the string, starting after the class name, until you find
				 * the class name again (the constructor).
				 * Return everything between class and right before the constructor.
				 */
				return fileOutput.substring(fileOutput.indexOf("class"), fileOutput.indexOf(className, fileOutput.indexOf(className) + className.length()+1));
		} catch (Exception e) {
			return null;
		}
	}

	private void parseString(String varBrackets)
	{
		//Remove all leading and trailing whitespace, as well as new lines and spaces.
		varBrackets = varBrackets.trim().replaceAll("\n ", "");
		
		//Trims everything off prior to the start of the class name, then sends this to a new variable. 
		String className = varBrackets.substring(0, varBrackets.indexOf("{"));
		
		//className will just be the name of the class, sent without whitespace.
		className = getClassName(className.replaceAll("\\s+",""));

		//Deliminate lines with line separator.
		String[] varLines = varBrackets.split("\\r?\\n|\\r");
		
		String varName = "";
		String varType = "";
		String line = "";
		boolean varStatic = false;
		
		//Start at 1, because first line is the class declaration.
		for (int i = 1; i < varLines.length - 1; i++)
		{
			line = varLines[i];
			varName = "";
			varType = "";
			varStatic = false;
			
			//If there is string left without whitespace
			if (line.replaceAll("\\s+","").length() > 1)
			{
				//If there are paranthesis, return.
				if (line.contains("(") || line.contains(")"))
					return;

				//If the command doesn't end on this line, it is multi lined.
				if (!line.contains(";")) 
				{
					//Progress through the lines until ; is found. Increment line as well.
					int multiLineVar = 0;
					while (!line.contains(";")) 
					{
						multiLineVar++;
						line += varLines[i + multiLineVar];
					}
					i += multiLineVar;
				}

				//If the line is static
				if (line.contains("static"))
					varStatic = true;
				
				//Remove trailing or leading whitespace.
 				line = line.trim();
				varType = getVarType(line);
				varName = getVarName(line, varType);
				
				//If there is anything after the actual name, remove it.
				if (varName.contains("="))
					varName = varName.substring(0, varName.indexOf("=")).trim();
				//If the var name isn't empty and the var type isn't empty.
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
		//Loops through all primitive var types.
		for (String type : primitiveVarTypes)
			
			//line is a primitive var type
			if( line.contains(type)) 
			{
				//Starting spot of the type.
				int index = line.indexOf(type);
				
				//If the index is 0.
				if (index == 0) {
					//There is whitespace immediately after the type.
					if (isWSAfter(line, index, type))
						return type;
				//If the index is at the end of the line.
				} else if (index == (line.length() - type.length())) {
					if (isWSBefore(line, index))
						return type;
				//If the index is in the middle of the line, check type is standalone word.
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
			//Gets the substring after the type and before the semicolon, removes all whitespace.
			return line.substring(line.indexOf(varType) + varType.length() + 1, line.indexOf(';') ).replaceAll("\\s+","");
		} catch (Exception e) {
			return "";
		}
	}

} 
