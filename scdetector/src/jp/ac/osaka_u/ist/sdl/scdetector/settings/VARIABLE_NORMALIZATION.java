package jp.ac.osaka_u.ist.sdl.scdetector.settings;

public enum VARIABLE_NORMALIZATION {

    /**
     * 変数をそのまま用いる 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * 変数をその型の正規化する
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     * 全ての変数を同一の字句に正規化する
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
