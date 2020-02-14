package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import static sun.misc.Version.println;

public class MainGame extends ApplicationAdapter {
	SpriteBatch batch;
	TiledMapRenderer tiledMapRenderer;
	OrthographicCamera camera;
	Sprite playerSprite;
	TiledMap tiledMap;

	int TILE_WIDTH = 32;
	int TILE_HEIGHT = 32;

	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		//Always shows the area of our world.
		camera.setToOrtho(false, 800, 400);

		tiledMap = new TmxMapLoader().load("MyMap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		playerSprite = new Sprite(new Texture("badlogic.jpg"));
		playerSprite.setSize(64, 64);
	}

	@Override
	public void render () {
		checkCollisions();

		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();
		playerSprite.draw(batch);
		batch.end();

		movePlayer(Gdx.graphics.getDeltaTime());

		moveCamera();
	}

	public void movePlayer(float delta) {
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			playerSprite.translateX(3f);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			playerSprite.translateY(3f);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			playerSprite.translateY(-3f);
		}
	}

	private void checkCollisions() {
		MapLayer collisionObjectLayer =
				(MapLayer)tiledMap.getLayers().get("collectibles-rectangles");
		MapObjects mapObjects = collisionObjectLayer.getObjects();
		Array<RectangleMapObject> rectangleObjects =
				mapObjects.getByType(RectangleMapObject.class);
		for (RectangleMapObject rectangleMapObject : rectangleObjects) {
			Rectangle rectangle = rectangleMapObject.getRectangle();
			if (playerSprite.getBoundingRectangle().overlaps(rectangle)) {
				System.out.println("Crash");
			}
		}
	}

	public void moveCamera() {
		//camera.translate(1, 0);
		//camera.position.x = 200;
		camera.position.x = playerSprite.getX();
		camera.position.y = playerSprite.getY();
		camera.update();
	}

	@Override
	public void dispose () {

	}
}
