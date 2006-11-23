package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import sun.reflect.FieldInfo;


/**
 * �N���X�̏���ۗL����N���X�D�ȉ��̏������D
 * <ul>
 * <li>�N���X��</li>
 * <li>�C���q</li>
 * <li>���O��ԁi�p�b�P�[�W���j</li>
 * <li>�s��</li>
 * <li>�p�����Ă���N���X</li>
 * <li>�p������Ă���N���X</li>
 * <li>�Q�Ƃ��Ă���N���X</li>
 * <li>�Q�Ƃ���Ă���N���X</li>
 * <li>�����N���X</li>
 * <li>���̃N���X���Œ�`����Ă��郁�\�b�h</li>
 * <li>���̃N���X���Œ�`����Ă���t�B�[���h</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public final class ClassInfo implements TypeInfo, Comparable<ClassInfo> {

    /**
     * �N���X�I�u�W�F�N�g������������D �ȉ��̏�񂪈����Ƃ��ė^�����Ȃ���΂Ȃ�Ȃ��D
     * <ul>
     * <li>�N���X��</li>
     * <li>�C���q</li>
     * </ul>
     * 
     * @param className �N���X��
     */
    public ClassInfo(final NamespaceInfo namespace, final String className) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == namespace) || (null == className)) {
            throw new NullPointerException();
        }

        this.namespace = namespace;
        this.className = className;
        this.superClasses = new TreeSet<ClassInfo>();
        this.subClasses = new TreeSet<ClassInfo>();
        this.innerClasses = new TreeSet<ClassInfo>();
        this.definedMethods = new TreeSet<MethodInfo>();
        this.definedFields = new TreeSet<FieldInfo>();
    }

    /**
     * ���̃N���X�ɐe�N���X��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param superClass �ǉ�����e�N���X
     */
    public void addSuperClass(final ClassInfo superClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superClass) {
            throw new NullPointerException();
        }

        this.superClasses.add(superClass);
    }

    /**
     * ���̃N���X�Ɏq�N���X��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param subClass �ǉ�����q�N���X
     */
    public void addSubClass(final ClassInfo subClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == subClass) {
            throw new NullPointerException();
        }

        this.subClasses.add(subClass);
    }

    /**
     * ���̃N���X�ɃC���i�[�N���X��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param innerClass �ǉ�����C���i�[�N���X
     */
    public void addInnerClass(final ClassInfo innerClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ���\�b�h����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedMethod �ǉ������`���ꂽ���\�b�h
     */
    public void addDefinedMethod(final MethodInfo definedMethod) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ�t�B�[���h����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedField �ǉ������`���ꂽ�t�B�[���h
     */
    public void addDefinedField(final FieldInfo definedField) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }

        this.definedFields.add(definedField);
    }

    /**
     * �N���X�I�u�W�F�N�g�̏����֌W���`���郁�\�b�h�D ���݂́C���O��Ԗ�������p���Ă���D���O��Ԗ��������ꍇ�́C�N���X���iString�j�̏����ɂȂ�D
     */
    public int compareTo(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        NamespaceInfo namespace = this.getNamespace();
        NamespaceInfo correspondNamespace = classInfo.getNamespace();
        int namespaceOrder = namespace.compareTo(correspondNamespace);
        if (namespaceOrder != 0) {
            return namespaceOrder;
        } else {
            String name = this.getClassName();
            String correspondName = classInfo.getClassName();
            return name.compareTo(correspondName);
        }
    }

    /**
     * ���̃N���X�̃N���X����Ԃ�
     * 
     * @return �N���X��
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * ���̃N���X�̖��O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public NamespaceInfo getNamespace() {
        return this.namespace;
    }

    /**
     * ���̃N���X�̖��O��Ԃ��D �����̖��O�Ƃ́C���O��Ԗ� + �N���X����\���D
     */
    public String getName() {
        NamespaceInfo namespace = this.getNamespace();
        StringBuffer buffer = new StringBuffer();
        buffer.append(namespace.getName());
        buffer.append('.');
        buffer.append(this.getClassName());
        return buffer.toString();
    }

    /**
     * ���̃N���X�̍s����Ԃ�
     * 
     * @return ���̃N���X�̍s��
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * ���̃N���X�̃X�[�p�[�N���X�� Iterator ��Ԃ��D
     * 
     * @return �X�[�p�[�N���X�� Iterator
     */
    public Iterator<ClassInfo> superClassIterator() {
        Set<ClassInfo> unmodifiableSuperClasses = Collections.unmodifiableSet(this.superClasses);
        return unmodifiableSuperClasses.iterator();
    }

    /**
     * ���̃N���X�̃T�u�N���X�� Iterator ��Ԃ��D
     * 
     * @return �T�u�N���X�� Iterator
     */
    public Iterator<ClassInfo> subClassIterator() {
        Set<ClassInfo> unmodifiablesubClasses = Collections.unmodifiableSet(this.subClasses);
        return unmodifiablesubClasses.iterator();
    }

    /**
     * ���̃N���X�̃C���i�[�N���X�� Iterator ��Ԃ��D
     * 
     * @return �C���i�[�N���X�� Iterator
     */
    public Iterator<ClassInfo> innerClassIterator() {
        Set<ClassInfo> unmodifiableInnerClasses = Collections.unmodifiableSet(this.innerClasses);
        return unmodifiableInnerClasses.iterator();
    }

    /**
     * ���̃N���X�ɒ�`����Ă��郁�\�b�h�� Iterator ��Ԃ��D
     * 
     * @return ��`����Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> definedMethodIterator() {
        Set<MethodInfo> unmodifiableDefinedMethods = Collections
                .unmodifiableSet(this.definedMethods);
        return unmodifiableDefinedMethods.iterator();
    }

    /**
     * ���̃N���X�ɒ�`����Ă���t�B�[���h�� Iterator ��Ԃ��D
     * 
     * @return ��`����Ă���t�B�[���h�� Iterator
     */
    public Iterator<FieldInfo> definedFieldIterator() {
        Set<FieldInfo> unmodifiableDefinedFields = Collections.unmodifiableSet(this.definedFields);
        return unmodifiableDefinedFields.iterator();
    }

    /**
     * �N���X����ۑ�����ϐ�
     */
    private final String className;

    /**
     * ���O��Ԗ���ۑ�����ϐ�
     */
    private final NamespaceInfo namespace;

    /**
     * �C���q��ۑ�����ϐ�
     */
    // TODO �C���q��ۑ����邽�߂̕ϐ�
    /**
     * �s����ۑ����邽�߂̕ϐ�
     */
    private int loc;

    /**
     * ���̃N���X���p�����Ă���N���X�ꗗ��ۑ����邽�߂̕ϐ��D ���ڂ̐e�N���X�݂̂�ۗL���邪�C���d�p�����l���� Set �ɂ��Ă���D
     */
    private final Set<ClassInfo> superClasses;

    /**
     * ���̃N���X���p�����Ă���N���X�ꗗ��ۑ����邽�߂̕ϐ��D���ڂ̎q�N���X�݂̂�ۗL����D
     */
    private final Set<ClassInfo> subClasses;

    /**
     * ���̃N���X�̓����N���X�ꗗ��ۑ����邽�߂̕ϐ��D���ڂ̓����N���X�݂̂�ۗL����D
     */
    private final Set<ClassInfo> innerClasses;

    /**
     * ���̃N���X�Œ�`����Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    private final Set<MethodInfo> definedMethods;

    /**
     * ���̃N���X�Œ�`����Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    private final Set<FieldInfo> definedFields;

}
