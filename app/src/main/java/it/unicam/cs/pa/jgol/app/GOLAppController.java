package it.unicam.cs.pa.jgol.app;


import it.unicam.cs.pa.jgol.Controller;
import it.unicam.cs.pa.jgol.model.GridCoordinates;
import it.unicam.cs.pa.jgol.model.conway.ConwayState;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * JavaFX Controller of GOLApp.
 */
public class GOLAppController {
    /**
     * Application window width.
     */
    public static final int WIDTH = 600;
    /**
     * Application window height.
     */
    public static final int HEIGHT = 600;
    private final Controller<ConwayState, GridCoordinates> controller = Controller.getConwayController();
    /**
     * Locking object for evolution animation.
     */
    private Object animationLock;
    /**
     * Boolean flag for animating.
     */
    private boolean animating;
    /**
     * Thread used for evolution animation.
     */
    private Thread animationThread;
    @FXML
    private AnchorPane fieldArea;
    @FXML
    private Button openButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Button scrollLeftButton;
    @FXML
    private Button scrollRightButton;
    @FXML
    private Button scrollUpButton;
    @FXML
    private Button scrollDownButton;
    @FXML
    private Button stepForwardButton;
    @FXML
    private Button stepBackwardButton;
    private Rectangle[][] rectangles;
    private int row = 50;
    private int columns = 50;
    private int cellSize = 30;
    private int firstRow = 0;
    private int firstColumn = 0;

    /**
     * Method used to initialize this controller. This method is invoked by JavaFX.
     */
    public void initialize() {
        drawFieldArea();
        this.animationLock = new Object();
        this.animating = false;
        this.animationThread = new Thread(() -> {
            while (true) {
                if (this.animating) {
                    this.controller.stepForward();
                    this.refreshCells();
                }
                try {
                    Thread.sleep(125);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        this.animationThread.start();
    }

    /**
     * This method is used to draw the cells and their status. The number of rectangles depend on the level of zoom.
     * Moreover, rectangle in position (i,j) will be used the cell in position (i+firstRow, j+firstColumn).
     */
    private void drawFieldArea() {
        row = 1 + HEIGHT / cellSize;
        columns = 1 + WIDTH / cellSize;
        rectangles = new Rectangle[row][columns];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < columns; j++) {
                rectangles[i][j] = getCellAt(i, j);
            }
        }
        fieldArea.setMinSize(cellSize * columns, cellSize * row);
    }

    /**
     * Returns the rectangle at position (i,j) representing the cell at (i+firstRow, j+firstColumn).
     *
     * @param i rectangle row.
     * @param j cell column.
     * @return the rectangle representing the cell at position (i,j).
     */
    private Rectangle getCellAt(int i, int j) {
        Rectangle rect = new Rectangle(i * cellSize, j * cellSize, cellSize, cellSize);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);
        rect.setFill(getColorOf(controller.getStateOf(getCoordinates(i, j))));
        fieldArea.getChildren().add(rect);
        rect.setOnMouseClicked(clickOnCell(i, j));
        return rect;
    }


    /**
     * Returns the event used to handle the click event over the rectangle at position (i,j).
     *
     * @param i rectangle row.
     * @param j rectangle column.
     * @return the event used to handle the click event over the rectangle at position (i,j).
     */
    private EventHandler<MouseEvent> clickOnCell(int i, int j) {
        return e -> swapState(i, j);
    }


    /**
     * Swap the state of the cell represented by the rectangle at (i,j).
     *
     * @param i rectangle row.
     * @param j rectangle column.
     */
    private void swapState(int i, int j) {
        GridCoordinates loc = getCoordinates(i, j);
        ConwayState state = controller.getStateOf(loc).swap();
        controller.set(loc, state);
        rectangles[i][j].setFill(getColorOf(state));
    }

    /**
     * Returns the grid coordinates associated with the rectangle at (i,j).
     *
     * @param i rectangle row.
     * @param j rectangle column.
     * @return the grid coordinates associated with the rectangle at (i,j).
     */
    private GridCoordinates getCoordinates(int i, int j) {
        return new GridCoordinates(i + firstRow, j + firstColumn);
    }

    /**
     * Returns the color associated with the given state.
     *
     * @param state a Conway state.
     * @return the color associated with the given state.
     */
    private Paint getColorOf(ConwayState state) {
        return switch (state) {
            case LIVE -> Color.BLACK;
            case DEAD -> Color.WHITE;
        };
    }


    /**
     * This is the method used to handle save command.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onSaveCommand(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save GOL File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("GOL Files", "*.gol"));
        File selectedFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try {
                controller.save(selectedFile);
                refreshCells();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error...");
                alert.setHeaderText(e.getMessage());
            }
        }
    }

    /**
     * This is the method used to handle open command.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onOpenCommand(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open GOL File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("GOL Files", "*.gol"), new FileChooser.ExtensionFilter("Txt Files", "*.txt"), new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try {
                controller.open(selectedFile);
                refreshCells();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error...");
                alert.setHeaderText(e.getMessage());
            }
        }
    }

    /**
     * This is the method invoked when the observed cells are scrolled on the left.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onScrollLeftCommand(Event event) {
        scrollLeft();
    }

    /**
     * This is the method invoked when the observed cells are scrolled on the right.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onScrollRightCommand(Event event) {
        scrollRight();
    }

    /**
     * This is the method invoked when the observed cells are up scrolled.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onScrollUpCommand(Event event) {
        scrollUp();
    }

    /**
     * This is the method invoked when the observed cells are down scrolled.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onScrollDownCommand(Event event) {
        scrollDown();
    }

    /**
     * This is the method invoked when the cells are zoomed in.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onZoomInCommand(Event event) {
        zoomIn();
    }

    /**
     * This is the method invoked when the cells are zoomed out.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onZoomOutCommand(Event event) {
        zoomOut();
    }

    /**
     * This is the method invoked when the cells are cleared.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onClearCommand(Event event) {
        controller.clear();
        refreshCells();
    }


    /**
     * This is the method invoked to handle key events.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) {
            scrollLeft();
        }
        if (event.getCode() == KeyCode.RIGHT) {
            scrollRight();
        }
        if (event.getCode() == KeyCode.UP) {
            scrollUp();
        }
        if (event.getCode() == KeyCode.DOWN) {
            scrollDown();
        }
        if (event.getCode() == KeyCode.PLUS) {
            zoomIn();
        }
        if (event.getCode() == KeyCode.MINUS) {
            zoomOut();
        }
        refreshCells();
    }

    /**
     * This method is used to scroll down the observed cells.
     */
    private void scrollDown() {
        firstColumn++;
        refreshCells();
    }

    /**
     * This method is used to scroll up the observed cells.
     */
    private void scrollUp() {
        firstColumn--;
        refreshCells();
    }

    /**
     * This method is used to scroll on the right the observed cells.
     */
    private void scrollRight() {
        firstRow++;
        refreshCells();
    }

    /**
     * This method is used to scroll on the left the observed cells.
     */
    private void scrollLeft() {
        firstRow--;
        refreshCells();
    }

    /**
     * This method is used to zoom in the observed cells, namely to increase the number of cells depicted in the interface.
     */
    private void zoomIn() {
        synchronized (this.animationLock) {
            this.animating = !this.animating;
            if (cellSize < 50) {
                cellSize += 5;
                removeAllCells();
                drawFieldArea();
            }
            this.animating = !this.animating;
        }
    }

    /**
     * This method is used to zoom out the observed cells, namely to increase the cells size.
     */
    private void zoomOut() {
        synchronized (this.animationLock) {
            this.animating = !this.animating;
            if (cellSize > 15) {
                cellSize -= 5;
                removeAllCells();
                drawFieldArea();
            }
            this.animating = !this.animating;
        }
    }

    /**
     * Remove all the elements from the view.
     */
    private void removeAllCells() {
        for (Rectangle[] row : rectangles) {
            fieldArea.getChildren().removeAll(row);
        }
    }

    /**
     * Repaint all the cells.
     */
    private void refreshCells() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < columns; j++) {
                rectangles[i][j].setFill(getColorOf(controller.getStateOf(getCoordinates(i, j))));
            }
        }
    }

    /**
     * This method is invoked to perform one evolution step.
     *
     * @param event the triggering event.
     */
    @FXML
    public void onStepForwardCommand(Event event) {
        controller.stepForward();
        refreshCells();
    }

    /**
     * This method is invoked to step back at the previous evolution step.
     *
     * @param event the triggering event.
     */
    @FXML
    public void onStepBackwardCommand(Event event) {
        controller.stepBackward();
        refreshCells();
    }

    /**
     * This method is invoked to start and stop animating the evolution steps.
     *
     * @param event the triggering event.
     */
    @FXML
    public void onStepForwardHotCommand(Event event) {
        synchronized (this.animationLock) {
            this.animating = !this.animating;
            this.clearButton.setDisable(this.animating);
            this.saveButton.setDisable(this.animating);
            this.openButton.setDisable(this.animating);
            this.stepBackwardButton.setDisable(this.animating);
            this.stepForwardButton.setDisable(this.animating);
        }
    }

    /**
     * Application shutdown hook (within controller instead of Runtime shutdown hook).
     */
    public void shutdown() {
        this.animationThread.interrupt();
    }
}
