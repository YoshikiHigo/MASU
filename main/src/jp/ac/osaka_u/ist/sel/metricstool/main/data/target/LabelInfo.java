package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���x����`��\���N���X
 * 
 * @author higo
 *
 */
public final class LabelInfo extends UnitInfo {

    /**
     * �ʒu����^���ă��x���I�u�W�F�N�g��������
     * 
     * @param name ���x����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public LabelInfo(final String name, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        this.name = name;

    }

    /**
     * ���̃��x���̖��O��Ԃ�
     * 
     * @return ���̃��x���̖��O
     */
    public String getName() {
        return this.name;
    }

    private final String name;
}
