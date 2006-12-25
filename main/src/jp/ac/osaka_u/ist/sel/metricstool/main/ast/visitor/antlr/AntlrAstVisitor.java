package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr;


import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;

import antlr.collections.AST;


/**
 * antlr��AST��K�₷�� {@link AstVisitor}.
 * 
 * 
 * @author kou-tngt
 *
 */
public class AntlrAstVisitor implements AstVisitor<AST> {

    /**
     * ����translator�Ŏw�肳�ꂽ {@link AstTokenTranslator} �ƃf�t�H���g�� {@link AstVisitStrategy}��
     * �ݒ肷��R���X�g���N�^.
     * ���̃R���X�g���N�^���琶�����ꂽ�f�t�H���g��AstVisitStrategy�̓N���X�⃁�\�b�h�����̃m�[�h���K�₷��悤�Ƀr�W�^�[��U������.
     * 
     * @param translator�@���̃r�W�^�[���g�p����AST�m�[�h�̖|��@
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator) {
        this(translator, true, true);
    }

    /**
     * ����translator�Ŏw�肳�ꂽ {@link AstTokenTranslator} �ƃf�t�H���g�� {@link AstVisitStrategy}��
     * �ݒ肷��R���X�g���N�^.
     * 
     * �N���X�⃁�\�b�h�����̃m�[�h��K�₷�邩�ǂ���������intoClass��intoMethod�Ŏw�肷��.
     * 
     * @param translator�@���̃r�W�^�[���g�p����AST�m�[�h�̖|��@
     * @param intoClass �N���X��\��AST�̓�����K�₷�邩�ǂ������w�肷��.�K�₷��ꍇ��true.
     * @param intoMethod�@���\�b�h��\��AST�̓�����K�₷�邩�ǂ������w�肷��.�K�₷��ꍇ��true.
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator, final boolean intoClass,
            final boolean intoMethod) {
        this(translator, new AntlrAstVisitStrategy(intoClass, intoMethod));
    }

    /**
     * �����Ŏw�肳�ꂽ {@link AstTokenTranslator} �� {@link AstVisitStrategy}��
     * �ݒ肷��R���X�g���N�^.
     * 
     * @param translator�@���̃r�W�^�[���g�p����AST�m�[�h�̖|��@
     * @param strategy�@���̃r�W�^�[�̖K����U������AstVisitStrategy�C���X�^���X
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator,
            final AstVisitStrategy<AST> strategy) {
        if (null == translator){
            throw new NullPointerException("translator is null.");
        }
        if (null == strategy){
            throw new NullPointerException("starategy is null.");
        }
        
        this.visitStrategy = strategy;
        this.translator = translator;
    }

    /**
     * ���̃r�W�^�[�����s����e {@link AstVisitEvent} �̒ʒm���󂯂郊�X�i��o�^����.
     * 
     * @param listener �o�^���郊�X�i
     * @throws NullPointerException listener��null�̏ꍇ
     */
    public void addVisitListener(final AstVisitListener listener) {
        if (null == listener){
            throw new NullPointerException("listener is null.");
        }
        
        this.listeners.add(listener);
    }
    
    /**
     * ���O�� {@link #visit(T)} ���\�b�h�ɂ���ē��B�����m�[�h�̒��ɓ���.
     */
    public void enter() {
        if (null != currentEvent){
            //�C�x���g�𑗂�
            this.fireEnterEvent(this.eventStack.push(this.currentEvent));
        }
    }

    /**
     * ���݂̃m�[�h�̒�����O�ɏo��.
     */
    public void exit() {
        if (!eventStack.isEmpty()){
            //�C�x���g�𑗂�
            this.fireExitEvent(this.eventStack.pop());
        }
    }
    
    /**
     * �����ŗ^����ꂽ�m�[�h�Ɋ��ɓ��B�ς݂��ǂ�����Ԃ�.
     * 
     * @param node�@���B�ς݂��ǂ����𔻒肵�����m�[�h
     * @return�@���B�ς݂ł����true, �����łȂ����false.
     */
    public boolean isVisited(final AST node) {
        return this.visitedNode.contains(node);
    }
    
    /**
     * ���̃r�W�^�[�����s����e {@link AstVisitEvent} �̒ʒm���󂯂郊�X�i���폜����.
     * 
     * @param listener�@�폜���郊�X�i
     * @throws NullPointerException listener��null�̏ꍇ
     */
    public void removeVisitListener(final AstVisitListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * ���̃r�W�^�[�̏�Ԃ�������Ԃɖ߂�.
     * �C�x���g���X�i�͍폜����Ȃ�.
     */
    public void reset() {
        this.currentEvent = null;
        this.eventStack.clear();
        this.visitedNode.clear();
    }

    /**
     * AST�m�[�h�̃\�[�X�R�[�h��ł̈ʒu�����Ǘ����� {@link PositionManager} ���Z�b�g����.
     * 
     * @param position�@AST�m�[�h�̃\�[�X�R�[�h��ł̈ʒu�����Ǘ����� {@link PositionManager}
     */
    public void setPositionManager(final PositionManager lineColumn) {
        this.positionManager = lineColumn;
    }

    /**
     * �����ŗ^����ꂽ�m�[�h��K�₷��.
     * node��null�̏ꍇ�͉������Ȃ�.
     * 
     * @param node �K�₷��m�[�h.
     */
    public void visit(final AST node) {
        if (null == node) {
            return;
        }
        
        //�K��ς݂̃m�[�h�ɓo�^
        this.visitedNode.add(node);

        //���̃m�[�h�̃g�[�N������AstToken�ɕϊ�����
        final AstToken token = this.translator.translate(node);

        //�ʒu��񂪗��p�ł���Ȃ�擾����.
        int startLine = 0;
        int startColumn = 0;
        int endLine = 0;
        int endColumn = 0;
        if (null != this.positionManager) {
            startLine = this.positionManager.getStartLine(node);
            startColumn = this.positionManager.getStartColumn(node);
            endLine = this.positionManager.getEndLine(node);
            endColumn = this.positionManager.getEndColumn(node);
        }

        //�C�x���g���쐬���ăJ�����g�C�x���g�Ƃ��ēo�^
        final AstVisitEvent event = new AstVisitEvent(this, token, startLine, startColumn, endLine,
                endColumn);
        this.currentEvent = event;

        //�C�x���g�𑗂� 
        this.fireVisitEvent(event);

        //���̃m�[�h�֗U�����Ă��炤
        this.visitStrategy.guide(this, node, token);
    }

    /**
     * ���B�C�x���g�𔭍s����
     * @param event ���s����C�x���g
     */
    private void fireVisitEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.visited(event);
        }
    }

    /**
     * ���݂̃m�[�h�̓����ɓ���C�x���g�𔭍s����
     * @param event�@���s����C�x���g
     */
    private void fireEnterEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.entered(event);
        }
    }

    /**
     * ���݂̃m�[�h�̓�������o��C�x���g�𔭍s����
     * @param event�@���s����C�x���g
     */
    private void fireExitEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.exited(event);
        }
    }

    /**
     * ���̃r�W�^�[�̖K����U������.
     */
    private final AstVisitStrategy<AST> visitStrategy;

    /**
     * �K�₵��AST�m�[�h��AstToken�ɕϊ�����
     */
    private final AstTokenTranslator<AST> translator;

    /**
     * �K�₵��AST�m�[�h�̈ʒu�����Ǘ�����
     */
    private PositionManager positionManager;

    /**
     * ���݂̃m�[�h�Ɋ֘A����K��C�x���g
     */
    private AstVisitEvent currentEvent;

    /**
     * �C�x���g���Ǘ�����X�^�b�N
     */
    private final Stack<AstVisitEvent> eventStack = new Stack<AstVisitEvent>();

    /**
     * ���B�ς݂̃m�[�h�̃Z�b�g
     */
    private final Set<AST> visitedNode = new HashSet<AST>();

    /**
     * �C�x���g�ʒm���󂯎�郊�X�i�[�̃Z�b�g
     */
    private final Set<AstVisitListener> listeners = new LinkedHashSet<AstVisitListener>();
}
