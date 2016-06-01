# Playable game

In the version 1 of our game, we will develop the basics of a typing game using Greenfoot by following the list bellow:
- Reading words from .txt file
- Displaying words on the screen
- Processing the user input
- Handling the user input
- Delete word on "space" key press
- Make the words descend
- Making the finish line (lose game when it crosses)
- Make the words appear randomly

The game will consist of Japanese :jp: words falling from the sky while the user try to type the correct word. If the word crosses the finish line, the game ends. In case of a correct typing, another word appear.

<h2 align="center">Setup</h2>

In order for us to have an easy way to choose words for our game we are going to have them
read from a .txt file and loaded into an `ArrayList` of words. The first thing to do here is to create
the `Word` class to be our **Actor**.

###### Create a new subclass of Actor in the project

In our game, a `Word` is a combination of 2 strings, the part that the user sees and the part
that needs to be typed in order to get it right.

```java
public class Word extends Actor {
    private String front, back;

    public void act() {

    }
}
```

Next, we implement the constructor for the `Word` actor as well as setting it's background
as the front part of the string we received, since that's what we want the user to see. We will need to import the `java.awt.Color` in order to use it.

```java
import java.awt.Color;

public class Word extends Actor {
    private String front, back;

    public Word(String front, String back) {
        this.front = front;
        this.back = back;

        GreenfootImage bg = new GreenfootImage(front, 32, Color.BLACK, null);
        setImage(bg);
    }

    public void act() {

    }
}
```

The above code creates a `GreenfootImage` by using a string and giving it's size and color.

<h2 align="center">ReadFile class</h2>

To read from a **txt** file we are going to use a class developed to extract words from a text file
and return them as an ArrayList of words. Since this tutorial won't cover Java File I/O operations you
can have a look [here](http://www.tutorialspoint.com/java/java_files_io.htm) for more details.

###### Right-click and Save-link-as [here](https://raw.githubusercontent.com/iptp/Japanese-Typing-Game/master/ReadFile.java) to download the `ReadFile` class. Put it inside your project folder.

If the class is not appearing at the bottom of your greenfoot interface, just restart the greenfoot software in order for it to recognize the new class in the folder.

Basically this class has one single static method that you can use as following:

```java
ArrayList<Word> getListOfWords(String file_name);
```

This will read from a file specified by the `file_name` parameter. Note that the file needs to be
inside your project folder as well.

The above method reads a **txt** file:

```txt
あ;a
にほんご;nihongo
```

And return an `ArrayList` of words containing each line as a `Word`, where **あ** and **にほんご** are the
front parts and **a** and **nihongo** are the back parts.

An example txt file is available:
###### Right-click and Save-link-as [here](https://github.com/iptp/Japanese-Typing-Game/raw/master/nihongo.txt). Put it inside your projects folder.

<h2 align="center">Words on screen</h2>

For displaying words on screen, let's delete the default `MyWorld` that the Greenfoot creates for us and create a new world from scratch. We can name it `JapaneseTyping`. Then, let's define some global variables for our game, like the **width**, **height**, **cellsize** and **endline** position. Also set the game **speed** to 50.

```java
public class JapaneseTyping extends World {

    public static int WIDTH = 14;
    public static int HEIGHT = 16;
    public static int CELLSIZE = 30;
    public static int ENDLINE = HEIGHT - 3;

    public JapaneseTyping() {    
        super(WIDTH, HEIGHT, CELLSIZE);
        Greenfoot.setSpeed(50);
    }
}
```

To get the list of words from our **txt** file let's declare an ArrayList of words. Don't forget to add the import for the ArrayList type in the top of the file: `import java.util.ArrayList;`. Then, we use the `ReadFile.getListOfWords` method to get the words from the txt file.

```java
import java.util.ArrayList;

public class JapaneseTyping extends World {

    public static int WIDTH = 14;
    public static int HEIGHT = 16;
    public static int CELLSIZE = 30;
    public static int ENDLINE = HEIGHT - 3;

    private ArrayList<Word> list_of_words;

    public JapaneseTyping() {    
        super(WIDTH, HEIGHT, CELLSIZE);
        Greenfoot.setSpeed(50);

        list_of_words = ReadFile.getListOfWords("nihongo.txt");
    }
}
```

Another thing we are going to need is a way to control which word is to be displayed in the screen
by the time the user has got some words typed right. To do so we will create an index variable. Then, we can start adding words to the screen by using Greenfoot's `addObject` method and the index we've just created. Let's declare a new method called `addWordToScreen` that will be in charge of what it's name already says, adding words to the screen.

```java
public class JapaneseTyping extends World {

    public static int WIDTH = 14;
    public static int HEIGHT = 16;
    public static int CELLSIZE = 30;
    public static int ENDLINE = HEIGHT - 3;

    private int wordIndex;

    private ArrayList<Word> list_of_words;

    public JapaneseTyping() {    
        super(WIDTH, HEIGHT, CELLSIZE);
        Greenfoot.setSpeed(50);

        wordIndex = 0; //initial position for the array
        list_of_words = ReadFile.getListOfWords("nihongo.txt");
        addWordToScreen(wordIndex);
    }
}
```

See how the `addWordToScreen` method receives the `wordIndex` parameter for it to know which word from
the `list_of_words` array it will add to the screen.

Now, declare the `addWordToScreen` method:

```java
public void addWordToScreen(int index) {
    addObject(list_of_words.get(index), WIDTH/2, 1);
}
```

If we execute our code now we should already see the first word from the `nihongo.txt` file appearing at the top of our screen.

<h2 align="center">Process user input</h2>

In order to get the word that the user is typing we will use the `JapaneseTyping` world class as a controller. The idea is to check if the user is typing at every frame of our game and get what key is being typed to update in our screen. We can use the **`act`** method of the `JapaneseTyping` class to do that.

To show a `String` in the screen we can use the `showText` method from the World class and for getting the user input we can use the `Greenfoot.getKey()` method. This second method return a String to us corresponding to the most recently pressed key, since the last time it was called. If no key was pressed since this method was last called, it will return null.

```java
public class JapaneseTyping extends World {

    //Variable declarations

    public JapaneseTyping() {    
        //Constructor code
    }

    public void act() {
        String key = Greenfoot.getKey();
        if(key != null) {
            showText(key, WIDTH/2, HEIGHT - 2);
        }
    }
}
```

We can see now that every time we press a new key, the world is showing the corresponding string as a text in the bottom.

<h2 align="center">Handle user input</h2>

Since we already have the user input, we can start thinking about how to handle it. If you type different keys while our game is running you can see that we are getting strings like "a", "space", "backspace", and many others. However, since our game needs to deal with words, we only want to get the actual alphabet letters to form the word. In order to do so we will create a 'String' that will be what our user is currently typing. This variable will accumulate the letters to form a word and we will check wether it's currently equal to the answer for the current word on screen. If yes, than the user has typed correctly.

```java
public class JapaneseTyping extends World {

    //Variables declaration
    String typing;

    public JapaneseTyping() {    
        //Constructor code
        typing = "";
    }

    public void act() {
        showText(typing, WIDTH/2, HEIGHT - 2);

        String key = Greenfoot.getKey();
        if(key != null) {
            //Only keys with 1 character will be considered
            if(key.length() == 1) {
                typing += key.toLowerCase();
            }
        }
    }
}
```

Note how we use the `showText` method with the `typing` variable to display the word at every act cycle and how we accumulate the key that is being typed by calling the `toLowerCase` method on it and adding it to the `typing` variable.

As you can see, our string initiates as "" and we only accumulate the new key into the `typing` variable when it's length is equal to 1. But, we are still getting for example the numbers, who's key length are also 1. To fix that, we can create a new method that checks wether a single `char` is one of the letters from the alphabet and use that.

```java
private boolean checkRange(char c) {
    return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
}
```

```java
public class JapaneseTyping extends World {

    //Variables declaration
    String typing;

    public JapaneseTyping() {    
        //Constructor code
        typing = "";
    }

    public void act() {
        showText(typing, WIDTH/2, HEIGHT - 2);

        String key = Greenfoot.getKey();
        if(key != null) {
            //checks to see if the key is an alphabet letter
            if(key.length() == 1 && checkRange(key.charAt(0))) {
                typing += toLowerCase();
            }
        }
    }

    private boolean checkRange(char c) {
        return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
    }
}
```

The `charAt(i)` method from the String class returns the char in the given **index** from the string.

If we test our game now, we can see that we're only considering alphabet letters when the user is typing.

<h2 align="center">Delete key</h2>

In order for the user to have the possibility of restarting to write, we will implement the "space" key as a
delete key. By using the code we already have, let's just reset the `typing` string.

```java
public class JapaneseTyping extends World {

    //...

    public void act() {
        showText(typing, WIDTH/2, HEIGHT - 2);

        String key = Greenfoot.getKey();
        if(key != null) {
            //checks to see if the key is an alphabet letter
            if(key == "space") {
                typing = "";
            }
            else if(key.length() == 1 && checkRange(key.charAt(0))) {
                typing += key.toLowerCase();
            }
        }
    }
}
```

<h2 align="center">The words are falling</h2>

To make the words start to go down let's return to the `Word` class. Now, in this part, we need a way to make the words fall without delaying the execution of our game, because while it happens we still need to check for the user input every frame. That's why we are going to use a programmatically way of delaying the words.

We will create a **Draw Interval** and increment it every frame. Than, we will set a **Delay** for the word to be draw in the world whenever the draw interval surpasses the delay previously established. The `delay` variable will be from the `Word` class so that we can play around with it later on.

```java
public class Word extends Actor {
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
        drawInterval++;
        while(drawInterval > this.delay) {

            drawInterval = drawInterval - this.delay;
        }
    }
}
```

We are using a `while` statement to perform the execution because in the eventual case that
we need to do a lot of work under only 1 frame, the program won't go to the next frame until the
execution of the while loop is finished.

Now we just need to make the word go down by increasing it's **y** position by 1.

```java
public void act() {
    drawInterval++;
    while(drawInterval > this.delay){
        setLocation(getX(), getY()+1);
        drawInterval -= this.delay;
    }
}    
```

Now if we execute our game, **the words are falling**!

<h2 align="center">Finish line</h2>

For the finish line part, we already have a variable declared under the `JapaneseTyping` world class for it. Let's use one the Greenfoot World class methods to draw the finish line in a `GreenfootImage` and set it as the background after the world has been created.

```java
public void started() {
    GreenfootImage bg = new GreenfootImage(WIDTH*CELLSIZE, HEIGHT*CELLSIZE);
    bg.drawLine(0, ENDLINE*CELLSIZE, WIDTH*CELLSIZE, ENDLINE*CELLSIZE);
    setBackground(bg);
}
```

In the `act` method of the `Word` class, let's verify if our **y** position is equal to the position of the finish line. If positive, we have reached the endline and then the game is over. To finish the game, we get an instance of it by calling `getWorldOfType(World.class)` and call the `endGame` method.

```java
public class Word extends Actor {
    private String front, back;
    private int drawInterval;
    private int delay;

    public Word(String front, String back) {
        this.front = front;
        this.back = back;
        this.drawInterval = 0;
        this.delay = 0;

        GreenfootImage bg = new GreenfootImage(front, 32, Color.BLACK, null);
        setImage(bg);
    }

    public void started() {
        GreenfootImage bg = new GreenfootImage(WIDTH*CELLSIZE, HEIGHT*CELLSIZE);
        bg.drawLine(0, ENDLINE*CELLSIZE, WIDTH*CELLSIZE, ENDLINE*CELLSIZE);
        setBackground(bg);
    }

    public void act() {
        drawInterval++;
        while(drawInterval > this.delay) {
            setLocation(getX(), getY()+1);
            if(getY() == JapaneseTyping.ENDLINE) {
                JapaneseTyping w = getWorldOfType(JapaneseTyping.class);
                w.endGame();
            }
            drawInterval = drawInterval - this.delay;
        }
    }
}
```

For the `endGame` method in the `JapaneseTyping` world class, we just print the "Game Over" message and stop the execution of the game by calling Greenfoot's `stop` method.

```java
public void endGame() {
    showText("Game Over", WIDTH/2, HEIGHT/2);
    Greenfoot.stop();
}
```

<h2 align="center">Typed the right word</h2>

The next thing we need to work on now is if the user has typed the right word that's appearing on the screen. In that case, we need to remove that word from screen and put the following one from the `list_of_words` array. For that we will use the `wordIndex` variable that we created in the beginning of this tutorial.

Whenever we see that a user has typed a new key, we need to check whether the current word being typed is the same as the **back part** of the word on screen. Let's create a new method for that in the `JapaneseTyping` world class called `checkCorrectAnswer`.

```java
public boolean checkCorrectAnswer(String typing) {
    String wordOnScreen = list_of_words.get(wordIndex).getBack();
    return typing.equals(wordOnScreen);
}
```

This method receives a String and check whether is equal or not to the **back part** of the word that's currently being shown on screen. We also need to create the `getBack` method in the `Word` class.

```java
public String getBack() {
    return this.back;
}
```

Than, if the `checkCorrectAnswer` method returns **true**, we need to change the current word.

To do that, let's work on the `act` method since this will need to be done every frame of execution.

```java
public class JapaneseTyping extends World {

    //...

    public void act() {
        showText(typing, WIDTH/2, HEIGHT - 2);

        String key = Greenfoot.getKey();
        if(key != null) {
            //checks to see if the key is an alphabet letter
            if(key == "space") {
                typing = "";
            }
            else if(checkRange(key.charAt(0))) {
                typing += key.toLowerCase();
            }

            if(checkCorrectAnswer(typing)) {
                removeObject(list_of_words.get(wordIndex));
                wordIndex++;
                addWordToScreen(wordIndex);
                typing = "";
            }
        }
    }
}
```

Now our word is being replaced. If the user has typed the correct word, we remove the current word from the world,
increase the index from the array, add a new word with the new index and reset the current typing word.

<h2 align="center">Winning</h2>

To finish with the **Version 1** of our game, the last thing we need to do is to check wether the user has won
the game. This happens when the user has typed all words from the `list_of_words` correctly.

Since we know the size of the array and the wordIndex to control which word is to be displayed each time, the user has won the game whenever the `wordIndex` variable equals the size of the `list_of_words` array. Let's create a new method in the world class to verify that.

```java
public void checkWin() {
    if(wordIndex == list_of_words.size()) {
        showText("You won", WIDTH/2, HEIGHT/2);
        Greenfoot.stop();
    }
}
```

Now, every time we increment the `wordIndex` variable, let's check if the value has reached the size of the array and if yes, the user won the game.

The following is a complete implementation of the `JapaneseTyping` world class:

```java
public class JapaneseTyping extends World {

    public static int WIDTH = 14;
    public static int HEIGHT = 16;
    public static int CELLSIZE = 30;
    public static int ENDLINE = HEIGHT - 3;

    private ArrayList<Word> list_of_words;
    private String typing;

    private int wordIndex;

    public JapaneseTyping() {    
        super(WIDTH, HEIGHT, CELLSIZE);
        Greenfoot.setSpeed(50);

        list_of_words = ReadFile.getListOfWords("nihongo.txt");
        typing = "";
        wordIndex = 0;

        addWordToScreen(wordIndex);
    }

    public void started() {
        GreenfootImage bg = new GreenfootImage(WIDTH*CELLSIZE, HEIGHT*CELLSIZE);
        bg.drawLine(0, ENDLINE*CELLSIZE, WIDTH*CELLSIZE, ENDLINE*CELLSIZE);
        setBackground(bg);
    }

    public void act() {
        showText(typing, WIDTH/2, HEIGHT-2);

        String key = Greenfoot.getKey();
        if(key != null) {
            if(key == "space") {
                typing = "";
            }
            else if(checkRange(key.charAt(0))){
                typing += key.toLowerCase();
            }

            if(checkCorrectAnswer(typing)) {
                removeObject(list_of_words.get(wordIndex));
                wordIndex++;
                checkWin();
                addWordToScreen(wordIndex);
                typing = "";
            }
        }
    }

    public void checkWin() {
        if(wordIndex == list_of_words.size()) {
            showText("You won", WIDTH/2, HEIGHT/2);
            Greenfoot.stop();
        }
    }

    public void endGame() {
        showText("Game Over", WIDTH/2, HEIGHT/2);
        Greenfoot.stop();
    }

    public boolean checkCorrectAnswer(String typing) {
        return typing.equals(list_of_words.get(wordIndex).getBack());
    }

    public void addWordToScreen(int index) {
        addObject(list_of_words.get(index), WIDTH/2, 1);
    }

    private boolean checkRange(char c) {
        return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
    }
}
```

<h2 align="center">Conclusion</h2>

And that is it for the first version of our game. In this tutorial you have gone through how to develop a Japanese Typing game from scratch by using the [Greenfoot Environment](http://www.greenfoot.org/home)

From now on I have some suggestions about what you can try to do to improve the game experience, some of these improvements will be covered in the next versions of the game. For example:
- Add a score counter and a score for each word.
    - *<small>You will need to implement a score parameter for each Word and increment it when the user is right</small>*
- Make the game faster as the score increase.
    - *<small>Try to get a simple equation that relates the score of the game with the `delay` variable of the Word class</small>*
- Make the words appear in a random order from the txt file.
    - *<small>Change the way we use the `wordIndex` variable so that it's aways different, considering the current size of the `list_of_words` array</small>*

In the following versions of our game, the tutorial will not be as detailed as this one but instead will give you
new ideas of implementation and how you can upgrade our simple game to be even more playable for the user.

Thanks for reading.
