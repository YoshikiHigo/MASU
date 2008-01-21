package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfoManager;


/**
 * �v���O�C���� FileInfo �ɃA�N�Z�X���邽�߂ɗp����C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public class DefaultFileInfoAccessor implements FileInfoAccessor {

    /**
     * FileInfo �̃C�e���[�^��Ԃ��D ���̃C�e���[�^�͎Q�Ɛ�p�ł���ύX�������s�����Ƃ͂ł��Ȃ��D
     * 
     * @return FileInfo �̃C�e���[�^
     */
    public Iterator<FileInfo> iterator() {
        FileInfoManager fileInfoManager = FileInfoManager.getInstance();
        SortedSet<FileInfo> fileInfos = fileInfoManager.getFileInfos();
        return fileInfos.iterator();
    }

    /**
     * �Ώۃt�@�C���̐���Ԃ����\�b�h.
     * @return �Ώۃt�@�C���̐�
     */
    public int getFileCount() {
        return FileInfoManager.getInstance().getFileCount();
    }

}
