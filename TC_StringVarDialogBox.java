import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;

public class TC_StringVarDialogBox extends JDialog implements ActionListener
{
	private JCheckBox emptyChkBx;
	private JCheckBox canBeDefaultChkBx;
	private JCheckBox numbersAllowedChkBx;
	private JCheckBox lettersAllowedChkBx;

	private JTextField illegalCharsTxt;
	private JTextField requiredCharsTxt;
	private JComboBox<String> spaceCountCombo;

	private JCheckBox spaceAtBeginningChkBx;
	private JCheckBox spaceAtEndChkBx;

	private JButton okBtn;
	private JButton cancelBtn;

	private final int numPanels = 7;
	private JPanel[] gridPanels;

	private final int spaceCountCapacity = 20;
	private String[] spaceCountField;

	private boolean emptyAllowed;
	private boolean canBeDefault;
	private boolean numbersAllowed;
	private boolean lettersAllowed;

	private String illegalChars = "";
	private String requiredChars = "";
	private int spaceCount;
	
	private boolean spaceAtBeginning;
	private boolean spaceAtEnd;

	private Font firstChecksFont;

	private TC_VarsPanel paFrame;
	private int row;

	public TC_StringVarDialogBox(TC_VarsPanel paFrame, int row) 
	{
		firstChecksFont = new Font("Futura", Font.PLAIN, 12);
		gridPanels = new JPanel[numPanels];
		for (int i = 0; i < numPanels; i++)
			gridPanels[i] = new JPanel();

		spaceCountField = new String[spaceCountCapacity];
		for (int i = 0; i < spaceCountCapacity; i++)
			spaceCountField[i] = Integer.toString(i);


		emptyChkBx = new JCheckBox("String can be empty: ");
		canBeDefaultChkBx = new JCheckBox("String can be default value: ");
		numbersAllowedChkBx = new JCheckBox("Numbers allowed: ");
		lettersAllowedChkBx = new JCheckBox("Letters allowed: ");

		okBtn = new JButton("Ok");
		cancelBtn = new JButton("Cancel");

		okBtn.setPreferredSize(new Dimension(100, 30));
		cancelBtn.setPreferredSize(new Dimension(100, 30));

		okBtn.addActionListener(this);
		cancelBtn.addActionListener(this);

		emptyChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		canBeDefaultChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		numbersAllowedChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		lettersAllowedChkBx.setHorizontalTextPosition(SwingConstants.LEFT);

		emptyChkBx.setFont(firstChecksFont);
		canBeDefaultChkBx.setFont(firstChecksFont);
		numbersAllowedChkBx.setFont(firstChecksFont);
		lettersAllowedChkBx.setFont(firstChecksFont);

		illegalCharsTxt = new JTextField("");
		illegalCharsTxt.setPreferredSize(new Dimension(150, 20));
		requiredCharsTxt = new JTextField("");
		requiredCharsTxt.setPreferredSize(new Dimension(150, 20));
		spaceCountCombo = new JComboBox<String>(spaceCountField);
//		spaceCountTxt.setPreferredSize(new Dimension(80, 20));

		spaceAtBeginningChkBx = new JCheckBox("Space at beginning: ");
		spaceAtEndChkBx = new JCheckBox("Space at end: ");

		spaceAtBeginningChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		spaceAtEndChkBx.setHorizontalTextPosition(SwingConstants.LEFT);
		
		spaceAtBeginningChkBx.setFont(firstChecksFont);
		spaceAtEndChkBx.setFont(firstChecksFont);

		gridPanels[0].add(emptyChkBx);
		gridPanels[0].add(canBeDefaultChkBx);
		gridPanels[1].add(numbersAllowedChkBx);
		gridPanels[1].add(lettersAllowedChkBx);
		gridPanels[2].add(new JLabel("Illegal chars: "));
		gridPanels[2].add(illegalCharsTxt);
		gridPanels[3].add(new JLabel("Required chars: "));
		gridPanels[3].add(requiredCharsTxt);
		gridPanels[4].add(new JLabel("Number of spaces: "));
		gridPanels[4].add(spaceCountCombo);
		gridPanels[5].add(spaceAtBeginningChkBx);
		gridPanels[5].add(spaceAtEndChkBx);
		gridPanels[6].add(okBtn);
		gridPanels[6].add(cancelBtn);

		for (int i = 0; i < numPanels; i++)
			add(gridPanels[i]);

		this.paFrame = paFrame;
		this.row = row;

		setLayout(new GridLayout(numPanels, 1));
		setSize(400, 350);
        	setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);	
	}
	
	private boolean checkVars()
	{
		emptyAllowed = emptyChkBx.isSelected();
		canBeDefault = canBeDefaultChkBx.isSelected();
		numbersAllowed = numbersAllowedChkBx.isSelected();
		lettersAllowed = lettersAllowedChkBx.isSelected();
		spaceAtBeginning = spaceAtBeginningChkBx.isSelected();
		spaceAtEnd = spaceAtEndChkBx.isSelected();

		illegalChars = illegalCharsTxt.getText();
		requiredChars = requiredCharsTxt.getText();

		spaceCount = spaceCountCombo.getSelectedIndex();
		

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
		if (requiredChars.contains(" ") && spaceCount == 0)
			return false;

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

	private String createString()
	{
		return illegalChars + "---" + requiredChars + "---" + emptyAllowed + "---" + canBeDefault + "---" + spaceAtBeginning + "---" + spaceAtEnd + "---" + spaceCount;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		Object source = arg0.getSource();

		if (source == okBtn && checkVars()) {
			paFrame.varChecked(row, createString());
			dispose();		
		} else if (source == cancelBtn)
			dispose();
	}
}
