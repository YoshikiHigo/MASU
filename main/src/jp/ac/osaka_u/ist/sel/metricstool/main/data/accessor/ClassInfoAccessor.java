package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;


/**
 * ���̃C���^�[�t�F�[�X�́C�N���X�����擾���邽�߂̃��\�b�h�S��񋟂���D
 * 
 * @author y-higo
 *
 */
public interface ClassInfoAccessor extends Iterable<ClassInfo> {

    /**
     * �ΏۃN���X�̃C�e���[�^��Ԃ����\�b�h�D
     * 
     * @return �ΏۃN���X�̃C�e���[�^
     */
    public Iterator<ClassInfo> iterator();

    /**
     * �ΏۃN���X�̐���Ԃ����\�b�h.
     * @return �ΏۃN���X�̐�
     */
    public int getClassCount();
}
