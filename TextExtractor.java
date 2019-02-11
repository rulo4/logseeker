import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextExtractor {


  public static void extractRegexFromFile(File file) {
    String wholeContent;
    Pattern searchFor = Pattern.compile("public String toString\\(\\).*\\{(\\n.*)+return\\s.*.toString\\(\\);\\n.*}");
    try{
      wholeContent = Files.asCharSource(file, Charsets.UTF_8).read();
      Matcher matcher = searchFor.matcher(wholeContent);
      System.out.println(wholeContent);
      System.out.println("\n\n");
      if (matcher.find()){
        System.out.println(matcher.group(1));
      } else {
        System.out.println("No match");
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  public static void main(String[] args){
    System.out.println("File:");
    String file = new Scanner(System.in).next();
    extractRegexFromFile(new File(file));
  }
}
