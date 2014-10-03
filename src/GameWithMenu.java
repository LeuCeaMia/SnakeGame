import com.golden.gamedev.*;
import com.golden.gamedev.object.*;
import java.awt.*;
import java.awt.event.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author student
 */
public class GameWithMenu extends Game{
    
    Block Snake;
    Block Food;
    Block[] snakePrev = new Block[100]; 
    GameFont font;
    
    Block[] walls = new Block[100];
    int nWalls;
    
    double snakeX, snakeY; // snake
    double foodX, foodY; // food
    double snakeSpawnX, snakeSpawnY;
    int highScore[] = new int[5];
    int snakeDirection;
    int snakeLength;
    final int dimension = 16; // 16 * 16
    //levels created - starts at level 0
    final int levels = 4;
    //For Difficulty
    int snakeFPS;
    int levelsUnlocked;
    int index, currentScore, currentLevel, snakeSpeed, tempSnakeLevel, tempSnakeSpeed;
    String gameLabels[];
    boolean gameFlags[];
    
    @Override
    public void initResources() {
        font = fontManager.getFont(getImages("smallfont.png", 8, 12),
                                   " !\"#$%&'()*+,-./0123456789:;<=>?" +
                                   "@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_" +
                                   "`abcdefghijklmnopqrstuvwxyz{|}~~");
        index = 0;
        currentLevel = 0;
        currentScore = 0;
        levelsUnlocked = 0;
        tempSnakeLevel = currentLevel;
        tempSnakeSpeed = snakeSpeed;
        gameLabels = new String[4];
        gameLabels[0] = "Start Game";
        gameLabels[1] = "Select Level";
        gameLabels[2] = "Select Speed";
        gameLabels[3] = "Exit";
        gameFlags = new boolean[5];
        gameFlags[0] = true; // MAIN MENU FLAG, IT IS INITIALLY TRUE
        gameFlags[1] = false; // START GAME FLAG, IT IS INITIALLY FALSE
        gameFlags[2] = false; // SELECT LEVEL FLAG, IT IS INITIALLY FALSE
        gameFlags[3] = false; // SELECT SPEED FLAG, IT IS INITIALLY FALSE
        gameFlags[4] = false; // GAME OVER FLAG, IT IS INITIALLY FALSE
        snakeX = 10;
        snakeY = 12;
        snakeSpawnX = 10;
        snakeSpawnY = 12;
        snakeDirection = 2;
        snakeLength = 0;
        snakeSpeed = 16;
        int i;
        for(i=0;i<5;i++)highScore[i] = 0;
        this.setFPS(snakeSpeed);
        foodX = 20;
        foodY = 20;
        
        Snake = new Block(getImage("head.png"), snakeX * dimension, snakeY * dimension);
        Food = new Block(getImage("food.png"), foodX * dimension, foodY * dimension);
        

        
        nWalls = 100;
        for(i=0;i<nWalls/2;i++)
        {
            walls[i] = new Block(getImage("wall.png"), 41 * dimension, i * dimension);
        }
        
        for(i=nWalls/2;i<nWalls;i++)
        {
            walls[i] = new Block(getImage("wall.png"), 41 * dimension, i * dimension);
        }
        //Snake Tail
        for(i=0;i<100;i++)
        {
            snakePrev[i] = new Block(getImage("head.png"), snakeX * dimension, snakeY * dimension);
        }
    }

    public void moveSnake()
    {
        switch(snakeDirection)
        {
            case 1: // NORTH
                    snakeY -= 1;
                    break;
            case 2: // EAST
                    snakeX += 1;
                    break;
            case 3: // SOUTH
                    snakeY += 1;
                    break;
            case 4: // WEST
                    snakeX -= 1;
                    break;
            default: break;
        }
        int i;
        if(snakeLength==1)
        {
            snakePrev[0].setY(Snake.getY());
            snakePrev[0].setX(Snake.getX());
        }else if(snakeLength>1)
        {   
           //Push Previous posistion further into the array
           for(i=snakeLength-1;i>=1;i--)
            {  
               snakePrev[i].setX(snakePrev[i-1].getX());
               snakePrev[i].setY(snakePrev[i-1].getY());
            }
            //Most Recent Snake Position
            snakePrev[0].setY(Snake.getY());
            snakePrev[0].setX(Snake.getX());
        }
        Snake.setX(snakeX * dimension);
        Snake.setY(snakeY * dimension);
    }
    
    public void resetFood()
    {
        int newX, newY;
        newX = ((int)(Math.random() * 100) % 40);
        newY = ((int)(Math.random() * 100) % 40);
        Food.setX((double)(newX * dimension));
        Food.setY((double)(newY * dimension));
    }
    
    public void resetSnake()
    {
        currentScore = snakeLength;
        gameFlags[1] = false;
        gameFlags[4] = true;
        int displayedLevel;
        snakeX = snakeSpawnX;
        snakeY = snakeSpawnY;
        snakeDirection = 2;
        //Update High Score
        if(snakeLength>highScore[currentLevel])highScore[currentLevel]=snakeLength;
        displayedLevel = currentLevel+1;
        System.out.println("Level "+ displayedLevel +" Highest Length: " + highScore[currentLevel]);
        if(highScore[currentLevel]>0&&levelsUnlocked==currentLevel&&levelsUnlocked<levels){
            levelsUnlocked++;
            displayedLevel = levelsUnlocked+1;
            System.out.println("Level " + displayedLevel + " Unlocked");
        } 
        snakeLength = 0;
            
        Snake.setX(snakeX * dimension);
        Snake.setY(snakeY * dimension);
    }
    
    
    public void readInput(){
        if(gameFlags[0]){
            // look for user input
            if (keyPressed(KeyEvent.VK_DOWN) && index < gameLabels.length - 1){
                index++;
            }
            else if (keyPressed(KeyEvent.VK_UP) && index > 0){
                index--;
            }
            else if (keyPressed(KeyEvent.VK_ENTER)){
                switch(index){
                    case 0: resetSnake(); gameFlags[0] = false; gameFlags[1] = true; break;
                    case 1: tempSnakeLevel = currentLevel; // stores the current currentLevel temporarily
                            gameFlags[0] = false; gameFlags[2] = true; break;
                    case 2: tempSnakeSpeed = snakeSpeed; // stores the current currentLevel temporarily
                            gameFlags[0] = false; gameFlags[3] = true; break;
                    case 3: finish(); break;
                }
            }
            else if (keyPressed(KeyEvent.VK_ESCAPE)){
                finish();
            }
        }
        else if (gameFlags[1]){
            // look for user input
            if (keyPressed(KeyEvent.VK_UP) && snakeDirection != 3){
                snakeDirection = 1;
            }
            if (keyPressed(KeyEvent.VK_RIGHT) && snakeDirection != 4){
                snakeDirection = 2;
            }
            if (keyPressed(KeyEvent.VK_DOWN) && snakeDirection != 1){
                snakeDirection = 3;
            }
            if (keyPressed(KeyEvent.VK_LEFT) && snakeDirection != 2){
                snakeDirection = 4;
            } 
            if (keyPressed(KeyEvent.VK_ESCAPE)){
                gameFlags[0] = true;
                gameFlags[1] = false;
            }
        }
        else if (gameFlags[2]){
            if (keyPressed(KeyEvent.VK_RIGHT) && tempSnakeLevel < levelsUnlocked){
                tempSnakeLevel++;
                System.out.println("Snake Level: " + (tempSnakeLevel + 1));
            }
            else if (keyPressed(KeyEvent.VK_LEFT) && tempSnakeLevel > 0){
                tempSnakeLevel--;
                System.out.println("Snake Level: " + (tempSnakeLevel + 1));
            }
            else if (keyPressed(KeyEvent.VK_ENTER)){
                currentLevel = tempSnakeLevel; // save changes
                switch(currentLevel){
                    case 0: createMap0();break;
                    case 1: createMap1();break;
                    case 2: createMap2();break;
                    case 3: createMap3();break;
                    case 4: createMap4();break;
                }
                gameFlags[0] = true;
                gameFlags[2] = false;
            }
            else if (keyPressed(KeyEvent.VK_ESCAPE)){
                gameFlags[0] = true;
                gameFlags[2] = false;
            }
        }
        else if (gameFlags[3]){
            if (keyPressed(KeyEvent.VK_RIGHT) && tempSnakeSpeed < 40){
                tempSnakeSpeed++;
                System.out.println("Snake Speed: " + (tempSnakeSpeed + 1));
            }
            else if (keyPressed(KeyEvent.VK_LEFT) && tempSnakeSpeed > 1){
                tempSnakeSpeed--;
                System.out.println("Snake Speed: " + (tempSnakeSpeed + 1));
            }
            else if (keyPressed(KeyEvent.VK_ENTER)){
                snakeSpeed = tempSnakeSpeed;
                this.setFPS(snakeSpeed);
                gameFlags[0] = true;
                gameFlags[3] = false;
            }
            else if (keyPressed(KeyEvent.VK_ESCAPE)){
                gameFlags[0] = true;
                gameFlags[3] = false;
            }
        }
        else if (gameFlags[4]){
            if (keyPressed(KeyEvent.VK_ENTER)){
                gameFlags[1] = true;
                gameFlags[4] = false;
            }
            else if (keyPressed(KeyEvent.VK_ESCAPE)){
                gameFlags[0] = true;
                gameFlags[4] = false;
            }
        }
    }
    private void createMap0(){
        nWalls=0;
        snakeSpawnX = 10;
        snakeSpawnY = 12;
    }
    private void createMap1(){
        //Map layout
        int i;
        snakeSpawnX = 10;
        snakeSpawnY = 12;
        nWalls = 40;
        for(i=0;i<nWalls/2;i++)
        {
            walls[i].setX(10*dimension); 
            walls[i].setY(i * dimension);
        }

        for(i=nWalls/2;i<nWalls;i++)
        {
            walls[i].setX(30*dimension); 
            walls[i].setY(i * dimension);
        }
        
    }
    private void createMap2(){
        //Map layout
        int i,c;
        //Map layout "| |" in the middle
        snakeSpawnX = 10;
        snakeSpawnY  = 5;
        nWalls = 40;
        c=10;
        for(i=0;i<nWalls/2;i++)
        {
            walls[i].setX(12*dimension); 
            walls[i].setY(c * dimension);
            c++;
        }
        c=10;
        for(i=nWalls/2;i<nWalls;i++)
        {
            walls[i].setX(28*dimension); 
            walls[i].setY(c * dimension);
            c++;
        }
    }
    private void createMap3(){
        //Map layout
        int i,c;
        //Previous level shifted 90 degrees
        snakeSpawnX = 20;
        snakeSpawnY  = 5;
        nWalls = 40;
        c=10;
        for(i=0;i<nWalls/2;i++)
        {
            walls[i].setY(12*dimension); 
            walls[i].setX(c * dimension);
            c++;
        }
        c=10;
        for(i=nWalls/2;i<nWalls;i++)
        {
            walls[i].setY(28*dimension); 
            walls[i].setX(c * dimension);
            c++;
        }
    }
    private void createMap4(){
        //Map layout
        int i,c;
        //Map layout "| |" in the middle
        snakeSpawnX = 10;
        snakeSpawnY  = 0;
        nWalls = 80;
        c=5;
        for(i=0;i<nWalls/2;i++)
        {
            walls[i].setX(12*dimension); 
            walls[i].setY(c * dimension);
            c++;
        }
        c=5;
        for(i=nWalls/2;i<nWalls;i++)
        {
            walls[i].setX(28*dimension); 
            walls[i].setY(c * dimension);
            c++;
        }
    }
    private void collisionCheck(){
        //Check for Out of Bounds
        if (Snake.getX() < 0 || Snake.getY() < 0 || Snake.getX() > getWidth() || Snake.getY() > getHeight())
            {
                resetSnake();
                resetFood();
            }
        //Check for Snake Food Collision
        if (Math.abs(Snake.getX() - Food.getX()) < dimension && Math.abs(Snake.getY() - Food.getY()) < dimension)
        {
            snakeLength++;
            resetFood();
        }
        //Check for Snake Tail Collision
        int i;
        for(i=0;i<snakeLength;i++)
        {   
            if (Math.abs(Snake.getX() - snakePrev[i].getX()) < dimension && Math.abs(Snake.getY() - snakePrev[i].getY()) < dimension)
            {
                resetSnake();
                resetFood();
            }    
        }
        //Check for Snake Wall Collision
        for(i=0;i<nWalls;i++)
        {   
            if (Math.abs(Snake.getX() - walls[i].getX()) < dimension && Math.abs(Snake.getY() - walls[i].getY()) < dimension)
            {
                resetSnake();
                resetFood();
            }    
        }
        //Make sure food doesn't get stuck inside walls
        for(i=0;i<nWalls;i++)
        {   
            if (Math.abs(Food.getX() - walls[i].getX()) < dimension && Math.abs(Food.getY() - walls[i].getY()) < dimension)
            {
                resetFood();
            }    
        }
    }
    @Override
    public void update(long l) {
        if(gameFlags[0]){
        }
        //Game Start
        else if(gameFlags[1]){
            collisionCheck();
            moveSnake();
            Snake.update(l);
            Food.update(l);
            //Update Snake Tail
            int i;
            if(snakeLength>0){
                for(i=0;i<snakeLength;i++){
                    snakePrev[i].update(1);
                }  
            }
        }
        else if(gameFlags[2]){
        }
        else if(gameFlags[3]){   
        }
        readInput();
    }

    @Override
    public void render(Graphics2D gd) {
        if(gameFlags[0]){
            // main menu
            gd.setColor(Color.black);
            gd.fillRect(0, 0, getWidth(), getHeight());
            font.drawString(gd, "The Snake Game", 250, 160);
            switch(index){
                case 0: font.drawString(gd, "-->", 200, 200); break;
                case 1: font.drawString(gd, "-->", 200, 220); break;
                case 2: font.drawString(gd, "-->", 200, 240); break;
                case 3: font.drawString(gd, "-->", 200, 260); break;
            }
            font.drawString(gd, gameLabels[0], 250, 200);
            font.drawString(gd, gameLabels[1], 250, 220);
            font.drawString(gd, gameLabels[2], 250, 240);
            font.drawString(gd, gameLabels[3], 250, 260);
        }
        else if(gameFlags[1]){
            int displayLevel = currentLevel+1;
            
            // game proper
            gd.setColor(Color.black);
            gd.fillRect(0, 0, getWidth(), getHeight());
            Snake.render(gd);
            Food.render(gd);
            font.drawString(gd, "Level: " + displayLevel + " Highscore: " + highScore[currentLevel], 400, 50);
            //Render Tail
            if(snakeLength>0){
                int i;
                for(i=0;i<snakeLength;i++){
                    snakePrev[i].render(gd);
                }
            }
            //Render Walls
            int i;
            for(i=0;i<nWalls;i++)
            {
                walls[i].render(gd);
            }
        }
        else if(gameFlags[2]){
            gd.setColor(Color.black);
            gd.fillRect(0, 0, getWidth(), getHeight());
            font.drawString(gd, "Current Snake Level: " + (tempSnakeLevel + 1), 250, 200);
            font.drawString(gd, "Press the left and right buttons to adjust", 200, 240);
            font.drawString(gd, "Enter key saves changes, Escape key discards changes", 160, 260);
        }
        else if(gameFlags[3]){
            gd.setColor(Color.black);
            gd.fillRect(0, 0, getWidth(), getHeight());
            font.drawString(gd, "Current Snake Speed: " + (tempSnakeSpeed + 1), 250, 200);
            font.drawString(gd, "Press the left and right buttons to adjust", 200, 240);
            font.drawString(gd, "Enter key saves changes, Escape key discards changes", 160, 260);
        }
        else if(gameFlags[4]){
            gd.setColor(Color.black);
            gd.fillRect(0, 0, getWidth(), getHeight());
            font.drawString(gd, "Game over. You lose", 250, 200);
            font.drawString(gd, "Your current score is " + currentScore, 220, 240);
            font.drawString(gd, "High score is " + highScore[currentLevel], 240, 260);
            font.drawString(gd, "Escape to go back to main menu, Enter plays again" , 140, 300);
        }
    }
    
}
