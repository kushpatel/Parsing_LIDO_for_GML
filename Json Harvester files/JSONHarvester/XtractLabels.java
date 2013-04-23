package JSONHarvester;
import java.io.*;
import java.util.ArrayList;
public class XtractLabels
{
  
  private final static String xslFile = "C:\\Parsing_LIDO_for_GML\\extract_labels.xsl";
  private final static String workingDir = "C:\\Parsing_LIDO_for_GML";                //should be where the saxon9he.jar is located
  private final static String outputFile = "labels.txt";
  private final static String errorLog = "C:\\Parsing_LIDO_for_GML\\error_log_labels.txt";
  public static void main(String[] args) throws FileNotFoundException, IOException
  {
    
    //create an array containing all the files on which the command is to be run
    //File[] listOfFiles = gatherFiles();
    File[] listOfFiles = gatherFilesFromErrorLog();
    
    //Setup the command line executor and the working directory
    SysCommandExecutor cmdExecutor = new SysCommandExecutor();
    cmdExecutor.setWorkingDirectory(workingDir);
    
    //setup a file to write json to in steps .. continue to write to file without erasing past data
    File writeFile = new File(outputFile);
    writeFile.delete();     //clean the file if a file with the given name existed
    PrintWriter out = new PrintWriter(new FileWriter(writeFile, true));
    
    //setup a file to write filenames on which there was JSONException...continue to write without erasing
    File writeErrors = new File(errorLog);
    writeErrors.delete();     //clean the file if a file with the given name existed
    PrintWriter errorsOut = new PrintWriter(new FileWriter(writeErrors, true));
    
    //run the command in a loop on all the files in the listOfFiles array
    int json_errors = 0;
    int cmd_errors = 0;
    for(int i = 0; i < listOfFiles.length; i++)   //i < listOfFiles.length
    {
      String textOut = "";
      try
      {
        textOut = extractTextFromXml(extractXml(listOfFiles[i], cmdExecutor));
        //textOut = extractTextFromXml(extractXml(new File("C:\\Parsing_LIDO_for_GML\\bac4-lido\\bac4-lido1-1000\\oai_tms.ycba.yale.edu_1029.xml"), cmdExecutor));
        //System.out.println(jsonOut);
        out.write(textOut + "\n");
        System.out.println("Reached file: " + (i + 1) + "/" + listOfFiles.length);
      }
      catch(JSONException je)
      {
        json_errors++;
        String filePath = listOfFiles[i].getAbsolutePath();
        errorsOut.write(filePath + "\n");
        System.out.println("Error: JSONException");
      }
      catch(Exception e)
      {
        cmd_errors++;
        System.out.println("Error in cmd: " + cmdExecutor.getCommandError());
      }
    }
    System.out.println("JSONException on " + json_errors + " files.");
    System.out.println("Command line error on " + cmd_errors + " files.");
    out.close();
    errorsOut.close();
  }
  
  /*
   * gathers all the files into a huge file array
   * @return listOfFiles the array of files from which xml is to be extracted
   * */
  public static File[] gatherFiles()
  {
    //gather the files in folders into arrays of files 
    File folder1 = new File("C:\\Parsing_LIDO_for_GML\\bac4-lido\\bac4-lido1-1000");
    File[] listOfFiles1 = folder1.listFiles();
    File folder2 = new File("C:\\Parsing_LIDO_for_GML\\bac4-lido\\bac4-lido1001-2000");
    File[] listOfFiles2 = folder2.listFiles();
    File folder3 = new File("C:\\Parsing_LIDO_for_GML\\bac4-lido\\bac4-lido2001-3000");
    File[] listOfFiles3 = folder3.listFiles();
    File folder4 = new File("C:\\Parsing_LIDO_for_GML\\bac4-lido\\bac4-lido3001-4000");
    File[] listOfFiles4 = folder4.listFiles();
    File folder5 = new File("C:\\Parsing_LIDO_for_GML\\bac4-lido\\bac4-lido4001-5000");
    File[] listOfFiles5 = folder5.listFiles();
    File folder6 = new File("C:\\Parsing_LIDO_for_GML\\bac4-lido\\bac4-lido5001-6000");
    File[] listOfFiles6 = folder6.listFiles();
    
    //create an array containing all the files on which the command is to be run
    File[][] listOfFileArrays = {listOfFiles1,listOfFiles2,listOfFiles3,listOfFiles4,listOfFiles5,listOfFiles6};
    File[] listOfFiles = createListOfFiles(listOfFileArrays);
    return listOfFiles;
  }
  
  /*
   * merges all the elements of a 2D array into a single array
   * @param fileArrays an array of array of files
   * @return listOfFiles an array of files
   * */
  public static File[] createListOfFiles(File[][] fileArrays)
  {
    int totalFiles = 0;
    for(int i = 0; i < fileArrays.length; i++)
    {
      totalFiles = totalFiles + fileArrays[i].length;
    }
    
    File[] listOfFiles = new File[totalFiles];
    int pos = 0;
    for(int i = 0; i < fileArrays.length; i++)
    {
      for(int j = 0; j < fileArrays[i].length; j++)
      {
        listOfFiles[pos] = fileArrays[i][j];
        pos++;
      }
    }
    return listOfFiles;
  }
  
  public static File[] gatherFilesFromErrorLog()throws IOException
  {
    ArrayList<File> fileArr = new ArrayList<File>();
    try
    {
      FileInputStream fstream = new FileInputStream("C:\\Parsing_LIDO_for_GML\\error_log_labels_3.txt");
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      int pos = 0;
      //Read File Line By Line
      while ((strLine = br.readLine()) != null)   
      {
        File file = new File(strLine);
        fileArr.add(file);
      }
      //Close the input stream
      in.close();
    }
    catch(FileNotFoundException e)
    {
      System.out.println("Error log not found.");
    }
    int fileArrSize = fileArr.size();
    File[] fileArray = new File[fileArrSize];
    for(int i = 0; i < fileArrSize; i++)
    {
      fileArray[i] = fileArr.get(i);
    }
    return fileArray;
  }
  
  /*
   * @param f the xml file from which xml is to be extracted
   * @param cmdExecutor an instance of SysCommandExecutor that runs commands on command line
   * @return cmdOutput a string containing all the xml data extracted
   * */
  public static String extractXml(File f, SysCommandExecutor cmdExecutor) throws Exception
  {    
    String commandLine = "java -jar saxon9he.jar "+ f +" "+ xslFile;
    int exitStatus = cmdExecutor.runCommand(commandLine);
    String cmdOutput = cmdExecutor.getCommandOutput();
    if(exitStatus != 0)
    {
      throw new Exception();
    }
    //cmdOutput = stripWhiteSpace(cmdOutput);
    return cmdOutput;
  }

  /*
   * @param xml the string containing the xml data from which text is to be extracted
   * @return output the string obtained from xml string of form "SubSite, UnitType"
   * */
  public static String extractTextFromXml(String xml)throws JSONException
  {
    String xmlString = xml;
    String output = "";
    JSONObject jo = XML.toJSONObject(xmlString);
    //System.out.println(jo.toString());
    JSONObject labels_jo = jo.getJSONObject("Labels");
    JSONArray appellation_jarr = labels_jo.getJSONArray("lido:appellationValue");
    
    String subsite = "";
    String unitType = "";
    for(int i = 0; i < appellation_jarr.length(); i++)
    {
      JSONObject jobj = appellation_jarr.getJSONObject(i);
      String label = "";
      try
      {
        label = jobj.getString("lido:label");
      }
      catch(JSONException je)
      {
        continue;
      }
      if(label.equals("SubSite"))
      {
        try
        {
          subsite = jobj.getString("content");
        }
        catch(JSONException je)
        {
          subsite = "" + jobj.getInt("content");
        }
      }
      else if(label.equals("UnitType"))
      {
        unitType = jobj.getString("content");
      }
    }
    output = subsite + ", " + unitType;
    return output;
  }
}