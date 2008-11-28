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
        this.arguments = new LinkedList<ExpressionInfo>();
        this.typeArguments = new LinkedList<ReferenceTypeInfo>();
    }

    /**
     * ���̃��\�b�h�Ăяo���̎�������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param argument �ǉ����������
     */
    public final void addArgument(final EntityUsageInfo argument) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == argument) {
            throw new NullPointerException();
        }

        this.arguments.add(argument);
    }

    /**
     * ���̌Ăяo���̎�������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param arguments �ǉ����������
     */
    public final void addArguments(final List<ExpressionInfo> arguments) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == arguments) {
            throw new NullPointerException();
        }

        this.arguments.addAll(arguments);
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
     * @return�@���̌Ăяo���̎�������List
     */
    public List<ExpressionInfo> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    /**
     * ���̌Ăяo���ɂ�����ϐ��g�p�Q��Ԃ�
     * 
     * @return ���̌Ăяo���ɂ�����ϐ��g�p�Q
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        for (final ExpressionInfo parameter : this.getArguments()) {
            variableUsages.addAll(parameter.getVariableUsages());
        }
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    private final List<ExpressionInfo> arguments;

    private final List<ReferenceTypeInfo> typeArguments;
}
