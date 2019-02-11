import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiffExtractor {

  private static Runtime rt = Runtime.getRuntime();

  private static List<String> getModifiedFiles(String projectPath){
    List<String> modifiedFiles = new ArrayList<>();
    try {
      Process gitStatus = rt.exec("git status", null, new File(projectPath));
      BufferedReader reader = new BufferedReader(new InputStreamReader(gitStatus.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith("\tmodified:")){
          modifiedFiles.add(line.substring(10).trim());
        }
      }
      int exitVal = gitStatus.waitFor();
      if (exitVal == 0) {
        return modifiedFiles;
      } else {
        return null;
      }
    } catch (IOException | InterruptedException e){
      e.printStackTrace();
      return null;
    }
  }

  private static String getDiff(String projectPath, String filePath){
    String modifiedLines = new String();
    try {
      Process gitStatus = rt.exec("git diff " + filePath, null, new File(projectPath));
      BufferedReader reader = new BufferedReader(new InputStreamReader(gitStatus.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith("- ") || line.startsWith("+ ")){
          modifiedLines += "|" + line.substring(2) + "\n";
        }
      }
      int exitVal = gitStatus.waitFor();
      if (exitVal == 0) {
        return modifiedLines;
      } else {
        return null;
      }
    } catch (IOException | InterruptedException e){
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args){
    String report = "";
    System.out.println("Path");
    String path = new Scanner(System.in).nextLine();

    List<String> files = getModifiedFiles(path);
    files.forEach(s -> {

      System.out.println(s + getDiff(path, s));

    });
  }
}
