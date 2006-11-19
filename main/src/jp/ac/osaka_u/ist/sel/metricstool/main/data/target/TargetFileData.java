package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * 
 * @author y-higo
 * 
 * �Ώۃt�@�C�����i�[���邽�߂̃N���X�D TargetFile ��v�f�Ƃ��Ď��D
 * 
 * since 2006.11.12
 */
public class TargetFileData implements Iterable<TargetFile> {

    /**
     * 
     * @return �Ώۃt�@�C�����i�[���Ă��� Set ��Ԃ��D
     * 
     * �V���O���g���p�^�[����p���Ď������Ă���D
     */
    public static TargetFileData getInstance() {
        if (TARGET_FILE_DATA == null) {
            TARGET_FILE_DATA = new TargetFileData();
        }
        return TARGET_FILE_DATA;
    }

    /**
     * 
     * @param targetFile �ǉ�����Ώۃt�@�C�� (TargetFile)
     */
    public void add(final TargetFile targetFile) {
        this.targetFiles.add(targetFile);
    }

    public Iterator<TargetFile> iterator() {
        return this.targetFiles.iterator();
    }

    /**
     * 
     * �R���X�g���N�^�D �V���O���g���p�^�[���Ŏ������Ă��邽�߂� private �����Ă���
     * �ȑO�� HashSet ��p���Ă������C�����f�B���N�g���̃t�@�C���͂܂Ƃ߂ĕԂ��ق����悢�̂ŁCTreeSet �ɕύX�����D
     */
    private TargetFileData() {
        /*this.targetFiles = new HashSet<TargetFile>();*/
        this.targetFiles = new TreeSet<TargetFile>();
    }

    /**
     * 
     * �V���O���g���p�^�[�����������邽�߂̕ϐ��D
     */
    private static TargetFileData TARGET_FILE_DATA = null;

    /**
     * 
     * �Ώۃt�@�C�� (TargetFile) ���i�[����ϐ��D
     */
    private final Set<TargetFile> targetFiles;
}
