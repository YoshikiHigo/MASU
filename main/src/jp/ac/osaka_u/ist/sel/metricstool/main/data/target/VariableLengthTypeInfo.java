package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashMap;
import java.util.Map;


/**
 * �ϒ��^��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class VariableLengthTypeInfo extends ArrayTypeInfo {

    /**
     * VariableLengthTypeInfo �̃C���X�^���X��Ԃ����߂̃t�@�N�g�����\�b�h�D
     * 
     * @param element �^��\���ϐ�
     * @return �������� VariableLengthTypeInfo �I�u�W�F�N�g
     */
    public static VariableLengthTypeInfo getType(final TypeInfo element) {

        if (null == element) {
            throw new IllegalArgumentException();
        }

        VariableLengthTypeInfo variableLengthType = VARIABLE_LENGTH_TYPE_MAP.get(element);
        if (variableLengthType == null) {
            variableLengthType = new VariableLengthTypeInfo(element);
            VARIABLE_LENGTH_TYPE_MAP.put(element, variableLengthType);
        }

        return variableLengthType;
    }

    VariableLengthTypeInfo(final TypeInfo element) {
        super(element, 1);
    }

    /**
     * �^����Ԃ�
     * 
     * @return �^��
     */
    @Override
    public String getTypeName() {

        final StringBuilder sb = new StringBuilder();

        final TypeInfo element = this.getElementType();
        sb.append(element.getTypeName());
        sb.append(" ...");

        return sb.toString();
    }

    /**
     * VariableLengthTypeInfo �I�u�W�F�N�g���ꌳ�Ǘ����邽�߂� Map�D�I�u�W�F�N�g�̓t�@�N�g�����\�b�h�Ő��������D
     */
    private static final Map<TypeInfo, VariableLengthTypeInfo> VARIABLE_LENGTH_TYPE_MAP = new HashMap<TypeInfo, VariableLengthTypeInfo>();
}
