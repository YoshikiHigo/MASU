package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class ProgramSlice {

    /**
     * ��(statementA�CstatementB)����_�Ƃ��ăo�b�N���[�h�X���C�X���s���C
     * �X���C�X�Ɍ����n�b�V���l�̈�v���镶���N���[�����\�����镶�Ƃ��Ď擾����
     * 
     * @param elementA
     * @param elementB
     * @param clonePair
     * @param assignedVariableHashes
     * @param elementHash
     * @param usedVariableHashesA
     * @param usedVariableHashesB
     */
    static void performBackwordSlice(final ExecutableElement elementA,
            final ExecutableElement elementB, final ClonePairInfo clonePair,
            final Map<VariableInfo<?>, Set<ExecutableElement>> assignedVariableHashes,
            final Map<ExecutableElement, Integer> elementHash,
            final Set<VariableInfo<?>> usedVariableHashesA,
            final Set<VariableInfo<?>> usedVariableHashesB) {

        // �X���C�X��_�̕ϐ����p���擾
        final Set<VariableUsageInfo<?>> variableUsagesA = elementA.getVariableUsages();
        final Set<VariableUsageInfo<?>> variableUsagesB = elementB.getVariableUsages();

        // �X���C�X�ǉ��p��Set��錾
        final SortedSet<ExecutableElement> relatedElementsA = new TreeSet<ExecutableElement>();
        final SortedSet<ExecutableElement> relatedElementsB = new TreeSet<ExecutableElement>();

        //�@�X���C�X��_(statementA)�̕ϐ����p���Q�Ƃł���΁C���̕ϐ��ɑ΂��đ�����s���Ă��镶���X���C�X�ǉ��p��Set�Ɋi�[����
        for (final VariableUsageInfo<?> variableUsage : variableUsagesA) {
            if (variableUsage.isReference()) {
                final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                if (!usedVariableHashesA.contains(usedVariable)
                        && !(usedVariable instanceof FieldInfo)
                        && assignedVariableHashes.containsKey(usedVariable)) {
                    relatedElementsA.addAll(assignedVariableHashes.get(usedVariable));
                    usedVariableHashesA.add(usedVariable);
                }
            }
        }

        //�@�X���C�X��_(statementB)�̕ϐ����p���Q�Ƃł���΁C���̕ϐ��ɑ΂��đ�����s���Ă��镶���X���C�X�ǉ��p��Set�Ɋi�[����
        for (final VariableUsageInfo<?> variableUsage : variableUsagesB) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (variableUsage.isReference()) {
                if (!usedVariableHashesB.contains(usedVariable)
                        && !(usedVariable instanceof FieldInfo)
                        && assignedVariableHashes.containsKey(usedVariable)) {
                    relatedElementsB.addAll(assignedVariableHashes.get(usedVariable));
                    usedVariableHashesB.add(usedVariable);
                }
            }
        }

        final ExecutableElement[] relatedElementArrayA = relatedElementsA
                .toArray(new ExecutableElement[] {});
        final ExecutableElement[] relatedElementArrayB = relatedElementsB
                .toArray(new ExecutableElement[] {});

        for (int a = 0; a < relatedElementArrayA.length; a++) {

            if (relatedElementArrayA[a] == elementA) {
                break;
            }

            for (int b = 0; b < relatedElementArrayB.length; b++) {

                if (relatedElementArrayB[b] == elementB) {
                    break;
                }

                if ((relatedElementArrayA[a] instanceof SingleStatementInfo)
                        && (relatedElementArrayB[b] instanceof SingleStatementInfo)) {

                    final int hashA = elementHash.get(relatedElementArrayA[a]);
                    final int hashB = elementHash.get(relatedElementArrayB[b]);
                    if (hashA == hashB) {
                        clonePair.add(relatedElementArrayA[a], relatedElementArrayB[b]);

                        ProgramSlice.performBackwordSlice(relatedElementArrayA[a],
                                relatedElementArrayB[b], clonePair, assignedVariableHashes,
                                elementHash, usedVariableHashesA, usedVariableHashesB);
                    }

                } else if ((relatedElementArrayA[a] instanceof ConditionalBlockInfo)
                        && (relatedElementArrayB[b] instanceof ConditionalBlockInfo)) {

                    final ConditionInfo conditionA = ((ConditionalBlockInfo) relatedElementArrayA[a])
                            .getCondition();
                    final ConditionInfo conditionB = ((ConditionalBlockInfo) relatedElementArrayB[b])
                            .getCondition();

                    final int hashA = conditionA.getText().hashCode();
                    final int hashB = conditionB.getText().hashCode();

                    if (hashA == hashB) {

                        clonePair.add(conditionA, conditionB);

                        ProgramSlice.performForwardSlice(
                                (ConditionalBlockInfo) relatedElementArrayA[a],
                                (ConditionalBlockInfo) relatedElementArrayB[b], clonePair,
                                assignedVariableHashes, elementHash, usedVariableHashesA,
                                usedVariableHashesB);
                    }
                }
            }
        }

        /*
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
                }
            }
        }*/
    }

    static void performForwardSlice(final ConditionalBlockInfo blockA,
            final ConditionalBlockInfo blockB, final ClonePairInfo clonePair,
            final Map<VariableInfo<?>, Set<ExecutableElement>> variableUsageHashes,
            final Map<ExecutableElement, Integer> statementHash,
            final Set<VariableInfo<?>> usedVariableHashesA,
            final Set<VariableInfo<?>> usedVariableHashesB) {

        final ConditionInfo conditionA = blockA.getCondition();
        final ConditionInfo conditionB = blockB.getCondition();

        final Set<VariableUsageInfo<?>> variableUsagesA = conditionA.getVariableUsages();
        final Set<VariableUsageInfo<?>> variableUsagesB = conditionB.getVariableUsages();

        final Set<VariableInfo<?>> usedVariablesA = new HashSet<VariableInfo<?>>();
        for (final VariableUsageInfo<?> variableUsage : variableUsagesA) {
            final VariableInfo<?> variable = variableUsage.getUsedVariable();
            usedVariablesA.add(variable);
        }

        final Set<VariableInfo<?>> usedVariablesB = new HashSet<VariableInfo<?>>();
        for (final VariableUsageInfo<?> variableUsage : variableUsagesB) {
            final VariableInfo<?> variable = variableUsage.getUsedVariable();
            usedVariablesB.add(variable);
        }

        final SortedSet<StatementInfo> innerStatementA = blockA.getStatements();
        final SortedSet<StatementInfo> innerStatementB = blockB.getStatements();

        final StatementInfo[] innerStatementArrayA = innerStatementA
                .toArray(new StatementInfo[] {});
        final StatementInfo[] innerStatementArrayB = innerStatementB
                .toArray(new StatementInfo[] {});

        for (int i = 0; i < innerStatementArrayA.length; i++) {
            for (int j = 0; j < innerStatementArrayB.length; j++) {

                if ((innerStatementArrayA[i] instanceof SingleStatementInfo)
                        && (innerStatementArrayB[j] instanceof SingleStatementInfo)) {

                    final int hashA = statementHash.get(innerStatementArrayA[i]);
                    final int hashB = statementHash.get(innerStatementArrayB[j]);

                    if ((hashA == hashB)
                            && ProgramSlice.isUsed(usedVariablesA, innerStatementArrayA[i])
                            && ProgramSlice.isUsed(usedVariablesB, innerStatementArrayB[j])) {
                    }

                } else if ((innerStatementArrayA[i] instanceof ConditionalBlockInfo)
                        && (innerStatementArrayB[j] instanceof ConditionalBlockInfo)) {

                    final ConditionInfo innerConditionA = ((ConditionalBlockInfo) innerStatementArrayA[i])
                            .getCondition();
                    final ConditionInfo innerConditionB = ((ConditionalBlockInfo) innerStatementArrayB[j])
                            .getCondition();

                    final int hashA = innerConditionA.getText().hashCode();
                    final int hashB = innerConditionB.getText().hashCode();

                    if ((hashA == hashB) && ProgramSlice.isUsed(usedVariablesA, innerConditionA)
                            && ProgramSlice.isUsed(usedVariablesB, innerConditionB)) {
                        clonePair.add(innerConditionA, innerConditionB);

                        ProgramSlice.performForwardSlice(
                                (ConditionalBlockInfo) innerStatementArrayA[i],
                                (ConditionalBlockInfo) innerStatementArrayB[j], clonePair,
                                variableUsageHashes, statementHash, usedVariableHashesA,
                                usedVariableHashesB);
                    }
                }
            }
        }
    }

    private static boolean isUsed(final Set<VariableInfo<?>> variables,
            final ExecutableElement executableElement) {

        final Set<VariableUsageInfo<?>> variableUsages = executableElement.getVariableUsages();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (variables.contains(usedVariable)) {
                return true;
            }
        }

        return false;
    }
}
