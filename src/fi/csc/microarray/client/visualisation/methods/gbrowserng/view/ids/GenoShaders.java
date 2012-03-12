package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids;


import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.managers.AssetManager;

import javax.media.opengl.GL2;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class GenoShaders {

	public enum ShaderID {
		GENE_CIRCLE,
		TORRENT,
		CIRCLESEPARATOR,
		BEZIER,
		TEXRECTANGLE,
		CENTROMERE,
		PLAINMVP
	}
	
	private static final Map<ShaderID, Shader> programs = new EnumMap<ShaderID, Shader>(ShaderID.class);

	public static void createShaders(GL2 gl) {
		programs.put(ShaderID.CIRCLESEPARATOR, createProgram(gl, "shaders/genecircleseparator.vert", "shaders/genecircleseparator.frag"));
		programs.put(ShaderID.TORRENT, createProgram(gl, "shaders/torrent.vert", "shaders/torrent.frag"));
		programs.put(ShaderID.BEZIER, createProgram(gl, "shaders/bezier2.vert", "shaders/bezier2.frag"));
		programs.put(ShaderID.TEXRECTANGLE, createProgram(gl, "shaders/texrectangle.vert", "shaders/texrectangle.frag"));
		programs.put(ShaderID.GENE_CIRCLE, createProgram(gl, "shaders/genecircle.vert", "shaders/genecircle.frag"));
		programs.put(ShaderID.CENTROMERE, createProgram(gl, "shaders/centromere.vert", "shaders/centromere.frag"));
		programs.put(ShaderID.PLAINMVP, createProgram(gl, "shaders/plainMVP.vert", "shaders/plain.frag"));
	}
	
	public static Shader getProgram(ShaderID program) {
		return programs.get(program);
	}
	
	public static Shader createProgram(GL2 gl, String vertexshader, String fragmentshader) {
		try {
			return new Shader(gl, readFile(vertexshader), readFile(fragmentshader));
		}
		catch(FileNotFoundException e) {
			throw new RuntimeException("File not found: ", e);
		}
	}
	
	private static String readFile(String filename) throws FileNotFoundException {
		InputStream is = AssetManager.get(filename);
		if(is==null) {
			throw new RuntimeException("Could not load shader file: " + filename);
		}
		return new Scanner(is).useDelimiter("\\Z").next();
	}
}
