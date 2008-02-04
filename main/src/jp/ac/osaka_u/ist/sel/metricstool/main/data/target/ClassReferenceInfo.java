package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �N���X�̎Q�Ƃ�\���N���X�D
 * ReferenceTypeInfo�@�́u�Q�ƌ^�v��\���̂ɑ΂��āC
 * ���̃N���X�̓N���X�̎Q�ƂɊւ�����i�Q�ƈʒu�Ȃǁj��\��
 * 
 * @author higo
 *
 */
public final class ClassReferenceInfo extends EntityUsageInfo {

    /**
     * �Q�ƌ^��^���ăI�u�W�F�N�g��������
     * 
     * @param referenceType�@���̃N���X�Q�Ƃ̎Q�ƌ^
     */
    public ClassReferenceInfo(final ClassTypeInfo referenceType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.referenceType = referenceType;
    }

    /**
     * ���̃N���X�Q�Ƃ̎Q�ƌ^��Ԃ�
     * 
     * @return ���̃N���X�Q�Ƃ̎Q�ƌ^
     */
    @Override
    public TypeInfo getType() {
        return this.referenceType;
    }

    /**
     * ���̃N���X�Q�ƂŎQ�Ƃ���Ă���N���X��Ԃ�
     * 
     * @return ���̃N���X�Q�ƂŎQ�Ƃ���Ă���N���X
     */
    public ClassInfo getReferencedClass() {
        return this.referenceType.getReferencedClass();
    }

    /**
     * ���̃N���X�Q�Ƃ̎Q�ƌ^��ۑ�����ϐ�
     */
    private final ClassTypeInfo referenceType;
}
