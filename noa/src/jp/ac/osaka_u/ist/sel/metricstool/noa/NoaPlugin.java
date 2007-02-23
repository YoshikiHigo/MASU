package jp.ac.osaka_u.ist.sel.metricstool.noa;

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LanguageUtil;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * �N���X�̑����̐��𐔂���v���O�C���N���X.
 * 
 * {@link TargetClassInfo#getDefinedFields()} �� size
 * 
 * @author rniitani
 */
public class NoaPlugin extends AbstractPlugin {
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
            try {
                // NOA
                this.registMetric(targetClass, targetClass.getDefinedFields().size());
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
        return "Counting number of attributes.";
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
        return "NOA";
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
     * ���̃v���O�C�����t�B�[���h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D
     * true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useFieldInfo() {
        return true;
    }

    static {
        // DETAIL_DESCRIPTION ����
        {
            StringWriter buffer = new StringWriter();
            PrintWriter writer = new PrintWriter(buffer);

            writer.println("This plugin measures the NOA (number of attributes) metric.");
            writer.println();
            writer.println("NOA = number of attributes in a class");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }

}
