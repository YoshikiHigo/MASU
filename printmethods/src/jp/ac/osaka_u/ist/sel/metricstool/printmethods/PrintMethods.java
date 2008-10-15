package jp.ac.osaka_u.ist.sel.metricstool.printmethods;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * �Ώۃt�@�C���Ɋ܂܂��Java���\�b�h�̃V�O�j�`�����o�͂���v���O����
 * �Ώۃt�@�C���͈����ŗ^������D
 * 
 * @author higo
 */
public class PrintMethods extends MetricsTool {

    public static void main(String[] args) {

        // ��͗p�ݒ�
        try {

            Class c = Settings.class;
            Field language = c.getDeclaredField("language");
            language.setAccessible(true);
            language.set(null, "java");
            Field directory = c.getDeclaredField("targetDirectory");
            directory.setAccessible(true);
            directory.set(null, args[0]);

        } catch (NoSuchFieldException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

        // �Ώۃf�B���N�g���ȉ���Java�t�@�C����o�^���C���
        final PrintMethods printMethods = new PrintMethods();
        printMethods.registerFilesFromDirectory();
        printMethods.analyzeTargetFiles();

        // �ΏۃN���X�ꗗ���擾
        final Set<TargetClassInfo> classes = ClassInfoManager.getInstance().getTargetClassInfos();
        for (final TargetClassInfo classInfo : classes) {

            // �N���X�����o��
            System.out.println("class: " + classInfo.getFullQualifiedName("."));

            // �e�N���X���̃��\�b�h�ꗗ���擾
            final Set<TargetMethodInfo> methods = classInfo.getDefinedMethods();
            for (final TargetMethodInfo methodInfo : methods) {

                System.out.println("\tmethod: " + methodInfo.getMethodName());

                // �e���\�b�h�̈����ꗗ���擾
                System.out.print("\t\tparameters: ");
                final List<ParameterInfo> parameters = methodInfo.getParameters();
                for (final ParameterInfo parameterInfo : parameters) {

                    System.out.print(parameterInfo.getType().getTypeName() + " "
                            + parameterInfo.getName() + ", ");
                }
                System.out.println();

                // �e���\�b�h�̕Ԃ�l���擾
                System.out.println("\t\treturn type: " + methodInfo.getReturnType().getTypeName());
            }
        }
    }
}
