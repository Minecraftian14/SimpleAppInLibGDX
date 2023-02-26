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

import java.time.LocalDate;

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
        tabbedPane.switchTab(0);
        
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

        tabbedPane.add(tab(contentBottom("week"), "WEEK"));
        tabbedPane.add(tab(contentBottom("month"), "MONTH"));
        tabbedPane.add(tab(contentBottom("year"), "YEAR"));
        tabbedPane.switchTab(0);

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
        int r = name.equals("week") ? 7 : name.equals("month") ? 30 : name.equals("year") ? 365 : 1000;
        VisTable content = new VisTable();
        content.defaults().growX().pad(0, 20, 10, 20);
        content.add(new VisLabel("TOTAL", "small")).row();
        content.add(new VisLabel("" + ((43 * r) / 7))).padBottom(60);
        LocalDate now = LocalDate.now();
        LocalDate pre = r == 7 ? now.minusDays(7) : r == 30 ? now.minusMonths(1) : now.minusYears(1);
        String yea = String.valueOf(now.getYear()).substring(2);
        String lye = String.valueOf(pre.getYear()).substring(2);
        String mon = String.valueOf(now.getMonth()).substring(0, 3);
        String lmo = String.valueOf(pre.getMonth()).substring(0, 3);
        String dat = String.valueOf(now.getDayOfMonth());
        String lda = String.valueOf(pre.getDayOfMonth());
        content.add(new VisLabel(
                        r == 7 ? String.format("%s %s - %s %s", lda, lmo, dat, mon) :
                                String.format("%s %s %s - %s %s %s", lda, lmo, lye, dat, mon, yea),
                        "small")).padBottom(60)
                .fill(0, 0).right().row();

        ShapeDrawer drawer = new ShapeDrawer(batch, VisUI.getSkin().getRegion("pixel"));
        float values[] = new float[r];
        for (int i = 0; i < r; i++)
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

                if (r <= 30) {

                    for (int i = 0; i < r + 1; i++)
                        drawer.line(x + i * w / r, y, x + i * w / r, y + h, VisUI.getSkin().getColor("ui-grey"));

                    for (int i = 0; i < r; i++) {
                        float ah = (h - w / (4 * r)) * values[i];
                        float ax = x + (i * 8 + 4) * w / (8 * r);
                        drawer.filledRectangle(ax - w / (8 * r), y + w / (8 * r), w / (4 * r), ah, VisUI.getSkin().getColor("ui-blue"));
                        drawer.filledCircle(ax, y + w / (8 * r), w / (8 * r), VisUI.getSkin().getColor("ui-blue"));
                        drawer.filledCircle(ax, y + w / (8 * r) + ah, w / (8 * r), VisUI.getSkin().getColor("ui-blue"));
                    }
                } else {
                    for (int i = 0; i < r; i++)
                        drawer.filledRectangle(x + i * w / r, y, w / r, h * values[i], VisUI.getSkin().getColor("ui-blue"));
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
