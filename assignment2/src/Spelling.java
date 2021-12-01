import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//Class that builds a trie to store a dictionary and makes suggestions of words given an input string
public class Spelling {

        /*
        Data members to hold dictionary words in a list, key + value pairs (frequency number included) in
        hashmap, and root node of trie
         */
        ArrayList<String> strings;
        HashMap<String, Double> map;
        private Node trie;

        // Trie node class
        private class Node {
            //String to hold prefix of word and hashmap to hold nodes children
            String prefix;
            HashMap<Character, Node> children;

            // Boolean to indicate if node represents a complete word
            boolean isWord;

            private Node(String prefix) {
                this.prefix = prefix;
                this.children = new HashMap<Character, Node>();

            }
        }

        //Constructor that creates dictionary
        public Spelling(String filepath) {
            CSVReader reader= new CSVReader(filepath);
            strings = reader.read();
            map = reader.map;
            trie = new Node("");
            for (int i = 0; i < strings.size(); i++) {
                insertWord(strings.get(i));
            }
        }


        //Insert a word into the trie
        private void insertWord(String s) {
            /*
             Iterate through each character in the string, add character node to tree if it's not there, update
             isWord once full word is entered
            */
            Node curr = trie;
            for (int i = 0; i < s.length(); i++) {
                if (!curr.children.containsKey(s.charAt(i))) {
                    curr.children.put(s.charAt(i), new Node(s.substring(0, i + 1)));
                }

                curr = curr.children.get(s.charAt(i));
                if (i == s.length() - 1)
                    curr.isWord = true;

            }
        }


        //Find all words in trie that start with prefix
        public List<String> getSuggestions(String prefix) {
            List<String> results = new LinkedList<String>();

            // Iterate to the end of the prefix
            Node curr = trie;
            for (char c : prefix.toCharArray()) {
                if (curr.children.containsKey(c)) {
                    curr = curr.children.get(c);
                } else {
                    return results;
                }
            }

            // At the end of the prefix, find all child words
            findAllChildWords(curr, results);
            return results;
        }

        //Recursively find every child word
        private void findAllChildWords(Node node, List<String> results) {
            if (node.isWord)
                results.add(node.prefix);
            //Recursively loop through all children nodes
            for (Character c : node.children.keySet()) {
                findAllChildWords(node.children.get(c), results);
            }
        }

        /*
        Function that runs a loop through input strings characters and makes suggestion for each character
        typed in. Delete loop to run function individually for each letter typed in.
            */
        public List<List<String>> suggest (String token, int count){

            /*
            Data members to keep track of which prefix we're checking suggestions for,return list, and
            index of last elements in return list
            */
            String prefix="";
            int counter = -1;
            List<List<String>> returner = new ArrayList<>();

            //Loop through each character typed in and add suggestions to return list
            for (char c : token.toCharArray()) {
                prefix += c;

                /*
                List to keep track of all potential suggestions and array of doubles to keep track of
                frequencies
                */
                List<String> tokens = getSuggestions(prefix);
                Double[] doubler = new Double[tokens.size()];

                //Loop through potential suggestions and add frequency number to array
                for (int i = 0; i < tokens.size(); i++) {
                    doubler[i] = map.get(tokens.get(i));
                }

                //Create insertion sort object to perform insertion sort on frequencies
                InsertionSort sort = new InsertionSort();
                sort.sort(doubler);
                List<String> toReturn = new ArrayList<>();

                //If not enough suggestion, return the most frequent types with longest prefix
                if (tokens.size() < count) {
                    toReturn=returner.get(counter);
                }

                //Get words corresponding to highest frequencies and add to return list
                else {
                    for (int j = doubler.length - 1; j > doubler.length - 1 - count; j--) {
                        String s = "";
                        for (int k = 0; k < tokens.size(); k++) {
                            if (map.get(tokens.get(k)).equals(doubler[j]))
                                s = tokens.get(k);
                        }
                        toReturn.add(s);

                    }
                }
                returner.add(toReturn);
                counter++;
            }


            return returner;
        }

        //Class to perform insertion sort
        public class InsertionSort {
            public void sort(Double[] arr){
                for (int k = 0; k < arr.length; k++) {
                    double temp = arr[k];
                    int l = k - 1;
                    while (l >= 0 && arr[l] > temp) {
                        arr[l + 1] = arr[l];
                        --l;
                    }
                    arr[l + 1] = temp;
                }
            }

        }

        public static void main(String[] args) {
            //File located in the src file. Download and insert filepath containing dictionary file.
            //File must have first row, "TRUE" row, and "FALSE" row deleted

            //Enter filepath into Spelling constructor
            Spelling spell = new Spelling("/Users/penthouse/Downloads/unigram_freq.csv");


            List<List<String>> output = spell.suggest("hello",7);

            //Print all suggestions for each character entered
            for (int i = 0; i < output.size(); i++) {
                System.out.println(output.get(i));
            }

            /*
            Part 4

            When compared to frequently misspelled words, the program sometimes fails to provide the
            correct suggestions if the token entered is misspelled. It sometimes works whenever the
            count variable is closer to 7. Program can be improved if it reads file of common misspelled words,
            looped through a hashtable of key value pairs including the correctly spelled word and its common
            misspelling, and added matches to our input token to the front of our suggestions list once
            suggestions amount becomes less than count.
             */
        }


}
