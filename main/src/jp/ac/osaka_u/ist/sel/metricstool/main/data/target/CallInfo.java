package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���\�b�h�Ăяo���C�R���X�g���N�^�Ăяo���̋��ʂ̐e�N���X
 * 
 * @author higo
 *
 */
public abstract class CallInfo extends EntityUsageInfo {

    /**
     * �ʒu����^���ăI�u�W�F�N�g��������
     */
    CallInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);
        this.parameters = new LinkedList<EntityUsageInfo>();
    }

    /**
     * ���̃��\�b�h�Ăяo���̎�������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param parameter �ǉ����������
     */
    public final void addParameter(final EntityUsageInfo parameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameter) {
            throw new NullPointerException();
        }

        this.parameters.add(parameter);
    }

    /**
     * ���̃��\�b�h�Ăяo���̎�������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param parameters �ǉ����������
     */
    public final void addParameters(final List<EntityUsageInfo> parameters) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameters) {
            throw new NullPointerException();
        }

        this.parameters.addAll(parameters);
    }

    /**
     * ���̃R���X�g���N�^�Ăяo���̎�������List��Ԃ��D
     * 
     * @return
     */
    public List<EntityUsageInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    private final List<EntityUsageInfo> parameters;
}
