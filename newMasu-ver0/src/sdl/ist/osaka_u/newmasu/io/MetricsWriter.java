package sdl.ist.osaka_u.newmasu.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;

import sdl.ist.osaka_u.newmasu.Settings;
import sdl.ist.osaka_u.newmasu.AST.MethodVisitor;
import sdl.ist.osaka_u.newmasu.dataManager.BindingManager;

public class MetricsWriter {

	public static void writeMethodMetrics(String filepath){

		try {
			File file = new File(filepath);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

			System.out.println("Writing method information");
			pw.println("Writing method information");

			Set<String> paths = Settings.getInstance().getListFiles();
			for(String path : paths){
				CompilationUnit unit = BindingManager.getRel().get(Paths.get(path));
				MethodVisitor mv = new MethodVisitor(pw);
				unit.accept(mv);
			}

			pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}




	private MetricsWriter(){

	}
}
