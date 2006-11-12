package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.FileInputStream;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.file.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.file.TargetFileData;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;


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

        TargetFileData targetFiles = TargetFileData.getInstance();
        for (int i = 0; i < args.length; i++) {
            targetFiles.add(new TargetFile(args[i]));
        }

        try {
            // targetFiles �� TargetFiles�^�Ő錾����ƃG���[�ɂȂ�D�Ȃ��H
            for (Object targetFile : targetFiles) {
                String name = ((TargetFile) targetFile).getName();
                System.out.println("processing " + name);
                Java15Lexer lexer = new Java15Lexer(new FileInputStream(name));
                Java15Parser parser = new Java15Parser(lexer);
                parser.compilationUnit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
