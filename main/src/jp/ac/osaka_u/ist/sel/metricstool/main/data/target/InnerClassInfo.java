package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public interface InnerClassInfo extends HavingOuterUnit {

    /**
     * �O���̃N���X��Ԃ�.
     * 
     * @return�@�O���̃N���X
     */
    ClassInfo getOuterClass();

    /**
     * �O���̌Ăяo���\�ȃ��j�b�g�i���\�b�h�C�R���X�g���N�^���j��Ԃ�
     * 
     * @return �O���̌Ăяo���\�ȃ��j�b�g�i���\�b�h�C�R���X�g���N�^���j
     */
    CallableUnitInfo getOuterCallableUnit();
}
