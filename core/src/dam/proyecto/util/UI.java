package dam.proyecto.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import org.w3c.dom.Text;

public class UI {

    private static final float anchuraPantalla = Gdx.graphics.getWidth();
    private static final float alturaPantalla = Gdx.graphics.getHeight();
    private static final float anchuraCamara = 540.0f;
    private static final float alturaCamara = (anchuraCamara * alturaPantalla / anchuraPantalla);
    private static Skin skin;
    private static final int MEDIUM = 1536;
    private static final int HIGH = 2048;
    private static String aspecto; //Glassy, Default

    //Patrón Factory Method para crear un botón de imagen
    public static ImageButton createImageButton(Texture texture) {
        return
                new ImageButton(
                        new TextureRegionDrawable(
                                new TextureRegion(texture) ) );
    }

    //Patrón Factory Method para crear un botón de texto
    public static TextButton createTextButton(String text) {
        if(aspecto.equalsIgnoreCase("glassy")){
            return new TextButton(text,getSkin(),"small");
        }else{
            return new TextButton(text, getSkin());
        }
    }

    //Patrón Factory Method para crear un área de texto
    public static TextField createTextField(String text) {
        return
                new TextField(text, getSkin());
    }

    //Patrón Factory Method para crear un label
    public static Label createLabel(String text){
        if(aspecto.equalsIgnoreCase("glassy")){
            return new Label(text,getSkin(),"black");
        }else{
            return new Label(text, getSkin());
        }
    }

    //Metodo para devolver un "skin" según el aspecto seleccionado a los elementos de la UI
    public static Skin getSkin() {
        //Skin Glassy
        if(aspecto.equalsIgnoreCase("glassy")){
            if (skin == null) {
                skin = new Skin(Gdx.files.internal("skin/glassy/glassy-ui.json"));
            }
            if (anchuraPantalla < MEDIUM){
                skin.getFont("font").getData().setScale(1.0f);
                skin.getFont("font-big").getData().setScale(0.5f);
            }
            else if (anchuraPantalla >= MEDIUM && anchuraPantalla < HIGH){
                skin.getFont("font").getData().setScale(2.4f);
                skin.getFont("font-big").getData().setScale(1.2f);
            }
            else{
                skin.getFont("font").getData().setScale(3.6f);
                skin.getFont("font-big").getData().setScale(1.8f);
            }
        //Skin Default
        }else {
            if (skin == null) {
                skin = new Skin(Gdx.files.internal("skin/default/uiskin.json"));
            }
            if (anchuraPantalla < MEDIUM)
                skin.getFont("default-font").getData().setScale(1.5f);
            else if (anchuraPantalla >= MEDIUM && anchuraPantalla < HIGH)
                skin.getFont("default-font").getData().setScale(4.0f);
            else
                skin.getFont("default-font").getData().setScale(6.0f);
        }
        return skin;
    }

    //Método para determinar la terminación de los archivos de imagen, adaptados según la resolución
    public static String ending(){
        String ending;
        if(anchuraPantalla < MEDIUM)
            ending = "-xhdpi.png";
        else if (anchuraPantalla >= MEDIUM && anchuraPantalla < HIGH)
            ending = "-xxhdpi.png";
        else
            ending = "-xxxhdpi.png";
        return  ending;
    }



    //Getters y Setters de la UI
    public static float getAnchuraPantalla() {
        return anchuraPantalla;
    }

    public static float getAlturaPantalla() {
        return alturaPantalla;
    }

    public static float getAnchuraCamara() {
        return anchuraCamara;
    }

    public static float getAlturaCamara() {
        return alturaCamara;
    }

    public static String getAspecto() {
        return aspecto;
    }

    public static void setAspecto(String aspecto) {
        UI.aspecto = aspecto;
        skin = null;
    }
}
