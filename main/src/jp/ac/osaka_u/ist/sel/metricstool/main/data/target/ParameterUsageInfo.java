package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �����̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
public final class ParameterUsageInfo extends VariableUsageInfo<ParameterInfo> {

    /**
     * �g�p����Ă��������^���ăI�u�W�F�N�g��������
     * 
     * @param ownerExecutableElement �I�[�i�[�G�������g
     * @param usedParameter �g�p����Ă������
     * @param reference �Q�Ƃł���ꍇ�� true, ����ł���ꍇ�� false
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    private ParameterUsageInfo(final ExecutableElementInfo ownerExecutableElement,
            final ParameterInfo usedParameter, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerExecutableElement, usedParameter, reference, fromLine, fromColumn, toLine,
                toColumn);
    }

    @Override
    public TypeInfo getType() {
        final ParameterInfo parameter = this.getUsedVariable();
        final TypeInfo usedVariableType = parameter.getType();
        return usedVariableType;
    }

    /**
     * �g�p����Ă���p�����[�^�C�g�p�̎�ށC�g�p����Ă���ʒu����^���ăC���X�^���X���擾
     * 
     * @param ownerExecutableElement �I�[�i�[�G�������g
     * @param usedParameter �g�p����Ă���p�����[�^
     * @param reference �Q�Ƃł���ꍇ��true�C����ł���ꍇ��false
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @return �p���[���[�^�g�p�̃C���X�^���X
     */
    public static ParameterUsageInfo getInstance(
            final ExecutableElementInfo ownerExecutableElement, final ParameterInfo usedParameter,
            final boolean reference, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        final ParameterUsageInfo instance = new ParameterUsageInfo(ownerExecutableElement,
                usedParameter, reference, fromLine, fromColumn, toLine, toColumn);
        addParameterUsage(instance);
        return instance;
    }

    /**
     * �p�����[�^�ϐ��g�p�̃C���X�^���X���p�����[�^�ϐ�����p�����[�^�ϐ��g�p�ւ̃}�b�v�ɒǉ�
     * @param parameterUsage �p�����[�^�ϐ��g�p
     */
    private static void addParameterUsage(final ParameterUsageInfo parameterUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == parameterUsage) {
            throw new IllegalArgumentException("localVariableUsage is null");
        }

        final ParameterInfo usedParameter = parameterUsage.getUsedVariable();
        if (USAGE_MAP.containsKey(usedParameter)) {
            USAGE_MAP.get(usedParameter).add(parameterUsage);
        } else {
            final TreeSet<ParameterUsageInfo> usages = new TreeSet<ParameterUsageInfo>();
            usages.add(parameterUsage);
            USAGE_MAP.put(usedParameter, usages);
        }
    }

    /**
     * �^����ꂽ�p�����[�^�̎g�p���̃Z�b�g���擾
     * @param parameter �g�p�����擾���������[�J���ϐ�
     * @return �p�����[�^�g�p�̃Z�b�g�D�����ŗ^����ꂽ���[�J���ϐ����g�p����Ă��Ȃ��ꍇ��null
     */
    public final static Set<ParameterUsageInfo> getUsages(final ParameterInfo parameter) {
        if (USAGE_MAP.containsKey(parameter)) {
            return USAGE_MAP.get(parameter);
        } else {
            return Collections.<ParameterUsageInfo> emptySet();
        }
    }

    private static final Map<ParameterInfo, TreeSet<ParameterUsageInfo>> USAGE_MAP = new HashMap<ParameterInfo, TreeSet<ParameterUsageInfo>>();
}
