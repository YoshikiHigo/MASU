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
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;


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

            final Class<?> settings = Settings.class;
            final Field language = settings.getDeclaredField("language");
            language.setAccessible(true);
            language.set(null, "java");
            final Field directory = settings.getDeclaredField("targetDirectory");
            directory.setAccessible(true);
            directory.set(null, args[0]);
            final Field verbose = settings.getDeclaredField("verbose");
            verbose.setAccessible(true);
            verbose.set(null, Boolean.TRUE);

            // ���\���p�ݒ�
            final Class<?> metricstool = MetricsTool.class;
            final Field out = metricstool.getDeclaredField("out");
            out.setAccessible(true);
            out.set(null, new DefaultMessagePrinter(new MessageSource() {
                public String getMessageSourceName() {
                    return "printmethods";
                }
            }, MESSAGE_TYPE.OUT));
            final Field err = metricstool.getDeclaredField("err");
            err.setAccessible(true);
            err.set(null, new DefaultMessagePrinter(new MessageSource() {
                public String getMessageSourceName() {
                    return "main";
                }
            }, MESSAGE_TYPE.ERROR));
            MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(new MessageListener() {
                public void messageReceived(MessageEvent event) {
                    System.out.print(event.getSource().getMessageSourceName() + " > "
                            + event.getMessage());
                }
            });
            MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(new MessageListener() {
                public void messageReceived(MessageEvent event) {
                    System.err.print(event.getSource().getMessageSourceName() + " > "
                            + event.getMessage());
                }
            });

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
