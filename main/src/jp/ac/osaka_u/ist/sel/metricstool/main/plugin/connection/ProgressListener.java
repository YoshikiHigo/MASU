package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection;


import java.util.EventListener;


/**
 * �i���񍐂��󂯎�邽�߂̃C���^�t�F�[�X
 * 
 * @author kou-tngt
 *
 */
public interface ProgressListener extends EventListener {
    /**
     * �i���񍐂�����ƌĂяo����郁�\�b�h
     * 
     * @param event �i���񍐓��e��\���C�x���g
     */
    public void updataProgress(ProgressEvent event);
    
    /**
     * �i���񍐂��r�؂ꂽ���ɌĂяo����郁�\�b�h
     * @param event �i���񍐂��r�؂ꂽ���Ƃ�\���C�x���g
     */
    public void disconnected(ProgressEvent event);
}
