package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;


/**
 * if����while���Ȃǂ̃��\�b�h���̍\���i�u���b�N�j��\�����߂̃N���X
 * 
 * @author higo
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> extends UnresolvedLocalSpaceInfo<T> {

    /**
     * �u���b�N�\����\���I�u�W�F�N�g������������
     * 
     */
    public UnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> ownerSpace) {
        super();
        
        if(null == ownerSpace) {
            throw new IllegalArgumentException("ownerSpace is null");
        }
        
        this.ownerSpace = ownerSpace;
    }
    
    /**
     * ���̃u���b�N���������Ԃ�Ԃ�
     * @return ���̃u���b�N����������
     */
    public UnresolvedLocalSpaceInfo<?> getOwnerSpace() {
        return this.ownerSpace;
    }

    /**
     * ���̃u���b�N�����ɉ�������Ă��邩�ǂ�����������
     * 
     * @return ���ɉ�������Ă���ꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * ���̃u���b�N�̉����ς݃I�u�W�F�N�g��Ԃ�
     */
    @Override
    public final T getResolvedUnit() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * ���̃u���b�N���������Ԃ�ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedLocalSpaceInfo<?> ownerSpace;
    
    /**
     * �����ς݃u���b�N����ۑ����邽�߂̕ϐ�
     */
    protected T resolvedInfo;
}
