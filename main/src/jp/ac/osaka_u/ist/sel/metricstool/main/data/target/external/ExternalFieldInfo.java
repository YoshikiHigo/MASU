package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;


/**
 * �O���N���X�ɒ�`����Ă���t�B�[���h�̏���ۑ����邽�߂̃N���X�D
 * 
 * @author higo
 */
public final class ExternalFieldInfo extends FieldInfo {

    /**
     * ���O�ƒ�`���Ă���N���X����^���ď������D �^�͕s���D
     * 
     * @param name �t�B�[���h��
     * @param ownerClass �t�B�[���h���`���Ă���N���X
     */
    public ExternalFieldInfo(final String name, final ClassInfo ownerClass) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(), ownerClass, 0, 0,
                0, 0);
    }

}
