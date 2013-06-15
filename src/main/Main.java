package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Main {

	// display constants
	private static final int    DISPLAY_WIDTH     = 1280;
	private static final int    DISPLAY_HEIGHT    = 720;
	private static final String DISPLAY_TITLE     = "Intro to Shaders";
	private static final int    TARGET_FRAME_RATE = 60;

	// perspective constants
	private static final float FIELD_OF_VIEW = 65.0f;
	private static final float NEAR_PLANE    = 0.001f;
	private static final float FAR_PLANE     = 100.0f;

	public Main() {
		initializeProgram();
		programLoop();
		exitProgram();
	}

	private void initializeProgram() {
		initializeDisplay();
		initializeGL();
		initializeVariables();
	}

	private void initializeDisplay() {
		// create the display
		try {
			Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
			Display.setTitle(DISPLAY_TITLE);
			Display.create();
		} catch(LWJGLException exception) {
			exception.printStackTrace();
			exitProgramWithErrorCode(1);
		}
	}

	private void initializeGL() {
		// edit the projection matrix
		glMatrixMode(GL_PROJECTION);
		// reset the projection matrix
		glLoadIdentity();
		gluPerspective(FIELD_OF_VIEW, (float) (DISPLAY_WIDTH / DISPLAY_HEIGHT), NEAR_PLANE, FAR_PLANE);

		// switch back to the model view matrix
		glMatrixMode(GL_MODELVIEW);

		initializeShaders();
	}

	private void initializeShaders() {
		int shaderProgram = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

		StringBuilder vertexShaderSource = loadShaderSourceFromPath("src/shader/shader.vertex");
		StringBuilder fragmentShaderSource = loadShaderSourceFromPath("src/shader/shader.fragment");
	}

	private StringBuilder loadShaderSourceFromPath(String path) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			exitProgramWithErrorCode(2);
		}
		return shaderSource;
	}

	private void initializeVariables() {
		// TODO: initialize program specific variables
	}

	private void programLoop() {
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			renderGL();
			update();

			Display.update();
			Display.sync(TARGET_FRAME_RATE);
		}
	}

	private void renderGL() {
		// clear both buffers
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// TODO: render
	}

	private void update() {
		// TODO: update
	}

	private void exitProgram() {
		destroyBuffers();
		Display.destroy();
		System.exit(0);
	}

	private void exitProgramWithErrorCode(int error) {
		System.err.println("Terminated with error code " + error);
		destroyBuffers();
		Display.destroy();
		System.exit(error);
	}

	private void destroyBuffers() {
		// TODO: destroy buffers
	}

	public static void main(String[] args) {
		new Main();
	}

}
