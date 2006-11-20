package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���O��Ԗ���\���N���X
 * 
 * @author y-higo
 */
public class NamespaceInfo implements Comparable<NamespaceInfo> {

    /**
     * ���O��ԃI�u�W�F�N�g������������D���O��Ԗ����^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name
     */
    public NamespaceInfo(String name) {
        this.name = name;
    }

    /**
     * ���O��Ԗ��̏������`���郁�\�b�h�D���݂͖��O��Ԃ�\�� String �N���X�� compareTo ��p���Ă���D
     */
    public int compareTo(NamespaceInfo namespaceInfo) {
        String name = this.getName();
        String correspondName = namespaceInfo.getName();
        return name.compareTo(correspondName);
    }

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public String getName() {
        return this.name;
    }

    /**
     * ���O��Ԃ�\���ϐ�
     */
    private final String name;

}
