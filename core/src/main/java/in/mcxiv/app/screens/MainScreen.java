package in.mcxiv.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import in.mcxiv.app.AppManager;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class MainScreen extends AbstractScreen {

    public MainScreen(AppManager app) {
        super(app);
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        super.show();
    }

    public void create() {
        VisTable row;
        root.add(row = new VisTable()).growX().row();
        row.add().uniformX();
        row.add(new VisLabel("Main", Align.center)).growX();
        row.add(new Image(VisUI.getSkin().getRegion("gear")) {{
            setAlign(Align.right);
        }}).uniformX();

        root.addSeparator();

        TabbedPane tabbedPane;

        final VisTable topContent = new VisTable();
        tabbedPane = new TabbedPane();
        tabbedPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab(Tab tab) {
                topContent.clearChildren();
                topContent.add(tab.getContentTable()).grow();
            }
        });

        root.add(tabbedPane.getTable()).growX().row();
        root.add(topContent).growX().row();

        tabbedPane.add(tab(contentTop("Statistics"), "STATISTICS"));
        tabbedPane.add(tab(contentTop("Journey"), "JOURNEY"));

        maUtil.hackTheForbiddenBonesOutFromTheirCharredLives(tabbedPane);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        root.add(row = new VisTable()).growX().row();
        row.add(new VisLabel("Recent fasts")).expandX().left();
        row.add(new LinkLabel("See more")).right();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        final VisTable botContent = new VisTable();
        tabbedPane = new TabbedPane();
        tabbedPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab(Tab tab) {
                botContent.clearChildren();
                botContent.add(tab.getContentTable()).grow();
            }
        });

        root.add(tabbedPane.getTable()).growX().row();
        root.add(botContent).growX().row();

        tabbedPane.add(tab(contentBottom("Week"), "WEEK"));
        tabbedPane.add(tab(contentBottom("Month"), "MONTH"));
        tabbedPane.add(tab(contentBottom("Year"), "YEAR"));

        maUtil.hackTheForbiddenBonesOutFromTheirCharredLives(tabbedPane);

        root.addSeparator();
    }

    private VisTable contentTop(String name) {
        VisTable content = new VisTable();
        content.defaults().growX().pad(0, 20, 10, 20);
        content.add(new VisLabel("Overview:" + name)).padLeft(0).padBottom(60).colspan(3).row();
        content.add(new VisLabel("TOTAL", "small"));
        content.add(new VisLabel("7-FAST AVG.", "small"));
        content.add(new VisLabel("LONGEST", "small")).row();
        content.add(new VisLabel("93")).padBottom(60);
        content.add(new VisLabel("15.1 h")).padBottom(60);
        content.add(new VisLabel("21 h")).padBottom(60).row();
        content.add(new VisLabel("MAX STREAK", "small"));
        content.add(new VisLabel("STREAK NOW", "small")).row();
        content.add(new VisLabel("27"));
        content.add(new VisLabel("4")).row();
        return content;
    }

    private VisTable contentBottom(String name) {
        VisTable content = new VisTable();
        content.defaults().growX().pad(0, 20, 10, 20);
        content.add(new VisLabel("TOTAL", "small")).row();
        content.add(new VisLabel("43")).padBottom(60);
        content.add(new VisLabel("May 23 - May 29", "small")).padBottom(60)
                .fill(0, 0).right().row();

        ShapeDrawer drawer = new ShapeDrawer(batch, VisUI.getSkin().getRegion("pixel"));
        float values[] = new float[7];
        for (int i = 0; i < 7; i++)
            values[i] = MathUtils.random(0.3f, 0.8f);

        content.add(new Widget() {
            {
                setSize(getPrefWidth(), getPrefHeight());
                invalidate();
            }

            @Override
            public float getMinHeight() {
                return 0;
            }

            @Override
            public float getMinWidth() {
                return 0;
            }

            @Override
            public float getPrefWidth() {
                return Gdx.graphics.getWidth() - 300;
            }

            @Override
            public float getPrefHeight() {
                return getPrefWidth() / 2;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                validate();

                float x = getX(), y = getY(), w = getWidth(), h = getHeight();

                for (int i = 0; i < 8; i++)
                    drawer.line(x + i * w / 7, y, x + i * w / 7, y + h, VisUI.getSkin().getColor("ui-grey"));

                for (int i = 0; i < 7; i++) {
                    float ah = (h - w / 28) * values[i];
                    float ax = x + (i * 8 + 4) * w / 56;
                    drawer.filledRectangle(ax - w / 56, y + w / 56, w / 28, ah, VisUI.getSkin().getColor("ui-blue"));
                    drawer.filledCircle(ax, y + w / 56, w / 56, VisUI.getSkin().getColor("ui-blue"));
                    drawer.filledCircle(ax, y + w / 56 + ah, w / 56, VisUI.getSkin().getColor("ui-blue"));
                }

            }
        }).colspan(2).fill(0, 0);
        return content;
    }

    private Tab tab(VisTable content, String name) {
        return new Tab(false, false) {
            @Override
            public String getTabTitle() {
                return name;
            }

            @Override
            public Table getContentTable() {
                return content;
            }
        };
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            app.setScreen(app.loginScreen);
            return true;
        }
        return super.keyDown(keycode);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
