package jp.ac.osaka_u.ist.sel.metricstool.tcc;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * TCC(Tight Class Cohesion)���v�Z���郁�g���N�X
 * <p>
 * �S�ẴI�u�W�F�N�g�w������ɑΉ�
 * 
 * @author y-higo
 * 
 */
public class TCCPlugin extends AbstractClassMetricPlugin {

    /**
     * �����ŗ^����ꂽ�N���X��TCC���v�Z����
     * 
     * @param targetClass TCC�v�Z�ΏۃN���X
     * @return �v�Z����
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {

        int couplings = 0;
        final SortedSet<TargetMethodInfo> methods = targetClass.getDefinedMethods();
        for (final TargetMethodInfo method1 : methods) {

            // method1 ���Q�Ƃ��Ă���ϐ��C������Ă���ϐ����擾
            final SortedSet<FieldInfo> method1Referencees = method1.getReferencees();
            final SortedSet<FieldInfo> method1Assignmentees = method1.getAssignmentees();

            METHOD2: for (final TargetMethodInfo method2 : methods.tailSet(method1)) {

                // metho1 �� method2 �������ꍇ�̓X�L�b�v
                if (method1.equals(method2)) {
                    continue;
                }

                // method2 ���Q�Ƃ��Ă���ϐ��C������Ă���ϐ����擾
                final SortedSet<FieldInfo> method2Referencees = method2.getReferencees();
                final SortedSet<FieldInfo> method2Assignmentees = method2.getAssignmentees();

                // method1 ���Q�Ƃ��Ă���ϐ��� method2 �����p���Ă�����D�D�D
                for (final FieldInfo field : method1Referencees) {
                    if (method2Referencees.contains(field) || method2Assignmentees.contains(field)) {
                        couplings++;
                        continue METHOD2;
                    }
                }

                // method1 ��������Ă���ϐ��� method2 �����p���Ă�����D�D�D
                for (final FieldInfo field : method1Assignmentees) {
                    if (method2Referencees.contains(field) || method2Assignmentees.contains(field)) {
                        couplings++;
                        continue METHOD2;
                    }
                }
            }
        }

        final int combinations = methods.size() * (methods.size() - 1) / 2;
        return 1 < methods.size() ? new Float((float) couplings / (float) combinations)
                : new Float(0);
    }

    /**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ�
     * 
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Measuring the TCC metric.";
    }

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̖��O��Ԃ�
     * 
     * @return TCC
     */
    @Override
    protected String getMetricName() {
        return "TCC";
    }

    /**
     * ���̃v���O�C�����t�B�[���h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useFieldInfo() {
        return true;
    }

    /**
     * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useMethodInfo() {
        return true;
    }
}