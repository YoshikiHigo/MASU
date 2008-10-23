package jp.ac.osaka_u.ist.sel.metricstool.printlocation;


import java.lang.reflect.Field;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * �Ώۃt�@�C���Ɋ܂܂��Java���\�b�h�̃V�O�j�`�����o�͂���v���O����
 * �Ώۃt�@�C���͈����ŗ^������D
 * 
 * @author higo
 */
public class PrintLocation extends MetricsTool {

    public static void main(String[] args) {

        // ��͗p�ݒ�
        try {

            Class settings = Settings.class;
            Field language = settings.getDeclaredField("language");
            language.setAccessible(true);
            language.set(null, "java");
            Field directory = settings.getDeclaredField("targetDirectory");
            directory.setAccessible(true);
            directory.set(null, args[0]);

        } catch (NoSuchFieldException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

        // �Ώۃf�B���N�g���ȉ���Java�t�@�C����o�^���C���
        final PrintLocation printLocation = new PrintLocation();
        printLocation.registerFilesFromDirectory();
        printLocation.analyzeTargetFiles();

        for (final TargetClassInfo classInfo : ClassInfoManager.getInstance().getTargetClassInfos()) {
            System.out.println("Class: " + classInfo.getFromLine() + ":"
                    + classInfo.getFromColumn() + " - " + classInfo.getToLine() + ":"
                    + classInfo.getToColumn());

            for (final TargetMethodInfo methodInfo : classInfo.getDefinedMethods()) {
                System.out.println("Method: " + methodInfo.getFromLine() + ":"
                        + methodInfo.getFromColumn() + " - " + methodInfo.getToLine() + ":"
                        + methodInfo.getToColumn());

                for (final StatementInfo statementInfo : methodInfo.getStatements()) {
                    System.out.println("Statement: " + statementInfo.getFromLine() + ":"
                            + statementInfo.getFromColumn() + " - " + statementInfo.getToLine()
                            + ":" + statementInfo.getToColumn());
                }
            }

            for (final TargetConstructorInfo constructorInfo : classInfo.getDefinedConstructors()) {
                System.out.println("Constructor: " + constructorInfo.getFromLine() + ":"
                        + constructorInfo.getFromColumn() + " - " + constructorInfo.getToLine()
                        + ":" + constructorInfo.getToColumn());
            }

            for (final TargetFieldInfo fieldInfo : classInfo.getDefinedFields()) {
                System.out.println("Field: " + fieldInfo.getFromLine() + ":"
                        + fieldInfo.getFromColumn() + " - " + fieldInfo.getToLine() + ":"
                        + fieldInfo.getToColumn());
            }
        }
    }
}
