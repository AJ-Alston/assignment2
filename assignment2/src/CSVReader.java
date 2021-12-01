import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
Class that takes a dictionary where every line has two strings (1st: word, 2nd: frequency used number)
seperated by a comma, places strings in a hashmap, and returns an arraylist of all words
*/
public class CSVReader {
    //Data member for dictionary file path
    String path;

    //Constructor to fill path string
    CSVReader(String filesource){
        this.path=filesource;
    }

    //Array of strings used to split each line of file, hashmap to hold dictionary word and its frequency
    String[] values;
    HashMap<String,Double> map = new HashMap<>();

    /*
    Function that reads file line by line, places words in an array list, and places word+frequency combo in
    hashmap
    */
    public ArrayList<String> read() {

        String line = "";
        ArrayList<String> strings = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            //Read file by line places strings in an array, add word to list, and add key+values to map
            while ((line = reader.readLine()) != null) {

                values = line.split(",");
                strings.add(values[0]);
                map.put(values[0], Double.parseDouble(values[1]));


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }
}
