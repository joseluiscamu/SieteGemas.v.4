package dam.proyecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import dam.proyecto.SieteGemas;
import dam.proyecto.util.UI;

public class EntradaScreen implements Screen {

    final SieteGemas juego;
    private final Texture texturaFondo, texturaTitulo;
    private final Stage stage;
    private Music musicaMenu;

    //Constructor
    public EntradaScreen(final SieteGemas juego) {
        this.juego = juego;
        UI.setAspecto("glassy");

        //Música del menú
        musicaMenu = Gdx.audio.newMusic(Gdx.files.internal("music/intro_by_joshuaempyre_epic-orchestra.mp3"));
        musicaMenu.setLooping(true);

        //Elementos de fondo
        texturaFondo = new Texture(Gdx.files.internal("images/fondo-gemas" + UI.ending()));
        TextureRegion regionFondo = new TextureRegion(texturaFondo, 0, 0, (int) UI.getAnchuraPantalla(), (int) UI.getAlturaPantalla());
        Image fondo = new Image(regionFondo);

        //Tabla de contenidos
        Table tablaEntrada = new Table();
        tablaEntrada.setFillParent(true);
        tablaEntrada.debug();

        float elementWidth = UI.getAnchuraPantalla()/5;
        float elementHeight = UI.getAlturaPantalla()/10;

        //Elementos de la fila 1
        texturaTitulo = new Texture(Gdx.files.internal("images/titulo-gemas" + UI.ending()));
        TextureRegion regionTitulo = new TextureRegion(texturaTitulo);
        Image titulo = new Image(regionTitulo);
        //Elementos de la fila 2
        TextButton textButtonEntrar = UI.createTextButton("Entrar");
        Label labelJugador = UI.createLabel("Jugador");
        TextField textFieldJugador = UI.createTextField("");
        TextButton textButtonComoJugar = UI.createTextButton("? Como Jugar");
        //Elementos de la fila 3
        TextButton textButtonRegistro = UI.createTextButton("Registro");
        Label labelPassword = UI.createLabel("Password");
        TextField textFieldPassword = UI.createTextField("");
        TextButton textButtonAcercaDe = UI.createTextButton("Acerca de ...");
        //Elementos de la fila 4
        Label labelNombre = UI.createLabel("Nombre");
        TextField textFieldNombre = UI.createTextField("");
        //Elementos de la fila 5
        TextButton textButtonSalir = UI.createTextButton("Salir");
        Label labelApellidos = UI.createLabel("Apellidos");
        TextField textFieldApellidos = UI.createTextField("");
        TextButton textButtonOlvido= UI.createTextButton("Recuperar");
        //Elementos de la fila 6
        TextButton textButtonVolver = UI.createTextButton("Volver");
        Label labelEmail = UI.createLabel("Email");
        TextField textFieldEmail = UI.createTextField("");
        TextButton textButtonSiguiente= UI.createTextButton("Siguiente");

        textButtonSiguiente.addListener((new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                juego.setScreen(new JuegoScreen(juego));
                return true;
            }
        }));

        //Fila 1
        tablaEntrada.row().size(UI.getAnchuraPantalla(), titulo.getHeight());
        tablaEntrada.add(titulo).colspan(4).center();
        //Fila 2
        tablaEntrada.row();
        tablaEntrada.add(textButtonEntrar);
        tablaEntrada.add(labelJugador).right();
        tablaEntrada.add(textFieldJugador);
        tablaEntrada.add(textButtonComoJugar).setActorHeight(elementHeight);
        //Fila 3
        tablaEntrada.row();
        tablaEntrada.add(textButtonRegistro);
        tablaEntrada.add(labelPassword).right();
        tablaEntrada.add(textFieldPassword);
        tablaEntrada.add(textButtonAcercaDe);
        //Fila 4
        tablaEntrada.row();
        tablaEntrada.add(labelNombre).colspan(2).right();
        tablaEntrada.add(textFieldNombre);
        //Fila 5
        tablaEntrada.row();
        tablaEntrada.add(textButtonSalir);
        tablaEntrada.add(labelApellidos).right();
        tablaEntrada.add(textFieldApellidos);
        tablaEntrada.add(textButtonOlvido);
        //Fila 6
        tablaEntrada.row();
        tablaEntrada.add(textButtonVolver);
        tablaEntrada.add(labelEmail).right();
        tablaEntrada.add(textFieldEmail);
        tablaEntrada.add(textButtonSiguiente);

        stage = new Stage();
        stage.addActor(fondo);
        stage.addActor(tablaEntrada);
        stage.getCamera().position.set(UI.getAnchuraPantalla()/2,UI.getAlturaPantalla()/2,0);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        juego.camera.update();
        juego.batch.setProjectionMatrix(juego.camera.view);

        juego.batch.begin();
        stage.draw();
        juego.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    @Override
    public void dispose() {
        texturaFondo.dispose();
        texturaTitulo.dispose();
        stage.dispose();
        musicaMenu.dispose();
    }
}