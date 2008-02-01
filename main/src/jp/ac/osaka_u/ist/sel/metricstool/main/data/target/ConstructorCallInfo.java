package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �R���X�g���N�^�Ăяo����ۑ�����ϐ�
 * 
 * @author higo
 *
 */
public final class ConstructorCallInfo extends MemberCallInfo {

    /**
     * �Ăяo�����R���X�g���N�^��^���ăI�u�W�F�N�g��������
     * 
     * @param callee �Ăяo�����R���X�g���N�^
     */
    public ConstructorCallInfo(final MethodInfo callee) {
        super(callee);
    }

    /**
     * �R���X�g���N�^�Ăяo���̌^��Ԃ�
     */
    @Override
    public TypeInfo getType() {

        final ClassInfo ownerClass = this.getCallee().getOwnerClass();
        final ReferenceTypeInfo reference = new ReferenceTypeInfo(ownerClass);
        return reference;
    }

}
