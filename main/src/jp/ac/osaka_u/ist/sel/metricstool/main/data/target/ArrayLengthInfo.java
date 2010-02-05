package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;


/**
 * �z��̒�����\���ϐ� length ��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayLengthInfo extends FieldInfo {

    /**
     * ���̃I�u�W�F�N�g��(�֋X��)��`���Ă���z��I�u�W�F�N�g��^���ď�����
     * 
     * @param ownerArray �z��I�u�W�F�N�g
     */
    public ArrayLengthInfo(final ArrayTypeInfo ownerArray) {

        super(new HashSet<ModifierInfo>(), "length", PrimitiveTypeInfo.INT, new ArrayTypeClassInfo(
                ownerArray), true, true, true, true, true, 0, 0, 0, 0);
    }
}
