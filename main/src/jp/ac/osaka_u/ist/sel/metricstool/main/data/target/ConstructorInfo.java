package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.List;
import java.util.Set;


/**
 * �R���X�g���N�^��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public abstract class ConstructorInfo extends CallableUnitInfo {

    /**
     * �K�v�ȏ���^���ď�����
     * 
     * @param modifiers ���̃R���X�g���N�^�̏C���q
     * @param privateVisible private���ǂ�����
     * @param namespaceVisible �������O��Ԃ�������ǂ���
     * @param inheritanceVisible �q�N���X��������ǂ���
     * @param publicVisible public ���ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    ConstructorInfo(final Set<ModifierInfo> modifiers, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(modifiers, privateVisible, namespaceVisible, inheritanceVisible, publicVisible,
                fromLine, fromColumn, toLine, toColumn);

    }

    /**
     * ���̃R���X�g���N�^���C�����ŗ^����ꂽ�����g���ČĂяo�����Ƃ��ł��邩�ǂ����𔻒肷��
     * 
     * @param actualParameters �����̌^�̃��X�g
     * @return �Ăяo����ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    @Override
    public final boolean canCalledWith(final List<ExpressionInfo> actualParameters) {

        if (null == actualParameters) {
            throw new IllegalArgumentException();
        }

        return super.canCalledWith(actualParameters);
    }

    @Override
    public final String getSignatureText() {

        final StringBuilder text = new StringBuilder();

        text.append(this.getOwnerClass().getClassName());

        text.append("(");
        for (final ParameterInfo parameter : this.getParameters()) {
            text.append(parameter.getType().getTypeName());
            text.append(",");
        }
        if (0 < this.getParameterNumber()) {
            text.deleteCharAt(text.length() - 1);
        }
        text.append(")");

        return text.toString();
    }
}
