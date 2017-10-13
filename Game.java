import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * Andrew ID: msriha.
 * @author Mohamed Sriha
 */
public class Game {
    /**
     * Variable that holds and display the user score.
     */
    private int scoreText = 0;
    /**
     * 20 Seconds is the time maximum for one game.
     */
    private int count = 20;
    /**
     * Variable is in role of holding and displaying the user name.
     */
    private String userNameThread;
    /**
     * One second thread sleep.
     */
    private final int timeCounter = 1000;
    /**
     * 5 seconds thread sleep before the game starts again.
     */
    private final int timeStartOver = 5000;
    /**
     * Mole up for one second.
     */
    private final int moleUp = 1000;
    /**
     * Mole down for 2 seconds.
     */
    private final int moleDown = 2000;
    /**
     * Text appears when the Mole is Up and not hit.
     */
    private final String textMoleUp = ":-)";
    /**
     * Text appears when the Mole is hit.
     */
    private final String moleHit = ":-(";
    /**
     * @param userName
     * Game Class. (GUI).
     */
    public Game(String userName) {
        userNameThread = userName;
        JFrame window = new JFrame("Whack-a-Mole");
        window.setSize(750, 530);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        // Score Panel
        JPanel displayPanel = new JPanel();
        displayPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        displayPanel.setPreferredSize(new Dimension(740, 60));
        // Mole Panel
        JPanel molePanel = new JPanel();
        molePanel.setBackground(Color.white);
        molePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        molePanel.setPreferredSize(new Dimension(740, 410));
        // create and add components to the container dynamically
        JButton[] buttons = new JButton[91];
        for (int i = 0; i < buttons.length; i++) {
            // set every button to default state (neither walk nor stop)
            buttons[i] = new JButton("   ");
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setPreferredSize(new Dimension(50, 50));
            buttons[i].setOpaque(true);
            molePanel.add(buttons[i]);
        }
        // Timer
        JButton timer = new JButton("00:0");
        timer.setPreferredSize(new Dimension(100, 50));
        displayPanel.add(timer);
        // Score Display
        JButton score = new JButton(userName + " : " + scoreText);
        score.setPreferredSize(new Dimension(100, 50));
        displayPanel.add(score);
        // Start Button
        JButton startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(100, 50));
        displayPanel.add(startButton);
        Random ran = new Random();
        int randomMole = ran.nextInt(50);
        startButton.addActionListener(new ActionListener() {
            /**
             * @param ActionEvent
             * Synchronized actionListener method. As soon as the user click on the
             * button 'Start', the two threads will start, one for timer counting and the other
             * for Mole up and down configuration.
             */
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                // Thread Timer constructor
                Thread timerThreadStart = new HelperThreadTimer(startButton, timer);
                timerThreadStart.start();
                for (int i = 0; i < randomMole; i++) {
                // Thread Mole Up down constructor
                Thread moleThread = new HelperThreadMole(buttons, textMoleUp, score);
                moleThread.start();
                }
            }
        });
        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(100, 50));
        displayPanel.add(exitButton);
        // Exit the game
        exitButton.addActionListener(new ActionListener() {
            /**
             * Action listener that will exit the game as soon as
             * the user click on the button 'Exit'.
             * @param ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // Add components into JFrame
        mainPanel.add(molePanel);
        mainPanel.add(displayPanel);
        window.setContentPane(mainPanel);
        window.setVisible(true);
    }
    /**
     * Thread timer class.
     * This thread is in role of counting the time remaining before the game
     * is done.
     * Andrew ID: msriha.
     * @author Mohamed Sriha
     */
    public class HelperThreadTimer extends Thread {
        /**
         * JButton is in role of representing the time
         * thread button, which its main purpose is
         * to start the time thread. The button is enabled when
         * the thread starts.
         */
        private JButton startButtonThread;
        /**
         * JButton timerThread is in charge of displaying
         * the timer count from 20 seconds to 0 second.
         */
        private JButton timerThread;
        /**
         * timerThread.
         * @param myTimerThread
         * Thread starter.
         * @param myStartButtonThread
         * HelpThreadTimer constructor.
         */
        public HelperThreadTimer(JButton myStartButtonThread, JButton myTimerThread) {
            startButtonThread = myStartButtonThread;
            timerThread = myTimerThread;
        }
        /**
         * Run method. Sleep thread sleeps for one second and wakes up
         * to decrement the count variable until 0 before the thread ends.
         * While loop then exits and give place to the second sleep thread
         * for 5 second sleep and wakes up to ask the user if he/she wants
         * to play again.
         */
        @Override
        public void run() {
            startButtonThread.setText("Running...");
            try {
                while (count >= 0) {
                    startButtonThread.setEnabled(false);
                    timerThread.setText("00:" + count);
                    Thread.sleep(timeCounter);
                    count--;
                }
            } catch (InterruptedException f) {
                throw new AssertionError(f);
            }
            try {
                Thread.sleep(timeStartOver);
                // timerThread.setText("00:" + count);
                startButtonThread.setEnabled(true);
                startButtonThread.setText("Start Over");
                count = 20;
                scoreText = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * HelperThreadMole.
     * This thread is in role of managing the configuration
     * of Up and Down Mole configuration.
     * Andrew ID: msriha.
     * @author Mohamed Sriha
     */
    public class HelperThreadMole extends Thread {
       /**
        * JButton array to hold all buttons
        * present on the Mole panel.
        */
        private JButton[] buttonMole;
        /**
         * JButton to display the score of the user
         * when the user hits the right mole in the right time.
         */
        private JButton scoreThread;
        /**
         * Random integer variable to instantiate
         * a random button as an Up Mole.
         */
        private Random random = new Random();
        /**
         * upMole is the mole button when it is Up after
         * random instantiation.
         */
        private JButton upMole;
        /**
         * Text variable is the image or text that
         * informs the user that the Mole which is Up.
         */
        private String text;
        /**
         * Hold the mole button.
         * @param myButtonMole
         * Mole text when it is up.
         * @param myText
         * Text displaying the score.
         * @param myScoreThread
         * HelperThreadMole constructor.
         */
        public HelperThreadMole(JButton[] myButtonMole, String myText, JButton myScoreThread) {
            buttonMole = myButtonMole;
            text = myText;
            scoreThread = myScoreThread;
        }
        /**
         * Run Method. This method is charge of
         * Assigning randomly a Mole button for
         * one second Mole up and 2 seconds Mole down.
         */
        @Override
        public void run() {
            try {
                // None Mole will be assigned after the timer is 0.
                while (count > 0) {
                    upMole = new JButton();
                    int randomNumberOne = random.nextInt(buttonMole.length);
                    upMole = buttonMole[randomNumberOne];
                    Thread.sleep(moleDown);
                    synchronized (upMole) {
                        upMole.setText(text);
                        upMole.setBackground(Color.RED);
                    }
                    scoreThread.setText(userNameThread + " : " + scoreText);
                    upMole.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() == upMole) {
                                scoreText++;
                                upMole.setText(moleHit);
                                upMole.setBackground(Color.GREEN);
                                return;
                            }
                        }
                    });
                    Thread.sleep(moleUp);
                    upMole.setText("");
                    upMole.setBackground(Color.WHITE);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scoreThread.setText(userNameThread + " : " + scoreText);
        }
    }
    /**
     * @param args
     * Main Method.
     * JOptionPane is charge of asking for the
     * user's name to display it then
     * on the screen next to the score text.
     * if the user name is empty, 'Score' string will be
     * displayed instead. Cancel button will exit
     * the game.
     */
    public static void main(String[] args) {
        String s = JOptionPane.showInputDialog("Please enter your name.");
        if (s == null) {
            System.exit(0);
        }
        if (s.equals("")) {
            s = "Score";
        }
        new Game(s);
    }
}
