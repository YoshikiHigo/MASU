package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;

/**
 * @author kou-tngt
 * 
 * ���̗�O�́C�v���O�C���̃f�B���N�g���\�����v���O�C���̋K���ɏ]���Ă��Ȃ��ꍇ�ɓ�������D
 * ��̓I�ɂ́Cplugin.xml���v���O�C���̃f�B���N�g�������ɑ��݂��Ȃ��ꍇ�Ȃǂł���D
 */
public class IlleagalPluginDirectoryStructureException extends PluginLoadException {

    public IlleagalPluginDirectoryStructureException() {
        super();
    }

    public IlleagalPluginDirectoryStructureException(String message, Throwable cause) {
        super(message, cause);
    }

    public IlleagalPluginDirectoryStructureException(String message) {
        super(message);
    }

    public IlleagalPluginDirectoryStructureException(Throwable cause) {
        super(cause);
    }

}
