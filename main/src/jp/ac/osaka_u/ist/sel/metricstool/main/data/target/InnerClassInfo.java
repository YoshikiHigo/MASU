package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public interface InnerClassInfo<T extends ClassInfo<?, ?, ?, ?>> {

    /**
     * �O���̃��j�b�g��Ԃ�
     * 
     * @return �O���̃��j�b�g
     */
    UnitInfo getOuterUnit();

    /**
     * �O���̃N���X��Ԃ�.
     * �܂�CgetOuterUnit �̕Ԃ�l��ClassInfo�ł���ꍇ�́C���̃I�u�W�F�N�g��Ԃ��C
     * �Ԃ�l���CMethodInfo�ł���ꍇ�́C���̃I�u�W�F�N�g�� ownerClass ��Ԃ��D
     * 
     * @return�@�O���̃N���X
     */
    T getOuterClass();
}
