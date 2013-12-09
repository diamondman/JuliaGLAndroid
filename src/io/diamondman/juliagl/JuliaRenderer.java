package io.diamondman.juliagl;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.opengl.GLES20;
import android.util.Log;

public class JuliaRenderer extends GLRenderer {
	int vertshad;
	int fragshad;
	int progid;

	@Override
	public void onCreate(int width, int height, boolean contextLost) {
		progid = GLES20.glCreateProgram();
		if (progid == 0) {
			Log.e("JuliaGL", "Error creating GL Program object.\n");
			throw new RuntimeException("FUCK");
		}

		vertshad = createShaderFromResource(R.raw.juliavert,
				GLES20.GL_VERTEX_SHADER);
		fragshad = createShaderFromResource(R.raw.juliafrag,
				GLES20.GL_FRAGMENT_SHADER);

		GLES20.glAttachShader(progid, vertshad);
		GLES20.glAttachShader(progid, fragshad);

		GLES20.glBindAttribLocation(progid, 0, "vPosition");

		GLES20.glLinkProgram(progid);

		int status[] = new int[1];
		GLES20.glGetProgramiv(progid, GLES20.GL_LINK_STATUS,
				IntBuffer.wrap(status));

		if (status[0] == GLES20.GL_FALSE) {
			Log.e("JuliaGL", "FUCK IT ALL!!!!****\n\n");

			String logMessage = GLES20.glGetProgramInfoLog(progid);
			Log.e("JuliaGL", "ERROR: " + logMessage);
			GLES20.glDeleteProgram(progid);
			throw new RuntimeException();
		}

		GLES20.glClearColor(0f, .5f, 0.5f, 1f);
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vVertices.length * 4); 
		vbb.order(ByteOrder.nativeOrder());    // use the device hardware's native byte order
		fbvertices = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		fbvertices.put(vVertices);    // add the coordinates to the FloatBuffer
		fbvertices.position(0);
	}

	//float vVertices[] = { 0.0f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f };
	float vVertices[] = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };
	FloatBuffer fbvertices;

	@Override
	public void onDrawFrame(boolean firstDraw) {
		try {
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
					| GLES20.GL_DEPTH_BUFFER_BIT);
			GLES20.glUseProgram(progid);
			GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 0,
					fbvertices);
			GLES20.glEnableVertexAttribArray(0);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		} catch (Exception e) {
			Log.e("JuliaGL", "DERP!");
		}

	}

	public int createShaderFromResource(int res, int TYPE) {
		InputStream jvertinput = AppBase.getContext().getResources()
				.openRawResource(res);
		java.util.Scanner s = new java.util.Scanner(jvertinput)
				.useDelimiter("\\A");
		String vertsource = s.hasNext() ? s.next() : "";

		int shader = GLES20.glCreateShader(TYPE);
		GLES20.glShaderSource(shader, vertsource);
		GLES20.glCompileShader(shader);
		int statusVertexShader[] = new int[1];
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS,
				IntBuffer.wrap(statusVertexShader));

		if (statusVertexShader[0] == GLES20.GL_FALSE) {
			String logMessage = GLES20.glGetShaderInfoLog(shader);
			System.out.println("Vertex shader error:\n" + logMessage);
			GLES20.glDeleteShader(shader);
			throw new RuntimeException();
		}
		return shader;
	}

}
