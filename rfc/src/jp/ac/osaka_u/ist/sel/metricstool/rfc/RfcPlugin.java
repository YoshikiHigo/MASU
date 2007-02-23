package jp.ac.osaka_u.ist.sel.metricstool.rfc;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LanguageUtil;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * RFC���v������v���O�C���N���X.
 * 
 * @author rniitani
 */
public class RfcPlugin extends AbstractPlugin {
    /**
     * �ڍא���������萔
     */
    private final static String DETAIL_DESCRIPTION;

    /**
     * ���g���N�X�v�����J�n����D
     */
    @Override
    protected void execute() {
        // �N���X���A�N�Z�T���擾
        final ClassInfoAccessor classAccessor = this.getClassInfoAccessor();

        // �i���񍐗p
        int measuredClassCount = 0;
        final int maxClassCount = classAccessor.getClassCount();

        //�S�N���X�ɂ���
        for (final TargetClassInfo targetClass : classAccessor) {
            // ���̐��� RFC
            final Set<MethodInfo> rfcMethods = new HashSet<MethodInfo>();

            // ���݂̃N���X�Œ�`����Ă��郁�\�b�h
            final Set<TargetMethodInfo> localMethods = targetClass.getDefinedMethods();
            rfcMethods.addAll(localMethods);

            // localMethods �ŌĂ΂�Ă��郁�\�b�h
            for (final TargetMethodInfo m : localMethods) {
                rfcMethods.addAll(m.getCallees());
            }

            try {
                this.registMetric(targetClass, rfcMethods.size());
            } catch (final MetricAlreadyRegisteredException e) {
                this.err.println(e);
            }

            //1�N���X���Ƃ�%�Ői����
            this.reportProgress(++measuredClassCount * 100 / maxClassCount);
        }
    }

    /**
     * ���̃v���O�C���̊ȈՐ�����1�s�ŕԂ�
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Measuring the RFC metric.";
    }

    /**
     * ���̃v���O�C���̏ڍא�����Ԃ�
     * @return�@�ڍא���������
     */
    @Override
    protected String getDetailDescription() {
        return DETAIL_DESCRIPTION;
    }

    /**
     * ���̃v���O�C�������g���N�X���v���ł��錾���Ԃ��D
     * 
     * �v���Ώۂ̑S����̒��ŃI�u�W�F�N�g�w������ł�����̂̔z���Ԃ��D
     * 
     * @return �I�u�W�F�N�g�w������̔z��
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    @Override
    protected LANGUAGE[] getMeasurableLanguages() {
        return LanguageUtil.getObjectOrientedLanguages();
    }

    /**
     * ���g���N�X����Ԃ��D
     * 
     * @return ���g���N�X��
     */
    @Override
    protected String getMetricName() {
        return "RFC";
    }

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̃^�C�v��Ԃ��D
     * 
     * @return ���g���N�X�^�C�v
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE
     */
    @Override
    protected METRIC_TYPE getMetricType() {
        return METRIC_TYPE.CLASS_METRIC;
    }

    /**
     * ���̃v���O�C�����N���X�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D
     * true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useClassInfo() {
        return true;
    }

    /**
     * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D
     * true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useMethodInfo() {
        return true;
    }

    /**
     * ���̃v���O�C�������\�b�h�����Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h.
     * true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useMethodLocalInfo() {
        return true;
    }

    static {
        // DETAIL_DESCRIPTION ����
        {
            StringWriter buffer = new StringWriter();
            PrintWriter writer = new PrintWriter(buffer);

            writer.println("This plugin measures the RFC (Response for a Class) metric.");
            writer.println();
            writer.println("RFC = number of local methods in a class");
            writer.println("    + number of remote methods called by local methods");
            writer.println();
            writer.println("A given remote method is counted by once.");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }

}
