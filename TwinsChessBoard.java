import java.awt.*;
import java.awt.event.*;
//import java.awt.image.*;  // again i test to help
import java.io.*; // needed
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
//import java.net.URL;
import javax.imageio.ImageIO;
//import javax.imageio.*; /// I test maybe help?
/**
 * Chessboard made from JPanel with JButtons as tiles.... no move validator yet.
 * Just move like you can a real board - as you like, I think....
 * Based on code from 218 LOC on stackoverflow... adapted and labeld for my learning
 * @author Paul Clatworthy
 * @version Draft 0.1 Oct 2017
 */

public class TwinsChessBoard {

    private final JPanel mainwindow = new JPanel(new BorderLayout(3, 3)); // Initialise a JPanel called mainwindow, 3px boarders 
    private JPanel chessBoard;  // initialised below with GridLayout(0, 9)
    private JButton[][] chessBoardSquares = new JButton[8][8];  // array of JButton, number in [across][down]
    private BufferedImage imageWholeSet = null; // create a space to read in the image sheet into
    private Image[][] chessPieceImages = new Image[2][6]; // create an array of images to cut the image sheet into, 6 pieces per side!
    private final JLabel topmessage = new JLabel("This board doesn't do much yet!"); // topmessage displays on the tool bar
    private static final String ColLab = "abcdefgh";
    private static final String RowLab = "87654321";
    public static final int BLACK = 0, WHITE = 1;
    public static final int QUEEN = 0, KING = 1, CASTLE = 2, HORSE = 3, BISHOP = 4, PAWN = 5;
    public static final int[] STARTING_ROW = {CASTLE, HORSE, BISHOP, KING, QUEEN, BISHOP, HORSE, CASTLE};
    
    TwinsChessBoard() // Constructor called at start, kicks of creating the window and tool bar
    {
        initializeGui();
    }

    // called by TwinsChessboard, the constructor for this whole class / object.
    public final void initializeGui() 
    {
        // Create the Chess Piece Images
        createPieceImages();  // reads image file and creates a 2x3 array for the 6 pieces, called 
        
        // set up the main GUI
        mainwindow.setBorder(new EmptyBorder(5, 10, 5, 10));   // mainwindow is a JPanel, EmptyBorders of 5 pixels top/l/b/r
        JToolBar gamebuttons = new JToolBar();     // Tool bar called gamebuttons to put buttons on. Default horizontal
        gamebuttons.setFloatable(false);           // Fixed toolbar, not able tear off and have floating
        mainwindow.add(gamebuttons, BorderLayout.PAGE_START);  // puts the toobar on the mainwindow at page start = top?
        Action setUpGameAction = new AbstractAction("Load")   // adds a JButton, auto put to left, WRITE method for Load later.. currently setsUpGame
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setUpNewGame();
            }
        
        };
        gamebuttons.add(new JButton("Save"));   // WRITE method to save game later
        gamebuttons.add(new JButton("Reset"));  // WRITE method to Reset/initialise board later
        gamebuttons.add(new JButton("Resign")); // WRITE method to resign later
        gamebuttons.addSeparator();
        gamebuttons.add(topmessage);

        //mainwindow.add(new JLabel("?"), BorderLayout.LINE_START);  //Temp off as don't need (maybe mess with columns?)

        chessBoard = new JPanel(new GridLayout(0, 10));      // adds JPanel 0 row, 9 col. 0 means row will be added as filled.
        chessBoard.setBorder(new LineBorder(Color.BLACK));   // Sets board board colour black
        mainwindow.add(chessBoard);                          // adds the board (and surrounding labels) to the main window.

        // CREATE 8x8 BOARD OF SQUAREs made of b/w tiles that are buttons
        Insets buttonMargin = new Insets(0,0,0,0);   // variable buttonMargin is an Inset and is set to 0 for all margins t/l/b/r
        for (int colnum = 0; colnum < chessBoardSquares.length; colnum++)  // repeat next for number of rows? switched for ...
        {
            for (int rownum = 0; rownum < chessBoardSquares[colnum].length; rownum++) {
                JButton chesstile = new JButton();  // this is a button that will be the tile
                chesstile.setMargin(buttonMargin);  // uses Insets called buttonMargin set to 0,0,0,0 = no margin
                
                ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)); // Transparent 64x64px ImageIcon called icon
                chesstile.setIcon(icon);   // Sets the tile icon as a transparent 44x44px button
                
                if ((rownum % 2 == 1 && colnum % 2 == 1) || (rownum % 2 == 0 && colnum % 2 == 0))  // selects every other grid location, alternating on rows.
                {
                    chesstile.setBackground(Color.WHITE);
                } 
                else 
                {
                    chesstile.setBackground(Color.BLACK);
                }
                
                chessBoardSquares[rownum][colnum] = chesstile;    // Sets each board tile to the created JButton, black or white
            }
        }

        
        //-------------------------------------------------------------------------------
        //FILL ALL SQAURES THAT MAKE UP BOARD AREA; blanks, labels and the tiles (starts in top left, row by row with [add.JLabel new] filling each box one at a time)
        
        // Put letters along the top
        chessBoard.add(new JLabel("")); // Blank label - TOP LEFT empty box. Each .add fills the next box
        for (int col = 0; col < 8; col++) 
        {
            chessBoard.add(new JLabel(ColLab.substring(col, col + 1), SwingConstants.CENTER)); // cuts out characters from string 0,1 is first etc.
        }
        chessBoard.add(new JLabel("")); // Blank label - TOP RIGHT empty box
        
        // Fill the numbers on each side and all the white/black tiles
        for (int col = 0; col < 8; col++)
        {
            for (int row = 0; row < 8; row++) 
            {
                switch (row) 
                {
                    case 0:         // very left box is label of row
                        chessBoard.add(new JLabel(RowLab.substring(col, col + 1),SwingConstants.CENTER));
                    default:        // across the 1-8 grid put the appropriate tile, given by grid [r][c]
                        chessBoard.add(chessBoardSquares[row][col]);
                  }
            }
            chessBoard.add(new JLabel(RowLab.substring(col, col + 1),SwingConstants.CENTER)); // Blank label - FAR RIGHT of each row an empty box
        }
        
        chessBoard.add(new JLabel("")); // Blank label - BOTTOM LEFT empty box
        // Put letters along the bottom
        for (int ii = 0; ii < 8; ii++) 
        {
            chessBoard.add(new JLabel(ColLab.substring(ii, ii + 1), SwingConstants.CENTER)); // cuts out characters from string 0,1 is first etc.
        }
        //--------------------------------------------------------------
    }
        
    
    public final JComponent getTwinsChessBoard()
    {
        return chessBoard;
    }

    
    public final JComponent getGui() // method getGui which returns JPanel called mainwindow 
    {
        return mainwindow;
    }

    
    // load image file of chess pieces - this method is called by 'initializeGui'
    private final void createPieceImages()
    {

        try
        {
            //imageWholeSet = ImageIO.read(new File("ChessPieces.png"));  // since the file is in the folder 
                                                         // its in the jar so not sure why it can't see it?!
            
              imageWholeSet = ImageIO.read(new File("/home/paulclatworthy/Documents/BluJay_Projects/ChessBoard/ChessPieces.png"));
            for (int row = 0; row <2; row++)
            {
                for (int col = 0; col <6; col++)
                {
                    chessPieceImages[row][col] = imageWholeSet.getSubimage(col * 64, row * 64, 64, 64);
                }
            }
        }
        catch (IOException ioerror)
        {
            ioerror.printStackTrace();
            System.exit(1);
        }
    }
    
    // Sets up the board pieces in starting position
    private final void setUpNewGame()
    {
        topmessage.setText("Whites Start!");  // message apon starting a new game
        
        // Sets out black pieces
        for (int row = 0; row < 8; row++)
        {
            chessBoardSquares[row][0].setIcon(new ImageIcon(chessPieceImages[BLACK][STARTING_ROW[row]]));
        }
        for (int row = 0; row < 8; row++)
        {
            chessBoardSquares[row][1].setIcon(new ImageIcon(chessPieceImages[BLACK][PAWN]));
        }
       
    }
    
    
    public static void main(String[] args)
    {
        Runnable r = new Runnable()   // Creates Runnable class called r, can only have 1 method called run.
        {
            @Override
            public void run()
            {
                TwinsChessBoard gameboard = new TwinsChessBoard(); // Creates an object called game of TwinsChessBoard

                JFrame twinChessFrame = new JFrame("TwinChess");  // Opens JFrame titled TwinChess
                twinChessFrame.add(gameboard.getGui());           // calls method getGui which returns JPanel called mainwindow of gameboard.
                                                                  // where the whole chessbaord and tool bar etc are created
                twinChessFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // ?? think means kill on 'X' click
                twinChessFrame.setLocationByPlatform(true);                         // ??

                twinChessFrame.pack(); // makes frame minimum size for size of objects created in it to fit
                twinChessFrame.setMinimumSize(twinChessFrame.getSize()); // sets min size allowed the starting packed down size
                twinChessFrame.setVisible(true);                         // makes visible
            }
        };
     SwingUtilities.invokeLater(r);
    }
}