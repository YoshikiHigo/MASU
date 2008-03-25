package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;


/**
 * if����while���Ȃǂ̃��\�b�h���̍\���i�u���b�N�j��\�����߂̃N���X
 * 
 * @author higo
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> extends UnresolvedLocalSpaceInfo<T>
        implements UnresolvedStatementInfo<T> {

    /**
     * �u���b�N�\����\���I�u�W�F�N�g������������
     * 
     */
    public UnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super();

        if (null == outerSpace) {
            throw new IllegalArgumentException("outerSpace is null");
        }

        this.outerSpace = outerSpace;
    }

    /**
     * ���̃u���b�N���������Ԃ�Ԃ�
     * @return ���̃u���b�N����������
     */
    public UnresolvedLocalSpaceInfo<?> getOuterSpace() {
        return this.outerSpace;
    }

    /**
     * ���̃u���b�N���������Ԃ�ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedLocalSpaceInfo<?> outerSpace;

}
