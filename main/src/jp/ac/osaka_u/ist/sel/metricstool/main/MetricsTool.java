package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.File;

import org.jargp.ArgumentProcessor;
import org.jargp.BoolDef;
import org.jargp.ParameterDef;
import org.jargp.StringDef;


/**
 * 
 * @author y-higo
 * 
 * MetricsTool�̃��C���N���X�D ���݂͉������D
 * 
 * since 2006.11.12
 * 
 */
public class MetricsTool {

    /**
     * 
     * @param args �Ώۃt�@�C���̃t�@�C���p�X
     * 
     * ���݉������D �Ώۃt�@�C���̃f�[�^���i�[������C�\����͂��s���D
     */
    public static void main(String[] args) {

        Settings settings = new Settings();
        ArgumentProcessor.processArgs(args, parameterDefs, settings);
        checkParameterValidation();

        /*
         * TargetFileData targetFiles = TargetFileData.getInstance(); for (int i = 0; i <
         * args.length; i++) { targetFiles.add(new TargetFile(args[i])); }
         * 
         * try { for (TargetFile targetFile : targetFiles) { String name = targetFile.getName();
         * System.out.println("processing " + name); Java15Lexer lexer = new Java15Lexer(new
         * FileInputStream(name)); Java15Parser parser = new Java15Parser(lexer);
         * parser.compilationUnit(); } } catch (Exception e) { e.printStackTrace(); }
         */

    }

    /**
     * �����̎d�l�� Jargp �ɓn�����߂̔z��D
     */
    private static ParameterDef[] parameterDefs = {
            new BoolDef('h', "helpMode", "display usage", true),
            new BoolDef('x', "displayMode", "display available language or metrics", true),
            new StringDef('d', "targetDirectory", "Target directory"),
            new StringDef('i', "listFile", "List file including paths of target files"),
            new StringDef('l', "language", "Programming language"),
            new StringDef('m', "metrics", "Measured metrics"),
            new StringDef('F', "fileMetricsFile", "File storing file metrics"),
            new StringDef('C', "classMetricsFile", "File storing class metrics"),
            new StringDef('M', "methodMetricsFile", "File storing method metrics") };

    /**
     * 
     * �����̐��������m�F���邽�߂̃��\�b�h�D �s���Ȉ������w�肳��Ă����ꍇ�Cmain ���\�b�h�ɂ͖߂炸�C���̊֐����Ńv���O�������I������D
     * 
     */
    private static void checkParameterValidation() {

        // �w���v���[�h�Ə��\�����[�h�������ɃI���ɂȂ��Ă���ꍇ�͕s��
        if (Settings.isHelpMode() && Settings.isDisplayMode()) {
            System.err.println("-h and -x can\'t be set at the same time!");
            printUsage();
            System.exit(0);
        }

        
        
        // -d �Ŏw�肳�ꂽ�^�[�Q�b�g�f�B���N�g���̃`�F�b�N
        if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
            String directoryPath = Settings.getTargetDirectory();
            File directory = new File(directoryPath);
            if (!directory.isDirectory()) {
                System.err.println("\"directoryPath\" is not a valid directory!");
                System.exit(0);
            }
        }

        // -i �Ŏw�肳�ꂽ���X�g�t�@�C���̃`�F�b�N
        if (!Settings.getListFile().equals(Settings.INIT)) {
            String filePath = Settings.getTargetDirectory();
            File file = new File(filePath);
            if (!file.isFile()) {
                System.err.println("\"filePath\" is not a valid file!");
                System.exit(0);
            }
        }

        // -l �Ŏw�肳�ꂽ���ꂪ��͉\����ł��邩���`�F�b�N
        if (!Settings.getLanguage().equals(Settings.INIT)) {
            // TODO
        }

        // -m �Ŏw�肳�ꂽ���g���N�X���Z�o�\�ȃ��g���N�X�ł��邩���`�F�b�N
        if (!Settings.getMetrics().equals(Settings.INIT)) {
            // TODO
        }

        // -C �Ŏw�肳�ꂽ���X�g�t�@�C���̃`�F�b�N
        if (!Settings.getClassMetricsFile().equals(Settings.INIT)) {
            String filePath = Settings.getTargetDirectory();
            File file = new File(filePath);
            if (!file.isFile()) {
                System.err.println("\"filePath\" is not a valid file!");
                System.exit(0);
            }
        }

        // -F �Ŏw�肳�ꂽ���X�g�t�@�C���̃`�F�b�N
        if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {
            String filePath = Settings.getTargetDirectory();
            File file = new File(filePath);
            if (!file.isFile()) {
                System.err.println("\"filePath\" is not a valid file!");
                System.exit(0);
            }
        }

        // -M �Ŏw�肳�ꂽ���X�g�t�@�C���̃`�F�b�N
        if (!Settings.getMethodMetricsFile().equals(Settings.INIT)) {
            String filePath = Settings.getTargetDirectory();
            File file = new File(filePath);
            if (!file.isFile()) {
                System.err.println("\"filePath\" is not a valid file!");
                System.exit(0);
            }
        }

        // �w���v���[�h�̏ꍇ
        if (Settings.isHelpMode()) {
            // -h �͑��̃I�v�V�����Ɠ����w��ł��Ȃ�
            if ((!Settings.getTargetDirectory().equals(Settings.INIT))
                    || (!Settings.getListFile().equals(Settings.INIT))
                    || (!Settings.getLanguage().equals(Settings.INIT))
                    || (!Settings.getMetrics().equals(Settings.INIT))
                    || (!Settings.getFileMetricsFile().equals(Settings.INIT))
                    || (!Settings.getClassMetricsFile().equals(Settings.INIT))
                    || (!Settings.getMethodMetricsFile().equals(Settings.INIT))) {
                System.err.println("-h can\'t be specified with any other options!");
                printUsage();
                System.exit(0);
            }

        }

        // ���\�����[�h�̏ꍇ
        if (Settings.isDisplayMode()) {
            // -d �͎g���Ȃ�
            if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
                System.err.println("-d can\'t be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -i �͎g���Ȃ�
            if (!Settings.getListFile().equals(Settings.INIT)) {
                System.err.println("-i can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -F �͎g���Ȃ�
            if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {
                System.err.println("-F can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -C �͎g���Ȃ�
            if (Settings.getFileMetricsFile().equals(Settings.INIT)) {
                System.err.println("-C can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -M �͎g���Ȃ�
            if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {
                System.err.println("-M can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -m ���w�肳��Ă���ꍇ�C-l �ŉ�͉\���ꂪ�w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
            if (!Settings.getMetrics().equals(Settings.INIT)) {
                System.err
                        .println("available language must be specified by -l when -m is specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // ��̓��[�h�̏ꍇ
            if (!Settings.isHelpMode() && !Settings.isDisplayMode()) {

                // -d �� -i �̂ǂ�����w�肳��Ă���͕̂s��
                if (Settings.getTargetDirectory().equals(Settings.INIT)
                        && Settings.getListFile().equals(Settings.INIT)) {
                    System.err.println("-d or -i must be specified in the analysis mode!");
                    printUsage();
                    System.exit(0);
                }

                // -d �� -i �̗������w�肳��Ă���͕̂s��
                if (!Settings.getTargetDirectory().equals(Settings.INIT)
                        && !Settings.getListFile().equals(Settings.INIT)) {
                    System.err.println("-d and -i can't be specified at the same time!");
                    printUsage();
                    System.exit(0);
                }
                
                // �t�@�C�����g���N�X���Z�o����ꍇ�́C-F ���w�肳��Ă��Ȃ����͕s��
                // TODO
                
                // �N���X���g���N�X���Z�o����ꍇ�́C-C ���w�肳��Ă��Ȃ����͕s��
                // TODO
                
                // ���\�b�h���g���N�X���Z�o����ꍇ�́C-M ���w�肳��Ă��Ȃ����͕s��
                // TODO
            }
        }

    }

    /**
     * 
     * �c�[���̎g�����i�R�}���h���C���I�v�V�����j��\������D
     * 
     */
    private static void printUsage() {

        System.err.println();
        System.err.println("Available options:");
        System.err.println("\t-d: root directory that you are going to analysis.");
        System.err.println("\t-i: List file including file paths that you are going to analysis.");
        System.err.println("\t-l: Programming language of the target files.");
        System.err
                .println("\t-m: Metrics that you want to get. Metrics names are separated with \',\'.");
        System.err.println("\t-C: File path that the class type metrics are output");
        System.err.println("\t-F: File path that the file type metrics are output.");
        System.err.println("\t-M: File path that the method type metrics are output");

        System.err.println();
        System.err.println("Usage:");
        System.err.println("\t<Help Mode>");
        System.err.println("\tMetricsTool -h");
        System.err.println();
        System.err.println("\t<Display Mode>");
        System.err.println("\tMetricsTool -x -l");
        System.err.println("\tMetricsTool -x -l language -m");
        System.err.println();
        System.err.println("\t<Analysis Mode>");
        System.err
                .println("\tMetricsTool -d directory -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
        System.err
                .println("\tMetricsTool -l listFile -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
    }
}
