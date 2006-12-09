package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���\�b�h�����Ǘ�����N���X�D methodInfo ��v�f�Ƃ��Ď��D
 * 
 * @author y-higo
 * 
 */
public final class MethodInfoManager {

    /**
     * ���\�b�h�����Ǘ����Ă���C���X�^���X��Ԃ��D �V���O���g���p�^�[���������Ă���D
     * 
     * @return ���\�b�h�����Ǘ����Ă���C���X�^���X
     */
    public static MethodInfoManager getInstance() {
        return SINGLETON;
    }

    /**
     * 
     * @param methodInfo �ǉ����郁�\�b�h���
     */
    public void add(final TargetMethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        this.targetMethodInfos.add(methodInfo);
    }

    /**
     * 
     * @param methodInfo �ǉ����郁�\�b�h���
     */
    public void add(final ExternalMethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        this.externalMethodInfos.add(methodInfo);
    }
    
    /**
     * �Ώۃ��\�b�h����SortedSet��Ԃ��D
     * 
     * @return �Ώۃ��\�b�h����SortedSet
     */
    public SortedSet<TargetMethodInfo> getTargetMethodInfos() {
        return Collections.unmodifiableSortedSet(this.targetMethodInfos);
    }

    /**
     * �O�����\�b�h����SortedSet��Ԃ��D
     * 
     * @return �O�����\�b�h����SortedSet
     */
    public SortedSet<ExternalMethodInfo> getExternalMethodInfos() {
        return Collections.unmodifiableSortedSet(this.externalMethodInfos);
    }
    
    /**
     * �����Ă���Ώۃ��\�b�h���̌���Ԃ�.
     * 
     * @return �Ώۃ��\�b�h�̌�
     */
    public int getTargetMethodCount() {
        return this.targetMethodInfos.size();
    }

    /**
     * �����Ă���O�����\�b�h���̌���Ԃ�.
     * 
     * @return �O�����\�b�h�̌�
     */
    public int getExternalMethodCount() {
        return this.externalMethodInfos.size();
    }
    
    /**
     * 
     * �R���X�g���N�^�D �V���O���g���p�^�[���Ŏ������Ă��邽�߂� private �����Ă���D
     */
    private MethodInfoManager() {
        this.targetMethodInfos = new TreeSet<TargetMethodInfo>();
        this.externalMethodInfos = new TreeSet<ExternalMethodInfo>();
    }

    /**
     * 
     * �V���O���g���p�^�[�����������邽�߂̕ϐ��D
     */
    private static final MethodInfoManager SINGLETON = new MethodInfoManager();

    /**
     * 
     * �Ώۃ��\�b�h�����i�[����ϐ��D
     */
    private final SortedSet<TargetMethodInfo> targetMethodInfos;
    
    /**
     * 
     * �O�����\�b�h�����i�[����ϐ��D
     */
    private final SortedSet<ExternalMethodInfo> externalMethodInfos;
}
