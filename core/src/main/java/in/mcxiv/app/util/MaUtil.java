package in.mcxiv.app.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.layout.DragPane;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import in.mcxiv.app.AppManager;

public class MaUtil {

    AppManager app;

    public MaUtil(AppManager app) {
        this.app = app;
    }

    public VisTextButton whiteButton(String text) {
        VisTextButton button = new VisTextButton(text);
        button.getStyle().fontColor = VisUI.getSkin().getColor("ui-grey");
        return button;
    }

    public VisTextButton blueButton(String text) {
        VisTextButton button = new VisTextButton(text, "blue");
        button.pad(28);
        button.getStyle().fontColor = VisUI.getSkin().getColor("ui-white");
        return button;
    }

    public VisLabel label(String text) {
        return new VisLabel(text);
    }

    public VisLabel label(String text, String style) {
        return new VisLabel(text, style);
    }

    public VisTextField field(String style) {
        VisTextField field = new VisTextField("", style);
        field.getStyle().fontColor = VisUI.getSkin().getColor("ui-grey");
        field.setPasswordMode(style.equals("password"));
        field.setPasswordCharacter('*');
        return field;
    }

    public VisSelectBox<String> combo() {
        VisSelectBox<String> box = new VisSelectBox<>();
        box.getStyle().fontColor = VisUI.getSkin().getColor("ui-grey");
        box.setItems("A", "B", "C", "D", "E");
        return box;
    }

    public Actor blueButton(String text, Runnable action) {
        VisTextButton button = blueButton(text);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        return button;
    }

    public void hackTheForbiddenBonesOutFromTheirCharredLives(TabbedPane tabbedPane) {
        DragPane g = (DragPane) tabbedPane.getTable().getChild(0);
        SnapshotArray<Actor> children = g.getChildren();
        VisTable g2 = new VisTable();
        while (children.size > 0) {
            Table actor = ((Table) children.get(0));
            actor.getCell(actor.getChild(0)).growX();
            g2.add(actor).growX().uniformX();
        }
        tabbedPane.getTable().clearChildren();
        tabbedPane.getTable().add(g2).growX();
    }
}
