package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
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
     */
    public ExternalMethodInfo(final String methodName, final ClassInfo ownerClass) {

        super(new HashSet<ModifierInfo>(), methodName, ownerClass, false, true, true, true, 0, 0,
                0, 0);

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������D
     * ��`���Ă���N���X���s���ȏꍇ�ɗp����R���X�g���N�^
     * 
     * @param methodName ���\�b�h��
     */
    public ExternalMethodInfo(final String methodName) {

        super(new HashSet<ModifierInfo>(), methodName, ExternalClassInfo.UNKNOWN, false, true,
                true, true, 0, 0, 0, 0);
        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * ExternalMethodInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<CallInfo> getCalls() {
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
    public SortedSet<StatementInfo> getStatements() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getFromLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getFromColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getToLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getToColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isInheritanceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isNamespaceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isPrivateVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isPublicVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        throw new CannotUseException();
    }
}
