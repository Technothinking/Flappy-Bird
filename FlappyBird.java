import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;




public class FlappyBird extends JPanel implements ActionListener, KeyListener
{
    Image birdImage;
    Image bgImage;
    Image bottomPipeImage;
    Image topPipeImage;

    //Bird
    int birdX = 360/8;
    int birdY = 640/2;
    int birdHeight = 24;
    int birdWidth = 34;
    boolean gameOver = false;

    class Bird
    {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img)
        {
            this.img = img;

        }

    }

    //Pipes
    int PipeX = 360;
    int PipeY = 0;
    int PipeWidth = 64;
    int PipeHeight = 512;

    class Pipe
    {
        int x = PipeX;
        int y = PipeY;
        int width = PipeWidth;
        int height = PipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img)
        {
            this.img = img;

        }


    }

    // Game Logic
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();


    Timer gameLoop;
    Timer placePipesTimer;
    double score = 0;

    public void placePipes()
    {
        int randomPipeY = (int) (PipeY - (PipeHeight/4) - (Math.random()*(PipeHeight/2)));
        int openingSpace = 640/4;
        Pipe topPipe = new Pipe(topPipeImage);

        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + PipeHeight + openingSpace;
        pipes.add(bottomPipe);

    }

    FlappyBird()
    {
        setPreferredSize(new Dimension(360,640));
        setBackground(Color.BLUE);

        setFocusable(true);
        addKeyListener(this);

        // Load images
        bgImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();

        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();


        //Game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        //Place Pipe timer
        placePipesTimer = new Timer(1500, new ActionListener()
         
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                placePipes();
            }

        }
        );
        placePipesTimer.start();

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        // set background
        g.drawImage( bgImage, 0, 0, 360, 640, null );

        // bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //pipes
        for( int i = 0; i < pipes.size(); i++)
        {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

         //score
         g.setColor(Color.white);

         g.setFont(new Font("Arial", Font.PLAIN, 32));
         if (gameOver) 
        {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else 
        {
            g.drawString(String.valueOf((int) score), 10, 35);
        }

    }

    public void move()
    {
        //Bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //pipes
        for( int i = 0; i < pipes.size(); i++)
        {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                pipe.passed = true;
            }

            if(collision(bird, pipe))
            {
                gameOver = true;
            }
        }

        if(bird.y > 640)
        {
            gameOver = true;
        }

    }

    public boolean collision(Bird a, Pipe b)
    {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        move();
        repaint();
        if(gameOver)
        {
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) 
    {
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            velocityY = -10;
        }

        if (gameOver) 
        {
            //restart game by resetting conditions
            bird.y = birdY;
            velocityY = 0;
            pipes.clear();
            gameOver = false;
            score = 0;
            gameLoop.start();
            placePipesTimer.start();
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    
    @Override
    public void keyReleased(KeyEvent e) {}


}
