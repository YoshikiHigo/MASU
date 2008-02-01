package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �N���X�����i�[���邽�߂̒��ۃN���X
 * 
 * @author higo
 * 
 */
public abstract class ClassInfo extends UnitInfo implements Comparable<ClassInfo>, MetricMeasurable {

    /**
     * ���O��Ԗ��ƃN���X������I�u�W�F�N�g�𐶐�����
     * 
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn�@�I���s
     */
    public ClassInfo(final NamespaceInfo namespace, final String className, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == namespace) || (null == className)) {
            throw new NullPointerException();
        }

        this.namespace = namespace;
        this.className = className;
        this.superClasses = new TreeSet<ClassTypeInfo>();
        this.subClasses = new TreeSet<ClassInfo>();
    }

    /**
     * ���S���薼����N���X���I�u�W�F�N�g�𐶐�����
     * 
     * @param fullQualifiedName ���S���薼
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn�@�I���s
     */
    public ClassInfo(final String[] fullQualifiedName, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

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
        this.superClasses = new TreeSet<ClassTypeInfo>();
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

        final NamespaceInfo namespace = this.getNamespace();
        final NamespaceInfo correspondNamespace = classInfo.getNamespace();
        final int namespaceOrder = namespace.compareTo(correspondNamespace);
        if (namespaceOrder != 0) {
            return namespaceOrder;
        }

        final String name = this.getClassName();
        final String correspondName = classInfo.getClassName();
        return name.compareTo(correspondName);

    }

    /**
     * ���g���N�X�v���ΏۂƂ��Ă̖��O��Ԃ�
     * 
     * @return ���g���N�X�v���ΏۂƂ��Ă̖��O
     */
    public final String getMeasuredUnitName() {
        return this.getFullQualifiedName(Settings.getLanguage().getNamespaceDelimiter());
    }

    /**
     * ���̃N���X�ɐe�N���X�i�̌^�j��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param referenceType �ǉ�����e�N���X�̌^
     */
    public void addSuperClass(final ClassTypeInfo referenceType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.superClasses.add(referenceType);
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
    public SortedSet<ClassTypeInfo> getSuperClasses() {
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
     * ���̃N���X�̊��S���薼��Ԃ�
     * 
     * @return ���̃N���X�̊��S���薼
     */
    public final String[] getFullQualifiedName() {

        final String[] namespace = this.getNamespace().getName();
        final String[] fullQualifiedName = new String[namespace.length + 1];
        System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
        fullQualifiedName[fullQualifiedName.length - 1] = this.getClassName();

        return fullQualifiedName;
    }

    /**
     * ���̃N���X�̊��S���薼��Ԃ��D���S���薼�͈����ŗ^����ꂽ������ɂ��A������C�Ԃ����D
     * 
     * @param delimiter ��؂蕶��
     * @return ���̃N���X�̊��S���薼
     */
    public final String getFullQualifiedName(final String delimiter) {

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
     * ���������ǂ����̃`�F�b�N
     * 
     * @return �������ꍇ�� true, �������Ȃ��ꍇ�� false
     */
    @Override
    public final boolean equals(final Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof ClassInfo)) {
            return false;
        }

        final NamespaceInfo namespace = this.getNamespace();
        final NamespaceInfo correspondNamespace = ((ClassInfo) o).getNamespace();
        if (!namespace.equals(correspondNamespace)) {
            return false;
        }

        final String className = this.getClassName();
        final String correspondClassName = ((ClassInfo) o).getClassName();
        return className.equals(correspondClassName);
    }

    /**
     * ���̃N���X�������ŗ^����ꂽ�N���X�̐e�N���X�ł��邩�𔻒肷��
     * 
     * @param classInfo �ΏۃN���X
     * @return ���̃N���X�������ŗ^����ꂽ�N���X�̐e�N���X�ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean isSuperClass(final ClassInfo classInfo) {

        // �����̒��ڂ̐e�N���X�ɑ΂���
        for (final ClassInfo superClass : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            // �ΏۃN���X�̒��ڂ̐e�N���X�����̃N���X�Ɠ������ꍇ�� true ��Ԃ�
            if (this.equals(superClass)) {
                return true;
            }

            // �ΏۃN���X�̐e�N���X�ɑ΂��čċA�I�ɏ����Ctrue ���Ԃ��ꂽ�ꍇ�́C���̃��\�b�h�� true ��Ԃ�
            if (this.isSuperClass(superClass)) {
                return true;
            }
        }

        return false;
    }

    /**
     * ���̃N���X�������ŗ^����ꂽ�N���X�̎q�N���X�ł��邩�𔻒肷��
     * 
     * @param classInfo �ΏۃN���X
     * @return ���̃N���X�������ŗ^����ꂽ�N���X�̎q�N���X�ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean isSubClass(final ClassInfo classInfo) {

        // �����̒��ڂ̎q�N���X�ɑ΂���
        for (final ClassInfo subClassInfo : classInfo.getSubClasses()) {

            // �ΏۃN���X�̒��ڂ̐e�N���X�����̃N���X�Ɠ������ꍇ�� true ��Ԃ�
            if (this.equals(subClassInfo)) {
                return true;
            }

            // �ΏۃN���X�̐e�N���X�ɑ΂��čċA�I�ɏ����Ctrue ���Ԃ��ꂽ�ꍇ�́C���̃��\�b�h�� true ��Ԃ�
            if (this.isSubClass(subClassInfo)) {
                return true;
            }
        }

        return false;
    }

    /**
     * ���̃N���X�������ŗ^����ꂽ�N���X�̃C���i�[�N���X�ł��邩�𔻒肷��
     * 
     * @param classInfo �ΏۃN���X
     * @return ���̃N���X�������ŗ^����ꂽ�N���X�̃C���i�[�N���X�ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean isInnerClass(final ClassInfo classInfo) {

        // �����ŗ^����ꂽ�N���X�� TargetClassInfo �o�Ȃ��ꍇ�� false
        if (!(classInfo instanceof TargetClassInfo)) {
            return false;
        }

        for (final ClassInfo innerClassInfo : ((TargetClassInfo) classInfo).getInnerClasses()) {

            // ���̃N���X�������̒��ڂ̎q�N���X�Ɠ������ꍇ�� true ��Ԃ�
            if (innerClassInfo.equals(this)) {
                return true;
            }

            // ���̃N���X�������̊ԐړI�Ȏq�N���X�ł���ꍇ�� true ��Ԃ�
            if (this.isInnerClass(innerClassInfo)) {
                return true;
            }
        }

        // �q�N���X���ċA�I�ɒ��ׂ����ʁC���̃N���X�ƈ�v����N���X��������Ȃ������̂� false ��Ԃ�
        return false;
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
    private final SortedSet<ClassTypeInfo> superClasses;

    /**
     * ���̃N���X���p�����Ă���N���X�ꗗ��ۑ����邽�߂̕ϐ��D���ڂ̎q�N���X�݂̂�ۗL����D
     */
    private final SortedSet<ClassInfo> subClasses;
}
