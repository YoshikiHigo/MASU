package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �O���̃��j�b�g�����݂��邱�Ƃ�\���C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface HavingOuterUnit {

    /**
     * �O���̃��j�b�g��Ԃ�
     * 
     * @return �O���̃��j�b�g
     */
    UnitInfo getOuterUnit();
    
    /**
     * �O���̃��j�b�g��ݒ肷��
     * 
     * @param outerUnit �O���̃��j�b�g
     */
    void setOuterUnit(UnitInfo outerUnit);
}
