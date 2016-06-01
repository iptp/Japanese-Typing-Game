# Increasing Game Experience

<h2 align="center">Introduction</h2>

In the version 2 of our game, we will try to enhance the user experience when playing our game by improving it. Some of the things we will work on are as following:
- Implement the backspace key to delete letters
- Add a score counter
- Make the words fall more rapidly when the score goes up
- Make the words appear randomly
- Add more lives to give the player more chances in our game
- Possibility of choosing between Hiragana and/or Katakana

The code for version 2 is available in the [version2 branch](https://github.com/iptp/Japanese-Typing-Game/tree/version2)

<h2 align="center">Backspace key</h2>

We will add the functionality of clicking the backspace key so that the user can delete already typed letters. To do so, we need to check for the "backspace" key in the part of the code where we handle the user input, and erase one letter from the `typing` string. We also need to check wether the `typing` string has any character, otherwise we will get an error when trying to delete a letter.

```java
public void act() {
    showText(typing, WIDTH/2, HEIGHT-2);

    String key = Greenfoot.getKey();
    if(key != null) {
        if(key == "space") {
            typing = "";
        }
        else if(typing.length() > 0 && key == "backspace") {
            typing = typing.substring(0, typing.length()-1);
        }
        else if(key.length() == 1 && checkRange(key.charAt(0))){
            typing += key.toLowerCase();
        }

        //Check correct answer...
    }
}
```

With this we can now type wrong letters and erase them without the need to erase the whole word by using the 'space' key.

<h2 align="center">Score</h2>

The score in our game can be implemented by giving the `Word` class a score parameter that is equal to the length of the word. We also need a method to get the score.

```java
public class Word extends Actor {
    private String front, back;
    private int drawInterval;
    private int delay;
    private int score;

    public Word(String front, String back) {
        this.front = front;
        this.back = back;
        this.drawInterval = 0;
        this.delay = 70;
        this.score = this.front.length();

        GreenfootImage bg = new GreenfootImage(front, 32, Color.BLACK, null);
        setImage(bg);
    }

    public act() {
        //act code
    }

    public int getScore() {
        return this.score;
    }
}
```

After that, we can create a parameter for the World class to hold the score and display it on screen in the act method, as well as updating it after the user has typed a word correctly.

```java
public class JapaneseTyping extends World {
    //previous variable declarations

    private int score;

    public JapaneseTyping() {    
        //previous constructor code
        score = 0;
    }

    public void started() {
        //previous code
    }

    public void act() {
        showText("Score: " + score, WIDTH-3, 0);
        showText(typing, WIDTH/2, HEIGHT - 2);

        //previous key handling code

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
```

<h2 align="center">Speed</h2>

To make the words fall in different speeds we just need to play with the delay parameter of every word. We can, for example, make the delay relatively to the score of the game, by using the formula `delay = score + default_delay - (score*2)`

First, let's make the score parameter from the World class a public and static parameter so that we can access it from the word class.

```java
public class JapaneseTyping extends World {
    //previous variable declarations

    public static int score;
}
```

Then, in constructor of the word class:

```java
public class Word extends Actor {
    private String front, back;
    private int drawInterval;
    private int delay;
    private int score;

    public Word(String front, String back) {
        this.front = front;
        this.back = back;
        this.drawInterval = 0;

        int gameScore = JapaneseTyping.score;
        this.delay = gameScore + 70 - (gameScore*2);

        this.score = this.front.length();

        GreenfootImage bg = new GreenfootImage(front, 32, Color.BLACK, null);
        setImage(bg);
    }

    //previous methods
}
```

Now we have the speed of the words increasing with the game score.

<h2 align="center">Randomness</h2>

To make our words appear randomly from the txt file we need to change the way we interact with our `wordIndex` variable. As from now, this variable starts at 0 and is increased whenever the user type a word correctly. First, we will make a new method called `int getNewWordIndex` that will return a different word index every time it's called. Also, this method takes into consideration the current size of the `list_of_words` array. Let's create de method in the world class:

```java
public int getNewWordIndex() {
    return Greenfoot.getRandomNumber(list_of_words.size());
}
```

This returns a number between 0 and the size of the array - 1. Next, let's change the initial value of the `wordIndex` variable in the constructor of the world class:

```java
public JapaneseTyping() {    
    super(WIDTH, HEIGHT, CELLSIZE);
    Greenfoot.setSpeed(50);
    list_of_words = ReadFile.getListOfWords("nihongo.txt");
    wordIndex = getNewWordIndex();
    addWordToScreen(wordIndex);
    typing = "";
    score = 0;
}
```

And lastly, whenever the user type the correct word we need to remove it from the array and get a new index for the `wordIndex` variable. In the act method of the world class:

```java
public void act() {
    showText("Score: " + score, WIDTH-3, 0);
    showText(typing, WIDTH/2, HEIGHT - 2);

    String key = Greenfoot.getKey();
    if(key != null) {

        //key checking code

        if(checkCorrectAnswer(typing)) {
            score += list_of_words.get(wordIndex).getScore();
            removeObject(list_of_words.get(wordIndex));
            list_of_words.remove(wordIndex);
            wordIndex = getNewWordIndex();
            checkWin();
            addWordToScreen(wordIndex);
            typing = "";
        }
    }
}
```

With this modifications, the words now appear in a random order.

<h2 align="center">Lives</h2>

For the last part of our version 2, we will add more lives to give our players more chances while playing the game.

First, let's declare a static variable in our world class:

```java
public class JapaneseTyping extends World {

    public static int WIDTH = 14;
    public static int HEIGHT = 16;
    public static int CELLSIZE = 30;
    public static int ENDLINE = HEIGHT - 3;
    public static int lives = 3;

    //previous code
}
```

Now, since the endline part of the game is controlled by the word itself, let's add another condition in the `act` method of the `Word` class that will end the game only when the number of lives is 0.

```java
public void act() {
    this.drawInterval++;
    while(drawInterval > this.delay) {
        setLocation(getX(), getY()+1);
        if(getY() == JapaneseTyping.ENDLINE) {
            JapaneseTyping w = getWorldOfType(JapaneseTyping.class);
            w.lives--;
            if(w.lives == 0) {
                w.endGame();
            }
        }
        drawInterval -= this.delay;
    }
}    
```

Then, we need to remove the current word and put a new one in the screen. If we check the `act` method of our world class we can see that this code is done in the `act` method itself:

```java
public void act() {
    showText("Score: " + score, WIDTH-3, 0);
    showText(typing, WIDTH/2, HEIGHT - 2);

    String key = Greenfoot.getKey();
    if(key != null) {

        //key checking code

        if(checkCorrectAnswer(typing)) {
            score += list_of_words.get(wordIndex).getScore();
            removeObject(list_of_words.get(wordIndex));
            list_of_words.remove(wordIndex);
            wordIndex = getNewWordIndex();
            checkWin();
            addWordToScreen(wordIndex);
            typing = "";
        }
    }
}
```

Let's move the code into another method so that we can reuse it, leaving the score part behind since the score is only updated when the user has typed a correct word:

```java
public void act() {
    showText("Score: " + score, WIDTH-3, 0);
    showText(typing, WIDTH/2, HEIGHT - 2);

    String key = Greenfoot.getKey();
    if(key != null) {

        //key checking code

        if(checkCorrectAnswer(typing)) {
            score += list_of_words.get(wordIndex).getScore();
            changeWordOnScreen();
        }
    }
}

public void changeWordOnScreen() {
    removeObject(list_of_words.get(wordIndex));
    list_of_words.remove(wordIndex);
    wordIndex = getNewWordIndex();
    checkWin();
    addWordToScreen(wordIndex);
    typing = "";
}
```

Now we can reuse this method in the `act` method of the `Word` class to change the word on screen when the word hits the endline but there's still lives to spend:

```java
public void act() {
    this.drawInterval++;
    while(drawInterval > this.delay) {
        setLocation(getX(), getY()+1);
        if(getY() == JapaneseTyping.ENDLINE) {
            JapaneseTyping w = getWorldOfType(JapaneseTyping.class);
            w.lives--;
            if(w.lives == 0) {
                w.endGame();
            } else {
                w.changeWordOnScreen();
            }
        }
        drawInterval -= this.delay;
    }
}   
```

As a final tweak, we can show the lives remaining in the screen the same way we do with the score:

```java
public void act() {
    showText("Score: " + score, WIDTH-3, 0);
    showText("Lives: " + lives, 2, 0);
    showText(typing, WIDTH/2, HEIGHT - 2);

    String key = Greenfoot.getKey();

    //key handling code...

}
```

<h2 align="center">Conclusion</h2>

And this is it for the version 2 of our game. In this part we have gone through how to improve our previously defined [Japanese Typing](https://github.com/iptp/Japanese-Typing-Game/wiki) game. If you want to improve even more your game here are some suggestions:

- Changing font and color to give more life to it.
    - *<small>Take a look at the `Greenfoot` class for methods to change font and color</small>*
- Letting the user choose from Hiragana and/or Katakana for the words. (Kanji maybe?)
    - *<small>Create a new txt file to handle katanara or kanji</small>*
- Add a highscore variable
    - *<small>Create a new file for handling highscore values</small>*
- Create more screens, for instance, menus, game over, winner, etc...
    - *<small>You will need to create new `World` classes and change them by using the `Greenfoot.setWorld()` method<small>*
