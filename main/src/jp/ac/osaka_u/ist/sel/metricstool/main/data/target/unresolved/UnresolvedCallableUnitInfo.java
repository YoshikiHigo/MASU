package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class UnresolvedCallableUnitInfo<T extends CallableUnitInfo> extends
        UnresolvedLocalSpaceInfo<T> implements VisualizableSetting {

    UnresolvedCallableUnitInfo() {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.parameters = new LinkedList<UnresolvedParameterInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
    }

    UnresolvedCallableUnitInfo(final UnresolvedClassInfo ownerClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.parameters = new LinkedList<UnresolvedParameterInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
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
     * ���̃��\�b�h���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃��\�b�h���`���Ă���N���X
     */
    public final UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���\�b�h���`���Ă���N���X���Z�b�g����
     * 
     * @param ownerClass ���\�b�h���`���Ă���N���X
     */
    public final void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
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
    private UnresolvedClassInfo ownerClass;

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
     * �C���q��ۑ�����
     */
    private Set<ModifierInfo> modifiers;

}
