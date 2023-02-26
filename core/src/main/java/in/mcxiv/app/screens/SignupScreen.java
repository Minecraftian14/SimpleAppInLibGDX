package in.mcxiv.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.kotcrab.vis.ui.VisUI;
import com.ray3k.tenpatch.TenPatchDrawable;
import in.mcxiv.app.AppManager;

public class SignupScreen extends AbstractScreen {

    public SignupScreen(AppManager app) {
        super(app);
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        super.show();
    }

    public void create() {
        root.add(new ImageButton(VisUI.getSkin().getDrawable("cross")) {{
            addListener(__(() -> app.setScreen(app.loginScreen)));
        }}).fill(0, 0).right().row();

        fieldTitleFor("Sign Up", "big").padLeft(100).row();
        fieldTitleFor("Enter your phone number to sign up").padLeft(100).row();

        fieldTitle("COUNTRY", 40);
        fieldActor(maUtil.combo());

        fieldTitle("LABEL");
        fieldActor(maUtil.field("password"));

        fieldActor(maUtil.blueButton("CONTINUE"), 40);

        TenPatchDrawable tenPatchDrawable = new TenPatchDrawable(
                new int[]{10, 20, 156, 166},
                new int[]{},
                false,
                VisUI.getSkin().getRegion("sep")
        );
        root.add(new Image(tenPatchDrawable)).growX().row();

        root.add(maUtil.whiteButton("CONTINUE WITH FACEBOOK")).padTop(30).row();
        root.add(maUtil.whiteButton("CONTINUE WITH TWITTER")).padTop(30).row();
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
    public void render(float delta) {
        super.render(delta);
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
