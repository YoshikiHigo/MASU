package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;


/**
 * if����while���Ȃǂ̃��\�b�h���̍\���i�u���b�N�j��\�����߂̃N���X
 * 
 * @author higo
 * @param <T> �����ς݂̃u���b�N�̌^
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> extends UnresolvedLocalSpaceInfo<T>
        implements UnresolvedStatementInfo<T> {

    /**
     * ���̃u���b�N�̊O���Ɉʒu����u���b�N��^���āC�I�u�W�F�N�g��������
     * 
     * @param outerSpace ���̃u���b�N�̊O���Ɉʒu����u���b�N
     * 
     */
    public UnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super();

        if (null == outerSpace) {
            throw new IllegalArgumentException("outerSpace is null");
        }

        this.outerSpace = outerSpace;
    }
    
    @Override
    public final int compareTo(UnresolvedStatementInfo<T> o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return -1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return 1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return -1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return 1;
        } else if (this.getToLine() < o.getToLine()) {
            return -1;
        } else if (this.getToLine() > o.getToLine()) {
            return 1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return -1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return 1;
        }

        return 0;
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
