package dam.proyecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import dam.proyecto.Gema;
import dam.proyecto.SieteGemas;
import dam.proyecto.util.UI;

public class JuegoScreen implements Screen, InputProcessor {

    final SieteGemas juego;
    //private Stage stage;
    //Objeto que recoge el mapa de baldosas
    private final TiledMap mapa;
    //Ancho y alto del mapa en tiles
    private final int anchoTiles;
    private final int altoTiles;
    //Posición de finalización del stage
    private final Vector2 celdaFinal;
    //Matriz booleana que contiene los obstáculos del TiledMap.
    private final boolean[][] obstaculo;
    private final boolean[][] checkPoint;
    //Array de objetos Gema
    private final Gema[] gema;
    //Número de gemas en el stage
    private int cuentaGemas;
    //Numero de gemas que van a aparecer en el juego
    private final int numeroGemas = 7;
    //Objeto con el que se pinta el mapa de baldosas
    private final TiledMapRenderer mapaRenderer;
    // Atributo en el que se cargará la imagen del personaje protagonista.
    private final Texture PC;
    // Este atributo indica el tiempo en segundos transcurridos desde que se inicia la animación,
    // servirá para determinar cual es el frame que se debe utilizar
    private float stateTime;
    //Lo mismo para los otros personajes y gemas
    private float stateTimeMovement;
    //Animacion que se muestra en el metodo render()
    private Animation<TextureRegion> personaje;
    //Booleanos para poder mover el sprite de forma continua
    private boolean izquierda, derecha, arriba, abajo;
    //Dimensiones del sprite del personaje
    private final int anchoPersonaje;
    private final int altoPersonaje;
    //Animaciones para cada una de las direcciones de mvto. del personaje
    private final Animation<TextureRegion> personajeArriba;
    private final Animation<TextureRegion> personajeDrcha;
    private final Animation<TextureRegion> personajeAbajo;
    private final Animation<TextureRegion> personajeIzda;
    //Personajes no principales
    private final Texture[] imgNPC;
    private final Animation[] npc;
    //Animaciones para los NPC
    private final Animation[] npcArriba;
    private final Animation[] npcDerecha;
    private final Animation[] npcAbajo;
    private final Animation[] npcIzquierda;
    //Numero de NPC	que van a aparecer en el juego
    private final int numeroNPC = 5;
    //Posiciones del personaje, NPC, punto de origen y destino.
    private final Vector2 posicionPersonaje;
    private final Vector2 posicionReinicio;
    //Posiciones de los NPC
    private final Vector2[] posicionNPC;
    //Posiciones iniciales
    private final Vector2[] origen;
    //Posiciones finales
    private final Vector2[] destino;
    //Tamaño del mapa de baldosas.
    private final int anchoMapa;
    private final int altoMapa;
    //Atributos que indican la anchura y la altura de un tile del mapa de baldosas
    private final int anchoCelda;
    private final int altoCelda;
    //Velocidad de desplazamiento del personaje por cada pulsacion
    private final float velocidadPersonaje;
    //Velocidad de desplazamiento de los NPC
    private final float velocidadNPC;
    //Numero de vidas del personaje
    private int nVidas;
    //Elementos para sobreimpresionar informacion
    private BitmapFont fontVidas;
    private BitmapFont fontGemas;
    private BitmapFont fontPausa;
    //Elementos para la musica
    private Music musicaJuego;
    //Elementos para los sonidos
    private Sound pasos;
    private int cycle, cycle_ant;
    private Sound encontrada;
    private Sound pillado;
    private Sound fracaso;
    private Sound exito;
    //Booleano de fin de jugo
    private boolean juegoAcabado;


    //Método constructor de la clase
    public JuegoScreen(SieteGemas juego){
        this.juego = juego;

        //Creamos una cámara y la vinculamos con el lienzo del juego.
        //El juego se muestre con la misma relación de aspecto que la pantalla del dispositivo.
        int anchoCamara = 400, altoCamara = (int) (anchoCamara  * UI.getAlturaCamara() / UI.getAnchuraPantalla());
        juego.camera.setToOrtho(false,anchoCamara, altoCamara);

        //Vinculamos los eventos de entrada a esta clase.
        juego.camera.update();

        //Ponemos a cero el atributo stateTime, que marca el tiempo e ejecución de todas las animaciones
        stateTime = 0f;
        stateTimeMovement = 0f;

        //Escenario para el menu inicial
        //stage = new Stage();

        //Cargamos la imagen del personaje principal en el objeto img de la clase Texture
        PC = new Texture(Gdx.files.internal("sprites/personajes/pc.png"));

        //Sacamos los frames de img en un array de TextureRegion
        //Atributo que permite dibujar imágenes 2D, en este caso el sprite.
        //private SpriteBatch sb;
        //Constantes que indican el numero de filas y columnas de la hoja de sprites
        int PERSONAJE_COLS = 3;
        int PERSONAJE_ROWS = 4;
        TextureRegion[][] tmp = TextureRegion.split(PC,
                PC.getWidth() / PERSONAJE_COLS,
                PC.getHeight() / PERSONAJE_ROWS);


        //Creamos el objeto SpriteBatch que nos permitira representarlo adecuadamente en el render()
        //sb = new SpriteBatch();

        //Cargamos el mapa de baldosas desde la carpeta de assets
        mapa = new TmxMapLoader().load("map/GemasMap.tmx");
        mapaRenderer = new OrthogonalTiledMapRenderer(mapa);

        //Determinamos el alto y ancho del mapa de baldosas. Para ello necesitamos extraer la capa
        //base del mapa y, a partir de ella, determinamos el número de celdas a lo ancho y alto,
        //así como el tamaño de la celda, que multiplicando por el número de celdas a lo alto y
        //ancho, da como resultado el alto y ancho en pixeles del mapa.
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(0);
        anchoCelda = (int) capa.getTileWidth();
        altoCelda = (int) capa.getTileHeight();

        anchoMapa = capa.getWidth() * anchoCelda;
        altoMapa = capa.getHeight() * altoCelda;

        //Cargamos las capas de los obstáculos y las de los pasos en el TiledMap.
        TiledMapTileLayer capaSuelo = (TiledMapTileLayer) mapa.getLayers().get(0);
        TiledMapTileLayer capaObstaculos1 = (TiledMapTileLayer) mapa.getLayers().get(1);
        TiledMapTileLayer capaObstaculos2 = (TiledMapTileLayer) mapa.getLayers().get(2);
        TiledMapTileLayer capaPasos = (TiledMapTileLayer) mapa.getLayers().get(3);
        TiledMapTileLayer capaGemas = (TiledMapTileLayer) mapa.getLayers().get(4);
        TiledMapTileLayer capaProfundidad = (TiledMapTileLayer) mapa.getLayers().get(5);
        TiledMapTileLayer capaCheckPoints = (TiledMapTileLayer) mapa.getLayers().get(6);

        //El numero de tiles es igual en todas las capas. Lo tomamos de la capa Suelo
        anchoTiles = capaSuelo.getWidth();
        altoTiles = capaSuelo.getHeight();

        //Creamos el array y los objetos Gema
        gema = new Gema[numeroGemas];
        for(int i=0; i<numeroGemas; i++){
            gema[i] = new Gema();
            gema[i].setIndex(i);
        }

        //Creamos una matriz de booleanos para obstaculos y chekpoints
        obstaculo = new boolean[anchoTiles][altoTiles];
        checkPoint = new boolean[anchoTiles][altoTiles];

        int g= 0;
        //Rellenamos los valores de obstáculos, checkpoints y gemas, recorriendo el mapa
        for (int x = 0; x < anchoTiles; x++) {
            for (int y = 0; y < altoTiles; y++) {
                //creamos matriz de obstaculos
                obstaculo[x][y] = (((capaObstaculos1.getCell(x, y) != null) //obstaculos de la capa Obstaculos1
                        || (capaObstaculos2.getCell(x, y) != null)) //obstaculos de la capa Obstaculos2
                        && (capaPasos.getCell(x, y) == null) //que no sean pasos de la capa Pasos
                        && (capaProfundidad.getCell(x, y) == null));  //o de la capa Profundidad
                //creamos matriz de checkpoints
                checkPoint[x][y] = (capaCheckPoints.getCell(x, y) != null);
                //Establecemos la posición de las gemas encontradas
                if(capaGemas.getCell(x,y)!=null){
                    gema[g++].setPosicion(baldosaAPosicion(x,altoTiles-1-y,
                            anchoCelda/2 - 8,
                            altoCelda/2 - 8));
                }
            }
        }

        //Tile Inicial y Final
        Vector2 celdaInicial = new Vector2(0, 0);
        celdaFinal = new Vector2(24, 1);

        //Creamos las distintas animaciones, teniendo en cuenta que el tiempo de muestra sera
        //150 milisegundos, y que les pasamos las distintas filas de la matriz
        float framePersonaje = 0.15f;
        personajeAbajo = new Animation<>(framePersonaje, tmp[0]);
        personajeAbajo.setPlayMode(Animation.PlayMode.LOOP);
        personajeIzda = new Animation<>(framePersonaje, tmp[1]);
        personajeIzda.setPlayMode(Animation.PlayMode.LOOP);
        personajeDrcha = new Animation<>(framePersonaje, tmp[2]);
        personajeDrcha.setPlayMode(Animation.PlayMode.LOOP);
        personajeArriba = new Animation<>(framePersonaje, tmp[3]);
        personajeArriba.setPlayMode(Animation.PlayMode.LOOP);
        //En principio se utliza la animacion del personaje abajo por defecto
        personaje = personajeAbajo;
        //Dimensiones de cualquier personaje
        anchoPersonaje = tmp[0][0].getRegionWidth();
        altoPersonaje = tmp[0][0].getRegionHeight();
        //Creamos arrays para los NPC
        npc = new Animation[numeroNPC];
        npcAbajo = new Animation[numeroNPC];
        npcIzquierda = new Animation[numeroNPC];
        npcDerecha = new Animation[numeroNPC];
        npcArriba = new Animation[numeroNPC];
        posicionNPC = new Vector2[numeroNPC];
        origen = new Vector2[numeroNPC];
        destino = new Vector2[numeroNPC];
        //Creamos las animaciones posicionales de los NPC
        imgNPC = new Texture[numeroNPC];
        imgNPC[0] = new Texture(Gdx.files.internal("sprites/personajes/npc1.png"));
        imgNPC[1] = new Texture(Gdx.files.internal("sprites/personajes/npc2.png"));
        imgNPC[2] = new Texture(Gdx.files.internal("sprites/personajes/npc3.png"));
        imgNPC[3] = new Texture(Gdx.files.internal("sprites/personajes/npc4.png"));
        imgNPC[4] = new Texture(Gdx.files.internal("sprites/personajes/npc5.png"));
        for (int i = 0; i < numeroNPC; i++) {
            //Sacamos los frames de img en un array de TextureRegion
            tmp = TextureRegion.split(imgNPC[i],
                    imgNPC[i].getWidth() / PERSONAJE_COLS,
                    imgNPC[i].getHeight() / PERSONAJE_ROWS);
            //Creamos las distintas animaciones, teniendo en cuenta el timepo de muestra
            float frameNPC = 0.15f;
            npcAbajo[i] = new Animation<>(frameNPC, tmp[0]);
            npcAbajo[i].setPlayMode(Animation.PlayMode.LOOP);
            npcIzquierda[i] = new Animation<>(frameNPC, tmp[1]);
            npcIzquierda[i].setPlayMode(Animation.PlayMode.LOOP);
            npcDerecha[i] = new Animation<>(frameNPC, tmp[2]);
            npcDerecha[i].setPlayMode(Animation.PlayMode.LOOP);
            npcArriba[i] = new Animation<>(frameNPC, tmp[3]);
            npcArriba[i].setPlayMode(Animation.PlayMode.LOOP);
            npc[i] = npcAbajo[i];
        }
        //Establecemos los atributos de Textures y Color de las Gemas
        gema[0].setImgGema(new Texture(Gdx.files.internal("sprites/gemas/gem-16-red.png")));
        gema[0].setColor("red");
        gema[1].setImgGema(new Texture(Gdx.files.internal("sprites/gemas/gem-16-ambar.png")));
        gema[1].setColor("ambar");
        gema[2].setImgGema(new Texture(Gdx.files.internal("sprites/gemas/gem-16-marine.png")));
        gema[2].setColor("marine");
        gema[3].setImgGema(new Texture(Gdx.files.internal("sprites/gemas/gem-16-green.png")));
        gema[3].setColor("green");
        gema[4].setImgGema(new Texture(Gdx.files.internal("sprites/gemas/gem-16-violet.png")));
        gema[4].setColor("violet");
        gema[5].setImgGema(new Texture(Gdx.files.internal("sprites/gemas/gem-16-purple.png")));
        gema[5].setColor("purple");
        gema[6].setImgGema(new Texture(Gdx.files.internal("sprites/gemas/gem-16-yellow.png")));
        gema[6].setColor("yellow");
        for (int i = 0; i < numeroGemas; i++){
            int GEM_COLS = 8;
            int GEM_ROWS = 1;
            tmp = TextureRegion.split(gema[i].getImgGema(), gema[i].getImgGema().getWidth() / GEM_COLS,
                    gema[i].getImgGema().getHeight() / GEM_ROWS);
            //Creamos las distintas animaciones, teniendo en cuenta el tiempo de muestra
            float frameGema = 0.15f;
            gema[i].setAnimationGema(new Animation<>(frameGema,tmp[0]));
            gema[i].getAnimationGema().setPlayMode(Animation.PlayMode.LOOP);
        }
        //RECORRIDO DE LOS NPC. Indicamos las baldosas de inicio y fin de su recorrido y  usamos
        //la funcion posicionaMapa para traducirlo a puntos del mapa.
        origen[0] = baldosaAPosicion(3, 2, 0 ,0);
        destino[0] = baldosaAPosicion(3, 5, 0 ,0);
        origen[1] = baldosaAPosicion(12, 10, 0 ,0);
        destino[1] = baldosaAPosicion(14, 10, 0 ,0);
        origen[2] = baldosaAPosicion(17, 3, 0 ,0);
        destino[2] = baldosaAPosicion(15, 3, 0 ,0);
        origen[3] = baldosaAPosicion(18, 10, 0 ,0);
        destino[3] = baldosaAPosicion(21, 10, 0 ,0);
        origen[4] = baldosaAPosicion(23, 8, 0 ,0);
        destino[4] = baldosaAPosicion(23, 5, 0 ,0);
        //Inicializamos la musica de fondo del juego
        musicaJuego = Gdx.audio.newMusic(Gdx.files.internal("music/poor_danielle_by_matrixofquartz.mp3"));
        musicaJuego.setLooping(true);
        //Inicializamos los sonidos
        encontrada = Gdx.audio.newSound(Gdx.files.internal("music/tesoro_by_funwithsound.mp3"));
        pillado = Gdx.audio.newSound(Gdx.files.internal("music/lose_music.mp3"));
        pasos = Gdx.audio.newSound(Gdx.files.internal("music/paso_060.mp3"));
        cycle = 0;
        cycle_ant = -1;//Sirven para controlar los ciclos de reproduccion del sonido pasos.
        fracaso = Gdx.audio.newSound(Gdx.files.internal("music/fin.mp3"));
        exito = Gdx.audio.newSound(Gdx.files.internal("music/exito.mp3"));
        /////// SITUACION INICIAL DEL JUEGO /////
        //Posicion inicial de los NPC
        for (int i = 0; i < numeroNPC; i++) {
            posicionNPC[i] = new Vector2();
            posicionNPC[i].set(origen[i]);
        }
        //velocidad del personaje
        velocidadPersonaje = 0.75f;
        //velocidad de los personajes
        velocidadNPC = 0.5f;
        //Numero de vidas
        nVidas = 3;
        //Empezamos un juego nuevo
        //juegoAcabado = false;
        //Textos sobreimpresionados
        fontVidas = new BitmapFont(Gdx.files.internal("skin/default/default.fnt"));
        fontGemas = new BitmapFont(Gdx.files.internal("skin/default/default.fnt"));
        fontPausa = new BitmapFont(Gdx.files.internal("skin/default/default.fnt"));
        //Posicion inicial y de reinicio del personaje
        posicionPersonaje = new Vector2(baldosaAPosicion((int)celdaInicial.x, (int)celdaInicial.y, 0, 0));
        posicionReinicio = new Vector2(baldosaAPosicion((int)celdaInicial.x, (int)celdaInicial.y, 0, 0));
    }

    ///METODOS DE LA INTERFAZ STAGE
    @Override
    public void render(float delta) {

        //ponemos a la escucha de eventos la propia clase JuegoScreen
        Gdx.input.setInputProcessor(this);

        //Centramos la camara en el personaje principal
        juego.camera.position.set(posicionPersonaje, 0);

        //Comprobamos que la cámara no se salga de los límites del mapa de baldosas,
        juego.camera.position.x = MathUtils.clamp(juego.camera.position.x,
                juego.camera.viewportWidth / 2f,
                anchoMapa - juego.camera.viewportWidth / 2f);
        //Acotamos, con el método clamp(), la posición de la cámara
        juego.camera.position.y = MathUtils.clamp(juego.camera.position.y,
                juego.camera.viewportHeight / 2f,
                altoMapa - juego.camera.viewportHeight / 2f);

        //Actualizamos la cámara del juego
        juego.camera.update();
        //Vinculamos el objeto que dibuja el mapa con la cámara del juego
        mapaRenderer.setView(juego.camera);

        //Dibujamos las tres primeras capas del TiledMap
        int[] capas = {0, 1, 2, 3};
        mapaRenderer.render(capas);
        stateTimeMovement += Gdx.graphics.getDeltaTime();

        //Actualizamos la posición del personaje para el nuevo delta y comprobamos posibles eventos
        actualizaPosicionPersonaje();
        // le indicamos al SpriteBatch que se muestre en el sistema de coordenadas específicas de la cámara.
        juego.batch.setProjectionMatrix(juego.camera.combined);
        //Inicializamos el objeto SpriteBatch
        juego.batch.begin();

        //Pintamos la animacion del personaje principal
        //Contendrá el frame que se va a mostrar en cada momento.
        TextureRegion cuadroActual = personaje.getKeyFrame(stateTime);
        juego.batch.draw(cuadroActual, posicionPersonaje.x, posicionPersonaje.y);
        //Deteccion de colisiones con NPC
        detectaColisiones();

        //Dibujamos las animaciones de los NPC
        for (int i = 0; i < numeroNPC; i++) {
            actualizaPosicionNPC(i);
            cuadroActual = (TextureRegion) npc[i].getKeyFrame(stateTimeMovement);
            juego.batch.draw(cuadroActual, posicionNPC[i].x, posicionNPC[i].y);
        }

        //Dibujamos las gemas en el plano
        for (int i = 0; i < numeroGemas; i++){
            if (!gema[i].isEncontrada()){
                cuadroActual = (TextureRegion) gema[i].getAnimationGema().getKeyFrame(stateTimeMovement);
                juego.batch.draw(cuadroActual, gema[i].getPosicion().x, gema[i].getPosicion().y);
            }
        }

        //Finalizamos el objeto SpriteBatch
        juego.batch.end();
        //Pintamos la cuarta capa del mapa de baldosas.
        capas = new int[1];
        capas[0] = 5;
        mapaRenderer.render(capas);

        String infoGemas = "Gemas: " + cuentaGemas;
        String infoVidas = "Vidas: " + nVidas;
        juego.batch.begin();
        fontGemas.draw(juego.batch, infoGemas, juego.camera.position.x - juego.camera.viewportWidth / 2, juego.camera.position.y - juego.camera.viewportHeight / 2 + 60);
        fontVidas.draw(juego.batch, infoVidas, juego.camera.position.x - juego.camera.viewportWidth / 2, juego.camera.position.y - juego.camera.viewportHeight / 2 + 30);
        juego.batch.end();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        //Music
        musicaJuego.dispose();
        //Sound
        encontrada.dispose();
        pillado.dispose();
        pasos.dispose();
        exito.dispose();
        fracaso.dispose();
        //Texture
        PC.dispose();
        for(int i=0; i<numeroNPC; i++) imgNPC[i].dispose();
        for(int i=0; i<numeroGemas; i++) gema[i].getImgGema().dispose();
        //BitMapFont
        fontVidas.dispose();
        fontGemas.dispose();
        fontPausa.dispose();
        //TiledMap
        mapa.dispose();
    }
    ///////////////////// METODOS DE LA INTERFAZ InputProcessor ////////////////////////////////////
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                setIzquierda(true);
                break;
            case Input.Keys.RIGHT:
                setDerecha(true);
                break;
            case Input.Keys.UP:
                setArriba(true);
                break;
            case Input.Keys.DOWN:
                setAbajo(true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.LEFT:
                setIzquierda(false);
                break;
            case Input.Keys.RIGHT:
                setDerecha(false);
                break;
            case Input.Keys.UP:
                setArriba(false);
                break;
            case Input.Keys.DOWN:
                setAbajo(false);
                break;
        }

        //Para ocultar/mostrar las distintas capas pulsamos desde el 1 en adelante...
        int codigoCapa = keycode - Input.Keys.NUM_1;
        if (codigoCapa < 6)
            mapa.getLayers().get(codigoCapa).setVisible(!mapa.getLayers().get(codigoCapa).isVisible());

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchDragged(screenX,screenY,pointer);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        setArriba(false);
        setAbajo(false);
        setIzquierda(false);
        setDerecha(false);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0f);
        //Transformamos las coordenadas del vector a coordenadas de nuestra camara
        Vector3 pulsacion3d = juego.camera.unproject(clickCoordinates);
        Vector2 pulsacion = new Vector2(pulsacion3d.x, pulsacion3d.y);

        //Calculamos la diferencia entre la pulsacion y el centro del personaje
        Vector2 centroPersonaje = new Vector2(posicionPersonaje).add((float) anchoPersonaje / 2, (float) altoPersonaje / 2);
        Vector2 diferencia = new Vector2(pulsacion.sub(centroPersonaje));

        //Vamos a determinar la intencion del usuario para mover al personaje en funcion del
        //angulo entre la pulsacion y la posicion del personaje
        float angulo = diferencia.angle();

        if (angulo > 30 && angulo <= 150) setArriba(true);
        if (angulo > 120 && angulo <= 240) setIzquierda(true);
        if (angulo > 210 && angulo <= 330) setAbajo(true);
        if ((angulo > 0 && angulo <= 60) || (angulo > 300 && angulo < 360)) setDerecha(true);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
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

    //////////////////////////////////// METODOS PROPIOS ///////////////////////////////////////////

    // Método que permite transformar la posicion de una baldosa (tile) en coordenadas del plano
    // con un offset para situarlo en la celda
    protected Vector2 baldosaAPosicion(int columnaBaldosa, int filaBaldosa, int xOffset, int yOffset) {
        Vector2 res = new Vector2();
        if (columnaBaldosa + 1 > anchoTiles ||
                filaBaldosa + 1 > altoTiles) {  //Si la peticion esta mal, situamos en el origen del mapa
            res.set(0, 0);
            System.err.println("Posicion inválida de baldosa");
        }
        res.x = (float) columnaBaldosa * anchoCelda + xOffset;
        res.y = (float) (altoTiles - 1 - filaBaldosa) * altoCelda + yOffset;
        return res;
    }

    // Método que permite transformar las coordenadas del plano en una baldosa (tile)
    protected Vector2 posicionABaldosa(Vector2 posicion){
        Vector2 res = new Vector2();
        if (posicion.x > anchoMapa || posicion.y > altoMapa){
            res.set(0,0);
            System.err.println("Posicion inválida en el mapa");
        }
        res.x = (int)(posicion.x / anchoCelda);
        res.y = altoTiles - 1 - (int) (posicion.y / altoCelda);

        return res;
    }

    //Metodo que permite actualizar la posicion del personaje
    protected void actualizaPosicionPersonaje() {

        //Guardamos la posicion del personaje por si encontramos algun obstaculo
        Vector2 posicionAnterior = new Vector2();
        posicionAnterior.set(posicionPersonaje);

        if (izquierda) {
            posicionPersonaje.x -= velocidadPersonaje;
            personaje = personajeIzda;
        }
        if (derecha) {
            posicionPersonaje.x += velocidadPersonaje;
            personaje = personajeDrcha;
        }
        if (arriba) {
            posicionPersonaje.y += velocidadPersonaje;
            personaje = personajeArriba;
        }
        if (abajo) {
            posicionPersonaje.y -= velocidadPersonaje;
            personaje = personajeAbajo;
        }

        //Control del sonido de los pasos del personaje
        if (izquierda || derecha || arriba || abajo) {
            stateTime += Gdx.graphics.getDeltaTime();
            cycle = (int) (stateTime / 0.3f); //El sonido de los pasos dura 0.3 segundos
            //Si estamos en un nuevo ciclo de 0.3 segundos, reproducimos de nuevo los pasos
            if (cycle != cycle_ant)
                pasos.play(0.5f);
            cycle_ant = cycle;
        }

        //Limites en el mapa para el personaje
        posicionPersonaje.x = MathUtils.clamp(posicionPersonaje.x, 0, anchoMapa - anchoPersonaje);
        posicionPersonaje.y = MathUtils.clamp(posicionPersonaje.y, 0, altoMapa - altoPersonaje);

        //Calculamos las celdas bajo los límites del personaje
        int limIzq = (int) ((posicionPersonaje.x + 0.25 * anchoPersonaje) / anchoCelda);
        int limDrcha = (int) ((posicionPersonaje.x + 0.75 * anchoPersonaje) / anchoCelda);
        int limSup = (int) ((posicionPersonaje.y + 0.25 * altoPersonaje) / altoCelda);
        int limInf = (int) ((posicionPersonaje.y) / altoCelda);

        if (obstaculo[limIzq][limInf] || obstaculo[limDrcha][limSup])
            posicionPersonaje.set(posicionAnterior);

        //Deteccion de Checkpoints
        if (checkPoint[limIzq][limInf]) {
            encontrada.play();
            posicionReinicio.set(posicionPersonaje);
            checkPoint[limIzq][limInf] = false;
        }else if(checkPoint[limDrcha][limSup]){
            encontrada.play();
            posicionReinicio.set(posicionPersonaje);
            checkPoint[limDrcha][limSup] = false;
        }

        for (int i=0; i<numeroGemas; i++){
            if(!gema[i].isEncontrada()){
                if(gema[i].getPosicion().epsilonEquals(posicionPersonaje, anchoCelda/2)){
                    System.err.println("Gema "+ gema[i].getColor() + " encontrada.");
                    gema[i].setEncontrada(true);
                    encontrada.play();
                    cuentaGemas++;
                }
            }
        }

        //Deteccion de fin del mapa
        if (posicionABaldosa(posicionPersonaje).epsilonEquals(celdaFinal)) {
            exito.play();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Activamos el booleano que sirve para presentar los resultados en el Menu
            juegoAcabado = true;
            //setGameState(State.ENTRADA);
        }
    }

    //Método que permite cambiar las coordenadas del NPC en la posición "i",
    //dada una variación "delta" en ambas coordenadas.
    private void actualizaPosicionNPC(int i) {

        //vector diferencia marca el vector posicion relativa del click con respecto al personaje
        Vector2 diferencia = new Vector2();
        diferencia.set(destino[i]);
        diferencia.sub(posicionNPC[i]);

        if (posicionNPC[i].y < destino[i].y) {
            posicionNPC[i].y += velocidadNPC;
            npc[i] = npcArriba[i];
        }
        if (posicionNPC[i].y > destino[i].y) {
            posicionNPC[i].y -= velocidadNPC;
            npc[i] = npcAbajo[i];
        }
        if (posicionNPC[i].x < destino[i].x) {
            posicionNPC[i].x += velocidadNPC;
            npc[i] = npcDerecha[i];
        }
        if (posicionNPC[i].x > destino[i].x) {
            posicionNPC[i].x -= velocidadNPC;
            npc[i] = npcIzquierda[i];
        }

        posicionNPC[i].x = MathUtils.clamp(posicionNPC[i].x, 0, anchoMapa - anchoPersonaje);
        posicionNPC[i].y = MathUtils.clamp(posicionNPC[i].y, 0, altoMapa - altoPersonaje);

        //Dar la vuelta al NPC cuando llega a un extremo
        if (posicionNPC[i].epsilonEquals(destino[i])) {
            destino[i].set(origen[i]);
            origen[i].set(posicionNPC[i]);
        }
    }

    //Método que detecta si se producen colisiones usando rectángulos
    private void detectaColisiones() {
        //Vamos a comprobar que el rectángulo que rodea al personaje, no se solape
        //con el rectángulo de alguno de los NPC. Primero calculamos el rectángulo
        //entorno al personaje restandole un 10% en cada lado.
        Rectangle rPersonaje = new Rectangle((float) (posicionPersonaje.x + 0.25 * anchoPersonaje), (float) (posicionPersonaje.y + 0.25 * altoPersonaje),
                (float) (0.5 * anchoPersonaje), (float) (0.5 * altoPersonaje));
        Rectangle rNPC;
        //Ahora recorremos el array de NPC, para cada uno generamos su rectángulo envolvente
        //y comprobamos si se solapa o no con el del personaje.
        for (int i = 0; i < numeroNPC; i++) {
            rNPC = new Rectangle((float) (posicionNPC[i].x + 0.1 * anchoPersonaje), (float) (posicionNPC[i].y + 0.1 * altoPersonaje),
                    (float) (0.8 * anchoPersonaje), (float) (0.8 * altoPersonaje));
            //Si hay colision
            if (rPersonaje.overlaps(rNPC)) {
                //pausamos la musica
                float posicionMusica = 0f;
                if (musicaJuego.isPlaying())
                    posicionMusica = musicaJuego.getPosition();
                musicaJuego.pause();
                //reproducimos el sonido "pillado"
                pillado.play();
                //Hacemos vibrar el telefono
                Gdx.input.vibrate(100);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Descontamos una vida y si siguen quedando vidas continuamos
                if (--nVidas > 0) {
                    //Posicionamos el personaje en la posicion del ultimo checkpoint pasado.
                    posicionPersonaje.set(posicionReinicio);
                    musicaJuego.setPosition(posicionMusica);
                    //reanudamos la musica del juego
                    if(musicaJuego.isPlaying())
                        musicaJuego.play();
                } else {  //Si no quedan vidas, el juego acaba con derrota
                    musicaJuego.stop();
                    fracaso.play();
                    //Hacemos vibrar el telefono mas tiempo
                    Gdx.input.vibrate(300);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Activamos el booleano que sirve para presentar los resultados en el Menu
                    juegoAcabado = true;
                    //Volvemos al menu
                    //setGameState(State.ENTRADA);
                }
                return;
            }
        }

    }

    //Con estos setters se impide la situacion de direcciones contradictorias pero no las
    //direcciones compuestas que permiten movimientos diagonales

    private void setIzquierda(boolean izq) {
        if (derecha && izq) derecha = false;
        izquierda = izq;
    }

    private void setDerecha(boolean der) {
        if (izquierda && der) izquierda = false;
        derecha = der;
    }

    private void setArriba(boolean arr) {
        if (abajo && arr) abajo = false;
        arriba = arr;
    }

    private void setAbajo(boolean abj) {
        if (arriba && abj) arriba = false;
        abajo = abj;
    }


}
