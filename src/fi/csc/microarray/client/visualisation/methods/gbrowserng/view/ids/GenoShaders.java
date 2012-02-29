package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids;

import gles.SoulGL2;
import gles.shaders.ShaderID;
import managers.ShaderManager;

public class GenoShaders {

	public enum GenoShaderID implements ShaderID {
		GENE_CIRCLE,
		TORRENT,
		CIRCLESEPARATOR,
		BEZIER,
		TEXRECTANGLE,
		CENTROMERE,
		PLAINMVP}

	public static void createShaders(SoulGL2 gl) {
		ShaderManager.createProgram(gl, GenoShaderID.CIRCLESEPARATOR, "genecircleseparator.vert", "genecircleseparator.frag");
		ShaderManager.createProgram(gl, GenoShaderID.TORRENT, "torrent.vert", "torrent.frag");
		ShaderManager.createProgram(gl, GenoShaderID.BEZIER, "bezier2.vert", "bezier2.frag");
		ShaderManager.createProgram(gl, GenoShaderID.TEXRECTANGLE, "texrectangle.vert", "texrectangle.frag");
		ShaderManager.createProgram(gl, GenoShaderID.GENE_CIRCLE, "genecircle.vert", "genecircle.frag");
		ShaderManager.createProgram(gl, GenoShaderID.CENTROMERE, "centromere.vert", "centromere.frag");
		ShaderManager.createProgram(gl, GenoShaderID.PLAINMVP, "plainMVP.vert", "plain.frag");
	}
}
