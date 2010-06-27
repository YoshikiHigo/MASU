package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author higo
 * 
 * �Ώۃt�@�C�����i�[���邽�߂̃N���X�D TargetFile ��v�f�Ƃ��Ď��D
 * 
 * since 2006.11.12
 */
public final class TargetFileManager implements Iterable<TargetFile> {

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
     * �Ώۃt�@�C���̐���Ԃ�
     * 
     * @return �Ώۃt�@�C���̐�
     */
    public int size() {
        return this.targetFiles.size();
    }

    /**
     * �Ώۃt�@�C�����N���A
     */
    public void clear() {
        this.targetFiles.clear();
    }

    /**
     * �o�^����Ă���Ώۃt�@�C����SortedSet��Ԃ�
     * 
     * @return �o�^����Ă���Ώۃt�@�C����SortedSet
     */
    public SortedSet<TargetFile> getFiles() {
        return Collections.unmodifiableSortedSet(this.targetFiles);
    }

    /**
     * 
     * �R���X�g���N�^�D 
     * �ȑO�� HashSet ��p���Ă������C�����f�B���N�g���̃t�@�C���͂܂Ƃ߂ĕԂ��ق����悢�̂ŁCTreeSet �ɕύX�����D
     */
    public TargetFileManager() {
        this.targetFiles = new TreeSet<TargetFile>();
    }

    /**
     * 
     * �Ώۃt�@�C�� (TargetFile) ���i�[����ϐ��D
     */
    private final SortedSet<TargetFile> targetFiles;
}
