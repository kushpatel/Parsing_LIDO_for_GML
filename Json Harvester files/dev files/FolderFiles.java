import java.io.*;
public class FolderFiles
{
  public static void main(String[] args) throws FileNotFoundException, Exception
  {
    //Stores the path names of all the files present in a given folder
    File folder = new File("C:\\Parsing_LIDO_for_GML\\bac4-lido\\bac4-lido1-1000");
    File[] listOfFiles = folder.listFiles();
    
    //Setup the command line executor and the working directory
    SysCommandExecutor cmdExecutor = new SysCommandExecutor();
    cmdExecutor.setWorkingDirectory("C:\\Parsing_LIDO_for_GML");
    
    File writeFile = new File("out.xml");
    writeFile.delete();
    PrintWriter out = new PrintWriter(new FileWriter(writeFile, true));
    
    //run command in a loop for each file and write to file the output of each command
    int exitStatus = 0;
    for(int i = 0; i < listOfFiles.length && exitStatus == 0; i++)
    {
      String commandLine = "java -jar saxon9he.jar "+ listOfFiles[i] +" xml_extract5.xsl";
      exitStatus = cmdExecutor.runCommand(commandLine);
      String cmdOutput = cmdExecutor.getCommandOutput();
      out.write(cmdOutput);
      System.out.println("Reached file: " + i);
    }
    System.out.println("Exited with status: " + exitStatus);
    String cmdError = cmdExecutor.getCommandError();
    out.close();
  }
}