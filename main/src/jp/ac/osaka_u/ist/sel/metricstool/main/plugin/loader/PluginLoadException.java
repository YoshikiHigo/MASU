package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;

/**
 * @author kou-tngt
 * 
 * ���̗�O�̓v���O�C���̃��[�h�����݁C���s�������ɓ�������D
 */
public class PluginLoadException extends Exception {
    public PluginLoadException(){
        super();
    }
    
    public PluginLoadException(String message){
        super(message);
    }

    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoadException(Throwable cause) {
        super(cause);
    }
}
