package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE;


/**
 * @author kou-tngt
 * 
 * ������(2006/11/17�j
 * ���̃R�[�h�Q�Ō^�𗘗p���邽�߁C�R���p�C���p�ɓo�^�D
 * 
 * <p>
 * ���g���N�X�v���v���O�C�������p�̒��ۃN���X<p>
 * �e�v���O�C���͂��̃N���X���p�������N���X��1�����Ȃ���΂Ȃ�Ȃ��D
 * �܂��C���̃N���X����plugin.xml�t�@�C���Ɏw��̌`���ŋL�q���Ȃ���΂Ȃ�Ȃ��D<p>
 * main���W���[���͊e�v���O�C���f�B���N�g������plugin.xml�t�@�C����T�����A
 * �����ɋL�q����Ă���C���̃N���X���p�������N���X���C���X�^���X�����A
 * �e���\�b�h��ʂ��ď����擾������Aexecute���\�b�h���Ăяo���ă��g���N�X�l���v������
 */
public abstract class AbstractPlugin {

    /**
     * ���̃v���O�C�������g���N�X���v���ł��錾���Ԃ�
     * ���p�ł��錾��ɐ����̂���v���O�C���́A���̃��\�b�h���I�[�o�[���C�h����K�v������D
     * @return �v���\�Ȍ����S�Ċ܂ޔz��
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    public LANGUAGE[] getMesuarableLanguages() {
        return LANGUAGE.values();
    }

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̖��O��Ԃ����ۃ��\�b�h�D
     * @return ���g���N�X��
     */
    public abstract String getMetricsName();

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̃^�C�v��Ԃ����ۃ��\�b�h�D
     * @return ���g���N�X�^�C�v
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE
     */
    public abstract METRICS_TYPE getMetricsType();

    /**
     * ���̃v���O�C�����N���X�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D
     * �f�t�H���g�����ł�false��Ԃ��D
     * �N���X�Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * @return �t�@�C���Ɋւ�����𗘗p����D
     */
    public boolean useClassInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�����t�@�C���Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D
     * �f�t�H���g�����ł�false��Ԃ��D
     * �t�@�C���Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * @return �t�@�C���Ɋւ�����𗘗p����D
     */
    public boolean useFileInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D
     * �f�t�H���g�����ł�false��Ԃ��D
     * �t�@�C���Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * @return �t�@�C���Ɋւ�����𗘗p����D
     */
    public boolean useMethodInfo() {
        return false;
    }

    /**
     * ���g���N�X��͂��X�^�[�g���钊�ۃ��\�b�h�D
     */
    protected abstract void execute();
}
