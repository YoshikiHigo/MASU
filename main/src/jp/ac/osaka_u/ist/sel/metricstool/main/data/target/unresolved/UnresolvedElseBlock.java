package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ else �u���b�N��\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedElseBlock extends UnresolvedBlock {

    /**
     * �Ή����� if �u���b�N��^���āCelse �u���b�N����������
     * 
     * @param correspondingIfBlock
     */
    UnresolvedElseBlock(final UnresolvedIfBlock correspondingIfBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == correspondingIfBlock) {
            throw new NullPointerException();
        }

        this.correspondingIfBlock = correspondingIfBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��Ԃ�
     * 
     * @return ���� else �u���b�N�ƑΉ����� if �u���b�N
     */
    public UnresolvedIfBlock getCorrespondingIfBlock() {
        return this.correspondingIfBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedIfBlock correspondingIfBlock;
}
