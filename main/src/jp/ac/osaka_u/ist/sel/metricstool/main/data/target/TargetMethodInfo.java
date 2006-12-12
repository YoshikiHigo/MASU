package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �Ώۃ��\�b�h�̏���ۗL����N���X�D �ȉ��̏������D
 * <ul>
 * <li>���\�b�h��</li>
 * <li>�C���q</li>
 * <li>�Ԃ�l�̌^</li>
 * <li>�����̃��X�g</li>
 * <li>�s��</li>
 * <li>�R���g���[���O���t�i���΂炭�͖������j</li>
 * <li>���[�J���ϐ�</li>
 * <li>�������Ă���N���X</li>
 * <li>�Ăяo���Ă��郁�\�b�h</li>
 * <li>�Ăяo����Ă��郁�\�b�h</li>
 * <li>�I�[�o�[���C�h���Ă��郁�\�b�h</li>
 * <li>�I�[�o�[���C�h����Ă��郁�\�b�h</li>
 * <li>�Q�Ƃ��Ă���t�B�[���h</li>
 * <li>������Ă���t�B�[���h</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public final class TargetMethodInfo extends MethodInfo {

    /**
     * ���\�b�h�I�u�W�F�N�g������������D �ȉ��̏�񂪈����Ƃ��ė^�����Ȃ���΂Ȃ�Ȃ��D
     * <ul>
     * <li>���\�b�h��</li>
     * <li>�C���q</li>
     * <li>�V�O�l�`��</li>
     * <li>���L���Ă���N���X</li>
     * <li>�R���X�g���N�^���ǂ���</li>
     * </ul>
     * 
     * @param modifier �C���q
     * @param name ���\�b�h��
     * @param returnType �Ԃ�l�̌^�D�R���X�g���N�^�̏ꍇ�́C���̃N���X�̌^��^����D
     * @param ownerClass ���L���Ă���N���X
     * @param constructor �R���X�g���N�^���ǂ����D�R���X�g���N�^�̏ꍇ�� true,�����łȂ��ꍇ�� false�D
     */
    public TargetMethodInfo(final ModifierInfo modifier, final String name,
            final TypeInfo returnType, final ClassInfo ownerClass, final boolean constructor,
            final int loc) {

        super(name, returnType, ownerClass, constructor);

        if (null == modifier) {
            throw new NullPointerException();
        }

        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be 0 or more!");
        }

        this.modifier = modifier;
        this.loc = loc;
        this.localVariables = new TreeSet<LocalVariableInfo>();

        this.referencees = new TreeSet<FieldInfo>();
        this.assignmentees = new TreeSet<FieldInfo>();
    }

    /**
     * ���̃��\�b�h�Œ�`����Ă��郍�[�J���ϐ���ǉ�����D public �錾���Ă��邪�C �v���O�C������̌Ăяo���͂͂����D
     * 
     * @param localVariable �ǉ��������
     */
    public void addLocalVariable(final LocalVariableInfo localVariable) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * ���̃��\�b�h���Q�Ƃ��Ă���ϐ���ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param referencee �ǉ�����Q�Ƃ���Ă���ϐ�
     */
    public void addReferencee(final FieldInfo referencee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencee) {
            throw new NullPointerException();
        }

        this.referencees.add(referencee);
    }

    /**
     * ���̃��\�b�h��������s���Ă���ϐ���ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param assignmentee �ǉ�����������Ă���ϐ�
     */
    public void addAssignmentee(final FieldInfo assignmentee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assignmentee) {
            throw new NullPointerException();
        }

        this.assignmentees.add(assignmentee);
    }

    /**
     * ���̃��\�b�h�Œ�`����Ă��郍�[�J���ϐ��� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h�Œ�`����Ă��郍�[�J���ϐ��� SortedSet
     */
    public SortedSet<LocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSortedSet(this.localVariables);
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
     * ���̃��\�b�h�̍s����Ԃ�
     * 
     * @return ���̃��\�b�h�̍s��
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * ���̃��\�b�h���Q�Ƃ��Ă���t�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h���Q�Ƃ��Ă���t�B�[���h�� SortedSet
     */
    public SortedSet<FieldInfo> getReferencees() {
        return Collections.unmodifiableSortedSet(this.referencees);
    }

    /**
     * ���̃��\�b�h��������Ă���t�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h��������Ă���t�B�[���h�� SortedSet
     */
    public SortedSet<FieldInfo> getAssignmentees() {
        return Collections.unmodifiableSortedSet(this.assignmentees);
    }

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final ModifierInfo modifier;

    /**
     * �s����ۑ����邽�߂̕ϐ�
     */
    private final int loc;

    /**
     * ���̃��\�b�h�̓����Œ�`����Ă��郍�[�J���ϐ�
     */
    private final SortedSet<LocalVariableInfo> localVariables;

    /**
     * �Q�Ƃ��Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<FieldInfo> referencees;

    /**
     * ������Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<FieldInfo> assignmentees;

}
