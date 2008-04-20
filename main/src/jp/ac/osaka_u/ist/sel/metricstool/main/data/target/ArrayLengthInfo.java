package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ArrayTypeClassInfo;


/**
 * �z��̒�����\���ϐ� length ��\���N���X
 * 
 * @author higo
 *
 */
public final class ArrayLengthInfo extends FieldInfo {

    /**
     * ���̃I�u�W�F�N�g��(�֋X��)��`���Ă���z��I�u�W�F�N�g��^���ď�����
     * 
     * @param ownerArray �z��I�u�W�F�N�g
     */
    public ArrayLengthInfo(final ArrayTypeInfo ownerArray) {

        super(new HashSet<ModifierInfo>(), "length", PrimitiveTypeInfo.INT, new ArrayTypeClassInfo(
                ownerArray), 0, 0, 0, 0);
    }
}
