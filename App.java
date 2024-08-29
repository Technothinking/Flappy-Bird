import javax.swing.*;


public class App {
    public static void main(String[] args) throws Exception 
    {
        JFrame f = new JFrame("Flappy Bird");
        f.setSize(360,640);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        

        FlappyBird flappyBird = new FlappyBird();
        f.add(flappyBird);
        f.pack();
        flappyBird.requestFocus();
        f.setVisible(true);

    }
}
