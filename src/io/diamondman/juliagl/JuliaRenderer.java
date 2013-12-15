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
	private int vertshad;
	private int fragshad;
	private int progid;
	private int fbovertshad;
	private int fbofragshad;
	private int fboprogid;
	private int drawPlane_vbo;
	private int posAttrib;
	private int texcoordAttrib;
	private int fbo_tex1;
	private int fbo1;
	private int fboposAttrib;
	private int ufboMVPMatrix_id;
	private int ufboc_id;
	private int texcoord_vbo;
	
	long baseTime = System.currentTimeMillis();
	
	float mattransformVertices[] = new float[16];
	FloatBuffer fbmattransform;

	@Override
	public void onCreate(int width, int height, boolean contextLost) {	
		GLES20.glViewport(0, 0, width, height);
		calculateViewMatrix(mattransformVertices, width, height);
		fbmattransform = JuliaRenderer.fa2fb(mattransformVertices);
		
		float vVertices[] = { -4f, 2f, -4f, -2f, 4f, 2f, 4f, -2f };
		FloatBuffer fbvertices = JuliaRenderer.fa2fb(vVertices);
		drawPlane_vbo = genSingleBuffer();
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, drawPlane_vbo);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vVertices.length*4, fbvertices, GLES20.GL_STATIC_DRAW);

		float vtexVertices[] = { 
					-1f, 1f, 0f, 1f, 
					-1f, -1f, 0f, 0f, 
					1f, 1f, 1f, 1f, 
					1f, -1f, 1f, 0f };
		FloatBuffer fbtexVertices = JuliaRenderer.fa2fb(vtexVertices);
		texcoord_vbo = genSingleBuffer();
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, texcoord_vbo);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vtexVertices.length*4, fbtexVertices, GLES20.GL_STATIC_DRAW);
		
		
		int[] framebuffer_ids = new int[1];
		GLES20.glGenFramebuffers(1, framebuffer_ids, 0);
		fbo1 = framebuffer_ids[0];
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo1);
		
		int[] texture_ids = new int[1];
		GLES20.glGenTextures(1, texture_ids, 0);
		fbo_tex1 = texture_ids[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fbo_tex1);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, fbo_tex1, 0);


		fboprogid = GLES20.glCreateProgram();
		fbovertshad = createShaderFromResource(R.raw.juliafbovert, GLES20.GL_VERTEX_SHADER);
		fbofragshad = createShaderFromResource(R.raw.juliafbofrag, GLES20.GL_FRAGMENT_SHADER);
		GLES20.glAttachShader(fboprogid, fbovertshad);
		GLES20.glAttachShader(fboprogid, fbofragshad);
		GLES20.glLinkProgram(fboprogid);
		checkLinkerrors(fboprogid);

		fboposAttrib = GLES20.glGetAttribLocation(fboprogid, "vPosition");
		
		ufboMVPMatrix_id = GLES20.glGetUniformLocation(fboprogid, "uMVPMatrix");
		ufboc_id = GLES20.glGetUniformLocation(fboprogid, "c");
		
		

		progid = GLES20.glCreateProgram();
		vertshad = createShaderFromResource(R.raw.juliavert, GLES20.GL_VERTEX_SHADER);
		fragshad = createShaderFromResource(R.raw.juliafrag, GLES20.GL_FRAGMENT_SHADER);
		GLES20.glAttachShader(progid, vertshad);
		GLES20.glAttachShader(progid, fragshad);
		GLES20.glLinkProgram(progid);
		checkLinkerrors(progid);
		
		posAttrib = GLES20.glGetAttribLocation(progid, "vPosition");
		texcoordAttrib = GLES20.glGetAttribLocation(progid, "texcoord");
	}

	@Override
	public void onDrawFrame(boolean firstDraw) {
		try {
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo1);
			GLES20.glUseProgram(fboprogid);
			
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, drawPlane_vbo);
			GLES20.glEnableVertexAttribArray(fboposAttrib);
			GLES20.glVertexAttribPointer(fboposAttrib, 2, GLES20.GL_FLOAT, false, 0, 0);

			//float f = ((float)(System.currentTimeMillis()-baseTime))/100000f;
			GLES20.glUniformMatrix4fv(ufboMVPMatrix_id, 1, false, fbmattransform);
			GLES20.glUniform2f(ufboc_id, -0.75f, 0);

			GLES20.glClearColor(0f, 0f, 0f, 1f);		
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
			
			
			
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
			GLES20.glUseProgram(progid);

			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, texcoord_vbo);
			GLES20.glEnableVertexAttribArray(texcoordAttrib);
			GLES20.glVertexAttribPointer(texcoordAttrib, 2, GLES20.GL_FLOAT, false, 16, 8);
			GLES20.glEnableVertexAttribArray(posAttrib);
			GLES20.glVertexAttribPointer(posAttrib, 2, GLES20.GL_FLOAT, false, 16, 0);
			
			
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fbo_tex1);
			
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
	
 	private void calculateViewMatrix(float[] m, int width, int height) {
		//Matrix.perspectiveM(mattransformVertices2, 0, 45, ((float)mWidth)/((float)mHeight), 1, 10);
		Matrix.setIdentityM(m, 0);
		float scale = 1f;
		if(width>height){
			m[0] = scale*((float)height)/((float)width);
			m[5] = scale;
		}else{
			m[0] = scale;
			m[5] = scale*((float)width)/((float)height);
		}
	}
	
	private void checkLinkerrors(int pid) {
		int status[] = new int[1];
		GLES20.glGetProgramiv(pid, GLES20.GL_LINK_STATUS,
				IntBuffer.wrap(status));

		if (status[0] == GLES20.GL_FALSE) {
			Log.e("JuliaGL", "FUCK IT ALL!!!!****\n\n");

			String logMessage = GLES20.glGetProgramInfoLog(pid);
			Log.e("JuliaGL", "ERROR: " + logMessage);
			GLES20.glDeleteProgram(pid);
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
