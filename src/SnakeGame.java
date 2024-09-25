import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
  
public class SnakeGame extends JPanel implements ActionListener, KeyListener{

    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //gamelogic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    // int Highscore = 0;
    boolean gameOver = false;

    int boardWidth;
    int boardHeight;
    int tilesize = 25;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //grid
        // for(int i = 0; i < boardWidth/tilesize; i++){
        //     //x1,y1,x2,y2
        //     g.drawLine(i*tilesize, 0, i*tilesize, boardHeight);
        //     g.drawLine(0, i*tilesize, boardWidth, i*tilesize);
        // }

        //snakeHead
        g.setColor(Color.green);
        // g.fillRect(snakeHead.x * tilesize, snakeHead.y*tilesize, tilesize, tilesize);
        g.fill3DRect(snakeHead.x * tilesize, snakeHead.y*tilesize, tilesize, tilesize, true);

        //food
        g.setColor(Color.red);
        // g.fillRect(food.x * tilesize, food.y * tilesize, tilesize, tilesize);
        g.fill3DRect(food.x * tilesize, food.y * tilesize, tilesize, tilesize, true);

        //Snake body
        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.setColor(Color.green);
            // g.fillRect(snakePart.x * tilesize, snakePart.y * tilesize, tilesize, tilesize);
            g.fill3DRect(snakePart.x * tilesize, snakePart.y * tilesize, tilesize, tilesize, true);
        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over : " + String.valueOf(snakeBody.size()), tilesize - 16, tilesize);
            // if(Highscore < snakeBody.size()){
            //     Highscore = snakeBody.size();
            // }
        }
        else{
            g.setColor(Color.green);
            g.drawString("Score : " + String.valueOf(snakeBody.size()), tilesize - 16, tilesize);
        }

        // g.setColor(Color.white);
        // g.drawString("High Score : " + Highscore , 480, tilesize);
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tilesize);
        food.y = random.nextInt(boardHeight/tilesize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){
        //eat food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //Snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
               Tile prev = snakeBody.get(i-1);
               snakePart.x = prev.x;
               snakePart.y = prev.y; 
            }
        }

        //Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //collision with snake head
            if(collision(snakeHead, snakePart)){
                gameOver = true;  
            }
        }

        if (snakeHead.x * tilesize < 0 || snakeHead.x * tilesize > 600 ||
        snakeHead.y * tilesize < 0 || snakeHead.y * tilesize > 600) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY!= 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY!= -1){
            velocityX = 0;
            velocityY = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX!= 1){
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX!= -1){
            velocityX = 1;
            velocityY = 0;
        }
        // else{
        //     snakeHead = new Tile(5, 5);
        //     snakeBody = new ArrayList<Tile>();

        //     food = new Tile(10, 10);
        //     random = new Random();
        //     placeFood();

        //     velocityX = 0;
        //     velocityY = 0;

        //     gameLoop = new Timer(100, this);
        //     gameLoop.start();
            
        // }
    }

    //not in use
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}
