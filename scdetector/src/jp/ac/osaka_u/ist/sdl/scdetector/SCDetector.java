package jp.ac.osaka_u.ist.sdl.scdetector;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


/**
 * �v���O�����ˑ��O���t�̓��`�ȃT�u�O���t�������R�[�h�N���[���Ƃ��Č��o����v���O����
 * ���o�Ώۂ͈����ŗ^������D
 * 
 * @author higo
 */
public class SCDetector extends MetricsTool {

    public static void main(String[] args) {

        try {

            //�@�R�}���h���C������������
            final Options options = new Options();

            final Option d = new Option("d", "directory", true, "target directory");
            d.setArgName("directory");
            d.setArgs(1);
            d.setRequired(true);
            options.addOption(d);

            final Option l = new Option("l", "language", true,
                    "programming language of analyzed source code");
            l.setArgName("language");
            l.setArgs(1);
            l.setRequired(true);
            options.addOption(l);

            final Option o = new Option("o", "output", true, "output file");
            o.setArgName("output file");
            o.setArgs(1);
            o.setRequired(true);
            options.addOption(o);

            final Option s = new Option("s", "size", true, "lower size of detected clone");
            s.setArgName("size");
            s.setArgs(1);
            s.setRequired(true);
            options.addOption(s);

            final Option pv = new Option("pv", true, "parameterize variables");
            pv.setArgName("variable parameterization level");
            pv.setArgs(1);
            pv.setRequired(false);
            pv.setType(Integer.class);
            options.addOption(pv);

            final Option pm = new Option("pm", true, "parameterize method invocation");
            pm.setArgName("method invocation parameterization level");
            pm.setArgs(1);
            pm.setRequired(false);
            pm.setType(Integer.class);
            options.addOption(pm);

            final Option pc = new Option("pc", true, "parameterize constructor invocation");
            pc.setArgName("constructor invocation parameterization level");
            pc.setArgs(1);
            pc.setRequired(false);
            pc.setType(Integer.class);
            options.addOption(pc);

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            Configuration.INSTANCE.setD(cmd.getOptionValue("d"));
            Configuration.INSTANCE.setL(cmd.getOptionValue("l"));
            Configuration.INSTANCE.setO(cmd.getOptionValue("o"));
            Configuration.INSTANCE.setS(Integer.valueOf(cmd.getOptionValue("s")));

            if (cmd.hasOption("pv")) {
                Configuration.INSTANCE.setPV(Integer.valueOf(cmd.getOptionValue("pv")));
            }

            if (cmd.hasOption("pm")) {
                Configuration.INSTANCE.setPM(Integer.valueOf(cmd.getOptionValue("pm")));
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        try {

            // ��͗p�ݒ�
            final Class<?> settings = Settings.class;
            final Field language = settings.getDeclaredField("language");
            language.setAccessible(true);
            language.set(null, Configuration.INSTANCE.getL());
            final Field directory = settings.getDeclaredField("targetDirectory");
            directory.setAccessible(true);
            directory.set(null, Configuration.INSTANCE.getD());
            final Field verbose = settings.getDeclaredField("verbose");
            verbose.setAccessible(true);
            verbose.set(null, Boolean.TRUE);

            // ���\���p�ݒ�
            final Class<?> metricstool = MetricsTool.class;
            final Field out = metricstool.getDeclaredField("out");
            out.setAccessible(true);
            out.set(null, new DefaultMessagePrinter(new MessageSource() {
                public String getMessageSourceName() {
                    return "scdetector";
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
        final SCDetector scdetector = new SCDetector();
        scdetector.registerFilesFromDirectory();
        scdetector.analyzeTargetFiles();

        // �ϐ����w�肷�邱�Ƃɂ��C���̕ϐ��ɑ΂��đ�����s���Ă��镶���擾���邽�߂̃n�b�V�����\�z����
        // �������C�����߂ɂ��ẮC�ϐ����Q�Ƃ��Ă���ꍇ���n�b�V�����\�z����
        final Map<VariableInfo<?>, Set<ExecutableElement>> assignedVariableHashes = new HashMap<VariableInfo<?>, Set<ExecutableElement>>();
        for (final TargetMethodInfo method : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {
            SCDetector.makeAssignedVariableHashes(method, assignedVariableHashes);
        }

        // ���P�ʂŐ��K�����s��
        final Map<Integer, List<ExecutableElement>> normalizedElements = new HashMap<Integer, List<ExecutableElement>>();
        for (final TargetMethodInfo method : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {
            SCDetector.makeNormalizedElement(method, normalizedElements);
        }

        // ExecutableElement�P�ʂ̃n�b�V���f�[�^���쐬
        final Map<ExecutableElement, Integer> elementHash = new HashMap<ExecutableElement, Integer>();
        for (final Integer hash : normalizedElements.keySet()) {
            final List<ExecutableElement> elements = normalizedElements.get(hash);
            for (final ExecutableElement element : elements) {
                elementHash.put(element, hash);
            }

        }

        // �n�b�V���l������2�̕�����_�ɂ��ăR�[�h�N���[�������o
        final Set<ClonePairInfo> clonePairs = new HashSet<ClonePairInfo>();
        for (final Integer hash : normalizedElements.keySet()) {

            final List<ExecutableElement> elements = normalizedElements.get(hash);

            for (int i = 0; i < elements.size(); i++) {
                for (int j = i + 1; j < elements.size(); j++) {

                    final ExecutableElement elementA = elements.get(i);
                    final ExecutableElement elementB = elements.get(j);

                    final ClonePairInfo clonePair = new ClonePairInfo(elementA, elementB);

                    final Set<VariableInfo<?>> usedVariableHashesA = new HashSet<VariableInfo<?>>();
                    final Set<VariableInfo<?>> usedVariableHashesB = new HashSet<VariableInfo<?>>();

                    ProgramSlice.performBackwordSlice(elementA, elementB, clonePair,
                            assignedVariableHashes, elementHash, usedVariableHashesA,
                            usedVariableHashesB);

                    if (Configuration.INSTANCE.getS() <= clonePair.size()) {
                        clonePairs.add(clonePair);
                    }
                }
            }
        }

        try {

            final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                    Configuration.INSTANCE.getO()));
            oos.writeObject(clonePairs);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makeAssignedVariableHashes(final LocalSpaceInfo localSpace,
            final Map<VariableInfo<?>, Set<ExecutableElement>> assignedVariableHashes) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                final Set<VariableUsageInfo<?>> variableUsages = ((SingleStatementInfo) statement)
                        .getVariableUsages();
                for (final VariableUsageInfo<?> variableUsage : variableUsages) {
                    if (variableUsage.isAssignment()) {
                        final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                        Set<ExecutableElement> statements = assignedVariableHashes
                                .get(usedVariable);
                        if (null == statements) {
                            statements = new HashSet<ExecutableElement>();
                            assignedVariableHashes.put(usedVariable, statements);
                        }
                        statements.add(statement);
                    }
                }

            } else if (statement instanceof BlockInfo) {

                if (statement instanceof ConditionalBlockInfo) {
                    final ConditionInfo condition = ((ConditionalBlockInfo) statement)
                            .getCondition();
                    if (null != condition) {
                        final Set<VariableUsageInfo<?>> variableUsages = condition
                                .getVariableUsages();
                        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
                            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                            Set<ExecutableElement> statements = assignedVariableHashes
                                    .get(usedVariable);
                            if (null == statements) {
                                statements = new HashSet<ExecutableElement>();
                                assignedVariableHashes.put(usedVariable, statements);
                            }
                            statements.add(condition);
                        }
                    }
                }

                SCDetector
                        .makeAssignedVariableHashes((BlockInfo) statement, assignedVariableHashes);
            }
        }
    }

    private static void makeNormalizedElement(final LocalSpaceInfo localSpace,
            final Map<Integer, List<ExecutableElement>> normalizedElements) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                final String normalizedStatement = Conversion
                        .getNormalizedString((SingleStatementInfo) statement);
                final int hash = normalizedStatement.hashCode();

                List<ExecutableElement> statements = normalizedElements.get(hash);
                if (null == statements) {
                    statements = new ArrayList<ExecutableElement>();
                    normalizedElements.put(hash, statements);
                }
                statements.add(statement);

            } else if (statement instanceof BlockInfo) {

                // ConditionalBlockInfo�@�ł���Ȃ�΁C���̏��������n�b�V��������
                if (statement instanceof ConditionalBlockInfo) {
                    final ConditionInfo condition = ((ConditionalBlockInfo) statement)
                            .getCondition();
                    final String normalizedCondition = Conversion.getNormalizedString(condition);
                    final int hash = normalizedCondition.hashCode();

                    List<ExecutableElement> statements = normalizedElements.get(hash);
                    if (null == statements) {
                        statements = new ArrayList<ExecutableElement>();
                        normalizedElements.put(hash, statements);
                    }
                    statements.add(condition);
                }

                SCDetector.makeNormalizedElement((BlockInfo) statement, normalizedElements);
            }
        }
    }
}
