package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�B�[���h�I�u�W�F�N�g��\���N���X�D �ȉ��̏������D
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
 */
public abstract class FieldInfo extends VariableInfo {

    /**
     * �t�B�[���h�I�u�W�F�N�g������������D �t�B�[���h���ƌ^�C��`���Ă���N���X���^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name �t�B�[���h��
     * @param type �t�B�[���h�̌^
     * @param ownerClass �t�B�[���h���`���Ă���N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public FieldInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final ClassInfo ownerClass, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(modifiers, name, type, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
        this.referencers = new TreeSet<TargetMethodInfo>();
        this.assignmenters = new TreeSet<TargetMethodInfo>();
    }

    /**
     * ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h��ǉ�����
     * 
     * @param referencer ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h
     */
    public final void addReferencer(final TargetMethodInfo referencer) {

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
    public final void addAssignmenter(final TargetMethodInfo assignmenter) {

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
    public final int compareTo(final TargetFieldInfo fieldInfo) {

        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        final ClassInfo classInfo = this.getOwnerClass();
        final ClassInfo correspondClassInfo = this.getOwnerClass();
        final int classOrder = classInfo.compareTo(correspondClassInfo);
        return 0 != classOrder? classOrder : super.compareTo(fieldInfo);
    }

    /**
     * ���̃t�B�[���h���`���Ă���N���X��Ԃ�
     * 
     * @return ���̃t�B�[���h���`���Ă���N���X
     */
    public final ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�� SortedSet
     */
    public final SortedSet<TargetMethodInfo> getReferences() {
        return Collections.unmodifiableSortedSet(this.referencers);
    }

    /**
     * ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�� SortedSet
     */
    public final SortedSet<TargetMethodInfo> getAssignmenters() {
        return Collections.unmodifiableSortedSet(this.assignmenters);
    }

    /**
     * ���̃t�B�[���h���`���Ă���N���X��ۑ�����ϐ�
     */
    protected final ClassInfo ownerClass;

    /**
     * ���̃t�B�[���h���Q�Ƃ��Ă��郁�\�b�h�Q��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<TargetMethodInfo> referencers;

    /**
     * ���̃t�B�[���h�ɑ΂��đ�����s���Ă��郁�\�b�h�Q��ۑ����邽�߂̕ϐ�
     */
    protected final SortedSet<TargetMethodInfo> assignmenters;

}
