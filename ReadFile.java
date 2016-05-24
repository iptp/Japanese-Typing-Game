import java.io.*;
import java.util.ArrayList;

/**
 * Final class for reading lines from a file and returning it's 
 * content in the form of an ArrayList of Word Classes.
 * 
 * getListOfWords reads lines as:
 * あ;a
 * にほんご;nihongo
 * 
 * And return them as:
 * ArrayList<Word> array = { Word(あ, a), Word(にほんご, nihongo) };
 */
public final class ReadFile {
    
    public static ArrayList<Word> getListOfWords(String file_name) {
        FileReader in = null;
        ArrayList<Word> array = null;
        try {
            in = new FileReader(file_name);
            array = new ArrayList<Word>();
            
            BufferedReader br = new BufferedReader(in);
            String line;
            while((line = br.readLine()) != null) {
                //System.out.println(line);
                String[] parts = line.split(";");
                array.add(new Word(parts[0], parts[1]));
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
        finally {
            if(in != null) {
                try {
                    in.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
            
        return array;
    }
}
