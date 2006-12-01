package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class ClassInfo implements TypeInfo, Comparable<ClassInfo> {

    /**
     * ���O��Ԗ��ƃN���X������I�u�W�F�N�g�𐶐�����
     * 
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     * 
     */
    public ClassInfo(final NamespaceInfo namespace, final String className) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == namespace) || (null == className)) {
            throw new NullPointerException();
        }

        this.namespace = namespace;
        this.className = className;
    }

    /**
     * �N���X����ۑ����邽�߂̕ϐ�
     */
    private final String className;

    /**
     * ���O��Ԗ���ۑ����邽�߂̕ϐ�
     */
    private final NamespaceInfo namespace;

    /**
     * ���̃N���X�̃N���X����Ԃ�
     * 
     * @return �N���X��
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * ���̃N���X�̖��O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public NamespaceInfo getNamespace() {
        return this.namespace;
    }

    /**
     * �N���X�I�u�W�F�N�g�̏����֌W���`���郁�\�b�h�D ���݂́C���O��Ԗ�������p���Ă���D���O��Ԗ��������ꍇ�́C�N���X���iString�j�̏����ɂȂ�D
     */
    public final int compareTo(final ClassInfo classInfo) {
    
        if (null == classInfo) {
            throw new NullPointerException();
        }
    
        NamespaceInfo namespace = this.getNamespace();
        NamespaceInfo correspondNamespace = classInfo.getNamespace();
        int namespaceOrder = namespace.compareTo(correspondNamespace);
        if (namespaceOrder != 0) {
            return namespaceOrder;
        } else {
            String name = this.getClassName();
            String correspondName = classInfo.getClassName();
            return name.compareTo(correspondName);
        }
    }

    /**
     * ���̃N���X�̖��O��Ԃ��D �����̖��O�Ƃ́C���O��Ԗ� + �N���X����\���D
     */
    public final String getFullQualifiedtName() {
        NamespaceInfo namespace = this.getNamespace();
        StringBuffer buffer = new StringBuffer();
        buffer.append(namespace.getName());
        buffer.append(this.getClassName());
        return buffer.toString();
    }
    
    /**
     * ���̃N���X�̌^����Ԃ�
     */
    public final String getName(){
        return this.getFullQualifiedtName();
    }
}
