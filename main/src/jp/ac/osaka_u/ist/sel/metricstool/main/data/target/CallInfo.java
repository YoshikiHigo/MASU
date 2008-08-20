package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���\�b�h�Ăяo���C�R���X�g���N�^�Ăяo���̋��ʂ̐e�N���X
 * 
 * @author higo
 *
 */
public abstract class CallInfo extends EntityUsageInfo {

    /**
     * �ʒu����^���ăI�u�W�F�N�g��������
     */
    CallInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);
        this.parameters = new LinkedList<EntityUsageInfo>();
        this.typeArguments = new LinkedList<ReferenceTypeInfo>();
    }

    /**
     * ���̃��\�b�h�Ăяo���̎�������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param parameter �ǉ����������
     */
    public final void addParameter(final EntityUsageInfo parameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameter) {
            throw new NullPointerException();
        }

        this.parameters.add(parameter);
    }

    /**
     * ���̌Ăяo���̎�������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param parameters �ǉ����������
     */
    public final void addParameters(final List<EntityUsageInfo> parameters) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameters) {
            throw new NullPointerException();
        }

        this.parameters.addAll(parameters);
    }

    /**
     * ���̃��\�b�h�Ăяo���̌^������ǉ��D�v���O�C������͌Ăяo���Ȃ�
     * 
     * @param typeArgument �ǉ�����^����
     */
    public final void addTypeArgument(final ReferenceTypeInfo typeArgument) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArgument) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeArgument);
    }

    /**
     * ���̌Ăяo���̌^������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param typeArguments �ǉ�����^����
     */
    public final void addTypeArguments(final List<ReferenceTypeInfo> typeArguments) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArguments) {
            throw new NullPointerException();
        }

        this.typeArguments.addAll(typeArguments);
    }

    /**
     * ���̌Ăяo���̎�������List��Ԃ��D
     * 
     * @return
     */
    public List<EntityUsageInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        for (EntityUsageInfo parameter : this.getParameters()) {
            variableUsages.addAll(parameter.getVariableUsages());
        }
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    private final List<EntityUsageInfo> parameters;

    private final List<ReferenceTypeInfo> typeArguments;
}
