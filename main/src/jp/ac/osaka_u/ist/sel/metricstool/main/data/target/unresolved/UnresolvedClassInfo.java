package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * AST�p�[�X�Ŏ擾�����N���X�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D �ȉ��̏�������
 * 
 * <ul>
 * <li>�C���q</li>
 * <li>���������O���</li>
 * <li>�N���X��</li>
 * <li>�s��</li>
 * <li>�������e�N���X���ꗗ</li>
 * <li>�������q�N���X���ꗗ</li>
 * <li>�������C���i�[�N���X�ꗗ</li>
 * <li>��������`���\�b�h�ꗗ</li>
 * <li>��������`�t�B�[���h�ꗗ
 * <li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedClassInfo {

    /**
     * �����Ȃ��R���X�g���N�^
     */
    public UnresolvedClassInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.modifier = null;
        this.namespace = null;
        this.className = null;
        this.loc = 0;

        this.superClasses = new HashSet<UnresolvedTypeInfo>();
        this.innerClasses = new HashSet<UnresolvedClassInfo>();
        this.definedMethods = new HashSet<UnresolvedMethodInfo>();
        this.definedFields = new HashSet<UnresolvedFieldInfo>();
    }

    /**
     * ���̃N���X�ƑΏۃN���X�����������ǂ����𔻒肷��
     * 
     * @param o ��r�ΏۃN���X
     */
    public boolean equals(Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof UnresolvedClassInfo)) {
            return false;
        }

        String[] fullQualifiedName = this.getFullQualifiedName();
        String[] correspondFullQualifiedName = ((UnresolvedClassInfo) o).getFullQualifiedName();

        if (fullQualifiedName.length != correspondFullQualifiedName.length) {
            return false;
        }

        for (int i = 0; i < fullQualifiedName.length; i++) {
            if (!fullQualifiedName[i].equals(correspondFullQualifiedName[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * ���̃N���X�̃n�b�V���R�[�h��Ԃ�
     * 
     * @param ���̃N���X�̃n�b�V���R�[�h
     */
    public int hashCode() {

        StringBuffer buffer = new StringBuffer();
        String[] fullQualifiedName = this.getFullQualifiedName();
        for (int i = 0; i < fullQualifiedName.length; i++) {
            buffer.append(fullQualifiedName[i]);
        }

        return buffer.toString().hashCode();
    }

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public String[] getNamespace() {
        return this.namespace;
    }

    /**
     * �N���X�����擾����
     * 
     * @return �N���X��
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * ���̃N���X�̊��S�C������Ԃ�
     * 
     * @return ���̃N���X�̊��S�C����
     */
    public String[] getFullQualifiedName() {

        String[] namespace = this.getNamespace();
        String[] fullQualifiedName = new String[namespace.length + 1];

        for (int i = 0; i < namespace.length; i++) {
            fullQualifiedName[i] = namespace[i];
        }
        fullQualifiedName[fullQualifiedName.length - 1] = this.getClassName();

        return fullQualifiedName;
    }

    /**
     * �C���q��ۑ�����
     * 
     * @param modidier �C���q
     * 
     */
    public void setModifier(final ModifierInfo modifier) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifier = modifier;
    }

    /**
     * �C���q��Ԃ�
     * 
     * @return �C���q
     */
    public ModifierInfo getModifier() {
        return this.modifier;
    }

    /**
     * ���O��Ԗ���ۑ�����.���O��Ԗ����Ȃ��ꍇ�͒���0�̔z���^���邱�ƁD
     * 
     * @param namespace ���O��Ԗ�
     */
    public void setNamespace(final String[] namespace) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace) {
            throw new NullPointerException();
        }

        this.namespace = namespace;
    }

    /**
     * �N���X����ۑ�����
     * 
     * @param className
     */
    public void setClassName(final String className) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == className) {
            throw new NullPointerException();
        }

        this.className = className;
    }

    /**
     * �s�����擾����
     * 
     * @return �s��
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * �s����ۑ�����
     * 
     * @param loc �s��
     */
    public void setLOC(final int loc) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be o or more!");
        }

        this.loc = loc;
    }

    /**
     * �e�N���X��ǉ�����
     * 
     * @param superClass �e�N���X��
     */
    public void addSuperClass(final UnresolvedTypeInfo superClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superClass) {
            throw new NullPointerException();
        }

        this.superClasses.add(superClass);
    }

    /**
     * �C���i�[�N���X��ǉ�����
     * 
     * @param innerClass �C���i�[�N���X
     */
    public void addInnerClass(final UnresolvedClassInfo innerClass) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * ��`���Ă��郁�\�b�h��ǉ�����
     * 
     * @param definedMethod ��`���Ă��郁�\�b�h
     */
    public void addDefinedMethod(final UnresolvedMethodInfo definedMethod) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * ��`���Ă���t�B�[���h��ǉ�����
     * 
     * @param definedField ��`���Ă���t�B�[���h
     */
    public void addDefinedField(final UnresolvedFieldInfo definedField) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }

        this.definedFields.add(definedField);
    }

    /**
     * �e�N���X���̃Z�b�g��Ԃ�
     * 
     * @return �e�N���X���̃Z�b�g
     */
    public Set<UnresolvedTypeInfo> getSuperClasses() {
        return Collections.unmodifiableSet(this.superClasses);
    }

    /**
     * �C���i�[�N���X�̃Z�b�g��Ԃ�
     * 
     * @return �C���i�[�N���X�̃Z�b�g
     */
    public Set<UnresolvedClassInfo> getInnerClasses() {
        return Collections.unmodifiableSet(this.innerClasses);
    }

    /**
     * ��`���Ă��郁�\�b�h�̃Z�b�g��Ԃ�
     * 
     * @return ��`���Ă��郁�\�b�h�̃Z�b�g
     */
    public Set<UnresolvedMethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSet(this.definedMethods);
    }

    /**
     * ��`���Ă���t�B�[���h�̃Z�b�g
     * 
     * @return ��`���Ă���t�B�[���h�̃Z�b�g
     */
    public Set<UnresolvedFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSet(this.definedFields);
    }

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private ModifierInfo modifier;

    /**
     * ���O��Ԗ���ۑ����邽�߂̕ϐ�
     */
    private String[] namespace;

    /**
     * �N���X����ۑ����邽�߂̕ϐ�
     */
    private String className;

    /**
     * �s����ۑ����邽�߂̕ϐ�
     */
    private int loc;

    /**
     * �e�N���X��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedTypeInfo> superClasses;

    /**
     * �C���i�[�N���X��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedClassInfo> innerClasses;

    /**
     * ��`���Ă��郁�\�b�h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedMethodInfo> definedMethods;

    /**
     * ��`���Ă���t�B�[���h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedFieldInfo> definedFields;
}
