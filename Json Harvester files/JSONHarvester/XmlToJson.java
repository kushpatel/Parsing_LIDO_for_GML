/*
 * AUTHOR : Kush Patel
 * */

package JSONHarvester;
import java.io.*;
import java.util.ArrayList;
public class XmlToJson
{
  private final static int workingDir = 0;
  private final static int jarExec = 1;
  private final static int xslFile = 2;
  private final static int sourcePath = 3;
  private final static int outputFile = 4;
  private final static int errorLog = 5;
  private final static int gatherErrorLog = 6;
  
  public static void harvestJson(String[] args) throws FileNotFoundException, IOException
  {
    
    //create an array containing all the files on which the command is to be run
    File[] listOfFiles;
    if(args[gatherErrorLog].equals(""))
    {
      listOfFiles = gatherFiles(args[sourcePath]);
    }
    else
    {
      listOfFiles = gatherFilesFromErrorLog(args[gatherErrorLog]);
    }
    
    //Setup the command line executor and the working directory
    SysCommandExecutor cmdExecutor = new SysCommandExecutor();
    cmdExecutor.setWorkingDirectory(args[workingDir]);
    
    //setup a file to write json to in steps .. continue to write to file without erasing past data
    File writeFile = new File(args[outputFile]);
    writeFile.delete();     //clean the file if a file with the given name existed
    PrintWriter out = new PrintWriter(new FileWriter(writeFile, true));
    out.write("{\"RecordsJson\":[");
    
    //setup a file to write filenames on which there was JSONException...continue to write without erasing
    File writeErrors = new File(args[errorLog]);
    writeErrors.delete();     //clean the file if a file with the given name existed
    PrintWriter errorsOut = new PrintWriter(new FileWriter(writeErrors, true));
    
    //run the command in a loop on all the files in the listOfFiles array
    int json_errors = 0;
    int cmd_errors = 0;
    for(int i = 0; i < listOfFiles.length; i++)   //i < listOfFiles.length
    {
      String jsonOut = "";
      try
      {
        jsonOut = xmlStringToJson(extractXml(listOfFiles[i], cmdExecutor, args));
        //jsonOut = xmlStringToJson(extractXml(new File("C:\\Parsing_LIDO_for_GML\\bac4-lido\\bac4-lido1-1000\\oai_tms.ycba.yale.edu_1010.xml"), cmdExecutor));
        //System.out.println(jsonOut);
        out.write(jsonOut);
        if(i != listOfFiles.length - 1 && !jsonOut.equals(""))        //so as not to print the comma after the last element in array
        {
          out.write(",");
        }
        System.out.println("Reached file: " + (i + 1) + "/" + listOfFiles.length);
        
      }
      catch(JSONException je)
      {
        json_errors++;
        String filePath = listOfFiles[i].getAbsolutePath();
        //System.out.println(filePath);
        errorsOut.write(filePath + "\n");
        System.out.println("Error: JSONException");
      }
      catch(Exception e)
      {
        cmd_errors++;
        System.out.println("Error in cmd: " + cmdExecutor.getCommandError());
      }
    }
    out.write("]}");
   //errorsOut.write("JSONException on " + json_errors + " files.\n");
   //errorsOut.write("Command line error on " + cmd_errors + " files.\n");
    System.out.println("JSONException on " + json_errors + " files.");
    System.out.println("Command line error on " + cmd_errors + " files.");
    out.close();
    errorsOut.close();
    
  }
  
  /*
   * gathers all the files into a huge file array
   * @param path the full path to the folder with the files
   * @return listOfFiles the array of files from which xml is to be extracted
   * */
  public static File[] gatherFiles(String path)
  {
    File folder = new File(path);
    File[] listOfFiles = folder.listFiles();
    return listOfFiles;
  }
  
  /*
   * gathers all the files into a huge file array
   * @return listOfFiles the array of files from which xml is to be extracted
   * */
/*  public static File[] gatherFiles()
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
  }*/
  
  /*
   * merges all the elements of a 2D array into a single array
   * @param fileArrays an array of array of files
   * @return listOfFiles an array of files
   * */
 /* public static File[] createListOfFiles(File[][] fileArrays)
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
  */
  public static File[] gatherFilesFromErrorLog(String log)throws IOException
  {
    ArrayList<File> fileArr = new ArrayList<File>();
    try
    {
      FileInputStream fstream = new FileInputStream(log);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
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
  
  public static String stripWhiteSpace(String xmlString)
  {
    xmlString = xmlString.replaceAll("(\\r|\\n)", "");
    String xml = "";
    int pos = 0;
    while(pos < xmlString.length())
    {
      int openerPos = findChar('<',pos,xmlString);
      int closerPos = findChar('>',pos,xmlString);
      if(openerPos >= 0 && closerPos >= 0)
      {
        xml = xml + xmlString.substring(openerPos,closerPos + 1);
        pos = closerPos + 1;
        if(pos < xmlString.length() && xmlString.charAt(pos) != ' ')  //assuming that random word won't be dangling out at the end of xml
        {
          openerPos = findChar('<',pos,xmlString);
          xml = xml + xmlString.substring(pos, openerPos);
          pos = openerPos;
        }
      }
      else
      {
        break;
      }
    }
    return xml;
  }
  
  public static int findChar(char ch, int startPos, String xmlString)
  {
    int pos = startPos;
    char c = xmlString.charAt(pos);
    do
    {
      c = xmlString.charAt(pos);
      pos++;
    }
    while(pos < xmlString.length() && c != ch);
    if(pos > xmlString.length())
    {
      return -1;
    }
    return pos - 1;
  }
  
  /*
   * @param f the xml file from which xml is to be extracted
   * @param cmdExecutor an instance of SysCommandExecutor that runs commands on command line
   * @return cmdOutput a string containing all the xml data extracted
   * */
  public static String extractXml(File f, SysCommandExecutor cmdExecutor, String[] args) throws Exception
  {    
    String commandLine = "java -jar "+ args[jarExec] +" "+ f +" "+ args[xslFile];
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
   * @param xml the string containing the xml data to be converted to JSON
   * @return output the JSON string obtained from xml string
   * */
  public static String xmlStringToJson(String xml)throws JSONException
  {
    String xmlString = xml;
    String output = "";
    JSONObject jo = XML.toJSONObject(xmlString);
    //System.out.println(jo.toString());
    
    JSONArray subjectsArray = extractSubjectsJson(jo);
    //System.out.println(subjectsArray.toString());
    
    JSONArray eventsArray = extractEventsJson(jo);
    //System.out.println(eventsArray.toString());
    
    String title = extractTitleJson(jo);
    //System.out.println(title);
    
    String url = extractUrlJson(jo);
    //System.out.println(url.toString());
    
    String thumb = extractThumb(jo);
    //System.out.println(thumb);
    
    String author = extractAuthor(jo);
    //System.out.println(author);
    
    JSONObject recordJson = new JSONObject(); //put if statements to check for null!!
    if(subjectsArray != null)
    {
      recordJson = recordJson.put("Subjects",subjectsArray);
    }
    else
    {
      //recordJson = recordJson.put("Subjects","");
      //returns an empty string if the subjectsArray is empty
      return "";
    }
    /*if(eventsArray != null)
    {
      recordJson = recordJson.put("Events",eventsArray);
    }
    else
    {
      recordJson = recordJson.put("Events","");
    }*/
      
    recordJson = recordJson.put("Title",title);
    recordJson = recordJson.put("Url",url);
    recordJson = recordJson.put("Thumb", thumb);
    recordJson = recordJson.put("Author",author);
    //System.out.println(recordJson.toString());
    
    output = recordJson.toString();
    return output;
  }
  
  /*
   * makes a new JSONObject with only the required fields from the subject tag/key
   * @param jo the JSONObject from which the fields are to be extracted
   * @return mySubjects a newly made JSONArray e.g.
   * [{"place":"United Kingdom","coordinates":"54.0000, -4.5000"}]
   * */
  public static JSONArray extractSubjectsJson(JSONObject jo)throws JSONException
  {
    JSONObject record_jo = jo.getJSONObject("Record");
    JSONObject subjectsWrap_jo;
    try
    {
      subjectsWrap_jo = record_jo.getJSONObject("SubjectsWrap");
    }
    catch(JSONException je)   //if subjectsWrap is null
    {
      return null;
    }
    
    JSONArray mySubjects = new JSONArray();
    if(subjectsWrap_jo != null)
    {
      try  //for case where there's only one subject..JSONObject sufficient
      {
        JSONObject subject_jo = subjectsWrap_jo.getJSONObject("Subject");
        JSONObject mySubject = extractSubject(subject_jo);
        if(mySubject != null)
        {
          mySubjects = mySubjects.put(mySubject);
        }
      }
      catch(JSONException je)   //for multiple event...JSONArray has to be used
      {
        JSONArray subjects_jsonarr = subjectsWrap_jo.getJSONArray("Subject");
        for(int i = 0; i < subjects_jsonarr.length(); i++)
        {
          JSONObject subject_jo = subjects_jsonarr.getJSONObject(i);
          JSONObject mySubject = extractSubject(subject_jo);
          if(mySubject != null)
          {
            mySubjects = mySubjects.put(mySubject);
          }
        }
      }
    }
    return mySubjects;
  }
  
  /*
   * makes a new JSONObject from a given subject tag/key
   * @param subject_jo the JSONObject containing data about a single subject
   * @return myJson the new JSONObject created e.g.
   * {"place":"United Kingdom","coordinates":"54.0000, -4.5000"}
   * */
  public static JSONObject extractSubject(JSONObject subject_jo)throws JSONException
  {
    JSONObject lidoPlace_jo = subject_jo.getJSONObject("lido:place");
    
    JSONObject lidoGml_jo = lidoPlace_jo.getJSONObject("lido:gml");
    JSONObject gmlPoint_jo = lidoGml_jo.getJSONObject("gml:Point");
    
    JSONObject lidoNamePlaceSet_jo = lidoPlace_jo.getJSONObject("lido:namePlaceSet");
    
    JSONObject myJson = new JSONObject();
    String coordinates = gmlPoint_jo.getString("gml:coordinates");
    if(!coordinates.equals(""))  //no json created if coordinates are null
    {
      myJson = myJson.put("place",lidoNamePlaceSet_jo.getString("lido:appellationValue"));
      myJson = myJson.put("coordinates",coordinates);
      return myJson;
    }
   return null;
  }
  
  /*
   * makes a new JSONArray with only the required fields from the event tag/key
   * @param jo the JSONObject from which the fields are to be extracted
   * @return myEvents a newly made JSONArray e.g.
   * [{"place":"Chelsea","type":"Death place","coordinates":"51.4830, -0.1500"}]
   * */
  public static JSONArray extractEventsJson(JSONObject jo)throws JSONException
  {
    JSONObject record_jo = jo.getJSONObject("Record");
    JSONObject eventsWrap_jo;
    try
    {
      eventsWrap_jo = record_jo.getJSONObject("EventsWrap");
    }
    catch(JSONException je) //if eventsWrap is null
    {
      return null;
    }
    
    JSONArray myEvents = new JSONArray();
    if(eventsWrap_jo != null)
    {
      try  //for case where there's only one event..JSONObject sufficient
      {
        JSONObject event_jo = eventsWrap_jo.getJSONObject("Event");
        JSONObject myEvent = extractAnEvent(event_jo);
        if(myEvent != null)
        {
          myEvents = myEvents.put(myEvent);
        }
      }
      catch(JSONException je)   //for multiple event...JSONArray has to be used
      {
        JSONArray events_jsonarr = eventsWrap_jo.getJSONArray("Event");
        for(int i = 0; i < events_jsonarr.length(); i++)
        {
          JSONObject event_jo = events_jsonarr.getJSONObject(i);
          JSONObject myEvent = extractAnEvent(event_jo);
          if(myEvent != null)
          {
            myEvents = myEvents.put(myEvent);
          }
        }
      }
    }
    return myEvents;
  }
  
  
  /*
   * makes a new JSONObject from a given event tag/key
   * @param event_jo the JSONObject containing data about a single event
   * @return myJson the new JSONObject created e.g.
   * {"place":"Chelsea","type":"Death place","coordinates":"51.4830, -0.1500"}
   * */
  public static JSONObject extractAnEvent(JSONObject event_jo)throws JSONException
  {
    JSONObject lidoPlace_jo = event_jo.getJSONObject("lido:place");
    
    JSONObject lidoGml_jo = lidoPlace_jo.getJSONObject("lido:gml");
    JSONObject gmlPoint_jo = lidoGml_jo.getJSONObject("gml:Point");
    
    JSONObject lidoPlaceClass_jo = lidoPlace_jo.getJSONObject("lido:placeClassification");
    JSONObject lidoConceptID_jo = lidoPlaceClass_jo.getJSONObject("lido:conceptID");
    
    JSONObject lidoNamePlaceSet_jo = lidoPlace_jo.getJSONObject("lido:namePlaceSet");
    
    JSONObject myJson = new JSONObject();
    String coordinates = gmlPoint_jo.getString("gml:coordinates");
    if(!coordinates.equals(""))
    {
      myJson = myJson.put("coordinates",coordinates);
      myJson = myJson.put("type",lidoConceptID_jo.getString("lido:type"));
      myJson = myJson.put("place",lidoNamePlaceSet_jo.getString("lido:appellationValue"));
      return myJson;
    }
    return null;
  }
  
  /*
   * returns a string containing the title of the record
   * @param jo the JSONObject from which the title is to be extracted
   * @return title the title of the record e.g.
   * "The First Stage of Cruelty"
   * */
  public static String extractTitleJson(JSONObject jo)throws JSONException
  {
    JSONObject record_jo = jo.getJSONObject("Record");
    JSONObject titleWrap_jo = record_jo.getJSONObject("TitleWrap");
    try           //for case where there's only one title...use JSONObject
    {
      JSONObject title_jo = titleWrap_jo.getJSONObject("Title");
      JSONObject appellation_jo = title_jo.getJSONObject("lido:appellationValue");
      String title = appellation_jo.getString("content");
      return title;
    }
    catch(JSONException je)         //for case where there's preferred & alternate titles...use JSONArray and extract only preferred title
    {
      JSONArray title_jarr = titleWrap_jo.getJSONArray("Title");
      for(int i = 0; i < title_jarr.length(); i++)
      {
        JSONObject appellation_jo = title_jarr.getJSONObject(i);
        appellation_jo = appellation_jo.getJSONObject("lido:appellationValue");
        String lidoPref = appellation_jo.getString("lido:pref");
        if(lidoPref.equals("preferred"))
        {
          return appellation_jo.getString("content");
        }
      }
    }
    return "";
  }
  
  /*
   * returns a string containing the url of the record
   * @param jo the JSONObject from which the url is to be extracted
   * @return url the url of the record e.g.
   * "http://collections.britishart.yale.edu/vufind/Record/3387520"
   * */
  public static String extractUrlJson(JSONObject jo)throws JSONException
  {
    JSONObject record_jo = jo.getJSONObject("Record");
    JSONObject urlWrap_jo = record_jo.getJSONObject("UrlWrap");
    JSONObject url_jo = urlWrap_jo.getJSONObject("url");
    JSONObject recordInfoLink_jo = url_jo.getJSONObject("lido:recordInfoLink");
    
    String url = "";
    try
    {
      url = recordInfoLink_jo.getString("content");
    }
    catch(JSONException je)   //case where url is null
    {
      return url;
    }
    return url;
  }
  
  /*
   * returns a string containing the url of the thumbnail image of the record
   * @param jo the JSONObject from which the thumbnail url is to be extracted
   * @return thumb the url of the thumbnail e.g.
   * "http://collections.britishart.yale.edu:8080/MediaService/MediaService?system=tms&id=1000&size=thumb "
   * */
  public static String extractThumb(JSONObject jo)throws JSONException
  {
    JSONObject record_jo = jo.getJSONObject("Record");
    JSONObject imagesWrap_jo = record_jo.getJSONObject("ImagesWrap");
    JSONObject images_jo = imagesWrap_jo.getJSONObject("Images");
    JSONArray resourceReps_jarr = images_jo.getJSONArray("lido:resourceRepresentation");
    
    String thumb = "";
    for(int i = 0; i < resourceReps_jarr.length(); i++)
    {
      JSONObject obj = resourceReps_jarr.getJSONObject(i);
      if(obj.getString("lido:type").equals("thumb"))
      {
        JSONObject linkResource_jo = obj.getJSONObject("lido:linkResource");
        thumb = linkResource_jo.getString("content");
        return thumb;
      }
    }
    
    System.out.println(resourceReps_jarr.toString());
    return thumb;
  }
  
  /*
   * returns a string containing the author of the record
   * @param jo the JSONObject from which the author is to be extracted
   * @return author the author of the record e.g.
   * "Philip Reinagle, 1749-1833, British"
   * */
  public static String extractAuthor(JSONObject jo)throws JSONException
  {
    JSONObject record_jo = jo.getJSONObject("Record");
    JSONObject authorWrap_jo = record_jo.getJSONObject("AuthorWrap");
    
    String author = "";
    try
    {
      JSONObject author_jo = authorWrap_jo.getJSONObject("Author");
      JSONObject displayActorInRole_jo = author_jo.getJSONObject("lido:displayActorInRole");
      author = displayActorInRole_jo.getString("content");
    }
    catch(JSONException je)    //the case with multiple authors...return the first one
    {
      JSONArray author_jarr = authorWrap_jo.getJSONArray("Author");
      JSONObject author_jo = author_jarr.getJSONObject(0);
      JSONObject displayActorInRole_jo = author_jo.getJSONObject("lido:displayActorInRole");
      author = displayActorInRole_jo.getString("content");
    }
    
    return author;
    
  }
}