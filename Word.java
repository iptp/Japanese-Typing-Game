import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

public class Word extends Actor
{
    private String front, back;
    private int drawInterval;
    private int delay;
    
    public Word(String front, String back) {
        this.front = front;
        this.back = back;
        this.drawInterval = 0;
        this.delay = 70;

        GreenfootImage bg = new GreenfootImage(front, 32, Color.BLACK, null);
        setImage(bg);
    }
    
    public void act() {
        this.drawInterval++;
        while(drawInterval > this.delay) {
            setLocation(getX(), getY()+1);
            if(getY() == JapaneseTyping.ENDLINE) {
                JapaneseTyping w = getWorldOfType(JapaneseTyping.class);
                w.endGame();
            }
            drawInterval -= this.delay;
        }
    }    
    
    public String getBack() {
        return this.back;
    }
}
