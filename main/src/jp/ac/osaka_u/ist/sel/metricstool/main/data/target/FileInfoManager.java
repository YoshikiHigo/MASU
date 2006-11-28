package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�@�C�������Ǘ�����N���X�D FileInfo ��v�f�Ƃ��Ď��D
 * 
 * @author y-higo
 * 
 */
public final class FileInfoManager implements Iterable<FileInfo> {

    /**
     * �t�@�C�������Ǘ����Ă���C���X�^���X��Ԃ��D �V���O���g���p�^�[���������Ă���D
     * 
     * @return �t�@�C�������Ǘ����Ă���C���X�^���X
     */
    public static FileInfoManager getInstance() {
        return SINGLETON;
    }

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
     * �t�@�C������ Iterator ��Ԃ��D���� Iterator �� unmodifiable �ł���C�ύX������s�����Ƃ͂ł��Ȃ��D
     */
    public Iterator<FileInfo> iterator() {
        SortedSet<FileInfo> unmodifiableFileInfos = Collections
                .unmodifiableSortedSet(this.fileInfos);
        return unmodifiableFileInfos.iterator();
    }

    /**
     * ���������Ă���t�@�C���̌���Ԃ�
     * @return�@�t�@�C���̌�
     */
    public int getFileCount() {
        return this.fileInfos.size();
    }

    /**
     * 
     * �R���X�g���N�^�D �V���O���g���p�^�[���Ŏ������Ă��邽�߂� private �����Ă���D
     */
    private FileInfoManager() {
        this.fileInfos = new TreeSet<FileInfo>();
    }

    /**
     * 
     * �V���O���g���p�^�[�����������邽�߂̕ϐ��D
     */
    private static final FileInfoManager SINGLETON = new FileInfoManager();

    /**
     * 
     * �t�@�C����� (FileInfo) ���i�[����ϐ��D
     */
    private final SortedSet<FileInfo> fileInfos;
}
