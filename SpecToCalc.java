import java.util.*;
import java.io.*;
public class SpecToCalc {


	public static void main(String[] args) throws Exception {
	
		String inputFile = "input.txt";
		String outputFile = "output.txt";
		
		
		Start(inputFile, outputFile);
	}
	
	private static void Start(String inputFile, String outputFile) throws Exception
	{
		Scanner sc = new Scanner(new File(inputFile));
		PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
		int validLineCnt = 0;
		int currentReadLine = 1;
		sc.useDelimiter("\\n"); // delim with newline
		
		String ReferenceId = "";
		String LineNumber = "";
		String DataType = "";
		String Description = "";
		String TaCalcNotes = "";
		String Calculation = "";
		String FieldName = "";
		String InternalFieldName = "";
		String allowNegative = "false";
		String precisionType = ".Zero";
		
		while(sc.hasNext())
		{
			String outString;
			String inputTextRow = sc.next();
			ArrayList<String> info = new ArrayList<String>();

				
				Scanner rowScanner = new Scanner(inputTextRow);
				rowScanner.useDelimiter("\\t");	//tab
				
				while(rowScanner.hasNext())
				{
					info.add(rowScanner.next());
				}
				
				if (info.size() == 5)
				{
					int i = 0;
					ReferenceId = info.get(i++);
					LineNumber = info.get(i++);
					DataType = info.get(i++);
					Description = info.get(i++);
					TaCalcNotes = info.get(i++);
					//Calculation = rowScanner.next();
	
					FieldName = "TestField";
					 InternalFieldName = ToInternalFieldName(FieldName);
					
					 allowNegative = "false";
					 precisionType = ".Zero";
					

				
				outString = Money(ReferenceId, LineNumber, DataType, Description, TaCalcNotes, FieldName, InternalFieldName, Calculation);
				
				//next = next.replace("$","");	//remove $ char
				//next = next.replace(",","");	//remove , char
				writer.println(outString);
				validLineCnt++;
				
				}
				else
				{
				System.out.println("Something is wrong with " + currentReadLine); 
				}
				currentReadLine++;

		}
			writer.close();
			System.out.println(validLineCnt + " Lines processed...done!");
	}
	
	private static String ToInternalFieldName(String FieldName)
	{
		return "_" + FieldName.substring(0,1).toLowerCase() + FieldName.substring(1,FieldName.length());
	}
	
	private static String Money(String ReferenceId, String LineNumber, String DataType, String Description, String TaCalcNotes, String FieldName, String InternalFieldName, String Calculation)
	{
		String allowNegative = "false";
		
		if (DataType.equals("Money (allow negative)"))
			allowNegative = "true";
		
		
		return		"#region decimal "+FieldName+" (Line "+LineNumber+")\n"
					+ "internal Calculatable<decimal, RoundedToTheNearestInteger> "+InternalFieldName+";\n"
					+ "/// <summary> \n"
					+ "/// "+Description+"  (Calculatable) \n"
					+ "/// Reference Number "+ReferenceId+"\n"
					+ "/// </summary> \n"
					+ "[Money(AllowNegative = "+allowNegative+", Precision = PrecisionType.Zero)] \n"
					+ "[Description(\""+Description+"\"), Category(\"Category\"), ReferenceNumber(\""+ReferenceId+"\"), LineNumber(\""+LineNumber+"\")] \n"
					+ "public decimal "+FieldName+" { get { return _"+InternalFieldName+".Calculate("+FieldName+"_Calculation); } } \n"
					+ "private decimal "+FieldName+"_Calculation() \n"
					+ "{ \n"
					+ "\t "+Calculation+"\n"
					+ "\t //TODO: Enter code for "+FieldName+" calculation \n"
					+ "} \n"
					+ "#endregion "+FieldName+" \n"
					+ "\n\n";
	}
}
