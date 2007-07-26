package jp.ac.osaka_u.ist.sel.metricstool.cbo;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * CBO(CK���g���N�X��1��)���v�Z���郁�g���N�X
 * <p>
 * �S�ẴI�u�W�F�N�g�w������ɑΉ�
 * 
 * @author y-higo
 * 
 */
public class CBOPlugin extends AbstractClassMetricPlugin {

    
    /**
     * �����ŗ^����ꂽ�N���X��CBO���v�Z����
     * 
     * @param targetClass CBO�v�Z�ΏۃN���X
     * @return �v�Z����
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {

        Set<ClassInfo> classes = new HashSet<ClassInfo>();

        // �t�B�[���h�Ŏg�p����Ă���N���X�^���擾
        for (final TargetFieldInfo field : targetClass.getDefinedFields()) {
            final TypeInfo type = field.getType();
            if (type instanceof ClassInfo) {
                classes.add((ClassInfo) type);
            }
        }

        // ���\�b�h�Ŏg�p����Ă���N���X�^���擾
        for (final TargetMethodInfo method : targetClass.getDefinedMethods()) {

            // �Ԃ�l�ɂ��Ă̏���
            {
                final TypeInfo returnType = method.getReturnType();
                if (returnType instanceof ClassInfo) {
                    classes.add((ClassInfo) returnType);
                }
            }

            // �����̂��Ă̏���
            for (final ParameterInfo parameter : method.getParameters()) {
                final TypeInfo parameterType = parameter.getType();
                if(parameterType instanceof ClassInfo){
                    classes.add((ClassInfo) parameterType);
                }
            }
            
            // ���[�J���ϐ��ɂ��Ă̏���
            for (final LocalVariableInfo variable : method.getLocalVariables()){
                final TypeInfo variableType = variable.getType();
                if(variableType instanceof ClassInfo){
                    classes.add((ClassInfo)variableType);
                }
            }
        }
        
        //�������g�͎�菜��
        classes.remove(targetClass);

        return classes.size();
    }
    

    /**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ�
     * 
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Measuring the CBO metric.";
    }

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̖��O��Ԃ�
     * 
     * @return CBO
     */
    @Override
    protected String getMetricName() {
        return "CBO";
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
