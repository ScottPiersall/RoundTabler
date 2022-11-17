package RoundTabler;



public class ScanResult{

	public String TableName;
	public String TableColumn;
    public String HTMLEmphasizedResult;
	public String MatchType;    //"PCIDSS or NACHA"
    public int ConfidenceLevel;
    public String MatchResultRule;

	public ScanResult(){
		super();
		TableName = "NOT SET";
		TableColumn = "NOT SET";
		HTMLEmphasizedResult = "NOT SET";
		MatchType = "NOT SET";
		ConfidenceLevel = 0;
		MatchResultRule = "NOT SET";
	}

	
	public String toString(){
	
	return "<TR><TD>" + TableName + "</TD>" +
	    "<TD>" + TableColumn + "</TD>"+
	    "<TD>" + MatchType + "</TD>"+
		"<TD>"  + HTMLEmphasizedResult + "</TD>"+
		"<TD ALIGN=\"RIGHT\">"  + String.format("%,d", ConfidenceLevel ) + "</TD>"+
		"<TD>" + MatchResultRule + "</TD>" +
		"</TR>";

	}


}