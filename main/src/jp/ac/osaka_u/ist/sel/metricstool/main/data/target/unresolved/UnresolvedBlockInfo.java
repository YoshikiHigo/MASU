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
    public UnresolvedBlockInfo() {
        super();
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
     * �����ς݃u���b�N����ۑ����邽�߂̕ϐ�
     */
    protected T resolvedInfo;
}
