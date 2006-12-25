package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * AstVisitStateManager �̊�{�ƂȂ钊�ۃN���X.
 * <p>
 * ��ԕω��̃g���K�ƂȂ�g�[�N���ɓ��鎞�ɃX�^�b�N�Ɍ��݂̏�Ԃ��L�^���Ă����C
 * �g���K�ƂȂ�g�[�N������o�����ɃX�^�b�N����ߋ��̏�Ԃ����o���ď�Ԃ𕜌�����d�g�݂�񋟂���.
 * �^�p�����[�^T�ɔC�ӂ̌^�����邱�Ƃ�,��ԕ������ɓn�������̌^���w�肷�邱�Ƃ��ł���.
 * 
 * <p>
 * ���̃N���X���p������N���X�� {@link #isStateChangeTriggerToken(AstToken)},{@link #getState()},
 * {@link #setState(T)}��3�̒��ۃ��\�b�h����������K�v������.
 * <p>
 * 
 * @author kou-tngt
 *
 * @param <T> ��ԂƂ��ċL�^������̌^.
 */
public abstract class StackedAstVisitStateManager<T> implements AstVisitStateManager {
    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateManager#addStateChangeListener(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener)
     */
    public void addStateChangeListener(final StateChangeListener<AstVisitEvent> listener) {
        this.listeners.add(listener);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitListener#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitEvent)
     */
    public void entered(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (this.isStateChangeTriggerToken(token)) {
            this.pushState();
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitListener#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitEvent)
     */
    public void exited(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (this.isStateChangeTriggerToken(token)) {
            this.popState();
        }
    }

    /**
     * @return
     */
    public Set<StateChangeListener<AstVisitEvent>> getListeners() {
        return Collections.unmodifiableSet(this.listeners);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateManager#removeStateChangeListener(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener)
     */
    public void removeStateChangeListener(final StateChangeListener<AstVisitEvent> listener) {
        this.listeners.remove(listener);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitListener#visited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitEvent)
     */
    public final void visited(final AstVisitEvent event) {
        //�������Ȃ�
    }
    
    /**
     * @param type
     */
    protected final  void fireStateChangeEvent(StateChangeEventType type, AstVisitEvent triggerEvent){
        StateChangeEvent<AstVisitEvent> event = new StateChangeEvent<AstVisitEvent>(this,type,triggerEvent);
        for(StateChangeListener<AstVisitEvent> listener : getListeners()){
            listener.stateChangend(event);
        }
    }

    /**
     * ��Ԃ��X�^�b�N������o���ĕ�������.
     */
    private void popState() {
        this.setState(this.stateStack.pop());
    }

    /**
     * ���݂̏�Ԃ��X�^�b�N�ɓ����.
     */
    private void pushState() {
        this.stateStack.push(this.getState());
    }

    protected abstract T getState();

    protected abstract void setState(T state);

    protected abstract boolean isStateChangeTriggerToken(AstToken token);
    
    private final Set<StateChangeListener<AstVisitEvent>> listeners = new LinkedHashSet<StateChangeListener<AstVisitEvent>>();

    private final Stack<T> stateStack = new Stack<T>();
}
