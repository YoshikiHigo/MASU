package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;


/**
 * �������G���e�B�e�B�g�p��ۑ����邽�߂̃N���X�D �������G���e�B�e�B�g�p�Ƃ́C�p�b�P�[�W����N���X���̎Q�� ��\���D
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedEntityUsage implements UnresolvedTypeInfo {

    /**
     * �������G���e�B�e�B�g�p�I�u�W�F�N�g���쐬����D
     * 
     * @param availableNamespaces ���p�\�Ȗ��O��� 
     * @param name �������G���e�B�e�B�g�p��
     */
    public UnresolvedEntityUsage(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] name) {

        this.availableNamespaces = availableNamespaces;
        this.name = name;
    }

    /**
     * �^����Ԃ�. ���̃N���X�� UnresolvedTypeInfo ���������Ă���̂ŕ֋X�㑶�݂��Ă��邾���D
     */
    public String getTypeName() {

        final String delimiter = Settings.getLanguage().getNamespaceDelimiter();
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < (this.name.length - 1); i++) {
            buffer.append(this.name[i]);
            buffer.append(delimiter);
        }
        buffer.append(this.name[this.name.length - 1]);
        return buffer.toString();
    }

    /**
     * �������G���e�B�e�B�g�p����Ԃ��D
     * 
     * @return �������G���e�B�e�B�g�p��
     */
    public String[] getName() {
        return this.name;
    }

    /**
     * ���̖������G���e�B�e�B�g�p�����p���邱�Ƃ̂ł��閼�O��Ԃ�Ԃ��D
     * 
     * @return ���̖������G���e�B�e�B�g�p�����p���邱�Ƃ̂ł��閼�O���
     */
    public AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * ���̖������G���e�B�e�B�g�p�����p���邱�Ƃ̂ł��閼�O��Ԃ�ۑ����邽�߂̕ϐ�
     */
    private final AvailableNamespaceInfoSet availableNamespaces;

    /**
     * ���̖������G���e�B�e�B�g�p����ۑ����邽�߂̕ϐ�
     */
    private final String[] name;
}
