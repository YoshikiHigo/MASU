package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
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
     * @param usedVariableHashesA
     * @param usedVariableHashesB
     */
    static void performBackwordSlice(final ExecutableElementInfo elementA,
            final ExecutableElementInfo elementB, final ClonePairInfo clonePair,
            final Set<VariableInfo<?>> usedVariableHashesA,
            final Set<VariableInfo<?>> usedVariableHashesB) {

        // �X���C�X��_�̕ϐ����p���擾
        final Set<VariableUsageInfo<?>> variableUsagesA = elementA.getVariableUsages();
        final Set<VariableUsageInfo<?>> variableUsagesB = elementB.getVariableUsages();

        // �X���C�X�ǉ��p��Set��錾
        final SortedSet<ExecutableElementInfo> relatedElementsA = new TreeSet<ExecutableElementInfo>();
        final SortedSet<ExecutableElementInfo> relatedElementsB = new TreeSet<ExecutableElementInfo>();

        //�@�X���C�X��_(elementA)�̕ϐ����p���Q�Ƃł���΁C���̕ϐ��ɑ΂��đ�����s���Ă��镶���X���C�X�ǉ��p��Set�Ɋi�[����
        for (final VariableUsageInfo<?> variableUsage : variableUsagesA) {
            if (variableUsage.isReference()) {
                final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                if (!usedVariableHashesA.contains(usedVariable)
                        && !(usedVariable instanceof FieldInfo)
                        && AssignedVariableHashMap.INSTANCE.containsKey(usedVariable)) {
                    relatedElementsA.addAll(AssignedVariableHashMap.INSTANCE.get(usedVariable));
                    usedVariableHashesA.add(usedVariable);
                }
            }
        }

        //�@�X���C�X��_(elementB)�̕ϐ����p���Q�Ƃł���΁C���̕ϐ��ɑ΂��đ�����s���Ă��镶���X���C�X�ǉ��p��Set�Ɋi�[����
        for (final VariableUsageInfo<?> variableUsage : variableUsagesB) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (variableUsage.isReference()) {
                if (!usedVariableHashesB.contains(usedVariable)
                        && !(usedVariable instanceof FieldInfo)
                        && AssignedVariableHashMap.INSTANCE.containsKey(usedVariable)) {
                    relatedElementsB.addAll(AssignedVariableHashMap.INSTANCE.get(usedVariable));
                    usedVariableHashesB.add(usedVariable);
                }
            }
        }

        final ExecutableElementInfo[] relatedElementArrayA = relatedElementsA
                .toArray(new ExecutableElementInfo[] {});
        final ExecutableElementInfo[] relatedElementArrayB = relatedElementsB
                .toArray(new ExecutableElementInfo[] {});

        for (int a = 0; a < relatedElementArrayA.length; a++) {

            // �N���[�������݂��Ȃ�ExecutableElement�ɂ��Ă͒��ׂ�K�v���Ȃ�
            {
                final int hashA = NormalizedElementHashMap.INSTANCE
                        .getHash(relatedElementArrayA[a]);
                if (NormalizedElementHashMap.INSTANCE.get(hashA).size() < 2) {
                    continue;
                }
            }

            // Backword�X���C�X�Ȃ̂ŁC�����������ɂ���ExecutableElement�ɂ��Ă͒��ׂ�K�v���Ȃ�
            if (relatedElementArrayA[a] == elementA) {
                break;
            }

            for (int b = 0; b < relatedElementArrayB.length; b++) {

                // �N���[�������݂��Ȃ�ExecutableElement�ɂ��Ă͒��ׂ�K�v���Ȃ�
                {
                    final int hashB = NormalizedElementHashMap.INSTANCE
                            .getHash(relatedElementArrayB[b]);
                    if (NormalizedElementHashMap.INSTANCE.get(hashB).size() < 2) {
                        continue;
                    }
                }

                // Backword�X���C�X�Ȃ̂ŁC�����������ɂ���ExecutableElement�ɂ��Ă͒��ׂ�K�v���Ȃ�
                if (relatedElementArrayB[b] == elementB) {
                    break;
                }

                if ((relatedElementArrayA[a] instanceof SingleStatementInfo)
                        && (relatedElementArrayB[b] instanceof SingleStatementInfo)) {

                    final int hashA = NormalizedElementHashMap.INSTANCE
                            .getHash(relatedElementArrayA[a]);
                    final int hashB = NormalizedElementHashMap.INSTANCE
                            .getHash(relatedElementArrayB[b]);
                    if (hashA == hashB) {
                        clonePair.add(relatedElementArrayA[a], relatedElementArrayB[b]);

                        ProgramSlice.performBackwordSlice(relatedElementArrayA[a],
                                relatedElementArrayB[b], clonePair, usedVariableHashesA,
                                usedVariableHashesB);
                    }

                } else if ((relatedElementArrayA[a] instanceof ConditionInfo)
                        && (relatedElementArrayB[b] instanceof ConditionInfo)) {

                    final ConditionInfo conditionA = (ConditionInfo) relatedElementArrayA[a];
                    final ConditionInfo conditionB = (ConditionInfo) relatedElementArrayB[b];

                    final int hashA = conditionA.getText().hashCode();
                    final int hashB = conditionB.getText().hashCode();

                    if (hashA == hashB) {
                        clonePair.add(conditionA, conditionB);

                        ProgramSlice.performForwardSlice(conditionA, conditionB, clonePair,
                                usedVariableHashesA, usedVariableHashesB);
                    }
                }
            }
        }
    }

    static void performForwardSlice(final ConditionInfo conditionA, final ConditionInfo conditionB,
            final ClonePairInfo clonePair, final Set<VariableInfo<?>> usedVariableHashesA,
            final Set<VariableInfo<?>> usedVariableHashesB) {

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

        final ConditionalBlockInfo ownerBlockA = ConditionHashMap.INSTANCE.get(conditionA);
        final ConditionalBlockInfo ownerBlockB = ConditionHashMap.INSTANCE.get(conditionB);

        final SortedSet<ExecutableElementInfo> innerElementA = ProgramSlice
                .getAllInnerExecutableElementInfo(ownerBlockA);
        final SortedSet<ExecutableElementInfo> innerElementB = ProgramSlice
                .getAllInnerExecutableElementInfo(ownerBlockB);

        final ExecutableElementInfo[] innerElementArrayA = innerElementA
                .toArray(new ExecutableElementInfo[] {});
        final ExecutableElementInfo[] innerElementArrayB = innerElementB
                .toArray(new ExecutableElementInfo[] {});

        for (int i = 0; i < innerElementArrayA.length; i++) {

            //�@�N���[�����Ȃ�ExecutableElement�ɂ��Ă͒��ׂȂ��Ă悢
            final int hashA = NormalizedElementHashMap.INSTANCE.getHash(innerElementArrayA[i]);
            if (NormalizedElementHashMap.INSTANCE.get(hashA).size() < 2) {
                continue;
            }

            for (int j = 0; j < innerElementArrayB.length; j++) {

                //�@�N���[�����Ȃ�ExecutableElement�ɂ��Ă͒��ׂȂ��Ă悢
                final int hashB = NormalizedElementHashMap.INSTANCE.getHash(innerElementArrayB[j]);
                if (NormalizedElementHashMap.INSTANCE.get(hashB).size() < 2) {
                    continue;
                }

                if ((hashA == hashB) && ProgramSlice.isUsed(usedVariablesA, innerElementArrayA[i])
                        && ProgramSlice.isUsed(usedVariablesB, innerElementArrayB[j])) {

                    clonePair.add(innerElementArrayA[i], innerElementArrayB[j]);
                }
            }
        }
    }

    private static boolean isUsed(final Set<VariableInfo<?>> variables,
            final ExecutableElementInfo ExecutableElementInfo) {

        final Set<VariableUsageInfo<?>> variableUsages = ExecutableElementInfo.getVariableUsages();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (variables.contains(usedVariable)) {
                return true;
            }
        }

        return false;
    }

    private static SortedSet<ExecutableElementInfo> getAllInnerExecutableElementInfo(
            final BlockInfo block) {

        final SortedSet<ExecutableElementInfo> elements = new TreeSet<ExecutableElementInfo>();
        for (final StatementInfo statement : block.getStatements()) {
            if (statement instanceof SingleStatementInfo) {
                elements.add(statement);
            } else if (statement instanceof BlockInfo) {
                elements.addAll(ProgramSlice
                        .getAllInnerExecutableElementInfo((BlockInfo) statement));
            }
        }

        return elements;
    }
}
