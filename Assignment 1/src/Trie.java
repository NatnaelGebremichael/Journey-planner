import java.util.ArrayList;
import java.util.List;

public class Trie {
    private final TrieNode root = new TrieNode();

    //inserts a a word into the trie tree
    //if it doesn't exist already
    public void insert(String word, Stop stop) {
        TrieNode current = root;
        //loops through the word Until the Word is in the Trie
        for (char c : word.toCharArray()) {
            //Checks if the Node Contains the Character
            //if not makes a new Node
            //if it does changes the the current node to the node that Contains the character
            //if not returns null
            if (!current.childCharacter.contains(c)) {

                TrieNode newTrie = new TrieNode();
                current.addChildCharacter(c);
                current.addChildNode(newTrie);

                current = newTrie;
                current.addChar(c);

            } else {

                for (TrieNode t : current.getChildren()) {
                    if (t.getChar() == c) {
                        current = t;
                    }
                }
            }

        }
        current.addWord(word);
        current.setS(stop);
        current.stops.add(stop);

    }

    public List<Stop> get(char[] word) {
        TrieNode current = root;
        //loops through the word Until
        //the last node of the Word is found or the one of the letters is not found
        for (char c : word) {
            //Checks if the Node Contains the Character
            //if it does changes the the current node to the node that Contains the character
            //if not returns null
            if (!current.getCharacter().contains(c)) {
                return null;
            } else {
                for (TrieNode t : current.getChildren()) {
                    if (t.getChar() == c) {
                        current = t;
                    }
                }
            }

        }
        if (current.isWord != null) {
            return current.stops;
        } else
            return null;

    }

    //gets all the Nodes
    public List<Stop> getAll(char[] prefix) {
        TrieNode current = root;
        List<Stop> results = new ArrayList<>();
        //loops through the word Until
        //the last node of the Word is found or the one of the letters is not found
        for (char c : prefix) {
            //Checks that the Node Contains the Character
            if (!current.getCharacter().contains(c)) {
                return null;
            } else {
                for (TrieNode t : current.getChildren()) {
                    if (t.getChar() == c) {
                        current = t;
                    }
                }
            }
        }

        current.getAllFrom(current, results);
        return results;
    }
}

class TrieNode {
    private final List<TrieNode> children = new ArrayList<>(); //Hold the Children Node
    public ArrayList<Stop> stops = new ArrayList<>();
    public List<Character> childCharacter = new ArrayList<>(); //hold the Child character for each Node
    public char character; //holds character of each Node
    public String isWord = null; //Holds the a Word When it is inside the Trie
    public Stop s = null;


    public TrieNode() {

    }


    /**
     * Getters
     */
    public char getChar() {
        return character;
    }

    public List<TrieNode> getChildren() {
        return children;
    }

    public List<Character> getCharacter() {
        return childCharacter;
    }

    public void getAllFrom(TrieNode node, List<Stop> results) {
        //results.add(node.stops);
        results.addAll(node.stops);

        for (TrieNode child : node.getChildren()) {
            getAllFrom(child, results);
        }
    }

    /**
     * adders / setters
     */
    public void addChildNode(TrieNode child) {
        this.children.add(child);
    }

    public void addChildCharacter(Character character) {
        this.childCharacter.add(character);
    }

    public void addChar(char c) {
        this.character = c;
    }

    public void addWord(String c) {
        isWord += c;
    }

    public void setS(Stop s) {
        this.s = s;
    }



}
