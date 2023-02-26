package in.mcxiv.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import in.mcxiv.app.AppManager;

public class LoginScreen extends AbstractScreen {

    Model model;
    ModelInstance modelInstance;
    AnimationController animationController;
    ModelBatch modelBatch;
    Environment environment;
    float time_elapsed = 0;

    public LoginScreen(AppManager app) {
        super(app);
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
        super.show();
        for (Actor child : root.getChildren())
            if(child instanceof VisTextField)
                ((VisTextField) child).setText("");
    }

    public void create() {

        model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("logo2.g3dj"));
        modelInstance = new ModelInstance(model);
        modelInstance.transform
                .setFromEulerAngles(0, 10, 0)
                .setTranslation(stage.getViewport().getWorldWidth() / 2, stage.getViewport().getWorldHeight()-700, 100)
                .scale(100,100,100);
        animationController = new AnimationController(modelInstance);
        animationController.setAnimation("Sphere.002|Sphere.002Action", -1, 1f, null);

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .6f, .6f, .6f, .6f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

//        fieldActorFor(new Image(VisUI.getSkin().getRegion("logo")))
//                .expand(0, 0).fill(0, 0).center().row();
        root.add().pad(300).row();

        fieldTitle("EMAIL");
        fieldActor(maUtil.field("email"));

        fieldTitle("PASSWORD");
        fieldActor(maUtil.field("password"));

        fieldActor(maUtil.blueButton("LOGIN", () -> app.setScreen(app.mainScreen)), 70);

        Table bar = new VisTable();
        root.add(bar).expand(0, 0).fill(0, 0).center();
        bar.add(maUtil.label("Don't have an account? ", "grey"));
        bar.add(new LinkLabel("Sign up") {{
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    app.setScreen(app.signupScreen);
                }
            });
        }});
    }

    @Override
    public void render(float delta) {
        super.render(delta);
//        stage.getViewport().getCamera().lookAt(modelInstance.transform.getTranslation(new Vector3()));

    }

    @Override
    public void renderWOShaders(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        super.renderWOShaders(delta);
        stage.getViewport().getCamera().update();
        stage.getViewport().getCamera().far = 1000;
        stage.getViewport().getCamera().near = 100;
        stage.getViewport().getCamera().position.z=500;

        modelBatch.begin(stage.getViewport().getCamera());
        modelBatch.render(modelInstance, environment);
        modelBatch.end();
        animationController.update(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        modelBatch.dispose();
        model.dispose();
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
