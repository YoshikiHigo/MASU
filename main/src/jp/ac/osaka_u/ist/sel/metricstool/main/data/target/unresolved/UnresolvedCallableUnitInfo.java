package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������̌Ăяo�����P�ʂ̃u���b�N�i���\�b�h��R���X�g���N�^�j��\���N���X
 * 
 * @author higo
 * @param <T> �����ς݂̌^
 */
public abstract class UnresolvedCallableUnitInfo<T extends CallableUnitInfo> extends
        UnresolvedLocalSpaceInfo<T> implements VisualizableSetting, StaticOrInstanceSetting, ModifierSetting {

    protected UnresolvedCallableUnitInfo(final UnresolvedClassInfo ownerClass) {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.parameters = new LinkedList<UnresolvedParameterInfo>();
        this.thrownExceptions = new LinkedList<UnresolvedClassTypeInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
    }

    protected UnresolvedCallableUnitInfo(final UnresolvedClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        this(ownerClass);

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * �R���X�g���N�^�Ɉ�����ǉ�����
     * 
     * @param parameterInfo �ǉ��������
     */
    public final void addParameter(final UnresolvedParameterInfo parameterInfo) {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameterInfo) {
            throw new NullPointerException();
        }

        this.parameters.add(parameterInfo);
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �C���q��ǉ�����
     * 
     * @param modifier �ǉ�����C���q
     */
    public final void addModifier(final ModifierInfo modifier) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * �R���X�g���N�^�̈����̃��X�g��Ԃ�
     * 
     * @return ���\�b�h�̈����̃��X�g
     */
    public final List<UnresolvedParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * �������^�p�����[�^�� List ��Ԃ�
     * 
     * @return �������^�p�����[�^�� List
     */
    public final List<UnresolvedTypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * �������^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ����関�����^�p�����[�^
     */
    public final void addTypeParameter(final UnresolvedTypeParameterInfo typeParameter) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * ��������O�� List ��Ԃ�
     * 
     * @return ��������O�� List
    */
    public final List<UnresolvedClassTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableList(this.thrownExceptions);
    }

    /**
      * ��������O��ǉ�����
      * 
      * @param typeParameter �ǉ����関������O
      */
    public final void addTypeParameter(final UnresolvedClassTypeInfo thrownException) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == thrownException) {
            throw new NullPointerException();
        }

        this.thrownExceptions.add(thrownException);
    }

    /**
     * ���̃��\�b�h���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃��\�b�h���`���Ă���N���X
     */
    public final UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param inheritanceVisible �q�N���X����Q�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final void setInheritanceVisible(final boolean inheritanceVisible) {
        this.inheritanceVisible = inheritanceVisible;
    }

    /**
     * �������O��ԓ�����Q�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final void setNamespaceVisible(final boolean namespaceVisible) {
        this.namespaceVisible = namespaceVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param privateVisible �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final void setPrivateVibible(final boolean privateVisible) {
        this.privateVisible = privateVisible;
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param publicVisible �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final void setPublicVisible(final boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * ���̃��\�b�h���`���Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedClassInfo ownerClass;

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private boolean privateVisible;

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private boolean namespaceVisible;

    /**
     * �q�N���X����Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private boolean inheritanceVisible;

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private boolean publicVisible;

    /**
     * �������^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeParameterInfo> typeParameters;

    /**
     * �R���X�g���N�^������ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedParameterInfo> parameters;

    /**
     * throw������O��ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedClassTypeInfo> thrownExceptions;

    /**
     * �C���q��ۑ�����
     */
    private Set<ModifierInfo> modifiers;

}
