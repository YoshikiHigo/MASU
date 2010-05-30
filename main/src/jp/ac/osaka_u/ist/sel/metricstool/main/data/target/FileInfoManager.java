package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public synchronized void add(final FileInfo fileInfo, final Thread thread) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileInfo || null == thread) {
            throw new IllegalArgumentException();
        }

        this.fileInfos.add(fileInfo);

        List<FileInfo> files = this.threadMap.get(thread);
        if (null == files) {
            files = new ArrayList<FileInfo>();
            this.threadMap.put(thread, files);
        }
        files.add(fileInfo);
    }

    /**
     * ���݉�͒��̃t�@�C������Ԃ�
     * 
     * @return ���݉�͒��̃t�@�C�����D��͂��n�܂��Ă��Ȃ��ꍇ��null�C��͂��I�����Ă���ꍇ�͍Ō�ɉ�͂����t�@�C��
     */
    public FileInfo getCurrentFile(final Thread thread) {
        final List<FileInfo> files = this.threadMap.get(thread);
        return files == null ? null : files.get(files.size() - 1);
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
        this.threadMap = new HashMap<Thread, List<FileInfo>>();
    }

    /**
     * 
     * �t�@�C����� (FileInfo) ���i�[����ϐ��D
     */
    private final SortedSet<FileInfo> fileInfos;

    /**
     * �X���b�h�Ɠo�^���ꂽ�t�@�C���̑Ή��֌W��ۑ����邽�߂̕ϐ�
     */
    private final Map<Thread, List<FileInfo>> threadMap;
}
