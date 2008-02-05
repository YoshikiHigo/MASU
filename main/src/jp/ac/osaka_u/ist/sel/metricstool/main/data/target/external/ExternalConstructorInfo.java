package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;


/**
 * �O���N���X�ɒ�`����Ă���R���X�g���N�^����ۑ����邽�߂̃N���X
 * 
 * @author higo
 */
public final class ExternalConstructorInfo extends ConstructorInfo {

    /**
     * �O���N���X�ɒ�`����Ă���R���X�g���N�^�I�u�W�F�N�g������������
     * 
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     */
    public ExternalConstructorInfo(final ClassInfo ownerClass) {
        super(ownerClass, 0, 0, 0, 0);
    }

    /**
     * ExternalConstructorInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<CallInfo> getCalls() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public final SortedSet<MethodInfo> getCallees() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public SortedSet<LocalVariableInfo> getLocalVariables() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<FieldUsageInfo> getFieldUsages() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo�ł͗��p�ł��Ȃ�
     */
    @Override
    public SortedSet<BlockInfo> getInnerBlocks() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getFromLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getFromColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getToLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getToColumn() {
        throw new CannotUseException();
    }
}
