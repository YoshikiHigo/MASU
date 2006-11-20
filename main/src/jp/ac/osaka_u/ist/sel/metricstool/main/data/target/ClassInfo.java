package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
public class ClassInfo implements TypeInfo, Comparable<ClassInfo> {

    /**
     * �N���X�I�u�W�F�N�g������������D �ȉ��̏�񂪈����Ƃ��ė^�����Ȃ���΂Ȃ�Ȃ��D
     * <ul>
     * <li>�N���X��</li>
     * <li>�C���q</li>
     * </ul>
     * 
     * @param className �N���X��
     */
    public ClassInfo(NamespaceInfo namespace, String className) {
        this.namespace = namespace;
        this.className = className;

        this.superClasses = new TreeSet<ClassInfo>();
        this.subClasses = new TreeSet<ClassInfo>();
        this.innerClasses = new TreeSet<ClassInfo>();
        this.definedMethods = new TreeSet<MethodInfo>();
        this.definedFields = new TreeSet<FieldInfo>();
    }

    /**
     * �N���X�I�u�W�F�N�g�̏����֌W���`���郁�\�b�h�D ���݂́C���O��Ԗ�������p���Ă���D���O��Ԗ��������ꍇ�́C�N���X���iString�j�̏����ɂȂ�D
     */
    public int compareTo(ClassInfo classInfo) {
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
     * ���̃N���X�̖��O��Ԗ���Ԃ�
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
     * ���̃N���X�̖��O��Ԃ��D �����̖��O�Ƃ́C���O��Ԗ� + �N���X����\���D
     */
    /**
     * ���̃N���X�̃X�[�p�[�N���X�� Iterator ��Ԃ��D
     * 
     * @return �X�[�p�[�N���X�� Iterator
     */
    public Iterator<ClassInfo> superClassIterator() {
        return this.superClasses.iterator();
    }

    /**
     * ���̃N���X�̃T�u�N���X�� Iterator ��Ԃ��D
     * 
     * @return �T�u�N���X�� Iterator
     */
    public Iterator<ClassInfo> subClassIterator() {
        return this.subClasses.iterator();
    }

    /**
     * ���̃N���X�̃C���i�[�N���X�� Iterator ��Ԃ��D
     * 
     * @return �C���i�[�N���X�� Iterator
     */
    public Iterator<ClassInfo> innerClassIterator() {
        return this.innerClasses.iterator();
    }

    /**
     * ���̃N���X�ɒ�`����Ă��郁�\�b�h�� Iterator ��Ԃ��D
     * 
     * @return ��`����Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> definedMethodIterator() {
        return this.definedMethods.iterator();
    }

    /**
     * ���̃N���X�ɒ�`����Ă���t�B�[���h�� Iterator ��Ԃ��D
     * 
     * @return ��`����Ă���t�B�[���h�� Iterator
     */
    public Iterator<FieldInfo> definedFieldIterator() {
        return this.definedFields.iterator();
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
