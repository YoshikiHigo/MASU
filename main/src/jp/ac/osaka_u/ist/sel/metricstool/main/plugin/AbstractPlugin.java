package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE;


/**
 * @author kou-tngt
 * 
 * ������(2006/11/17�j ���̃R�[�h�Q�Ō^�𗘗p���邽�߁C�R���p�C���p�ɓo�^�D
 * 
 * <p>
 * ���g���N�X�v���v���O�C�������p�̒��ۃN���X
 * <p>
 * �e�v���O�C���͂��̃N���X���p�������N���X��1�����Ȃ���΂Ȃ�Ȃ��D �܂��C���̃N���X����plugin.xml�t�@�C���Ɏw��̌`���ŋL�q���Ȃ���΂Ȃ�Ȃ��D
 * <p>
 * main���W���[���͊e�v���O�C���f�B���N�g������plugin.xml�t�@�C����T�����A �����ɋL�q����Ă���C���̃N���X���p�������N���X���C���X�^���X�����A
 * �e���\�b�h��ʂ��ď����擾������Aexecute���\�b�h���Ăяo���ă��g���N�X�l���v������
 */
public abstract class AbstractPlugin {

    /**
     * �v���O�C���̏���ۑ���������s�σN���X�D AbstractPlugin����̂݃C���X�^���X���ł���D
     * <p>
     * �v���O�C���̏��𓮓I�ɕύX�����ƍ���̂ŁA���̓����N���X�̃C���X�^���X��p���� �������Ƃ��邱�ƂŃv���O�C�����̕s�ϐ�����������D
     * �e�v���O�C���̏���ۑ�����PluginInfo�C���X�^���X�̎擾�ɂ� {@link AbstractPlugin#getPluginInfo()}��p����D
     * 
     * @author kou-tngt
     * 
     */
    public class PluginInfo {

        /**
         * �f�t�H���g�̃R���X�g���N�^
         */
        private PluginInfo() {
            final LANGUAGE[] languages = AbstractPlugin.this.getMeasurableLanguages();
            this.measurableLanguages = new LANGUAGE[languages.length];
            System.arraycopy(languages, 0, this.measurableLanguages, 0, languages.length);
            this.metricsName = AbstractPlugin.this.getMetricsName();
            this.metricsType = AbstractPlugin.this.getMetricsType();
            this.useClassInfo = AbstractPlugin.this.useClassInfo();
            this.useMethodInfo = AbstractPlugin.this.useMethodInfo();
            this.useFileInfo = AbstractPlugin.this.useFileInfo();
            this.useMethodLocalInfo = AbstractPlugin.this.useMethodLocalInfo();
        }

        /**
         * ���̃v���O�C�������g���N�X���v���ł��錾���Ԃ��D
         * 
         * @return �v���\�Ȍ����S�Ċ܂ޔz��D
         */
        public LANGUAGE[] getMeasurableLanguages() {
            return measurableLanguages;
        }

        /**
         * ���̃v���O�C���������Ŏw�肳�ꂽ����ŗ��p�\�ł��邩��Ԃ��D
         * 
         * @param language ���p�\�ł��邩�𒲂ׂ�������
         * @return ���p�\�ł���ꍇ�� true�C���p�ł��Ȃ��ꍇ�� false�D
         */
        public boolean isMeasurable(LANGUAGE language) {
            LANGUAGE[] measurableLanguages = this.getMeasurableLanguages();
            for (int i = 0; i < measurableLanguages.length; i++) {
                if (language.equals(measurableLanguages[i])) {
                    return true;
                }
            }
            return false;
        }

        /**
         * ���̃v���O�C�����v�����郁�g���N�X�̖��O��Ԃ��D
         * 
         * @return ���g���N�X��
         */
        public String getMetricsName() {
            return metricsName;
        }

        /**
         * ���̃v���O�C�����v�����郁�g���N�X�̃^�C�v��Ԃ��D
         * 
         * @return ���g���N�X�^�C�v
         * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE
         */
        public METRICS_TYPE getMetricsType() {
            return metricsType;
        }

        /**
         * ���̃v���O�C�����N���X�Ɋւ�����𗘗p���邩�ǂ�����Ԃ��D
         * 
         * @return �N���X�Ɋւ�����𗘗p����ꍇ��true�D
         */
        public boolean isUseClassInfo() {
            return useClassInfo;
        }

        /**
         * ���̃v���O�C�����t�@�C���Ɋւ�����𗘗p���邩�ǂ�����Ԃ��D
         * 
         * @return �t�@�C���Ɋւ�����𗘗p����ꍇ��true�D
         */
        public boolean isUseFileInfo() {
            return useFileInfo;
        }

        /**
         * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ��D
         * 
         * @return ���\�b�h�Ɋւ�����𗘗p����ꍇ��true�D
         */
        public boolean isUseMethodInfo() {
            return useMethodInfo;
        }

        /**
         * ���̃v���O�C�������\�b�h�����Ɋւ�����𗘗p���邩�ǂ�����Ԃ��D
         * 
         * @return ���\�b�h�����Ɋւ�����𗘗p����ꍇ��true�D
         */
        public boolean isUseMethodLocalInfo() {
            return useMethodLocalInfo;
        }

        private final LANGUAGE[] measurableLanguages;

        private final String metricsName;

        private final METRICS_TYPE metricsType;

        private final boolean useClassInfo;

        private final boolean useFileInfo;

        private final boolean useMethodInfo;

        private final boolean useMethodLocalInfo;
    }

    /**
     * �v���O�C������ۑ����Ă���{@link PluginInfo}�N���X�̃C���X�^���X��Ԃ��D
     * �����AbstractPlugin�C���X�^���X�ɑ΂��邱�̃��\�b�h�͕K������̃C���X�^���X��Ԃ��C ���̓����ɕۑ�����Ă�����͕s�ςł���D
     * 
     * @return �v���O�C������ۑ����Ă���{@link PluginInfo}�N���X�̃C���X�^���X
     */
    public final PluginInfo getPluginInfo() {
        if (null == this.pluginInfo) {
            synchronized (this) {
                if (null == this.pluginInfo) {
                    this.pluginInfo = new PluginInfo();
                }
            }
        }
        return this.pluginInfo;
    }

    /**
     * ���̃v���O�C�������g���N�X���v���ł��錾���Ԃ� ���p�ł��錾��ɐ����̂���v���O�C���́A���̃��\�b�h���I�[�o�[���C�h����K�v������D
     * 
     * @return �v���\�Ȍ����S�Ċ܂ޔz��
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    protected LANGUAGE[] getMeasurableLanguages() {
        return LANGUAGE.values();
    }

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̖��O��Ԃ����ۃ��\�b�h�D
     * 
     * @return ���g���N�X��
     */
    protected abstract String getMetricsName();

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̃^�C�v��Ԃ����ۃ��\�b�h�D
     * 
     * @return ���g���N�X�^�C�v
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE
     */
    protected abstract METRICS_TYPE getMetricsType();

    /**
     * ���̃v���O�C�����N���X�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D �f�t�H���g�����ł�false��Ԃ��D
     * �N���X�Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * 
     * @return �N���X�Ɋւ�����𗘗p����ꍇ��true�D
     */
    protected boolean useClassInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�����t�@�C���Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D �f�t�H���g�����ł�false��Ԃ��D
     * �t�@�C���Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * 
     * @return �t�@�C���Ɋւ�����𗘗p����ꍇ��true�D
     */
    protected boolean useFileInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D �f�t�H���g�����ł�false��Ԃ��D
     * ���\�b�h�Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * 
     * @return ���\�b�h�Ɋւ�����𗘗p����ꍇ��true�D
     */
    protected boolean useMethodInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�������\�b�h�����Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D �f�t�H���g�����ł�false��Ԃ��D
     * ���\�b�h�����Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * 
     * @return ���\�b�h�����Ɋւ�����𗘗p����ꍇ��true�D
     */
    protected boolean useMethodLocalInfo() {
        return false;
    }

    /**
     * ���g���N�X��͂��X�^�[�g���钊�ۃ��\�b�h�D
     */
    protected abstract void execute();

    /**
     * �v���O�C���̏���ۑ�����{@link PluginInfo}�N���X�̃C���X�^���X getPluginInfo���\�b�h�̏���̌Ăяo���ɂ���č쐬����D
     * ����ȍ~�A���̃t�B�[���h�͏�ɓ����C���X�^���X���Q�Ƃ���D
     */
    private PluginInfo pluginInfo;
}
