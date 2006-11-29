package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�B�[���h�̏��������N���X�D �ȉ��̏������D
 * <ul>
 * <li>�t�B�[���h��</li>
 * <li>�t�B�[���h�̌^</li>
 * <li>�t�B�[���h�̏C���q</li>
 * <li>�t�B�[���h���`���Ă���N���X</li>
 * <li>�t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�Q</li>
 * <li>�t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�Q</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public final class FieldInfo extends VariableInfo {

    /**
     * �t�B�[���h�I�u�W�F�N�g������������D �t�B�[���h���ƌ^�C��`���Ă���N���X���^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name �t�B�[���h��
     * @param type �t�B�[���h�̌^
     * @param ownerClass �t�B�[���h���`���Ă���N���X
     */
    public FieldInfo(final String name, final TypeInfo type, final ClassInfo ownerClass) {

        super(name, type);

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
        this.referencers = new TreeSet<MethodInfo>();
        this.assignmenters = new TreeSet<MethodInfo>();
    }

    /**
     * ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h��ǉ�����
     * 
     * @param referencer ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h
     */
    public void addReferencer(final MethodInfo referencer) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencer) {
            throw new NullPointerException();
        }

        this.referencers.add(referencer);
    }

    /**
     * ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h��ǉ�����
     * 
     * @param assignmenter ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h
     */
    public void addAssignmenter(final MethodInfo assignmenter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assignmenter) {
            throw new NullPointerException();
        }

        this.assignmenters.add(assignmenter);
    }

    /**
     * �t�B�[���h�I�u�W�F�N�g�̏������`���郁�\�b�h�D���̃t�B�[���h���`���Ă���N���X�̏����ɏ]���D�����N���X���ɒ�`����Ă���ꍇ�́C
     * 
     * @return �t�B�[���h�̏����֌W
     */
    public int compareTo(final FieldInfo fieldInfo) {

        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        ClassInfo classInfo = this.getOwnerClass();
        ClassInfo correspondClassInfo = this.getOwnerClass();
        int classOrder = classInfo.compareTo(correspondClassInfo);
        if (classOrder != 0) {
            return classOrder;
        } else {
            return super.compareTo(fieldInfo);
        }
    }

    /**
     * ���̃t�B�[���h���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃t�B�[���h���`���Ă���N���X
     */
    public ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<MethodInfo> getReferences() {
        return Collections.unmodifiableSortedSet(this.referencers);
    }

    /**
     * ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<MethodInfo> getAssignmenters() {
        return Collections.unmodifiableSortedSet(this.assignmenters);
    }

    /**
     * ���̃t�B�[���h���`���Ă���N���X��ۑ�����ϐ�
     */
    private final ClassInfo ownerClass;

    /**
     * ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�Q��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<MethodInfo> referencers;

    /**
     * ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�Q��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<MethodInfo> assignmenters;

    /**
     * �t�B�[���h�̏C���q��\���ϐ�
     */
    // TODO �C���q��\���ϐ����`����D
}
