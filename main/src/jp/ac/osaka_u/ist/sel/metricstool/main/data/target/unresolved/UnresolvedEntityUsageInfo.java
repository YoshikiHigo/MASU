package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * �������̃N���X�Q�ƁC���\�b�h�Ăяo���C�t�B�[���h�g�p�Ȃǂ������N���X�̋��ʂ̊��N���X
 * 
 * @author y-higo
 */
public interface UnresolvedEntityUsageInfo {

    /**
     * ���O�������s��
     * 
     * @param usingClass ���O�������s���G���e�B�e�B������N���X
     * @param usingMethod ���O�������s���G���e�B�e�B�����郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * 
     * @return �����ς݂̃G���e�B�e�B
     */
    EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass, TargetMethodInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager);

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return ���O�������ꂽ���
     */
    EntityUsageInfo getResolvedEntityUsage();

    /**
     * ���ɖ��O�������ꂽ���ǂ�����Ԃ�
     * 
     * @return ���O��������Ă���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    boolean alreadyResolved();

    String getTypeName();
}
