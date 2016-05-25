import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

public class JapaneseTyping extends World {
    public static int WIDTH = 14;
    public static int HEIGHT = 16;
    public static int CELLSIZE = 30;
    public static int ENDLINE = HEIGHT - 3;
    
    private ArrayList<Word> list_of_words;
    private int wordIndex;
    
    String typing;
    
    public static int score;
   
    public JapaneseTyping() {    
        super(WIDTH, HEIGHT, CELLSIZE);
        Greenfoot.setSpeed(50);
        list_of_words = ReadFile.getListOfWords("nihongo.txt");
        wordIndex = 0;
        addWordToScreen(wordIndex);
        typing = "";
        score = 0;
    }
    
    public void started() {
        GreenfootImage bg = new GreenfootImage(WIDTH*CELLSIZE, HEIGHT*CELLSIZE);
        bg.drawLine(0, ENDLINE*CELLSIZE, WIDTH*CELLSIZE, ENDLINE*CELLSIZE);
        setBackground(bg);
    }
    
    public void act() {
        showText("Score: " + score, WIDTH-3, 0);
        showText(typing, WIDTH/2, HEIGHT - 2);
        
        String key = Greenfoot.getKey();
        if(key != null) {
             //checks to see if the key is an alphabet letter
            if(key == "space") {
                typing = "";
            }
            else if(typing.length() > 0 && key == "backspace") {
                typing = typing.substring(0, typing.length()-1);
            }
            else if(key.length() == 1 && checkRange(key.charAt(0))) {
                typing += key.toLowerCase();
            }
            
            if(checkCorrectAnswer(typing)) {
                score += list_of_words.get(wordIndex).getScore();
                removeObject(list_of_words.get(wordIndex));
                wordIndex++;
                checkWin();
                addWordToScreen(wordIndex);
                typing = "";
            }
        }
    }
    
    public void addWordToScreen(int index) {
        addObject(list_of_words.get(index), WIDTH/2, 1);
    }
    
    private boolean checkRange(char c) {
        return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
    }
    
    public void endGame() {
        showText("Game Over", WIDTH/2, HEIGHT/2);
        Greenfoot.stop();
    }
    
    public boolean checkCorrectAnswer(String typing) {
        String wordOnScreen = list_of_words.get(wordIndex).getBack();
        return typing.equals(wordOnScreen);
    }
    
    public void checkWin() {
        if(wordIndex == list_of_words.size()) {
            showText("You won", WIDTH/2, HEIGHT/2);
            Greenfoot.stop();
        }
    }
}
