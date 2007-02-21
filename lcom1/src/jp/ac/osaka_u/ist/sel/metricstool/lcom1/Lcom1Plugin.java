package jp.ac.osaka_u.ist.sel.metricstool.lcom1;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LanguageUtil;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * 
 * LCOM1(CK���g���N�X��LCOM)���v������v���O�C���N���X.
 * <p>
 * �S�ẴI�u�W�F�N�g�w������ɑΉ�.
 * �N���X�C���\�b�h�C�t�B�[���h�C���\�b�h�����̏���K�v�Ƃ���.
 * @author kou-tngt
 *
 */
public class Lcom1Plugin extends AbstractPlugin {

    /**
     * ���g���N�X�v�����J�n����D
     */
    @Override
    protected void execute() {
        final List<TargetMethodInfo> methods = new ArrayList<TargetMethodInfo>(100);
        final Set<TargetFieldInfo> instanceFields = new HashSet<TargetFieldInfo>();
        final Set<FieldInfo> usedFields = new HashSet<FieldInfo>();

        //�N���X���A�N�Z�T���擾
        final ClassInfoAccessor accessor = this.getClassInfoAccessor();

        int measuredClassCount = 0;
        final int maxClassCount = accessor.getClassCount();

        //�S�N���X�ɂ���
        for (final TargetClassInfo cl : accessor) {
            int p = 0;
            int q = 0;

            methods.addAll(cl.getDefinedMethods());
            
            //���̃N���X�̃C���X�^���X�t�B�[���h�̃Z�b�g���擾
            instanceFields.addAll(cl.getDefinedFields());
            for(Iterator<TargetFieldInfo> it = instanceFields.iterator(); it.hasNext();){
                if (it.next().isStaticMember()){
                    it.remove();
                }
            }
            
            final int methodCount = methods.size();

            //�t�B�[���h�𗘗p���郁�\�b�h��1���Ȃ����ǂ���
            boolean allMethodsDontUseAnyField = true;

            //�S���\�b�hi�΂���
            for (int i = 0; i < methodCount; i++) {
                //���\�b�hi���擾���āC���or�Q�Ƃ��Ă���t�B�[���h��S��set�ɓ����
                final TargetMethodInfo firstMethod = methods.get(i);
                usedFields.addAll(firstMethod.getAssignmentees());
                usedFields.addAll(firstMethod.getReferencees());
                
                //���N���X�̃C���X�^���X�t�B�[���h�������c��
                usedFields.retainAll(instanceFields);

                if (allMethodsDontUseAnyField) {
                    //�܂��ǂ̃��\�b�h��1���t�B�[���h�𗘗p���Ă��Ȃ��ꍇ
                    allMethodsDontUseAnyField = usedFields.isEmpty();
                }

                //i�ȍ~�̃��\�b�hj�ɂ���
                for (int j = i + 1; j < methodCount; j++) {
                    //���\�b�hj���擾���āC�Q�Ƃ��Ă���t�B�[���h���P�ł�set�ɂ��邩�ǂ����𒲂ׂ�
                    final TargetMethodInfo secondMethod = methods.get(j);
                    boolean isSharing = false;
                    for (final FieldInfo secondUsedField : secondMethod.getReferencees()) {
                        if (usedFields.contains(secondUsedField)) {
                            isSharing = true;
                            break;
                        }
                    }

                    //������Ă���t�B�[���h���P�ł�set�ɂ��邩�ǂ����𒲂ׂ�
                    if (!isSharing) {
                        for (final FieldInfo secondUsedField : secondMethod.getAssignmentees()) {
                            if (usedFields.contains(secondUsedField)) {
                                isSharing = true;
                                break;
                            }
                        }
                    }

                    //���L���Ă���t�B�[���h�������q���C�Ȃ����p�𑝂₷
                    if (isSharing) {
                        q++;
                    } else {
                        p++;
                    }
                }

                usedFields.clear();
            }

            try {
                if (p <= q || allMethodsDontUseAnyField) {
                    //p��q�ȉ��C�܂��͑S�Ẵ��\�b�h���t�B�[���h�𗘗p���Ȃ��ꍇlcom��0
                    this.registMetric(cl, 0);
                } else {
                    //�����łȂ��Ȃ�p-q��lcom
                    this.registMetric(cl, p - q);
                }
            } catch (final MetricAlreadyRegisteredException e) {
                this.err.println(e);
            }

            methods.clear();
            instanceFields.clear();
            
            //1�N���X���Ƃ�%�Ői����
            this.reportProgress(++measuredClassCount * 100 / maxClassCount);
        }
    }

    /**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ�
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Measuring the LCOM1 metric(CK-metrics's LCOM).";
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
        return "LCOM1";
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
     * �ڍא���������萔
     */
    private final static String DETAIL_DESCRIPTION;

    static {
        final String lineSeparator = "\n";//System.getProperty("line.separator");//TODO�@���̕ӂ̃Z�L�����e�B�͊ɘa����������������
        final StringBuilder builder = new StringBuilder();

        builder.append("This plugin measures the LCOM1 metric(CK-metrics's LCOM).");
        builder.append(lineSeparator);
        builder
                .append("The LCOM1 is one of the class cohesion metrics measured by following steps:");
        builder.append(lineSeparator);
        builder.append("1. P is a set of pairs of methods which do not share any field.");
        builder.append("If all methods do not use any field, P is a null set.");
        builder.append(lineSeparator);
        builder.append("2. Q is a set of pairs of methods which share some fields.");
        builder.append(lineSeparator);
        builder.append("3. If |P| > |Q|, the result is measured as |P| - |Q|, otherwise 0.");
        builder.append(lineSeparator);

        DETAIL_DESCRIPTION = builder.toString();
    }
}
