package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;


/**
 * �O�����\�b�h�̈�������ۑ����邽�߂̃N���X
 * 
 * @author higo
 */
public final class ExternalParameterInfo extends ParameterInfo {

    /**
     * �����̌^���w�肵�ăI�u�W�F�N�g���������D�O����`�̃��\�b�h���Ȃ̂ň������͕s���D
     * 
     * @param type �����̌^
     */
    public ExternalParameterInfo(final TypeInfo type) {
        super(new HashSet<ModifierInfo>(), UNKNOWN_NAME, type, 0, 0, 0, 0);
    }

    /**
     * �s���Ȉ�������\���萔
     */
    public final static String UNKNOWN_NAME = "unknown";
}
