package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids;

import com.soulaim.tech.gles.shaders.Shader;

import javax.media.opengl.GL2;
import javax.sound.midi.SysexMessage;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;

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
		programs.put(ShaderID.CIRCLESEPARATOR, createProgram(gl, "resources/shaders/genecircleseparator.vert", "resources/shaders/genecircleseparator.frag"));
		programs.put(ShaderID.TORRENT, createProgram(gl, "resources/shaders/torrent.vert", "resources/shaders/torrent.frag"));
		programs.put(ShaderID.BEZIER, createProgram(gl, "resources/shaders/bezier2.vert", "resources/shaders/bezier2.frag"));
		programs.put(ShaderID.TEXRECTANGLE, createProgram(gl, "resources/shaders/texrectangle.vert", "resources/shaders/texrectangle.frag"));
		programs.put(ShaderID.GENE_CIRCLE, createProgram(gl, "resources/shaders/genecircle.vert", "resources/shaders/genecircle.frag"));
		programs.put(ShaderID.CENTROMERE, createProgram(gl, "resources/shaders/centromere.vert", "resources/shaders/centromere.frag"));
		programs.put(ShaderID.PLAINMVP, createProgram(gl, "resources/shaders/plainMVP.vert", "resources/shaders/plain.frag"));
	}
	
	public static Shader getProgram(ShaderID program) {
		return programs.get(program);
	}
	
	public static Shader createProgram(GL2 gl, String vertexshader, String fragmentshader) {
		return new Shader(gl, vertexshader, fragmentshader);
	}
}
