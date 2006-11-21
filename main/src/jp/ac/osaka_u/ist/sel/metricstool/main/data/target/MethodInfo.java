package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * ���\�b�h�̏���ۗL����N���X�D �ȉ��̏������D
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
public final class MethodInfo implements Comparable<MethodInfo> {

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
    public MethodInfo(String name, TypeInfo returnType, ClassInfo ownerClass) {
        this.name = name;
        this.ownerClass = ownerClass;
        this.returnType = returnType;

        this.parameters = new LinkedList<ParameterInfo>();
        this.callees = new TreeSet<MethodInfo>();
        this.callers = new TreeSet<MethodInfo>();
        this.overridees = new TreeSet<MethodInfo>();
        this.overriders = new TreeSet<MethodInfo>();
        this.referencees = new TreeSet<FieldInfo>();
        this.assignmentees = new TreeSet<FieldInfo>();
    }

    /**
     * ������ǉ����郁�\�b�h�D public �錾���Ă��邪�C �v���O�C������̌Ăяo���͂͂����D
     * 
     * @param parameter �ǉ��������
     */
    public void addParameter(ParameterInfo parameter) {
        // TODO �v���O�C������̌Ăяo�����͂�������������
        this.parameters.add(parameter);
    }

    /**
     * ���\�b�h�Ԃ̏����֌W���`���郁�\�b�h�D�ȉ��̏����ŏ��������߂�D
     * <ol>
     * <li>���\�b�h���`���Ă���N���X�̖��O��Ԗ�</li>
     * <li>���\�b�h���`���Ă���N���X�̃N���X��</li>
     * <li>���\�b�h��</li>
     * <li>���\�b�h�̈����̌�</li>
     * <li>���\�b�h�̈����̌^�i���������珇�ԂɁj</li>
     */
    public int compareTo(MethodInfo method) {
        // �N���X�I�u�W�F�N�g�� compareTo ��p����D
        // �N���X�̖��O��Ԗ��C�N���X������r�ɗp�����Ă���D
        ClassInfo ownerClass = this.getOwnerClass();
        ClassInfo correspondOwnerClass = method.getOwnerClass();
        final int classOrder = ownerClass.compareTo(correspondOwnerClass);
        if (classOrder != 0) {
            return classOrder;
        } else {

            // ���\�b�h���Ŕ�r
            String name = this.getName();
            String correspondName = method.getName();
            final int methodNameOrder = name.compareTo(correspondName);
            if (methodNameOrder != 0) {
                return methodNameOrder;
            } else {

                // �����̌��Ŕ�r
                final int parameterNumber = this.getParameterNumber();
                final int correspondParameterNumber = method.getParameterNumber();
                if (parameterNumber < correspondParameterNumber) {
                    return 1;
                } else if (parameterNumber > correspondParameterNumber) {
                    return -1;
                } else {

                    // �����̌^�Ŕ�r�D���������珇�ԂɁD
                    Iterator<ParameterInfo> parameterIterator = this.parameterIterator();
                    Iterator<ParameterInfo> correspondParameterIterator = method
                            .parameterIterator();
                    while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                        ParameterInfo parameter = parameterIterator.next();
                        ParameterInfo correspondParameter = correspondParameterIterator.next();
                        String typeName = parameter.getName();
                        String correspondTypeName = correspondParameter.getName();
                        final int typeOrder = typeName.compareTo(correspondTypeName);
                        if (typeOrder != 0) {
                            return typeOrder;
                        }
                    }

                    return 0;
                }

            }
        }
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
     * ���̃��\�b�h�̈����̐���Ԃ�
     * 
     * @return ���̃��\�b�h�̈����̐�
     */
    public int getParameterNumber() {
        return this.parameters.size();
    }

    /**
     * ���̃��\�b�h�̕Ԃ�l�̌^��Ԃ�
     * 
     * @return �Ԃ�l�̌^
     */
    public TypeInfo getReturnType() {
        return this.returnType;
    }

    /**
     * ���̃��\�b�h�̈����� Iterator ��Ԃ��D
     * 
     * @return ���̃��\�b�h�̈����� Iterator
     */
    public Iterator<ParameterInfo> parameterIterator() {
        return this.parameters.iterator();
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
     * 
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> calleeIterator() {
        return this.callees.iterator();
    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� Iterator ��Ԃ��D
     * 
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> callerIterator() {
        return this.callers.iterator();
    }

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� Iterator ��Ԃ��D
     * 
     * @return ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> overrideeIterator() {
        return this.overridees.iterator();
    }

    /**
     * ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� Iterator ��Ԃ��D
     * 
     * @return ���̃��\�b�h���I�[�o�[���C�h���Ă��郁�\�b�h�� Iterator
     */
    public Iterator<MethodInfo> overriderIterator() {
        return this.overriders.iterator();
    }

    /**
     * ���̃��\�b�h���Q�Ƃ��Ă���t�B�[���h�� Iterator ��Ԃ��D
     * 
     * @return ���̃��\�b�h���Q�Ƃ��Ă���t�B�[���h�� Iterator
     */
    public Iterator<FieldInfo> referenceIterator() {
        return this.referencees.iterator();
    }

    /**
     * ���̃��\�b�h��������Ă���t�B�[���h�� Iterator ��Ԃ��D
     * 
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
     * �Ԃ�l�̌^��ۑ����邽�߂̕ϐ�
     */
    private TypeInfo returnType;

    /**
     * �����̃��X�g�̕ۑ����邽�߂̕ϐ�
     */
    private final List<ParameterInfo> parameters;

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
