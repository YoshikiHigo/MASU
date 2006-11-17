package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;

/**
 * @author kou-tngt
 * 
 * ���̗�O�̓v���O�C���̍\�������L�^����XML�t�@�C���̌`�����AXML�̍\���㐳�����Ȃ��ꍇ��
 * ����̃t�H�[�}�b�g�ɏ]���Ă��Ȃ��ꍇ�C�K�v�ȏ�񂪌����Ă���ꍇ�ɓ�������D
 */
public class IllegalPluginXmlFormatException extends PluginLoadException {

    public IllegalPluginXmlFormatException() {
        super();
    }

    public IllegalPluginXmlFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPluginXmlFormatException(String message) {
        super(message);
    }

    public IllegalPluginXmlFormatException(Throwable cause) {
        super(cause);
    }

}
