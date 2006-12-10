package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������Q�ƌ^��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedReferenceTypeInfo implements UnresolvedTypeInfo {

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
     * �I�u�W�F�N�g�̓��������`�F�b�N����
     */
    public boolean equals(final UnresolvedTypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof UnresolvedTypeInfo)) {
            return false;
        }

        String className = this.getClassName();
        String correspondClassName = ((UnresolvedReferenceTypeInfo) typeInfo).getClassName();
        return className.equals(correspondClassName);
    }

    /**
     * �������`����
     */
    public int compareTo(final UnresolvedTypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        // ��r�Ώۂ� UnresolvedReferenceTypeInfo �̏ꍇ
        // ������ PrimitiveType > UnresolvedReferenceTypeInfo
        if (typeInfo instanceof PrimitiveTypeInfo) {
            return -1;

        } else if (typeInfo instanceof UnresolvedReferenceTypeInfo) {

            String className = this.getClassName();
            String correspondClassName = ((UnresolvedReferenceTypeInfo) typeInfo).getClassName();
            return className.compareTo(correspondClassName);

        } else {
            throw new IllegalArgumentException(typeInfo.toString() + " is a wrong object!");
        }
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
