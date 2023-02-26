package in.mcxiv.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.kotcrab.vis.ui.VisUI;
import in.mcxiv.app.screens.AbstractScreen;
import in.mcxiv.app.screens.LoginScreen;
import in.mcxiv.app.screens.MainScreen;
import in.mcxiv.app.screens.SignupScreen;
import in.mcxiv.app.util.MaUtil;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class AppManager extends Game {

    public Screen loginScreen;
    public Screen signupScreen;
    public Screen mainScreen;
    public MaUtil maUtil;

    private Screen screenTransition = null;
    private final float transitionLength = .25f;
    private float transitionTime = 0;
    private FrameBuffer fbo;

    @Override
    public void create() {
        VisUI.load(Gdx.files.internal("ui/uiskin.json"));

        maUtil = new MaUtil(this);

        loginScreen = new LoginScreen(this);
        signupScreen = new SignupScreen(this);
        mainScreen = new MainScreen(this);

        setScreen(loginScreen);

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void setScreen(Screen screen) {
        screenTransition = getScreen();
        if (screenTransition != null)
            transitionTime = transitionLength;
        super.setScreen(screen);
    }

    @Override
    public void render() {
        if (screenTransition == null || transitionTime <= 0) {
            super.render();
            return;
        }
        if (screen == null) return;

        fbo.begin();
        ((AbstractScreen) screen).renderWOShaders(Gdx.graphics.getDeltaTime());
        ((AbstractScreen) screen).batch.flush();
        fbo.end();

        Texture texture = fbo.getColorBufferTexture();
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.flip(false, true);

        SpriteBatch batch = ((AbstractScreen) screenTransition).batch;
        screenTransition.render(Gdx.graphics.getDeltaTime());
        float progress = transitionTime / transitionLength;
        float anti_progress = 1 - progress;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        batch.begin();
        batch.draw(textureRegion, w * progress / 2, h * progress / 2, w * anti_progress, h * anti_progress);
        batch.end();
        transitionTime -= Gdx.graphics.getDeltaTime();
    }

    @Override
    public void dispose() {
        super.dispose();
        VisUI.dispose();
        fbo.dispose();
    }
}