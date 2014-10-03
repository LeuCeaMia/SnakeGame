
import com.golden.gamedev.*;
import java.awt.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author student
 */
public class SnakeGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameWithMenu gf = new GameWithMenu();
        GameLoader game = new GameLoader();
        
        game.setup(gf, new Dimension(640,640), false);
        game.start();
        // TODO code application logic here
    }
}
