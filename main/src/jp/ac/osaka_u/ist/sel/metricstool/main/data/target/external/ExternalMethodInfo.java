package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;


/**
 * �O���N���X�ɒ�`����Ă��郁�\�b�h����ۑ����邽�߂̃N���X
 * 
 * @author y-higo
 */
public class ExternalMethodInfo extends MethodInfo {

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������
     * 
     * @param methodName ���\�b�h��
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     * @param constructor �R���X�g���N�^���ǂ���
     */
    public ExternalMethodInfo(final String methodName, final ClassInfo ownerClass,
            final boolean constructor) {

        super(methodName, UnknownTypeInfo.getInstance(), ownerClass, constructor);
    }

}
