package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


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
public class TargetClassInfo extends ClassInfo {

    /**
     * ���O��Ԗ��C�N���X����^���ĕ�炷���I�u�W�F�N�g��������

     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     * @param loc �s��
     */
    public TargetClassInfo(final NamespaceInfo namespace, final String className, final int loc) {

        super(namespace, className);

        if (loc < 0) {
            throw new IllegalAccessError("LOC is must be 0 or more!");
        }

        this.loc = loc;
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();
    }

    /**
     * ���S���薼��^���āC�N���X���I�u�W�F�N�g��������
     * 
     * @param fullQualifiedName ���S���薼
     * @param loc �s��
     */
    public TargetClassInfo(final String[] fullQualifiedName, final int loc) {

        super(fullQualifiedName);

        if (loc < 0) {
            throw new IllegalAccessError("LOC is must be 0 or more!");
        }
        
        this.loc = loc;
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();
    }

    /**
     * ���̃N���X�ɃC���i�[�N���X��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param innerClass �ǉ�����C���i�[�N���X
     */
    public void addInnerClass(final TargetInnerClassInfo innerClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
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
     * ���̃N���X�̃C���i�[�N���X�� SortedSet ��Ԃ��D
     * 
     * @return �C���i�[�N���X�� SortedSet
     */
    public SortedSet<TargetInnerClassInfo> getInnerClasses() {
        return Collections.unmodifiableSortedSet(this.innerClasses);
    }

    /**
     * ���̃N���X�ɒ�`���ꂽ���\�b�h����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param definedMethod �ǉ������`���ꂽ���\�b�h
     */
    public void addDefinedMethod(final TargetMethodInfo definedMethod) {
    
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
    public void addDefinedField(final TargetFieldInfo definedField) {
    
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }
    
        this.definedFields.add(definedField);
    }

    /**
     * ���̃N���X�ɒ�`����Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ��`����Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<TargetMethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSortedSet(this.definedMethods);
    }

    /**
     * ���̃N���X�ɒ�`����Ă���t�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return ��`����Ă���t�B�[���h�� SortedSet
     */
    public SortedSet<TargetFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSortedSet(this.definedFields);
    }

    /**
     * �C���q��ۑ�����ϐ�
     */
    // TODO �C���q��ۑ����邽�߂̕ϐ�
    /**
     * �s����ۑ����邽�߂̕ϐ�
     */
    private final int loc;

    /**
     * ���̃N���X�̓����N���X�ꗗ��ۑ����邽�߂̕ϐ��D���ڂ̓����N���X�݂̂�ۗL����D
     */
    private final SortedSet<TargetInnerClassInfo> innerClasses;

    /**
     * ���̃N���X�Œ�`����Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    private final SortedSet<TargetMethodInfo> definedMethods;

    /**
     * ���̃N���X�Œ�`����Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ��D
     */
    private final SortedSet<TargetFieldInfo> definedFields;
}
