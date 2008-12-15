package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * ���\�b�h�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
public final class MethodCallInfo extends CallInfo {

    /**
     * �Ăяo����郁�\�b�h��^���ăI�u�W�F�N�g��������
     * 
     * @param ownerType ���\�b�h�Ăяo���̐e�̌^
     * @param ownerExpression ���\�b�h�Ăяo���̐e�G���e�B�e�B
     * @param callee �Ăяo����Ă��郁�\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public MethodCallInfo(final TypeInfo ownerType, final ExpressionInfo ownerExpression,
            final MethodInfo callee, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if ((null == ownerType) || (null == callee) || (null == ownerExpression)) {
            throw new NullPointerException();
        }

        this.ownerType = ownerType;
        this.ownerExpression = ownerExpression;
        this.callee = callee;
    }

    /**
     * ���̃��\�b�h�Ăяo���̌^��Ԃ�
     */
    @Override
    public TypeInfo getType() {

        final MethodInfo callee = this.getCallee();
        final TypeInfo definitionType = callee.getReturnType();

        // ��`�̕Ԃ�l���^�p�����[�^�łȂ���΂��̂܂ܕԂ���
        if (!(definitionType instanceof TypeParameterInfo)) {
            return definitionType;
        }

        //�@�^�p�����[�^�̏ꍇ
        final ClassTypeInfo callOwnerType = (ClassTypeInfo) this.getOwnerType();
        final List<TypeInfo> typeArguments = callOwnerType.getTypeArguments();

        // �^����������ꍇ�́C���̌^��Ԃ�
        if (0 < typeArguments.size()) {
            final int typeParameterIndex = ((TypeParameterInfo) definitionType).getIndex();
            final TypeInfo typeArgument = typeArguments.get(typeParameterIndex);
            return typeArgument;

            // �^�������Ȃ��ꍇ�́C����Ȍ^��Ԃ�
        } else {

            // Java�@�̏ꍇ (�^�p�����[�^��1.5���瓱�����ꂽ)
            if (Settings.getLanguage().equals(LANGUAGE.JAVA15)) {
                final ClassInfo referencedClass = DataManager.getInstance().getClassInfoManager()
                        .getClassInfo(new String[] { "java", "lang", "Object" });
                final TypeInfo classType = new ClassTypeInfo(referencedClass);
                return classType;
            }
        }

        assert false : "Here shouldn't be reached!";
        return null;
    }

    /**
     * ���̃��\�b�h�Ăяo�����������Ă���^��Ԃ�
     * 
     * @return ���̃��\�b�h�Ăяo�����������Ă���^
     */
    public TypeInfo getOwnerType() {
        return this.ownerType;
    }

    /**
     * ���̃��\�b�h�Ăяo���ŌĂяo����Ă��郁�\�b�h��Ԃ�
     * 
     * @return ���̃��\�b�h�Ăяo���ŌĂяo����Ă��郁�\�b�h
     */
    public MethodInfo getCallee() {
        return this.callee;
    }

    /**
     * ���̎��i���\�b�h�Ăяo���j�ɂ�����ϐ����p�̈ꗗ��Ԃ��N���X
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {

        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());

        final ExpressionInfo ownerExpression = this.getOwnerExpression();
        variableUsages.addAll(ownerExpression.getVariableUsages());

        return Collections.unmodifiableSortedSet(variableUsages);
    }

    /**
     * ���̃��\�b�h�Ăяo���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̃��\�b�h�Ăяo���̃e�L�X�g�\���i�^�j��Ԃ�
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo ownerExpression = this.getOwnerExpression();
        sb.append(ownerExpression.getText());

        sb.append(".");

        final MethodInfo method = this.getCallee();
        sb.append(method.getMethodName());

        sb.append("(");

        for (final ExpressionInfo argument : this.getArguments()) {
            sb.append(argument.getText());
            sb.append(",");
        }

        sb.append(")");

        return sb.toString();
    }

    /**
     * ���̃��\�b�h�Ăяo���̐e�C�܂肱�̃��\�b�h�Ăяo�����������Ă���v�f��Ԃ�
     * 
     * @return ���̃��\�b�h�Ăяo���̐e
     */
    public final ExpressionInfo getOwnerExpression() {
        return this.ownerExpression;
    }

    private final TypeInfo ownerType;

    private final MethodInfo callee;

    private final ExpressionInfo ownerExpression;
}
