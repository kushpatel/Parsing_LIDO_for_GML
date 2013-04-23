import java.io.*;
import java.util.Scanner;
public class WriteFile
{
  public static void main(String[] args) throws IOException 
  {
    File writeFile = new File("write.txt");
    writeFile.delete();
    PrintWriter out = new PrintWriter(new FileWriter(writeFile, true));
    Scanner in = new Scanner(System.in);
    //BufferedReader in = new BufferedReader(new FileReader(writeFile));
    String str = "";
    for(int i=0; i < 5; i++){
      str = in.nextLine();
      out.write(str);
    }
    in.close();
    System.out.println(str);
    out.close();
  }
}