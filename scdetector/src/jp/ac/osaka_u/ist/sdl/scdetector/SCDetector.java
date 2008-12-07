package jp.ac.osaka_u.ist.sdl.scdetector;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
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


/**
 * �v���O�����ˑ��O���t�̓��`�ȃT�u�O���t�������R�[�h�N���[���Ƃ��Č��o����v���O����
 * ���o�Ώۂ͈����ŗ^������D
 * 
 * @author higo
 */
public class SCDetector extends MetricsTool {

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

        // �ϐ����w�肷�邱�Ƃɂ��C���̕ϐ����g�p���Ă��镶���擾���邽�߂̃n�b�V�����\�z����
        final Map<VariableInfo<?>, Set<StatementInfo>> variableUsageHashes = new HashMap<VariableInfo<?>, Set<StatementInfo>>();
        for (final TargetMethodInfo method : MethodInfoManager.getInstance().getTargetMethodInfos()) {
            SCDetector.makeVariableUsageHashes(method, variableUsageHashes);
        }

        // ���P�ʂŐ��K�����s��
        final Map<Integer, List<StatementInfo>> normalizedStatementHashes = new HashMap<Integer, List<StatementInfo>>();
        for (final TargetMethodInfo method : MethodInfoManager.getInstance().getTargetMethodInfos()) {
            SCDetector.makeNormalizedStatementHashes(method, normalizedStatementHashes);
        }

        // ���P�ʂ̃n�b�V���̃L���b�V�����쐬
        final Map<StatementInfo, Integer> statementHashCache = new HashMap<StatementInfo, Integer>();
        for (final Integer hash : normalizedStatementHashes.keySet()) {
            final List<StatementInfo> statements = normalizedStatementHashes.get(hash);
            for (final StatementInfo statement : statements) {
                statementHashCache.put(statement, hash);
            }

        }

        /*
        for (final Integer hash : normalizedStatementHashes.keySet()) {

            final List<StatementInfo> statements = normalizedStatementHashes.get(hash);
            if (1 < statements.size()) {
                System.out.println("-----BEGIN-----");
                for (final StatementInfo statement : statements) {
                    final String text = statement.getText();
                    System.out.println(text);
                }
                System.out.println("----- END -----");
            }
        }*/

        int index = 0;
        for (final Integer hash : normalizedStatementHashes.keySet()) {

            System.out.println("===" + index++ + "/" + normalizedStatementHashes.size() + "===");

            final List<StatementInfo> statements = normalizedStatementHashes.get(hash);

            System.out.println(statements.size());

            if ((1 < statements.size()) && (statements.size() < 10)) {
                for (int i = 0; i < statements.size(); i++) {
                    for (int j = i + 1; j < statements.size(); j++) {

                        final StatementInfo statementA = statements.get(i);
                        final StatementInfo statementB = statements.get(j);

                        final ClonePairInfo clonePair = new ClonePairInfo(statementA, statementB);

                        SCDetector.performBackwordSlice((SingleStatementInfo) statementA,
                                (SingleStatementInfo) statementB, clonePair, variableUsageHashes,
                                statementHashCache);

                        if (1 < clonePair.size()) {
                            System.out.println("-----BEGIN-----");
                            System.out.println("-----  A  -----");
                            final SortedSet<StatementInfo> cloneA = clonePair.getCloneA();
                            final SortedSet<StatementInfo> cloneB = clonePair.getCloneB();
                            for (final StatementInfo statement : cloneA) {
                                System.out.println(statement.getFromLine() + ":"
                                        + statement.getText());
                            }
                            System.out.println("-----  B  -----");
                            for (final StatementInfo statement : cloneB) {
                                System.out.println(statement.getFromLine() + ":"
                                        + statement.getText());
                            }
                            System.out.println("----- END -----");
                        }
                    }
                }
            }
        }
    }

    private static void makeVariableUsageHashes(final LocalSpaceInfo localSpace,
            final Map<VariableInfo<?>, Set<StatementInfo>> variableUsageHashes) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                final Set<VariableUsageInfo<?>> variableUsages = ((SingleStatementInfo) statement)
                        .getVariableUsages();
                for (final VariableUsageInfo<?> variableUsage : variableUsages) {
                    final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                    Set<StatementInfo> statements = variableUsageHashes.get(usedVariable);
                    if (null == statements) {
                        statements = new HashSet<StatementInfo>();
                        variableUsageHashes.put(usedVariable, statements);
                    }
                    statements.add(statement);
                }

            } else if (statement instanceof BlockInfo) {
                SCDetector.makeVariableUsageHashes((BlockInfo) statement, variableUsageHashes);
            }
        }
    }

    private static void makeNormalizedStatementHashes(final LocalSpaceInfo localSpace,
            final Map<Integer, List<StatementInfo>> normalizedStatementHashes) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                final String normalizedStatement = Conversion
                        .getNormalizedString((SingleStatementInfo) statement);
                final int hash = normalizedStatement.hashCode();

                List<StatementInfo> statements = normalizedStatementHashes.get(hash);
                if (null == statements) {
                    statements = new ArrayList<StatementInfo>();
                    normalizedStatementHashes.put(hash, statements);
                }
                statements.add(statement);

            } else if (statement instanceof BlockInfo) {
                SCDetector.makeNormalizedStatementHashes((BlockInfo) statement,
                        normalizedStatementHashes);
            }
        }
    }

    private static void performBackwordSlice(final SingleStatementInfo statementA,
            final SingleStatementInfo statementB, final ClonePairInfo clonePair,
            final Map<VariableInfo<?>, Set<StatementInfo>> variableUsageHashes,
            final Map<StatementInfo, Integer> statementHashCache) {

        final Set<VariableUsageInfo<?>> variableUsagesA = statementA.getVariableUsages();
        final Set<VariableUsageInfo<?>> variableUsagesB = statementB.getVariableUsages();

        // Heuristisc: �����̕��ɂ�����ϐ��g�p�̐��������Ƃ��̂݃X���C�X�����
        if (variableUsagesA.size() == variableUsagesB.size()) {

            final Iterator<VariableUsageInfo<?>> iteratorA = variableUsagesA.iterator();
            final Iterator<VariableUsageInfo<?>> iteratorB = variableUsagesB.iterator();

            while (iteratorA.hasNext() && iteratorB.hasNext()) {

                final SortedSet<StatementInfo> relatedStatementsA = new TreeSet<StatementInfo>();
                final SortedSet<StatementInfo> relatedStatementsB = new TreeSet<StatementInfo>();

                final VariableUsageInfo<?> variableUsageA = iteratorA.next();
                final VariableUsageInfo<?> variableUsageB = iteratorB.next();

                final VariableInfo<?> usedVariableA = variableUsageA.getUsedVariable();
                final VariableInfo<?> usedVariableB = variableUsageB.getUsedVariable();

                // �ꎞ�I�Ƀ��[�J���ϐ��ƈ����݂̂��X���C�X�ɗ��p����悤�ɂ��Ă���
                // �ŏI�I�ɂ́C���ׂĂ̎�ނ̕ϐ���p���āC�������\�b�h���̕����Ƃ��Ă���悤�ɕύX
                if (!(usedVariableA instanceof FieldInfo) && !(usedVariableB instanceof FieldInfo)) {
                    relatedStatementsA.addAll(variableUsageHashes.get(usedVariableA));
                    relatedStatementsB.addAll(variableUsageHashes.get(usedVariableB));

                    final StatementInfo[] relatedStatementArrayA = relatedStatementsA
                            .toArray(new StatementInfo[] {});
                    final StatementInfo[] relatedStatementArrayB = relatedStatementsB
                            .toArray(new StatementInfo[] {});

                    for (int a = 0; a < relatedStatementArrayA.length; a++) {

                        if (relatedStatementArrayA[a] == statementA) {
                            break;
                        }

                        for (int b = 0; b < relatedStatementArrayB.length; b++) {

                            if (relatedStatementArrayB[b] == statementB) {
                                break;
                            }

                            if ((relatedStatementArrayA[a] instanceof SingleStatementInfo)
                                    && (relatedStatementArrayB[b] instanceof SingleStatementInfo)) {

                                final int hashA = statementHashCache
                                        .get((SingleStatementInfo) relatedStatementArrayA[a]);
                                final int hashB = statementHashCache
                                        .get((SingleStatementInfo) relatedStatementArrayB[b]);
                                if (hashA == hashB) {
                                    clonePair.add(relatedStatementArrayA[a],
                                            relatedStatementArrayB[b]);

                                    SCDetector.performBackwordSlice(
                                            (SingleStatementInfo) relatedStatementArrayA[a],
                                            (SingleStatementInfo) relatedStatementArrayB[b],
                                            clonePair, variableUsageHashes, statementHashCache);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
