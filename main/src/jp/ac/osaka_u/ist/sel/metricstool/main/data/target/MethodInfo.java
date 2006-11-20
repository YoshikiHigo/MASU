package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * ���\�b�h�̏���ۗL����N���X�D �ȉ��̏������D
 * <ul>
 * <li>���\�b�h��</li>
 * <li>�C���q</li>
 * <li>�V�O�l�`��</li>
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
public class MethodInfo {

    /**
     * ���\�b�h�I�u�W�F�N�g������������D �ȉ��̏�񂪈����Ƃ��ė^�����Ȃ���΂Ȃ�Ȃ��D
     * <ul>
     * <li>���\�b�h��</li>
     * <li>�C���q</li>
     * <li>�V�O�l�`��</li>
     * <li>���L���Ă���N���X</li>
     * </ul>
     * 
     * @param name ���\�b�h��
     * 
     */
    public MethodInfo(String name, ClassInfo ownerClass) {
        this.name = name;
        this.ownerClass = ownerClass;

        this.callees = new TreeSet<MethodInfo>();
        this.callers = new TreeSet<MethodInfo>();
        this.overridees = new TreeSet<MethodInfo>();
        this.overriders = new TreeSet<MethodInfo>();
        this.referencees = new TreeSet<FieldInfo>();
        this.assignmentees = new TreeSet<FieldInfo>();
    }

    /**
     * ���̃��\�b�h�̖��O��Ԃ�
     * 
     * @return ���\�b�h��
     */
    public String getName() {
        return this.name;
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
     * ���̃��\�b�h���`���Ă���N���X��Ԃ��D
     * 
     * @return ���̃��\�b�h���`���Ă���N���X
     */
    public ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� Iterator ��Ԃ��D
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> calleeIterator() {
        return this.callees.iterator();
    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� Iterator ��Ԃ��D
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> callerIterator() {
        return this.callers.iterator();
    }

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� Iterator ��Ԃ��D
     * @return ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> overrideeIterator() {
        return this.overridees.iterator();
    }

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� Iterator ��Ԃ��D
     * @return ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> overriderIterator() {
        return this.overriders.iterator();
    }
    
    /**
     * ���̃��\�b�h���Q�Ƃ��Ă���t�B�[���h�� Iterator ��Ԃ��D
     * @return ���̃��\�b�h���Q�Ƃ��Ă���t�B�[���h�� Iterator
     */
    public Iterator<FieldInfo> referenceIterator() {
        return this.referencees.iterator();
    }
    
    /**
     * ���̃��\�b�h��������Ă���t�B�[���h�� Iterator ��Ԃ��D
     * @return ���̃��\�b�h��������Ă���t�B�[���h�� Iterator
     */
    public Iterator<FieldInfo> assignmenteeIterator() {
        return this.assignmentees.iterator();
    }
    
    /**
     * ���\�b�h����ۑ����邽�߂̕ϐ�
     */
    private final String name;

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    // TODO �C���q��ۑ����邽�߂̕ϐ����`����
    /**
     * �V�O�l�`����ۑ����邽�߂̕ϐ�
     */
    // TODO �V�O�l�`����ۑ����邽�߂̕ϐ����`����
    /**
     * �s����ۑ����邽�߂̕ϐ�
     */
    private int loc;

    /**
     * ���[�J���ϐ���ۑ����邽�߂̕ϐ�
     */
    // TODO ���[�J���ϐ���ۑ����邽�߂̕ϐ����`����
    /**
     * �������Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private final ClassInfo ownerClass;

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<MethodInfo> callees;

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<MethodInfo> callers;

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<MethodInfo> overridees;

    /**
     * �I�[�o�[���C�h����Ă��郁�\�b�h��ۑ����邽�߂̕ϐ�
     */
    private final Set<MethodInfo> overriders;

    /**
     * �Q�Ƃ��Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<FieldInfo> referencees;

    /**
     * ������Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<FieldInfo> assignmentees;
}
