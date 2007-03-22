package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;


/**
 * {@link Edge} �̎��.
 * 
 * @author rniitani
 */
public enum EDGE_TYPE {
    CALL {
        @Override
        public String getLabel() {
            return "call";
        }
    },
    SUPERCLASS {
        @Override
        public String getLabel() {
            return "superclass";
        }
    },
    INNERCLASS {
        @Override
        public String getLabel() {
            return "innerclass";
        }
    },
    ;
    /**
     * ���x���Ɏg�p���镶����.
     */
    public abstract String getLabel();
}