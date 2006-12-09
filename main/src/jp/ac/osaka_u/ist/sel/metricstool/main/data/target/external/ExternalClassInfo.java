package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NamespaceInfo;


/**
 * �O���N���X����\���N���X
 * 
 * @author y-higo
 * 
 */
public class ExternalClassInfo extends ClassInfo {

    /**
     * ���O��Ԗ��ƃN���X����^���āC�I�u�W�F�N�g��������
     * 
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     */
    public ExternalClassInfo(final NamespaceInfo namespace, final String className) {

        super(namespace, className);
    }

    /**
     * ���S���薼��^���āC�N���X���I�u�W�F�N�g��������
     * 
     * @param fullQualifiedName ���S���薼
     */
    public ExternalClassInfo(final String[] fullQualifiedName) {

        super(fullQualifiedName);
    }

    /**
     * ���O��Ԃ��s���ȊO���N���X�̃I�u�W�F�N�g��������
     * 
     * @param className �N���X��
     */
    public ExternalClassInfo(final String className) {

        super(NamespaceInfo.UNNKNOWN, className);
    }
}
