package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;


/**
 * �v���O�C���� MethodInfo �ɃA�N�Z�X���邽�߂ɗp����C���^�[�t�F�[�X
 * 
 * @author y-higo
 *
 */
public class DefaultMethodInfoAccessor implements MethodInfoAccessor {

    /**
     * MethodInfo �̃C�e���[�^��Ԃ��D ���̃C�e���[�^�͎Q�Ɛ�p�ł���ύX�������s�����Ƃ͂ł��Ȃ��D
     * 
     * @return MethodInfo �̃C�e���[�^
     */
    public Iterator<MethodInfo> iterator() {
        MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();
        return methodInfoManager.iterator();
    }

    /**
     * �Ώۃ��\�b�h�̐���Ԃ����\�b�h.
     * @return �Ώۃ��\�b�h�̐�
     */
    public int getMethodCount() {
        return MethodInfoManager.getInstance().getMethodCount();
    }

}
