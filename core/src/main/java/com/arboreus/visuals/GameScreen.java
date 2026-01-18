package com.arboreus.visuals;

import com.arboreus.logic.BinaryTree;
import com.arboreus.logic.TreeNode;
import com.arboreus.logic.GameStateManager;
import com.arboreus.logic.NumberGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;
import java.util.Map;

public class GameScreen extends ScreenAdapter implements InputProcessor {
    private Stage stage;
    private BinaryTree tree;
    private ShapeRenderer shapeRenderer;
    private Skin skin;
    private Texture circleTexture;
    private com.badlogic.gdx.audio.Sound clickSound;
    private com.badlogic.gdx.audio.Sound gameOverSound;
    private com.badlogic.gdx.audio.Sound victorySound;

    // Game Loop Fields
    private enum GameState {
        SPAWNING, MOVING, WAITING_FOR_DECISION, GAME_OVER, GAME_WON
    }

    private GameState currentState = GameState.SPAWNING;

    private NodeActor activeFallingNode;
    private NodeActor currentComparisonNode; // The node we are currently comparing against

    private Map<TreeNode, NodeActor> nodeActors = new HashMap<>();

    // HUD and Logic Fields
    private GameStateManager gameStateManager;
    private NumberGenerator numberGenerator;

    private Table hudTable;
    private Label scoreLabel;
    private Label livesLabel;

    // Layout Constants
    private static final float TOP_MARGIN = 150f; // More space for HUD and spawn

    public GameScreen() {
        stage = new Stage(new ScreenViewport());
        shapeRenderer = new ShapeRenderer();
        tree = new BinaryTree();

        // Initialize Logic Managers
        gameStateManager = new GameStateManager(3); // 3 Lives default
        numberGenerator = new NumberGenerator(1, 99);

        // Assets
        skin = new Skin();
        clickSound = Gdx.audio.newSound(Gdx.files.internal("click-a.ogg"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("game-over-417465.mp3"));
        victorySound = Gdx.audio.newSound(Gdx.files.internal("victory.mp3"));

        // 1. Generate Fonts (True FreeType - Exact Sizes)
        BitmapFont uiFont = AssetGenerationHelper.getFont(32); // Standard UI/Node font (Sharp)
        skin.add("default", uiFont); // Used by Nodes
        skin.add("ui", uiFont); // Used by Standard Button Gen

        BitmapFont titleFont = AssetGenerationHelper.getFont(72); // Large Title
        skin.add("title", titleFont);

        // 2. Label Styles
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = uiFont;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);
        skin.add("ui", labelStyle); // Add missing 'ui' style for Info Dialog

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.CYAN; // Cyber Cyan
        skin.add("title", titleStyle); // For "GAME OVER"

        Label.LabelStyle hudStyle = new Label.LabelStyle();
        hudStyle.font = uiFont;
        hudStyle.fontColor = Color.WHITE; // Pure White per cleanup request
        skin.add("hud", hudStyle);

        // Info Body Font & Style (Size 22)
        BitmapFont infoBodyFont = AssetGenerationHelper.getInfoBodyFont();
        skin.add("info-body", infoBodyFont);

        Label.LabelStyle infoBodyStyle = new Label.LabelStyle();
        infoBodyStyle.font = infoBodyFont;
        infoBodyStyle.fontColor = Color.WHITE;
        skin.add("info-body", infoBodyStyle);

        circleTexture = AssetGenerationHelper.generateCircleTexture(25);
        circleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // 3. Button Styles (Custom Image)
        AssetGenerationHelper.loadCustomButtonSkin(skin);
        AssetGenerationHelper.loadSquarePopupStyle(skin);
        // Alias "cyber" to "default" so Dialog uses it automatically
        skin.add("default", skin.get("cyber", TextButton.TextButtonStyle.class));

        // Setup HUD & Header
        hudTable = new Table();
        hudTable.top();
        hudTable.setFillParent(true);
        hudTable.pad(20);

        // 1. Stats (Left)
        Table statsTable = new Table();
        scoreLabel = new Label("SCORE: 0", skin, "hud");
        livesLabel = new Label("LIVES: 3", skin, "hud");
        statsTable.add(scoreLabel).align(Align.left).row();
        statsTable.add(livesLabel).align(Align.left).padTop(10);

        // 2. Header (Center)
        Label.LabelStyle headerStyle = new Label.LabelStyle(skin.getFont("title"), Color.WHITE);
        Label headerLabel = new Label(AssetGenerationHelper.i18n.get("game_header"), headerStyle);

        // 3. Info Button (Right)
        Texture infoTex = new Texture(Gdx.files.internal("circle.png"));
        infoTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        com.badlogic.gdx.scenes.scene2d.ui.ImageButton infoButton = new com.badlogic.gdx.scenes.scene2d.ui.ImageButton(
                new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(
                        new com.badlogic.gdx.graphics.g2d.TextureRegion(infoTex)));
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(1.0f);
                showInfoDialog();
            }
        });

        // Assemble Layout
        hudTable.add(statsTable).left();
        hudTable.add(headerLabel).expandX().center();
        hudTable.add(infoButton).size(64).right();

        stage.addActor(hudTable);

        // Initialize UI Elements for Dialog
        initializeUIStyles(uiFont);
    }

    private void initializeUIStyles(BitmapFont font) {
        // Button Style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(Color.DARK_GRAY);
        p.fill();
        skin.add("btn-up", new Texture(p));
        p.setColor(Color.GRAY);
        p.fill();
        skin.add("btn-down", new Texture(p));
        buttonStyle.up = skin.newDrawable("btn-up");
        buttonStyle.down = skin.newDrawable("btn-down");
        skin.add("default", buttonStyle);

        // Window Style
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.RED;
        Pixmap bgMap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        bgMap.setColor(0f, 0f, 0f, 0.9f);
        bgMap.fill();
        skin.add("dialog-bg", new Texture(bgMap));
        windowStyle.background = skin.newDrawable("dialog-bg");
        skin.add("default", windowStyle);
    }

    @Override
    public void show() {
        com.badlogic.gdx.InputMultiplexer multiplexer = new com.badlogic.gdx.InputMultiplexer();
        multiplexer.addProcessor(stage); // Stage first for UI
        multiplexer.addProcessor(this); // Game logic second
        Gdx.input.setInputProcessor(multiplexer);

        // Initial setup
        tree.insert(50);
        rebuildStage();

        // Start the game loop
        spawnNextNode();
    }

    // --- Game Logic ---

    private void spawnNextNode() {
        if (currentState == GameState.GAME_OVER)
            return;
        currentState = GameState.SPAWNING;

        int nextValue = numberGenerator.generateUniqueNumber();
        TreeNode tempNode = new TreeNode(nextValue);
        activeFallingNode = new NodeActor(tempNode, skin, circleTexture);

        // Spawn below Top Margin to avoid clipping
        float startX = Gdx.graphics.getWidth() / 2f - activeFallingNode.getWidth() / 2;
        float startY = Gdx.graphics.getHeight() - TOP_MARGIN;

        activeFallingNode.setPosition(startX, startY);
        stage.addActor(activeFallingNode);

        // Target: Root comparison
        if (tree.root != null) {
            currentComparisonNode = nodeActors.get(tree.root);
            moveToComparison();
        } else {
            tree.insert(nextValue);
            activeFallingNode.remove();
            rebuildStage();
            spawnNextNode();
        }
    }

    private void moveToComparison() {
        if (currentState == GameState.GAME_OVER)
            return;
        currentState = GameState.MOVING;

        float targetX = currentComparisonNode.getX();
        float targetY = currentComparisonNode.getY() + 80; // Above

        // Speed Ramping: Faster as score increases
        float baseDuration = 0.8f;
        float duration = baseDuration / gameStateManager.getSpeedMultiplier();

        activeFallingNode.addAction(Actions.sequence(
                // Cartoon Juice: BounceOut for "Slam" effect
                Actions.moveTo(targetX, targetY, duration, Interpolation.bounceOut),
                Actions.run(() -> {
                    if (currentState != GameState.GAME_OVER) {
                        currentState = GameState.WAITING_FOR_DECISION;
                    }
                })));
    }

    private void addPulseAction() {
    }

    private void lockNode() {
        if (currentState == GameState.GAME_OVER || currentState == GameState.GAME_WON)
            return;

        float effectX = activeFallingNode.getX();
        float effectY = activeFallingNode.getY();

        // Try to insert
        boolean success = tree.insert(activeFallingNode.getNodeData().value);

        activeFallingNode.remove();
        activeFallingNode = null;

        if (!success) {
            // Tree too deep (Mistake)
            flashFeedback(false);

            // Show "Tree Too Deep!" popup
            Label errorPopup = new Label(AssetGenerationHelper.i18n.get("tree_too_deep"), skin, "title");
            errorPopup.setColor(Color.RED);
            errorPopup.setPosition(Gdx.graphics.getWidth() / 2f - errorPopup.getWidth() / 2f,
                    Gdx.graphics.getHeight() / 2f);
            errorPopup.addAction(Actions.sequence(
                    Actions.parallel(Actions.moveBy(0, 50, 1.5f), Actions.fadeOut(1.5f)),
                    Actions.removeActor()));
            stage.addActor(errorPopup);

            spawnNextNode();
            return;
        }

        rebuildStage();

        // Spawn Floating Score Text (Juice)
        Label scorePopup = new Label("+100", skin, "default");
        scorePopup.setColor(Color.GREEN); // Neon Green
        scorePopup.setPosition(effectX, effectY);

        scorePopup.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, 50, 1.0f, Interpolation.pow2Out),
                        Actions.fadeOut(1.0f)),
                Actions.removeActor()));

        stage.addActor(scorePopup);

        // Check Victory
        if (gameStateManager.checkWinCondition()) {
            currentState = GameState.GAME_WON;
            victorySound.play(1.0f);
            showVictorySequence();
        } else {
            spawnNextNode();
        }
    }

    private void processCorrectMove(boolean goLeft) {
        if (currentState == GameState.GAME_OVER)
            return;
        activeFallingNode.clearActions();
        activeFallingNode.setScale(1f);

        TreeNode compareLogicNode = currentComparisonNode.getNodeData();
        TreeNode nextLogicNode = goLeft ? compareLogicNode.left : compareLogicNode.right;

        if (nextLogicNode == null) {
            gameStateManager.registerSuccess();
            lockNode();
        } else {
            if (nodeActors.containsKey(nextLogicNode)) {
                gameStateManager.incrementScore(10);
                currentComparisonNode = nodeActors.get(nextLogicNode);
                moveToComparison();
            }
        }
    }

    private void flashFeedback(boolean positive) {
        if (currentState == GameState.GAME_OVER)
            return;
        if (!positive) {
            boolean gameOver = gameStateManager.registerMistake();
            activeFallingNode.addAction(Actions.sequence(
                    Actions.color(Color.RED, 0.1f),
                    Actions.color(Color.WHITE, 0.1f)));

            if (gameOver) {
                currentState = GameState.GAME_OVER;
                triggerCinematicGameOver();
            }
        }
    }

    private void showInfoDialog() {
        Dialog dialog = new Dialog("", skin, "square-dialog") {
            @Override
            public void result(Object object) {
                // Just close
            }
        };

        Label text = new Label(AssetGenerationHelper.i18n.get("info_text"), skin, "info-body");
        text.setWrap(true);
        text.setAlignment(Align.center);

        // Add text with defined width to support wrapping
        dialog.getContentTable().add(text).width(450f).pad(20);

        TextButton closeBtn = new TextButton(AssetGenerationHelper.i18n.get("btn_close"), skin, "cyber");

        // Sound listener
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(1.0f);
            }
        });

        dialog.button(closeBtn, true);

        dialog.show(stage);
    }

    private void triggerCinematicGameOver() {
        // 1. Ensure "white" pixel exists for fade overlay
        if (!skin.has("white", Texture.class)) {
            Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            p.setColor(Color.WHITE);
            p.fill();
            skin.add("white", new Texture(p));
            p.dispose();
        }

        // 2. Fade Out Overlay
        com.badlogic.gdx.scenes.scene2d.ui.Image overlay = new com.badlogic.gdx.scenes.scene2d.ui.Image(
                skin.newDrawable("white", Color.BLACK));

        overlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        overlay.setColor(0, 0, 0, 0f); // Invisible start
        stage.addActor(overlay);

        // 2. Labels (I18N)
        Label gameOverLabel = new Label(AssetGenerationHelper.i18n.get("game_over"), skin, "title");
        gameOverLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - gameOverLabel.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - gameOverLabel.getHeight() / 2f);
        gameOverLabel.setColor(1, 1, 1, 0); // Transparent start
        stage.addActor(gameOverLabel);

        Label finalScoreLabel = new Label(AssetGenerationHelper.i18n.format("final_score", gameStateManager.getScore()),
                skin);
        finalScoreLabel.setColor(Color.WHITE);
        finalScoreLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - finalScoreLabel.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - 100 // Below title
        );
        finalScoreLabel.setColor(1, 1, 1, 0); // Transparent start
        stage.addActor(finalScoreLabel);

        // 3. Cinematic Sequence
        stage.addAction(Actions.sequence(
                // Step A: Fade to Black (overlay) + Audio
                Actions.run(() -> {
                    overlay.addAction(Actions.alpha(0.9f, 1.5f));
                    gameOverSound.play(1.0f);
                }),
                Actions.delay(1.5f), // Wait for fade

                // Step B: Text Reveal (Game Over)
                Actions.run(() -> {
                    gameOverLabel.addAction(Actions.parallel(
                            Actions.fadeIn(0.5f),
                            Actions.moveBy(0, 50, 1.5f, Interpolation.sineOut) // float up a bit
                    ));
                }),
                Actions.delay(0.5f),

                // Step C: Score Reveal
                Actions.run(() -> finalScoreLabel.addAction(Actions.fadeIn(0.5f))),

                // Step D: Wait & Restart
                Actions.delay(3.0f),
                Actions.run(() -> ((com.arboreus.ArboreusGame) Gdx.app.getApplicationListener())
                        .setScreen(new com.arboreus.screens.MainMenuScreen()))));
    }

    private void showVictorySequence() {
        // 1. Ensure "white" pixel exists
        if (!skin.has("white", Texture.class)) {
            Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            p.setColor(Color.WHITE);
            p.fill();
            skin.add("white", new Texture(p));
            p.dispose();
        }

        // 2. Gold Overlay
        com.badlogic.gdx.scenes.scene2d.ui.Image overlay = new com.badlogic.gdx.scenes.scene2d.ui.Image(
                skin.newDrawable("white", new Color(1f, 0.84f, 0f, 1f))); // Gold
        overlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        overlay.setColor(1, 1, 1, 0f);
        stage.addActor(overlay);

        // 3. Victory Labels
        Label titleLabel = new Label(AssetGenerationHelper.i18n.get("game_won_title"), skin, "title");
        titleLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - titleLabel.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f + 50);
        titleLabel.setColor(1, 1, 1, 0);
        stage.addActor(titleLabel);

        Label textLabel = new Label(AssetGenerationHelper.i18n.get("game_won_text"), skin, "info-body");
        textLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - textLabel.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - 20);
        textLabel.setColor(1, 1, 1, 0);
        stage.addActor(textLabel);

        // 4. Back Button
        TextButton backBtn = new TextButton(AssetGenerationHelper.i18n.get("btn_back_menu"), skin, "cyber");
        backBtn.setPosition(
                Gdx.graphics.getWidth() / 2f - backBtn.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - 100);
        backBtn.setColor(1, 1, 1, 0);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                ((com.arboreus.ArboreusGame) Gdx.app.getApplicationListener())
                        .setScreen(new com.arboreus.screens.MainMenuScreen());
            }
        });
        stage.addActor(backBtn);

        // 5. Sequence
        stage.addAction(Actions.sequence(
                Actions.run(() -> overlay.addAction(Actions.alpha(0.7f, 1.0f))),
                Actions.delay(0.5f),
                Actions.run(() -> titleLabel.addAction(Actions.fadeIn(0.5f))),
                Actions.delay(0.5f),
                Actions.run(() -> textLabel.addAction(Actions.fadeIn(0.5f))),
                Actions.delay(0.5f),
                Actions.run(() -> backBtn.addAction(Actions.fadeIn(0.5f)))));
    }

    // --- Rendering & Stage Management ---

    private void addNodeActorsRecursive(TreeNode node, TreeNode parentNode) {
        if (node == null)
            return;

        NodeActor actor = new NodeActor(node, skin, circleTexture);

        // Position logic
        float targetX = node.targetX - actor.getWidth() / 2;
        float targetY = node.targetY - actor.getHeight() / 2;

        actor.setPosition(targetX, targetY);

        stage.addActor(actor);
        nodeActors.put(node, actor);

        addNodeActorsRecursive(node.left, node);
        addNodeActorsRecursive(node.right, node);
    }

    public void rebuildStage() {
        NodeActor tempActive = activeFallingNode;
        if (tempActive != null)
            tempActive.remove();

        stage.clear();
        nodeActors.clear();

        if (hudTable != null) {
            stage.addActor(hudTable);
        }

        float startX = Gdx.graphics.getWidth() / 2f;
        float startY = Gdx.graphics.getHeight() - TOP_MARGIN - 80;

        tree.recalculatePositions(startX, startY, Gdx.graphics.getWidth() / 2f, 80);

        if (tree.root != null) {
            addNodeActorsRecursive(tree.root, null);
        }

        if (tempActive != null) {
            stage.addActor(tempActive);
            activeFallingNode = tempActive;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.begin(ShapeType.Filled);
        Color topColor = new Color(0.2f, 0.8f, 0.9f, 1f);
        Color bottomColor = new Color(0.4f, 0.2f, 0.6f, 1f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                bottomColor, bottomColor, topColor, topColor);
        shapeRenderer.end();

        stage.act(delta);

        if (gameStateManager != null && scoreLabel != null && livesLabel != null) {
            scoreLabel.setText("SCORE: " + gameStateManager.getScore());
            livesLabel.setText("LIVES: " + gameStateManager.getLives());
        }

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.CYAN);

        for (NodeActor actor : nodeActors.values()) {
            TreeNode node = actor.getNodeData();
            if (node == null)
                continue;

            if (node.left != null && nodeActors.containsKey(node.left)) {
                NodeActor leftActor = nodeActors.get(node.left);
                if (leftActor != null) {
                    shapeRenderer.line(actor.getX() + actor.getWidth() / 2, actor.getY() + actor.getHeight() / 2,
                            leftActor.getX() + leftActor.getWidth() / 2, leftActor.getY() + leftActor.getHeight() / 2);
                }
            }
            if (node.right != null && nodeActors.containsKey(node.right)) {
                NodeActor rightActor = nodeActors.get(node.right);
                if (rightActor != null) {
                    shapeRenderer.line(actor.getX() + actor.getWidth() / 2, actor.getY() + actor.getHeight() / 2,
                            rightActor.getX() + rightActor.getWidth() / 2,
                            rightActor.getY() + rightActor.getHeight() / 2);
                }
            }
        }
        shapeRenderer.end();

        stage.draw();

        drawHUD();
    }

    private void drawHUD() {
        if (currentState == GameState.WAITING_FOR_DECISION) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.begin(ShapeType.Filled);

            float centerY = Gdx.graphics.getHeight() / 2f;
            float arrowSize = 40f;
            float margin = 20f;

            // Left Arrow
            boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
            float leftAlpha = leftPressed ? 0.8f : 0.2f;
            shapeRenderer.setColor(0.6f, 0.9f, 1f, leftAlpha); // Soft Cyan Glass
            // Triangle pointing left: (20, Cy), (60, Cy+20), (60, Cy-20)
            shapeRenderer.triangle(
                    margin, centerY,
                    margin + arrowSize, centerY + arrowSize / 2,
                    margin + arrowSize, centerY - arrowSize / 2);

            // Right Arrow
            boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            float rightAlpha = rightPressed ? 0.8f : 0.2f;
            shapeRenderer.setColor(0.6f, 0.9f, 1f, rightAlpha);
            // Triangle pointing right: (W-20, Cy), (W-60, Cy+20), (W-60, Cy-20)
            float rightX = Gdx.graphics.getWidth() - margin;
            shapeRenderer.triangle(
                    rightX, centerY,
                    rightX - arrowSize, centerY + arrowSize / 2,
                    rightX - arrowSize, centerY - arrowSize / 2);

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        rebuildStage();
    }

    @Override
    public void dispose() {
        if (stage != null)
            stage.dispose();
        if (shapeRenderer != null)
            shapeRenderer.dispose();
        if (skin != null)
            skin.dispose();
        if (circleTexture != null)
            circleTexture.dispose();
        if (clickSound != null)
            clickSound.dispose();
        if (gameOverSound != null)
            gameOverSound.dispose();
        if (victorySound != null)
            victorySound.dispose();
    }

    // --- InputProcessor Implementation ---

    @Override
    public boolean keyDown(int keycode) {
        if (currentState == GameState.GAME_OVER)
            return false;
        if (currentState != GameState.WAITING_FOR_DECISION)
            return false;

        int fallingVal = activeFallingNode.getNodeData().value;
        int compareVal = currentComparisonNode.getNodeData().value;

        if (keycode == Input.Keys.LEFT) {
            clickSound.play(1.0f);
            if (tree.shouldGoLeft(fallingVal, compareVal)) {
                processCorrectMove(true);
            } else {
                flashFeedback(false);
            }
            return true;
        } else if (keycode == Input.Keys.RIGHT) {
            clickSound.play(1.0f);
            if (tree.shouldGoRight(fallingVal, compareVal)) {
                processCorrectMove(false);
            } else {
                flashFeedback(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}
