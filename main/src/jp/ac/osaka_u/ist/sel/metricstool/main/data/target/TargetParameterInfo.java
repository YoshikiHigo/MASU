package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;

/**
 * �Ώۃ��\�b�h�̈�����\���N���X
 * 
 * @author y-higo
 *
 */
public final class TargetParameterInfo extends ParameterInfo {

    /**
     * �������C�����̌^��^���ăI�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�� Set
     * @param name ������
     * @param type �����̌^
     */
    public TargetParameterInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type){
        super(modifiers, name, type);
    }
}
