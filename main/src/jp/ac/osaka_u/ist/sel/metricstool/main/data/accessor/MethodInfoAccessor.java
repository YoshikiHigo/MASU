package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;


/**
 * ���̃C���^�[�t�F�[�X�́C���\�b�h�����擾���邽�߂̃��\�b�h�S��񋟂���D
 * 
 * @author y-higo
 *
 */
public interface MethodInfoAccessor extends Iterable<MethodInfo> {

    /**
     * �Ώۃ��\�b�h�̃C�e���[�^��Ԃ����\�b�h�D
     * 
     * @return �Ώۃ��\�b�h�̃C�e���[�^
     */
    public Iterator<MethodInfo> iterator();

    /**
     * �Ώۃ��\�b�h�̂̐���Ԃ����\�b�h.
     * @return �Ώۃ��\�b�h�̐�
     */
    public int getMethodCount();
}
