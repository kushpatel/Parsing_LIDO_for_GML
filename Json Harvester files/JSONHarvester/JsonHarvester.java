package JSONHarvester;
import java.io.*;

public class JsonHarvester
{
  
  private final static int workingDir = 0;
  private final static int jarExec = 1;
  private final static int xslFile = 2;
  private final static int sourcePath = 3;
  private final static int outputFile = 4;
  private final static int errorLog = 5;
  private final static int gatherErrorLog = 6;
  
  private final static int numArgs = 8;
  
  public static void main(String [] args)throws FileNotFoundException, IOException
  {
    try
    {
      String[] arguments = parseParams(args[0]);
      XmlToJson.harvestJson(arguments);
    }
    catch(Exception e)
    {
      System.out.println("Error: Parameters file not specified.");
    }
    
    /*String[] arguments = new String[numArgs];
    
    arguments[workingDir] = "C:\\Parsing_LIDO_for_GML";                 //should be where the saxon9he.jar is located
    arguments[jarExec] = "saxon9he.jar";                                //name of the jar package to execute
    arguments[xslFile] = "C:\\Parsing_LIDO_for_GML\\xml_extract.xsl";   //full path to where the xsl file is located
    arguments[sourcePath] = "C:\\Users\\acdr4\\Desktop\\RDFer_v3\\harvest\\data\\lido";          //full path to where the source xml files are
    arguments[outputFile] = "C:\\Users\\acdr4\\Desktop\\out.json";                                 //name of the output file...default path is in the working dir
    arguments[errorLog] = "C:\\Parsing_LIDO_for_GML\\error_log_json.txt";  //full path to where the error log file to write is located
    arguments[gatherErrorLog] = ""; //"C:\\Parsing_LIDO_for_GML\\Error_Logs\\error_log_json1.txt";   //full path of where to gather error log files from
    
    XmlToJson.harvestJson(arguments);
    */
  }

  /* parse the params file provided as argument on command line
    return an array of strings of arguments */
  public static String[] parseParams(String paramsFile)
  {
    String[] arguments = new String[numArgs];
    try
    {
      // Open the file
      FileInputStream fstream = new FileInputStream(paramsFile);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
  
      String strLine;
      int i = 0;
      //Read File Line By Line
      while ((strLine = br.readLine()) != null) 
      {
        String param = parseLine(strLine);
        //check if param is not null
        if(!param.equals(""))
        {
          //key word NONE indicates empty string for gatherErrorLog!
          if(param.equals("NONE"))
          {
            arguments[i] = "";
          }
          else
          {
            arguments[i] = param;
          }
          i++;
        }
        
      }
  
      //Close the input stream
      in.close();
    }
    catch (Exception e)
    {
      //Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
    return arguments;
  }

  /* parse a single line read in from params file provided as argument
    return the value given after the '=' sign */
  public static String parseLine(String line)
  {
    String param = "";
    //search for equals sign in the line
    int idx = line.indexOf('=');
    if(idx < 0 || line.substring(0,1).equals("#"))
    {
      //this skips lines with comments and blank lines
      return param;
    }
    else
    {
      //skip two indices...equals char and space after that
      param = line.substring(idx+2);
    }
    return param;
  }
}