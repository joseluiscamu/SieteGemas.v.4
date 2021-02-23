package dam.proyecto;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dam.proyecto.pantallas.EntradaScreen;
import dam.proyecto.util.UI;

public class SieteGemas extends Game {

	public OrthographicCamera camera;
	public SpriteBatch batch;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(new InputMultiplexer());

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,UI.getAnchuraPantalla(),UI.getAlturaPantalla());
		this.setScreen(new EntradaScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
