package org.academiadecodigo;

import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.graphics.Text;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;
import org.academiadecodigo.variachis.fila2.whowanttobeacodecadet.Game;
import org.academiadecodigo.variachis.fila2.whowanttobeacodecadet.Player;
import org.academiadecodigo.variachis.fila2.whowanttobeacodecadet.trivialpursuit.board.GfxDice;
import org.academiadecodigo.variachis.fila2.whowanttobeacodecadet.trivialpursuit.grid.QuestionsGfx;
import org.academiadecodigo.variachis.fila2.whowanttobeacodecadet.trivialpursuit.grid.SimpleGfxGrid;
import org.academiadecodigo.variachis.fila2.whowanttobeacodecadet.trivialpursuit.screens.EndScreen;
import org.academiadecodigo.variachis.fila2.whowanttobeacodecadet.trivialpursuit.screens.StartingScreen;

public class GameKeyboard implements KeyboardHandler {


    private Game game;
    private Player player;
    private Rectangle cursor;
    private GfxDice gfxDice;
    private boolean firstTurn;
    private StartingScreen startingScreen;
    private QuestionsGfx questionsGfx;
    private Text textFinal;
    private Rectangle win;
    private Rectangle turnFrame;
    private Text turnText;
    private String message;
    private boolean answered = true;
    private boolean rolledDice;
    private boolean timeToAnswer;
    private boolean firstmove = true;


    public GameKeyboard() {
        this.game = new Game();
        this.gfxDice = new GfxDice();
        game.nextPlayer();
        this.player = game.getCurrentPlayer();
        this.firstTurn = true;
        this.startingScreen = new StartingScreen(10, 10, "resources/img/startscreen.png");
        this.questionsGfx = game.getQuestionsGfx();
        this.win = new Rectangle(76, 363, 725, 200);
        win.setColor(Color.ORANGE);
        this.textFinal = new Text(106, 370, game.getCurrentPlayer().getName() + ": YOU WIN!");
        textFinal.setColor(Color.BLUE);
        textFinal.grow(19, 15);


    }

    public void printMessage(String message) {
        this.turnFrame = new Rectangle(145, 50, 600, 60);
        turnFrame.setColor(Color.WHITE);
        this.message = message;
        this.turnText = new Text(240, 80, message);
        turnText.grow(10, 10);

        turnFrame.fill();
        turnText.draw();
    }

    public void deleteMessage() {
        turnFrame.delete();
        turnText.delete();
    }

    public void keyboardInit() {
        Keyboard keyboard = new Keyboard(this);

        KeyboardEvent up = new KeyboardEvent();
        up.setKey(KeyboardEvent.KEY_UP);
        up.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(up);

        KeyboardEvent down = new KeyboardEvent();
        down.setKey(KeyboardEvent.KEY_DOWN);
        down.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(down);

        KeyboardEvent space = new KeyboardEvent();
        space.setKey(KeyboardEvent.KEY_SPACE);
        space.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(space);

        KeyboardEvent enter = new KeyboardEvent();
        enter.setKey(KeyboardEvent.KEY_S);
        enter.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(enter);

        KeyboardEvent one = new KeyboardEvent();
        one.setKey(KeyboardEvent.KEY_1);
        one.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(one);

        KeyboardEvent two = new KeyboardEvent();
        two.setKey(KeyboardEvent.KEY_2);
        two.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(two);

        KeyboardEvent three = new KeyboardEvent();
        three.setKey(KeyboardEvent.KEY_3);
        three.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(three);
    }


    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {

        switch (keyboardEvent.getKey()) {
            case KeyboardEvent.KEY_UP:
                game.movePlayerToNext(game.getCurrentPlayer());
                firstmove = false;
                break;

            case KeyboardEvent.KEY_DOWN:
                if (firstmove) {
                    return;
                }
                game.movePlayerToPrevious(game.getCurrentPlayer());
                break;

            case KeyboardEvent.KEY_SPACE:

                if (!game.isStarted()) {
                    startingScreen.getPictureStartingScreen().delete();
                    new SimpleGfxGrid(87, 70);
                    game.setStarted(true);
                    Player player1 = game.getPlayers()[0];
                    Player player2 = game.getPlayers()[1];
                    player1.getCheese().printCheese();
                    player2.getCheese().printCheese();
                    return;
                }

                if (!firstTurn) {
                    deleteMessage();
                }


                try {

                    if (!rolledDice) {
                        gfxDice.printDice(game.rollDice());
                        firstTurn = false;
                        rolledDice = true;
                        firstmove = true;
                        game.getCurrentPlayer().setAnswered(false);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
            case KeyboardEvent.KEY_S:
                if (answered && rolledDice) {
                    game.choosePathGame(game.getCurrentPlayer());
                    game.getSimpleGfxBoard().getCursor().delete();
                    game.showQuestion();
                    game.getSimpleGfxBoard().deleteHighlights();
                    answered = false;
                    timeToAnswer = true;
                }
                break;

            case KeyboardEvent.KEY_1:
                if (timeToAnswer) {
                    answered = true;
                    rolledDice = false;
                    timeToAnswer = false;
                    game.getCurrentPlayer().setAnswer(game.getAnswersInPlay()[0]);
                    game.answer(game.getQuestion());
                    if (!game.getCurrentPlayer().isRightAnswer()) {
                        wrongAnswer();
                        return;
                    }
                    if (game.isGameWinner(game.getCurrentPlayer())) {

                        endGame();
                        return;
                    }

                    printMessage(game.getCurrentPlayer().getName() + " You're right! Very Better! You can play again. Press Space to Roll Dice!");
                    game.getCurrentPlayer().setAnswer(game.getAnswersInPlay()[0]);
                    game.answer(game.getQuestion());
                }
                break;

            case KeyboardEvent.KEY_2:
                if (timeToAnswer) {
                    answered = true;
                    rolledDice = false;
                    timeToAnswer = false;
                    game.getCurrentPlayer().setAnswer(game.getAnswersInPlay()[1]);
                    game.answer(game.getQuestion());
                    if (!game.getCurrentPlayer().isRightAnswer()) {
                        wrongAnswer();
                        return;
                    }
                    if (game.isGameWinner(game.getCurrentPlayer())) {
                        endGame();
                        return;
                    }

                    printMessage(game.getCurrentPlayer().getName() + " You're right! Very Better! You can play again. Press Space to Roll Dice!");
                    game.getCurrentPlayer().setAnswer(game.getAnswersInPlay()[1]);
                    game.answer(game.getQuestion());
                }
                break;

            case KeyboardEvent.KEY_3:
                if (timeToAnswer) {
                    answered = true;
                    rolledDice = false;
                    timeToAnswer = false;
                    game.getCurrentPlayer().setAnswer(game.getAnswersInPlay()[2]);
                    game.answer(game.getQuestion());
                    if (!game.getCurrentPlayer().isRightAnswer()) {
                        wrongAnswer();
                        return;
                    }

                    if (game.isGameWinner(game.getCurrentPlayer())) {
                        endGame();
                        return;
                    }

                }
                break;
        }
    }


    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }


    public void sleep(int time) {
        try {
            Thread.sleep(time);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void wrongAnswer() {
        printMessage(game.getCurrentPlayer().getName() + " Sifufu! You're Wrong!");
        deleteMessage();
        game.nextPlayer();
        player = game.getCurrentPlayer();
        printMessage(" Sifufu! You're Wrong! " + game.getCurrentPlayer().getName() + " It's your turn. Press space to Roll dice!");
    }

    public void endGame() {
        win.fill();
        textFinal.draw();
        if (game.getCurrentPlayer().getName().equals("Player 1")) {
            EndScreen end = new EndScreen(10, 10, "resources/img/endscreenP1.png");
            return;
        }
        EndScreen end = new EndScreen(10, 10, "resources/img/endscreenP2.png");
    }

}
