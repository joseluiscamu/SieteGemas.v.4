package dam.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Gema {
    private int index;
    private String color = "";
    private Vector2 posicion = null;
    private boolean encontrada = false;
    private Texture imgGema = null;
    private Animation animationGema = null;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public void setPosicion(Vector2 posicion) {
        this.posicion = posicion;
    }

    public boolean isEncontrada() {
        return encontrada;
    }

    public void setEncontrada(boolean encontrada) {
        this.encontrada = encontrada;
    }

    public Texture getImgGema() {
        return imgGema;
    }

    public void setImgGema(Texture imgGema) {
        this.imgGema = imgGema;
    }

    public Animation getAnimationGema() {
        return animationGema;
    }

    public void setAnimationGema(Animation animationGema) {
        this.animationGema = animationGema;
    }
}
