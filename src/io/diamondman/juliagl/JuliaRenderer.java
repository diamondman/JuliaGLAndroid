package io.diamondman.juliagl;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class JuliaRenderer extends GLRenderer {
	int vertshad;
	int fragshad;
	int progid;
	int uMVPMatrix_id;
	int utime_id;
	int drawPlane_vbo;
	int posAttrib;
	
	float mattransformVertices[] = new float[16];
	FloatBuffer fbmattransform;

	@Override
	public void onCreate(int width, int height, boolean contextLost) {
		progid = GLES20.glCreateProgram();
		if (progid == 0) {
			Log.e("JuliaGL", "Error creating GL Program object.\n");
			throw new RuntimeException("FUCK");
		}
		
		float vVertices[] = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };
		FloatBuffer fbvertices = JuliaRenderer.fa2fb(vVertices);
		
		drawPlane_vbo = genSingleBuffer();
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, drawPlane_vbo);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vVertices.length*4, fbvertices, GLES20.GL_STATIC_DRAW);

		vertshad = createShaderFromResource(R.raw.juliavert, GLES20.GL_VERTEX_SHADER);
		fragshad = createShaderFromResource(R.raw.juliafrag, GLES20.GL_FRAGMENT_SHADER);
		GLES20.glAttachShader(progid, vertshad);
		GLES20.glAttachShader(progid, fragshad);
		GLES20.glLinkProgram(progid);

		checkLinkerrors();
		GLES20.glUseProgram(progid);
		
		posAttrib = GLES20.glGetAttribLocation(progid, "vPosition");
		GLES20.glEnableVertexAttribArray(posAttrib);
		GLES20.glVertexAttribPointer(posAttrib, 2, GLES20.GL_FLOAT, false, 0, 0);

		calculateViewMatrix(mattransformVertices);
		fbmattransform = JuliaRenderer.fa2fb(mattransformVertices);
		uMVPMatrix_id = GLES20.glGetUniformLocation(progid, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(uMVPMatrix_id, 1, false, fbmattransform);

		utime_id = GLES20.glGetUniformLocation(progid, "utime");		
	}

	@Override
	public void onDrawFrame(boolean firstDraw) {
		try {
			GLES20.glUseProgram(progid);

			GLES20.glUniform1f(utime_id, ((float)(System.currentTimeMillis()-baseTime))/1000f);

			GLES20.glClearColor(0f, .5f, 0.5f, 1f);		
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		} catch (Exception e) {
			Log.e("JuliaGL", "DERP!!");
		}
	}
	
	private int genSingleBuffer(){
		int[] vbos = new int[1];
		GLES20.glGenBuffers(1, vbos, 0);
		return vbos[0];
	}
	
 	private void calculateViewMatrix(float[] m) {
		//Matrix.perspectiveM(mattransformVertices2, 0, 45, ((float)mWidth)/((float)mHeight), 1, 10);
		Matrix.setIdentityM(m, 0);
		if(mWidth>mHeight){
			m[0] = ((float)mHeight)/((float)mWidth);
			m[5] = 1f;
		}else{
			m[0] = 1;
			m[5] = ((float)mWidth)/((float)mHeight);
		}
	}
	
	private void checkLinkerrors() {
		int status[] = new int[1];
		GLES20.glGetProgramiv(progid, GLES20.GL_LINK_STATUS,
				IntBuffer.wrap(status));

		if (status[0] == GLES20.GL_FALSE) {
			Log.e("JuliaGL", "FUCK IT ALL!!!!****\n\n");

			String logMessage = GLES20.glGetProgramInfoLog(progid);
			Log.e("JuliaGL", "ERROR: " + logMessage);
			GLES20.glDeleteProgram(progid);
			throw new RuntimeException();
		};
	}
	
	public static FloatBuffer fa2fb(float[] input){
		ByteBuffer bb = ByteBuffer.allocateDirect(input.length*4); 
		bb.order(ByteOrder.nativeOrder());    // use the device hardware's native byte order
		FloatBuffer fb = bb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		fb.put(input);    // add the coordinates to the FloatBuffer
		fb.position(0);
		return fb;
	}

	long baseTime = System.currentTimeMillis();
	
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
