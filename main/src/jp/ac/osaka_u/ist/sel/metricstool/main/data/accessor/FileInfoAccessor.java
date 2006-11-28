package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;


/**
 * ���̃C���^�[�t�F�[�X�́C�t�@�C�������擾���邽�߂̃��\�b�h�S��񋟂���D
 * 
 * @author y-higo
 *
 */
public interface FileInfoAccessor {

    /**
     * �Ώۃt�@�C���̃C�e���[�^��Ԃ����\�b�h�D
     * 
     * @return �Ώۃt�@�C���̃C�e���[�^
     */
    public Iterator<FileInfo> fileInfoIterator();

    /**
     * �Ώۃt�@�C���̐���Ԃ����\�b�h.
     * @return �Ώۃt�@�C���̐�
     */
    public int getFileCount();
}
