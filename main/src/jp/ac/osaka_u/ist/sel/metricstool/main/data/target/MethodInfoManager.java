package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���\�b�h�����Ǘ�����N���X�D methodInfo ��v�f�Ƃ��Ď��D
 * 
 * @author y-higo
 * 
 */
public class MethodInfoManager implements Iterable<MethodInfo> {

    /**
     * ���\�b�h�����Ǘ����Ă���C���X�^���X��Ԃ��D �V���O���g���p�^�[���������Ă���D
     * 
     * @return ���\�b�h�����Ǘ����Ă���C���X�^���X
     */
    public static MethodInfoManager getInstance() {
        if (METHOD_INFO_DATA == null) {
            METHOD_INFO_DATA = new MethodInfoManager();
        }
        return METHOD_INFO_DATA;
    }

    /**
     * 
     * @param methodInfo �ǉ����郁�\�b�h���
     */
    public void add(final MethodInfo methodInfo) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.methodInfos.add(methodInfo);
    }

    /**
     * ���\�b�h���� Iterator ��Ԃ��D���� Iterator �� unmodifiable �ł���C�ύX������s�����Ƃ͂ł��Ȃ��D
     */
    public Iterator<MethodInfo> iterator() {
        Set<MethodInfo> unmodifiableMethodInfos = Collections.unmodifiableSet(this.methodInfos);
        return unmodifiableMethodInfos.iterator();
    }

    /**
     * 
     * �R���X�g���N�^�D �V���O���g���p�^�[���Ŏ������Ă��邽�߂� private �����Ă���D
     */
    private MethodInfoManager() {
        this.methodInfos = new TreeSet<MethodInfo>();
    }

    /**
     * 
     * �V���O���g���p�^�[�����������邽�߂̕ϐ��D
     */
    private static MethodInfoManager METHOD_INFO_DATA = null;

    /**
     * 
     * ���\�b�h��� (methodInfo) ���i�[����ϐ��D
     */
    private final Set<MethodInfo> methodInfos;
}
