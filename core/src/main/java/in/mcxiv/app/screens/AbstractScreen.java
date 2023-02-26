package in.mcxiv.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import in.mcxiv.app.AppManager;
import in.mcxiv.app.util.MaUtil;

public abstract class AbstractScreen implements Screen, InputProcessor {

    private Pool<Vector3> POOL = new Pool<Vector3>() {
        @Override
        protected Vector3 newObject() {
            return new Vector3();
        }
    };
    private FrameBuffer fbo;
    private ShaderProgram shader;
    private Array<Vector3> mouseTouches = new Array<>();

    private final Vector3 cache3d = new Vector3();

    protected AppManager app;
    protected MaUtil maUtil;
    public SpriteBatch batch;
    public Matrix4 identity;
    public Stage stage;
    public VisTable root;

    public AbstractScreen(AppManager app) {
        this.app = app;
        this.maUtil = app.maUtil;
    }

    @Override
    public void show() {
        if (batch != null) {
            Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
            return;
        }

        batch = new SpriteBatch();
        identity = batch.getProjectionMatrix().cpy();
//        stage = new Stage(new ScreenViewport(), batch);
        stage = new Stage(new ExtendViewport(880, 1720), batch);
//        stage.setDebugAll(true);
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));

        root = new VisTable();
        root.setFillParent(true);
        root.defaults().left().growX().pad(40, 60, 40, 60);

        stage.addActor(root);

        create();

        String vertexShader = Gdx.files.internal("shaders/vert_shock.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/frag_super_shock.glsl").readString();

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled())
            System.out.println(shader.getLog());
        ShaderProgram.pedantic = false;

//        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, (int) stage.getViewport().getWorldWidth(), (int) stage.getViewport().getWorldHeight(), true);
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    abstract void create();

    public void renderWOShaders(float delta) {
        stage.act(Math.min(delta, 1 / 30f));
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void render(float delta) {
        fbo.begin();
        renderWOShaders(delta);
        fbo.end();

        Texture texture = fbo.getColorBufferTexture();
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.flip(false, true);

        float[] centersx = new float[mouseTouches.size];
        float[] centersy = new float[mouseTouches.size];
        float[] progress = new float[mouseTouches.size];
        for (int i = 0; i < mouseTouches.size; i++) {
            Vector3 pair = mouseTouches.get(i);
            centersx[i] = pair.x;
            centersy[i] = pair.y;
            progress[i] = pair.z;
        }

        batch.setProjectionMatrix(identity);
        batch.begin();
        batch.setShader(shader);
        shader.setUniform1fv("centersx", centersx, 0, mouseTouches.size);
        shader.setUniform1fv("centersy", centersy, 0, mouseTouches.size);
        shader.setUniform1fv("progresses", progress, 0, mouseTouches.size);
        shader.setUniformi("howMany", mouseTouches.size);
        shader.setUniformf("viewWidth", stage.getViewport().getWorldWidth());
        shader.setUniformf("viewHeight", stage.getViewport().getWorldHeight());
        batch.draw(textureRegion, 0, 0, fbo.getWidth(), fbo.getHeight());
        batch.end();
        batch.setShader(null);

        for (int i = 0; i < mouseTouches.size; i++) {
            mouseTouches.get(i).z += delta;
            if (mouseTouches.get(i).z > 2)
                POOL.free(mouseTouches.removeIndex(i--));
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 v = stage.getViewport().getCamera().unproject(POOL.obtain().set(screenX, screenY, 0))
                .scl(1f / stage.getViewport().getWorldWidth(), 1f / stage.getViewport().getWorldHeight(), 1);
        v.z = 0;
        mouseTouches.add(v);
        return true;
    }

    public final ChangeListener __(Runnable action) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        };
    }

    public void fieldTitle(String title) {
        root.add(maUtil.label(title, "small")).padLeft(130).padBottom(10).row();
    }

    public Cell fieldTitleFor(String title) {
        return root.add(maUtil.label(title)).padLeft(130).padBottom(10);
    }

    public void fieldTitle(String title, int padTop) {
        root.add(maUtil.label(title, "small")).padLeft(130).padBottom(10).padTop(padTop).row();
    }

    public Cell<VisLabel> fieldTitleFor(String title, String style) {
        return root.add(maUtil.label(title, style)).padLeft(130).padBottom(10);
    }

    public void fieldActor(Actor actor) {
        root.add(actor).padTop(0).row();
    }

    public Cell fieldActorFor(Actor actor) {
        return root.add(actor).padTop(0);
    }

    public void fieldActor(Actor actor, int padTop) {
        root.add(actor).padTop(padTop).row();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public boolean keyDown(int keycode) {
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
}
