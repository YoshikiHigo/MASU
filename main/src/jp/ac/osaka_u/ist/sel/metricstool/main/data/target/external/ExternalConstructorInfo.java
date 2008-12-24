package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;


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
        super(new HashSet<ModifierInfo>(), ownerClass, false, true, true, true, 0, 0, 0, 0);
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
    public SortedSet<StatementInfo> getStatements() {
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

    /**
     * ExternalConstructorInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isInheritanceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isNamespaceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isPrivateVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final boolean isPublicVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public TypeParameterInfo getTypeParameter(int index) {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public List<TypeParameterInfo> getTypeParameters() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public void addTypeParameter(TypeParameterInfo typeParameter) {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public void addTypeParameterUsage(TypeParameterInfo typeParameterInfo, TypeInfo usedType) {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages() {
        throw new CannotUseException();
    }
}
