package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * �N���X�C���\�b�h�C�t�B�[���h�Ȃǂ̏C���q��\���N���X�D���݈ȉ��́C�C���q��������
 * <ul>
 * <li>public</li>
 * <li>private</li>
 * <li>virtual(abstract)
 * <li>
 * </ul>
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class ModifierInfo implements Serializable {

    public ModifierInfo() {
    }

    /**
     * �C���q����Ԃ�
     * 
     * @return �C���q��
     */
    public String getName() {
        return this.name;
    }

    protected String name;

    @Override
    public String toString() {
        return this.name;
    }
}
