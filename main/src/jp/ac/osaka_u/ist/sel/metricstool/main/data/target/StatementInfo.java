package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * @author higo
 *
 */
public interface StatementInfo extends ExecutableElementInfo {

    /**
     * ���̃e�L�X�g�\��(String�^)��Ԃ�
     * 
     * @return�@���̃e�L�X�g�\��(String�^)��Ԃ�
     */
    String getText();

    /**
     * ���𒼐ڏ��L�����Ԃ�Ԃ�
     * @return ���𒼐ڏ��L������
     */
    public LocalSpaceInfo getOwnerSpace();
}
