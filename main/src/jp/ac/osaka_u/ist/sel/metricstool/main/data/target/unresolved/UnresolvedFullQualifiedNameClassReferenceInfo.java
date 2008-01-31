package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

/**
 * �������N���X�Q�Ƃ�\���N���X
 * 
 * @author higo, t-miyake
 *
 */
public final class UnresolvedFullQualifiedNameClassReferenceInfo extends UnresolvedClassReferenceInfo {

    /**
     * ���S���薼���킩���Ă���iUnresolvedClassInfo�̃I�u�W�F�N�g�����݂���j�N���X�̎Q�Ƃ�������
     * 
     * @param referencedClass �Q�Ƃ���Ă���N���X
     */
    public UnresolvedFullQualifiedNameClassReferenceInfo(final UnresolvedClassInfo referencedClass) {
        super(new AvailableNamespaceInfoSet(), referencedClass.getFullQualifiedName());
        this.referencedClass = referencedClass;
    }
    
    /**
     * �Q�Ƃ���Ă���N���X�̏���Ԃ�
     * 
     * @return �Q�Ƃ���Ă���N���X�̏��
     */
    public UnresolvedClassInfo getReferencedClass() {
        return referencedClass;
    }
    
    /**
     * �Q�Ƃ���Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedClassInfo referencedClass;



    
}
