package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * �������G���e�B�e�B���p��\���N���X
 * 
 * @author higo
 *
 */
public final class UnknownEntityUsageInfo extends ExpressionInfo {

    @Override
    public TypeInfo getType() {
        return UnknownTypeInfo.getInstance();
    }

    /**
     * �ʒu����^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnknownEntityUsageInfo(final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * ���̖������G���e�B�e�B�g�p�̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̖������G���e�B�e�B�g�p�̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {
        return UNKNOWNSTRING;
    }

    private static final String UNKNOWNSTRING = new String("UNKNOWN");
}
