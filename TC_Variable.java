public class TC_Variable {
	
	private String varName;
	private String varType;
	private String varClass;
	private boolean varStatic;
	private boolean varVerified;

	public TC_Variable() 
	{
		varName = "";
		varType = "";
		varClass = "";
		varStatic = false;
		varVerified = false;
	}

	public TC_Variable(String varName, String varType, String varClass, boolean varStatic)
	{
		this.varName = varName;
		this.varType = varType;
		this.varClass = varClass;
		this.varStatic = varStatic;
		varVerified = false;
	}

	public String getVarName()
	{
		return varName;
	}

	public void setVarName(String varName)
	{
		this.varName = varName;
	}

	public String getVarType()
	{
		return varType;
	}

	public void setVarType(String varType)
	{
		this.varType = varType;
	}

	public String getVarClass() 
	{
		return varClass;
	}

	public void setVarClass(String varClass) 
	{
		this.varClass = varClass;
	}

	public boolean isVarStatic() 
	{
		return varStatic;
	}

	public void setVarStatic(boolean varStatic) 
	{
		this.varStatic = varStatic;
	}

	public boolean isVerified() 
	{
		return varVerified;
	}

	public void setVarVerified(boolean varVerified) 
	{
		this.varVerified = varVerified;
	}

	public void createJUnitString(String varString)
	{
		String[] tempArr = varString.split("---");
		System.out.println(tempArr);
	}

}
