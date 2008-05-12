package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NamespaceInfo;


/**
 * �z��^�̒�`��\�����߂̃N���X�D
 * �ł���Ύg�������͂Ȃ��D
 * 
 * @author higo
 *
 */
public final class ArrayTypeClassInfo extends ClassInfo {

    /**
     * �z��̌^��^���āC�I�u�W�F�N�g��������
     * 
     * @param arrayType �z��̌^
     */
    public ArrayTypeClassInfo(final ArrayTypeInfo arrayType) {

        super(new HashSet<ModifierInfo>(), NamespaceInfo.UNKNOWN, NONAME, 0, 0, 0, 0);

        if (null == arrayType) {
            throw new IllegalArgumentException();
        }
        this.arrayType = arrayType;
    }

    /**
     * �z��̌^��Ԃ�
     * 
     * @return �z��̌^
     */
    public ArrayTypeInfo getArrayType() {
        return this.arrayType;
    }

    private final ArrayTypeInfo arrayType;

    public static final String NONAME = new String("noname");
}
