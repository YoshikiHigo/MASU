package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;


/**
 * �v���O�C���� ClassInfo �ɃA�N�Z�X���邽�߂ɗp����C���^�[�t�F�[�X
 * 
 * @author y-higo
 *
 */
public class DefaultClassInfoAccessor implements ClassInfoAccessor {

    /**
     * ClassInfo �̃C�e���[�^��Ԃ��D ���̃C�e���[�^�͎Q�Ɛ�p�ł���ύX�������s�����Ƃ͂ł��Ȃ��D
     * 
     * @return ClassInfo �̃C�e���[�^
     */
    public Iterator<ClassInfo> iterator() {
        ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        return classInfoManager.iterator();
    }

    /**
     * �ΏۃN���X�̐���Ԃ����\�b�h.
     * @return �ΏۃN���X�̐�
     */
    public int getClassCount() {
        return ClassInfoManager.getInstance().getClassCount();
    }

}
