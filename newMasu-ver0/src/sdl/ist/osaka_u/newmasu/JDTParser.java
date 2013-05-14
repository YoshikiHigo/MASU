﻿package sdl.ist.osaka_u.newmasu;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import sdl.ist.osaka_u.newmasu.AST.ASTCaller;
import sdl.ist.osaka_u.newmasu.dataManager.BindingManager;
import sdl.ist.osaka_u.newmasu.io.MetricsWriter;
import sdl.ist.osaka_u.newmasu.test.TestVisitor;
import sdl.ist.osaka_u.newmasu.util.ListFiles;

public class JDTParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// コマンドライン引数を処理
		final Options options = new Options();
		{
			options.addOption(OptionBuilder.withArgName("directory").hasArg()
					.isRequired().withDescription("target directory")
					.create("d"));

			options.addOption(OptionBuilder
					.withArgName("language")
					.hasArg()
					.isRequired()
					.withDescription(
							"java version")
					.create("v"));

			options.addOption(OptionBuilder.withArgName("Library directory")
					.hasArgs()
					.withDescription("specify directory that stored libraries")
					.create("l"));

			options.addOption(OptionBuilder
					.withArgName("ClassMetricsFile")
					.hasArg()
					.withDescription(
							"specify file that measured CLASS metrics were stored into")
					.create("C"));

			options.addOption(OptionBuilder
					.withArgName("MethodMetricsFile")
					.hasArg()
					.withDescription(
							"specify file that measured METHOD metrics were stored into")
					.create("M"));

			options.addOption(OptionBuilder
					.withArgName("FieldMetricsFile")
					.hasArg()
					.withDescription(
							"specify file that measured FIELD metrics were stored into")
					.create("F"));
		}

		CommandLine cmd = null;

		try {
			final CommandLineParser parser = new BasicParser();
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			HelpFormatter f = new HelpFormatter();
			f.printHelp("OptionsTip", options);

			e.printStackTrace();
			return;
		}

		// 解析用設定
		if (cmd.hasOption("C")) {
			Settings.getInstance()
					.setClassMetricsFile(cmd.getOptionValue("C"));
		}
		if (cmd.hasOption("M")) {
			Settings.getInstance()
					.setMethodMetricsFile(cmd.getOptionValue("M"));
		}
		if (cmd.hasOption("F")) {
			Settings.getInstance()
					.setFieldMetricsFile(cmd.getOptionValue("F"));
		}

		Settings.getInstance().setVersion(cmd.getOptionValue("v"));
		Settings.getInstance().addTargetDirectory(cmd.getOptionValue("d"));
//		Settings.getInstance().setVerbose(true);

		// 対象ディレクトリ以下のJavaファイルを登録し，解析
		{
			final JDTParser viewer = new JDTParser();

//			viewer.loadPlugins(Settings.getInstance().getMetrics());

			// viewer.analyzeLibraries();
			viewer.addLibraries(cmd);

			// viewer.readTargetFiles();
			viewer.addTargetFiles();

			viewer.parseTargetFiles();
//			viewer.analyzeTargetFiles();
//			viewer.launchPlugins();
			viewer.writeMetrics();
		}

		// for test
		// final DrawPDG testPDG = new DrawPDG();
//		for (Entry<Path, CompilationUnit> map : BindingManager.getRel()
//				.entrySet()) {
//			System.out.println("----" + map.getKey() + "----");
//			map.getValue().accept(new TestVisitor());
//			System.out.println();
//		}

		System.out.println("successfully finished.");
	}



	/**
	 * 対象ファイルのASTから未解決クラス，フィールド，メソッド情報を取得
	 */
	public void parseTargetFiles() {
		final ASTCaller caller = new ASTCaller();

		// final TargetFile[] files = DataManager.getInstance()
		// .getTargetFileManager().getFiles().toArray(new TargetFile[0]);
	}

	/**
	 * Settingsの中にライブラリファイルを登録する． デフォルトのディレクトリは./resource
	 */
	public void addLibraries(final CommandLine cmd) {
		String[] libPaths = new String[1];
		libPaths[0] = "./resource";

		if (cmd.hasOption("l"))
			libPaths = cmd.getOptionValues("l");

		for (String arg : libPaths) {
			Path path = Paths.get(arg);
			if (Files.exists(path)) {
				ArrayList<String> jarList = ListFiles.list("jar", path);

				for (String p : jarList) {
					Settings.getInstance().addLibrary(p);
				}
			}
		}
	}

	/**
	 * 対象ディレクトリから対象ファイルを列挙
	 */
	private void addTargetFiles() {
		final Set<String> targetDir = Settings.getInstance()
				.getTargetDirectories();
		for (String dir : targetDir) {
			final ArrayList<String> files = ListFiles.list("java",
					Paths.get(dir));
			for (String file : files)
				Settings.getInstance().addListFile(file);
		}

	}

	private void writeMetrics() {

		Settings setting = Settings.getInstance();

		if(null != setting.getClassMetricsFile()){

		}
		if(null != setting.getMethodMetricsFile()){
			MetricsWriter.writeMethodMetrics(setting.getMethodMetricsFile());
		}
		if(null != setting.getFieldMetricsFile()){

		}

	}

//	/**
//	 * プラグインをロードする. 指定された言語，指定されたメトリクスに関連するプラグインのみを {@link PluginManager}に登録する.
//	 * null が指定された場合は対象言語において計測可能な全てのメトリクスを登録する
//	 *
//	 * @param metrics
//	 *            指定するメトリクスの配列，指定しない場合はnull
//	 */
//	public void loadPlugins(final String[] metrics) {
//
//		final PluginManager pluginManager = DataManager.getInstance()
//				.getPluginManager();
//		final Settings settings = Settings.getInstance();
//		try {
//			for (final AbstractPlugin plugin : (new DefaultPluginLoader())
//					.loadPlugins()) {// プラグインを全ロード
//				final PluginInfo info = plugin.getPluginInfo();
//
//				// 対象言語で計測可能でなければ登録しない
//				if (!info.isMeasurable(settings.getLanguage())) {
//					continue;
//				}
//
//				if (null != metrics) {
//					// メトリクスが指定されているのでこのプラグインと一致するかチェック
//					final String pluginMetricName = info.getMetricName();
//					for (final String metric : metrics) {
//						if (metric.equalsIgnoreCase(pluginMetricName)) {
//							pluginManager.addPlugin(plugin);
//							break;
//						}
//					}
//
//					// メトリクスが指定されていないのでとりあえず全部登録
//				} else {
//					pluginManager.addPlugin(plugin);
//				}
//			}
//		} catch (PluginLoadException e) {
//			err.println(e.getMessage());
//			System.exit(0);
//		}
//	}



}