package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
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
public final class TargetMethodInfo extends MethodInfo implements Visualizable, Member, Position {

    /**
     * ���\�b�h�I�u�W�F�N�g������������D �ȉ��̏�񂪈����Ƃ��ė^�����Ȃ���΂Ȃ�Ȃ��D
     * <ul>
     * <li>���\�b�h��</li>
     * <li>�V�O�l�`��</li>
     * <li>���L���Ă���N���X</li>
     * <li>�R���X�g���N�^���ǂ���</li>
     * <li>�s��</li>
     * </ul>
     * 
     * @param modifier �C���q
     * @param name ���\�b�h��
     * @param returnType �Ԃ�l�̌^�D�R���X�g���N�^�̏ꍇ�́C���̃N���X�̌^��^����D
     * @param ownerClass ���L���Ă���N���X
     * @param constructor �R���X�g���N�^���ǂ����D�R���X�g���N�^�̏ꍇ�� true,�����łȂ��ꍇ�� false�D
     * @param loc ���\�b�h�̍s��
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetMethodInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo returnType, final ClassInfo ownerClass, final boolean constructor,
            final int loc, final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(name, returnType, ownerClass, constructor);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be 0 or more!");
        }

        this.loc = loc;
        this.modifiers = new HashSet<ModifierInfo>();
        this.localVariables = new TreeSet<LocalVariableInfo>();
        this.referencees = new TreeSet<FieldInfo>();
        this.assignmentees = new TreeSet<FieldInfo>();
        this.unresolvedUsage = new HashSet<UnresolvedTypeInfo>();

        this.modifiers.addAll(modifiers);

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.instance = instance;
        
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
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
     * ���̃��\�b�h���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo����ǉ�����D �v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param unresolvedType ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo��
     */
    public void addUnresolvedUsage(final UnresolvedTypeInfo unresolvedType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == unresolvedType) {
            throw new NullPointerException();
        }

        this.unresolvedUsage.add(unresolvedType);
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
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
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
     * ���̃��\�b�h���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set ��Ԃ��D
     * 
     * @return ���̃��\�b�h���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set
     */
    public Set<UnresolvedTypeInfo> getUnresolvedUsages() {
        return Collections.unmodifiableSet(this.unresolvedUsage);
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isInheritanceVisible() {
        return this.privateVisible;
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isPrivateVisible() {
        return this.inheritanceVisible;
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    public boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public int getToLine() {
        return this.toLine;
    }

    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public int getToColumn() {
        return this.toColumn;
    }

    /**
     * �s����ۑ����邽�߂̕ϐ�
     */
    private final int loc;

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final Set<ModifierInfo> modifiers;

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

    /**
     * ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���Ȃǂ�ۑ����邽�߂̕ϐ�
     */
    private final Set<UnresolvedTypeInfo> unresolvedUsage;

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean privateVisible;

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean namespaceVisible;

    /**
     * �q�N���X����Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean inheritanceVisible;

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ����ۑ����邽�߂̕ϐ�
     */
    private final boolean publicVisible;

    /**
     * �C���X�^���X�����o�[���ǂ�����ۑ����邽�߂̕ϐ�
     */
    private final boolean instance;

    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private final int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private final int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private final int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private final int toColumn;
}
