package com.nat.maze.game;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.ComboBox;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class GameScreenManager {
    private static boolean shouldExit = false;
    private static boolean shouldPlay = false;
    static GameConfig cfg = new GameConfig();

    private GameScreenManager() {}

    private static Screen createMenuScreen() throws IOException {
        final Font font = new Font("Cascadia Mono", Font.PLAIN, 24);
        final SwingTerminalFontConfiguration cfg = SwingTerminalFontConfiguration.newInstance(font);
        final DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setTerminalEmulatorFontConfiguration(cfg);

        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        return new TerminalScreen(terminal);
    }

    private static Screen createGameScreen() throws IOException {
        final Font font = new Font("Cascadia Mono", Font.PLAIN, 6);
        final SwingTerminalFontConfiguration cfg = SwingTerminalFontConfiguration.newInstance(font);
        final DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setTerminalEmulatorFontConfiguration(cfg);

        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        return new TerminalScreen(terminal);
    }

    private static Screen createWinScreen() throws IOException {
        final Font font = new Font("Cascadia Mono", Font.PLAIN, 24);
        final SwingTerminalFontConfiguration cfg = SwingTerminalFontConfiguration.newInstance(font);
        final DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setTerminalEmulatorFontConfiguration(cfg);

        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        return new TerminalScreen(terminal);
    }

    public static void start() throws IOException {
        while (!shouldExit) {
            try (final Screen screen = GameScreenManager.createMenuScreen()) {
                WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
                screen.startScreen();

                final com.googlecode.lanterna.gui2.Window window = new BasicWindow("Menu");
                com.googlecode.lanterna.gui2.Panel contentPanel = new com.googlecode.lanterna.gui2.Panel(new com.googlecode.lanterna.gui2.GridLayout(2));
                com.googlecode.lanterna.gui2.GridLayout gridLayout = (com.googlecode.lanterna.gui2.GridLayout)contentPanel.getLayoutManager();
                gridLayout.setHorizontalSpacing(3);
                com.googlecode.lanterna.gui2.Label title = new com.googlecode.lanterna.gui2.Label("Map path:");
                contentPanel.addComponent(title);
                ComboBox<Path> comboBox = new ComboBox<>();
                try (Stream<Path> paths = Files.walk(Paths.get("./maps/"))) {
                    paths
                            .filter(Files::isRegularFile)
                            .filter((p) -> p.getFileName().toString().endsWith(".map"))
                            .forEach(comboBox::addItem);
                }
                contentPanel.addComponent(comboBox);

                com.googlecode.lanterna.gui2.Button button = new com.googlecode.lanterna.gui2.Button("Play", () -> {
                    shouldPlay = true;
                    cfg.setMapPath(comboBox.getText());
                    window.close();
                });
                contentPanel.addComponent(button);

                com.googlecode.lanterna.gui2.Label emptyLabel = new Label("");
                contentPanel.addComponent(emptyLabel);

                com.googlecode.lanterna.gui2.Label widthLabel = new Label("Width");
                contentPanel.addComponent(widthLabel);

                com.googlecode.lanterna.gui2.TextBox widthTextBox = new com.googlecode.lanterna.gui2.TextBox("10");
                widthTextBox.setValidationPattern(Pattern.compile("[0-9]*"));
                contentPanel.addComponent(widthTextBox);

                com.googlecode.lanterna.gui2.Label heightLabel = new Label("Height");
                contentPanel.addComponent(heightLabel);

                com.googlecode.lanterna.gui2.TextBox heightTextBox = new com.googlecode.lanterna.gui2.TextBox("10");
                heightTextBox.setValidationPattern(Pattern.compile("[0-9]*"));
                contentPanel.addComponent(heightTextBox);

                com.googlecode.lanterna.gui2.Button genButton = new com.googlecode.lanterna.gui2.Button("Random", () -> {
                    shouldPlay = true;
                    new MazeGenerator(Integer.parseInt(widthTextBox.getText()),Integer.parseInt(heightTextBox.getText()))
                            .writeToFile(".\\maps\\generated.map");
                    cfg.setMapPath(".\\maps\\generated.map");
                    window.close();
                });
                contentPanel.addComponent(genButton);

                com.googlecode.lanterna.gui2.Button exitButton = new Button("Quit", () -> {
                    shouldExit = true;
                    window.close();
                });
                contentPanel.addComponent(exitButton);

                window.setComponent(contentPanel);
                gui.addWindowAndWait(window);
            }

            if (!shouldPlay) break;

            try (final Screen screen = GameScreenManager.createGameScreen()) {
                screen.startScreen();
                Game game = new Game(cfg, screen);
                game.loop();

                if (game.isWon()) {
                    try (final Screen winScreen = GameScreenManager.createWinScreen()) {
                        WindowBasedTextGUI gui = new MultiWindowTextGUI(winScreen);
                        winScreen.startScreen();

                        final com.googlecode.lanterna.gui2.Window window = new BasicWindow("YOU WIN!!");
                        com.googlecode.lanterna.gui2.Panel contentPanel = new com.googlecode.lanterna.gui2.Panel(new com.googlecode.lanterna.gui2.GridLayout(2));
                        com.googlecode.lanterna.gui2.Label title = new com.googlecode.lanterna.gui2.Label(String.format("YOU BEAT THE MAZE IN %fs!", game.getClearTime()));
                        contentPanel.addComponent(title);
                        window.setComponent(contentPanel);
                        gui.addWindowAndWait(window);

                    }
                }
            }

            shouldPlay = false;
        }
    }
}
