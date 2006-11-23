package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author y-higo
 * 
 * �Ώۃt�@�C�����i�[���邽�߂̃N���X�D TargetFile ��v�f�Ƃ��Ď��D
 * 
 * since 2006.11.12
 */
public final class TargetFileManager implements Iterable<TargetFile> {

    /**
     * 
     * @return �Ώۃt�@�C�����i�[���Ă��� Set ��Ԃ��D
     * 
     * �V���O���g���p�^�[����p���Ď������Ă���D
     */
    public static TargetFileManager getInstance() {
        if (TARGET_FILE_DATA == null) {
            TARGET_FILE_DATA = new TargetFileManager();
        }
        return TARGET_FILE_DATA;
    }

    /**
     * 
     * @param targetFile �ǉ�����Ώۃt�@�C�� (TargetFile)
     */
    public void add(final TargetFile targetFile) {
        
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == targetFile) {
            throw new NullPointerException();
        }
        
        this.targetFiles.add(targetFile);
    }

    public Iterator<TargetFile> iterator() {
        Set<TargetFile> unmodifiableTargetFiles = Collections.unmodifiableSet(this.targetFiles);
        return unmodifiableTargetFiles.iterator();
    }

    /**
     * 
     * �R���X�g���N�^�D �V���O���g���p�^�[���Ŏ������Ă��邽�߂� private �����Ă���
     * �ȑO�� HashSet ��p���Ă������C�����f�B���N�g���̃t�@�C���͂܂Ƃ߂ĕԂ��ق����悢�̂ŁCTreeSet �ɕύX�����D
     */
    private TargetFileManager() {
        MetricsToolSecurityManager.getInstance().checkAccess();        
        this.targetFiles = new TreeSet<TargetFile>();
    }

    /**
     * 
     * �V���O���g���p�^�[�����������邽�߂̕ϐ��D
     */
    private static TargetFileManager TARGET_FILE_DATA = null;

    /**
     * 
     * �Ώۃt�@�C�� (TargetFile) ���i�[����ϐ��D
     */
    private final Set<TargetFile> targetFiles;
}
