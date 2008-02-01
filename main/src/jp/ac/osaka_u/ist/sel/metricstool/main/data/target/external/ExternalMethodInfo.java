package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MemberCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;


/**
 * �O���N���X�ɒ�`����Ă��郁�\�b�h����ۑ����邽�߂̃N���X
 * 
 * @author higo
 */
public final class ExternalMethodInfo extends MethodInfo {

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������
     * 
     * @param methodName ���\�b�h��
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     * @param constructor �R���X�g���N�^���ǂ���
     */
    public ExternalMethodInfo(final String methodName, final ClassInfo ownerClass,
            final boolean constructor) {

        super(methodName, ownerClass, constructor);

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * ExternalMethodInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<MemberCallInfo> getMemberCalls() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public final SortedSet<MethodInfo> getCallees() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public SortedSet<LocalVariableInfo> getLocalVariables() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<FieldUsageInfo> getFieldUsages() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public SortedSet<BlockInfo> getInnerBlocks() {
        throw new CannotUseException();
    }
}
