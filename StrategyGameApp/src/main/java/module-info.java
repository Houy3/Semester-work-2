module StrategyGameApp {
    requires java.datatransfer;
    requires java.desktop;
    requires java.sql;
    requires org.apache.commons.codec;
    exports Protocol;
    exports Protocol.MessageValues.User;
    exports Protocol.exceptions;
}