package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������Q�ƌ^��\���N���X
 * 
 * @author y-higo
 * 
 */
public class UnresolvedReferenceTypeInfo implements UnresolvedTypeInfo {

    /**
     * ���O��Ԗ��C�N���X����^���ď�����
     * 
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     */
    public UnresolvedReferenceTypeInfo(final AvailableNamespaceInfoSet availableNamespaceSet,
            final String className) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaceSet) || (null == className)) {
            throw new NullPointerException();
        }

        this.availableNamespaceSet = availableNamespaceSet;
        this.className = className;
    }

    /**
     * ���̎Q�ƌ^�̃N���X����Ԃ�
     * 
     * @return ���̎Q�ƌ^�̃N���X����Ԃ�
     */
    public String getName() {
        return this.getClassName();
    }

    /**
     * ���̎Q�ƌ^�̃N���X����Ԃ�
     * 
     * @return ���̎Q�ƌ^�̃N���X��
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * ���̎Q�ƌ^�̊��S���薼�Ƃ��ĉ\���̂��閼�O��Ԗ��̈ꗗ��Ԃ�
     * 
     * @return ���̎Q�ƌ^�̊��S���薼�Ƃ��ĉ\���̂��閼�O��Ԗ��̈ꗗ
     */
    public AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaceSet;
    }

    /**
     * ���p�\�Ȗ��O��Ԗ���ۑ����邽�߂̕ϐ��C���O���������̍ۂɗp����
     */
    private final AvailableNamespaceInfoSet availableNamespaceSet;

    /**
     * �N���X����ۑ�����ϐ�
     */
    private final String className;
}
