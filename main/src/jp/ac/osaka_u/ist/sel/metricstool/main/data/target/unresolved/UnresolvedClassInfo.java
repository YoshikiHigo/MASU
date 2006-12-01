package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ��x�ڂ�AST�p�[�X�Ŏ擾�����N���X�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedClassInfo {

    /**
     * �����Ȃ��R���X�g���N�^
     * 
     */
    public UnresolvedClassInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.namespace = null;
        this.className = null;
        this.loc = 0;

        this.superClasses = new HashSet<String[]>();
        this.innerClasses = new HashSet<String>();
        this.definedMethods = new HashSet<UnresolvedMethodInfo>();
        this.definedFields = new HashSet<UnresolvedFieldInfo>();
    }

    /**
     * ���̃N���X�ƑΏۃN���X�����������ǂ����𔻒肷��
     */
    public boolean equals(Object o) {

        String[] fullQualifiedName = this.getFullQualifiedName();
        String[] correspondFullQualifiedName = ((UnresolvedClassInfo) o).getFullQualifiedName();

        if (fullQualifiedName.length != correspondFullQualifiedName.length) {
            return false;
        }

        for (int i = 0; i < fullQualifiedName.length; i++) {
            if (!fullQualifiedName[i].equals(correspondFullQualifiedName[i])){
                return false;
            }
        }
        
        return true;
    }

    /**
     * ���̃N���X�̃n�b�V���R�[�h��Ԃ�
     */
    public int hashCode(){
        
        StringBuffer buffer = new StringBuffer();
        String[] fullQualifiedName = this.getFullQualifiedName();
        for (int i = 0 ; i < fullQualifiedName.length ; i++){
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
    public String[] getClassName() {
        return this.className;
    }

    /**
     * ���̃N���X�̊��S�C������Ԃ�
     * 
     * @return ���̃N���X�̊��S�C����
     */
    public String[] getFullQualifiedName() {

        String[] namespace = this.getNamespace();
        String[] className = this.getClassName();

        int length = namespace.length + className.length;
        String[] fullQualifiedName = new String[length];

        for (int i = 0; i < length; i++) {
            if (i < namespace.length) {
                fullQualifiedName[i] = namespace[i];
            } else {
                fullQualifiedName[i] = className[i - namespace.length];
            }
        }

        return fullQualifiedName;
    }

    /**
     * ���O��Ԗ���ۑ�����.���O��Ԗ����Ȃ��ꍇ�͒���0�̔z���^���邱�ƁD
     * 
     * @param namespace ���O��Ԗ�
     */
    public void setNamespace(final String[] namespace) {

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
    public void setClassName(final String[] className) {

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
        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be o or more!");
        }
    }

    /**
     * �e�N���X��ǉ�����
     * 
     * @param superClass �e�N���X��
     */
    public void addSuperClass(final String[] superClass) {

        if (null == superClass) {
            throw new NullPointerException();
        }

        this.superClasses.add(superClass);
    }

    /**
     * �C���i�[�N���X��ǉ�����
     * 
     * @param innerClass �C���i�[�N���X��
     */
    public void addInnerClass(final String innerClass) {

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
    public Set<String[]> getSuperClasses() {
        return Collections.unmodifiableSet(this.superClasses);
    }

    /**
     * �C���i�[�N���X���̃Z�b�g��Ԃ�
     * 
     * @return �C���i�[�N���X���̃Z�b�g
     */
    public Set<String> getInnerClasses() {
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
     * ���O��Ԗ���ۑ����邽�߂̕ϐ�
     */
    private String[] namespace;

    /**
     * �N���X����ۑ����邽�߂̕ϐ�
     */
    private String[] className;

    /**
     * �s����ۑ����邽�߂̕ϐ�
     */
    private int loc;

    /**
     * �e�N���X��ۑ����邽�߂̃Z�b�g
     */
    private final Set<String[]> superClasses;

    /**
     * �C���i�[�N���X��ۑ����邽�߂̃Z�b�g
     */
    private final Set<String> innerClasses;

    /**
     * ��`���Ă��郁�\�b�h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedMethodInfo> definedMethods;

    /**
     * ��`���Ă���t�B�[���h��ۑ����邽�߂̃Z�b�g
     */
    private final Set<UnresolvedFieldInfo> definedFields;
}
