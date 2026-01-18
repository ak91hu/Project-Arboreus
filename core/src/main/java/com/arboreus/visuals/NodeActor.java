package com.arboreus.visuals;

import com.arboreus.logic.TreeNode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class NodeActor extends Group {
    private TreeNode nodeData;
    private Image background;
    private Image glow;
    private Label label;

    public NodeActor(TreeNode nodeData, Skin skin, Texture circleTexture) {
        this.nodeData = nodeData;

        // 1. Glow Effect (Behind main circle)
        glow = new Image(circleTexture);
        glow.setColor(0f, 0.8f, 1f, 0.5f); // Neon Blue, Transparent
        glow.setSize(70, 70); // Slightly larger than main
        glow.setOrigin(Align.center);
        glow.setPosition(-5, -5); // Offset to center relative to main (60x60)
        addActor(glow);

        // 2. Main Background Circle (Cyber-Nature: Neon Yellow tint)
        background = new Image(circleTexture);
        background.setColor(Color.WHITE); // Use texture colors directly
        background.setSize(60, 60);
        background.setOrigin(Align.center);
        addActor(background);

        // 3. Label
        label = new Label(String.valueOf(nodeData.value), skin);
        label.setAlignment(Align.center);
        // High contrast text for dark center
        label.setColor(Color.CYAN);
        // Center label on the background (60x60)
        label.setPosition(
                (60 - label.getWidth()) / 2,
                (60 - label.getHeight()) / 2);
        addActor(label);

        // Group Settings
        setSize(60, 60); // Logical size for layout
        setOrigin(Align.center);

        // 4. Pulse Animation (Juice)
        this.addAction(Actions.forever(
                Actions.sequence(
                        Actions.scaleTo(1.05f, 1.05f, 0.5f, com.badlogic.gdx.math.Interpolation.sine),
                        Actions.scaleTo(0.95f, 0.95f, 0.5f, com.badlogic.gdx.math.Interpolation.sine))));
    }

    public TreeNode getNodeData() {
        return nodeData;
    }
}
