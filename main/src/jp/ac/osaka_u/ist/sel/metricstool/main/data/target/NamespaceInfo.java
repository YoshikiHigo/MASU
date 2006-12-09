package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���O��Ԗ���\���N���X
 * 
 * @author y-higo
 */
public final class NamespaceInfo implements Comparable<NamespaceInfo> {

    /**
     * ���O��ԃI�u�W�F�N�g������������D���O��Ԗ����^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name
     */
    public NamespaceInfo(final String[] name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * ���O��Ԗ��̏������`���郁�\�b�h�D���݂͖��O��Ԃ�\�� String �N���X�� compareTo ��p���Ă���D
     * 
     * @param namaspace ��r�Ώۖ��O��Ԗ�
     * @return ���O��Ԃ̏���
     */
    public int compareTo(final NamespaceInfo namespace) {

        if (null == namespace) {
            throw new NullPointerException();
        }

        String name = this.getName(".");
        String correspondName = namespace.getName(".");
        return name.compareTo(correspondName);
    }

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public String[] getName() {
        return this.name;
    }

    /**
     * �s���Ȗ��O��Ԗ���\���萔
     */
    public final static NamespaceInfo UNNKNOWN = new NamespaceInfo(new String[] { "unknown" });

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @param separator ���O�Ԃ̃Z�p���[�^
     * @return ���O��Ԃ��Ȃ��� String
     */
    public String getName(final String separator) {

        if (null == separator){
            throw new  NullPointerException();
        }
        
        String[] names = this.getName();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < names.length; i++) {
            buffer.append(names[i]);
            buffer.append(separator);
        }

        return buffer.toString();

    }

    /**
     * ���O��Ԃ�\���ϐ�
     */
    private final String[] name;

}
