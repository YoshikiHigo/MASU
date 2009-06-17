package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�@�C�������Ǘ�����N���X�D FileInfo ��v�f�Ƃ��Ď��D
 * 
 * @author higo
 * 
 */
public final class FileInfoManager {

    /**
     * 
     * @param fileInfo �ǉ�����N���X���
     */
    public void add(final FileInfo fileInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileInfo) {
            throw new NullPointerException();
        }

        this.fileInfos.add(fileInfo);
    }

    /**
     * ���݉�͒��̃t�@�C������Ԃ�
     * 
     * @return ���݉�͒��̃t�@�C�����D��͂��n�܂��Ă��Ȃ��ꍇ��null�C��͂��I�����Ă���ꍇ�͍Ō�ɉ�͂����t�@�C��
     */
    public FileInfo getCurrentFile() {
        return this.fileInfos.size() > 0 ? this.fileInfos.last() : null;
    }

    /**
     * �t�@�C������ SortedSet ��Ԃ��D
     * 
     * @return �t�@�C������ SortedSet
     */
    public SortedSet<FileInfo> getFileInfos() {
        return Collections.unmodifiableSortedSet(this.fileInfos);
    }

    /**
     * ���������Ă���t�@�C���̌���Ԃ�
     * 
     * @return �t�@�C���̌�
     */
    public int getFileCount() {
        return this.fileInfos.size();
    }

    /**
     * �o�^����Ă���t�@�C���̑��s����Ԃ�
     * 
     * @return �o�^����Ă���t�@�C���̑��s��
     */
    public int getTotalLOC() {
        int loc = 0;
        for (final FileInfo file : this.getFileInfos()) {
            loc += file.getLOC();
        }
        return loc;
    }

    /**
     * �t�@�C�������N���A
     */
    public void clear() {
        this.fileInfos.clear();
    }

    /**
     * 
     * �R���X�g���N�^�D �V���O���g���p�^�[���Ŏ������Ă��邽�߂� private �����Ă���D
     */
    public FileInfoManager() {
        this.fileInfos = new TreeSet<FileInfo>();
    }

    /**
     * 
     * �t�@�C����� (FileInfo) ���i�[����ϐ��D
     */
    private final SortedSet<FileInfo> fileInfos;
}
