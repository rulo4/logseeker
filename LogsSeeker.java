import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LogsSeeker {

  public static List<String> getLogs(List<Path> classes){
    List<String> logs = new ArrayList<>();
    Scanner sc;
    String tmpLine;
    String tmpString;
    int ln;
    for(Path classFile : classes){
      try {
        LineNumberReader lnr = new LineNumberReader(new FileReader(classFile.toString()));
        while((tmpLine = lnr.readLine()) != null){
          sc = new Scanner(tmpLine);
          ln = lnr.getLineNumber();
          while (sc.hasNext()) {
            tmpString = sc.nextLine();
            if (tmpString.contains("more than 1 record: \" + bean);")){
              tmpString = classFile.toString() + "| " + ln + "| " + tmpString;
              if (!tmpString.endsWith(";")){
                tmpString = tmpString + lnr.readLine();
              }
              logs.add(tmpString);
            }
          }
        }
      }catch (Exception e){
        e.printStackTrace();
      }
    }
    return logs;
  }


  public static List<Path> getAllClasses(String uri) {
    ArrayList<Path> paths = new ArrayList<>();
    try{
      Files.walk(Paths.get(uri)).filter(Files::isRegularFile).forEach(path -> {
        if (path.toString().endsWith(".java")){
          paths.add(path);
        }
      });
    }
    catch (IOException e){
      e.printStackTrace();
    }
    return paths;
  }

  public static void main(String[] args) {

    System.out.println("Path:");
    String uri = new Scanner(System.in).next();
    List<Path> classes ;
    List<String> logs;

    classes = LogsSeeker.getAllClasses(uri);
    logs = LogsSeeker.getLogs(classes);


    File output = new File(Paths.get(uri).getFileName().toString() + ".txt");
    try {
      FileWriter fw = new FileWriter(output);
      BufferedWriter bw = new BufferedWriter(fw);
      logs.forEach(s -> {
        try {
          bw.write(s);
          bw.write("\n");
        } catch (IOException e){
          e.printStackTrace();
        }
      });
      bw.close();
      fw.close();
    } catch (IOException e){
      e.printStackTrace();
    }
  }


}

