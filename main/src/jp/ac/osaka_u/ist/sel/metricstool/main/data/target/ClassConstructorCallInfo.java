package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �N���X�̃R���X�g���N�^�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ClassConstructorCallInfo extends ConstructorCallInfo<ClassTypeInfo> {

    /**
     * �^��^���ăR���X�g���N�^�Ăяo����������
     * 
     * @param classType �Ăяo���̌^
     * @param callee �Ăяo����Ă���R���X�g���N�^
     * @param ownerMethod �I�[�i�[���\�b�h 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
     */
    public ClassConstructorCallInfo(final ClassTypeInfo classType, final ConstructorInfo callee,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(classType, callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

    }
}
