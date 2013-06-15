package main;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

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

	// shaders
	private int shaderProgram;
	private int vertexShader;
	private int fragmentShader;

	// triangle vbo
	private int vboVertexHandle;
	private int vboColorHandle;
	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;

	private final int numberOfVertices = 3;
	private final int vertexSize = 2;
	private final int colorSize = 3;

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

		// switch back to the model view matrix
		glMatrixMode(GL_MODELVIEW);

		initializeShaders();
	}

	private void initializeShaders() {
		shaderProgram = glCreateProgram();
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

		StringBuilder vertexShaderSource = loadShaderSourceFromPath("src/shader/shader.vertex");
		StringBuilder fragmentShaderSource = loadShaderSourceFromPath("src/shader/shader.fragment");

		compileShader(vertexShader, vertexShaderSource);
		compileShader(fragmentShader, fragmentShaderSource);

		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);

		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
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

	private void compileShader(int shaderHandle, StringBuilder shaderSource) {
		glShaderSource(shaderHandle, shaderSource);
		glCompileShader(shaderHandle);
		if (glGetShaderi(shaderHandle, GL_COMPILE_STATUS) == GL_FALSE)
			System.out.println("Not able to compile shader " + shaderHandle + "\n" + "With source: " + shaderSource);
	}

	private void initializeVariables() {
		float[] vertexData = new float[] {
			-0.5f, -0.5f,
			0.5f, -0.5f,
			0.0f, 0.5f
		};

		float[] colorData = new float[] {
			1, 0, 0,
			0, 1, 0,
			0, 0, 1
		};

		vertexBuffer = createFloatBuffer(vertexData);
		colorBuffer = createFloatBuffer(colorData);

		vboVertexHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

		vboColorHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	private void programLoop() {
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			glUseProgram(shaderProgram);
			renderGL();
			update();

			glUseProgram(0);
			Display.update();
			Display.sync(TARGET_FRAME_RATE);
		}
	}

	private void renderGL() {
		// clear both buffers
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glVertexPointer(vertexSize, GL_FLOAT, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glColorPointer(colorSize, GL_FLOAT, 0, 0L);

		glDrawArrays(GL_TRIANGLES, 0, numberOfVertices);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
	}

	private void update() {
		// TODO: update
	}

	private void exitProgram() {
		destroyBuffers();
		destroyShaders();
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

	private void destroyShaders() {
		glDeleteProgram(shaderProgram);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
	}

	private FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
		floatBuffer.put(data);
		floatBuffer.flip();
		return floatBuffer;
	}

	public static void main(String[] args) {
		new Main();
	}

}
