package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �N���X�����i�[���邽�߂̒��ۃN���X
 * 
 * @author y-higo
 * 
 */
public abstract class ClassInfo implements TypeInfo, Comparable<ClassInfo> {

    /**
     * ���O��Ԗ��ƃN���X������I�u�W�F�N�g�𐶐�����
     * 
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     * 
     */
    public ClassInfo(final NamespaceInfo namespace,
            final String className) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == namespace) || (null == className)) {
            throw new NullPointerException();
        }

        this.namespace = namespace;
        this.className = className;
        this.superClasses = new TreeSet<ClassInfo>();
        this.subClasses = new TreeSet<ClassInfo>();
    }

    /**
     * ���S���薼����N���X���I�u�W�F�N�g�𐶐�����
     * 
     * @param fullQualifiedName ���S���薼
     */
    public ClassInfo(final String[] fullQualifiedName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fullQualifiedName) {
            throw new NullPointerException();
        }
        if (0 == fullQualifiedName.length) {
            throw new IllegalArgumentException("Full Qualified Name must has at least 1 word!");
        }

        String[] namespace = new String[fullQualifiedName.length - 1];
        System.arraycopy(fullQualifiedName, 0, namespace, 0, fullQualifiedName.length - 1);
        this.namespace = new NamespaceInfo(namespace);
        this.className = fullQualifiedName[fullQualifiedName.length - 1];
        this.superClasses = new TreeSet<ClassInfo>();
        this.subClasses = new TreeSet<ClassInfo>();
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
     * �N���X�I�u�W�F�N�g�̏����֌W���`���郁�\�b�h�D ���݂́C���O��Ԗ�������p���Ă���D���O��Ԗ��������ꍇ�́C�N���X���iString�j�̏����ɂȂ�D
     */
    public final int compareTo(final ClassInfo classInfo) {

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
     * ���̃N���X�̃X�[�p�[�N���X�� SortedSet ��Ԃ��D
     * 
     * @return �X�[�p�[�N���X�� SortedSet
     */
    public SortedSet<ClassInfo> getSuperClasses() {
        return Collections.unmodifiableSortedSet(this.superClasses);
    }

    /**
     * ���̃N���X�̃T�u�N���X�� SortedSet ��Ԃ��D
     * 
     * @return �T�u�N���X�� SortedSet
     */
    public SortedSet<ClassInfo> getSubClasses() {
        return Collections.unmodifiableSortedSet(this.subClasses);
    }

    /**
     * ���̃N���X�̊��S���薼��Ԃ��D���S���薼�͈����ŗ^����ꂽ������ɂ��A������C�Ԃ����D
     * 
     * @param ��؂蕶��
     */
    public final String getFullQualifiedtName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        StringBuffer buffer = new StringBuffer();
        String[] namespace = this.getNamespace().getName();
        for (int i = 0; i < namespace.length; i++) {
            buffer.append(namespace[i]);
            buffer.append(delimiter);
        }
        buffer.append(this.getClassName());
        return buffer.toString();
    }

    /**
     * ���̃N���X�̌^����Ԃ�
     * 
     * @return ���̃N���X�̌^����Ԃ�
     */
    public final String getTypeName() {

        final String delimiter = Settings.getLanguage().getNamespaceDelimiter();
        final StringBuffer buffer = new StringBuffer();
        final String[] namespace = this.getNamespace().getName();
        for (int i = 0; i < namespace.length; i++) {
            buffer.append(namespace[i]);
            buffer.append(delimiter);
        }
        buffer.append(this.getClassName());

        return buffer.toString();
    }

    /**
     * ���������ǂ����̃`�F�b�N
     * 
     * @return �������ꍇ�� true, �������Ȃ��ꍇ�� false
     */
    public final boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (typeInfo instanceof ClassInfo) {

            NamespaceInfo namespace = this.getNamespace();
            NamespaceInfo correspondNamespace = ((ClassInfo) typeInfo).getNamespace();
            if (!namespace.equals(correspondNamespace)) {
                return false;
            } else {
                String className = this.getClassName();
                String correspondClassName = ((ClassInfo) typeInfo).getClassName();
                if (!className.equals(correspondClassName)) {
                    return false;
                } else {
                    return true;
                }
            }

        } else {
            return false;
        }
    }

    /**
     * �N���X����ۑ����邽�߂̕ϐ�
     */
    private final String className;

    /**
     * ���O��Ԗ���ۑ����邽�߂̕ϐ�
     */
    private final NamespaceInfo namespace;

    /**
     * ���̃N���X���p�����Ă���N���X�ꗗ��ۑ����邽�߂̕ϐ��D ���ڂ̐e�N���X�݂̂�ۗL���邪�C���d�p�����l���� Set �ɂ��Ă���D
     */
    private final SortedSet<ClassInfo> superClasses;

    /**
     * ���̃N���X���p�����Ă���N���X�ꗗ��ۑ����邽�߂̕ϐ��D���ڂ̎q�N���X�݂̂�ۗL����D
     */
    private final SortedSet<ClassInfo> subClasses;
}
